<?php

// Start of Core v.5.3.2-0.dotdeb.1

/**
 * Gets the version of the current Zend engine
 * @link http://php.net/manual/en/function.zend-version.php
 * @return string the Zend Engine version number, as a string.
 */
function zend_version () {}

/**
 * (PHP 4, PHP 5)<br/>
 * Returns the number of arguments passed to the function
 * @link http://php.net/manual/en/function.func-num-args.php
 * @return int the number of arguments passed into the current user-defined
 * function.
 */
function func_num_args () {}

/**
 * (PHP 4, PHP 5)<br/>
 * Return an item from the argument list
 * @link http://php.net/manual/en/function.func-get-arg.php
 * @param int $arg_num <p>
 * The argument offset. Function arguments are counted starting from
 * zero.
 * </p>
 * @return mixed the specified argument, or false on error.
 */
function func_get_arg ($arg_num) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Returns an array comprising a function's argument list
 * @link http://php.net/manual/en/function.func-get-args.php
 * @return array an array in which each element is a copy of the corresponding
 * member of the current user-defined function's argument list.
 */
function func_get_args () {}

/**
 * (PHP 4, PHP 5)<br/>
 * Get string length
 * @link http://php.net/manual/en/function.strlen.php
 * @param string $string <p>
 * The string being measured for length.
 * </p>
 * @return int The length of the string on success, 
 * and 0 if the string is empty.
 */
function strlen ($string) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Binary safe string comparison
 * @link http://php.net/manual/en/function.strcmp.php
 * @param string $str1 <p>
 * The first string.
 * </p>
 * @param string $str2 <p>
 * The second string.
 * </p>
 * @return int &lt; 0 if str1 is less than
 * str2; &gt; 0 if str1
 * is greater than str2, and 0 if they are
 * equal.
 */
function strcmp ($str1, $str2) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Binary safe string comparison of the first n characters
 * @link http://php.net/manual/en/function.strncmp.php
 * @param string $str1 <p>
 * The first string.
 * </p>
 * @param string $str2 <p>
 * The second string.
 * </p>
 * @param int $len <p>
 * Number of characters to use in the comparison.
 * </p>
 * @return int &lt; 0 if str1 is less than
 * str2; &gt; 0 if str1
 * is greater than str2, and 0 if they are
 * equal.
 */
function strncmp ($str1, $str2, $len) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Binary safe case-insensitive string comparison
 * @link http://php.net/manual/en/function.strcasecmp.php
 * @param string $str1 <p>
 * The first string
 * </p>
 * @param string $str2 <p>
 * The second string
 * </p>
 * @return int &lt; 0 if str1 is less than
 * str2; &gt; 0 if str1
 * is greater than str2, and 0 if they are
 * equal.
 */
function strcasecmp ($str1, $str2) {}

/**
 * (PHP 4 &gt;= 4.0.2, PHP 5)<br/>
 * Binary safe case-insensitive string comparison of the first n characters
 * @link http://php.net/manual/en/function.strncasecmp.php
 * @param string $str1 <p>
 * The first string.
 * </p>
 * @param string $str2 <p>
 * The second string.
 * </p>
 * @param int $len <p>
 * The length of strings to be used in the comparison.
 * </p>
 * @return int &lt; 0 if str1 is less than
 * str2; &gt; 0 if str1 is
 * greater than str2, and 0 if they are equal.
 */
function strncasecmp ($str1, $str2, $len) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Return the current key and value pair from an array and advance the array cursor
 * @link http://php.net/manual/en/function.each.php
 * @param array $array <p>
 * The input array.
 * </p>
 * @return array the current key and value pair from the array
 * array. This pair is returned in a four-element
 * array, with the keys 0, 1,
 * key, and value. Elements
 * 0 and key contain the key name of
 * the array element, and 1 and value
 * contain the data.
 * </p>
 * <p>
 * If the internal pointer for the array points past the end of the
 * array contents, each returns
 * false.
 */
function each (&$arrayarray ) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Sets which PHP errors are reported
 * @link http://php.net/manual/en/function.error-reporting.php
 * @param int $level [optional] <p>
 * The new error_reporting
 * level. It takes on either a bitmask, or named constants. Using named 
 * constants is strongly encouraged to ensure compatibility for future 
 * versions. As error levels are added, the range of integers increases, 
 * so older integer-based error levels will not always behave as expected.
 * </p>
 * <p>
 * The available error level constants and the actual
 * meanings of these error levels are described in the
 * predefined constants.
 * <table>
 * error_reporting level constants and bit values
 * <tr valign="top">
 * <td>value</td>
 * <td>constant</td>
 * </tr>
 * <tr valign="top">
 * <td>1</td>
 * <td>
 * E_ERROR
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>2</td>
 * <td>
 * E_WARNING
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>4</td>
 * <td>
 * E_PARSE
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>8</td>
 * <td>
 * E_NOTICE
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>16</td>
 * <td>
 * E_CORE_ERROR
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>32</td>
 * <td>
 * E_CORE_WARNING
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>64</td>
 * <td>
 * E_COMPILE_ERROR
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>128</td>
 * <td>
 * E_COMPILE_WARNING
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>256</td>
 * <td>
 * E_USER_ERROR
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>512</td>
 * <td>
 * E_USER_WARNING
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>1024</td>
 * <td>
 * E_USER_NOTICE
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>6143</td>
 * <td>
 * E_ALL
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>2048</td>
 * <td>
 * E_STRICT
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>4096</td>
 * <td>
 * E_RECOVERABLE_ERROR
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>8192</td>
 * <td>
 * E_DEPRECATED
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>16384</td>
 * <td>
 * E_USER_DEPRECATED
 * </td>
 * </tr>
 * </table>
 * </p>
 * @return int the old error_reporting
 * level.
 */
function error_reporting ($level = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Defines a named constant
 * @link http://php.net/manual/en/function.define.php
 * @param string $name <p>
 * The name of the constant.
 * </p>
 * @param mixed $value <p>
 * The value of the constant; only scalar and null values are allowed. 
 * Scalar values are integer, 
 * float, string or boolean values. It is 
 * possible to define resource constants, however it is not recommended 
 * and may cause unpredictable behavior.
 * </p>
 * @param bool $case_insensitive [optional] <p>
 * If set to true, the constant will be defined case-insensitive. 
 * The default behavior is case-sensitive; i.e. 
 * CONSTANT and Constant represent
 * different values.
 * </p>
 * <p>
 * Case-insensitive constants are stored as lower-case.
 * </p>
 * @return bool Returns true on success or false on failure.
 */
function define ($name, $value, $case_insensitive = false) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Checks whether a given named constant exists
 * @link http://php.net/manual/en/function.defined.php
 * @param string $name <p>
 * The constant name.
 * </p>
 * @return bool true if the named constant given by name
 * has been defined, false otherwise.
 */
function defined ($name) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Returns the name of the class of an object
 * @link http://php.net/manual/en/function.get-class.php
 * @param object $object [optional] <p>
 * The tested object
 * </p>
 * @return string the name of the class of which object is an
 * instance. Returns false if object is not an 
 * object.
 */
function get_class ($object = null) {}

/**
 * (PHP 5 &gt;= 5.3.0)<br/>
 * the "Late Static Binding" class name
 * @link http://php.net/manual/en/function.get-called-class.php
 * @return string the class name. Returns false if called from outside a class.
 */
function get_called_class () {}

/**
 * (PHP 4, PHP 5)<br/>
 * Retrieves the parent class name for object or class
 * @link http://php.net/manual/en/function.get-parent-class.php
 * @param mixed $object [optional] <p>
 * The tested object or class name
 * </p>
 * @return string the name of the parent class of the class of which
 * object is an instance or the name.
 * </p>
 * <p>
 * If the object does not have a parent false will be returned.
 * </p>
 * <p>
 * If called without parameter outside object, this function returns false.
 */
function get_parent_class ($object = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Checks if the class method exists
 * @link http://php.net/manual/en/function.method-exists.php
 * @param mixed $object <p>
 * An object instance or a class name
 * </p>
 * @param string $method_name <p>
 * The method name
 * </p>
 * @return bool true if the method given by method_name
 * has been defined for the given object, false 
 * otherwise.
 */
function method_exists ($object, $method_name) {}

/**
 * (PHP 5 &gt;= 5.1.0)<br/>
 * Checks if the object or class has a property
 * @link http://php.net/manual/en/function.property-exists.php
 * @param mixed $class <p>
 * The class name or an object of the class to test for
 * </p>
 * @param string $property <p>
 * The name of the property
 * </p>
 * @return bool true if the property exists, false if it doesn't exist or
 * &null; in case of an error.
 */
function property_exists ($class, $property) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Checks if the class has been defined
 * @link http://php.net/manual/en/function.class-exists.php
 * @param string $class_name <p>
 * The class name. The name is matched in a case-insensitive manner.
 * </p>
 * @param bool $autoload [optional] <p>
 * Whether or not to call &link.autoload; by default.
 * </p>
 * @return bool true if class_name is a defined class,
 * false otherwise.
 */
function class_exists ($class_name, $autoload = null) {}

/**
 * (PHP 5 &gt;= 5.0.2)<br/>
 * Checks if the interface has been defined
 * @link http://php.net/manual/en/function.interface-exists.php
 * @param string $interface_name <p>
 * The interface name
 * </p>
 * @param bool $autoload [optional] <p>
 * Whether to call &link.autoload; or not by default.
 * </p>
 * @return bool true if the interface given by 
 * interface_name has been defined, false otherwise.
 */
function interface_exists ($interface_name, $autoload = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Return &true; if the given function has been defined
 * @link http://php.net/manual/en/function.function-exists.php
 * @param string $function_name <p>
 * The function name, as a string.
 * </p>
 * @return bool true if function_name exists and is a
 * function, false otherwise.
 * </p>
 * <p>
 * This function will return false for constructs, such as 
 * include_once and echo.
 */
function function_exists ($function_name) {}

/**
 * (PHP 5 &gt;= 5.3.0)<br/>
 * Creates an alias for a class
 * @link http://php.net/manual/en/function.class-alias.php
 * @param string $original [optional] <p>
 * The original class.
 * </p>
 * @param string $alias [optional] <p>
 * The alias name for the class.
 * </p>
 * @return boolean Returns true on success or false on failure.
 */
function class_alias ($original = null, $alias = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Returns an array with the names of included or required files
 * @link http://php.net/manual/en/function.get-included-files.php
 * @return array an array of the names of all files.
 * </p>
 * <p>
 * The script originally called is considered an "included file," so it will
 * be listed together with the files referenced by 
 * include and family.
 * </p>
 * <p>
 * Files that are included or required multiple times only show up once in
 * the returned array.
 */
function get_included_files () {}

/**
 * (PHP 4, PHP 5)<br/>
 * &Alias; <function>get_included_files</function>
 * @link http://php.net/manual/en/function.get-required-files.php
 */
function get_required_files () {}

/**
 * (PHP 4, PHP 5)<br/>
 * Checks if the object has this class as one of its parents
 * @link http://php.net/manual/en/function.is-subclass-of.php
 * @param mixed $object <p>
 * A class name or an object instance
 * </p>
 * @param string $class_name <p>
 * The class name
 * </p>
 * @return bool This function returns true if the object object,
 * belongs to a class which is a subclass of 
 * class_name, false otherwise.
 */
function is_subclass_of ($object, $class_name) {}

/**
 * (PHP 4 &gt;= 4.2.0, PHP 5)<br/>
 * Checks if the object is of this class or has this class as one of its parents
 * @link http://php.net/manual/en/function.is-a.php
 * @param object $object <p>
 * The tested object
 * </p>
 * @param string $class_name <p>
 * The class name
 * </p>
 * @return bool true if the object is of this class or has this class as one of
 * its parents, false otherwise.
 */
function is_a ($object, $class_name) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Get the default properties of the class
 * @link http://php.net/manual/en/function.get-class-vars.php
 * @param string $class_name <p>
 * The class name
 * </p>
 * @return array an associative array of default public properties of the class.
 * The resulting array elements are in the form of 
 * varname => value.
 */
function get_class_vars ($class_name) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Gets the properties of the given object
 * @link http://php.net/manual/en/function.get-object-vars.php
 * @param object $object <p>
 * An object instance.
 * </p>
 * @return array an associative array of defined object accessible non-static properties 
 * for the specified object in scope. If a property have 
 * not been assigned a value, it will be returned with a &null; value.
 */
function get_object_vars ($object) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Gets the class methods' names
 * @link http://php.net/manual/en/function.get-class-methods.php
 * @param mixed $class_name <p>
 * The class name or an object instance
 * </p>
 * @return array an array of method names defined for the class specified by
 * class_name. In case of an error, it returns &null;.
 */
function get_class_methods ($class_name) {}

/**
 * (PHP 4 &gt;= 4.0.1, PHP 5)<br/>
 * Generates a user-level error/warning/notice message
 * @link http://php.net/manual/en/function.trigger-error.php
 * @param string $error_msg <p>
 * The designated error message for this error. It's limited to 1024 
 * characters in length. Any additional characters beyond 1024 will be 
 * truncated.
 * </p>
 * @param int $error_type [optional] <p>
 * The designated error type for this error. It only works with the E_USER
 * family of constants, and will default to E_USER_NOTICE.
 * </p>
 * @return bool This function returns false if wrong error_type is
 * specified, true otherwise.
 */
function trigger_error ($error_msg, $error_type = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Alias of <function>trigger_error</function>
 * @link http://php.net/manual/en/function.user-error.php
 * @param $messsage
 * @param $error_type [optional]
 */
function user_error ($messsage, $error_type) {}

/**
 * (PHP 4 &gt;= 4.0.1, PHP 5)<br/>
 * Sets a user-defined error handler function
 * @link http://php.net/manual/en/function.set-error-handler.php
 * @param callback $error_handler <p>
 * The user function needs to accept two parameters: the error code, and a
 * string describing the error. Then there are three optional parameters 
 * that may be supplied: the filename in which the error occurred, the
 * line number in which the error occurred, and the context in which the
 * error occurred (an array that points to the active symbol table at the
 * point the error occurred). The function can be shown as:
 * </p>
 * <p>
 * handler
 * interrno
 * stringerrstr
 * stringerrfile
 * interrline
 * arrayerrcontext
 * errno
 * The first parameter, errno, contains the
 * level of the error raised, as an integer.
 * @param int $error_types [optional] <p>
 * Can be used to mask the triggering of the
 * error_handler function just like the error_reporting ini setting 
 * controls which errors are shown. Without this mask set the
 * error_handler will be called for every error
 * regardless to the setting of the error_reporting setting.
 * </p>
 * @return mixed a string containing the previously defined error handler (if any). If
 * the built-in error handler is used &null; is returned. &null; is also returned
 * in case of an error such as an invalid callback. If the previous error handler
 * was a class method, this function will return an indexed array with the class
 * and the method name.
 */
function set_error_handler ($error_handler, $error_types = null) {}

/**
 * (PHP 4 &gt;= 4.0.1, PHP 5)<br/>
 * Restores the previous error handler function
 * @link http://php.net/manual/en/function.restore-error-handler.php
 * @return bool This function always returns true.
 */
function restore_error_handler () {}

/**
 * (PHP 5)<br/>
 * Sets a user-defined exception handler function
 * @link http://php.net/manual/en/function.set-exception-handler.php
 * @param callback $exception_handler <p>
 * Name of the function to be called when an uncaught exception occurs.
 * This function must be defined before calling
 * set_exception_handler. This handler function
 * needs to accept one parameter, which will be the exception object that
 * was thrown.
 * </p>
 * @return callback the name of the previously defined exception handler, or &null; on error. If
 * no previous handler was defined, &null; is also returned.
 */
function set_exception_handler ($exception_handler) {}

/**
 * (PHP 5)<br/>
 * Restores the previously defined exception handler function
 * @link http://php.net/manual/en/function.restore-exception-handler.php
 * @return bool This function always returns true.
 */
function restore_exception_handler () {}

/**
 * (PHP 4, PHP 5)<br/>
 * Returns an array with the name of the defined classes
 * @link http://php.net/manual/en/function.get-declared-classes.php
 * @return array an array of the names of the declared classes in the current
 * script.
 * </p>
 * <p>
 * Note that depending on what extensions you have compiled or
 * loaded into PHP, additional classes could be present. This means that
 * you will not be able to define your own classes using these
 * names. There is a list of predefined classes in the Predefined Classes section of
 * the appendices.
 */
function get_declared_classes () {}

/**
 * (PHP 5)<br/>
 * Returns an array of all declared interfaces
 * @link http://php.net/manual/en/function.get-declared-interfaces.php
 * @return array an array of the names of the declared interfaces in the current
 * script.
 */
function get_declared_interfaces () {}

/**
 * (PHP 4 &gt;= 4.0.4, PHP 5)<br/>
 * Returns an array of all defined functions
 * @link http://php.net/manual/en/function.get-defined-functions.php
 * @return array an multidimensional array containing a list of all defined
 * functions, both built-in (internal) and user-defined. The internal
 * functions will be accessible via $arr["internal"], and
 * the user defined ones using $arr["user"] (see example
 * below).
 */
function get_defined_functions () {}

/**
 * (PHP 4 &gt;= 4.0.4, PHP 5)<br/>
 * Returns an array of all defined variables
 * @link http://php.net/manual/en/function.get-defined-vars.php
 * @return array A multidimensional array with all the variables.
 */
function get_defined_vars () {}

/**
 * (PHP 4 &gt;= 4.0.1, PHP 5)<br/>
 * Create an anonymous (lambda-style) function
 * @link http://php.net/manual/en/function.create-function.php
 * @param string $args <p>
 * The function arguments.
 * </p>
 * @param string $code <p>
 * The function code.
 * </p>
 * @return string a unique function name as a string, or false on error.
 */
function create_function ($args, $code) {}

/**
 * (PHP 4 &gt;= 4.0.2, PHP 5)<br/>
 * Returns the resource type
 * @link http://php.net/manual/en/function.get-resource-type.php
 * @param resource $handle <p>
 * The evaluated resource handle.
 * </p>
 * @return string If the given handle is a resource, this function
 * will return a string representing its type. If the type is not identified
 * by this function, the return value will be the string 
 * Unknown.
 * </p>
 * <p>
 * This function will return false and generate an error if 
 * handle is not a resource.
 */
function get_resource_type ($handle) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Returns an array with the names of all modules compiled and loaded
 * @link http://php.net/manual/en/function.get-loaded-extensions.php
 * @param bool $zend_extensions [optional] <p>
 * Return zend_extensions or not, defaults to false (do not list
 * zend_extensions).
 * </p>
 * @return array an indexed array of all the modules names.
 */
function get_loaded_extensions ($zend_extensions = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Find out whether an extension is loaded
 * @link http://php.net/manual/en/function.extension-loaded.php
 * @param string $name <p>
 * The extension name.
 * </p>
 * <p>
 * You can see the names of various extensions by using
 * phpinfo or if you're using the
 * CGI or CLI version of
 * PHP you can use the -m switch to
 * list all available extensions:
 * </p>
 * @return bool true if the extension identified by name
 * is loaded, false otherwise.
 */
function extension_loaded ($name) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Returns an array with the names of the functions of a module
 * @link http://php.net/manual/en/function.get-extension-funcs.php
 * @param string $module_name <p>
 * The module name.
 * </p>
 * <p>
 * This parameter must be in lowercase.
 * </p>
 * @return array an array with all the functions, or false if 
 * module_name is not a valid extension.
 */
function get_extension_funcs ($module_name) {}

/**
 * (PHP 4 &gt;= 4.1.0, PHP 5)<br/>
 * Returns an associative array with the names of all the constants and their values
 * @link http://php.net/manual/en/function.get-defined-constants.php
 * @param bool $categorize [optional] <p>
 * Causing this function to return a multi-dimensional
 * array with categories in the keys of the first dimension and constants
 * and their values in the second dimension.
 * ]]>
 * &example.outputs.similar;
 * Array
 * (
 * [E_ERROR] => 1
 * [E_WARNING] => 2
 * [E_PARSE] => 4
 * [E_NOTICE] => 8
 * [E_CORE_ERROR] => 16
 * [E_CORE_WARNING] => 32
 * [E_COMPILE_ERROR] => 64
 * [E_COMPILE_WARNING] => 128
 * [E_USER_ERROR] => 256
 * [E_USER_WARNING] => 512
 * [E_USER_NOTICE] => 1024
 * [E_ALL] => 2047
 * [TRUE] => 1
 * )
 * [pcre] => Array
 * (
 * [PREG_PATTERN_ORDER] => 1
 * [PREG_SET_ORDER] => 2
 * [PREG_OFFSET_CAPTURE] => 256
 * [PREG_SPLIT_NO_EMPTY] => 1
 * [PREG_SPLIT_DELIM_CAPTURE] => 2
 * [PREG_SPLIT_OFFSET_CAPTURE] => 4
 * [PREG_GREP_INVERT] => 1
 * )
 * [user] => Array
 * (
 * [MY_CONSTANT] => 1
 * )
 * )
 * ]]>
 * </p>
 * @return array 
 */
function get_defined_constants ($categorize = null) {}

/**
 * (PHP 4 &gt;= 4.3.0, PHP 5)<br/>
 * Generates a backtrace
 * @link http://php.net/manual/en/function.debug-backtrace.php
 * @param int $provide_object [optional] <p>
 * Whether or not to populate the "object" index.
 * As of 5.3.6, this parameter is a bitmask for the following options:
 * <li>DEBUG_BACKTRACE_PROVIDE_OBJECT Whether or not to populate the "object" index.
 * <li>DEBUG_BACKTRACE_IGNORE_ARGS Whether or not to omit the "args" index, and thus all the function/method arguments, to save memory.
 * <p>Before 5.3.6, the only values recognized are TRUE or FALSE, which are the same as setting or not setting the DEBUG_BACKTRACE_PROVIDE_OBJECT option respectively.
 * @param int $limit [optional] As of 5.4.0, this parameter can be used to limit the number of stack frames returned. By default (limit=0) it returns all stack frames.
 * @return array an associative array. The possible returned elements
 * are as follows:
 * </p>
 * <p>
 * <table>
 * Possible returned elements from debug_backtrace
 * <tr valign="top">
 * <td>&Name;</td>
 * <td>&Type;</td>
 * <td>&Description;</td>
 * </tr>
 * <tr valign="top">
 * <td>function</td>
 * <td>string</td>
 * <td>
 * The current function name. See also
 * __FUNCTION__.
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>line</td>
 * <td>integer</td>
 * <td>
 * The current line number. See also
 * __LINE__.
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>file</td>
 * <td>string</td>
 * <td>
 * The current file name. See also
 * __FILE__.
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>class</td>
 * <td>string</td>
 * <td>
 * The current class name. See also
 * __CLASS__
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>object</td>
 * <td>object</td>
 * <td>
 * The current object.
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>type</td>
 * <td>string</td>
 * <td>
 * The current call type. If a method call, "->" is returned. If a static
 * method call, "::" is returned. If a function call, nothing is returned.
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>args</td>
 * <td>array</td>
 * <td>
 * If inside a function, this lists the functions arguments. If
 * inside an included file, this lists the included file name(s).
 * </td>
 * </tr>
 * </table>
 */
function debug_backtrace ($provide_object = DEBUG_BACKTRACE_PROVIDE_OBJECT, $limit = 0) {}

const DEBUG_BACKTRACE_PROVIDE_OBJECT = 0;
const DEBUG_BACKTRACE_IGNORE_ARGS = 0;

/**
 * (PHP 5)<br/>
 * Prints a backtrace
 * @link http://php.net/manual/en/function.debug-print-backtrace.php
 * @return void 
 */
function debug_print_backtrace () {}

/**
 * (PHP 5 &gt;= 5.3.0)<br/>
 * Forces collection of any existing garbage cycles
 * @link http://php.net/manual/en/function.gc-collect-cycles.php
 * @return int number of collected cycles.
 */
function gc_collect_cycles () {}

/**
 * (PHP 5 &gt;= 5.3.0)<br/>
 * Returns status of the circular reference collector
 * @link http://php.net/manual/en/function.gc-enabled.php
 * @return bool true if the garbage collector is enabled, false otherwise.
 */
function gc_enabled () {}

/**
 * (PHP 5 &gt;= 5.3.0)<br/>
 * Activates the circular reference collector
 * @link http://php.net/manual/en/function.gc-enable.php
 * @return void 
 */
function gc_enable () {}

/**
 * (PHP 5 &gt;= 5.3.0)<br/>
 * Deactivates the circular reference collector
 * @link http://php.net/manual/en/function.gc-disable.php
 * @return void 
 */
function gc_disable () {}


// End of Core v.5.3.2-0.dotdeb.1
?>
