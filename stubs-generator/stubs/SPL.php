<?php

// Start of SPL v.0.2

/**
 * Exception thrown if a logic expression is invalid
 * @link http://php.net/manual/en/class.logicexception.php
 */
class LogicException extends Exception  {
        protected $message;
        protected $code;
        protected $file;
        protected $line;


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
 * Exception thrown if a callback refers to an undefined function or if some arguments are missing
 * @link http://php.net/manual/en/class.badfunctioncallexception.php
 */
class BadFunctionCallException extends LogicException  {
        protected $message;
        protected $code;
        protected $file;
        protected $line;


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
 * Exception thrown if a callback refers to an undefined method or if some arguments are missing
 * @link http://php.net/manual/en/class.badmethodcallexception.php
 */
class BadMethodCallException extends BadFunctionCallException  {
        protected $message;
        protected $code;
        protected $file;
        protected $line;


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
 * Exception thrown if a value does not adhere to a defined valid data domain
 * @link http://php.net/manual/en/class.domainexception.php
 */
class DomainException extends LogicException  {
        protected $message;
        protected $code;
        protected $file;
        protected $line;


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
 * Exception thrown if an argument does not match with the expected value
 * @link http://php.net/manual/en/class.invalidargumentexception.php
 */
class InvalidArgumentException extends LogicException  {
        protected $message;
        protected $code;
        protected $file;
        protected $line;


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
 * Exception thrown if a length is invalid
 * @link http://php.net/manual/en/class.lengthexception.php
 */
class LengthException extends LogicException  {
        protected $message;
        protected $code;
        protected $file;
        protected $line;


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
 * Exception thrown when a value does not match with a range
 * @link http://php.net/manual/en/class.outofrangeexception.php
 */
class OutOfRangeException extends LogicException  {
        protected $message;
        protected $code;
        protected $file;
        protected $line;


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
 * Exception thrown if an error which can only be found on runtime occurs
 * @link http://php.net/manual/en/class.runtimeexception.php
 */
class RuntimeException extends Exception  {
        protected $message;
        protected $code;
        protected $file;
        protected $line;


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
 * Exception thrown if a value is not a valid key
 * @link http://php.net/manual/en/class.outofboundsexception.php
 */
class OutOfBoundsException extends RuntimeException  {
        protected $message;
        protected $code;
        protected $file;
        protected $line;


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
 * Exception thrown when you add an element into a full container
 * @link http://php.net/manual/en/class.overflowexception.php
 */
class OverflowException extends RuntimeException  {
        protected $message;
        protected $code;
        protected $file;
        protected $line;


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
 * Exception thrown when an invalid range is given.
 * @link http://php.net/manual/en/class.rangeexception.php
 */
class RangeException extends RuntimeException  {
        protected $message;
        protected $code;
        protected $file;
        protected $line;


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
 * Exception thrown when you try to remove an element of an empty container
 * @link http://php.net/manual/en/class.underflowexception.php
 */
class UnderflowException extends RuntimeException  {
        protected $message;
        protected $code;
        protected $file;
        protected $line;


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
 * Exception thrown if a value does not match with a set of values
 * @link http://php.net/manual/en/class.unexpectedvalueexception.php
 */
class UnexpectedValueException extends RuntimeException  {
        protected $message;
        protected $code;
        protected $file;
        protected $line;


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
 * Classes implementing RecursiveIterator can be used to iterate
 * over iterators recursively.
 * @link http://php.net/manual/en/class.recursiveiterator.php
 */
interface RecursiveIterator extends Iterator, Traversable {

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Returns if an iterator can be created fot the current entry.
	 * @link http://php.net/manual/en/recursiveiterator.haschildren.php
	 * @return bool true if the current entry can be iterated over, otherwise returns false.
	 */
    public function hasChildren ();

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Returns an iterator for the current entry.
	 * @link http://php.net/manual/en/recursiveiterator.getchildren.php
	 * @return RecursiveIterator An iterator for the current entry.
	 */
    public function getChildren ();

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Return the current element
	 * @link http://php.net/manual/en/iterator.current.php
	 * @return mixed Can return any type.
	 */
    public function current ();

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Move forward to next element
	 * @link http://php.net/manual/en/iterator.next.php
	 * @return void Any returned value is ignored.
	 */
    public function next ();

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Return the key of the current element
	 * @link http://php.net/manual/en/iterator.key.php
	 * @return scalar scalar on success, integer
	 * 0 on failure.
	 */
    public function key ();

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Checks if current position is valid
	 * @link http://php.net/manual/en/iterator.valid.php
	 * @return boolean The return value will be casted to boolean and then evaluated.
	 * Returns true on success or false on failure.
	 */
    public function valid ();

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Rewind the Iterator to the first element
	 * @link http://php.net/manual/en/iterator.rewind.php
	 * @return void Any returned value is ignored.
	 */
    public function rewind ();

}

/**
 * Can be used to iterate through recursive iterators.
 * @link http://php.net/manual/en/class.recursiveiteratoriterator.php
 */
class RecursiveIteratorIterator implements Iterator, Traversable, OuterIterator {
        const LEAVES_ONLY = 0;
        const SELF_FIRST = 1;
        const CHILD_FIRST = 2;
        const CATCH_GET_CHILD = 16;


        /**
	 * (PHP 5 &gt;= 5.1.3)<br/>
	 * Construct a RecursiveIteratorIterator
	 * @link http://php.net/manual/en/recursiveiteratoriterator.construct.php
	 * @param Traversable $iterator
	 * @param $mode [optional]
	 * @param $flags [optional]
         */
        public function __construct (Traversable $iterator, $mode, $flags) {}

	/**
	 * (PHP 5)<br/>
	 * Rewind the iterator to the first element of the top level inner iterator
	 * @link http://php.net/manual/en/recursiveiteratoriterator.rewind.php
	 * @return void 
	 */
        public function rewind () {}

	/**
	 * (PHP 5)<br/>
	 * Check whether the current position is valid
	 * @link http://php.net/manual/en/recursiveiteratoriterator.valid.php
	 * @return bool true if the current position is valid, otherwise false
	 */
        public function valid () {}

	/**
	 * (PHP 5)<br/>
	 * Access the current key
	 * @link http://php.net/manual/en/recursiveiteratoriterator.key.php
	 * @return mixed The current key.
	 */
        public function key () {}

	/**
	 * (PHP 5)<br/>
	 * Access the current element value
	 * @link http://php.net/manual/en/recursiveiteratoriterator.current.php
	 * @return mixed The current elements value.
	 */
        public function current () {}

	/**
	 * (PHP 5)<br/>
	 * Move forward to the next element
	 * @link http://php.net/manual/en/recursiveiteratoriterator.next.php
	 * @return void 
	 */
        public function next () {}

	/**
	 * (PHP 5)<br/>
	 * Get the current depth of the recursive iteration
	 * @link http://php.net/manual/en/recursiveiteratoriterator.getdepth.php
	 * @return int The current depth of the recursive iteration.
	 */
        public function getDepth () {}

        /**
	 * (PHP 5)<br/>
	 * The current active sub iterator
	 * @link http://php.net/manual/en/recursiveiteratoriterator.getsubiterator.php
	 * @param $level [optional]
	 * @return RecursiveIterator The current active sub iterator.
         */
        public function getSubIterator ($level) {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get inner iterator
	 * @link http://php.net/manual/en/recursiveiteratoriterator.getinneriterator.php
	 * @return iterator The current active sub iterator.
	 */
        public function getInnerIterator () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Begin Iteration
	 * @link http://php.net/manual/en/recursiveiteratoriterator.beginiteration.php
	 * @return void 
	 */
        public function beginIteration () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * End Iteration
	 * @link http://php.net/manual/en/recursiveiteratoriterator.enditeration.php
	 * @return void 
	 */
        public function endIteration () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Has children
	 * @link http://php.net/manual/en/recursiveiteratoriterator.callhaschildren.php
	 * @return bool true if the element has children, otherwise false
	 */
        public function callHasChildren () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get children
	 * @link http://php.net/manual/en/recursiveiteratoriterator.callgetchildren.php
	 * @return RecursiveIterator A RecursiveIterator.
	 */
        public function callGetChildren () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Begin children
	 * @link http://php.net/manual/en/recursiveiteratoriterator.beginchildren.php
	 * @return void 
	 */
        public function beginChildren () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * End children
	 * @link http://php.net/manual/en/recursiveiteratoriterator.endchildren.php
	 * @return void 
	 */
        public function endChildren () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Next element
	 * @link http://php.net/manual/en/recursiveiteratoriterator.nextelement.php
	 * @return void 
	 */
        public function nextElement () {}

        /**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Set max depth
	 * @link http://php.net/manual/en/recursiveiteratoriterator.setmaxdepth.php
	 * @param string $max_depth [optional] <p>
	 * The maximum allowed depth. -1 is used
	 * for any depth.
	 * </p>
	 * @return void 
	 */
	public function setMaxDepth ($max_depth = null) {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get max depth
	 * @link http://php.net/manual/en/recursiveiteratoriterator.getmaxdepth.php
	 * @return mixed The maximum accepted depth, or false if any depth is allowed.
         */
        public function getMaxDepth () {}

}

/**
 * Classes implementing OuterIterator can be used to iterate
 * over iterators.
 * @link http://php.net/manual/en/class.outeriterator.php
 */
interface OuterIterator extends Iterator, Traversable {

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Returns the inner iterator for the current entry.
	 * @link http://php.net/manual/en/outeriterator.getchildren.php
	 * @return Iterator The inner iterator for the current entry.
	 */
    public function getInnerIterator ();

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Return the current element
	 * @link http://php.net/manual/en/iterator.current.php
	 * @return mixed Can return any type.
	 */
    public function current ();

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Move forward to next element
	 * @link http://php.net/manual/en/iterator.next.php
	 * @return void Any returned value is ignored.
	 */
    public function next ();

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Return the key of the current element
	 * @link http://php.net/manual/en/iterator.key.php
	 * @return scalar scalar on success, integer
	 * 0 on failure.
	 */
    public function key ();

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Checks if current position is valid
	 * @link http://php.net/manual/en/iterator.valid.php
	 * @return boolean The return value will be casted to boolean and then evaluated.
	 * Returns true on success or false on failure.
	 */
    public function valid ();

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Rewind the Iterator to the first element
	 * @link http://php.net/manual/en/iterator.rewind.php
	 * @return void Any returned value is ignored.
	 */
    public function rewind ();

}

/**
 * This iterator wrapper allows the conversion of anything that is
 * Traversable into an Iterator.
 * It is important to understand that most classes that do not implement
 * Iterators have reasons as most likely they do not allow the full
 * Iterator feature set. If so, techniques should be provided to prevent
 * misuse, otherwise expect exceptions or fatal errors.
 * @link http://php.net/manual/en/class.iteratoriterator.php
 */
class IteratorIterator implements Iterator, Traversable, OuterIterator {

        /**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Create an iterator from anything that is traversable
	 * @link http://php.net/manual/en/iteratoriterator.construct.php
	 * @param Traversable $iterator
         */
        public function __construct (Traversable $iterator) {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Rewind to the first element
	 * @link http://php.net/manual/en/iteratoriterator.rewind.php
	 * @return void 
	 */
        public function rewind () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Checks if the iterator is valid
	 * @link http://php.net/manual/en/iteratoriterator.valid.php
	 * @return bool true if the iterator is valid, otherwise false
	 */
        public function valid () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get the key of the current element
	 * @link http://php.net/manual/en/iteratoriterator.key.php
	 * @return void The key of the current element.
	 */
        public function key () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get the current value
	 * @link http://php.net/manual/en/iteratoriterator.current.php
	 * @return void The value of the current element.
	 */
        public function current () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Forward to the next element
	 * @link http://php.net/manual/en/iteratoriterator.next.php
	 * @return void 
	 */
        public function next () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get the inner iterator
	 * @link http://php.net/manual/en/iteratoriterator.getinneriterator.php
	 * @return void The inner iterator as passed to IteratorIterator::__construct.
	 */
        public function getInnerIterator () {}

}

/**
 * This abstract iterator filters out unwanted values. This class should be extended to
 * implement custom iterator filters. The FilterIterator::accept()
 * must be implemented in the subclass.
 * @link http://php.net/manual/en/class.filteriterator.php
 */
abstract class FilterIterator extends IteratorIterator implements OuterIterator, Traversable, Iterator {

        /**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Construct a filterIterator
	 * @link http://php.net/manual/en/filteriterator.construct.php
	 * @param Iterator $iterator
         */
        public function __construct (Iterator $iterator) {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Rewind the iterator
	 * @link http://php.net/manual/en/filteriterator.rewind.php
	 * @return void 
	 */
        public function rewind () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Check whether the current element is valid
	 * @link http://php.net/manual/en/filteriterator.valid.php
	 * @return bool true if the current element is valid, otherwise false
	 */
        public function valid () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get the current key
	 * @link http://php.net/manual/en/filteriterator.key.php
	 * @return mixed The current key.
	 */
        public function key () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get the current element value
	 * @link http://php.net/manual/en/filteriterator.current.php
	 * @return mixed The current element value.
	 */
        public function current () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Move the iterator forward
	 * @link http://php.net/manual/en/filteriterator.next.php
	 * @return void 
	 */
        public function next () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get the inner iterator
	 * @link http://php.net/manual/en/filteriterator.getinneriterator.php
	 * @return Iterator The inner iterator.
	 */
        public function getInnerIterator () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Check whether the current element of the iterator is acceptable
	 * @link http://php.net/manual/en/filteriterator.accept.php
	 * @return bool true if the current element is acceptable, otherwise false.
	 */
        abstract public function accept ();

}

/**
 * This abstract iterator filters out unwanted values for a RecursiveIterator.
 * This class should be extended to implement custom filters. 
 * The RecursiveFilterIterator::accept() must be implemented in the subclass.
 * @link http://php.net/manual/en/class.recursivefilteriterator.php
 */
abstract class RecursiveFilterIterator extends FilterIterator implements Iterator, Traversable, OuterIterator, RecursiveIterator {

        /**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Create a RecursiveFilterIterator from a RecursiveIterator
	 * @link http://php.net/manual/en/recursivefilteriterator.construct.php
	 * @param RecursiveIterator $iterator
         */
        public function __construct (RecursiveIterator $iterator) {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Check whether the inner iterator's current element has children
	 * @link http://php.net/manual/en/recursivefilteriterator.haschildren.php
	 * @return void true if the inner iterator has children, otherwise false
	 */
        public function hasChildren () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Return the inner iterator's children contained in a RecursiveFilterIterator
	 * @link http://php.net/manual/en/recursivefilteriterator.getchildren.php
	 * @return void a RecursiveFilterIterator containing the inner iterator's children.
	 */
        public function getChildren () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Rewind the iterator
	 * @link http://php.net/manual/en/filteriterator.rewind.php
	 * @return void 
	 */
        public function rewind () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Check whether the current element is valid
	 * @link http://php.net/manual/en/filteriterator.valid.php
	 * @return bool true if the current element is valid, otherwise false
	 */
        public function valid () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get the current key
	 * @link http://php.net/manual/en/filteriterator.key.php
	 * @return mixed The current key.
	 */
        public function key () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get the current element value
	 * @link http://php.net/manual/en/filteriterator.current.php
	 * @return mixed The current element value.
	 */
        public function current () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Move the iterator forward
	 * @link http://php.net/manual/en/filteriterator.next.php
	 * @return void 
	 */
        public function next () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get the inner iterator
	 * @link http://php.net/manual/en/filteriterator.getinneriterator.php
	 * @return Iterator The inner iterator.
	 */
        public function getInnerIterator () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Check whether the current element of the iterator is acceptable
	 * @link http://php.net/manual/en/filteriterator.accept.php
	 * @return bool true if the current element is acceptable, otherwise false.
	 */
        abstract public function accept ();

}

/**
 * ...
 * @link http://php.net/manual/en/class.parentiterator.php
 */
class ParentIterator extends RecursiveFilterIterator implements RecursiveIterator, OuterIterator, Traversable, Iterator {

        /**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Constructs a ParentIterator
	 * @link http://php.net/manual/en/parentiterator.construct.php
	 * @param RecursiveIterator $iterator
         */
        public function __construct (RecursiveIterator $iterator) {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Determines acceptability
	 * @link http://php.net/manual/en/parentiterator.accept.php
	 * @return bool true if the current element is acceptable, otherwise false.
	 */
        public function accept () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Check whether the inner iterator's current element has children
	 * @link http://php.net/manual/en/recursivefilteriterator.haschildren.php
	 * @return void true if the inner iterator has children, otherwise false
	 */
        public function hasChildren () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Return the inner iterator's children contained in a RecursiveFilterIterator
	 * @link http://php.net/manual/en/recursivefilteriterator.getchildren.php
	 * @return void a RecursiveFilterIterator containing the inner iterator's children.
	 */
        public function getChildren () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Rewind the iterator
	 * @link http://php.net/manual/en/filteriterator.rewind.php
	 * @return void 
	 */
        public function rewind () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Check whether the current element is valid
	 * @link http://php.net/manual/en/filteriterator.valid.php
	 * @return bool true if the current element is valid, otherwise false
	 */
        public function valid () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get the current key
	 * @link http://php.net/manual/en/filteriterator.key.php
	 * @return mixed The current key.
	 */
        public function key () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get the current element value
	 * @link http://php.net/manual/en/filteriterator.current.php
	 * @return mixed The current element value.
	 */
        public function current () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Move the iterator forward
	 * @link http://php.net/manual/en/filteriterator.next.php
	 * @return void 
	 */
        public function next () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get the inner iterator
	 * @link http://php.net/manual/en/filteriterator.getinneriterator.php
	 * @return Iterator The inner iterator.
	 */
        public function getInnerIterator () {}

}

/**
 * Classes implementing Countable can be used with the
 * count function.
 * @link http://php.net/manual/en/class.countable.php
 */
interface Countable  {

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Count elements of an object
	 * @link http://php.net/manual/en/countable.count.php
	 * @return int The custom count as an integer.
	 * </p>
	 * <p>
	 * The return value is cast to an integer.
	 */
    public function count ();

}

/**
 * The Seekable iterator.
 * @link http://php.net/manual/en/class.seekableiterator.php
 */
interface SeekableIterator extends Iterator, Traversable {

        /**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Seeks to a position
	 * @link http://php.net/manual/en/seekableiterator.seek.php
	 * @param int $position <p>
	 * The position to seek to.
	 * </p>
	 * @return void 
         */
    public function seek ($position);

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Return the current element
	 * @link http://php.net/manual/en/iterator.current.php
	 * @return mixed Can return any type.
	 */
    public function current ();

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Move forward to next element
	 * @link http://php.net/manual/en/iterator.next.php
	 * @return void Any returned value is ignored.
	 */
    public function next ();

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Return the key of the current element
	 * @link http://php.net/manual/en/iterator.key.php
	 * @return scalar scalar on success, integer
	 * 0 on failure.
	 */
    public function key ();

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Checks if current position is valid
	 * @link http://php.net/manual/en/iterator.valid.php
	 * @return boolean The return value will be casted to boolean and then evaluated.
	 * Returns true on success or false on failure.
	 */
    public function valid ();

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Rewind the Iterator to the first element
	 * @link http://php.net/manual/en/iterator.rewind.php
	 * @return void Any returned value is ignored.
	 */
    public function rewind ();

}

/**
 * ...
 * @link http://php.net/manual/en/class.limititerator.php
 */
class LimitIterator extends IteratorIterator implements OuterIterator, Traversable, Iterator {

        /**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Construct a LimitIterator
	 * @link http://php.net/manual/en/limititerator.construct.php
	 * @param Iterator $iterator
	 * @param $offset [optional]
	 * @param $count [optional]
         */
        public function __construct (Iterator $iterator, $offset, $count) {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Rewind the iterator to the specified starting offset
	 * @link http://php.net/manual/en/limititerator.rewind.php
	 * @return void 
	 */
        public function rewind () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Check whether the current element is valid
	 * @link http://php.net/manual/en/limititerator.valid.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function valid () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get current key
	 * @link http://php.net/manual/en/limititerator.key.php
	 * @return mixed The current key.
	 */
        public function key () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get current element
	 * @link http://php.net/manual/en/limititerator.current.php
	 * @return mixed The current element.
	 */
        public function current () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Move the iterator forward
	 * @link http://php.net/manual/en/limititerator.next.php
	 * @return void 
	 */
        public function next () {}

        /**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Seek to the given position
	 * @link http://php.net/manual/en/limititerator.seek.php
	 * @param int $position <p>
	 * The position being seeked to.
	 * </p>
	 * @return void 
         */
        public function seek ($position) {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Return the current position
	 * @link http://php.net/manual/en/limititerator.getposition.php
	 * @return int The current position.
	 */
        public function getPosition () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get inner iterator
	 * @link http://php.net/manual/en/limititerator.getinneriterator.php
	 * @return Iterator The inner iterator.
	 */
        public function getInnerIterator () {}

}

/**
 * This object supports cached iteration over another iterator.
 * @link http://php.net/manual/en/class.cachingiterator.php
 */
class CachingIterator extends IteratorIterator implements OuterIterator, Traversable, Iterator, ArrayAccess, Countable {
        const CALL_TOSTRING = 1;
        const CATCH_GET_CHILD = 16;
        const TOSTRING_USE_KEY = 2;
        const TOSTRING_USE_CURRENT = 4;
        const TOSTRING_USE_INNER = 8;
        const FULL_CACHE = 256;


        /**
	 * (PHP 5)<br/>
	 * Construct a new CachingIterator object for the iterator.
	 * @link http://php.net/manual/en/cachingiterator.construct.php
	 * @param Iterator $iterator
	 * @param $flags [optional]
         */
        public function __construct (Iterator $iterator, $flags) {}

	/**
	 * (PHP 5)<br/>
	 * Rewind the iterator
	 * @link http://php.net/manual/en/cachingiterator.rewind.php
	 * @return void 
	 */
        public function rewind () {}

	/**
	 * (PHP 5)<br/>
	 * Check whether the current element is valid
	 * @link http://php.net/manual/en/cachingiterator.valid.php
	 * @return void Returns true on success or false on failure.
	 */
        public function valid () {}

	/**
	 * (PHP 5)<br/>
	 * Return the key for the current element
	 * @link http://php.net/manual/en/cachingiterator.key.php
	 * @return void 
	 */
        public function key () {}

	/**
	 * (PHP 5)<br/>
	 * Return the current element
	 * @link http://php.net/manual/en/cachingiterator.current.php
	 * @return void Mixed
	 */
        public function current () {}

	/**
	 * (PHP 5)<br/>
	 * Move the iterator forward
	 * @link http://php.net/manual/en/cachingiterator.next.php
	 * @return void 
	 */
        public function next () {}

	/**
	 * (PHP 5)<br/>
	 * Check whether the inner iterator has a valid next element
	 * @link http://php.net/manual/en/cachingiterator.hasnext.php
	 * @return void Returns true on success or false on failure.
	 */
        public function hasNext () {}

	/**
	 * (PHP 5)<br/>
	 * Return the string representation of the current element
	 * @link http://php.net/manual/en/cachingiterator.tostring.php
	 * @return void The string representation of the current element.
	 */
        public function __toString () {}

	/**
	 * (PHP 5)<br/>
	 * Return the innter iterator
	 * @link http://php.net/manual/en/cachingiterator.getinneriterator.php
	 * @return void an object implementing the Iterator interface.
	 */
        public function getInnerIterator () {}

	/**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Get flags used
	 * @link http://php.net/manual/en/cachingiterator.getflags.php
	 * @return void Description...
	 */
        public function getFlags () {}

        /**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * The setFlags purpose
	 * @link http://php.net/manual/en/cachingiterator.setflags.php
	 * @param bitmask $flags <p>
	 * Bitmask of the flags to set.
	 * </p>
	 * @return void 
         */
        public function setFlags ($flags) {}

        /**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * The offsetGet purpose
	 * @link http://php.net/manual/en/cachingiterator.offsetget.php
	 * @param string $index <p>
	 * Description...
	 * </p>
	 * @return void Description...
         */
        public function offsetGet ($index) {}

        /**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * The offsetSet purpose
	 * @link http://php.net/manual/en/cachingiterator.offsetset.php
	 * @param string $index <p>
	 * The index of the element to be set.
	 * </p>
	 * @param string $newval <p>
	 * The new value for the index.
	 * </p>
	 * @return void 
         */
        public function offsetSet ($index, $newval) {}

        /**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * The offsetUnset purpose
	 * @link http://php.net/manual/en/cachingiterator.offsetunset.php
	 * @param string $index <p>
	 * The index of the element to be unset.
	 * </p>
	 * @return void 
         */
        public function offsetUnset ($index) {}

        /**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * The offsetExists purpose
	 * @link http://php.net/manual/en/cachingiterator.offsetexists.php
	 * @param string $index <p>
	 * The index being checked.
	 * </p>
	 * @return void true if an entry referenced by the offset exists, false otherwise.
         */
        public function offsetExists ($index) {}

	/**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * The getCache purpose
	 * @link http://php.net/manual/en/cachingiterator.getcache.php
	 * @return void Description...
	 */
        public function getCache () {}

	/**
	 * (PHP 5 &gt;= 5.2.2)<br/>
	 * The number of elements in the iterator
	 * @link http://php.net/manual/en/cachingiterator.count.php
	 * @return void The count of the elements iterated over.
	 */
        public function count () {}

}

/**
 * ...
 * @link http://php.net/manual/en/class.recursivecachingiterator.php
 */
class RecursiveCachingIterator extends CachingIterator implements Countable, ArrayAccess, Iterator, Traversable, OuterIterator, RecursiveIterator {
        const CALL_TOSTRING = 1;
        const CATCH_GET_CHILD = 16;
        const TOSTRING_USE_KEY = 2;
        const TOSTRING_USE_CURRENT = 4;
        const TOSTRING_USE_INNER = 8;
        const FULL_CACHE = 256;


        /**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Construct
	 * @link http://php.net/manual/en/recursivecachingiterator.construct.php
	 * @param Iterator $iterator
	 * @param $flags [optional]
         */
        public function __construct (Iterator $iterator, $flags) {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Check whether the current element of the inner iterator has children
	 * @link http://php.net/manual/en/recursivecachingiterator.haschildren.php
	 * @return bool true if the inner iterator has children, otherwise false
	 */
        public function hasChildren () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Return the inner iterator's children as a RecursiveCachingIterator
	 * @link http://php.net/manual/en/recursivecachingiterator.getchildren.php
	 * @return RecursiveCachingIterator The inner iterator's children, as a RecursiveCachingIterator.
	 */
        public function getChildren () {}

	/**
	 * (PHP 5)<br/>
	 * Rewind the iterator
	 * @link http://php.net/manual/en/cachingiterator.rewind.php
	 * @return void 
	 */
        public function rewind () {}

	/**
	 * (PHP 5)<br/>
	 * Check whether the current element is valid
	 * @link http://php.net/manual/en/cachingiterator.valid.php
	 * @return void Returns true on success or false on failure.
	 */
        public function valid () {}

	/**
	 * (PHP 5)<br/>
	 * Return the key for the current element
	 * @link http://php.net/manual/en/cachingiterator.key.php
	 * @return void 
	 */
        public function key () {}

	/**
	 * (PHP 5)<br/>
	 * Return the current element
	 * @link http://php.net/manual/en/cachingiterator.current.php
	 * @return void Mixed
	 */
        public function current () {}

	/**
	 * (PHP 5)<br/>
	 * Move the iterator forward
	 * @link http://php.net/manual/en/cachingiterator.next.php
	 * @return void 
	 */
        public function next () {}

	/**
	 * (PHP 5)<br/>
	 * Check whether the inner iterator has a valid next element
	 * @link http://php.net/manual/en/cachingiterator.hasnext.php
	 * @return void Returns true on success or false on failure.
	 */
        public function hasNext () {}

	/**
	 * (PHP 5)<br/>
	 * Return the string representation of the current element
	 * @link http://php.net/manual/en/cachingiterator.tostring.php
	 * @return void The string representation of the current element.
	 */
        public function __toString () {}

	/**
	 * (PHP 5)<br/>
	 * Return the innter iterator
	 * @link http://php.net/manual/en/cachingiterator.getinneriterator.php
	 * @return void an object implementing the Iterator interface.
	 */
        public function getInnerIterator () {}

	/**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Get flags used
	 * @link http://php.net/manual/en/cachingiterator.getflags.php
	 * @return void Description...
	 */
        public function getFlags () {}

        /**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * The setFlags purpose
	 * @link http://php.net/manual/en/cachingiterator.setflags.php
	 * @param bitmask $flags <p>
	 * Bitmask of the flags to set.
	 * </p>
	 * @return void 
         */
        public function setFlags ($flags) {}

        /**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * The offsetGet purpose
	 * @link http://php.net/manual/en/cachingiterator.offsetget.php
	 * @param string $index <p>
	 * Description...
	 * </p>
	 * @return void Description...
         */
        public function offsetGet ($index) {}

        /**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * The offsetSet purpose
	 * @link http://php.net/manual/en/cachingiterator.offsetset.php
	 * @param string $index <p>
	 * The index of the element to be set.
	 * </p>
	 * @param string $newval <p>
	 * The new value for the index.
	 * </p>
	 * @return void 
         */
        public function offsetSet ($index, $newval) {}

        /**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * The offsetUnset purpose
	 * @link http://php.net/manual/en/cachingiterator.offsetunset.php
	 * @param string $index <p>
	 * The index of the element to be unset.
	 * </p>
	 * @return void 
         */
        public function offsetUnset ($index) {}

        /**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * The offsetExists purpose
	 * @link http://php.net/manual/en/cachingiterator.offsetexists.php
	 * @param string $index <p>
	 * The index being checked.
	 * </p>
	 * @return void true if an entry referenced by the offset exists, false otherwise.
         */
        public function offsetExists ($index) {}

	/**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * The getCache purpose
	 * @link http://php.net/manual/en/cachingiterator.getcache.php
	 * @return void Description...
	 */
        public function getCache () {}

	/**
	 * (PHP 5 &gt;= 5.2.2)<br/>
	 * The number of elements in the iterator
	 * @link http://php.net/manual/en/cachingiterator.count.php
	 * @return void The count of the elements iterated over.
	 */
        public function count () {}

}

/**
 * This iterator cannot be rewinded.
 * @link http://php.net/manual/en/class.norewinditerator.php
 */
class NoRewindIterator extends IteratorIterator implements OuterIterator, Traversable, Iterator {

        /**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Construct a NoRewindIterator
	 * @link http://php.net/manual/en/norewinditerator.construct.php
	 * @param Iterator $iterator
         */
        public function __construct (Iterator $iterator) {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Prevents the rewind operation on the inner iterator.
	 * @link http://php.net/manual/en/norewinditerator.haschildren.php
	 * @return void 
	 */
        public function rewind () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Validates the iterator
	 * @link http://php.net/manual/en/norewinditerator.valid.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function valid () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get the current key
	 * @link http://php.net/manual/en/norewinditerator.key.php
	 * @return mixed The current key.
	 */
        public function key () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get the current value
	 * @link http://php.net/manual/en/norewinditerator.current.php
	 * @return mixed The current value.
	 */
        public function current () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Forward to the next element
	 * @link http://php.net/manual/en/norewinditerator.next.php
	 * @return void 
	 */
        public function next () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get the inner iterator
	 * @link http://php.net/manual/en/norewinditerator.getinneriterator.php
	 * @return iterator The inner iterator, as passed to NoRewindIterator::__construct.
	 */
        public function getInnerIterator () {}

}

/**
 * Iterate over multiple collections.
 * @link http://php.net/manual/en/class.appenditerator.php
 */
class AppendIterator extends IteratorIterator implements OuterIterator, Traversable, Iterator {

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Constructs an AppendIterator
	 * @link http://php.net/manual/en/appenditerator.construct.php
	 */
        public function __construct () {}

        /**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Appends an iterator
	 * @link http://php.net/manual/en/appenditerator.append.php
	 * @param Iterator $iterator <p>
	 * The iterator to append.
	 * </p>
	 * @return void 
	 */
	public function append ($iterator) {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Rewinds the Iterator
	 * @link http://php.net/manual/en/appenditerator.rewind.php
	 * @return void 
	 */
        public function rewind () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Checks validity of the current element
	 * @link http://php.net/manual/en/appenditerator.valid.php
	 * @return void Returns true on success or false on failure.
	 */
        public function valid () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Gets the current key
	 * @link http://php.net/manual/en/appenditerator.key.php
	 * @return void The current key if it is valid or &null; otherwise.
	 */
        public function key () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Gets the current value
	 * @link http://php.net/manual/en/appenditerator.current.php
	 * @return void The current value if it is valid or &null; otherwise.
	 */
        public function current () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Moves to the next element
	 * @link http://php.net/manual/en/appenditerator.next.php
	 * @return void 
	 */
        public function next () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Gets an inner iterator
	 * @link http://php.net/manual/en/appenditerator.getinneriterator.php
	 * @return void The current inner Iterator.
	 */
        public function getInnerIterator () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Gets an index of iterators
	 * @link http://php.net/manual/en/appenditerator.getiteratorindex.php
	 * @return void The index of iterators.
	 */
        public function getIteratorIndex () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * The getArrayIterator method
	 * @link http://php.net/manual/en/appenditerator.getarrayiterator.php
	 * @return void 
	 */
        public function getArrayIterator () {}

}

/**
 * The InfiniteIterator allows one to
 * infinitely iterate over an iterator without having to manually
 * rewind the iterator upon reaching its end.
 * @link http://php.net/manual/en/class.infiniteiterator.php
 */
class InfiniteIterator extends IteratorIterator implements OuterIterator, Traversable, Iterator {

        /**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Constructs an InfiniteIterator
	 * @link http://php.net/manual/en/infiniteiterator.construct.php
	 * @param Iterator $iterator
         */
        public function __construct (Iterator $iterator) {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Moves the inner Iterator forward or rewinds it
	 * @link http://php.net/manual/en/infiniteiterator.next.php
	 * @return void 
	 */
        public function next () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Rewind to the first element
	 * @link http://php.net/manual/en/iteratoriterator.rewind.php
	 * @return void 
	 */
        public function rewind () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Checks if the iterator is valid
	 * @link http://php.net/manual/en/iteratoriterator.valid.php
	 * @return bool true if the iterator is valid, otherwise false
	 */
        public function valid () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get the key of the current element
	 * @link http://php.net/manual/en/iteratoriterator.key.php
	 * @return void The key of the current element.
	 */
        public function key () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get the current value
	 * @link http://php.net/manual/en/iteratoriterator.current.php
	 * @return void The value of the current element.
	 */
        public function current () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get the inner iterator
	 * @link http://php.net/manual/en/iteratoriterator.getinneriterator.php
	 * @return void The inner iterator as passed to IteratorIterator::__construct.
	 */
        public function getInnerIterator () {}

}

/**
 * This iterator can be used to filter another iterator via a regular expression.
 * @link http://php.net/manual/en/class.regexiterator.php
 */
class RegexIterator extends FilterIterator implements Iterator, Traversable, OuterIterator {
        const USE_KEY = 1;
        const MATCH = 0;
        const GET_MATCH = 1;
        const ALL_MATCHES = 2;
        const SPLIT = 3;
        const REPLACE = 4;

        public $replacement;


        /**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Creates a new RegexIterator.
	 * @link http://php.net/manual/en/regexiterator.construct.php
	 * @param Iterator $iterator
	 * @param $regex
	 * @param $mode [optional]
	 * @param $flags [optional]
	 * @param $preg_flags [optional]
         */
        public function __construct (Iterator $iterator, $regex, $mode, $flags, $preg_flags) {}

	/**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Get accept status
	 * @link http://php.net/manual/en/regexiterator.accept.php
	 * @return bool true if a match, otherwise false
	 */
        public function accept () {}

	/**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Returns operation mode.
	 * @link http://php.net/manual/en/regexiterator.getmode.php
	 * @return int the operation mode.
	 */
        public function getMode () {}

        /**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Sets the operation mode.
	 * @link http://php.net/manual/en/regexiterator.setmode.php
	 * @param int $mode <p>
	 * The operation mode.
	 * </p>
	 * <p>
	 * The available modes are listed below. The actual
	 * meanings of these modes are described in the
	 * predefined constants.
	 * <table>
	 * RegexIterator modes
	 * <tr valign="top">
	 * <td>value</td>
	 * <td>constant</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>0</td>
	 * <td>
	 * RegexIterator::MATCH
	 * </td>
	 * </tr>
	 * <tr valign="top">
	 * <td>1</td>
	 * <td>
	 * RegexIterator::GET_MATCH
	 * </td>
	 * </tr>
	 * <tr valign="top">
	 * <td>2</td>
	 * <td>
	 * RegexIterator::ALL_MATCHES
	 * </td>
	 * </tr>
	 * <tr valign="top">
	 * <td>3</td>
	 * <td>
	 * RegexIterator::SPLIT
	 * </td>
	 * </tr>
	 * <tr valign="top">
	 * <td>4</td>
	 * <td>
	 * RegexIterator::REPLACE
	 * </td>
	 * </tr>
	 * </table>
	 * </p>
	 * @return void 
         */
        public function setMode ($mode) {}

	/**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Returns the special flags.
	 * @link http://php.net/manual/en/regexiterator.getflags.php
	 * @return int the special flags.
	 */
        public function getFlags () {}

        /**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Sets the special flags.
	 * @link http://php.net/manual/en/regexiterator.setflags.php
	 * @param int $flags <p>
	 * The special flags. It takes a bitmask of class constants.
	 * </p>
	 * <p>
	 * The available flags are listed below. The actual
	 * meanings of these flags are described in the
	 * predefined constants.
	 * <table>
	 * RegexIterator special flags
	 * <tr valign="top">
	 * <td>value</td>
	 * <td>constant</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>1</td>
	 * <td>
	 * RegexIterator::USE_KEY
	 * </td>
	 * </tr>
	 * </table>
	 * </p>
	 * @return void 
         */
        public function setFlags ($flags) {}

	/**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Returns the regular expression flags.
	 * @link http://php.net/manual/en/regexiterator.getpregflags.php
	 * @return int the regular expression flags.
	 */
        public function getPregFlags () {}

        /**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Sets the regular expression flags.
	 * @link http://php.net/manual/en/regexiterator.setpregflags.php
	 * @param int $preg_flags <p>
	 * The regular expression flags. See RegexIterator::__construct
	 * for an overview of available flags.
	 * </p>
	 * @return void 
         */
        public function setPregFlags ($preg_flags) {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Rewind the iterator
	 * @link http://php.net/manual/en/filteriterator.rewind.php
	 * @return void 
	 */
        public function rewind () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Check whether the current element is valid
	 * @link http://php.net/manual/en/filteriterator.valid.php
	 * @return bool true if the current element is valid, otherwise false
	 */
        public function valid () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get the current key
	 * @link http://php.net/manual/en/filteriterator.key.php
	 * @return mixed The current key.
	 */
        public function key () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get the current element value
	 * @link http://php.net/manual/en/filteriterator.current.php
	 * @return mixed The current element value.
	 */
        public function current () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Move the iterator forward
	 * @link http://php.net/manual/en/filteriterator.next.php
	 * @return void 
	 */
        public function next () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get the inner iterator
	 * @link http://php.net/manual/en/filteriterator.getinneriterator.php
	 * @return Iterator The inner iterator.
	 */
        public function getInnerIterator () {}

}

/**
 * This recursive iterator can filter another recursive iterator via a regular expression.
 * @link http://php.net/manual/en/class.recursiveregexiterator.php
 */
class RecursiveRegexIterator extends RegexIterator implements OuterIterator, Traversable, Iterator, RecursiveIterator {
        const USE_KEY = 1;
        const MATCH = 0;
        const GET_MATCH = 1;
        const ALL_MATCHES = 2;
        const SPLIT = 3;
        const REPLACE = 4;

        public $replacement;


        /**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Creates a new RecursiveRegexIterator.
	 * @link http://php.net/manual/en/recursiveregexiterator.construct.php
	 * @param RecursiveIterator $iterator
	 * @param $regex
	 * @param $mode [optional]
	 * @param $flags [optional]
	 * @param $preg_flags [optional]
         */
        public function __construct (RecursiveIterator $iterator, $regex, $mode, $flags, $preg_flags) {}

	/**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Returns whether an iterator can be obtained for the current entry.
	 * @link http://php.net/manual/en/recursiveregexiterator.haschildren.php
	 * @return bool true if an iterator can be obtained for the current entry, otherwise returns false.
	 */
        public function hasChildren () {}

	/**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Returns an iterator for the current entry.
	 * @link http://php.net/manual/en/recursiveregexiterator.getchildren.php
	 * @return RecursiveRegexIterator An iterator for the current entry, if it can be iterated over by the inner iterator.
	 */
        public function getChildren () {}

	/**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Get accept status
	 * @link http://php.net/manual/en/regexiterator.accept.php
	 * @return bool true if a match, otherwise false
	 */
        public function accept () {}

	/**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Returns operation mode.
	 * @link http://php.net/manual/en/regexiterator.getmode.php
	 * @return int the operation mode.
	 */
        public function getMode () {}

        /**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Sets the operation mode.
	 * @link http://php.net/manual/en/regexiterator.setmode.php
	 * @param int $mode <p>
	 * The operation mode.
	 * </p>
	 * <p>
	 * The available modes are listed below. The actual
	 * meanings of these modes are described in the
	 * predefined constants.
	 * <table>
	 * RegexIterator modes
	 * <tr valign="top">
	 * <td>value</td>
	 * <td>constant</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>0</td>
	 * <td>
	 * RegexIterator::MATCH
	 * </td>
	 * </tr>
	 * <tr valign="top">
	 * <td>1</td>
	 * <td>
	 * RegexIterator::GET_MATCH
	 * </td>
	 * </tr>
	 * <tr valign="top">
	 * <td>2</td>
	 * <td>
	 * RegexIterator::ALL_MATCHES
	 * </td>
	 * </tr>
	 * <tr valign="top">
	 * <td>3</td>
	 * <td>
	 * RegexIterator::SPLIT
	 * </td>
	 * </tr>
	 * <tr valign="top">
	 * <td>4</td>
	 * <td>
	 * RegexIterator::REPLACE
	 * </td>
	 * </tr>
	 * </table>
	 * </p>
	 * @return void 
         */
        public function setMode ($mode) {}

	/**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Returns the special flags.
	 * @link http://php.net/manual/en/regexiterator.getflags.php
	 * @return int the special flags.
	 */
        public function getFlags () {}

        /**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Sets the special flags.
	 * @link http://php.net/manual/en/regexiterator.setflags.php
	 * @param int $flags <p>
	 * The special flags. It takes a bitmask of class constants.
	 * </p>
	 * <p>
	 * The available flags are listed below. The actual
	 * meanings of these flags are described in the
	 * predefined constants.
	 * <table>
	 * RegexIterator special flags
	 * <tr valign="top">
	 * <td>value</td>
	 * <td>constant</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>1</td>
	 * <td>
	 * RegexIterator::USE_KEY
	 * </td>
	 * </tr>
	 * </table>
	 * </p>
	 * @return void 
         */
        public function setFlags ($flags) {}

	/**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Returns the regular expression flags.
	 * @link http://php.net/manual/en/regexiterator.getpregflags.php
	 * @return int the regular expression flags.
	 */
        public function getPregFlags () {}

        /**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Sets the regular expression flags.
	 * @link http://php.net/manual/en/regexiterator.setpregflags.php
	 * @param int $preg_flags <p>
	 * The regular expression flags. See RegexIterator::__construct
	 * for an overview of available flags.
	 * </p>
	 * @return void 
         */
        public function setPregFlags ($preg_flags) {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Rewind the iterator
	 * @link http://php.net/manual/en/filteriterator.rewind.php
	 * @return void 
	 */
        public function rewind () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Check whether the current element is valid
	 * @link http://php.net/manual/en/filteriterator.valid.php
	 * @return bool true if the current element is valid, otherwise false
	 */
        public function valid () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get the current key
	 * @link http://php.net/manual/en/filteriterator.key.php
	 * @return mixed The current key.
	 */
        public function key () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get the current element value
	 * @link http://php.net/manual/en/filteriterator.current.php
	 * @return mixed The current element value.
	 */
        public function current () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Move the iterator forward
	 * @link http://php.net/manual/en/filteriterator.next.php
	 * @return void 
	 */
        public function next () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get the inner iterator
	 * @link http://php.net/manual/en/filteriterator.getinneriterator.php
	 * @return Iterator The inner iterator.
	 */
        public function getInnerIterator () {}

}

/**
 * The EmptyIterator class for an empty iterator.
 * @link http://php.net/manual/en/class.emptyiterator.php
 */
class EmptyIterator implements Iterator, Traversable {

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * The rewind() method
	 * @link http://php.net/manual/en/emptyiterator.rewind.php
	 * @return void 
	 */
        public function rewind () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * The valid() method
	 * @link http://php.net/manual/en/emptyiterator.valid.php
	 * @return void false
	 */
        public function valid () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * The key() method
	 * @link http://php.net/manual/en/emptyiterator.key.php
	 * @return void 
	 */
        public function key () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * The current() method
	 * @link http://php.net/manual/en/emptyiterator.current.php
	 * @return void 
	 */
        public function current () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * The next() method
	 * @link http://php.net/manual/en/emptyiterator.next.php
	 * @return void 
	 */
        public function next () {}

}

/**
 * Allows iterating over a RecursiveIterator to generate an ASCII graphic tree.
 * @link http://php.net/manual/en/class.recursivetreeiterator.php
 */
class RecursiveTreeIterator extends RecursiveIteratorIterator implements OuterIterator, Traversable, Iterator {
        const LEAVES_ONLY = 0;
        const SELF_FIRST = 1;
        const CHILD_FIRST = 2;
        const CATCH_GET_CHILD = 16;
        const BYPASS_CURRENT = 4;
        const BYPASS_KEY = 8;
        const PREFIX_LEFT = 0;
        const PREFIX_MID_HAS_NEXT = 1;
        const PREFIX_MID_LAST = 2;
        const PREFIX_END_HAS_NEXT = 3;
        const PREFIX_END_LAST = 4;
        const PREFIX_RIGHT = 5;


        /**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Construct a RecursiveTreeIterator
	 * @link http://php.net/manual/en/recursivetreeiterator.construct.php
	 * @param Traversable $iterator
	 * @param $flags [optional]
	 * @param $caching_it_flags [optional]
	 * @param $mode [optional]
         */
        public function __construct (Traversable $iterator, $flags, $caching_it_flags, $mode) {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Rewind iterator
	 * @link http://php.net/manual/en/recursivetreeiterator.rewind.php
	 * @return void 
	 */
        public function rewind () {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Check validity
	 * @link http://php.net/manual/en/recursivetreeiterator.valid.php
	 * @return bool true if the current position is valid, otherwise false
	 */
        public function valid () {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Get the key of the current element
	 * @link http://php.net/manual/en/recursivetreeiterator.key.php
	 * @return string the current key prefixed and postfixed.
	 */
        public function key () {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Get current element
	 * @link http://php.net/manual/en/recursivetreeiterator.current.php
	 * @return string the current element prefixed and postfixed.
	 */
        public function current () {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Move to next element
	 * @link http://php.net/manual/en/recursivetreeiterator.next.php
	 * @return void 
	 */
        public function next () {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Begin iteration
	 * @link http://php.net/manual/en/recursivetreeiterator.beginiteration.php
	 * @return RecursiveIterator A RecursiveIterator.
	 */
        public function beginIteration () {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * End iteration
	 * @link http://php.net/manual/en/recursivetreeiterator.enditeration.php
	 * @return void 
	 */
        public function endIteration () {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Has children
	 * @link http://php.net/manual/en/recursivetreeiterator.callhaschildren.php
	 * @return bool true if there are children, otherwise false
	 */
        public function callHasChildren () {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Get children
	 * @link http://php.net/manual/en/recursivetreeiterator.callgetchildren.php
	 * @return RecursiveIterator A RecursiveIterator.
	 */
        public function callGetChildren () {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Begin children
	 * @link http://php.net/manual/en/recursivetreeiterator.beginchildren.php
	 * @return void 
	 */
        public function beginChildren () {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * End children
	 * @link http://php.net/manual/en/recursivetreeiterator.endchildren.php
	 * @return void 
	 */
        public function endChildren () {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Next element
	 * @link http://php.net/manual/en/recursivetreeiterator.nextelement.php
	 * @return void 
	 */
        public function nextElement () {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Get the prefix
	 * @link http://php.net/manual/en/recursivetreeiterator.getprefix.php
	 * @return string the string to place in front of current element
	 */
        public function getPrefix () {}

        /**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Set a part of the prefix
	 * @link http://php.net/manual/en/recursivetreeiterator.setprefixpart.php
	 * @param int $part <p>
	 * One of the RecursiveTreeIterator::PREFIX_* constants.
	 * </p>
	 * @param string $value <p>
	 * The value to assign to the part of the prefix specified in part.
	 * </p>
	 * @return void 
         */
        public function setPrefixPart ($part, $value) {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Get current entry
	 * @link http://php.net/manual/en/recursivetreeiterator.getentry.php
	 * @return string the part of the tree built for the current element.
	 */
        public function getEntry () {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Get the postfix
	 * @link http://php.net/manual/en/recursivetreeiterator.getpostfix.php
	 * @return void the string to place after the current element.
	 */
        public function getPostfix () {}

	/**
	 * (PHP 5)<br/>
	 * Get the current depth of the recursive iteration
	 * @link http://php.net/manual/en/recursiveiteratoriterator.getdepth.php
	 * @return int The current depth of the recursive iteration.
	 */
        public function getDepth () {}

        /**
	 * (PHP 5)<br/>
	 * The current active sub iterator
	 * @link http://php.net/manual/en/recursiveiteratoriterator.getsubiterator.php
	 * @param $level [optional]
	 * @return RecursiveIterator The current active sub iterator.
         */
        public function getSubIterator ($level) {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get inner iterator
	 * @link http://php.net/manual/en/recursiveiteratoriterator.getinneriterator.php
	 * @return iterator The current active sub iterator.
	 */
        public function getInnerIterator () {}

        /**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Set max depth
	 * @link http://php.net/manual/en/recursiveiteratoriterator.setmaxdepth.php
	 * @param string $max_depth [optional] <p>
	 * The maximum allowed depth. -1 is used
	 * for any depth.
	 * </p>
	 * @return void 
	 */
	public function setMaxDepth ($max_depth = null) {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get max depth
	 * @link http://php.net/manual/en/recursiveiteratoriterator.getmaxdepth.php
	 * @return mixed The maximum accepted depth, or false if any depth is allowed.
         */
        public function getMaxDepth () {}

}

/**
 * This class allows objects to work as arrays.
 * @link http://php.net/manual/en/class.arrayobject.php
 */
class ArrayObject implements IteratorAggregate, Traversable, ArrayAccess, Serializable, Countable {
        const STD_PROP_LIST = 1;
        const ARRAY_AS_PROPS = 2;


    /**
     * (PHP 5 &gt;= 5.0.0)<br/>
     * Construct a new array object
     * @link http://php.net/manual/en/arrayobject.construct.php
     * @param array|object $input The input parameter accepts an array or an Object.
     * @param int $flags Flags to control the behaviour of the ArrayObject object.
     * @param string $iterator_class Specify the class that will be used for iteration of the ArrayObject object. ArrayIterator is the default class used.
     *
     */
    public function __construct ($input=null, int $flags=0, string $iterator_class="ArrayIterator") {}

        /**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Returns whether the requested index exists
	 * @link http://php.net/manual/en/arrayobject.offsetexists.php
	 * @param mixed $index <p>
	 * The index being checked.
	 * </p>
	 * @return bool true if the requested index exists, otherwise false
         */
        public function offsetExists ($index) {}

        /**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Returns the value at the specified index
	 * @link http://php.net/manual/en/arrayobject.offsetget.php
	 * @param mixed $index <p>
	 * The index with the value.
	 * </p>
	 * @return mixed The value at the specified index or false.
         */
        public function offsetGet ($index) {}

        /**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Sets the value at the specified index to newval
	 * @link http://php.net/manual/en/arrayobject.offsetset.php
	 * @param mixed $index <p>
	 * The index being set.
	 * </p>
	 * @param mixed $newval <p>
	 * The new value for the index.
	 * </p>
	 * @return void 
         */
        public function offsetSet ($index, $newval) {}

        /**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Unsets the value at the specified index
	 * @link http://php.net/manual/en/arrayobject.offsetunset.php
	 * @param mixed $index <p>
	 * The index being unset.
	 * </p>
	 * @return void 
         */
        public function offsetUnset ($index) {}

        /**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Appends the value
	 * @link http://php.net/manual/en/arrayobject.append.php
	 * @param mixed $value <p>
	 * The value being appended.
	 * </p>
	 * @return void 
         */
        public function append ($value) {}

	/**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Creates a copy of the ArrayObject.
	 * @link http://php.net/manual/en/arrayobject.getarraycopy.php
	 * @return array a copy of the array. When the ArrayObject refers to an object
	 * an array of the public properties of that object will be returned.
	 */
        public function getArrayCopy () {}

	/**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Get the number of public properties in the ArrayObject
	 * @link http://php.net/manual/en/arrayobject.count.php
	 * @return int The number of public properties in the ArrayObject.
	 * </p>
	 * <p>
	 * When the ArrayObject is constructed from an array all properties are public.
	 */
        public function count () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Gets the behavior flags.
	 * @link http://php.net/manual/en/arrayobject.getflags.php
	 * @return int the behavior flags of the ArrayObject.
	 */
        public function getFlags () {}

        /**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Sets the behavior flags.
	 * @link http://php.net/manual/en/arrayobject.setflags.php
	 * @param int $flags <p>
	 * The new ArrayObject behavior.
	 * It takes on either a bitmask, or named constants. Using named
	 * constants is strongly encouraged to ensure compatibility for future
	 * versions.
	 * </p>
	 * <p>
	 * The available behavior flags are listed below. The actual
	 * meanings of these flags are described in the
	 * predefined constants.
	 * <table>
	 * ArrayObject behavior flags
	 * <tr valign="top">
	 * <td>value</td>
	 * <td>constant</td>
	 * </tr>
	 * <tr valign="top">
	 * <td>1</td>
	 * <td>
	 * ArrayObject::STD_PROP_LIST
	 * </td>
	 * </tr>
	 * <tr valign="top">
	 * <td>2</td>
	 * <td>
	 * ArrayObject::ARRAY_AS_PROPS
	 * </td>
	 * </tr>
	 * </table>
	 * </p>
	 * @return void 
         */
        public function setFlags ($flags) {}

	/**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Sort the entries by value
	 * @link http://php.net/manual/en/arrayobject.asort.php
	 * @return void 
	 */
        public function asort () {}

	/**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Sort the entries by key
	 * @link http://php.net/manual/en/arrayobject.ksort.php
	 * @return void 
	 */
        public function ksort () {}

        /**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Sort the entries with a user-defined comparison function and maintain key association
	 * @link http://php.net/manual/en/arrayobject.uasort.php
	 * @param callback $cmp_function <p>
	 * Function cmp_function should accept two
	 * parameters which will be filled by pairs of entries.
	 * The comparison function must return an integer less than, equal
	 * to, or greater than zero if the first argument is considered to
	 * be respectively less than, equal to, or greater than the
	 * second.
	 * </p>
	 * @return void 
         */
        public function uasort ($cmp_function) {}

        /**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Sort the entries by keys using a user-defined comparison function
	 * @link http://php.net/manual/en/arrayobject.uksort.php
	 * @param callback $cmp_function <p>
	 * The callback comparison function.
	 * </p>
	 * <p>
	 * Function cmp_function should accept two
	 * parameters which will be filled by pairs of entry keys.
	 * The comparison function must return an integer less than, equal
	 * to, or greater than zero if the first argument is considered to
	 * be respectively less than, equal to, or greater than the
	 * second.
	 * </p>
	 * @return void 
         */
        public function uksort ($cmp_function) {}

	/**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Sort entries using a "natural order" algorithm
	 * @link http://php.net/manual/en/arrayobject.natsort.php
	 * @return void 
	 */
        public function natsort () {}

	/**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Sort an array using a case insensitive "natural order" algorithm
	 * @link http://php.net/manual/en/arrayobject.natcasesort.php
	 * @return void 
	 */
        public function natcasesort () {}

        /**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Unserialize an ArrayObject
	 * @link http://php.net/manual/en/arrayobject.unserialize.php
	 * @param string $serialized <p>
	 * The serialized ArrayObject.
	 * </p>
	 * @return void The unserialized ArrayObject.
         */
        public function unserialize ($serialized) {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Serialize an ArrayObject
	 * @link http://php.net/manual/en/arrayobject.serialize.php
	 * @return void The serialized representation of the ArrayObject.
	 */
        public function serialize () {}

        /**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Create a new iterator from an ArrayObject instance
	 * @link http://php.net/manual/en/arrayobject.getiterator.php
	 * @return ArrayIterator An iterator from an ArrayObject.
         */
	public function getIterator () {}

        /**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Exchange the array for another one.
	 * @link http://php.net/manual/en/arrayobject.exchangearray.php
	 * @param mixed $input <p>
	 * The new array or object to exchange with the current array.
	 * </p>
	 * @return array the old array.
	 */
	public function exchangeArray ($input) {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Sets the iterator classname for the ArrayObject.
	 * @link http://php.net/manual/en/arrayobject.setiteratorclass.php
	 * @param string $iterator_class <p>
	 * The classname of the array iterator to use when iterating over this object.
	 * </p>
	 * @return void 
	 */
	public function setIteratorClass ($iterator_class) {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Gets the iterator classname for the ArrayObject.
	 * @link http://php.net/manual/en/arrayobject.getiteratorclass.php
	 * @return int the iterator class name that is used to iterate over this object.
         */
        public function getIteratorClass () {}

}

/**
 * This iterator allows to unset and modify values and keys while iterating
 * over Arrays and Objects.
 * @link http://php.net/manual/en/class.arrayiterator.php
 */
class ArrayIterator implements Iterator, Traversable, ArrayAccess, SeekableIterator, Serializable, Countable {
        const STD_PROP_LIST = 1;
        const ARRAY_AS_PROPS = 2;


        /**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Construct an ArrayIterator
	 * @link http://php.net/manual/en/arrayiterator.construct.php
	 * @param $array
         */
        public function __construct ($array) {}

        /**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Check if offset exists
	 * @link http://php.net/manual/en/arrayiterator.offsetexists.php
	 * @param string $index <p>
	 * The offset being checked.
	 * </p>
	 * @return void true if the offset exists, otherwise false
         */
        public function offsetExists ($index) {}

        /**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Get value for an offset
	 * @link http://php.net/manual/en/arrayiterator.offsetget.php
	 * @param string $index <p>
	 * The offset to get the value from.
	 * </p>
	 * @return mixed The value at offset index.
         */
        public function offsetGet ($index) {}

        /**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Set value for an offset
	 * @link http://php.net/manual/en/arrayiterator.offsetset.php
	 * @param string $index <p>
	 * The index to set for.
	 * </p>
	 * @param string $newval <p>
	 * The new value to store at the index.
	 * </p>
	 * @return void 
         */
        public function offsetSet ($index, $newval) {}

        /**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Unset value for an offset
	 * @link http://php.net/manual/en/arrayiterator.offsetunset.php
	 * @param string $index <p>
	 * The offset to unset.
	 * </p>
	 * @return void 
         */
        public function offsetUnset ($index) {}

        /**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Append an element
	 * @link http://php.net/manual/en/arrayiterator.append.php
	 * @param string $value <p>
	 * The value to append.
	 * </p>
	 * @return void 
         */
        public function append ($value) {}

	/**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Get array copy
	 * @link http://php.net/manual/en/arrayiterator.getarraycopy.php
	 * @return void A copy of the array, or array of public properties
	 * if ArrayIterator refers to an object.
	 */
        public function getArrayCopy () {}

	/**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Count elements
	 * @link http://php.net/manual/en/arrayiterator.count.php
	 * @return void The number.
	 */
        public function count () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get flags
	 * @link http://php.net/manual/en/arrayiterator.getflags.php
	 * @return void The current flags.
	 */
        public function getFlags () {}

        /**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Set behaviour flags
	 * @link http://php.net/manual/en/arrayiterator.setflags.php
	 * @param string $flags <p>
	 * A bitmask as follows:
	 * 0 = Properties of the object have their normal functionality
	 * when accessed as list (var_dump, foreach, etc.).
	 * 1 = Array indices can be accessed as properties in read/write.
	 * </p>
	 * @return void 
         */
        public function setFlags ($flags) {}

	/**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Sort array by values
	 * @link http://php.net/manual/en/arrayiterator.asort.php
	 * @return void 
	 */
        public function asort () {}

	/**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Sort array by keys
	 * @link http://php.net/manual/en/arrayiterator.ksort.php
	 * @return void 
	 */
        public function ksort () {}

        /**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * User defined sort
	 * @link http://php.net/manual/en/arrayiterator.uasort.php
	 * @param string $cmp_function <p>
	 * The compare function used for the sort.
	 * </p>
	 * @return void 
         */
        public function uasort ($cmp_function) {}

        /**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * User defined sort
	 * @link http://php.net/manual/en/arrayiterator.uksort.php
	 * @param string $cmp_function <p>
	 * The compare function used for the sort.
	 * </p>
	 * @return void 
         */
        public function uksort ($cmp_function) {}

	/**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Sort an array naturally
	 * @link http://php.net/manual/en/arrayiterator.natsort.php
	 * @return void 
	 */
        public function natsort () {}

	/**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Sort an array naturally, case insensitive
	 * @link http://php.net/manual/en/arrayiterator.natcasesort.php
	 * @return void 
	 */
        public function natcasesort () {}

        /**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Unserialize
	 * @link http://php.net/manual/en/arrayiterator.unserialize.php
	 * @param string $serialized <p>
	 * The serialized ArrayIterator object to be unserialized.
	 * </p>
	 * @return string The ArrayIterator.
         */
        public function unserialize ($serialized) {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Serialize
	 * @link http://php.net/manual/en/arrayiterator.serialize.php
	 * @return string The serialized ArrayIterator.
	 */
        public function serialize () {}

	/**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Rewind array back to the start
	 * @link http://php.net/manual/en/arrayiterator.rewind.php
	 * @return void 
	 */
        public function rewind () {}

	/**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Return current array entry
	 * @link http://php.net/manual/en/arrayiterator.current.php
	 * @return mixed The current array entry.
	 */
        public function current () {}

	/**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Return current array key
	 * @link http://php.net/manual/en/arrayiterator.key.php
	 * @return mixed The current array key.
	 */
        public function key () {}

	/**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Move to next entry
	 * @link http://php.net/manual/en/arrayiterator.next.php
	 * @return void 
	 */
        public function next () {}

	/**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Check whether array contains more entries
	 * @link http://php.net/manual/en/arrayiterator.valid.php
	 * @return bool 
	 */
        public function valid () {}

        /**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Seek to position
	 * @link http://php.net/manual/en/arrayiterator.seek.php
	 * @param int $position <p>
	 * The position to seek to.
	 * </p>
	 * @return void 
         */
        public function seek ($position) {}

}

/**
 * This iterator allows to unset and modify values and keys while iterating over Arrays and Objects
 * in the same way as the ArrayIterator. Additionally it is possible to iterate
 * over the current iterator entry.
 * @link http://php.net/manual/en/class.recursivearrayiterator.php
 */
class RecursiveArrayIterator extends ArrayIterator implements Serializable, SeekableIterator, ArrayAccess, Traversable, Iterator, RecursiveIterator {
        const CHILD_ARRAYS_ONLY = 4;


	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Returns whether current entry is an array or an object.
	 * @link http://php.net/manual/en/recursivearrayiterator.haschildren.php
	 * @return bool true if the current entry is an array or an object,
	 * otherwise false is returned.
	 */
        public function hasChildren () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Returns an iterator for the current entry if it is an <type>array</type> or an <type>object</type>.
	 * @link http://php.net/manual/en/recursivearrayiterator.getchildren.php
	 * @return RecursiveArrayIterator An iterator for the current entry, if it is an array or object.
	 */
        public function getChildren () {}

        /**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Construct an ArrayIterator
	 * @link http://php.net/manual/en/arrayiterator.construct.php
	 * @param $array
         */
        public function __construct ($array) {}

        /**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Check if offset exists
	 * @link http://php.net/manual/en/arrayiterator.offsetexists.php
	 * @param string $index <p>
	 * The offset being checked.
	 * </p>
	 * @return void true if the offset exists, otherwise false
         */
        public function offsetExists ($index) {}

        /**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Get value for an offset
	 * @link http://php.net/manual/en/arrayiterator.offsetget.php
	 * @param string $index <p>
	 * The offset to get the value from.
	 * </p>
	 * @return mixed The value at offset index.
         */
        public function offsetGet ($index) {}

        /**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Set value for an offset
	 * @link http://php.net/manual/en/arrayiterator.offsetset.php
	 * @param string $index <p>
	 * The index to set for.
	 * </p>
	 * @param string $newval <p>
	 * The new value to store at the index.
	 * </p>
	 * @return void 
         */
        public function offsetSet ($index, $newval) {}

        /**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Unset value for an offset
	 * @link http://php.net/manual/en/arrayiterator.offsetunset.php
	 * @param string $index <p>
	 * The offset to unset.
	 * </p>
	 * @return void 
         */
        public function offsetUnset ($index) {}

        /**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Append an element
	 * @link http://php.net/manual/en/arrayiterator.append.php
	 * @param string $value <p>
	 * The value to append.
	 * </p>
	 * @return void 
         */
        public function append ($value) {}

	/**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Get array copy
	 * @link http://php.net/manual/en/arrayiterator.getarraycopy.php
	 * @return void A copy of the array, or array of public properties
	 * if ArrayIterator refers to an object.
	 */
        public function getArrayCopy () {}

	/**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Count elements
	 * @link http://php.net/manual/en/arrayiterator.count.php
	 * @return void The number.
	 */
        public function count () {}

	/**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Get flags
	 * @link http://php.net/manual/en/arrayiterator.getflags.php
	 * @return void The current flags.
	 */
        public function getFlags () {}

        /**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Set behaviour flags
	 * @link http://php.net/manual/en/arrayiterator.setflags.php
	 * @param string $flags <p>
	 * A bitmask as follows:
	 * 0 = Properties of the object have their normal functionality
	 * when accessed as list (var_dump, foreach, etc.).
	 * 1 = Array indices can be accessed as properties in read/write.
	 * </p>
	 * @return void 
         */
        public function setFlags ($flags) {}

	/**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Sort array by values
	 * @link http://php.net/manual/en/arrayiterator.asort.php
	 * @return void 
	 */
        public function asort () {}

	/**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Sort array by keys
	 * @link http://php.net/manual/en/arrayiterator.ksort.php
	 * @return void 
	 */
        public function ksort () {}

        /**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * User defined sort
	 * @link http://php.net/manual/en/arrayiterator.uasort.php
	 * @param string $cmp_function <p>
	 * The compare function used for the sort.
	 * </p>
	 * @return void 
         */
        public function uasort ($cmp_function) {}

        /**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * User defined sort
	 * @link http://php.net/manual/en/arrayiterator.uksort.php
	 * @param string $cmp_function <p>
	 * The compare function used for the sort.
	 * </p>
	 * @return void 
         */
        public function uksort ($cmp_function) {}

	/**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Sort an array naturally
	 * @link http://php.net/manual/en/arrayiterator.natsort.php
	 * @return void 
	 */
        public function natsort () {}

	/**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Sort an array naturally, case insensitive
	 * @link http://php.net/manual/en/arrayiterator.natcasesort.php
	 * @return void 
	 */
        public function natcasesort () {}

        /**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Unserialize
	 * @link http://php.net/manual/en/arrayiterator.unserialize.php
	 * @param string $serialized <p>
	 * The serialized ArrayIterator object to be unserialized.
	 * </p>
	 * @return string The ArrayIterator.
         */
        public function unserialize ($serialized) {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Serialize
	 * @link http://php.net/manual/en/arrayiterator.serialize.php
	 * @return string The serialized ArrayIterator.
	 */
        public function serialize () {}

	/**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Rewind array back to the start
	 * @link http://php.net/manual/en/arrayiterator.rewind.php
	 * @return void 
	 */
        public function rewind () {}

	/**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Return current array entry
	 * @link http://php.net/manual/en/arrayiterator.current.php
	 * @return mixed The current array entry.
	 */
        public function current () {}

	/**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Return current array key
	 * @link http://php.net/manual/en/arrayiterator.key.php
	 * @return mixed The current array key.
	 */
        public function key () {}

	/**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Move to next entry
	 * @link http://php.net/manual/en/arrayiterator.next.php
	 * @return void 
	 */
        public function next () {}

	/**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Check whether array contains more entries
	 * @link http://php.net/manual/en/arrayiterator.valid.php
	 * @return bool 
	 */
        public function valid () {}

        /**
	 * (PHP 5 &gt;= 5.0.0)<br/>
	 * Seek to position
	 * @link http://php.net/manual/en/arrayiterator.seek.php
	 * @param int $position <p>
	 * The position to seek to.
	 * </p>
	 * @return void 
         */
        public function seek ($position) {}

}

// End of SPL v.0.2
?>
