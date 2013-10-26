<?php
/**
 * Representation of date and time.
 * @link http://php.net/manual/en/class.datetime.php
 */
class DateTime {
    const ATOM = 'Y-m-d\TH:i:sP';
    const COOKIE = 'l, d-M-y H:i:s T';
    const ISO8601 = 'Y-m-d\TH:i:sO';
    const RFC822 = 'D, d M y H:i:s O';
    const RFC850 = 'l, d-M-y H:i:s T';
    const RFC1036 = 'D, d M y H:i:s O';
    const RFC1123 = 'D, d M Y H:i:s O';
    const RFC2822 = 'D, d M Y H:i:s O';
    const RFC3339 = 'Y-m-d\TH:i:sP';
    const RSS = 'D, d M Y H:i:s O';
    const W3C = 'Y-m-d\TH:i:sP';


    /**
     * @param string $time
     * @param DateTimeZone $timezone
     * @return DateTime
     * @link http://php.net/manual/en/datetime.construct.php
     */
    public function __construct ($time='now', DateTimeZone $timezone=null) {}

    /**
     * @return DateTime
     * @link http://php.net/manual/en/datetime.wakeup.php
     */
    public function __wakeup () {}


    /**
     * Returns date formatted according to given format.
     * @param string $format
     * @return string
     * @link http://php.net/manual/en/datetime.format.php
     */
    public function format ($format) {}

    /**
     * Alter the timestamp of a DateTime object by incrementing or decrementing
     * in a format accepted by strtotime().
     * @param string $modify
     * @return DateTime
     * @link http://php.net/manual/en/datetime.modify.php
     */
    public function modify ($modify) {}

    /**
     * Adds an amount of days, months, years, hours, minutes and seconds to a DateTime object
     * @param DateInterval $interval
     * @return DateTime
     * @link http://php.net/manual/en/datetime.add.php
     */
    public function add (DateInterval $interval) {}

    /**
     * Subtracts an amount of days, months, years, hours, minutes and seconds from a DateTime object
     * @param DateInterval $interval
     * @return DateTime
     * @link http://php.net/manual/en/datetime.sub.php
     */
    public function sub (DateInterval $interval) {}

    /**
     * Get the TimeZone associated with the DateTime
     * @return DateTimeZone
     * @link http://php.net/manual/en/datetime.gettimezone.php
     */
    public function getTimezone () {}

    /**
     * Set the TimeZone associated with the DateTime
     * @param DateTimeZone $timezone
     * @return DateTime
     * @link http://php.net/manual/en/datetime.settimezone.php
     */
    public function setTimezone ($timezone) {}

    /**
     * Returns the timezone offset
     * @return int
     * @link http://php.net/manual/en/datetime.getoffset.php
     */
    public function getOffset () {}

    /**
     * Sets the current time of the DateTime object to a different time.
     * @param int $hour
     * @param int $minute
     * @param int $second
     * @return DateTime
     * @link http://php.net/manual/en/datetime.settime.php
     */
    public function setTime ($hour, $minute, $second=0) {}

    /**
     * Sets the current date of the DateTime object to a different date.
     * @param int $year
     * @param int $month
     * @param int $day
     * @return DateTime
     * @link http://php.net/manual/en/datetime.setdate.php
     */
    public function setDate ($year, $month, $day) {}

    /**
     * Set a date according to the ISO 8601 standard - using weeks and day offsets rather than specific dates.
     * @param int $year
     * @param int $week
     * @param int $day
     * @return DateTime
     * @link http://php.net/manual/en/datetime.setisodate.php
     */
    public function setISODate ($year, $week, $day=1) {}

    /**
     * Sets the date and time based on a Unix timestamp.
     * @param int $unixtimestamp
     * @return DateTime
     * @link http://php.net/manual/en/datetime.settimestamp.php
     */
    public function setTimestamp ($unixtimestamp) {}

    /**
     * Gets the Unix timestamp.
     * @return int
     * @link http://php.net/manual/en/datetime.gettimestamp.php
     */
    public function getTimestamp () {}

    /**
     * Returns the difference between two DateTime objects represented as a DateInterval.
     * @param DateTime $datetime2 The date to compare to.
     * @param boolean $absolute [optional] Whether to return absolute difference.
     * @return DateInterval The DateInterval object representing the difference between the two dates or FALSE on failure.
     * @link http://php.net/manual/en/datetime.diff.php
     */
    public function diff ($datetime2, $absolute = false) {}


    /**
     * Parse a string into a new DateTime object according to the specified format
     * @param string $format Format accepted by date().
     * @param string $time String representing the time.
     * @param DateTimeZone $timezone A DateTimeZone object representing the desired time zone.
     * @return DateTime
     * @link http://php.net/manual/en/datetime.createfromformat.php
     */
    public static function createFromFormat ($format, $time, DateTimeZone $timezone=null) {}

    /**
     * Returns an array of warnings and errors found while parsing a date/time string
     * @return array
     * @link http://php.net/manual/en/datetime.getlasterrors.php
     */
    public static function getLastErrors () {}

    /**
     * @param array $array
     * @link http://php.net/manual/en/datetime.set-state.php
     */
    public static function __set_state ($array) {}
}

/**
 * Representation of time zone
 * @link http://php.net/manual/en/class.datetimezone.php
 */
class DateTimeZone {
    const AFRICA = 1;
    const AMERICA = 2;
    const ANTARCTICA = 4;
    const ARCTIC = 8;
    const ASIA = 16;
    const ATLANTIC = 32;
    const AUSTRALIA = 64;
    const EUROPE = 128;
    const INDIAN = 256;
    const PACIFIC = 512;
    const UTC = 1024;
    const ALL = 2047;
    const ALL_WITH_BC = 4095;
    const PER_COUNTRY = 4096;


    /**
     * @param string $timezone
     * @link http://php.net/manual/en/datetimezone.construct.php
     */
    public function __construct ($timezone) {}

    /**
     * Returns the name of the timezone
     * @return string
     * @link http://php.net/manual/en/datetimezone.getname.php
     */
    public function getName () {}

    /**
     * Returns location information for a timezone
     * @return array
     * @link http://php.net/manual/en/datetimezone.getlocation.php
     */
    public function getLocation () {}

    /**
     * Returns the timezone offset from GMT
     * @param DateTime $datetime
     * @return int
     * @link http://php.net/manual/en/datetimezone.getoffset.php
     */
    public function getOffset (DateTime $datetime) {}

    /**
     * Returns all transitions for the timezone
     * @param int $timestamp_begin [optional]
     * @param int $timestamp_end [optional]
     * @return array
     * @link http://php.net/manual/en/datetimezone.gettransitions.php
     */
    public function getTransitions ($timestamp_begin=null, $timestamp_end=null) {}


    /**
     * Returns associative array containing dst, offset and the timezone name
     * @return array
     * @link http://php.net/manual/en/datetimezone.listabbreviations.php
     */
    public static function listAbbreviations () {}

    /**
     * Returns a numerically indexed array with all timezone identifiers
     * @param int $what
     * @param string $country
     * @return array
     * @link http://php.net/manual/en/datetimezone.listidentifiers.php
     */
    public static function listIdentifiers ($what=DateTimeZone::ALL, $country=null) {}
}

/**
 * Representation of date interval. A date interval stores either a fixed amount of
 * time (in years, months, days, hours etc) or a relative time string in the format
 * that DateTime's constructor supports.
 * @link http://php.net/manual/en/class.dateinterval.php
 */
class DateInterval {
    /**
     * Number of years
     * @var int
     */
    public $y;

    /**
     * Number of months
     * @var int
     */
    public $m;

    /**
     * Number of days
     * @var int
     */
    public $d;

    /**
     * Number of hours
     * @var int
     */
    public $h;

    /**
     * Number of minutes
     * @var int
     */
    public $i;

    /**
     * Number of seconds
     * @var int
     */
    public $s;

    /**
     * Is 1 if the interval is inverted and 0 otherwise
     * @var int
     */
    public $invert;

    /**
     * Total number of days the interval spans. If this is unknown, days will be FALSE.
     * @var mixed
     */
    public $days;


    /**
     * @param string $interval_spec
     * @link http://php.net/manual/en/dateinterval.construct.php
     */
    public function __construct ($interval_spec) {}

    /**
     * Formats the interval
     * @param $format
     * @return string
     * @link http://php.net/manual/en/dateinterval.format.php
     */
    public function format ($format) {}

    /**
     * Sets up a DateInterval from the relative parts of the string
     * @param string $time
     * @return DateInterval
     * @link http://php.net/manual/en/dateinterval.createfromdatestring.php
     */
    public static function createFromDateString ($time) {}
}

/**
 * Representation of date period.
 * @link http://php.net/manual/en/class.dateperiod.php
 */
class DatePeriod implements Traversable {
    const EXCLUDE_START_DATE = 1;

    /**
     * @param DateTime $start
     * @param DateInterval $interval
     * @param DateTime $end
     * @param int $options
     * @link http://php.net/manual/en/dateperiod.construct.php
     */
    public function __construct (DateTime $start, DateInterval $interval, DateTime $end, $options=0) {}
}

?>