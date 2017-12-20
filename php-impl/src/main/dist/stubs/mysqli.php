<?php

// Start of mysqli v.0.1

class mysqli_sql_exception extends RuntimeException  {
	protected $message;
	protected $code;
	protected $file;
	protected $line;
	protected $sqlstate;


	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Clone the exception
	 * @link http://php.net/manual/en/exception.clone.php
	 * @return void 
	 */
	final private function __clone () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Construct the exception
	 * @link http://php.net/manual/en/exception.construct.php
	 * @param $message [optional]
	 * @param $code [optional]
	 * @param $previous [optional]
	 */
	public function __construct ($message, $code, $previous) {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Gets the Exception message
	 * @link http://php.net/manual/en/exception.getmessage.php
	 * @return string the Exception message as a string.
	 */
	final public function getMessage () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Gets the Exception code
	 * @link http://php.net/manual/en/exception.getcode.php
	 * @return int the Exception code as a integer.
	 */
	final public function getCode () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Gets the file in which the exception occurred
	 * @link http://php.net/manual/en/exception.getfile.php
	 * @return string the filename in which the exception was thrown.
	 */
	final public function getFile () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Gets the line in which the exception occurred
	 * @link http://php.net/manual/en/exception.getline.php
	 * @return int the line number where the exception was thrown.
	 */
	final public function getLine () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Gets the stack trace
	 * @link http://php.net/manual/en/exception.gettrace.php
	 * @return array the Exception stack trace as an array.
	 */
	final public function getTrace () {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Returns previous Exception
	 * @link http://php.net/manual/en/exception.getprevious.php
	 * @return Exception the previous Exception if available 
	 * or &null; otherwise.
	 */
	final public function getPrevious () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Gets the stack trace as a string
	 * @link http://php.net/manual/en/exception.gettraceasstring.php
	 * @return string the Exception stack trace as a string.
	 */
	final public function getTraceAsString () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * String representation of the exception
	 * @link http://php.net/manual/en/exception.tostring.php
	 * @return string the string representation of the exception.
	 */
	public function __toString () {}

}

/**
 * MySQLi Driver.
 * @link http://php.net/manual/en/class.mysqli-driver.php
 */
final class mysqli_driver  {
	/**
	 * @var string
	 */
	public $client_info;
	/**
	 * @var string
	 */
	public $client_version;
	/**
	 * @var string
	 */
	public $driver_version;
	/**
	 * @var string
	 */
	public $embedded;
	/**
	 * @var bool
	 */
	public $reconnect;
	/**
	 * @var int
	 */
	public $report_mode;

}

/**
 * Represents a connection between PHP and a MySQL database.
 * @link http://php.net/manual/en/class.mysqli.php
 */
class mysqli  {
	/**
	 * @var int
	 */
	public $affected_rows;
	/**
	 * @var string
	 */
	public $client_info;
	/**
	 * @var int
	 */
	public $client_version;
	/**
	 * @var string
	 */
	public $connect_errno;
	/**
	 * @var string
	 */
	public $connect_error;
	/**
	 * @var int
	 */
	public $errno;
	/**
	 * @var string
	 */
	public $error;
	public $field_count;
	/**
	 * @var string
	 */
	public $host_info;
	/**
	 * @var string
	 */
	public $info;
	/**
	 * @var mixed
	 */
	public $insert_id;
	/**
	 * @var string
	 */
	public $server_info;
	/**
	 * @var int
	 */
	public $server_version;
	/**
	 * @var string
	 */
	public $sqlstate;
	/**
	 * @var string
	 */
	public $protocol_version;
	/**
	 * @var int
	 */
	public $thread_id;
	/**
	 * @var int
	 */
	public $warning_count;


	/**
	 * (PHP 5)<br/>
	 * Open a new connection to the MySQL server
	 * </p>
	 * @param string $host [optional] Can be either a host name or an IP address. Passing the NULL value or the string "localhost" to this parameter, the local host is assumed. When possible, pipes will be used instead of the TCP/IP protocol. Prepending host by p: opens a persistent connection. mysqli_change_user() is automatically called on connections opened from the connection pool. Defaults to ini_get("mysqli.default_host")
	 * @param string $username [optional] The MySQL user name. Defaults to ini_get("mysqli.default_user")
	 * @param string $passwd [optional] If not provided or NULL, the MySQL server will attempt to authenticate the user against those user records which have no password only. This allows one username to be used with different permissions (depending on if a password as provided or not). Defaults to ini_get("mysqli.default_pw")
	 * @param string $dbname [optional] If provided will specify the default database to be used when performing queries. Defaults to ""
	 * @param int $port [optional] Specifies the port number to attempt to connect to the MySQL server. Defaults to ini_get("mysqli.default_port")
	 * @param string $socket [optional] Specifies the socket or named pipe that should be used. Defaults to ini_get("mysqli.default_socket")
	 */
	public function __construct (
	  $host,
	  $username,
	  $passwd,
	  $dbname,
	  $port,
	  $socket
	) {}

	/**
	 * (PHP 5)<br/>
	 * Turns on or off auto-commiting database modifications
	 * @link http://php.net/manual/en/mysqli.autocommit.php
	 * @param bool $mode <p>
	 * Whether to turn on auto-commit or not.
	 * </p>
	 * @return bool Returns true on success or false on failure.
	 */
	public function autocommit ($mode) {}

	/**
	 * (PHP 5)<br/>
	 * Changes the user of the specified database connection
	 * @link http://php.net/manual/en/mysqli.change-user.php
	 * @param string $user <p>
	 * The MySQL user name.
	 * </p>
	 * @param string $password <p>
	 * The MySQL password.
	 * </p>
	 * @param string $database <p>
	 * The database to change to.
	 * </p>
	 * <p>
	 * If desired, the &null; value may be passed resulting in only changing
	 * the user and not selecting a database. To select a database in this
	 * case use the mysqli_select_db function.
	 * </p>
	 * @return bool Returns true on success or false on failure.
	 */
	public function change_user ($user, $password, $database) {}

	/**
	 * (PHP 5)<br/>
	 * Returns the default character set for the database connection
	 * @link http://php.net/manual/en/mysqli.character-set-name.php
	 * @return string The default character set for the current connection
	 */
	public function character_set_name () {}

	public function client_encoding () {}

	/**
	 * (PHP 5)<br/>
	 * Closes a previously opened database connection
	 * @link http://php.net/manual/en/mysqli.close.php
	 * @return bool Returns true on success or false on failure.
	 */
	public function close () {}

	/**
	 * (PHP 5)<br/>
	 * Commits the current transaction
	 * @link http://php.net/manual/en/mysqli.commit.php
	 * @return bool Returns true on success or false on failure.
	 */
	public function commit () {}

	public function connect () {}

	/**
	 * (PHP 5)<br/>
	 * Dump debugging information into the log
	 * @link http://php.net/manual/en/mysqli.dump-debug-info.php
	 * @return bool Returns true on success or false on failure.
	 */
	public function dump_debug_info () {}

	/**
	 * (PHP 5)<br/>
	 * Performs debugging operations
	 * @link http://php.net/manual/en/mysqli.debug.php
	 * @param string $message <p>
	 * A string representing the debugging operation to perform
	 * </p>
	 * @return bool true.
	 */
	public function debug ($message) {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Returns a character set object
	 * @link http://php.net/manual/en/mysqli.get-charset.php
	 * @return object The function returns a character set object with the following properties:
	 * charset
	 * <p>Character set name</p>
	 * collation
	 * <p>Collation name</p>
	 * dir
	 * <p>Directory the charset description was fetched from (?) or "" for built-in character sets</p>
	 * min_length
	 * <p>Minimum character length in bytes</p>
	 * max_length
	 * <p>Maximum character length in bytes</p>
	 * number
	 * <p>Internal character set number</p>
	 * state
	 * <p>Character set status (?)</p>
	 */
	public function get_charset () {}

	/**
	 * (PHP 5)<br/>
	 * Returns the MySQL client version as a string
	 * @link http://php.net/manual/en/mysqli.get-client-info.php
	 * @return string A string that represents the MySQL client library version
	 */
	public function get_client_info () {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Returns statistics about the client connection
	 * @link http://php.net/manual/en/mysqli.get-connection-stats.php
	 * @return bool an array with connection stats if success, false otherwise.
	 */
	public function get_connection_stats () {}

	public function get_server_info () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get result of SHOW WARNINGS
	 * @link http://php.net/manual/en/mysqli.get-warnings.php
	 * @return mysqli_warnings 
	 */
	public function get_warnings () {}

	/**
	 * (PHP 5)<br/>
	 * Initializes MySQLi and returns a resource for use with mysqli_real_connect()
	 * @link http://php.net/manual/en/mysqli.init.php
	 * @return mysqli an object.
	 */
	public function init () {}

	/**
	 * (PHP 5)<br/>
	 * Asks the server to kill a MySQL thread
	 * @link http://php.net/manual/en/mysqli.kill.php
	 * @param int $processid 
	 * @return bool Returns true on success or false on failure.
	 */
	public function kill ($processid) {}

	/**
	 * (PHP 5)<br/>
	 * Performs a query on the database
	 * @link http://php.net/manual/en/mysqli.multi-query.php
	 * @param string $query <p>
	 * The query, as a string.
	 * </p>
	 * <p>
	 * Data inside the query should be properly escaped.
	 * </p>
	 * @return bool false if the first statement failed.
	 * To retrieve subsequent errors from other statements you have to call
	 * mysqli_next_result first.
	 */
	public function multi_query ($query) {}

	public function mysqli () {}

	/**
	 * (PHP 5)<br/>
	 * Check if there are any more query results from a multi query
	 * @link http://php.net/manual/en/mysqli.more-results.php
	 * @return bool Returns true on success or false on failure.
	 */
	public function more_results () {}

	/**
	 * (PHP 5)<br/>
	 * Prepare next result from multi_query
	 * @link http://php.net/manual/en/mysqli.next-result.php
	 * @return bool Returns true on success or false on failure.
	 */
	public function next_result () {}

	/**
	 * (PHP 5)<br/>
	 * Set options
	 * @link http://php.net/manual/en/mysqli.options.php
	 * @param int $option <p>
	 * The option that you want to set. It can be one of the following values:
	 * <table>
	 * Valid options
	 * <tr valign="top">
	 * <td>Name</td>
	 * <td>Description</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>MYSQLI_OPT_CONNECT_TIMEOUT</td>
	 * <td>connection timeout in seconds (supported on Windows with TCP/IP since PHP 5.3.1)</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>MYSQLI_OPT_LOCAL_INFILE</td>
	 * <td>enable/disable use of LOAD LOCAL INFILE</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>MYSQLI_INIT_COMMAND</td>
	 * <td>command to execute after when connecting to MySQL server</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>MYSQLI_READ_DEFAULT_FILE</td>
	 * <td>
	 * Read options from named option file instead of my.cnf
	 * </td>
	 * </tr>
	 * <tr valign="top">
	 * <td>MYSQLI_READ_DEFAULT_GROUP</td>
	 * <td>
	 * Read options from the named group from my.cnf
	 * or the file specified with MYSQL_READ_DEFAULT_FILE.
	 * </td>
	 * </tr>
	 * </table>
	 * </p>
	 * @param mixed $value <p>
	 * The value for the option.
	 * </p>
	 * @return bool Returns true on success or false on failure.
	 */
	public function options ($option, $value) {}

	/**
	 * (PHP 5)<br/>
	 * Pings a server connection, or tries to reconnect if the connection has gone down
	 * @link http://php.net/manual/en/mysqli.ping.php
	 * @return bool Returns true on success or false on failure.
	 */
	public function ping () {}

	/**
	 * (PHP 5)<br/>
	 * Prepare a SQL statement for execution
	 * @link http://php.net/manual/en/mysqli.prepare.php
	 * @param string $query <p>
	 * The query, as a string.
	 * </p>
	 * <p>
	 * You should not add a terminating semicolon or \g
	 * to the statement.
	 * </p>
	 * <p>
	 * This parameter can include one or more parameter markers in the SQL
	 * statement by embedding question mark (?) characters
	 * at the appropriate positions.
	 * </p>
	 * <p>
	 * The markers are legal only in certain places in SQL statements.
	 * For example, they are allowed in the VALUES()
	 * list of an INSERT statement (to specify column
	 * values for a row), or in a comparison with a column in a
	 * WHERE clause to specify a comparison value.
	 * </p>
	 * <p>
	 * However, they are not allowed for identifiers (such as table or
	 * column names), in the select list that names the columns to be
	 * returned by a SELECT statement, or to specify both
	 * operands of a binary operator such as the = equal
	 * sign. The latter restriction is necessary because it would be
	 * impossible to determine the parameter type. It's not allowed to
	 * compare marker with NULL by 
	 * ? IS NULL too. In general, parameters are legal
	 * only in Data Manipulation Language (DML) statements, and not in Data
	 * Definition Language (DDL) statements.
	 * </p>
	 * @return mysqli_stmt mysqli_prepare returns a statement object or false if an error occurred.
	 */
	public function prepare ($query) {}

	/**
	 * (PHP 5)<br/>
	 * Performs a query on the database
	 * @link http://php.net/manual/en/mysqli.query.php
	 * @param string $query <p>
	 * The query string.
	 * </p>
	 * <p>
	 * Data inside the query should be properly escaped.
	 * </p>
	 * @param int $resultmode [optional] <p>
	 * Either the constant MYSQLI_USE_RESULT or
	 * MYSQLI_STORE_RESULT depending on the desired
	 * behavior. By default, MYSQLI_STORE_RESULT is used.
	 * </p>
	 * <p>
	 * If you use MYSQLI_USE_RESULT all subsequent calls
	 * will return error Commands out of sync unless you
	 * call mysqli_free_result
	 * </p>
	 * <p>
	 * With MYSQLI_ASYNC (available with mysqlnd), it is
	 * possible to perform query asynchronously.
	 * mysqli_poll is then used to get results from such
	 * queries.
	 * </p>
	 * @return mixed Returns true on success or false on failure. For SELECT, SHOW, DESCRIBE or
	 * EXPLAIN mysqli_query will return
	 * a result object.
	 */
	public function query ($query, $resultmode = null) {}

	/**
	 * (PHP 5)<br/>
	 * Opens a connection to a mysql server
	 * @link http://php.net/manual/en/mysqli.real-connect.php
	 * @param string $host [optional] <p>
	 * Can be either a host name or an IP address. Passing the &null; value
	 * or the string "localhost" to this parameter, the local host is
	 * assumed. When possible, pipes will be used instead of the TCP/IP
	 * protocol.
	 * </p>
	 * @param string $username [optional] <p>
	 * The MySQL user name.
	 * </p>
	 * @param string $passwd [optional] <p>
	 * If provided or &null;, the MySQL server will attempt to authenticate
	 * the user against those user records which have no password only. This
	 * allows one username to be used with different permissions (depending
	 * on if a password as provided or not).
	 * </p>
	 * @param string $dbname [optional] <p>
	 * If provided will specify the default database to be used when
	 * performing queries.
	 * </p>
	 * @param int $port [optional] <p>
	 * Specifies the port number to attempt to connect to the MySQL server.
	 * </p>
	 * @param string $socket [optional] <p>
	 * Specifies the socket or named pipe that should be used.
	 * </p>
	 * <p>
	 * Specifying the socket parameter will not
	 * explicitly determine the type of connection to be used when
	 * connecting to the MySQL server. How the connection is made to the
	 * MySQL database is determined by the host
	 * parameter.
	 * </p>
	 * @param int $flags [optional] <p>
	 * With the parameter flags you can set different
	 * connection options:
	 * </p>
	 * <table>
	 * Supported flags
	 * <tr valign="top">
	 * <td>Name</td>
	 * <td>Description</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>MYSQLI_CLIENT_COMPRESS</td>
	 * <td>Use compression protocol</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>MYSQLI_CLIENT_FOUND_ROWS</td>
	 * <td>return number of matched rows, not the number of affected rows</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>MYSQLI_CLIENT_IGNORE_SPACE</td>
	 * <td>Allow spaces after function names. Makes all function names reserved words.</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>MYSQLI_CLIENT_INTERACTIVE</td>
	 * <td>
	 * Allow interactive_timeout seconds (instead of
	 * wait_timeout seconds) of inactivity before closing the connection
	 * </td>
	 * </tr>
	 * <tr valign="top">
	 * <td>MYSQLI_CLIENT_SSL</td>
	 * <td>Use SSL (encryption)</td>
	 * </tr>
	 * </table>
	 * <p>
	 * For security reasons the MULTI_STATEMENT flag is
	 * not supported in PHP. If you want to execute multiple queries use the
	 * mysqli_multi_query function.
	 * </p>
	 * @return bool Returns true on success or false on failure.
	 */
	public function real_connect ($host = null, $username = null, $passwd = null, $dbname = null, $port = null, $socket = null, $flags = null) {}

	/**
	 * (PHP 5)<br/>
	 * Escapes special characters in a string for use in a SQL statement, taking into account the current charset of the connection
	 * @link http://php.net/manual/en/mysqli.real-escape-string.php
	 * @param string $escapestr <p>
	 * The string to be escaped.
	 * </p>
	 * <p>
	 * Characters encoded are NUL (ASCII 0), \n, \r, \, ', ", and
	 * Control-Z.
	 * </p>
	 * @return string an escaped string.
	 */
	public function real_escape_string ($escapestr) {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Poll connections
	 * @link http://php.net/manual/en/mysqli.poll.php
	 * @param array $read <p>
	 * </p>
	 * @param array $error <p>
	 * </p>
	 * @param array $reject <p>
	 * </p>
	 * @param int $sec <p>
	 * Number of seconds to wait, must be non-negative.
	 * </p>
	 * @param int $usec [optional] <p>
	 * Number of microseconds to wait, must be non-negative.
	 * </p>
	 * @return int number of ready connections in success, false otherwise.
	 */
	public function poll (array &$read , array &$error , array &$reject , $sec, $usec = null) {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Get result from async query
	 * @link http://php.net/manual/en/mysqli.reap-async-query.php
	 * @return mysqli_result mysqli_result in success, false otherwise.
	 */
	public function reap_async_query () {}

	public function escape_string () {}

	/**
	 * (PHP 5)<br/>
	 * Execute an SQL query
	 * @link http://php.net/manual/en/mysqli.real-query.php
	 * @param string $query <p>
	 * The query, as a string.
	 * </p>
	 * <p>
	 * Data inside the query should be properly escaped.
	 * </p>
	 * @return bool Returns true on success or false on failure.
	 */
	public function real_query ($query) {}

	/**
	 * (PHP 5)<br/>
	 * Rolls back current transaction
	 * @link http://php.net/manual/en/mysqli.rollback.php
	 * @return bool Returns true on success or false on failure.
	 */
	public function rollback () {}

	/**
	 * (PHP 5)<br/>
	 * Selects the default database for database queries
	 * @link http://php.net/manual/en/mysqli.select-db.php
	 * @param string $dbname <p>
	 * The database name.
	 * </p>
	 * @return bool Returns true on success or false on failure.
	 */
	public function select_db ($dbname) {}

	/**
	 * (PHP 5 &gt;= 5.0.5)<br/>
	 * Sets the default client character set
	 * @link http://php.net/manual/en/mysqli.set-charset.php
	 * @param string $charset <p>
	 * The charset to be set as default.
	 * </p>
	 * @return bool Returns true on success or false on failure.
	 */
	public function set_charset ($charset) {}

	public function set_opt () {}

	/**
	 * (PHP 5)<br/>
	 * Gets the current system status
	 * @link http://php.net/manual/en/mysqli.stat.php
	 * @return string A string describing the server status. false if an error occurred.
	 */
	public function stat () {}

	/**
	 * (PHP 5)<br/>
	 * Initializes a statement and returns an object for use with mysqli_stmt_prepare
	 * @link http://php.net/manual/en/mysqli.stmt-init.php
	 * @return mysqli_stmt an object.
	 */
	public function stmt_init () {}

	/**
	 * (PHP 5)<br/>
	 * Transfers a result set from the last query
	 * @link http://php.net/manual/en/mysqli.store-result.php
	 * @return mysqli_result a buffered result object or false if an error occurred.
	 * </p>
	 * <p>
	 * mysqli_store_result returns false in case the query
	 * didn't return a result set (if the query was, for example an INSERT
	 * statement). This function also returns false if the reading of the
	 * result set failed. You can check if you have got an error by checking
	 * if mysqli_error doesn't return an empty string, if
	 * mysqli_errno returns a non zero value, or if
	 * mysqli_field_count returns a non zero value.
	 * Also possible reason for this function returning false after
	 * successful call to mysqli_query can be too large
	 * result set (memory for it cannot be allocated). If
	 * mysqli_field_count returns a non-zero value, the
	 * statement should have produced a non-empty result set.
	 */
	public function store_result () {}

	/**
	 * (PHP 5)<br/>
	 * Returns whether thread safety is given or not
	 * @link http://php.net/manual/en/mysqli.thread-safe.php
	 * @return bool true if the client library is thread-safe, otherwise false.
	 */
	public function thread_safe () {}

	/**
	 * (PHP 5)<br/>
	 * Initiate a result set retrieval
	 * @link http://php.net/manual/en/mysqli.use-result.php
	 * @return mysqli_result an unbuffered result object or false if an error occurred.
	 */
	public function use_result () {}

	public function refresh () {}

}

/**
 * Represents a MySQL warning.
 * @link http://php.net/manual/en/class.mysqli-warning.php
 */
final class mysqli_warning  {
	public $message;
	public $sqlstate;
	public $errno;


	/**
	 * (No version information available, might only be in SVN)<br/>
	 * The __construct purpose
	 * @link http://php.net/manual/en/mysqli-warning.construct.php
	 */
	protected function __construct () {}

	/**
	 * (No version information available, might only be in SVN)<br/>
	 * The next purpose
	 * @link http://php.net/manual/en/mysqli-warning.next.php
	 * @return void 
	 */
	public function next () {}

}

/**
 * Represents the result set obtained from a query against the database.
 * @link http://php.net/manual/en/class.mysqli-result.php
 */
class mysqli_result  {
	/**
	 * @var int
	 */
	public $current_field;
	/**
	 * @var int
	 */
	public $field_count;
	/**
	 * @var array
	 */
	public $lengths;
	/**
	 * @var int
	 */
	public $num_rows;
	public $type;


	public function __construct () {}

	public function close () {}

	/**
	 * (PHP 5)<br/>
	 * Frees the memory associated with a result
	 * @link http://php.net/manual/en/mysqli-result.free.php
	 * @return void 
	 */
	public function free () {}

	/**
	 * (PHP 5)<br/>
	 * Adjusts the result pointer to an arbitary row in the result
	 * @link http://php.net/manual/en/mysqli-result.data-seek.php
	 * @param int $offset <p>
	 * The field offset. Must be between zero and the total number of rows
	 * minus one (0..mysqli_num_rows - 1).
	 * </p>
	 * @return bool Returns true on success or false on failure.
	 */
	public function data_seek ($offset) {}

	/**
	 * (PHP 5)<br/>
	 * Returns the next field in the result set
	 * @link http://php.net/manual/en/mysqli-result.fetch-field.php
	 * @return object an object which contains field definition information or false
	 * if no field information is available.
	 * </p>
	 * <p>
	 * <table>
	 * Object properties
	 * <tr valign="top">
	 * <td>Property</td>
	 * <td>Description</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>name</td>
	 * <td>The name of the column</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>orgname</td>
	 * <td>Original column name if an alias was specified</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>table</td>
	 * <td>The name of the table this field belongs to (if not calculated)</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>orgtable</td>
	 * <td>Original table name if an alias was specified</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>def</td>
	 * <td>The default value for this field, represented as a string</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>max_length</td>
	 * <td>The maximum width of the field for the result set.</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>length</td>
	 * <td>The width of the field, as specified in the table definition.</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>charsetnr</td>
	 * <td>The character set number for the field.</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>flags</td>
	 * <td>An integer representing the bit-flags for the field.</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>type</td>
	 * <td>The data type used for this field</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>decimals</td>
	 * <td>The number of decimals used (for integer fields)</td>
	 * </tr>
	 * </table>
	 */
	public function fetch_field () {}

	/**
	 * (PHP 5)<br/>
	 * Returns an array of objects representing the fields in a result set
	 * @link http://php.net/manual/en/mysqli-result.fetch-fields.php
	 * @return array an array of objects which contains field definition information or
	 * false if no field information is available.
	 * </p>
	 * <p>
	 * <table>
	 * Object properties
	 * <tr valign="top">
	 * <td>Property</td>
	 * <td>Description</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>name</td>
	 * <td>The name of the column</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>orgname</td>
	 * <td>Original column name if an alias was specified</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>table</td>
	 * <td>The name of the table this field belongs to (if not calculated)</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>orgtable</td>
	 * <td>Original table name if an alias was specified</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>def</td>
	 * <td>The default value for this field, represented as a string</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>max_length</td>
	 * <td>The maximum width of the field for the result set.</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>length</td>
	 * <td>The width of the field, as specified in the table definition.</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>charsetnr</td>
	 * <td>The character set number for the field.</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>flags</td>
	 * <td>An integer representing the bit-flags for the field.</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>type</td>
	 * <td>The data type used for this field</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>decimals</td>
	 * <td>The number of decimals used (for integer fields)</td>
	 * </tr>
	 * </table>
	 */
	public function fetch_fields () {}

	/**
	 * (PHP 5)<br/>
	 * Fetch meta-data for a single field
	 * @link http://php.net/manual/en/mysqli-result.fetch-field-direct.php
	 * @param int $fieldnr <p>
	 * The field number. This value must be in the range from 
	 * 0 to number of fields - 1.
	 * </p>
	 * @return object an object which contains field definition information or false
	 * if no field information for specified fieldnr is 
	 * available.
	 * </p>
	 * <p>
	 * <table>
	 * Object attributes
	 * <tr valign="top">
	 * <td>Attribute</td>
	 * <td>Description</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>name</td>
	 * <td>The name of the column</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>orgname</td>
	 * <td>Original column name if an alias was specified</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>table</td>
	 * <td>The name of the table this field belongs to (if not calculated)</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>orgtable</td>
	 * <td>Original table name if an alias was specified</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>def</td>
	 * <td>The default value for this field, represented as a string</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>max_length</td>
	 * <td>The maximum width of the field for the result set.</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>length</td>
	 * <td>The width of the field, as specified in the table definition.</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>charsetnr</td>
	 * <td>The character set number for the field.</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>flags</td>
	 * <td>An integer representing the bit-flags for the field.</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>type</td>
	 * <td>The data type used for this field</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>decimals</td>
	 * <td>The number of decimals used (for integer fields)</td>
	 * </tr>
	 * </table>
	 */
	public function fetch_field_direct ($fieldnr) {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Fetches all result rows as an associative array, a numeric array, or both
	 * @link http://php.net/manual/en/mysqli-result.fetch-all.php
	 * @param int $resulttype [optional] <p>
	 * This optional parameter is a constant indicating what type of array
	 * should be produced from the current row data. The possible values for
	 * this parameter are the constants MYSQLI_ASSOC,
	 * MYSQLI_NUM, or MYSQLI_BOTH.
	 * </p>
	 * @return mixed an array of associative or numeric arrays holding result rows.
	 */
	public function fetch_all ($resulttype = null) {}

	/**
	 * (PHP 5)<br/>
	 * Fetch a result row as an associative, a numeric array, or both
	 * @link http://php.net/manual/en/mysqli-result.fetch-array.php
	 * @param int $resulttype [optional] <p>
	 * This optional parameter is a constant indicating what type of array
	 * should be produced from the current row data. The possible values for
	 * this parameter are the constants MYSQLI_ASSOC,
	 * MYSQLI_NUM, or MYSQLI_BOTH.
	 * </p>
	 * <p>
	 * By using the MYSQLI_ASSOC constant this function
	 * will behave identically to the mysqli_fetch_assoc,
	 * while MYSQLI_NUM will behave identically to the
	 * mysqli_fetch_row function. The final option 
	 * MYSQLI_BOTH will create a single array with the
	 * attributes of both.
	 * </p>
	 * @return mixed an array of strings that corresponds to the fetched row or &null; if there
	 * are no more rows in resultset.
	 */
	public function fetch_array ($resulttype = null) {}

	/**
	 * (PHP 5)<br/>
	 * Fetch a result row as an associative array
	 * @link http://php.net/manual/en/mysqli-result.fetch-assoc.php
	 * @return array an associative array of strings representing the fetched row in the result
	 * set, where each key in the array represents the name of one of the result
	 * set's columns or &null; if there are no more rows in resultset.
	 * </p>
	 * <p>
	 * If two or more columns of the result have the same field names, the last
	 * column will take precedence. To access the other column(s) of the same
	 * name, you either need to access the result with numeric indices by using
	 * mysqli_fetch_row or add alias names.
	 */
	public function fetch_assoc () {}

	/**
	 * (PHP 5)<br/>
	 * Returns the current row of a result set as an object
	 * @link http://php.net/manual/en/mysqli-result.fetch-object.php
	 * @param string $class_name [optional] <p>
	 * The name of the class to instantiate, set the properties of and return.
	 * If not specified, a stdClass object is returned.
	 * </p>
	 * @param array $params [optional] <p>
	 * An optional array of parameters to pass to the constructor
	 * for class_name objects.
	 * </p>
	 * @return stdClass|object an object with string properties that corresponds to the fetched
	 * row or &null; if there are no more rows in resultset.
	 */
	public function fetch_object ($class_name = null, array $params = null) {}

	/**
	 * (PHP 5)<br/>
	 * Get a result row as an enumerated array
	 * @link http://php.net/manual/en/mysqli-result.fetch-row.php
	 * @return mixed mysqli_fetch_row returns an array of strings that corresponds to the fetched row
	 * or &null; if there are no more rows in result set.
	 */
	public function fetch_row () {}

	/**
	 * (PHP 5)<br/>
	 * Set result pointer to a specified field offset
	 * @link http://php.net/manual/en/mysqli-result.field-seek.php
	 * @param int $fieldnr <p>
	 * The field number. This value must be in the range from 
	 * 0 to number of fields - 1.
	 * </p>
	 * @return bool Returns true on success or false on failure.
	 */
	public function field_seek ($fieldnr) {}

	public function free_result () {}

}

/**
 * Represents a prepared statement.
 * @link http://php.net/manual/en/class.mysqli-stmt.php
 */
class mysqli_stmt  {
	/**
	 * @var int
	 */
	public $affected_rows;
	/**
	 * @var int
	 */
	public $insert_id;
	/**
	 * @var int
	 */
	public $num_rows;
	/**
	 * @var int
	 */
	public $param_count;
	/**
	 * @var int
	 */
	public $field_count;
	/**
	 * @var int
	 */
	public $errno;
	/**
	 * @var string
	 */
	public $error;
	/**
	 * @var string
	 */
	public $sqlstate;
	public $id;


	public function __construct () {}

	/**
	 * (PHP 5)<br/>
	 * Used to get the current value of a statement attribute
	 * @link http://php.net/manual/en/mysqli-stmt.attr-get.php
	 * @param int $attr <p>
	 * The attribute that you want to get.
	 * </p>
	 * @return int false if the attribute is not found, otherwise returns the value of the attribute.
	 */
	public function attr_get ($attr) {}

	/**
	 * (PHP 5)<br/>
	 * Used to modify the behavior of a prepared statement
	 * @link http://php.net/manual/en/mysqli-stmt.attr-set.php
	 * @param int $attr <p>
	 * The attribute that you want to set. It can have one of the following values:
	 * <table>
	 * Attribute values
	 * <tr valign="top">
	 * <td>Character</td>
	 * <td>Description</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>MYSQLI_STMT_ATTR_UPDATE_MAX_LENGTH</td>
	 * <td>
	 * If set to 1, causes mysqli_stmt_store_result to
	 * update the metadata MYSQL_FIELD->max_length value.
	 * </td>
	 * </tr>
	 * <tr valign="top">
	 * <td>MYSQLI_STMT_ATTR_CURSOR_TYPE</td>
	 * <td>
	 * Type of cursor to open for statement when mysqli_stmt_execute
	 * is invoked. mode can be MYSQLI_CURSOR_TYPE_NO_CURSOR
	 * (the default) or MYSQLI_CURSOR_TYPE_READ_ONLY.
	 * </td>
	 * </tr>
	 * <tr valign="top">
	 * <td>MYSQLI_STMT_ATTR_PREFETCH_ROWS</td>
	 * <td>
	 * Number of rows to fetch from server at a time when using a cursor.
	 * mode can be in the range from 1 to the maximum
	 * value of unsigned long. The default is 1.
	 * </td>
	 * </tr>
	 * </table>
	 * </p>
	 * <p>
	 * If you use the MYSQLI_STMT_ATTR_CURSOR_TYPE option with
	 * MYSQLI_CURSOR_TYPE_READ_ONLY, a cursor is opened for the
	 * statement when you invoke mysqli_stmt_execute. If there
	 * is already an open cursor from a previous mysqli_stmt_execute call,
	 * it closes the cursor before opening a new one. mysqli_stmt_reset
	 * also closes any open cursor before preparing the statement for re-execution.
	 * mysqli_stmt_free_result closes any open cursor.
	 * </p>
	 * <p>
	 * If you open a cursor for a prepared statement, mysqli_stmt_store_result
	 * is unnecessary.
	 * </p>
	 * @param int $mode <p>The value to assign to the attribute.</p>
	 * @return bool 
	 */
	public function attr_set ($attr, $mode) {}

	/**
	 * (PHP 5)<br/>
	 * Binds variables to a prepared statement as parameters
	 * @link http://php.net/manual/en/mysqli-stmt.bind-param.php
	 * @param string $types <p>
	 * A string that contains one or more characters which specify the types
	 * for the corresponding bind variables:
	 * <table>
	 * Type specification chars
	 * <tr valign="top">
	 * <td>Character</td>
	 * <td>Description</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>i</td>
	 * <td>corresponding variable has type integer</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>d</td>
	 * <td>corresponding variable has type double</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>s</td>
	 * <td>corresponding variable has type string</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>b</td>
	 * <td>corresponding variable is a blob and will be sent in packets</td>
	 * </tr>
	 * </table>
	 * </p>
	 * @param mixed $var1 <p>
	 * The number of variables and length of string 
	 * types must match the parameters in the statement.
	 * </p>
	 * @param mixed $_ [optional] 
	 * @return bool Returns true on success or false on failure.
	 */
	public function bind_param ($types, &$var1, &$_ = null) {}

	/**
	 * (PHP 5)<br/>
	 * Binds variables to a prepared statement for result storage
	 * @link http://php.net/manual/en/mysqli-stmt.bind-result.php
	 * @param mixed $var1 <p>
	 * The variable to be bound.
	 * </p>
	 * @param mixed $_ [optional] 
	 * @return bool Returns true on success or false on failure.
	 */
	public function bind_result (&$var1, &$_ = null) {}

	/**
	 * (PHP 5)<br/>
	 * Closes a prepared statement
	 * @link http://php.net/manual/en/mysqli-stmt.close.php
	 * @return bool Returns true on success or false on failure.
	 */
	public function close () {}

	/**
	 * (PHP 5)<br/>
	 * Seeks to an arbitrary row in statement result set
	 * @link http://php.net/manual/en/mysqli-stmt.data-seek.php
	 * @param int $offset <p>
	 * Must be between zero and the total number of rows minus one (0..
	 * mysqli_stmt_num_rows - 1).
	 * </p>
	 * @return void 
	 */
	public function data_seek ($offset) {}

	/**
	 * (PHP 5)<br/>
	 * Executes a prepared Query
	 * @link http://php.net/manual/en/mysqli-stmt.execute.php
	 * @return bool Returns true on success or false on failure.
	 */
	public function execute () {}

	/**
	 * (PHP 5)<br/>
	 * Fetch results from a prepared statement into the bound variables
	 * @link http://php.net/manual/en/mysqli-stmt.fetch.php
	 * @return bool 
	 */
	public function fetch () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get result of SHOW WARNINGS
	 * @link http://php.net/manual/en/mysqli-stmt.get-warnings.php
	 * @param mysqli_stmt $stmt 
	 * @return object 
	 */
	public function get_warnings ($stmtmysqli_stmt ) {}

	/**
	 * (PHP 5)<br/>
	 * Returns result set metadata from a prepared statement
	 * @link http://php.net/manual/en/mysqli-stmt.result-metadata.php
	 * @return mysqli_result a result object or false if an error occurred.
	 */
	public function result_metadata () {}

	public function more_results () {}

	public function next_result () {}

	/**
	 * (PHP 5)<br/>
	 * Return the number of rows in statements result set
	 * @link http://php.net/manual/en/mysqli-stmt.num-rows.php
	 * @param mysqli_stmt $stmt 
	 * @return int An integer representing the number of rows in result set.
	 */
	public function num_rows ($stmtmysqli_stmt ) {}

	/**
	 * (PHP 5)<br/>
	 * Send data in blocks
	 * @link http://php.net/manual/en/mysqli-stmt.send-long-data.php
	 * @param int $param_nr <p>
	 * Indicates which parameter to associate the data with. Parameters are
	 * numbered beginning with 0.
	 * </p>
	 * @param string $data <p>
	 * A string containing data to be sent.
	 * </p>
	 * @return bool Returns true on success or false on failure.
	 */
	public function send_long_data ($param_nr, $data) {}

	public function stmt () {}

	/**
	 * (PHP 5)<br/>
	 * Frees stored result memory for the given statement handle
	 * @link http://php.net/manual/en/mysqli-stmt.free-result.php
	 * @return void 
	 */
	public function free_result () {}

	/**
	 * (PHP 5)<br/>
	 * Resets a prepared statement
	 * @link http://php.net/manual/en/mysqli-stmt.reset.php
	 * @return bool Returns true on success or false on failure.
	 */
	public function reset () {}

	/**
	 * (PHP 5)<br/>
	 * Prepare a SQL statement for execution
	 * @link http://php.net/manual/en/mysqli-stmt.prepare.php
	 * @param string $query <p>
	 * The query, as a string. It must consist of a single SQL statement.
	 * </p>
	 * <p>
	 * You can include one or more parameter markers in the SQL statement by
	 * embedding question mark (?) characters at the
	 * appropriate positions.
	 * </p>
	 * <p>
	 * You should not add a terminating semicolon or \g
	 * to the statement.
	 * </p>
	 * <p>
	 * The markers are legal only in certain places in SQL statements.
	 * For example, they are allowed in the VALUES() list of an INSERT statement
	 * (to specify column values for a row), or in a comparison with a column in
	 * a WHERE clause to specify a comparison value.
	 * </p>
	 * <p>
	 * However, they are not allowed for identifiers (such as table or column names),
	 * in the select list that names the columns to be returned by a SELECT statement),
	 * or to specify both operands of a binary operator such as the =
	 * equal sign. The latter restriction is necessary because it would be impossible
	 * to determine the parameter type. In general, parameters are legal only in Data
	 * Manipulation Language (DML) statements, and not in Data Definition Language
	 * (DDL) statements.
	 * </p>
	 * @return mixed Returns true on success or false on failure.
	 */
	public function prepare ($query) {}

	/**
	 * (PHP 5)<br/>
	 * Transfers a result set from a prepared statement
	 * @link http://php.net/manual/en/mysqli-stmt.store-result.php
	 * @return bool Returns true on success or false on failure.
	 */
	public function store_result () {}

	public function get_result () {}

}

function mysqli_affected_rows () {}

function mysqli_autocommit () {}

function mysqli_change_user () {}

function mysqli_character_set_name () {}

function mysqli_close () {}

function mysqli_commit () {}

function mysqli_connect () {}

function mysqli_connect_errno () {}

function mysqli_connect_error () {}

function mysqli_data_seek () {}

function mysqli_dump_debug_info () {}

function mysqli_debug () {}

function mysqli_errno () {}

function mysqli_error () {}

function mysqli_stmt_execute () {}

/**
 * (PHP 5)<br/>
 * Alias for <function>mysqli_stmt_execute</function>
 * @link http://php.net/manual/en/function.mysqli-execute.php
 */
function mysqli_execute () {}

function mysqli_fetch_field () {}

function mysqli_fetch_fields () {}

function mysqli_fetch_field_direct () {}

function mysqli_fetch_lengths () {}

function mysqli_fetch_all () {}

function mysqli_fetch_array () {}

function mysqli_fetch_assoc () {}

function mysqli_fetch_object () {}

function mysqli_fetch_row () {}

function mysqli_field_count () {}

function mysqli_field_seek () {}

function mysqli_field_tell () {}

function mysqli_free_result () {}

/**
 * (PHP 5 &gt;= 5.3.0)<br/>
 * Returns client Zval cache statistics
 * @link http://php.net/manual/en/mysqli.get-cache-stats.php
 * @return array an array with client Zval cache stats if success, false otherwise.
 */
function mysqli_get_cache_stats () {}

function mysqli_get_connection_stats () {}

/**
 * (PHP 5 &gt;= 5.3.0)<br/>
 * Returns client per-process statistics
 * @link http://php.net/manual/en/mysqli.get-client-stats.php
 * @return array an array with client stats if success, false otherwise.
 */
function mysqli_get_client_stats () {}

function mysqli_get_charset () {}

function mysqli_get_client_info () {}

function mysqli_get_client_version () {}

function mysqli_get_host_info () {}

function mysqli_get_proto_info () {}

function mysqli_get_server_info () {}

function mysqli_get_server_version () {}

function mysqli_get_warnings () {}

function mysqli_init () {}

function mysqli_info () {}

function mysqli_insert_id () {}

function mysqli_kill () {}

function mysqli_more_results () {}

function mysqli_multi_query () {}

function mysqli_next_result () {}

function mysqli_num_fields () {}

function mysqli_num_rows () {}

function mysqli_options () {}

function mysqli_ping () {}

/**
 * @param $read
 * @param $write
 * @param $error
 * @param $sec
 * @param $usec [optional]
 */
function mysqli_poll (array &$read = null, array &$write = null, &$error = null, $sec, $usec) {}

function mysqli_prepare () {}

/**
 * (PHP 5)<br/>
 * Enables or disables internal report functions
 * @link http://php.net/manual/en/function.mysqli-report.php
 * @param int $flags <p>
 * <table>
 * Supported flags
 * <tr valign="top">
 * <td>Name</td>
 * <td>Description</td>
 * </tr>
 * <tr valign="top">
 * <td>MYSQLI_REPORT_OFF</td>
 * <td>Turns reporting off</td>
 * </tr>
 * <tr valign="top">
 * <td>MYSQLI_REPORT_ERROR</td>
 * <td>Report errors from mysqli function calls</td>
 * </tr>
 * <tr valign="top">
 * <td>MYSQLI_REPORT_STRICT</td>
 * <td>Report warnings from mysqli function calls</td>
 * </tr>
 * <tr valign="top">
 * <td>MYSQLI_REPORT_INDEX</td>
 * <td>Report if no index or bad index was used in a query</td>
 * </tr>
 * <tr valign="top">
 * <td>MYSQLI_REPORT_ALL</td>
 * <td>Set all options (report all)</td>
 * </tr>
 * </table>
 * </p>
 * @return bool Returns true on success or false on failure.
 */
function mysqli_report ($flags) {}

function mysqli_query () {}

function mysqli_real_connect () {}

function mysqli_real_escape_string () {}

function mysqli_real_query () {}

function mysqli_reap_async_query () {}

function mysqli_rollback () {}

function mysqli_select_db () {}

function mysqli_set_charset () {}

function mysqli_stmt_attr_get () {}

function mysqli_stmt_attr_set () {}

function mysqli_stmt_field_count () {}

/**
 * (PHP 5)<br/>
 * Initializes a statement and returns an object for use with mysqli_stmt_prepare
 * @link http://php.net/manual/en/mysqli.stmt-init.php
 * @return mysqli_stmt an object.
 */
function mysqli_stmt_init () {}

function mysqli_stmt_prepare () {}

function mysqli_stmt_result_metadata () {}

function mysqli_stmt_send_long_data () {}

/**
 * @param $var1
 * @param $var2
 */
function mysqli_stmt_bind_param ($var1, $var2) {}

/**
 * @param $var1
 */
function mysqli_stmt_bind_result ($var1) {}

function mysqli_stmt_fetch () {}

function mysqli_stmt_free_result () {}

function mysqli_stmt_get_result () {}

function mysqli_stmt_get_warnings () {}

function mysqli_stmt_insert_id () {}

function mysqli_stmt_reset () {}

function mysqli_stmt_param_count () {}

function mysqli_sqlstate () {}

function mysqli_stat () {}

function mysqli_stmt_affected_rows () {}

function mysqli_stmt_close () {}

function mysqli_stmt_data_seek () {}

function mysqli_stmt_errno () {}

function mysqli_stmt_error () {}

function mysqli_stmt_more_results () {}

function mysqli_stmt_next_result () {}

function mysqli_stmt_num_rows () {}

function mysqli_stmt_sqlstate () {}

function mysqli_stmt_store_result () {}

function mysqli_store_result () {}

function mysqli_thread_id () {}

function mysqli_thread_safe () {}

function mysqli_use_result () {}

function mysqli_warning_count () {}

function mysqli_refresh () {}

/**
 * (PHP 5)<br/>
 * Alias for <function>mysqli_stmt_bind_param</function>
 * @link http://php.net/manual/en/function.mysqli-bind-param.php
 * @param $var1
 * @param $var2
 */
function mysqli_bind_param ($var1, $var2) {}

/**
 * (PHP 5)<br/>
 * Alias for <function>mysqli_stmt_bind_result</function>
 * @link http://php.net/manual/en/function.mysqli-bind-result.php
 * @param $var1
 */
function mysqli_bind_result ($var1) {}

/**
 * (PHP 5)<br/>
 * Alias of <function>mysqli_character_set_name</function>
 * @link http://php.net/manual/en/function.mysqli-client-encoding.php
 */
function mysqli_client_encoding () {}

/**
 * (PHP 5)<br/>
 * Alias of <function>mysqli_real_escape_string</function>
 * @link http://php.net/manual/en/function.mysqli-escape-string.php
 */
function mysqli_escape_string () {}

/**
 * (PHP 5)<br/>
 * Alias for <function>mysqli_stmt_fetch</function>
 * @link http://php.net/manual/en/function.mysqli-fetch.php
 */
function mysqli_fetch () {}

/**
 * (PHP 5)<br/>
 * Alias for <function>mysqli_stmt_param_count</function>
 * @link http://php.net/manual/en/function.mysqli-param-count.php
 */
function mysqli_param_count () {}

/**
 * (PHP 5)<br/>
 * Alias for <function>mysqli_stmt_result_metadata</function>
 * @link http://php.net/manual/en/function.mysqli-get-metadata.php
 */
function mysqli_get_metadata () {}

/**
 * (PHP 5)<br/>
 * Alias for <function>mysqli_stmt_send_long_data</function>
 * @link http://php.net/manual/en/function.mysqli-send-long-data.php
 */
function mysqli_send_long_data () {}

/**
 * (PHP 5)<br/>
 * Alias of <function>mysqli_options</function>
 * @link http://php.net/manual/en/function.mysqli-set-opt.php
 */
function mysqli_set_opt () {}


/**
 * <p>
 * Read options from the named group from my.cnf
 * or the file specified with MYSQLI_READ_DEFAULT_FILE
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_READ_DEFAULT_GROUP', 5);

/**
 * <p>
 * Read options from the named option file instead of from my.cnf
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_READ_DEFAULT_FILE', 4);

/**
 * <p>
 * Connect timeout in seconds
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_OPT_CONNECT_TIMEOUT', 0);

/**
 * <p>
 * Enables command LOAD LOCAL INFILE
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_OPT_LOCAL_INFILE', 8);

/**
 * <p>
 * Command to execute when connecting to MySQL server. Will automatically be re-executed when reconnecting.
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_INIT_COMMAND', 3);
define ('MYSQLI_OPT_NET_CMD_BUFFER_SIZE', 202);
define ('MYSQLI_OPT_NET_READ_BUFFER_SIZE', 203);
define ('MYSQLI_OPT_INT_AND_FLOAT_NATIVE', 201);

/**
 * <p>
 * Use SSL (encrypted protocol). This option should not be set by application programs; 
 * it is set internally in the MySQL client library
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_CLIENT_SSL', 2048);

/**
 * <p>
 * Use compression protocol
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_CLIENT_COMPRESS', 32);

/**
 * <p>
 * Allow interactive_timeout seconds
 * (instead of wait_timeout seconds) of inactivity before
 * closing the connection. The client's session
 * wait_timeout variable will be set to
 * the value of the session interactive_timeout variable. 
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_CLIENT_INTERACTIVE', 1024);

/**
 * <p>
 * Allow spaces after function names. Makes all functions names reserved words. 
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_CLIENT_IGNORE_SPACE', 256);

/**
 * <p>
 * Don't allow the db_name.tbl_name.col_name syntax.
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_CLIENT_NO_SCHEMA', 16);
define ('MYSQLI_CLIENT_FOUND_ROWS', 2);

/**
 * <p>
 * For using buffered resultsets
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_STORE_RESULT', 0);

/**
 * <p>
 * For using unbuffered resultsets
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_USE_RESULT', 1);
define ('MYSQLI_ASYNC', 8);

/**
 * <p>
 * Columns are returned into the array having the fieldname as the array index.
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_ASSOC', 1);

/**
 * <p>
 * Columns are returned into the array having an enumerated index.
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_NUM', 2);

/**
 * <p>
 * Columns are returned into the array having both a numerical index and the fieldname as the associative index. 
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_BOTH', 3);

/**
 * <p>
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_STMT_ATTR_UPDATE_MAX_LENGTH', 0);

/**
 * <p>
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_STMT_ATTR_CURSOR_TYPE', 1);

/**
 * <p>
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_CURSOR_TYPE_NO_CURSOR', 0);

/**
 * <p>
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_CURSOR_TYPE_READ_ONLY', 1);

/**
 * <p>
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_CURSOR_TYPE_FOR_UPDATE', 2);

/**
 * <p>
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_CURSOR_TYPE_SCROLLABLE', 4);

/**
 * <p>
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_STMT_ATTR_PREFETCH_ROWS', 2);

/**
 * <p>
 * Indicates that a field is defined as NOT NULL
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_NOT_NULL_FLAG', 1);

/**
 * <p>
 * Field is part of a primary index
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_PRI_KEY_FLAG', 2);

/**
 * <p>
 * Field is part of a unique index.
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_UNIQUE_KEY_FLAG', 4);

/**
 * <p>
 * Field is part of an index.
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_MULTIPLE_KEY_FLAG', 8);

/**
 * <p>
 * Field is defined as BLOB
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_BLOB_FLAG', 16);

/**
 * <p>
 * Field is defined as UNSIGNED
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_UNSIGNED_FLAG', 32);

/**
 * <p>
 * Field is defined as ZEROFILL
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_ZEROFILL_FLAG', 64);

/**
 * <p>
 * Field is defined as AUTO_INCREMENT
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_AUTO_INCREMENT_FLAG', 512);

/**
 * <p>
 * Field is defined as TIMESTAMP
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_TIMESTAMP_FLAG', 1024);

/**
 * <p>
 * Field is defined as SET
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_SET_FLAG', 2048);

/**
 * <p>
 * Field is defined as NUMERIC
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_NUM_FLAG', 32768);

/**
 * <p>
 * Field is part of an multi-index
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_PART_KEY_FLAG', 16384);

/**
 * <p>
 * Field is part of GROUP BY
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_GROUP_FLAG', 32768);

/**
 * <p>
 * Field is defined as ENUM. Available since PHP 5.3.0.
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_ENUM_FLAG', 256);
define ('MYSQLI_BINARY_FLAG', 128);
define ('MYSQLI_NO_DEFAULT_VALUE_FLAG', 4096);
define ('MYSQLI_ON_UPDATE_NOW_FLAG', 8192);

/**
 * <p>
 * Field is defined as DECIMAL
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_TYPE_DECIMAL', 0);

/**
 * <p>
 * Field is defined as TINYINT
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_TYPE_TINY', 1);

/**
 * <p>
 * Field is defined as SMALLINT
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_TYPE_SHORT', 2);

/**
 * <p>
 * Field is defined as INT
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_TYPE_LONG', 3);

/**
 * <p>
 * Field is defined as FLOAT
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_TYPE_FLOAT', 4);

/**
 * <p>
 * Field is defined as DOUBLE
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_TYPE_DOUBLE', 5);

/**
 * <p>
 * Field is defined as DEFAULT NULL
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_TYPE_NULL', 6);

/**
 * <p>
 * Field is defined as TIMESTAMP
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_TYPE_TIMESTAMP', 7);

/**
 * <p>
 * Field is defined as BIGINT
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_TYPE_LONGLONG', 8);

/**
 * <p>
 * Field is defined as MEDIUMINT
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_TYPE_INT24', 9);

/**
 * <p>
 * Field is defined as DATE
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_TYPE_DATE', 10);

/**
 * <p>
 * Field is defined as TIME
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_TYPE_TIME', 11);

/**
 * <p>
 * Field is defined as DATETIME
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_TYPE_DATETIME', 12);

/**
 * <p>
 * Field is defined as YEAR
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_TYPE_YEAR', 13);

/**
 * <p>
 * Field is defined as DATE
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_TYPE_NEWDATE', 14);

/**
 * <p>
 * Field is defined as ENUM
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_TYPE_ENUM', 247);

/**
 * <p>
 * Field is defined as SET
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_TYPE_SET', 248);

/**
 * <p>
 * Field is defined as TINYBLOB
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_TYPE_TINY_BLOB', 249);

/**
 * <p>
 * Field is defined as MEDIUMBLOB
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_TYPE_MEDIUM_BLOB', 250);

/**
 * <p>
 * Field is defined as LONGBLOB
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_TYPE_LONG_BLOB', 251);

/**
 * <p>
 * Field is defined as BLOB
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_TYPE_BLOB', 252);

/**
 * <p>
 * Field is defined as VARCHAR
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_TYPE_VAR_STRING', 253);

/**
 * <p>
 * Field is defined as STRING
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_TYPE_STRING', 254);

/**
 * <p>
 * Field is defined as CHAR
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_TYPE_CHAR', 1);

/**
 * <p>
 * Field is defined as INTERVAL
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_TYPE_INTERVAL', 247);

/**
 * <p>
 * Field is defined as GEOMETRY
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_TYPE_GEOMETRY', 255);

/**
 * <p>
 * Precision math DECIMAL or NUMERIC field (MySQL 5.0.3 and up)
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_TYPE_NEWDECIMAL', 246);

/**
 * <p>
 * Field is defined as BIT (MySQL 5.0.3 and up)
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_TYPE_BIT', 16);

/**
 * <p>
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_SET_CHARSET_NAME', 7);

/**
 * <p>
 * No more data available for bind variable
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_NO_DATA', 100);

/**
 * <p>
 * Data truncation occurred. Available since PHP 5.1.0 and MySQL 5.0.5.
 * </p>
 * @link http://php.net/manual/en/mysqli.constants.php
 */
define ('MYSQLI_DATA_TRUNCATED', 101);
define ('MYSQLI_REPORT_INDEX', 4);
define ('MYSQLI_REPORT_ERROR', 1);
define ('MYSQLI_REPORT_STRICT', 2);
define ('MYSQLI_REPORT_ALL', 255);
define ('MYSQLI_REPORT_OFF', 0);
define ('MYSQLI_DEBUG_TRACE_ENABLED', 0);
define ('MYSQLI_SERVER_QUERY_NO_GOOD_INDEX_USED', 16);
define ('MYSQLI_SERVER_QUERY_NO_INDEX_USED', 32);
define ('MYSQLI_SERVER_QUERY_WAS_SLOW', 1024);
define ('MYSQLI_REFRESH_GRANT', 1);
define ('MYSQLI_REFRESH_LOG', 2);
define ('MYSQLI_REFRESH_TABLES', 4);
define ('MYSQLI_REFRESH_HOSTS', 8);
define ('MYSQLI_REFRESH_STATUS', 16);
define ('MYSQLI_REFRESH_THREADS', 32);
define ('MYSQLI_REFRESH_SLAVE', 64);
define ('MYSQLI_REFRESH_MASTER', 128);
define ('MYSQLI_REFRESH_BACKUP_LOG', 2097152);

// End of mysqli v.0.1
?>
