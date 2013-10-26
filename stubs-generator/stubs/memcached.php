<?php

/**
 * The Memcached class
 * @link http://php.net/manual/en/book.memcached.php
 */
class Memcached
{
	const OPT_COMPRESSION = -1001;
	const OPT_SERIALIZER = -1003;
	const SERIALIZER_PHP = 1;
	const SERIALIZER_IGBINARY = 2;
	const SERIALIZER_JSON = 3;
	const OPT_PREFIX_KEY = -1002;
	const OPT_HASH = 2;
	const HASH_DEFAULT = 0;
	const HASH_MD5 = 1;
	const HASH_CRC = 2;
	const HASH_FNV1_64 = 3;
	const HASH_FNV1A_64 = 4;
	const HASH_FNV1_32 = 5;
	const HASH_FNV1A_32 = 6;
	const HASH_HSIEH = 7;
	const HASH_MURMUR = 8;
	const OPT_DISTRIBUTION = 9;
	const DISTRIBUTION_MODULA = 0;
	const DISTRIBUTION_CONSISTENT = 1;
	const OPT_LIBKETAMA_COMPATIBLE = 16;
	const OPT_BUFFER_WRITES = 10;
	const OPT_BINARY_PROTOCOL = 18;
	const OPT_NO_BLOCK = 0;
	const OPT_TCP_NODELAY = 1;
	const OPT_SOCKET_SEND_SIZE = 4;
	const OPT_SOCKET_RECV_SIZE = 5;
	const OPT_CONNECT_TIMEOUT = 14;
	const OPT_RETRY_TIMEOUT = 15;
	const OPT_SEND_TIMEOUT = 19;
	const OPT_RECV_TIMEOUT = 15;
	const OPT_POLL_TIMEOUT = 8;
	const OPT_CACHE_LOOKUPS = 6;
	const OPT_SERVER_FAILURE_LIMIT = 21;
	const HAVE_IGBINARY = null;
	const HAVE_JSON = null;
	const GET_PRESERVE_ORDER = 1;
	const RES_SUCCESS = 0;
	const RES_FAILURE = 1;
	const RES_HOST_LOOKUP_FAILURE = 2;
	const RES_UNKNOWN_READ_FAILURE = 7;
	const RES_PROTOCOL_ERROR = 8;
	const RES_CLIENT_ERROR = 9;
	const RES_SERVER_ERROR = 10;
	const RES_WRITE_FAILURE = 5;
	const RES_DATA_EXISTS = 12;
	const RES_NOTSTORED = 14;
	const RES_NOTFOUND = 16;
	const RES_PARTIAL_READ = 18;
	const RES_SOME_ERRORS = 19;
	const RES_NO_SERVERS = 20;
	const RES_END = 21;
	const RES_ERRNO = 26;
	const RES_BUFFERED = 32;
	const RES_TIMEOUT = 31;
	const RES_BAD_KEY_PROVIDED = 33;
	const RES_CONNECTION_SOCKET_CREATE_FAILURE = 11;
	const RES_PAYLOAD_FAILURE = -1001;

	/**
	 * Create a Memcached instance
	 * @param string $persistent_id
	 * @link http://php.net/manual/en/memcached.construct.php
	 */
	public function __construct($persistent_id = '')
	{ }

	/**
	 * Add an item under a new key
	 * @param string $key The key under which to store the value.
	 * @param mixed $value The value to store.
	 * @param int $expiration The expiration time, defaults to 0.
	 * @return boolean Returns TRUE on success or FALSE on failure. The Memcached::getResultCode() will return Memcached::RES_NOTSTORED if the key already exists.
	 * @link http://php.net/manual/en/memcached.add.php
	 */
	public function add($key, $value, $expiration = 0)
	{ }

	/**
	 * Add an item under a new key on a specific server
	 * @param string $server_key
	 * @param string $key
	 * @param mixed $value
	 * @param int $expiration
	 * @return boolean
	 * @link http://php.net/manual/en/memcached.addbykey.php
	 */
	public function addByKey($server_key, $key, $value, $expiration = 0)
	{ }

	/**
	 * Add a server to the server pool
	 * @param string $host
	 * @param int $port
	 * @param int $weight
	 * @return boolean
	 * @link http://php.net/manual/en/memcached.addserver.php
	 */
	public function addServer($host, $port, $weight = 0)
	{ }

	/**
	 * Add multiple servers to the server pool
	 * @param array $servers
	 * @return boolean
	 * @link http://php.net/manual/en/memcached.addservers.php
	 */
	public function addServers($servers)
	{ }

	/**
	 * Append data to an existing item
	 * @param string $key
	 * @param string $value
	 * @return boolean
	 * @link http://php.net/manual/en/memcached.append.php
	 */
	public function append($key, $value)
	{ }

	/**
	 * Append data to an existing item on a specific server.
	 * @param string $server_key
	 * @param string $key
	 * @param string $value
	 * @return boolean
	 * @link http://php.net/manual/en/memcached.appendbykey.php
	 */
	public function appendByKey($server_key, $key, $value)
	{ }

	/**
	 * Compare and swap an item
	 * @param float $cas_token
	 * @param string $key
	 * @param mixed $value
	 * @param int $expiration
	 * @return boolean
	 * @link http://php.net/manual/en/memcached.cas.php
	 */
	public function cas($cas_token, $key, $value, $expiration = 0)
	{ }

	/**
	 * Compare and swap an item on a specific server
	 * @param float $cas_token
	 * @param string $server_key
	 * @param string $key
	 * @param mixed $value
	 * @param int $expiration
	 * @return boolean
	 * @link http://php.net/manual/en/memcached.casbykey.php
	 */
	public function casByKey($cas_token, $server_key, $key, $value, $expiration = 0)
	{ }

	/**
	 * Decrement numeric item's value
	 * @param string $key
	 * @param int $offset
	 * @return int
	 * @link http://php.net/manual/en/memcached.decrement.php
	 */
	public function decrement($key, $offset = 1)
	{ }

	/**
	 * Delete an item
	 * @param string $key
	 * @param int $time
	 * @return boolean
	 * @link http://php.net/manual/en/memcached.delete.php
	 */
	public function delete($key, $time = 0)
	{ }

	/**
	 * Delete an item from a specific server
	 * @param string $server_key
	 * @param string $key
	 * @param int $time
	 * @return boolean
	 * @link http://php.net/manual/en/memcached.deletebykey.php
	 */
	public function deleteByKey($server_key, $key, $time = 0)
	{ }

	/**
	 * Fetch the next result
	 * @return array
	 * @link http://php.net/manual/en/memcached.fetch.php
	 */
	public function fetch()
	{ }

	/**
	 * Fetch all the remaining results
	 * @return array
	 * @link http://php.net/manual/en/memcached.fetchall.php
	 */
	public function fetchAll()
	{ }

	/**
	 * Invalidate all items in the cache
	 * @param int $delay
	 * @return boolean
	 * @link http://php.net/manual/en/memcached.flush.php
	 */
	public function flush($delay = 0)
	{ }

	/**
	 * Retrieve an item
	 * @param string $key
	 * @param callback $cache_cb
	 * @param float $cas_token
	 * @return mixed
	 * @link http://php.net/manual/en/memcached.get.php
	 */
	public function get($key, $cache_cb = null, &$cas_token = null)
	{ }

	/**
	 * Retrieve an item from a specific server
	 * @param string $server_key
	 * @param string $key
	 * @param callback $cache_cb
	 * @param float $cas_token
	 * @return mixed
	 * @link http://php.net/manual/en/memcached.getbykey.php
	 */
	public function getByKey($server_key, $key, $cache_cb = null, &$cas_token = null)
	{ }

	/**
	 * Request multiple items
	 * @param array $keys
	 * @param bool $with_cas
	 * @param callback $value_cb
	 * @return boolean
	 * @link http://php.net/manual/en/memcached.getdelayed.php
	 */
	public function getDelayed($keys, $with_cas = false, $value_cb = null)
	{ }

	/**
	 * Request multiple items from a specific server
	 * @param string $server_key
	 * @param array $keys
	 * @param bool $with_cas
	 * @param callback $value_cb
	 * @return boolean
	 * @link http://php.net/manual/en/memcached.getdelayedbykey.php
	 */
	public function getDelayedByKey($server_key, $keys, $with_cas = false, $value_cb = null)
	{ }

	/**
	 * Retrieve multiple items
	 * @param array $keys
	 * @param array &$cas_tokens
	 * @param int $flags
	 * @return mixed
	 * @link http://php.net/manual/en/memcached.getmulti.php
	 */
	public function getMulti($keys, &$cas_tokens = null, $flags = 0)
	{ }

	/**
	 * Retrieve multiple items from a specific server
	 * @param string $server_key
	 * @param array $keys
	 * @param array &$cas_tokens
	 * @param int $flags
	 * @return mixed
	 * @link http://php.net/manual/en/memcached.getmultibykey.php
	 */
	public function getMultiByKey($server_key, $keys, &$cas_tokens = null, $flags = 0)
	{ }

	/**
	 * Retrieve a Memcached option value
	 * @param int $option
	 * @return mixed
	 * @link http://php.net/manual/en/memcached.getoption.php
	 */
	public function getOption($option)
	{ }

	/**
	 * Return the result code of the last operation
	 * @return int
	 * @link http://php.net/manual/en/memcached.getresultcode.php
	 */
	public function getResultCode()
	{ }

	/**
	 * Return the message describing the result of the last operation
	 * @return string
	 * @link http://php.net/manual/en/memcached.getresultmessage.php
	 */
	public function getResultMessage()
	{ }

	/**
	 * Map a key to a server
	 * @param string $server_key
	 * @return array
	 * @link http://php.net/manual/en/memcached.getserverbykey.php
	 */
	public function getServerByKey($server_key)
	{ }

	/**
	 * Get the list of the servers in the pool
	 * @return array
	 * @link http://php.net/manual/en/memcached.getserverlist.php
	 */
	public function getServerList()
	{ }

	/**
	 * Get server pool statistics
	 * @return array
	 * @link http://php.net/manual/en/memcached.getstats.php
	 */
	public function getStats()
	{ }

	/**
	 * Get server pool version info
	 * @return array
	 * @link http://php.net/manual/en/memcached.getversion.php
	 */
	public function getVersion()
	{ }

	/**
	 * Increment numeric item's value
	 * @param string $key
	 * @param int $offset
	 * @return int
	 * @link http://php.net/manual/en/memcached.increment.php
	 */
	public function increment($key, $offset = 1)
	{ }

	/**
	 * Prepend data to an existing item
	 * @param string $key
	 * @param string $value
	 * @return boolean
	 * @link http://php.net/manual/en/memcached.prepend.php
	 */
	public function prepend($key, $value)
	{ }

	/**
	 * Prepend data to an existing item on a specific server
	 * @param string $server_key
	 * @param string $key
	 * @param string $value
	 * @return boolean
	 * @link http://php.net/manual/en/memcached.prependbykey.php
	 */
	public function prependByKey($server_key, $key, $value)
	{ }

	/**
	 * Replace the item under an existing key
	 * @param string $key
	 * @param mixed $value
	 * @param int $expiration
	 * @return boolean
	 * @link http://php.net/manual/en/memcached.replace.php
	 */
	public function replace($key, $value, $expiration = 0)
	{ }

	/**
	 * Replace the item under an existing key on a specific server
	 * @param string $server_key
	 * @param string $key
	 * @param mixed $value
	 * @param int $expiration
	 * @return boolean
	 * @link http://php.net/manual/en/memcached.replacebykey.php
	 */
	public function replaceByKey($server_key, $key, $value, $expiration = 0)
	{ }

	/**
	 * Store an item
	 * @param string $key
	 * @param mixed $value
	 * @param int $expiration
	 * @return boolean
	 * @link http://php.net/manual/en/memcached.set.php
	 */
	public function set($key, $value, $expiration = 0)
	{ }

	/**
	 * Store an item on a specific server
	 * @param string $server_key
	 * @param string $key
	 * @param mixed $value
	 * @param int $expiration
	 * @return boolean
	 * @link http://php.net/manual/en/memcached.setbykey.php
	 */
	public function setByKey($server_key, $key, $value, $expiration = 0)
	{ }

	/**
	 * Store multiple items
	 * @param array $items
	 * @param int $expiration
	 * @return boolean
	 * @link http://php.net/manual/en/memcached.setmulti.php
	 */
	public function setMulti($items, $expiration = 0)
	{ }

	/**
	 * Store multiple items on a specific server
	 * @param string $server_key
	 * @param array $items
	 * @param int $expiration
	 * @return boolean
	 * @link http://php.net/manual/en/memcached.setmultibykey.php
	 */
	public function setMultiByKey($server_key, $items, $expiration = 0)
	{ }

	/**
	 * Set a Memcached option
	 * @param int $option
	 * @param mixed $value
	 * @return boolean
	 * @link http://php.net/manual/en/memcached.setoption.php
	 */
	public function setOption($option, $value)
	{ }
}
