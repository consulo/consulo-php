<?php

//function sha256 () {}

//function sha256_file () {}

/**
 * (PHP 4, PHP 5)<br/>
 * @deprecated since 5.3.0
 * Loads a PHP extension at runtime
 * @link http://php.net/manual/en/function.dl.php
 * @param string $library <p>
 * This parameter is only the filename of the
 * extension to load which also depends on your platform. For example,
 * the sockets extension (if compiled
 * as a shared module, not the default!) would be called
 * sockets.so on Unix platforms whereas it is called
 * php_sockets.dll on the Windows platform.
 * </p>
 * <p>
 * The directory where the extension is loaded from depends on your
 * platform:
 * </p>
 * <p>
 * Windows - If not explicitly set in the &php.ini;, the extension is
 * loaded from c:\php4\extensions\ by default.
 * </p>
 * <p>
 * Unix - If not explicitly set in the &php.ini;, the default extension
 * directory depends on
 * whether PHP has been built with --enable-debug
 * or not
 * @return int Returns true on success or false on failure. If the functionality of loading modules is not available
 * or has been disabled (either by setting
 * enable_dl off or by enabling &safemode;
 * in &php.ini;) an E_ERROR is emitted
 * and execution is stopped. If dl fails because the
 * specified library couldn't be loaded, in addition to false an
 * E_WARNING message is emitted.
 */
function dl ($library) {}


/**
 * The full path and filename of the file. If used inside an include,
 * the name of the included file is returned.
 * Since PHP 4.0.2, __FILE__ always contains an
 * absolute path with symlinks resolved whereas in older versions it contained relative path
 * under some circumstances.
 * @link http://php.net/manual/en/language.constants.predefined.php
 */
define ('__FILE__', '');

/**
 * The current line number of the file.
 * @link http://php.net/manual/en/language.constants.predefined.php
 */
define ('__LINE__', 0);

/**
 * The class name. (Added in PHP 4.3.0) As of PHP 5 this constant
 * returns the class name as it was declared (case-sensitive). In PHP
 * 4 its value is always lowercased.
 * @link http://php.net/manual/en/language.constants.predefined.php
 */
define ('__CLASS__', '');

/**
 * The function name. (Added in PHP 4.3.0) As of PHP 5 this constant
 * returns the function name as it was declared (case-sensitive). In
 * PHP 4 its value is always lowercased.
 * @link http://php.net/manual/en/language.constants.predefined.php
 */
define ('__FUNCTION__', '');

/**
 * The class method name. (Added in PHP 5.0.0) The method name is
 * returned as it was declared (case-sensitive).
 * @link http://php.net/manual/en/language.constants.predefined.php
 */
define ('__METHOD__', '');

/**
 * The directory of the file. If used inside an include,
 * the directory of the included file is returned. This is equivalent
 * to dirname(__FILE__). This directory name
 * does not have a trailing slash unless it is the root directory.
 * (Added in PHP 5.3.0.)
 * @link http://php.net/manual/en/language.constants.predefined.php
 */
define ('__DIR__', '');

/**
 * The name of the current namespace (case-sensitive). This constant
 * is defined in compile-time (Added in PHP 5.3.0).
 * @link http://php.net/manual/en/language.constants.predefined.php
 */
define ('__NAMESPACE__', '');


?>
