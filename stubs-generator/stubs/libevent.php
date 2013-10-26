<?php

// Start of PECL libevent v.0.0.4

define('EV_TIMEOUT', 1);
define('EV_READ', 2);
define('EV_WRITE', 4);
define('EV_SIGNAL', 8);
define('EV_PERSIST', 16);
define('EVLOOP_NONBLOCK', 2);
define('EVLOOP_ONCE', 1);
define('EVBUFFER_READ', 1);
define('EVBUFFER_WRITE', 2);
define('EVBUFFER_EOF', 16);
define('EVBUFFER_ERROR', 32);
define('EVBUFFER_TIMEOUT', 64);


/**
 * <p>Create and initialize new event base</p>
 *
 * <p>Returns new event base, which can be used later in {@link event_base_set}(), {@link event_base_loop}() and other functions.</p>
 *
 * @link http://php.net/event_base_new
 *
 * @return resource|bool returns valid event base resource on success or FALSE on error.
 */
function event_base_new(){}

/**
 * <p>Destroy event base</p>
 * <p>(PECL libevent >= 0.0.1)</p>
 *
 * <p>Destroys the specified event_base and frees all the resources associated.
 * Note that it's not possible to destroy an event base with events attached to it.</p>
 *
 * @link http://php.net/event_base_free
 *
 * @param resource $event_base Valid event base resource.
 *
 * @return void
 */
function event_base_free($event_base) {}

/**
 * <p>Handle events</p>
 * <p>(PECL libevent >= 0.0.1)</p>
 *
 * <p>Starts event loop for the specified event base.</p>
 *
 * @link http://php.net/event_base_loop
 *
 * @param resource $event_base Valid event base resource.
 * @param int $flags [optional] Optional parameter, which can take any combination of EVLOOP_ONCE and EVLOOP_NONBLOCK.
 *
 * @return int returns 0 on success, -1 on error and 1 if no events were registered.
 */
function event_base_loop($event_base, $flags = null) {}

/**
 * <p>Abort event loop</p>
 * <p>(PECL libevent >= 0.0.1)</p>
 *
 * <p>Abort the active event loop immediately. The behaviour is similar to break statement.</p>
 *
 * @link http://php.net/event_base_loopbreak
 *
 * @param resource $event_base Valid event base resource.
 *
 * @return bool returns TRUE on success or FALSE on error.
 */
function event_base_loopbreak($event_base) {}

/**
 * <p>Exit loop after a time</p>
 * <p>(PECL libevent >= 0.0.1)</p>
 *
 * <p>The next event loop iteration after the given timer expires will
 * complete normally, then exit without blocking for events again.</p>
 *
 * @link http://php.net/event_base_loopexit
 *
 * @param resource $event_base Valid event base resource.
 * @param int $timeout [optional] Optional timeout parameter (in microseconds).
 *
 * @return bool returns TRUE on success or FALSE on error.
 */
function event_base_loopexit($event_base, $timeout = -1) {}

/**
 * <p>Associate event base with an event</p>
 * <p>(PECL libevent >= 0.0.1)</p>
 *
 * <p>Associates the event_base with the event.</p>
 *
 * @link http://php.net/event_base_set
 *
 * @param resource $event Valid event resource.
 * @param resource $event_base Valid event base resource.
 *
 * @return bool returns TRUE on success or FALSE on error.
 */
function event_base_set($event, $base) {}

/**
 * <p>Set the number of different event priority levels</p>
 * <p>(PECL libevent >= 0.0.2)</p>
 *
 * <p>By default all events are scheduled with the same priority (npriorities/2).
 * Using {@link event_base_priority_init}() you can change the number of event priority
 * levels and then set a desired priority for each event.</p>
 *
 * @link http://php.net/event_base_priority_init
 *
 * @param resource $event_base Valid event base resource.
 * @param int $npriorities The number of event priority levels.
 *
 * @return bool returns TRUE on success or FALSE on error.
 */
function event_base_priority_init($event_base, $npriorities) {}

/**
 * <p>Creates and returns a new event resource.</p>
 * <p>(PECL libevent >= 0.0.1)</p>
 *
 * @link http://php.net/event_new
 *
 * @return resource|bool returns a new event resource on success or FALSE on error.
 */
function event_new() {}

/**
 * <p>Free event resource.</p>
 * <p>(PECL libevent >= 0.0.1)</p>
 *
 * @link http://php.net/event_free
 *
 * @param resource $event Valid event resource.
 *
 * @return void
 */
function event_free($event) {}

/**
 * <p>Add an event to the set of monitored events</p>
 * <p>(PECL libevent >= 0.0.1)</p>
 *
 * <p>schedules the execution of the event when the event specified in
 * {@link event_set}() occurs or in at least the time specified by the timeout
 * argument. If timeout was not specified, not timeout is set. The event
 * must be already initalized by {@link event_set}() and {@link event_base_set}() functions.
 * If the event already has a timeout set, it is replaced by the new one.</p>
 *
 * @link http://php.net/event_add
 *
 * @param resource $event <p>
 * Valid event resource.
 * </p>
 * @param null $timeout [optional] <p>
 * Optional timeout (in microseconds).
 * </p>
 *
 * @return bool returns TRUE on success or FALSE on error.
 */
function event_add($event, $timeout = -1) {}

/**
 * <p>Prepares the event to be used in {@link event_add}().</p>
 * <p>(PECL libevent >= 0.0.1)</p>
 *
 * <p>The event is prepared to call the function specified by the callback
 * on the events specified in parameter events, which is a set of the following
 * flags: EV_TIMEOUT, EV_SIGNAL, EV_READ, EV_WRITE and EV_PERSIST.</p>
 *
 * <p>EV_SIGNAL support was added in version 0.0.4</p>
 *
 * <p>After initializing the event, use {@link event_base_set}() to associate the event with its event base.</p>
 *
 * <p>In case of matching event, these three arguments are passed to the callback function:
 * <table>
 * 	<tr>
 * 		<td><b>$fd</b></td>
 * 		<td>Signal number or resource indicating the stream.</td>
 * 	</tr>
 * 	<tr>
 * 		<td><b>$events</b></td>
 * 		<td>A flag indicating the event. Consists of the following flags: EV_TIMEOUT, EV_SIGNAL, EV_READ, EV_WRITE and EV_PERSIST.</td>
 * 	</tr>
 * 	<tr>
 * 		<td><b>$arg</b></td>
 * 		<td>Optional parameter, previously passed to {@link event_set}() as arg.</td>
 * 	</tr>
 * </table>
 * </p>
 *
 * @link http://php.net/event_set
 *
 * @param resource $event <p>
 * Valid event resource.
 * </p>
 * @param resource|mixed $fd <p>
 * Valid PHP stream resource. The stream must be castable to file descriptor,
 * so you most likely won't be able to use any of filtered streams.
 * </p>
 * @param int $events <p>
 * A set of flags indicating the desired event, can be EV_READ and/or EV_WRITE.
 * The additional flag EV_PERSIST makes the event to persist until {@link event_del}() is
 * called, otherwise the callback is invoked only once.
 * </p>
 * @param callback $callback <p>
 * Callback function to be called when the matching event occurs.
 * </p>
 * @param mixed $arg [optional] <p>
 * Optional callback parameter.
 * </p>
 *
 * @return void
 */
function event_set($event, $fd, $events, $callback, $arg = null) {}

/**
 * <p>Remove an event from the set of monitored events.</p>
 * <p>(PECL libevent >= 0.0.1)</p>
 *
 * @link http://php.net/event_del
 *
 * @param resource $event Valid event resource.
 *
 * @return bool returns TRUE on success or FALSE on error.
 */
function event_del($event) {}

/**
 * <p>Create new buffered event</p>
 * <p>(PECL libevent >= 0.0.1)</p>
 *
 * <p>Libevent provides an abstraction layer on top of the regular event API.
 * Using buffered event you don't need to deal with the I/O manually, instead
 * it provides input and output buffers that get filled and drained automatically.</p>
 *
 * @link http://php.net/event_buffer_new
 *
 * @param resource $stream Valid PHP stream resource. Must be castable to file descriptor.
 * @param callback|null $readcb Callback to invoke where there is data to read, or NULL if no callback is desired.
 * @param callback|null $writecb Callback to invoke where the descriptor is ready for writing, or NULL if no callback is desired.
 * @param callback|null $errorcb Callback to invoke where there is an error on the descriptor, cannot be NULL.
 * @param mixed $arg An argument that will be passed to each of the callbacks (optional).
 *
 * @return resource|bool returns new buffered event resource on success or FALSE on error.
 */
function event_buffer_new($stream, $readcb, $writecb, $errorcb, $arg = null) {}

/**
 * <p>Destroys the specified buffered event and frees all the resources associated.</p>
 * <p>(PECL libevent >= 0.0.1)</p>
 *
 * @link http://php.net/event_buffer_free
 *
 * @param resource $bevent Valid buffered event resource.
 *
 * @return void
 */
function event_buffer_free($bevent) {}

/**
 * <p>Associate buffered event with an event base</p>
 * <p>(PECL libevent >= 0.0.1)</p>
 *
 * <p>Assign the specified bevent to the event_base.</p>
 *
 * @link http://php.net/event_buffer_base_set
 *
 * @param resource $bevent Valid buffered event resource.
 * @param resource $event_base Valid event base resource.
 *
 * @return bool returns TRUE on success or FALSE on error.
 */
function event_buffer_base_set($bevent, $event_base) {}

/**
 * <p>Assign a priority to a buffered event</p>
 * <p>(PECL libevent >= 0.0.1)</p>
 *
 * @link http://php.net/event_buffer_priority_set
 *
 * @param resource $bevent <p>
 * Valid buffered event resource.
 * </p>
 * @param int $priority <p>
 * Priority level. Cannot be less than zero and cannot exceed
 * maximum priority level of the event base (see {@link event_base_priority_init}()).
 * </p>
 *
 * @return bool returns TRUE on success or FALSE on error.
 */
function event_buffer_priority_set($bevent, $priority) {}

/**
 * <p>Writes data to the specified buffered event.</p>
 * <p>(PECL libevent >= 0.0.1)</p>
 *
 * <p>The data is appended to the output buffer and written
 * to the descriptor when it becomes available for writing.</p>
 *
 * @link http://php.net/event_buffer_write
 *
 * @param resource $bevent Valid buffered event resource.
 * @param string $data The data to be written.
 * @param int $data_size Optional size parameter. {@link event_buffer_write}() writes all the data by default
 *
 * @return bool returns TRUE on success or FALSE on error.
 */
function event_buffer_write($bevent, $data, $data_size = -1) {}

/**
 * <p>Reads data from the input buffer of the buffered event.</p>
 * <p>(PECL libevent >= 0.0.1)</p>
 *
 * @link http://php.net/event_buffer_read
 *
 * @param resource $bevent Valid buffered event resource.
 * @param int $data_size Data size in bytes.
 *
 * @return void
 */
function event_buffer_read($bevent, $data_size) {}

/**
 * <p>Enables the specified buffered event.</p>
 * <p>(PECL libevent >= 0.0.1)</p>
 *
 * @link http://php.net/event_buffer_enable
 *
 * @param resource $bevent Valid buffered event resource.
 * @param int $events Any combination of EV_READ and EV_WRITE.
 *
 * @return void
 */
function event_buffer_enable($bevent, $events) {}

/**
 * <p>Disable a buffered event</p>
 * <p>(PECL libevent >= 0.0.1)</p>
 *
 * <p>Disables the specified buffered event.</p>
 *
 * @link http://php.net/event_buffer_disable
 *
 * @param resource $bevent Valid buffered event resource.
 * @param int $events Any combination of EV_READ and EV_WRITE.
 *
 * @return bool returns TRUE on success or FALSE on error.
 */
function event_buffer_disable($bevent, $events) {}

/**
 * <p>Sets the read and write timeouts for the specified buffered event.</p>
 * <p>(PECL libevent >= 0.0.1)</p>
 *
 * @link http://php.net/event_buffer_timeout_set
 *
 * @param resource $bevent Valid buffered event resource.
 * @param int $read_timeout Read timeout (in seconds).
 * @param int $write_timeout Write timeout (in seconds).
 *
 * @return void
 */
function event_buffer_timeout_set($bevent, $read_timeout, $write_timeout) {}

/**
 * <p>Set the watermarks for read and write events.</p>
 * <p>(PECL libevent >= 0.0.1)</p>
 *
 * <p>Libevent does not invoke read callback unless there is at least lowmark
 * bytes in the input buffer; if the read buffer is beyond the highmark,
 * reading is stopped. On output, the write callback is invoked whenever
 * the buffered data falls below the lowmark.</p>
 *
 * @link http://php.net/event_buffer_watermark_set
 *
 * @param resource $bevent Valid buffered event resource.
 * @param int $events Any combination of EV_READ and EV_WRITE.
 * @param int $lowmark Low watermark.
 * @param int $highmark High watermark.
 *
 * @return void
 */
function event_buffer_watermark_set($bevent, $events, $lowmark, $highmark) {}

/**
 * <p>Changes the file descriptor on which the buffered event operates.</p>
 * <p>(PECL libevent >= 0.0.1)</p>
 *
 * @link http://php.net/event_buffer_fd_set
 *
 * @param resource $bevent Valid buffered event resource.
 * @param resource $fd Valid PHP stream, must be castable to file descriptor.
 *
 * @return void
 */
function event_buffer_fd_set($bevent, $fd) {}

/**
 * <p>Set or reset callbacks for a buffered event</p>
 * <p>(PECL libevent >= 0.0.4)</p>
 *
 * <p>Sets or changes existing callbacks for the buffered event.</p>
 *
 * @link http://php.net/event_buffer_set_callback
 *
 * @param resource $bevent Valid buffered event resource.
 * @param callback|null $readcb Callback to invoke where there is data to read, or NULL if no callback is desired.
 * @param callback|null $writecb Callback to invoke where the descriptor is ready for writing, or NULL if no callback is desired.
 * @param callback|null $errorcb Callback to invoke where there is an error on the descriptor, cannot be NULL.
 * @param mixed $arg An argument that will be passed to each of the callbacks (optional).
 *
 * @return bool returns TRUE on success or FALSE on error.
 */
function event_buffer_set_callback($bevent, $readcb, $writecb, $errorcb, $arg = null) {}

function event_timer_new() {}

function event_timer_set($event, $callback, $arg = null) {}

function event_timer_pending($event, $timeout = -1) {}

function event_timer_add($event, $timeout = -1) {}

function event_timer_del($event) {}

// End of PECL libevent v.0.0.4
