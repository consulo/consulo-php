<?php

// Start of Core v.5.3.2-0.dotdeb.1

/**
 * Created by typecasting to object.
 * @link http://php.net/manual/en/reserved.classes.php
 */
class stdClass  {
}

/**
 * Interface to detect if a class is traversable using &foreach;.
 * @link http://php.net/manual/en/class.traversable.php
 */
interface Traversable  {
}

/**
 * Interface to create an external Iterator.
 * @link http://php.net/manual/en/class.iteratoraggregate.php
 */
interface IteratorAggregate extends Traversable {

        /**
         * (PHP 5 &gt;= 5.1.0)<br/>
         * Retrieve an external iterator
         * @link http://php.net/manual/en/iteratoraggregate.getiterator.php
         * @return Traversable An instance of an object implementing Iterator or
         * Traversable
         */
        public function getIterator ();

}

/**
 * Interface for external iterators or objects that can be iterated
 * themselves internally.
 * @link http://php.net/manual/en/class.iterator.php
 */
interface Iterator extends Traversable {

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
 * Interface to provide accessing objects as arrays.
 * @link http://php.net/manual/en/class.arrayaccess.php
 */
interface ArrayAccess  {

        /**
         * (PHP 5 &gt;= 5.1.0)<br/>
         * Whether a offset exists
         * @link http://php.net/manual/en/arrayaccess.offsetexists.php
         * @param mixed $offset <p>
         * An offset to check for.
         * </p>
         * @return boolean Returns true on success or false on failure.
         * </p>
         * <p>
         * The return value will be casted to boolean if non-boolean was returned.
         */
        public function offsetExists ($offset);

        /**
         * (PHP 5 &gt;= 5.1.0)<br/>
         * Offset to retrieve
         * @link http://php.net/manual/en/arrayaccess.offsetget.php
         * @param mixed $offset <p>
         * The offset to retrieve.
         * </p>
         * @return mixed Can return all value types.
         */
        public function offsetGet ($offset);

        /**
         * (PHP 5 &gt;= 5.1.0)<br/>
         * Offset to set
         * @link http://php.net/manual/en/arrayaccess.offsetset.php
         * @param mixed $offset <p>
         * The offset to assign the value to.
         * </p>
         * @param mixed $value <p>
         * The value to set.
         * </p>
         * @return void 
         */
        public function offsetSet ($offset, $value);

        /**
         * (PHP 5 &gt;= 5.1.0)<br/>
         * Offset to unset
         * @link http://php.net/manual/en/arrayaccess.offsetunset.php
         * @param mixed $offset <p>
         * The offset to unset.
         * </p>
         * @return void 
         */
        public function offsetUnset ($offset);

}

/**
 * Interface for customized serializing.
 * @link http://php.net/manual/en/class.serializable.php
 */
interface Serializable  {

        /**
         * (PHP 5 &gt;= 5.1.0)<br/>
         * String representation of object
         * @link http://php.net/manual/en/serializable.serialize.php
         * @return string the string representation of the object or &null;
         */
        public function serialize ();

        /**
         * (PHP 5 &gt;= 5.1.0)<br/>
         * Constructs the object
         * @link http://php.net/manual/en/serializable.unserialize.php
         * @param string $serialized <p>
         * The string representation of the object.
         * </p>
         * @return mixed the original value unserialized.
         */
        public function unserialize ($serialized);

}

/**
 * Exception is the base class for
 * all Exceptions.
 * @link http://php.net/manual/en/class.exception.php
 */
class Exception  {
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
 * An Error Exception.
 * @link http://php.net/manual/en/class.errorexception.php
 */
class ErrorException extends Exception  {
        protected $message;
        protected $code;
        protected $file;
        protected $line;
        protected $severity;


        /**
         * (PHP 5 &gt;= 5.1.0)<br/>
         * Construct the exception
         * @link http://php.net/manual/en/errorexception.construct.php
         * @param $message [optional]
         * @param $code [optional]
         * @param $severity [optional]
         * @param $filename [optional]
         * @param $lineno [optional]
         * @param $previous [optional]
         */
        public function __construct ($message, $code, $severity, $filename, $lineno, $previous) {}

        /**
         * (PHP 5 &gt;= 5.1.0)<br/>
         * Gets the exception severity
         * @link http://php.net/manual/en/errorexception.getseverity.php
         * @return int the severity level of the exception.
         */
        final public function getSeverity () {}

        /**
         * (PHP 5 &gt;= 5.1.0)<br/>
         * Clone the exception
         * @link http://php.net/manual/en/exception.clone.php
         * @return void 
         */
        final private function __clone () {}

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
 * The predefined final class Closure was introduced in PHP 5.3.0. It is used for internal implementation of anonymous functions.
 * The class has a constructor forbidding the manual creation of the object (issues E_RECOVERABLE_ERROR) and the __invoke method with the calling magic.
 * @link http://php.net/manual/en/reserved.classes.php
 */
final class Closure  {

        private function __construct () {}
        public function __invoke () {}

}

// End of Core v.5.3.2-0.dotdeb.1
?>
