<?php

// Start of PDO v.1.0.4dev

/**
 * Represents an error raised by PDO. You should not throw a
 * PDOException from your own code.
 * See Exceptions for more
 * information about Exceptions in PHP.
 * @link http://php.net/manual/en/class.pdoexception.php
 */
class PDOException extends RuntimeException  {
	protected $message;
	protected $code;
	protected $file;
	protected $line;
	public $errorInfo;


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
 * Represents a connection between PHP and a database server.
 * @link http://php.net/manual/en/class.pdo.php
 */
class PDO  {

	/**
	 * Represents a boolean data type.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const PARAM_BOOL = 5;

	/**
	 * Represents the SQL NULL data type.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const PARAM_NULL = 0;

	/**
	 * Represents the SQL INTEGER data type.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const PARAM_INT = 1;

	/**
	 * Represents the SQL CHAR, VARCHAR, or other string data type.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const PARAM_STR = 2;

	/**
	 * Represents the SQL large object data type.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const PARAM_LOB = 3;

	/**
	 * Represents a recordset type. Not currently supported by any drivers.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const PARAM_STMT = 4;

	/**
	 * Specifies that the parameter is an INOUT parameter for a stored
	 * procedure. You must bitwise-OR this value with an explicit
	 * PDO::PARAM_* data type.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const PARAM_INPUT_OUTPUT = 2147483648;

	/**
	 * Allocation event
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const PARAM_EVT_ALLOC = 0;

	/**
	 * Deallocation event
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const PARAM_EVT_FREE = 1;

	/**
	 * Event triggered prior to execution of a prepared statement.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const PARAM_EVT_EXEC_PRE = 2;

	/**
	 * Event triggered subsequent to execution of a prepared statement.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const PARAM_EVT_EXEC_POST = 3;

	/**
	 * Event triggered prior to fetching a result from a resultset.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const PARAM_EVT_FETCH_PRE = 4;

	/**
	 * Event triggered subsequent to fetching a result from a resultset.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const PARAM_EVT_FETCH_POST = 5;

	/**
	 * Event triggered during bound parameter registration
	 * allowing the driver to normalize the parameter name.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const PARAM_EVT_NORMALIZE = 6;

	/**
	 * Specifies that the fetch method shall return each row as an object with
	 * variable names that correspond to the column names returned in the result
	 * set. PDO::FETCH_LAZY creates the object variable names as they are accessed.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const FETCH_LAZY = 1;

	/**
	 * Specifies that the fetch method shall return each row as an array indexed
	 * by column name as returned in the corresponding result set. If the result
	 * set contains multiple columns with the same name,
	 * PDO::FETCH_ASSOC returns
	 * only a single value per column name.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const FETCH_ASSOC = 2;

	/**
	 * Specifies that the fetch method shall return each row as an array indexed
	 * by column number as returned in the corresponding result set, starting at
	 * column 0.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const FETCH_NUM = 3;

	/**
	 * Specifies that the fetch method shall return each row as an array indexed
	 * by both column name and number as returned in the corresponding result set,
	 * starting at column 0.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const FETCH_BOTH = 4;

	/**
	 * Specifies that the fetch method shall return each row as an object with
	 * property names that correspond to the column names returned in the result
	 * set.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const FETCH_OBJ = 5;

	/**
	 * Specifies that the fetch method shall return TRUE and assign the values of
	 * the columns in the result set to the PHP variables to which they were
	 * bound with the PDOStatement::bindParam or
	 * PDOStatement::bindColumn methods.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const FETCH_BOUND = 6;

	/**
	 * Specifies that the fetch method shall return only a single requested
	 * column from the next row in the result set.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const FETCH_COLUMN = 7;

	/**
	 * Specifies that the fetch method shall return a new instance of the
	 * requested class, mapping the columns to named properties in the class.
	 * The magic
	 * __set
	 * method is called if the property doesn't exist in the requested class
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const FETCH_CLASS = 8;

	/**
	 * Specifies that the fetch method shall update an existing instance of the
	 * requested class, mapping the columns to named properties in the class.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const FETCH_INTO = 9;
	const FETCH_FUNC = 10;
	const FETCH_GROUP = 65536;
	const FETCH_UNIQUE = 196608;

	/**
	 * Fetch into an array where the 1st column is a key and all subsequent
	 * columns are values. Available since PHP 5.2.3.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const FETCH_KEY_PAIR = 12;

	/**
	 * Determine the class name from the value of first column.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const FETCH_CLASSTYPE = 262144;

	/**
	 * As PDO::FETCH_INTO but object is provided as a serialized string.
	 * Available since PHP 5.1.0.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const FETCH_SERIALIZE = 524288;

	/**
	 * Available since PHP 5.2.0
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const FETCH_PROPS_LATE = 1048576;

	/**
	 * Specifies that the fetch method shall return each row as an array indexed
	 * by column name as returned in the corresponding result set. If the result
	 * set contains multiple columns with the same name,
	 * PDO::FETCH_NAMED returns
	 * an array of values per column name.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const FETCH_NAMED = 11;

	/**
	 * If this value is false, PDO attempts to disable autocommit so that the
	 * connection begins a transaction.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const ATTR_AUTOCOMMIT = 0;

	/**
	 * Setting the prefetch size allows you to balance speed against memory
	 * usage for your application. Not all database/driver combinations support
	 * setting of the prefetch size. A larger prefetch size results in
	 * increased performance at the cost of higher memory usage.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const ATTR_PREFETCH = 1;

	/**
	 * Sets the timeout value in seconds for communications with the database.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const ATTR_TIMEOUT = 2;

	/**
	 * See the Errors and error
	 * handling section for more information about this attribute.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const ATTR_ERRMODE = 3;

	/**
	 * This is a read only attribute; it will return information about the
	 * version of the database server to which PDO is connected.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const ATTR_SERVER_VERSION = 4;

	/**
	 * This is a read only attribute; it will return information about the
	 * version of the client libraries that the PDO driver is using.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const ATTR_CLIENT_VERSION = 5;

	/**
	 * This is a read only attribute; it will return some meta information about the
	 * database server to which PDO is connected.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const ATTR_SERVER_INFO = 6;
	const ATTR_CONNECTION_STATUS = 7;

	/**
	 * Force column names to a specific case specified by the PDO::CASE_*
	 * constants.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const ATTR_CASE = 8;

	/**
	 * Get or set the name to use for a cursor. Most useful when using
	 * scrollable cursors and positioned updates.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const ATTR_CURSOR_NAME = 9;

	/**
	 * Selects the cursor type. PDO currently supports either
	 * PDO::CURSOR_FWDONLY and 
	 * PDO::CURSOR_SCROLL. Stick with
	 * PDO::CURSOR_FWDONLY unless you know that you need a
	 * scrollable cursor.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const ATTR_CURSOR = 10;

	/**
	 * Convert empty strings to SQL NULL values on data fetches.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const ATTR_ORACLE_NULLS = 11;

	/**
	 * Request a persistent connection, rather than creating a new connection.
	 * See Connections and Connection
	 * management for more information on this attribute.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const ATTR_PERSISTENT = 12;
	const ATTR_STATEMENT_CLASS = 13;

	/**
	 * Prepend the containing table name to each column name returned in the
	 * result set. The table name and column name are separated by a decimal (.)
	 * character. Support of this attribute is at the driver level; it may not
	 * be supported by your driver.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const ATTR_FETCH_TABLE_NAMES = 14;

	/**
	 * Prepend the containing catalog name to each column name returned in the
	 * result set. The catalog name and column name are separated by a decimal
	 * (.) character. Support of this attribute is at the driver level; it may
	 * not be supported by your driver.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const ATTR_FETCH_CATALOG_NAMES = 15;

	/**
	 * Returns the name of the driver.
	 * <p>
	 * using PDO::ATTR_DRIVER_NAME
	 * getAttribute(PDO::ATTR_DRIVER_NAME) == 'mysql') {
	 * echo "Running on mysql; doing something mysql specific here\n";
	 * }
	 * ?>
	 * ]]>
	 * </p>
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const ATTR_DRIVER_NAME = 16;
	const ATTR_STRINGIFY_FETCHES = 17;
	const ATTR_MAX_COLUMN_LEN = 18;

	/**
	 * Available since PHP 5.1.3.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const ATTR_EMULATE_PREPARES = 20;

	/**
	 * Available since PHP 5.2.0
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const ATTR_DEFAULT_FETCH_MODE = 19;

	/**
	 * Do not raise an error or exception if an error occurs. The developer is
	 * expected to explicitly check for errors. This is the default mode.
	 * See Errors and error handling
	 * for more information about this attribute.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const ERRMODE_SILENT = 0;

	/**
	 * Issue a PHP E_WARNING message if an error occurs.
	 * See Errors and error handling
	 * for more information about this attribute.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const ERRMODE_WARNING = 1;

	/**
	 * Throw a PDOException if an error occurs.
	 * See Errors and error handling
	 * for more information about this attribute.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const ERRMODE_EXCEPTION = 2;

	/**
	 * Leave column names as returned by the database driver.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const CASE_NATURAL = 0;

	/**
	 * Force column names to lower case.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const CASE_LOWER = 2;

	/**
	 * Force column names to upper case.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const CASE_UPPER = 1;
	const NULL_NATURAL = 0;
	const NULL_EMPTY_STRING = 1;
	const NULL_TO_STRING = 2;

	/**
	 * Corresponds to SQLSTATE '00000', meaning that the SQL statement was
	 * successfully issued with no errors or warnings. This constant is for
	 * your convenience when checking PDO::errorCode or
	 * PDOStatement::errorCode to determine if an error
	 * occurred. You will usually know if this is the case by examining the
	 * return code from the method that raised the error condition anyway.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const ERR_NONE = 00000;

	/**
	 * Fetch the next row in the result set. Valid only for scrollable cursors.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const FETCH_ORI_NEXT = 0;

	/**
	 * Fetch the previous row in the result set. Valid only for scrollable
	 * cursors.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const FETCH_ORI_PRIOR = 1;

	/**
	 * Fetch the first row in the result set. Valid only for scrollable cursors.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const FETCH_ORI_FIRST = 2;

	/**
	 * Fetch the last row in the result set. Valid only for scrollable cursors.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const FETCH_ORI_LAST = 3;

	/**
	 * Fetch the requested row by row number from the result set. Valid only
	 * for scrollable cursors.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const FETCH_ORI_ABS = 4;

	/**
	 * Fetch the requested row by relative position from the current position
	 * of the cursor in the result set. Valid only for scrollable cursors.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const FETCH_ORI_REL = 5;

	/**
	 * Create a PDOStatement object with a forward-only cursor. This is the
	 * default cursor choice, as it is the fastest and most common data access
	 * pattern in PHP.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const CURSOR_FWDONLY = 0;

	/**
	 * Create a PDOStatement object with a scrollable cursor. Pass the
	 * PDO::FETCH_ORI_* constants to control the rows fetched from the result set.
	 * @link http://php.net/manual/en/pdo.constants.php
	 */
	const CURSOR_SCROLL = 1;

	/**
	 * If this attribute is set to true on a
	 * PDOStatement, the MySQL driver will use the
	 * buffered versions of the MySQL API. If you're writing portable code, you
	 * should use PDOStatement::fetchAll instead.
	 * <p>
	 * Forcing queries to be buffered in mysql
	 * getAttribute(PDO::ATTR_DRIVER_NAME) == 'mysql') {
	 * $stmt = $db->prepare('select * from foo',
	 * array(PDO::MYSQL_ATTR_USE_BUFFERED_QUERY => true));
	 * } else {
	 * die("my application only works with mysql; I should use \$stmt->fetchAll() instead");
	 * }
	 * ?>
	 * ]]>
	 * </p>
	 * @link http://php.net/manual/en/pdo-mysql.constants.php
	 */
	const MYSQL_ATTR_USE_BUFFERED_QUERY = 1000;

	/**
	 * <p>
	 * Enable LOAD LOCAL INFILE.
	 * </p>
	 * @link http://php.net/manual/en/pdo-mysql.constants.php
	 */
	const MYSQL_ATTR_LOCAL_INFILE = 1001;

	/**
	 * <p>
	 * Command to execute when connecting to the MySQL server. Will
	 * automatically be re-executed when reconnecting.
	 * </p>
	 * @link http://php.net/manual/en/pdo-mysql.constants.php
	 */
	const MYSQL_ATTR_INIT_COMMAND = 1002;

	/**
	 * <p>
	 * Perform direct queries, don't use prepared statements.
	 * </p>
	 * @link http://php.net/manual/en/pdo-mysql.constants.php
	 */
	const MYSQL_ATTR_DIRECT_QUERY = 1003;
	const MYSQL_ATTR_FOUND_ROWS = 1004;
	const MYSQL_ATTR_IGNORE_SPACE = 1005;
	const ODBC_ATTR_USE_CURSOR_LIBRARY = 1000;
	const ODBC_ATTR_ASSUME_UTF8 = 1001;
	const ODBC_SQL_USE_IF_NEEDED = 0;
	const ODBC_SQL_USE_DRIVER = 2;
	const ODBC_SQL_USE_ODBC = 1;
	const PGSQL_ATTR_DISABLE_NATIVE_PREPARED_STATEMENT = 1000;


	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.1.0)<br/>
	 * Creates a PDO instance representing a connection to a database
	 * @link http://php.net/manual/en/pdo.construct.php
	 * @param $dsn
	 * @param $username [optional]
	 * @param $passwd [optional]
	 * @param $options [optional]
	 */
	public function __construct ($dsn, $username, $passwd, $options) {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.1.0)<br/>
	 * Prepares a statement for execution and returns a statement object
	 * @link http://php.net/manual/en/pdo.prepare.php
	 * @param string $statement <p>
	 * This must be a valid SQL statement for the target database server.
	 * </p>
	 * @param array $driver_options [optional] <p>
	 * This array holds one or more key=&gt;value pairs to set
	 * attribute values for the PDOStatement object that this method
	 * returns. You would most commonly use this to set the
	 * PDO::ATTR_CURSOR value to
	 * PDO::CURSOR_SCROLL to request a scrollable cursor.
	 * Some drivers have driver specific options that may be set at
	 * prepare-time.
	 * </p>
	 * @return PDOStatement If the database server successfully prepares the statement,
	 * PDO::prepare returns a
	 * PDOStatement object.
	 * If the database server cannot successfully prepare the statement,
	 * PDO::prepare returns false or emits
	 * PDOException (depending on error handling).
	 * </p>
	 * <p>
	 * Emulated prepared statements does not communicate with the database server
	 * so PDO::prepare does not check the statement.
	 */
	public function prepare ($statement, array $driver_options = null) {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.1.0)<br/>
	 * Initiates a transaction
	 * @link http://php.net/manual/en/pdo.begintransaction.php
	 * @return bool Returns true on success or false on failure.
	 */
	public function beginTransaction () {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.1.0)<br/>
	 * Commits a transaction
	 * @link http://php.net/manual/en/pdo.commit.php
	 * @return bool Returns true on success or false on failure.
	 */
	public function commit () {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.1.0)<br/>
	 * Rolls back a transaction
	 * @link http://php.net/manual/en/pdo.rollback.php
	 * @return bool Returns true on success or false on failure.
	 */
	public function rollBack () {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.1.0)<br/>
	 * Set an attribute
	 * @link http://php.net/manual/en/pdo.setattribute.php
	 * @param int $attribute 
	 * @param mixed $value 
	 * @return bool Returns true on success or false on failure.
	 */
	public function setAttribute ($attribute, $value) {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.1.0)<br/>
	 * Execute an SQL statement and return the number of affected rows
	 * @link http://php.net/manual/en/pdo.exec.php
	 * @param string $statement <p>
	 * The SQL statement to prepare and execute.
	 * </p>
	 * <p>
	 * Data inside the query should be properly escaped.
	 * </p>
	 * @return int PDO::exec returns the number of rows that were modified
	 * or deleted by the SQL statement you issued. If no rows were affected, 
	 * PDO::exec returns 0. 
	 * </p>
	 * &return.falseproblem;
	 * <p>
	 * The following example incorrectly relies on the return value of
	 * PDO::exec, wherein a statement that affected 0 rows
	 * results in a call to die:
	 * exec() or die(print_r($db->errorInfo(), true));
	 * ?>
	 * ]]>
	 */
	public function exec ($statement) {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.2.0)<br/>
	 * Executes an SQL statement, returning a result set as a PDOStatement object
	 * @link http://php.net/manual/en/pdo.query.php
	 * @param string $statement <p>
	 * The SQL statement to prepare and execute.
	 * </p>
	 * <p>
	 * Data inside the query should be properly escaped.
	 * </p>
	 * @return PDOStatement PDO::query returns a PDOStatement object, or false
	 * on failure.
	 */
	public function query ($statement) {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.1.0)<br/>
	 * Returns the ID of the last inserted row or sequence value
	 * @link http://php.net/manual/en/pdo.lastinsertid.php
	 * @param string $name [optional] <p>
	 * Name of the sequence object from which the ID should be returned.
	 * </p>
	 * @return string If a sequence name was not specified for the name
	 * parameter, PDO::lastInsertId returns a
	 * string representing the row ID of the last row that was inserted into
	 * the database.
	 * </p>
	 * <p>
	 * If a sequence name was specified for the name
	 * parameter, PDO::lastInsertId returns a
	 * string representing the last value retrieved from the specified sequence
	 * object.
	 * </p>
	 * <p>
	 * If the PDO driver does not support this capability,
	 * PDO::lastInsertId triggers an
	 * IM001 SQLSTATE.
	 */
	public function lastInsertId ($name = null) {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.1.0)<br/>
	 * Fetch the SQLSTATE associated with the last operation on the database handle
	 * @link http://php.net/manual/en/pdo.errorcode.php
	 * @return mixed a SQLSTATE, a five characters alphanumeric identifier defined in
	 * the ANSI SQL-92 standard. Briefly, an SQLSTATE consists of a
	 * two characters class value followed by a three characters subclass value. A
	 * class value of 01 indicates a warning and is accompanied by a return code
	 * of SQL_SUCCESS_WITH_INFO. Class values other than '01', except for the
	 * class 'IM', indicate an error. The class 'IM' is specific to warnings
	 * and errors that derive from the implementation of PDO (or perhaps ODBC,
	 * if you're using the ODBC driver) itself. The subclass value '000' in any
	 * class indicates that there is no subclass for that SQLSTATE.
	 * </p>
	 * <p>
	 * PDO::errorCode only retrieves error codes for operations
	 * performed directly on the database handle. If you create a PDOStatement
	 * object through PDO::prepare or
	 * PDO::query and invoke an error on the statement
	 * handle, PDO::errorCode will not reflect that error.
	 * You must call PDOStatement::errorCode to return the error
	 * code for an operation performed on a particular statement handle.
	 * </p>
	 * <p>
	 * Returns &null; if no operation has been run on the database handle.
	 */
	public function errorCode () {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.1.0)<br/>
	 * Fetch extended error information associated with the last operation on the database handle
	 * @link http://php.net/manual/en/pdo.errorinfo.php
	 * @return array PDO::errorInfo returns an array of error information
	 * about the last operation performed by this database handle. The array
	 * consists of the following fields:
	 * <tr valign="top">
	 * <td>Element</td>
	 * <td>Information</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>0</td>
	 * <td>SQLSTATE error code (a five characters alphanumeric identifier defined
	 * in the ANSI SQL standard).</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>1</td>
	 * <td>Driver-specific error code.</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>2</td>
	 * <td>Driver-specific error message.</td>
	 * </tr>
	 * </p>
	 * <p>
	 * If the SQLSTATE error code is not set or there is no driver-specific
	 * error, the elements following element 0 will be set to &null;.
	 * </p>
	 * <p>
	 * PDO::errorInfo only retrieves error information for
	 * operations performed directly on the database handle. If you create a
	 * PDOStatement object through PDO::prepare or
	 * PDO::query and invoke an error on the statement
	 * handle, PDO::errorInfo will not reflect the error
	 * from the statement handle. You must call
	 * PDOStatement::errorInfo to return the error
	 * information for an operation performed on a particular statement handle.
	 */
	public function errorInfo () {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.2.0)<br/>
	 * Retrieve a database connection attribute
	 * @link http://php.net/manual/en/pdo.getattribute.php
	 * @param int $attribute <p>
	 * One of the PDO::ATTR_* constants. The constants that
	 * apply to database connections are as follows:
	 * PDO::ATTR_AUTOCOMMIT
	 * PDO::ATTR_CASE
	 * PDO::ATTR_CLIENT_VERSION
	 * PDO::ATTR_CONNECTION_STATUS
	 * PDO::ATTR_DRIVER_NAME
	 * PDO::ATTR_ERRMODE
	 * PDO::ATTR_ORACLE_NULLS
	 * PDO::ATTR_PERSISTENT
	 * PDO::ATTR_PREFETCH
	 * PDO::ATTR_SERVER_INFO
	 * PDO::ATTR_SERVER_VERSION
	 * PDO::ATTR_TIMEOUT
	 * </p>
	 * @return mixed A successful call returns the value of the requested PDO attribute.
	 * An unsuccessful call returns null.
	 */
	public function getAttribute ($attribute) {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.2.1)<br/>
	 * Quotes a string for use in a query.
	 * @link http://php.net/manual/en/pdo.quote.php
	 * @param string $string <p>
	 * The string to be quoted.
	 * </p>
	 * @param int $parameter_type [optional] <p>
	 * Provides a data type hint for drivers that have alternate quoting styles.
	 * </p>
	 * @return string a quoted string that is theoretically safe to pass into an
	 * SQL statement. Returns false if the driver does not support quoting in
	 * this way.
	 */
	public function quote ($string, $parameter_type = null) {}

	final public function __wakeup () {}

	final public function __sleep () {}

	/**
	 * (PHP 5 &gt;= 5.1.3, PECL pdo &gt;= 1.0.3)<br/>
	 * Return an array of available PDO drivers
	 * @link http://php.net/manual/en/pdo.getavailabledrivers.php
	 * @return array PDO::getAvailableDrivers returns an array of PDO driver names. If
	 * no drivers are available, it returns an empty array.
	 */
	public static function getAvailableDrivers () {}

}

/**
 * Represents a prepared statement and, after the statement is executed, an 
 * associated result set.
 * @link http://php.net/manual/en/class.pdostatement.php
 */
class PDOStatement implements Traversable {
	/**
	 * @var string
	 */
	public $queryString;


	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.1.0)<br/>
	 * Executes a prepared statement
	 * @link http://php.net/manual/en/pdostatement.execute.php
	 * @param array $input_parameters [optional] <p>
	 * An array of values with as many elements as there are bound
	 * parameters in the SQL statement being executed.
	 * All values are treated as PDO::PARAM_STR.
	 * </p>
	 * <p>
	 * You cannot bind multiple values to a single parameter; for example,
	 * you cannot bind two values to a single named parameter in an IN()
	 * clause.
	 * </p>
	 * @return bool Returns true on success or false on failure.
	 */
	public function execute (array $input_parameters = null) {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.1.0)<br/>
	 * Fetches the next row from a result set
	 * @link http://php.net/manual/en/pdostatement.fetch.php
	 * @param int $fetch_style [optional] <p>
	 * Controls how the next row will be returned to the caller. This value
	 * must be one of the PDO::FETCH_* constants,
	 * defaulting to PDO::FETCH_BOTH.
	 * <p>
	 * PDO::FETCH_ASSOC: returns an array indexed by column
	 * name as returned in your result set
	 * </p>
	 * @param int $cursor_orientation [optional] <p>
	 * For a PDOStatement object representing a scrollable cursor, this
	 * value determines which row will be returned to the caller. This value
	 * must be one of the PDO::FETCH_ORI_* constants,
	 * defaulting to PDO::FETCH_ORI_NEXT. To request a
	 * scrollable cursor for your PDOStatement object, you must set the
	 * PDO::ATTR_CURSOR attribute to
	 * PDO::CURSOR_SCROLL when you prepare the SQL
	 * statement with PDO::prepare.
	 * </p>
	 * @param int $cursor_offset [optional] 
	 * @return mixed The return value of this function on success depends on the fetch type. In
	 * all cases, false is returned on failure.
	 */
	public function fetch ($fetch_style = null, $cursor_orientation = null, $cursor_offset = null) {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.1.0)<br/>
	 * Binds a parameter to the specified variable name
	 * @link http://php.net/manual/en/pdostatement.bindparam.php
	 * @param mixed $parameter <p>
	 * Parameter identifier. For a prepared statement using named
	 * placeholders, this will be a parameter name of the form
	 * :name. For a prepared statement using
	 * question mark placeholders, this will be the 1-indexed position of
	 * the parameter. 
	 * </p>
	 * @param mixed $variable <p>
	 * Name of the PHP variable to bind to the SQL statement parameter.
	 * </p>
	 * @param int $data_type [optional] <p>
	 * Explicit data type for the parameter using the PDO::PARAM_*
	 * constants.
	 * To return an INOUT parameter from a stored procedure, 
	 * use the bitwise OR operator to set the PDO::PARAM_INPUT_OUTPUT bits
	 * for the data_type parameter.
	 * </p>
	 * @param int $length [optional] <p>
	 * Length of the data type. To indicate that a parameter is an OUT
	 * parameter from a stored procedure, you must explicitly set the
	 * length.
	 * </p>
	 * @param mixed $driver_options [optional] <p>
	 * </p>
	 * @return bool Returns true on success or false on failure.
	 */
	public function bindParam ($parameter, &$variable, $data_type = null, $length = null, $driver_options = null) {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.1.0)<br/>
	 * Bind a column to a PHP variable
	 * @link http://php.net/manual/en/pdostatement.bindcolumn.php
	 * @param mixed $column <p>
	 * Number of the column (1-indexed) or name of the column in the result set.
	 * If using the column name, be aware that the name should match the
	 * case of the column, as returned by the driver.
	 * </p>
	 * @param mixed $param <p>
	 * Name of the PHP variable to which the column will be bound.
	 * </p>
	 * @param int $type [optional] <p>
	 * Data type of the parameter, specified by the PDO::PARAM_* constants.
	 * </p>
	 * @param int $maxlen [optional] <p>
	 * A hint for pre-allocation.
	 * </p>
	 * @param mixed $driverdata [optional] <p>
	 * Optional parameter(s) for the driver.
	 * </p>
	 * @return bool Returns true on success or false on failure.
	 */
	public function bindColumn ($column, &$param, $type = null, $maxlen = null, $driverdata = null) {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 1.0.0)<br/>
	 * Binds a value to a parameter
	 * @link http://php.net/manual/en/pdostatement.bindvalue.php
	 * @param mixed $parameter <p>
	 * Parameter identifier. For a prepared statement using named
	 * placeholders, this will be a parameter name of the form
	 * :name. For a prepared statement using
	 * question mark placeholders, this will be the 1-indexed position of
	 * the parameter. 
	 * </p>
	 * @param mixed $value <p>
	 * The value to bind to the parameter.
	 * </p>
	 * @param int $data_type [optional] <p>
	 * Explicit data type for the parameter using the PDO::PARAM_*
	 * constants.
	 * </p>
	 * @return bool Returns true on success or false on failure.
	 */
	public function bindValue ($parameter, $value, $data_type = null) {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.1.0)<br/>
	 * Returns the number of rows affected by the last SQL statement
	 * @link http://php.net/manual/en/pdostatement.rowcount.php
	 * @return int the number of rows.
	 */
	public function rowCount () {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.9.0)<br/>
	 * Returns a single column from the next row of a result set
	 * @link http://php.net/manual/en/pdostatement.fetchcolumn.php
	 * @param int $column_number [optional] <p>
	 * 0-indexed number of the column you wish to retrieve from the row. If
	 * no value is supplied, PDOStatement::fetchColumn
	 * fetches the first column.
	 * </p>
	 * @return string PDOStatement::fetchColumn returns a single column
	 * in the next row of a result set.
	 * </p>
	 * <p>
	 * There is no way to return another column from the same row if you
	 * use PDOStatement::fetchColumn to retrieve data.
	 */
	public function fetchColumn ($column_number = null) {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.1.0)<br/>
	 * Returns an array containing all of the result set rows
	 * @link http://php.net/manual/en/pdostatement.fetchall.php
	 * @param int $fetch_style [optional] <p>
	 * Controls the contents of the returned array as documented in
	 * PDOStatement::fetch.
	 * </p>
	 * <p>
	 * To return an array consisting of all values of a single column from
	 * the result set, specify PDO::FETCH_COLUMN. You
	 * can specify which column you want with the
	 * column-index parameter.
	 * </p>
	 * <p>
	 * To fetch only the unique values of a single column from the result set,
	 * bitwise-OR PDO::FETCH_COLUMN with
	 * PDO::FETCH_UNIQUE.
	 * </p>
	 * <p>
	 * To return an associative array grouped by the values of a specified
	 * column, bitwise-OR PDO::FETCH_COLUMN with
	 * PDO::FETCH_GROUP.
	 * </p>
	 * @param int $column_index [optional] <p>
	 * Returns the indicated 0-indexed column when the value of
	 * fetch_style is
	 * PDO::FETCH_COLUMN.
	 * </p>
	 * @param array $ctor_args [optional] <p>
	 * Arguments of custom class constructor.
	 * </p>
	 * @return array PDOStatement::fetchAll returns an array containing
	 * all of the remaining rows in the result set. The array represents each
	 * row as either an array of column values or an object with properties
	 * corresponding to each column name.
	 * </p>
	 * <p>
	 * Using this method to fetch large result sets will result in a heavy
	 * demand on system and possibly network resources. Rather than retrieving
	 * all of the data and manipulating it in PHP, consider using the database
	 * server to manipulate the result sets. For example, use the WHERE and
	 * SORT BY clauses in SQL to restrict results before retrieving and
	 * processing them with PHP.
	 */
	public function fetchAll ($fetch_style = null, $column_index = null, array $ctor_args = null) {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.2.4)<br/>
	 * Fetches the next row and returns it as an object.
	 * @link http://php.net/manual/en/pdostatement.fetchobject.php
	 * @param string $class_name [optional] <p>
	 * Name of the created class.
	 * </p>
	 * @param array $ctor_args [optional] <p>
	 * Elements of this array are passed to the constructor.
	 * </p>
	 * @return mixed an instance of the required class with property names that
	 * correspond to the column names &return.falseforfailure;.
	 */
	public function fetchObject ($class_name = null, array $ctor_args = null) {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.1.0)<br/>
	 * Fetch the SQLSTATE associated with the last operation on the statement handle
	 * @link http://php.net/manual/en/pdostatement.errorcode.php
	 * @return string Identical to PDO::errorCode, except that 
	 * PDOStatement::errorCode only retrieves error codes
	 * for operations performed with PDOStatement objects.
	 */
	public function errorCode () {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.1.0)<br/>
	 * Fetch extended error information associated with the last operation on the statement handle
	 * @link http://php.net/manual/en/pdostatement.errorinfo.php
	 * @return array PDOStatement::errorInfo returns an array of
	 * error information about the last operation performed by this
	 * statement handle. The array consists of the following fields:
	 * <tr valign="top">
	 * <td>Element</td>
	 * <td>Information</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>0</td>
	 * <td>SQLSTATE error code (a five characters alphanumeric identifier defined
	 * in the ANSI SQL standard).</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>1</td>
	 * <td>Driver specific error code.</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>2</td>
	 * <td>Driver specific error message.</td>
	 * </tr>
	 */
	public function errorInfo () {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.2.0)<br/>
	 * Set a statement attribute
	 * @link http://php.net/manual/en/pdostatement.setattribute.php
	 * @param int $attribute 
	 * @param mixed $value 
	 * @return bool Returns true on success or false on failure.
	 */
	public function setAttribute ($attribute, $value) {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.2.0)<br/>
	 * Retrieve a statement attribute
	 * @link http://php.net/manual/en/pdostatement.getattribute.php
	 * @param int $attribute 
	 * @return mixed the attribute value.
	 */
	public function getAttribute ($attribute) {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.2.0)<br/>
	 * Returns the number of columns in the result set
	 * @link http://php.net/manual/en/pdostatement.columncount.php
	 * @return int the number of columns in the result set represented by the
	 * PDOStatement object. If there is no result set,
	 * PDOStatement::columnCount returns 0.
	 */
	public function columnCount () {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.2.0)<br/>
	 * Returns metadata for a column in a result set
	 * @link http://php.net/manual/en/pdostatement.getcolumnmeta.php
	 * @param int $column <p>
	 * The 0-indexed column in the result set.
	 * </p>
	 * @return array an associative array containing the following values representing
	 * the metadata for a single column:
	 * </p>
	 * <table>
	 * Column metadata
	 * <tr valign="top">
	 * <td>Name</td>
	 * <td>Value</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>native_type</td>
	 * <td>The PHP native type used to represent the column value.</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>driver:decl_type</td>
	 * <td>The SQL type used to represent the column value in the database.
	 * If the column in the result set is the result of a function, this value
	 * is not returned by PDOStatement::getColumnMeta.
	 * </td>
	 * </tr>
	 * <tr valign="top">
	 * <td>flags</td>
	 * <td>Any flags set for this column.</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>name</td>
	 * <td>The name of this column as returned by the database.</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>table</td>
	 * <td>The name of this column's table as returned by the database.</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>len</td>
	 * <td>The length of this column. Normally -1 for
	 * types other than floating point decimals.</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>precision</td>
	 * <td>The numeric precision of this column. Normally
	 * 0 for types other than floating point
	 * decimals.</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>pdo_type</td>
	 * <td>The type of this column as represented by the
	 * PDO::PARAM_* constants.</td>
	 * </tr>
	 * </table>
	 * <p>
	 * Returns false if the requested column does not exist in the result set,
	 * or if no result set exists.
	 */
	public function getColumnMeta ($column) {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.2.0)<br/>
	 * Set the default fetch mode for this statement
	 * @link http://php.net/manual/en/pdostatement.setfetchmode.php
	 * @param int $mode <p>
	 * The fetch mode must be one of the PDO::FETCH_* constants.
	 * </p>
	 * @return bool 1 on success&return.falseforfailure;.
	 */
	public function setFetchMode ($mode) {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.2.0)<br/>
	 * Advances to the next rowset in a multi-rowset statement handle
	 * @link http://php.net/manual/en/pdostatement.nextrowset.php
	 * @return bool Returns true on success or false on failure.
	 */
	public function nextRowset () {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.9.0)<br/>
	 * Closes the cursor, enabling the statement to be executed again.
	 * @link http://php.net/manual/en/pdostatement.closecursor.php
	 * @return bool Returns true on success or false on failure.
	 */
	public function closeCursor () {}

	/**
	 * (PHP 5 &gt;= 5.1.0, PECL pdo &gt;= 0.9.0)<br/>
	 * Dump a SQL prepared command
	 * @link http://php.net/manual/en/pdostatement.debugdumpparams.php
	 * @return bool 
	 */
	public function debugDumpParams () {}

	final public function __wakeup () {}

	final public function __sleep () {}

}

final class PDORow  {
}

function pdo_drivers () {}

// End of PDO v.1.0.4dev
?>
