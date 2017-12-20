<?php
$outDir = "../gen";
if (!file_exists($outDir)) {
	$result = mkdir($outDir, 0755, true);
}

$loaded_extensions = get_loaded_extensions();

foreach ($loaded_extensions as $extensionName) {
	handleExtension($extensionName);
}

function handleExtension($extensionName)
{
	global $outDir;

	$methodModifiers = array(
		ReflectionMethod::IS_ABSTRACT  => 'abstract',
		ReflectionMethod::IS_FINAL     => 'final',
		ReflectionMethod::IS_PRIVATE   => 'private',
		ReflectionMethod::IS_PROTECTED => 'protected',
		ReflectionMethod::IS_PUBLIC    => 'public',
		ReflectionMethod::IS_STATIC    => 'static',
	);


	$out = fopen("$outDir/$extensionName.php", 'w');
	fwrite($out, "<?php\n");
	$reflectionExtension = new ReflectionExtension($extensionName);

	//constants
	$constants = $reflectionExtension->getConstants();
	foreach ($constants as $constantName => $constantValue) {
		if (is_string($constantValue)) {
			$constantValue = "'$constantValue'";
		}
		fwrite($out, "const $constantName = $constantValue;\n");
	}

	//functions
	$defined_functions = $reflectionExtension->getFunctions();
	foreach ($defined_functions as $definedFunction) {
		/** @var $definedFunction ReflectionFunction */
		$definedFunctionName = $definedFunction->getName();
		fwrite($out, "function $definedFunctionName(");
		$parameters = $definedFunction->getParameters();
		$isFirstParameter = true;
		foreach ($parameters as $parameter) {
			if ($isFirstParameter) {
				$isFirstParameter = false;
			} else {
				fwrite($out, ", ");
			}
			/** @var $parameter ReflectionParameter */
			if ($parameter->isPassedByReference()) {
				fwrite($out, "&");
			}
			$parameterName = $parameter->getName();
			fwrite($out, "$$parameterName");
			if ($parameter->isDefaultValueAvailable()) {
				fwrite($out, " = ");
				print_r($parameter->getDefaultValue());
			}
		}
		fwrite($out, "){ }\n");
	}

	//classes
	$classes = $reflectionExtension->getClasses();
	foreach ($classes as $class) {
		/** @var $class ReflectionClass */
		$className = $class->getName();

		if ($class->getNamespaceName()) fwrite($out, "namespace {$class->getNamespaceName()};\n");

		$modifiers = $class->isAbstract() ? 'abstract ' : '';
		fwrite($out, "{$modifiers}class {$class->getShortName()} {\n");

		// methods
		foreach ($class->getMethods() as /** @var ReflectionMethod $method */ $method) {
			$modifierFlags = $method->getModifiers();
			$modifiers = array();
			foreach ($methodModifiers as $flag => $code) {
				if ($modifierFlags & $flag) $modifiers[] = $code;
			}

			// args
			$args = array();
			foreach ($method->getParameters() as $parameter) {
				try {
					$args[] =
						($parameter->getClass() ? '\'' . $parameter->getClass()->getName() . ' ' : '') .
						'$' . $parameter->getName() .
						($parameter->isDefaultValueAvailable() ? ' = ' . var_export($parameter->getDefaultValue(), true) : '');
				} catch(Exception $e) {
					$args[] = '$' . $parameter->getName();
				}
			}
			$args = implode(', ', $args);

			fwrite($out, "\t" . implode(' ', $modifiers) . " function {$method->getName()}($args) {}\n");
		}

		fwrite($out, "}\n");
	}
	fclose($out);
}
