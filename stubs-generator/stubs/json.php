<?php

// Start of json v.1.2.1

/**
 * (PHP 5 &gt;= 5.2.0, PECL json &gt;= 1.2.0)<br/>
 * Returns the JSON representation of a value
 * @link http://php.net/manual/en/function.json-encode.php
 * @param mixed $value <p>
 * The value being encoded. Can be any type except
 * a resource.
 * </p>
 * <p>
 * This function only works with UTF-8 encoded data.
 * </p>
 * @param int $options [optional] <p>
 * Bitmask consisting of JSON_HEX_QUOT, JSON_HEX_TAG, JSON_HEX_AMP, JSON_HEX_APOS, JSON_NUMERIC_CHECK, JSON_PRETTY_PRINT, JSON_UNESCAPED_SLASHES, JSON_FORCE_OBJECT.
 * </p>
 * @return string a JSON encoded string on success.
 */
function json_encode ($value, $options = null) {}

/**
 * (PHP 5 &gt;= 5.2.0, PECL json &gt;= 1.2.0)<br/>
 * Decodes a JSON string
 * @link http://php.net/manual/en/function.json-decode.php
 * @param string $json <p>
 * The json string being decoded.
 * </p>
 * @param bool $assoc [optional] <p>
 * When true, returned objects will be converted into
 * associative arrays.
 * </p>
 * @param int $depth [optional] <p>
 * User specified recursion depth.
 * </p>
 * @return mixed the value encoded in json in appropriate
 * PHP type. Values true, false and
 * null (case-insensitive) are returned as true, false
 * and &null; respectively. &null; is returned if the
 * json cannot be decoded or if the encoded
 * data is deeper than the recursion limit.
 */
function json_decode ($json, $assoc = null, $depth = null) {}

/**
 * (PHP 5 &gt;= 5.3.0)<br/>
 * Returns the last error occurred
 * @link http://php.net/manual/en/function.json-last-error.php
 * @return int an integer, the value can be one of the following 
 * constants:
 */
function json_last_error () {}


/**
 * All &lt; and &gt; are converted to \u003C and \u003E.
 * @link http://php.net/manual/en/json.constants.php
 */
define ('JSON_HEX_TAG', 1);

/**
 * All &amp;s are converted to \u0026.
 * @link http://php.net/manual/en/json.constants.php
 */
define ('JSON_HEX_AMP', 2);

/**
 * All ' are converted to \u0027.
 * @link http://php.net/manual/en/json.constants.php
 */
define ('JSON_HEX_APOS', 4);

/**
 * All " are converted to \u0022.
 * @link http://php.net/manual/en/json.constants.php
 */
define ('JSON_HEX_QUOT', 8);

/**
 * Outputs an object rather than an array when a non-associative array is
 * used. Especially useful when the recipient of the output is expecting
 * an object and the array is empty.
 * @link http://php.net/manual/en/json.constants.php
 */
define ('JSON_FORCE_OBJECT', 16);

/**
 * No error has occurred.
 * @link http://php.net/manual/en/json.constants.php
 */
define ('JSON_ERROR_NONE', 0);

/**
 * Since 5.3.3
 * Encodes numeric strings as numbers.
 * @link http://php.net/manual/en/json.constants.php
 */
define ('JSON_NUMERIC_CHECK', 0);

/**
 * Since 5.4.0
 * Use whitespace in returned data to format it.
 * @link http://php.net/manual/en/json.constants.php
 */
define ('JSON_PRETTY_PRINT', 0);

/**
 * Since 5.4.0
 * Don't escape /
 * @link http://php.net/manual/en/json.constants.php
 */
define ('JSON_UNESCAPED_SLASHES', 0);

/**
 * Since 5.4.0
 * @link http://php.net/manual/en/json.constants.php
 */
define ('JSON_BIGINT_AS_STRING', 0);


/**
 * The maximum stack depth has been exceeded.
 * @link http://php.net/manual/en/json.constants.php
 */
define ('JSON_ERROR_DEPTH', 1);
define ('JSON_ERROR_STATE_MISMATCH', 2);

/**
 * Control character error, possibly incorrectly encoded.
 * @link http://php.net/manual/en/json.constants.php
 */
define ('JSON_ERROR_CTRL_CHAR', 3);

/**
 * Syntax error.
 * @link http://php.net/manual/en/json.constants.php
 */
define ('JSON_ERROR_SYNTAX', 4);

/**
 * (PHP 5.3.3)<br>
 * Malformed UTF-8 characters, possibly incorrectly encoded
 * @link http://php.net/manual/en/json.constants.php
 */
define ('JSON_ERROR_UTF8', 5);

// End of json v.1.2.1
?>
