<?php


/**
 * (PHP 4, PHP 5)<br/>
 * Generate a system log message
 * @link http://php.net/manual/en/function.syslog.php
 * @param int $priority <p>
 * priority is a combination of the facility and
 * the level. Possible values are:
 * <table>
 * syslog Priorities (in descending order)
 * <tr valign="top">
 * <td>Constant</td>
 * <td>Description</td>
 * </tr>
 * <tr valign="top">
 * <td>LOG_EMERG</td>
 * <td>system is unusable</td>
 * </tr>
 * <tr valign="top">
 * <td>LOG_ALERT</td>
 * <td>action must be taken immediately</td>
 * </tr>
 * <tr valign="top">
 * <td>LOG_CRIT</td>
 * <td>critical conditions</td>
 * </tr>
 * <tr valign="top">
 * <td>LOG_ERR</td>
 * <td>error conditions</td>
 * </tr>
 * <tr valign="top">
 * <td>LOG_WARNING</td>
 * <td>warning conditions</td>
 * </tr>
 * <tr valign="top">
 * <td>LOG_NOTICE</td>
 * <td>normal, but significant, condition</td>
 * </tr>
 * <tr valign="top">
 * <td>LOG_INFO</td>
 * <td>informational message</td>
 * </tr>
 * <tr valign="top">
 * <td>LOG_DEBUG</td>
 * <td>debug-level message</td>
 * </tr>
 * </table>
 * </p>
 * @param string $message <p>
 * The message to send, except that the two characters
 * %m will be replaced by the error message string
 * (strerror) corresponding to the present value of
 * errno.
 * </p>
 * @return bool Returns true on success or false on failure.
 */
function syslog ($priority, $message) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Close connection to system logger
 * @link http://php.net/manual/en/function.closelog.php
 * @return bool Returns true on success or false on failure.
 */
function closelog () {}

/**
 * (PHP 4, PHP 5)<br/>
 * Initializes all syslog related variables
 * @link http://php.net/manual/en/function.define-syslog-variables.php
 * @deprecated since 5.3.0
 * @return void
 */
function define_syslog_variables () {}

/**
 * (PHP 4, PHP 5)<br/>
 * Combined linear congruential generator
 * @link http://php.net/manual/en/function.lcg-value.php
 * @return float A pseudo random float value in the range of (0, 1)
 */
function lcg_value () {}

/**
 * (PHP 4, PHP 5)<br/>
 * Calculate the metaphone key of a string
 * @link http://php.net/manual/en/function.metaphone.php
 * @param string $str <p>
 * The input string.
 * </p>
 * @param int $phones [optional] <p>
 * </p>
 * @return string the metaphone key as a string.
 */
function metaphone ($str, $phones = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Turn on output buffering
 * @link http://php.net/manual/en/function.ob-start.php
 * @param callback $output_callback [optional] <p>
 * An optional output_callback function may be
 * specified. This function takes a string as a parameter and should
 * return a string. The function will be called when
 * the output buffer is flushed (sent) or cleaned (with
 * ob_flush, ob_clean or similar
 * function) or when the output buffer
 * is flushed to the browser at the end of the request. When
 * output_callback is called, it will receive the
 * contents of the output buffer as its parameter and is expected to
 * return a new output buffer as a result, which will be sent to the
 * browser. If the output_callback is not a
 * callable function, this function will return false.
 * </p>
 * <p>
 * If the callback function has two parameters, the second parameter is
 * filled with a bit-field consisting of
 * PHP_OUTPUT_HANDLER_START,
 * PHP_OUTPUT_HANDLER_CONT and
 * PHP_OUTPUT_HANDLER_END.
 * </p>
 * <p>
 * If output_callback returns false original
 * input is sent to the browser.
 * </p>
 * <p>
 * The output_callback parameter may be bypassed
 * by passing a &null; value.
 * </p>
 * <p>
 * ob_end_clean, ob_end_flush,
 * ob_clean, ob_flush and
 * ob_start may not be called from a callback
 * function. If you call them from callback function, the behavior is
 * undefined. If you would like to delete the contents of a buffer,
 * return "" (a null string) from callback function.
 * You can't even call functions using the output buffering functions like
 * print_r($expression, true) or
 * highlight_file($filename, true) from a callback
 * function.
 * </p>
 * <p>
 * In PHP 4.0.4, ob_gzhandler was introduced to
 * facilitate sending gz-encoded data to web browsers that support
 * compressed web pages. ob_gzhandler determines
 * what type of content encoding the browser will accept and will return
 * its output accordingly.
 * </p>
 * @param int $chunk_size [optional] <p>
 * If the optional parameter chunk_size is passed, the
 * buffer will be flushed after any output call which causes the buffer's
 * length to equal or exceed chunk_size.
 * Default value 0 means that the function is called only in the end,
 * other special value 1 sets chunk_size to 4096.
 * </p>
 * @param bool $erase [optional] <p>
 * If the optional parameter erase is set to false,
 * the buffer will not be deleted until the script finishes.
 * This causes that flushing and cleaning functions would issue a notice
 * and return false if called.
 * </p>
 * @return bool Returns true on success or false on failure.
 */
function ob_start ($output_callback = null, $chunk_size = null, $erase = null) {}

/**
 * (PHP 4 &gt;= 4.2.0, PHP 5)<br/>
 * Flush (send) the output buffer
 * @link http://php.net/manual/en/function.ob-flush.php
 * @return void 
 */
function ob_flush () {}

/**
 * (PHP 4 &gt;= 4.2.0, PHP 5)<br/>
 * Clean (erase) the output buffer
 * @link http://php.net/manual/en/function.ob-clean.php
 * @return void 
 */
function ob_clean () {}

/**
 * (PHP 4, PHP 5)<br/>
 * Flush (send) the output buffer and turn off output buffering
 * @link http://php.net/manual/en/function.ob-end-flush.php
 * @return bool Returns true on success or false on failure. Reasons for failure are first that you called the
 * function without an active buffer or that for some reason a buffer could
 * not be deleted (possible for special buffer).
 */
function ob_end_flush () {}

/**
 * (PHP 4, PHP 5)<br/>
 * Clean (erase) the output buffer and turn off output buffering
 * @link http://php.net/manual/en/function.ob-end-clean.php
 * @return bool Returns true on success or false on failure. Reasons for failure are first that you called the
 * function without an active buffer or that for some reason a buffer could
 * not be deleted (possible for special buffer).
 */
function ob_end_clean () {}

/**
 * (PHP 4 &gt;= 4.3.0, PHP 5)<br/>
 * Flush the output buffer, return it as a string and turn off output buffering
 * @link http://php.net/manual/en/function.ob-get-flush.php
 * @return string the output buffer or false if no buffering is active.
 */
function ob_get_flush () {}

/**
 * (PHP 4 &gt;= 4.3.0, PHP 5)<br/>
 * Get current buffer contents and delete current output buffer
 * @link http://php.net/manual/en/function.ob-get-clean.php
 * @return string the contents of the output buffer and end output buffering.
 * If output buffering isn't active then false is returned.
 */
function ob_get_clean () {}

/**
 * (PHP 4 &gt;= 4.0.2, PHP 5)<br/>
 * Return the length of the output buffer
 * @link http://php.net/manual/en/function.ob-get-length.php
 * @return int the length of the output buffer contents or false if no
 * buffering is active.
 */
function ob_get_length () {}

/**
 * (PHP 4 &gt;= 4.2.0, PHP 5)<br/>
 * Return the nesting level of the output buffering mechanism
 * @link http://php.net/manual/en/function.ob-get-level.php
 * @return int the level of nested output buffering handlers or zero if output
 * buffering is not active.
 */
function ob_get_level () {}

/**
 * (PHP 4 &gt;= 4.2.0, PHP 5)<br/>
 * Get status of output buffers
 * @link http://php.net/manual/en/function.ob-get-status.php
 * @param bool $full_status [optional] <p>
 * true to return all active output buffer levels. If false or not
 * set, only the top level output buffer is returned.
 * </p>
 * @return array If called without the full_status parameter
 * or with full_status = false a simple array
 * with the following elements is returned:
 * 2
 * [type] => 0
 * [status] => 0
 * [name] => URL-Rewriter
 * [del] => 1
 * )
 * ]]>
 * Simple ob_get_status results
 * KeyValue
 * levelOutput nesting level
 * typePHP_OUTPUT_HANDLER_INTERNAL (0) or PHP_OUTPUT_HANDLER_USER (1)
 * statusOne of PHP_OUTPUT_HANDLER_START (0), PHP_OUTPUT_HANDLER_CONT (1) or PHP_OUTPUT_HANDLER_END (2)
 * nameName of active output handler or ' default output handler' if none is set
 * delErase-flag as set by ob_start
 * </p>
 * <p>
 * If called with full_status = true an array
 * with one element for each active output buffer level is returned.
 * The output level is used as key of the top level array and each array
 * element itself is another array holding status information
 * on one active output level.
 * Array
 * (
 * [chunk_size] => 0
 * [size] => 40960
 * [block_size] => 10240
 * [type] => 1
 * [status] => 0
 * [name] => default output handler
 * [del] => 1
 * )
 * [1] => Array
 * (
 * [chunk_size] => 0
 * [size] => 40960
 * [block_size] => 10240
 * [type] => 0
 * [buffer_size] => 0
 * [status] => 0
 * [name] => URL-Rewriter
 * [del] => 1
 * )
 * )
 * ]]>
 * </p>
 * <p>
 * The full output contains these additional elements:
 * Full ob_get_status results
 * KeyValue
 * chunk_sizeChunk size as set by ob_start
 * size...
 * blocksize...
 */
function ob_get_status ($full_status = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Return the contents of the output buffer
 * @link http://php.net/manual/en/function.ob-get-contents.php
 * @return string This will return the contents of the output buffer or false, if output
 * buffering isn't active.
 */
function ob_get_contents () {}

/**
 * (PHP 4, PHP 5)<br/>
 * Turn implicit flush on/off
 * @link http://php.net/manual/en/function.ob-implicit-flush.php
 * @param int $flag [optional] <p>
 * true to turn implicit flushing on, false otherwise.
 * </p>
 * @return void 
 */
function ob_implicit_flush ($flag = null) {}

/**
 * (PHP 4 &gt;= 4.3.0, PHP 5)<br/>
 * List all output handlers in use
 * @link http://php.net/manual/en/function.ob-list-handlers.php
 * @return array This will return an array with the output handlers in use (if any). If
 * output_buffering is enabled or
 * an anonymous function was used with ob_start,
 * ob_list_handlers will return "default output
 * handler".
 */
function ob_list_handlers () {}

/**
 * (PHP 4, PHP 5)<br/>
 * Sort an array by key
 * @link http://php.net/manual/en/function.ksort.php
 * @param array $array <p>
 * The input array.
 * </p>
 * @param int $sort_flags [optional] <p>
 * You may modify the behavior of the sort using the optional
 * parameter sort_flags, for details
 * see sort.
 * </p>
 * @return bool Returns true on success or false on failure.
 */
function ksort (array &$array, $sort_flags = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Sort an array by key in reverse order
 * @link http://php.net/manual/en/function.krsort.php
 * @param array $array <p>
 * The input array.
 * </p>
 * @param int $sort_flags [optional] <p>
 * You may modify the behavior of the sort using the optional parameter
 * sort_flags, for details see
 * sort.
 * </p>
 * @return bool Returns true on success or false on failure.
 */
function krsort (array &$array, $sort_flags = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Sort an array using a "natural order" algorithm
 * @link http://php.net/manual/en/function.natsort.php
 * @param array $array <p>
 * The input array.
 * </p>
 * @return bool Returns true on success or false on failure.
 */
function natsort (array &$array) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Sort an array using a case insensitive "natural order" algorithm
 * @link http://php.net/manual/en/function.natcasesort.php
 * @param array $array <p>
 * The input array.
 * </p>
 * @return bool Returns true on success or false on failure.
 */
function natcasesort (array &$array) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Sort an array and maintain index association
 * @link http://php.net/manual/en/function.asort.php
 * @param array $array <p>
 * The input array.
 * </p>
 * @param int $sort_flags [optional] <p>
 * You may modify the behavior of the sort using the optional
 * parameter sort_flags, for details
 * see sort.
 * </p>
 * @return bool Returns true on success or false on failure.
 */
function asort (array &$array, $sort_flags = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Sort an array in reverse order and maintain index association
 * @link http://php.net/manual/en/function.arsort.php
 * @param array $array <p>
 * The input array.
 * </p>
 * @param int $sort_flags [optional] <p>
 * You may modify the behavior of the sort using the optional parameter
 * sort_flags, for details see
 * sort.
 * </p>
 * @return bool Returns true on success or false on failure.
 */
function arsort (array &$array, $sort_flags = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Sort an array
 * @link http://php.net/manual/en/function.sort.php
 * @param array $array <p>
 * The input array.
 * </p>
 * @param int $sort_flags [optional] <p>
 * The optional second parameter sort_flags
 * may be used to modify the sorting behavior using these values:
 * </p>
 * <p>
 * Sorting type flags:
 * SORT_REGULAR - compare items normally
 * (don't change types)
 * @return bool Returns true on success or false on failure.
 */
function sort (array &$array, $sort_flags = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Sort an array in reverse order
 * @link http://php.net/manual/en/function.rsort.php
 * @param array $array <p>
 * The input array.
 * </p>
 * @param int $sort_flags [optional] <p>
 * You may modify the behavior of the sort using the optional
 * parameter sort_flags, for details see
 * sort.
 * </p>
 * @return bool Returns true on success or false on failure.
 */
function rsort (array &$array, $sort_flags = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Sort an array by values using a user-defined comparison function
 * @link http://php.net/manual/en/function.usort.php
 * @param array $array <p>
 * The input array.
 * </p>
 * @param callback $cmp_function <p>
 * The comparison function must return an integer less than, equal to, or
 * greater than zero if the first argument is considered to be
 * respectively less than, equal to, or greater than the second.
 * </p>
 * @return bool Returns true on success or false on failure.
 */
function usort (array &$array, $cmp_function) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Sort an array with a user-defined comparison function and maintain index association
 * @link http://php.net/manual/en/function.uasort.php
 * @param array $array <p>
 * The input array.
 * </p>
 * @param callback $cmp_function <p>
 * See usort and uksort for
 * examples of user-defined comparison functions.
 * </p>
 * @return bool Returns true on success or false on failure.
 */
function uasort (array &$array, $cmp_function) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Sort an array by keys using a user-defined comparison function
 * @link http://php.net/manual/en/function.uksort.php
 * @param array $array <p>
 * The input array.
 * </p>
 * @param callback $cmp_function <p>
 * The callback comparison function.
 * </p>
 * <p>
 * Function cmp_function should accept two
 * parameters which will be filled by pairs of array keys.
 * The comparison function must return an integer less than, equal
 * to, or greater than zero if the first argument is considered to
 * be respectively less than, equal to, or greater than the
 * second.
 * </p>
 * @return bool Returns true on success or false on failure.
 */
function uksort (array &$array, $cmp_function) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Shuffle an array
 * @link http://php.net/manual/en/function.shuffle.php
 * @param array $array <p>
 * The array.
 * </p>
 * @return bool Returns true on success or false on failure.
 */
function shuffle (array &$array) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Apply a user function to every member of an array
 * @link http://php.net/manual/en/function.array-walk.php
 * @param array $array <p>
 * The input array.
 * </p>
 * @param callback $funcname <p>
 * Typically, funcname takes on two parameters.
 * The array parameter's value being the first, and
 * the key/index second.
 * </p>
 * <p>
 * If funcname needs to be working with the
 * actual values of the array, specify the first parameter of
 * funcname as a
 * reference. Then,
 * any changes made to those elements will be made in the
 * original array itself.
 * </p>
 * <p>
 * Users may not change the array itself from the
 * callback function. e.g. Add/delete elements, unset elements, etc. If
 * the array that array_walk is applied to is
 * changed, the behavior of this function is undefined, and unpredictable.
 * </p>
 * @param mixed $userdata [optional] <p>
 * If the optional userdata parameter is supplied,
 * it will be passed as the third parameter to the callback
 * funcname.
 * </p>
 * @return bool Returns true on success or false on failure.
 */
function array_walk (array &$array, $funcname, $userdata = null) {}

/**
 * (PHP 5)<br/>
 * Apply a user function recursively to every member of an array
 * @link http://php.net/manual/en/function.array-walk-recursive.php
 * @param array $input <p>
 * The input array.
 * </p>
 * @param callback $funcname <p>
 * Typically, funcname takes on two parameters.
 * The input parameter's value being the first, and
 * the key/index second.
 * </p>
 * <p>
 * If funcname needs to be working with the
 * actual values of the array, specify the first parameter of
 * funcname as a
 * reference. Then,
 * any changes made to those elements will be made in the
 * original array itself.
 * </p>
 * @param mixed $userdata [optional] <p>
 * If the optional userdata parameter is supplied,
 * it will be passed as the third parameter to the callback
 * funcname.
 * </p>
 * @return bool Returns true on success or false on failure.
 */
function array_walk_recursive (array &$input, $funcname, $userdata = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Count all elements in an array, or properties in an object
 * @link http://php.net/manual/en/function.count.php
 * @param mixed $var <p>
 * The array.
 * </p>
 * @param int $mode [optional] <p>
 * If the optional mode parameter is set to
 * COUNT_RECURSIVE (or 1), count
 * will recursively count the array. This is particularly useful for
 * counting all the elements of a multidimensional array.
 * count does not detect infinite recursion.
 * </p>
 * @return int the number of elements in var, which is
 * typically an array, since anything else will have one
 * element.
 * </p>
 * <p>
 * If var is not an array or an object with
 * implemented Countable interface,
 * 1 will be returned.
 * There is one exception, if var is &null;,
 * 0 will be returned.
 * </p>
 * <p>
 * count may return 0 for a variable that isn't set,
 * but it may also return 0 for a variable that has been initialized with an
 * empty array. Use isset to test if a variable is set.
 */
function count ($var, $mode = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Set the internal pointer of an array to its last element
 * @link http://php.net/manual/en/function.end.php
 * @param array $array <p>
 * The array. This array is passed by reference because it is modified by
 * the function. This means you must pass it a real variable and not
 * a function returning an array because only actual variables may be
 * passed by reference.
 * </p>
 * @return mixed the value of the last element or false for empty array.
 */
function end (array &$array) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Rewind the internal array pointer
 * @link http://php.net/manual/en/function.prev.php
 * @param array $array <p>
 * The input array.
 * </p>
 * @return mixed the array value in the previous place that's pointed to by
 * the internal array pointer, or false if there are no more
 * elements.
 */
function prev (array &$array) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Advance the internal array pointer of an array
 * @link http://php.net/manual/en/function.next.php
 * @param array $array <p>
 * The array being affected.
 * </p>
 * @return mixed the array value in the next place that's pointed to by the
 * internal array pointer, or false if there are no more elements.
 */
function next (array &$array) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Set the internal pointer of an array to its first element
 * @link http://php.net/manual/en/function.reset.php
 * @param array $array <p>
 * The input array.
 * </p>
 * @return mixed the value of the first array element, or false if the array is
 * empty.
 */
function reset (array &$array) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Return the current element in an array
 * @link http://php.net/manual/en/function.current.php
 * @param array $array <p>
 * The array.
 * </p>
 * @return mixed The current function simply returns the
 * value of the array element that's currently being pointed to by the
 * internal pointer. It does not move the pointer in any way. If the
 * internal pointer points beyond the end of the elements list or the array is 
 * empty, current returns false.
 */
function current (array &$array) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Fetch a key from an array
 * @link http://php.net/manual/en/function.key.php
 * @param array $array <p>
 * The array.
 * </p>
 * @return mixed The key function simply returns the
 * key of the array element that's currently being pointed to by the
 * internal pointer. It does not move the pointer in any way. If the
 * internal pointer points beyond the end of the elements list or the array is 
 * empty, key returns &null;.
 */
function key (array &$array) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Find lowest value
 * @link http://php.net/manual/en/function.min.php
 * @param array|mixed $value1 Array to look through or first value to compare
 * @param mixed $value2 [optional] second value to compare
 * </p>
 * @return mixed min returns the numerically lowest of the
 * parameter values.
 */
function min (array $value1, $value2 = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Find highest value
 * @link http://php.net/manual/en/function.max.php
 * @param array|mixed $value1 Array to look through or first value to compare
 * @param mixed $value2 [optional] second value to compare
 * </p>
 * @return mixed max returns the numerically highest of the
 * parameter values, either within a arg array or two arguments.
 */
function max (array $value1, $value2 = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Checks if a value exists in an array
 * @link http://php.net/manual/en/function.in-array.php
 * @param mixed $needle <p>
 * The searched value.
 * </p>
 * <p>
 * If needle is a string, the comparison is done
 * in a case-sensitive manner.
 * </p>
 * @param array $haystack <p>
 * The array.
 * </p>
 * @param bool $strict [optional] <p>
 * If the third parameter strict is set to true
 * then the in_array function will also check the
 * types of the
 * needle in the haystack.
 * </p>
 * @return bool true if needle is found in the array,
 * false otherwise.
 */
function in_array ($needle, array $haystack, $strict = null) {}

/**
 * (PHP 4 &gt;= 4.0.5, PHP 5)<br/>
 * Searches the array for a given value and returns the corresponding key if successful
 * @link http://php.net/manual/en/function.array-search.php
 * @param mixed $needle <p>
 * The searched value.
 * </p>
 * <p>
 * If needle is a string, the comparison is done
 * in a case-sensitive manner.
 * </p>
 * @param array $haystack <p>
 * The array.
 * </p>
 * @param bool $strict [optional] <p>
 * If the third parameter strict is set to true
 * then the array_search function will also check the
 * types of the
 * needle in the haystack.
 * </p>
 * @return mixed the key for needle if it is found in the
 * array, false otherwise.
 * </p>
 * <p>
 * If needle is found in haystack
 * more than once, the first matching key is returned. To return the keys for
 * all matching values, use array_keys with the optional
 * search_value parameter instead.
 */
function array_search ($needle, array $haystack, $strict = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Import variables into the current symbol table from an array
 * @link http://php.net/manual/en/function.extract.php
 * @param array array $var_<p>
 * Note that prefix is only required if
 * extract_type is EXTR_PREFIX_SAME,
 * EXTR_PREFIX_ALL, EXTR_PREFIX_INVALID
 * or EXTR_PREFIX_IF_EXISTS. If
 * the prefixed result is not a valid variable name, it is not
 * imported into the symbol table. Prefixes are automatically separated from
 * the array key by an underscore character.
 * </p>
 * @param int $extract_type [optional] <p>
 * The way invalid/numeric keys and collisions are treated is determined
 * by the extract_type. It can be one of the
 * following values:
 * EXTR_OVERWRITE
 * If there is a collision, overwrite the existing variable.
 * @param string $prefix [optional] Only overwrite the variable if it already exists in the
 * current symbol table, otherwise do nothing. This is useful
 * for defining a list of valid variables and then extracting
 * only those variables you have defined out of
 * $_REQUEST, for example.
 * @return int the number of variables successfully imported into the symbol
 * table.
 */
function extract (array $var_array, $extract_type = null, $prefix = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Create array containing variables and their values
 * @link http://php.net/manual/en/function.compact.php
 * @param mixed $varname <p>
 * compact takes a variable number of parameters.
 * Each parameter can be either a string containing the name of the
 * variable, or an array of variable names. The array can contain other
 * arrays of variable names inside it; compact
 * handles it recursively.
 * </p>
 * @param mixed $_ [optional] 
 * @return array the output array with all the variables added to it.
 */
function compact ($varname, $_ = null) {}

/**
 * (PHP 4 &gt;= 4.2.0, PHP 5)<br/>
 * Fill an array with values
 * @link http://php.net/manual/en/function.array-fill.php
 * @param int $start_index <p>
 * The first index of the returned array.
 * Supports non-negative indexes only.
 * </p>
 * @param int $num <p>
 * Number of elements to insert
 * </p>
 * @param mixed $value <p>
 * Value to use for filling
 * </p>
 * @return array the filled array
 */
function array_fill ($start_index, $num, $value) {}

/**
 * (PHP 5 &gt;= 5.2.0)<br/>
 * Fill an array with values, specifying keys
 * @link http://php.net/manual/en/function.array-fill-keys.php
 * @param array $keys <p>
 * Array of values that will be used as keys. Illegal values
 * for key will be converted to string.
 * </p>
 * @param mixed $value <p>
 * Value to use for filling
 * </p>
 * @return array the filled array
 */
function array_fill_keys (array $keys, $value) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Create an array containing a range of elements
 * @link http://php.net/manual/en/function.range.php
 * @param mixed $low <p>
 * Low value.
 * </p>
 * @param mixed $high <p>
 * High value.
 * </p>
 * @param number $step [optional] <p>
 * If a step value is given, it will be used as the
 * increment between elements in the sequence. step
 * should be given as a positive number. If not specified,
 * step will default to 1.
 * </p>
 * @return array an array of elements from low to
 * high, inclusive. If low > high, the sequence will
 * be from high to low.
 */
function range ($low, $high, $step = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Sort multiple or multi-dimensional arrays
 * @link http://php.net/manual/en/function.array-multisort.php
 * @param array $arr <p>
 * An array being sorted.
 * </p>
 * @param mixed $arg [optional] <p>
 * Optionally another array, or sort options for the
 * previous array argument: 
 * SORT_ASC, 
 * SORT_DESC, 
 * SORT_REGULAR,
 * SORT_NUMERIC,
 * SORT_STRING.
 * </p>
 * @param mixed $arg [optional] 
 * @param mixed $_ [optional] 
 * @return bool Returns true on success or false on failure.
 */
function array_multisort (array &$arr, $arg = null, $arg = null, $_ = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Push one or more elements onto the end of array
 * @link http://php.net/manual/en/function.array-push.php
 * @param array $array <p>
 * The input array.
 * </p>
 * @param mixed $var <p>
 * The pushed value.
 * </p>
 * @param mixed $_ [optional] 
 * @return int the new number of elements in the array.
 */
function array_push (array &$array, $var, $_ = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Pop the element off the end of array
 * @link http://php.net/manual/en/function.array-pop.php
 * @param array $array <p>
 * The array to get the value from.
 * </p>
 * @return mixed the last value of array.
 * If array is empty (or is not an array),
 * &null; will be returned.
 */
function array_pop (array &$array) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Shift an element off the beginning of array
 * @link http://php.net/manual/en/function.array-shift.php
 * @param array $array <p>
 * The input array.
 * </p>
 * @return mixed the shifted value, or &null; if array is
 * empty or is not an array.
 */
function array_shift (array &$array) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Prepend one or more elements to the beginning of an array
 * @link http://php.net/manual/en/function.array-unshift.php
 * @param array $array <p>
 * The input array.
 * </p>
 * @param mixed $var <p>
 * The prepended variable.
 * </p>
 * @param mixed $_ [optional] 
 * @return int the new number of elements in the array.
 */
function array_unshift (array &$array, $var, $_ = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Remove a portion of the array and replace it with something else
 * @link http://php.net/manual/en/function.array-splice.php
 * @param array $input <p>
 * The input array.
 * </p>
 * @param int $offset <p>
 * If offset is positive then the start of removed
 * portion is at that offset from the beginning of the
 * input array. If offset
 * is negative then it starts that far from the end of the
 * input array.
 * </p>
 * @param int $length [optional] <p>
 * If length is omitted, removes everything
 * from offset to the end of the array. If
 * length is specified and is positive, then
 * that many elements will be removed. If
 * length is specified and is negative then
 * the end of the removed portion will be that many elements from
 * the end of the array. Tip: to remove everything from
 * offset to the end of the array when
 * replacement is also specified, use
 * count($input) for
 * length.
 * </p>
 * @param mixed $replacement [optional] <p>
 * If replacement array is specified, then the
 * removed elements are replaced with elements from this array.
 * </p>
 * <p>
 * If offset and length
 * are such that nothing is removed, then the elements from the
 * replacement array are inserted in the place
 * specified by the offset. Note that keys in
 * replacement array are not preserved.
 * </p>
 * <p>
 * If replacement is just one element it is
 * not necessary to put array()
 * around it, unless the element is an array itself.
 * </p>
 * @return array the array consisting of the extracted elements.
 */
function array_splice (array &$input, $offset, $length = null, $replacement = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Extract a slice of the array
 * @link http://php.net/manual/en/function.array-slice.php
 * @param array $array <p>
 * The input array.
 * </p>
 * @param int $offset <p>
 * If offset is non-negative, the sequence will
 * start at that offset in the array. If
 * offset is negative, the sequence will
 * start that far from the end of the array.
 * </p>
 * @param int $length [optional] <p>
 * If length is given and is positive, then
 * the sequence will have that many elements in it. If
 * length is given and is negative then the
 * sequence will stop that many elements from the end of the
 * array. If it is omitted, then the sequence will have everything
 * from offset up until the end of the
 * array.
 * </p>
 * @param bool $preserve_keys [optional] <p>
 * Note that array_slice will reorder and reset the
 * array indices by default. You can change this behaviour by setting
 * preserve_keys to true.
 * </p>
 * @return array the slice.
 */
function array_slice (array $array, $offset, $length = null, $preserve_keys = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Merge one or more arrays
 * @link http://php.net/manual/en/function.array-merge.php
 * @param array $array1 <p>
 * Initial array to merge.
 * </p>
 * @param array $array2 [optional] 
 * @param array $_ [optional] 
 * @return array the resulting array.
 */
function array_merge (array $array1, array $array2 = null, array $_ = null) {}

?>
