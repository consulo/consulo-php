<?php
/*
 * Mongo extension stubs
 * Gathered from http://www.php.net/manual/en/book.mongo.php
 * Maintainer: Alexander Makarov, sam@rmcreative.ru
 */

/**
 * The connection point between MongoDB and PHP. 
 * This class is used to initiate a connection and for database server commands.
 * @link http://www.php.net/manual/en/class.mongo.php
 */
class Mongo {
	/**
	 * Host to connect to if no host is given.
	 */
    const DEFAULT_HOST = 'localhost';

	/**
	 * Port to connect to if no port is given.
	 */
    const DEFAULT_PORT = 27017;

	/**
	 * PHP driver version. May be suffixed with "+" or "-" if it is in-between versions.
	 */
    const VERSION = '1.0.4+';

    /**
    * @var $connected
    */
    public $connected = false;
    
    /**
    * @var $server
    */
    protected $server;
    
    /**
    * @var $persistent
    */
    protected $persistent = false;

    /**
	 * Creates a new database connection object
	 *
	 * @param string $server The server name.
	 * @param array $options An array of options for the connection.
	 * @link http://php.net/manual/en/mongo.construct.php
	 *
	 * @throws MongoConnectionException
	 * @return Mongo A new database connection object.
	 */
    public function __construct($server = "mongodb://localhost:27017", array $options = array("connect" => TRUE)) {}
    
   /**
	* Connects to a database server
	*
	* @link http://www.php.net/manual/en/mongo.connect.php
	* 
	* @throws MongoConnectionException
    * @return boolean If the connection was successful.
    */
    public function connect() {}
    
   /**
	* Connects to paired database server
	* @deprecated Pass a string of the form "mongodb://server1,server2" to the constructor instead of using this method.
	* @link http://www.php.net/manual/en/mongo.pairconnect.php
	* @throws MongoConnectionException
    * @return boolean
    */
    public function pairConnect() {}
    
    /**
	 * Creates a persistent connection with a database server
	 * @link http://www.php.net/manual/en/mongo.persistconnect.php
	 * @deprecated Pass array("persist" => $id) to the constructor instead of using this method.
	 * @param string $username A username used to identify the connection.
	 * @param string $password A password used to identify the connection.
	 * @throws MongoConnectionException
	 * @return boolean If the connection was successful.
	 */
    public function persistConnect($username = "", $password = "") {}
    
    /**
	 * Creates a persistent connection with paired database servers
	 * @deprecated Pass "mongodb://server1,server2" and array("persist" => $id) to the constructor instead of using this method.
	 * @link http://www.php.net/manual/en/mongo.pairpersistconnect.php
	 * @param string $username A username used to identify the connection.
	 * @param string $password A password used to identify the connection.
	 * @throws MongoConnectionException
	 * @return boolean If the connection was successful.
	 */
    public function pairPersistConnect($username = "", $password = "") {}
    
   /**
	* Connects with a database server
	*
	* @link http://www.php.net/manual/en/mongo.connectutil.php
	* @throws MongoConnectionException
    * @return boolean If the connection was successful.
    */
    protected function connectUtil() {}
    
   /**
	* String representation of this connection
	* @link http://www.php.net/manual/en/mongo.tostring.php
    * @return string Returns hostname and port for this connection.
    */
    public function __toString() {}
    
   /**
	* Gets a database
	*
	* @link http://www.php.net/manual/en/mongo.get.php  
    * @param string $name The database name.
	* @throws Exception
    * @return MongoDB
    */
    public function __get($name) {}
    
    /**
	 * Gets a database
	 * @link http://www.php.net/manual/en/mongo.selectdb.php
	 * @param string $dbname The database name.
	 * @throws InvalidArgumentException
	 * @return MongoDB Returns a new db object.
	 */
    public function selectDB($dbname) {}
    
    /**
	 * Gets a database collection
	 * @link http://www.php.net/manual/en/mongo.selectcollection.php
	 * @param string|MongoDB $db The database name.
	 * @param string $collection The collection name.
	 * @throws InvalidArgumentException
	 * @return MongoCollection Returns a new collection object.
	 */
    public function selectCollection($db, $collection) {}
    
    /**
	 * Drops a database
	 *
	 * @link http://www.php.net/manual/en/mongo.dropdb.php
	 * @param mixed $db The database to drop. Can be a MongoDB object or the name of the database.
	 * @return array The database response.
	 */
    public function dropDB($db) {}
    
   /**
	* Check if there was an error on the most recent db operation performed
	* @deprecated Use MongoDB::lastError() instead.
	* @link http://www.php.net/manual/en/mongo.lasterror.php 
    * @return array|null Returns the error, if there was one, or NULL.
    */
    public function lastError() {}
    
   /**
	* Checks for the last error thrown during a database operation
	* @deprecated Use MongoDB::prevError() instead.
	* @link http://www.php.net/manual/en/mongo.preverror.php
    * @return array Returns the error and the number of operations ago it occurred.
    */
    public function prevError() {}
    
   /**
	* Clears any flagged errors on the connection
	* @deprecated Use MongoDB::resetError() instead.
	* @link http://www.php.net/manual/en/mongo.reseterror.php 
    * @return array Returns the database response.
    */
    public function resetError() {}
    
   /**
	* Creates a database error on the database.
	* @deprecated Use MongoDB::forceError() instead.
	* @link http://www.php.net/manual/en/mongo.forceerror.php
    * @return boolean The database response.
    */
    public function forceError() {}
    
   /**
	* Lists all of the databases available
	* @link http://www.php.net/manual/en/mongo.listdbs.php
    * @return array Returns an associative array containing three fields. The first field is databases, which in turn contains an array. Each element of the array is an associative array corresponding to a database, giving the database's name, size, and if it's empty. The other two fields are totalSize (in bytes) and ok, which is 1 if this method ran successfully.
    */
    public function listDBs() {}
    
    /**
	 * Closes this database connection
	 *
	 * This method does not need to be called, except in unusual circumstances.
	 * The driver will cleanly close the database connection when the Mongo object goes out of scope.
	 *
	 * @link http://www.php.net/manual/en/mongo.close.php
	 * 
	 * @return boolean If the connection was successfully closed.
	 */
    public function close() {}
}

/**
 * Instances of this class are used to interact with a database.
 * @link http://www.php.net/manual/en/class.mongodb.php
 */
class MongoDB {
	/**
	 * Profiling is off.
	 */
    const PROFILING_OFF = 0;

	/**
	 * Profiling is on for slow operations (>100 ms).
	 */
    const PROFILING_SLOW = 1;

	/**
	 * Profiling is on for all operations.
	 */
    const PROFILING_ON = 2;

	/**
	 * Creates a new database
	 * This method is not meant to be called directly. The preferred way to create an instance of MongoDB is through Mongo::__get() or Mongo::selectDB().
	 * @link http://www.php.net/manual/en/mongodb.construct.php
	 * @param Mongo $conn Database connection.
	 * @param string $name Database name.
	 * @throws Exception
	 * @return MongoDB
	 */
    public function __construct(Mongo $conn, $name) {}
    
   /**
	* The name of this database
	* @link http://www.php.net/manual/en/mongodb.--tostring.php
    * @return string Returns this database's name.
    */
    public function __toString() {}
    
    /**
	* Gets a collection
	* @link http://www.php.net/manual/en/mongodb.get.php
    * @param string $name The name of the collection.
    * @return MongoCollection
    */
    public function __get($name) {}
    
    /**
	 * Fetches toolkit for dealing with files stored in this database
	 * @link http://www.php.net/manual/en/mongodb.getgridfs.php
	 * @param string $prefix The prefix for the files and chunks collections.
	 * @return MongoGridFS Returns a new gridfs object for this database.
	 */
    public function getGridFS($prefix = "fs") {}
    
   /**
	* Gets this database's profiling level
	* @link http://www.php.net/manual/en/mongodb.getprofilinglevel.php
    * @return int Returns the profiling level.
    */
    public function getProfilingLevel() {}
    
    /**
	 * Sets this database's profiling level
	 * @link http://www.php.net/manual/en/mongodb.setprofilinglevel.php
	 * @param int $level Profiling level.
	 * @return int Returns the previous profiling level.
	 */
    public function setProfilingLevel($level) {}
    
   /**
	* Drops this database
	* @link http://www.php.net/manual/en/mongodb.drop.php 
    * @return array Returns the database response.
    */
    public function drop() {}

    /**
	 * Repairs and compacts this database
	 * @link http://www.php.net/manual/en/mongodb.repair.php
	 * @param bool $preserve_cloned_files If cloned files should be kept if the repair fails.
	 * @param bool $backup_original_files If original files should be backed up.
	 * @return array Returns db response.
	 */
    public function repair($preserve_cloned_files = FALSE, $backup_original_files = FALSE) {}
    
    /**
	 * Gets a collection
	 * @link http://www.php.net/manual/en/mongodb.selectcollection.php
	 * @param string $name
	 * @return MongoCollection
	 */
    public function selectCollection($name) {}
    

	/**
	 * Creates a collection
	 * @link http://www.php.net/manual/en/mongodb.createcollection.php
	 * @param string $name The name of the collection.
	 * @param bool $capped If the collection should be a fixed size.
	 * @param int $size If the collection is fixed size, its size in bytes.
	 * @param int $max If the collection is fixed size, the maximum number of elements to store in the collection.
	 * @return MongoCollection
	 */
    public function createCollection($name, $capped = FALSE, $size = 0, $max = 0) {}
    
    /**
	 * Drops a collection
	 * @link http://www.php.net/manual/en/mongodb.dropcollection.php
	 * @param MongoCollection|string $coll MongoCollection or name of collection to drop.
	 * @return array Returns the database response.
	 */
    public function dropCollection($coll) {}
    
   /**
	* Get a list of collections in this database
	* @link http://www.php.net/manual/en/mongodb.listcollections.php
    * @return array Returns a list of MongoCollections.
    */
    public function listCollections() {}
    
    /**
	 * Creates a database reference
	 * @link http://www.php.net/manual/en/mongodb.createdbref.php
	 * @param string $collection The collection to which the database reference will point.
	 * @param mixed $a Object or _id to which to create a reference. If an object or associative array is given, this will create a reference using the _id field.
	 * @return array Returns a database reference array.
	 */
    public function createDBRef($collection, $a) {}
    

	/**
	 * Fetches the document pointed to by a database reference
	 * @link http://www.php.net/manual/en/mongodb.getdbref.php
	 * @param array $ref A database reference.
	 * @return array Returns the document pointed to by the reference.
	 */
    public function getDBRef(array $ref) {}
    
    /**
	 * Runs JavaScript code on the database server.
	 * @link http://www.php.net/manual/en/mongodb.execute.php
	 * @param MongoCode|string $code Code to execute.
	 * @param array $args Arguments to be passed to code.
	 * @return array Returns the result of the evaluation.
	 */
    public function execute($code, array $args = array()) {}
    
    /**
	 * Execute a database command
	 * @link http://www.php.net/manual/en/mongodb.command.php
	 * @param array $data The query to send.
	 * @return array Returns database response.
	 */
    public function command(array $data) {}
    
   /**
	* Check if there was an error on the most recent db operation performed
	* @link http://www.php.net/manual/en/mongodb.lasterror.php
    * @return array Returns the error, if there was one.
    */
    public function lastError() {}
    
   /**
	* Checks for the last error thrown during a database operation
	* @link http://www.php.net/manual/en/mongodb.preverror.php 
    * @return array Returns the error and the number of operations ago it occurred.
    */
    public function prevError() {}
    
   /**
	* Clears any flagged errors on the database
	* @link http://www.php.net/manual/en/mongodb.reseterror.php
    * @return array Returns the database response.
    */
    public function resetError() {}
    
    /**
	 * Creates a database error
	 * @link http://www.php.net/manual/en/mongodb.forceerror.php
	 * @return boolean Returns the database response.
	 */
    public function forceError() {}
    
    /**
	 * Log in to this database
	 * 
	 * @link http://www.php.net/manual/en/mongodb.authenticate.php
	 * @param string $username The username.
	 * @param string $password The password (in plaintext).
	 * @return array Returns database response.
	 */
    public function authenticate($username, $password) {}
}

/**
 * Represents a database collection.
 * @link http://www.php.net/manual/en/class.mongocollection.php
 */
class MongoCollection {
	/**
	 * @var MongoDB
	 */
	public $db = NULL ;

    /**
	 * Creates a new collection
	 * @link http://www.php.net/manual/en/mongocollection.construct.php
	 * @param MongoDB $db Parent database.
	 * @param string $name Name for this collection.
	 * @throws Exception
	 * @return MongoCollection
	 */
    public function __construct(MongoDB $db, $name) {}
    
   /**
	* String representation of this collection
	* @link http://www.php.net/manual/en/mongocollection.--tostring.php
    * @return string Returns the full name of this collection.
    */
    public function __toString() {}

	/**
	 * Gets a collection
	 * @link http://www.php.net/manual/en/mongocollection.get.php
	 * @param string $name The next string in the collection name.
	 * @return MongoCollection
	 */
    public function __get($name) {}
    
    /**
	 * Returns this collection's name
	 * @link http://www.php.net/manual/en/mongocollection.getname.php
	 * @return string
	 */
    public function getName() {}
    
   /**
	* Drops this collection
	* @link http://www.php.net/manual/en/mongocollection.drop.php
    * @return array Returns the database response.
    */
    public function drop() {}
    
    /**
	 * Validates this collection
	 * @link http://www.php.net/manual/en/mongocollection.validate.php
	 * @param bool $scan_data Only validate indices, not the base collection.
	 * @return array Returns the database's evaluation of this object.
	 */
    public function validate($scan_data = FALSE) {}
    
    /**
	 * Inserts an array into the collection
	 * @link http://www.php.net/manual/en/mongocollection.insert.php
	 * @param array $a An array.
	 * @param array $options Options for the insert.
	 * @throws MongoCursorException
	 * @return mixed
	 */
    public function insert(array $a, array $options = array()) {}
    
    /**
	 * Inserts multiple documents into this collection
	 * @link http://www.php.net/manual/en/mongocollection.batchinsert.php
	 * @param array $a An array of arrays.
	 * @param array $options Options for the inserts.
	 * @throws MongoCursorException
	 * @return mixed f "safe" is set, returns an associative array with the status of the inserts ("ok") and any error that may have occured ("err"). Otherwise, returns TRUE if the batch insert was successfully sent, FALSE otherwise.
	 */
    public function batchInsert(array $a, array $options = array()) {}
    
    /**
	 * Update records based on a given criteria
	 * @link http://www.php.net/manual/en/mongocollection.update.php
	 * @param array $criteria Description of the objects to update.
	 * @param array $newobj The object with which to update the matching records.
	 * @param array $options This parameter is an associative array of the form array("optionname" => <boolean>, ...).
	 * @throws MongoCursorException
	 * @return boolean
	 */
    public function update(array $criteria , array $newobj, array $options = array()) {}
    
    /**
	 * Remove records from this collection
	 * @link http://www.php.net/manual/en/mongocollection.remove.php
	 * @param array $criteria Description of records to remove.
	 * @param array $options Options for remove.
	 * @throws MongoCursorException
	 * @return mixed
	 */
    public function remove(array $criteria, array $options = array()) {}
    
    /**
	 * Querys this collection
	 * @link http://www.php.net/manual/en/mongocollection.find.php
	 * @param array $query The fields for which to search.
	 * @param array $fields Fields of the results to return.
	 * @return MongoCursor
	 */
    public function find(array $query = array(), array $fields = array()) {}
    
    /**
	 * Querys this collection, returning a single element
	 * @link http://www.php.net/manual/en/mongocollection.findone.php
	 * @param array $query The fields for which to search.
	 * @param array $fields Fields of the results to return.
	 * @return array|null
	 */
    public function findOne(array $query = array(), array $fields = array()) {}
    
    /**
	 * Creates an index on the given field(s), or does nothing if the index already exists
	 * @link http://www.php.net/manual/en/mongocollection.ensureindex.php
	 * @param array $keys Field or fields to use as index.
	 * @param array $options [optional] This parameter is an associative array of the form array("optionname" => <boolean>, ...).
	 * @return boolean always true
	 */
    public function ensureIndex(array $keys, array $options = array()) {}
    
    /**
	 * Deletes an index from this collection
	 * @link http://www.php.net/manual/en/mongocollection.deleteindex.php
	 * @param string|array $keys Field or fields from which to delete the index.
	 * @return array Returns the database response.
	 */
    public function deleteIndex($keys) {}
    
   /**
	* Delete all indexes for this collection
	* @link http://www.php.net/manual/en/mongocollection.deleteindexes.php
    * @return array Returns the database response.
    */
    public function deleteIndexes() {}
    
    /**
	 * Returns an array of index names for this collection
	 * @link http://www.php.net/manual/en/mongocollection.getindexinfo.php
	 * @return array Returns a list of index names.
	 */
    public function getIndexInfo() {}
    
    /**
	 * Counts the number of documents in this collection
	 * @link http://www.php.net/manual/en/mongocollection.count.php
	 * @param array|stdClass $query
	 * @return int Returns the number of documents matching the query.
	 */
    public function count($query = array()) {}
    
    /**
	 * Saves an object to this collection
	 * @link http://www.php.net/manual/en/mongocollection.save.php
	 * @param mixed $a Array to save.
	 * @param array $options Options for the save.
	 * @throws MongoCursorException
	 * @return mixed
	 */
    public function save(array $a, array $options = array()) {}
    
    /**
	 * Creates a database reference
	 * @link http://www.php.net/manual/en/mongocollection.createdbref.php
	 * @param array $a Object to which to create a reference.
	 * @return array Returns a database reference array.
	 */
    public function createDBRef(array $a) {}
    
	/**
	 * Fetches the document pointed to by a database reference
	 * @link http://www.php.net/manual/en/mongocollection.getdbref.php
	 * @param array $ref A database reference.
	 * @return array Returns the database document pointed to by the reference.
	 */
    public function getDBRef(array $ref) {}
    
    /**
    * @static
    * @return
    */
    protected static function toIndexString() {}
        
	/**
	 * Performs an operation similar to SQL's GROUP BY command
	 * @link http://www.php.net/manual/en/mongocollection.group.php
	 * @param mixed $keys Fields to group by. If an array or non-code object is passed, it will be the key used to group results.
	 * @param array $initial Initial value of the aggregation counter object.
	 * @param MongoCode $reduce A function that aggregates (reduces) the objects iterated.
	 * @param array $condition An condition that must be true for a row to be considered.
	 * @return array
	 */
    public function group($keys, array $initial, MongoCode $reduce, array $condition = array()) {}
}

/**
 * Result object for database query.
 * @link http://www.php.net/manual/en/class.mongocursor.php
 */
class MongoCursor implements Iterator, Traversable {
    /**
	 * @static
     * @var bool $slaveOkay
     */
    public static $slaveOkay = FALSE;

    /**
	 * Create a new cursor
	 * @link http://www.php.net/manual/en/mongocursor.construct.php
	 * @param resource $connection Database connection.
	 * @param string $ns Full name of database and collection.
	 * @param array $query Database query.
	 * @param array $fields Fields to return.
	 * @return MongoCursor
	 */
    public function __construct(resource $connection, $ns, array $query = array(), array $fields = array()) {}
    
    /**
	 * Checks if there are any more elements in this cursor
	 * @link http://www.php.net/manual/en/mongocursor.hasnext.php
	 * @throws MongoConnectionException
	 * @throws MongoCursorTimeoutException
	 * @return boolean
	 */
    public function hasNext() {}
    
    /**
	 * Return the next object to which this cursor points, and advance the cursor
	 * @link http://www.php.net/manual/en/mongocursor.getnext.php
	 * @throws MongoConnectionException
	 * @throws MongoCursorTimeoutException
	 * @return array
	 */
    public function getNext() {}
    
    /**
	 * Limits the number of results returned
	 * @link http://www.php.net/manual/en/mongocursor.limit.php
	 * @param int $num The number of results to return.
	 * @throws MongoCursorException
	 * @return MongoCursor
	 */
    public function limit($num) {}
    
    /**
	 * Skips a number of results
	 * @link http://www.php.net/manual/en/mongocursor.skip.php
	 * @param int $num The number of results to skip.
	 * @throws MongoCursorException
	 * @return MongoCursor
	 */
    public function skip($num) {}
    
    /**
	 * Sets whether this query can be done on a slave
	 * This method will override the static class variable slaveOkay.
	 * @link http://www.php.net/manual/en/mongocursor.slaveOkay.php
	 * @param boolean $okay If it is okay to query the slave.
	 * @throws MongoCursorException
	 * @return MongoCursor
	 */
    public function slaveOkay($okay = true) {}
    
    /**
	 * Sets whether this cursor will be left open after fetching the last results
	 * @link http://www.php.net/manual/en/mongocursor.tailable.php
	 * @param bool $tail If the cursor should be tailable.
	 * @return MongoCursor
	 */
    public function tailable($tail = true) {}
    
    /**
	 * Sets whether this cursor will timeout
	 * @link http://www.php.net/manual/en/mongocursor.immortal.php
	 * @param bool $liveForever If the cursor should be immortal.
	 * @throws MongoCursorException
	 * @return MongoCursor
	 */
    public function immortal($liveForever = true) {}
    
    /**
	 * Sets a client-side timeout for this query
	 * @link http://www.php.net/manual/en/mongocursor.timeout.php
	 * @param int $ms The number of milliseconds for the cursor to wait for a response. By default, the cursor will wait forever.
	 * @throws MongoCursorTimeoutException
	 * @return MongoCursor
	 */
    public function timeout(int $ms) {}
    
   /**
	* Checks if there are documents that have not been sent yet from the database for this cursor
	* @link http://www.php.net/manual/en/mongocursor.dead.php
    * @return boolean Returns if there are more results that have not been sent to the client, yet.
    */
    public function dead() {}
    
   /**
	* Use snapshot mode for the query
	* @link http://www.php.net/manual/en/mongocursor.snapshot.php
	* @throws MongoCursorException
    * @return MongoCursor
    */
    public function snapshot() {}
    
    /**
	 * Sorts the results by given fields
	 * @link http://www.php.net/manual/en/mongocursor.sort.php
	 * @param array $fields The fields by which to sort.
	 * @throws MongoCursorException	 
	 * @return MongoCursor
	 */
    public function sort(array $fields) {}
    
   /**
	* Gives the database a hint about the query
	* @link http://www.php.net/manual/en/mongocursor.hint.php
	* @param array $key_pattern Indexes to use for the query.
	* @throws MongoCursorException
    * @return MongoCursor
    */
    public function hint(array $key_pattern) {}
    

	/**
	 * Adds a top-level key/value pair to a query
	 * @link http://www.php.net/manual/en/mongocursor.addoption.php
	 * @param string $key Fieldname to add.
	 * @param mixed $value Value to add.
	 * @throws MongoCursorException
	 * @return MongoCursor
	 */
    public function addOption($key, $value) {}
    
   /**
	* Execute the query
	* @link http://www.php.net/manual/en/mongocursor.doquery.php
	* @return MongoConnectionException
    * @return void
    */
    protected function doQuery() {}
    
   /**
	* Returns the current element
	* @link http://www.php.net/manual/en/mongocursor.current.php
    * @return array
    */
    public function current() {}
    
   /**
	* Returns the current result's _id
	* @link http://www.php.net/manual/en/mongocursor.key.php
    * @return string The current result's _id as a string.
    */
    public function key() {}
    
    /**
	 * Advances the cursor to the next result
	 * @link http://www.php.net/manual/en/mongocursor.next.php
	 * @throws MongoConnectionException
	 * @throws MongoCursorTimeoutException
	 * @return void
	 */
    public function next() {}
    
   /**
	* Returns the cursor to the beginning of the result set
	* @throws MongoConnectionException
	* @throws MongoCursorTimeoutException
    * @return void
    */
    public function rewind() {}
    
    /**
	 * Checks if the cursor is reading a valid result.
	 * @link http://www.php.net/manual/en/mongocursor.valid.php
	 * @return boolean If the current result is not null.
	 */
    public function valid() {}
    
   /**
	* Clears the cursor
	* @link http://www.php.net/manual/en/mongocursor.reset.php	 
    * @return void
    */
    public function reset() {}
    
   /**
	* Return an explanation of the query, often useful for optimization and debugging
	* @link http://www.php.net/manual/en/mongocursor.explain.php
    * @return array Returns an explanation of the query.
    */
    public function explain() {}
    
    /**
	 * Counts the number of results for this query
	 * @link http://www.php.net/manual/en/mongocursor.count.php
	 * @param bool $all Send cursor limit and skip information to the count function, if applicable.
	 * @return int The number of documents returned by this cursor's query.
	 */
    public function count($all = FALSE) {}

	/**
	 * Sets the fields for a query
	 * @link http://www.php.net/manual/en/mongocursor.fields.php
	 * @param array $f Fields to return (or not return).
	 * @throws MongoCursorException
	 * @return MongoCursor
	 */
	public function fields(array $f){}

	/**
	 * Gets the query, fields, limit, and skip for this cursor
	 * @link http://www.php.net/manual/en/mongocursor.info.php
	 * @return array The query, fields, limit, and skip for this cursor as an associative array.
	 */
	public function info(){}
}

/**
 * 
 */
class MongoGridFS extends MongoCollection {
    /**
    * @var $chunks
    */
    public $chunks;
    
    /**
    * @var $filesName
    */
    protected $filesName;
    
    /**
    * @var $chunksName
    */
    protected $chunksName;

    /**
    * @return
    */
    public function __construct() {}
    
    /**
    * @return
    */
    public function drop() {}
    
    /**
    * @return
    */
    public function find() {}
    
    /**
    * @return
    */
    public function storeFile() {}
    
    /**
    * @return
    */
    public function storeBytes() {}
    
    /**
    * @return
    */
    public function findOne() {}
    
    /**
    * @return
    */
    public function remove() {}
    
    /**
    * @return
    */
    public function storeUpload() {}
    
    /**
    * @return
    */
    public function __toString() {}
    
    /**
    * @param $name
    * @return
    */
    public function __get($name) {}
    
    /**
    * @return
    */
    public function getName() {}
    
    /**
    * @return
    */
    public function validate() {}
    
    /**
    * @return
    */
    public function insert() {}
    
    /**
    * @return
    */
    public function batchInsert() {}
    
    /**
    * @return
    */
    public function update() {}
    
    /**
    * @return
    */
    public function ensureIndex() {}
    
    /**
    * @return
    */
    public function deleteIndex() {}
    
    /**
    * @return
    */
    public function deleteIndexes() {}
    
    /**
    * @return
    */
    public function getIndexInfo() {}
    
    /**
    * @return
    */
    public function count() {}
    
    /**
    * @return
    */
    public function save() {}
    
    /**
    * @return
    */
    public function createDBRef() {}
    
    /**
    * @return
    */
    public function getDBRef() {}
    
    /**
    * @static
    * @return
    */
    protected static function toIndexString() {}
    
    /**
    * @return
    */
    public function group() {}
}

class MongoGridFSFile {
    /**
    * @var $file
    */
    public $file;
    
    /**
    * @var $gridfs
    */
    protected $gridfs;

    /**
    * @return
    */
    public function __construct() {}
    
    /**
    * @return
    */
    public function getFilename() {}
    
    /**
    * @return
    */
    public function getSize() {}
    
    /**
    * @return
    */
    public function write() {}
    
    /**
    * @return
    */
    public function getBytes() {}
}

class MongoGridFSCursor extends MongoCursor implements Traversable, Iterator {
    /**@static
    * 
    * @var $slaveOkay
    */
    public static $slaveOkay;
    
    /**
    * @var $gridfs
    */
    protected $gridfs;

    /**
    * @return
    */
    public function __construct() {}
    
    /**
    * @return
    */
    public function getNext() {}
    
    /**
    * @return
    */
    public function current() {}
    
    /**
    * @return
    */
    public function key() {}
    
    /**
    * @return
    */
    public function hasNext() {}
    
    /**
    * @return
    */
    public function limit() {}
    
    /**
    * @return
    */
    public function skip() {}
    
    /**
    * @return
    */
    public function slaveOkay() {}
    
    /**
    * @return
    */
    public function tailable() {}
    
    /**
    * @return
    */
    public function immortal() {}
    
    /**
    * @return
    */
    public function timeout() {}
    
    /**
    * @return
    */
    public function dead() {}
    
    /**
    * @return
    */
    public function snapshot() {}
    
    /**
    * @return
    */
    public function sort() {}
    
    /**
    * @return
    */
    public function hint() {}
    
    /**
    * @return
    */
    public function addOption() {}
    
    /**
    * @return
    */
    protected function doQuery() {}
    
    /**
    * @return
    */
    public function next() {}
    
    /**
    * @return
    */
    public function rewind() {}
    
    /**
    * @return
    */
    public function valid() {}
    
    /**
    * @return
    */
    public function reset() {}
    
    /**
    * @return
    */
    public function explain() {}
    
    /**
    * @return
    */
    public function count() {}
}

/**
 * A unique identifier created for database objects.
 * @link http://www.php.net/manual/en/class.mongoid.php
 */
class MongoId {
    /**
	 * Creates a new id
	 * @link http://www.php.net/manual/en/mongoid.construct.php
	 * @param string $id A string to use as the id. Must be 24 hexidecimal characters. If an invalid string is passed to this constructor, the constructor will ignore it and create a new id value.
	 * @return MongoId
	 */
    public function __construct($id = NULL) {}
    
   /**
	* Returns a hexidecimal representation of this id
	* @link http://www.php.net/manual/en/mongoid.tostring.php
    * @return string This id.
    */
    public function __toString() {}
    
   /**
	* Gets the number of seconds since the epoch that this id was created
	* @link http://www.php.net/manual/en/mongoid.gettimestamp.php
    * @return int
    */
    public function getTimestamp() {}
}

class MongoCode {
    /**
    * @var $code
    */
    public $code;
    
    /**
    * @var $scope
    */
    public $scope;

    /**
    * @return
    */
    public function __construct() {}
    
    /**
    * @return
    */
    public function __toString() {}
}

class MongoRegex {
    /**
    * @var $regex
    */
    public $regex;
    
    /**
    * @var $flags
    */
    public $flags;

    /**
    * @return
    */
    public function __construct() {}
    
    /**
    * @return
    */
    public function __toString() {}
}

class MongoDate {
    /**
    * @var $sec
    */
    public $sec;
    
    /**
    * @var $usec
    */
    public $usec;

    /**
    * @return
    */
    public function __construct() {}
    
    /**
    * @return
    */
    public function __toString() {}
}

class MongoBinData {
    const FUNC = 1;
    const BYTE_ARRAY = 2;
    const UUID = 3;
    const MD5 = 5;
    const CUSTOM = 128;

    /**
    * @var $bin
    */
    public $bin;
    
    /**
    * @var $type
    */
    public $type;

    /**
    * @return
    */
    public function __construct() {}
    
    /**
    * @return
    */
    public function __toString() {}
}

class MongoDBRef {
    /**@static
    * 
    * @var $refKey
    */
    protected static $refKey = '$ref';
    
    /**@static
    * 
    * @var $idKey
    */
    protected static $idKey = '$id';

    /**
    * @static
    * @return
    */
    public static function create() {}
    
    /**
    * @static
    * @return
    */
    public static function isRef() {}
    
    /**
    * @static
    * @return
    */
    public static function get() {}
}

class MongoException extends Exception {
    /**
    * @var $message
    */
    protected $message;
    
    /**
    * @var $code
    */
    protected $code;
    
    /**
    * @var $file
    */
    protected $file;
    
    /**
    * @var $line
    */
    protected $line;

    /**
    * @final
    * @return
    */
    private final function __clone() {}
    
    /**
    * @param $message
    * @param $code
    * @param $previous
    * @return
    */
    public function __construct($message, $code = null, $previous = null) {}
    
    /**
    * @final
    * @return
    */
    public final function getMessage() {}
    
    /**
    * @final
    * @return
    */
    public final function getCode() {}
    
    /**
    * @final
    * @return
    */
    public final function getFile() {}
    
    /**
    * @final
    * @return
    */
    public final function getLine() {}
    
    /**
    * @final
    * @return
    */
    public final function getTrace() {}
    
    /**
    * @final
    * @return
    */
    public final function getPrevious() {}
    
    /**
    * @final
    * @return
    */
    public final function getTraceAsString() {}
    
    /**
    * @return
    */
    public function __toString() {}
}

class MongoCursorException extends MongoException {
    /**
    * @var $message
    */
    protected $message;
    
    /**
    * @var $code
    */
    protected $code;
    
    /**
    * @var $file
    */
    protected $file;
    
    /**
    * @var $line
    */
    protected $line;

    /**
    * @final
    * @return
    */
    private final function __clone() {}
    
    /**
    * @param $message
    * @param $code
    * @param $previous
    * @return
    */
    public function __construct($message, $code = null, $previous = null) {}
    
    /**
    * @final
    * @return
    */
    public final function getMessage() {}
    
    /**
    * @final
    * @return
    */
    public final function getCode() {}
    
    /**
    * @final
    * @return
    */
    public final function getFile() {}
    
    /**
    * @final
    * @return
    */
    public final function getLine() {}
    
    /**
    * @final
    * @return
    */
    public final function getTrace() {}
    
    /**
    * @final
    * @return
    */
    public final function getPrevious() {}
    
    /**
    * @final
    * @return
    */
    public final function getTraceAsString() {}
    
    /**
    * @return
    */
    public function __toString() {}
}

class MongoCursorTimeoutException extends MongoCursorException {
    /**
    * @var $message
    */
    protected $message;
    
    /**
    * @var $code
    */
    protected $code;
    
    /**
    * @var $file
    */
    protected $file;
    
    /**
    * @var $line
    */
    protected $line;

    /**
    * @final
    * @return
    */
    private final function __clone() {}
    
    /**
    * @param $message
    * @param $code
    * @param $previous
    * @return
    */
    public function __construct($message, $code = null, $previous = null) {}
    
    /**
    * @final
    * @return
    */
    public final function getMessage() {}
    
    /**
    * @final
    * @return
    */
    public final function getCode() {}
    
    /**
    * @final
    * @return
    */
    public final function getFile() {}
    
    /**
    * @final
    * @return
    */
    public final function getLine() {}
    
    /**
    * @final
    * @return
    */
    public final function getTrace() {}
    
    /**
    * @final
    * @return
    */
    public final function getPrevious() {}
    
    /**
    * @final
    * @return
    */
    public final function getTraceAsString() {}
    
    /**
    * @return
    */
    public function __toString() {}
}

class MongoConnectionException extends MongoException {
    /**
    * @var $message
    */
    protected $message;
    
    /**
    * @var $code
    */
    protected $code;
    
    /**
    * @var $file
    */
    protected $file;
    
    /**
    * @var $line
    */
    protected $line;

    /**
    * @final
    * @return
    */
    private final function __clone() {}
    
    /**
    * @param $message
    * @param $code
    * @param $previous
    * @return
    */
    public function __construct($message, $code = null, $previous = null) {}
    
    /**
    * @final
    * @return
    */
    public final function getMessage() {}
    
    /**
    * @final
    * @return
    */
    public final function getCode() {}
    
    /**
    * @final
    * @return
    */
    public final function getFile() {}
    
    /**
    * @final
    * @return
    */
    public final function getLine() {}
    
    /**
    * @final
    * @return
    */
    public final function getTrace() {}
    
    /**
    * @final
    * @return
    */
    public final function getPrevious() {}
    
    /**
    * @final
    * @return
    */
    public final function getTraceAsString() {}
    
    /**
    * @return
    */
    public function __toString() {}
}

class MongoGridFSException extends MongoException {
    /**
    * @var $message
    */
    protected $message;
    
    /**
    * @var $code
    */
    protected $code;
    
    /**
    * @var $file
    */
    protected $file;
    
    /**
    * @var $line
    */
    protected $line;

    /**
    * @final
    * @return
    */
    private final function __clone() {}
    
    /**
    * @param $message
    * @param $code
    * @param $previous
    * @return
    */
    public function __construct($message, $code = null, $previous = null) {}
    
    /**
    * @final
    * @return
    */
    public final function getMessage() {}
    
    /**
    * @final
    * @return
    */
    public final function getCode() {}
    
    /**
    * @final
    * @return
    */
    public final function getFile() {}
    
    /**
    * @final
    * @return
    */
    public final function getLine() {}
    
    /**
    * @final
    * @return
    */
    public final function getTrace() {}
    
    /**
    * @final
    * @return
    */
    public final function getPrevious() {}
    
    /**
    * @final
    * @return
    */
    public final function getTraceAsString() {}
    
    /**
    * @return
    */
    public function __toString() {}
}

class MongoTimestamp {
    /**
    * @var $sec
    */
    public $sec;
    
    /**
    * @var $inc
    */
    public $inc;

    /**
    * @return
    */
    public function __construct() {}
    
    /**
    * @return
    */
    public function __toString() {}
}

class MongoMaxKey {
}

class MongoMinKey {
}

