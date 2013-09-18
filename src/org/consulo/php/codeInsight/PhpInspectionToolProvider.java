package org.consulo.php.codeInsight;

import com.intellij.codeInspection.InspectionToolProvider;
import org.consulo.php.lang.inspections.PhpDynamicAsStaticMethodCall;
import org.consulo.php.lang.inspections.PhpUndefinedMethodCall;
import org.consulo.php.lang.inspections.PhpUndefinedVariable;
import org.consulo.php.lang.inspections.classes.PhpUnimplementedMethodsInClass;

/**
 * @author VISTALL
 * @since 18.09.13.
 */
public class PhpInspectionToolProvider implements InspectionToolProvider {
	@Override
	public Class[] getInspectionClasses() {
		return new Class[]{
				PhpUndefinedVariable.class,
				PhpUndefinedMethodCall.class,
				PhpDynamicAsStaticMethodCall.class,
				PhpUnimplementedMethodsInClass.class,
		};
	}
}
