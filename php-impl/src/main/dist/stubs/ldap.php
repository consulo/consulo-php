<?php

// Start of ldap v.

/**
 * (PHP 4, PHP 5)<br/>
 * Connect to an LDAP server
 * @link http://php.net/manual/en/function.ldap-connect.php
 * @param string $hostname [optional] <p>
 * If you are using OpenLDAP 2.x.x you can specify a URL instead of the
 * hostname. To use LDAP with SSL, compile OpenLDAP 2.x.x with SSL
 * support, configure PHP with SSL, and set this parameter as 
 * ldaps://hostname/.
 * </p>
 * @param int $port [optional] <p>
 * The port to connect to. Not used when using URLs.
 * </p>
 * @return resource a positive LDAP link identifier on success, or false on error.
 * When OpenLDAP 2.x.x is used, ldap_connect will always
 * return a resource as it does not actually connect but just
 * initializes the connecting parameters. The actual connect happens with
 * the next calls to ldap_* funcs, usually with
 * ldap_bind.
 * </p>
 * <p>
 * If no arguments are specified then the link identifier of the already
 * opened link will be returned.
 */
function ldap_connect ($hostname = null, $port = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * &Alias; <function>ldap_unbind</function>
 * @link http://php.net/manual/en/function.ldap-close.php
 * @param $link_identifier
 */
function ldap_close ($link_identifier) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Bind to LDAP directory
 * @link http://php.net/manual/en/function.ldap-bind.php
 * @param resource $link_identifier <p>
 * An LDAP link identifier, returned by ldap_connect.
 * </p>
 * @param string $bind_rdn [optional] <p>
 * </p>
 * @param string $bind_password [optional] <p>
 * </p>
 * @return bool Returns true on success or false on failure.
 */
function ldap_bind ($link_identifier, $bind_rdn = null, $bind_password = null) {}

/**
 * (PHP 5)<br/>
 * Bind to LDAP directory using SASL
 * @link http://php.net/manual/en/function.ldap-sasl-bind.php
 * @param resource $link 
 * @param string $binddn [optional] 
 * @param string $password [optional] 
 * @param string $sasl_mech [optional] 
 * @param string $sasl_realm [optional] 
 * @param string $sasl_authc_id [optional] 
 * @param string $sasl_authz_id [optional] 
 * @param string $props [optional] 
 * @return bool Returns true on success or false on failure.
 */
function ldap_sasl_bind ($link, $binddn = null, $password = null, $sasl_mech = null, $sasl_realm = null, $sasl_authc_id = null, $sasl_authz_id = null, $props = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Unbind from LDAP directory
 * @link http://php.net/manual/en/function.ldap-unbind.php
 * @param resource $link_identifier <p>
 * An LDAP link identifier, returned by ldap_connect.
 * </p>
 * @return bool Returns true on success or false on failure.
 */
function ldap_unbind ($link_identifier) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Read an entry
 * @link http://php.net/manual/en/function.ldap-read.php
 * @param resource $link_identifier <p>
 * An LDAP link identifier, returned by ldap_connect.
 * </p>
 * @param string $base_dn <p>
 * The base DN for the directory.
 * </p>
 * @param string $filter <p>
 * An empty filter is not allowed. If you want to retrieve absolutely all
 * information for this entry, use a filter of 
 * objectClass=*. If you know which entry types are
 * used on the directory server, you might use an appropriate filter such
 * as objectClass=inetOrgPerson.
 * </p>
 * @param array $attributes [optional] <p>
 * An array of the required attributes, e.g. array("mail", "sn", "cn").
 * Note that the "dn" is always returned irrespective of which attributes
 * types are requested.
 * </p>
 * <p>
 * Using this parameter is much more efficient than the default action
 * (which is to return all attributes and their associated values). 
 * The use of this parameter should therefore be considered good
 * practice.
 * </p>
 * @param int $attrsonly [optional] <p>
 * Should be set to 1 if only attribute types are wanted. If set to 0
 * both attributes types and attribute values are fetched which is the
 * default behaviour.
 * </p>
 * @param int $sizelimit [optional] <p>
 * Enables you to limit the count of entries fetched. Setting this to 0
 * means no limit.
 * </p>
 * <p>
 * This parameter can NOT override server-side preset sizelimit. You can
 * set it lower though.
 * </p>
 * <p>
 * Some directory server hosts will be configured to return no more than
 * a preset number of entries. If this occurs, the server will indicate
 * that it has only returned a partial results set. This also occurs if
 * you use this parameter to limit the count of fetched entries.
 * </p>
 * @param int $timelimit [optional] <p>
 * Sets the number of seconds how long is spend on the search. Setting
 * this to 0 means no limit.
 * </p>
 * <p>
 * This parameter can NOT override server-side preset timelimit. You can
 * set it lower though.
 * </p>
 * @param int $deref [optional] <p>
 * Specifies how aliases should be handled during the search. It can be
 * one of the following:
 * LDAP_DEREF_NEVER - (default) aliases are never
 * dereferenced.
 * @return resource a search result identifier or false on error.
 */
function ldap_read ($link_identifier, $base_dn, $filter, array $attributes = null, $attrsonly = null, $sizelimit = null, $timelimit = null, $deref = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Single-level search
 * @link http://php.net/manual/en/function.ldap-list.php
 * @param resource $link_identifier <p>
 * An LDAP link identifier, returned by ldap_connect.
 * </p>
 * @param string $base_dn <p>
 * The base DN for the directory.
 * </p>
 * @param string $filter <p>
 * </p>
 * @param array $attributes [optional] <p>
 * An array of the required attributes, e.g. array("mail", "sn", "cn").
 * Note that the "dn" is always returned irrespective of which attributes
 * types are requested.
 * </p>
 * <p>
 * Using this parameter is much more efficient than the default action
 * (which is to return all attributes and their associated values). 
 * The use of this parameter should therefore be considered good
 * practice.
 * </p>
 * @param int $attrsonly [optional] <p>
 * Should be set to 1 if only attribute types are wanted. If set to 0
 * both attributes types and attribute values are fetched which is the
 * default behaviour.
 * </p>
 * @param int $sizelimit [optional] <p>
 * Enables you to limit the count of entries fetched. Setting this to 0
 * means no limit.
 * </p>
 * <p>
 * This parameter can NOT override server-side preset sizelimit. You can
 * set it lower though.
 * </p>
 * <p>
 * Some directory server hosts will be configured to return no more than
 * a preset number of entries. If this occurs, the server will indicate
 * that it has only returned a partial results set. This also occurs if
 * you use this parameter to limit the count of fetched entries.
 * </p>
 * @param int $timelimit [optional] <p>
 * Sets the number of seconds how long is spend on the search. Setting
 * this to 0 means no limit.
 * </p>
 * <p>
 * This parameter can NOT override server-side preset timelimit. You can
 * set it lower though.
 * </p>
 * @param int $deref [optional] <p>
 * Specifies how aliases should be handled during the search. It can be
 * one of the following:
 * LDAP_DEREF_NEVER - (default) aliases are never
 * dereferenced.
 * @return resource a search result identifier or false on error.
 */
function ldap_list ($link_identifier, $base_dn, $filter, array $attributes = null, $attrsonly = null, $sizelimit = null, $timelimit = null, $deref = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Search LDAP tree
 * @link http://php.net/manual/en/function.ldap-search.php
 * @param resource $link_identifier <p>
 * An LDAP link identifier, returned by ldap_connect.
 * </p>
 * @param string $base_dn <p>
 * The base DN for the directory.
 * </p>
 * @param string $filter <p>
 * The search filter can be simple or advanced, using boolean operators in
 * the format described in the LDAP documentation (see the Netscape Directory SDK for full
 * information on filters).
 * </p>
 * @param array $attributes [optional] <p>
 * An array of the required attributes, e.g. array("mail", "sn", "cn").
 * Note that the "dn" is always returned irrespective of which attributes
 * types are requested.
 * </p>
 * <p>
 * Using this parameter is much more efficient than the default action
 * (which is to return all attributes and their associated values). 
 * The use of this parameter should therefore be considered good
 * practice.
 * </p>
 * @param int $attrsonly [optional] <p>
 * Should be set to 1 if only attribute types are wanted. If set to 0
 * both attributes types and attribute values are fetched which is the
 * default behaviour.
 * </p>
 * @param int $sizelimit [optional] <p>
 * Enables you to limit the count of entries fetched. Setting this to 0
 * means no limit.
 * </p>
 * <p>
 * This parameter can NOT override server-side preset sizelimit. You can
 * set it lower though.
 * </p>
 * <p>
 * Some directory server hosts will be configured to return no more than
 * a preset number of entries. If this occurs, the server will indicate
 * that it has only returned a partial results set. This also occurs if
 * you use this parameter to limit the count of fetched entries.
 * </p>
 * @param int $timelimit [optional] <p>
 * Sets the number of seconds how long is spend on the search. Setting
 * this to 0 means no limit.
 * </p>
 * <p>
 * This parameter can NOT override server-side preset timelimit. You can
 * set it lower though.
 * </p>
 * @param int $deref [optional] <p>
 * Specifies how aliases should be handled during the search. It can be
 * one of the following:
 * LDAP_DEREF_NEVER - (default) aliases are never
 * dereferenced.
 * @return resource a search result identifier or false on error.
 */
function ldap_search ($link_identifier, $base_dn, $filter, array $attributes = null, $attrsonly = null, $sizelimit = null, $timelimit = null, $deref = null) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Free result memory
 * @link http://php.net/manual/en/function.ldap-free-result.php
 * @param resource $result_identifier <p>
 * </p>
 * @return bool Returns true on success or false on failure.
 */
function ldap_free_result ($result_identifier) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Count the number of entries in a search
 * @link http://php.net/manual/en/function.ldap-count-entries.php
 * @param resource $link_identifier <p>
 * An LDAP link identifier, returned by ldap_connect.
 * </p>
 * @param resource $result_identifier <p>
 * The internal LDAP result.
 * </p>
 * @return int number of entries in the result or false on error.
 */
function ldap_count_entries ($link_identifier, $result_identifier) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Return first result id
 * @link http://php.net/manual/en/function.ldap-first-entry.php
 * @param resource $link_identifier <p>
 * An LDAP link identifier, returned by ldap_connect.
 * </p>
 * @param resource $result_identifier <p>
 * </p>
 * @return resource the result entry identifier for the first entry on success and
 * false on error.
 */
function ldap_first_entry ($link_identifier, $result_identifier) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Get next result entry
 * @link http://php.net/manual/en/function.ldap-next-entry.php
 * @param resource $link_identifier <p>
 * An LDAP link identifier, returned by ldap_connect.
 * </p>
 * @param resource $result_entry_identifier <p>
 * </p>
 * @return resource entry identifier for the next entry in the result whose entries
 * are being read starting with ldap_first_entry. If
 * there are no more entries in the result then it returns false.
 */
function ldap_next_entry ($link_identifier, $result_entry_identifier) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Get all result entries
 * @link http://php.net/manual/en/function.ldap-get-entries.php
 * @param resource $link_identifier <p>
 * An LDAP link identifier, returned by ldap_connect.
 * </p>
 * @param resource $result_identifier <p>
 * </p>
 * @return array a complete result information in a multi-dimensional array on
 * success and false on error.
 * </p>
 * <p>
 * The structure of the array is as follows.
 * The attribute index is converted to lowercase. (Attributes are
 * case-insensitive for directory servers, but not when used as
 * array indices.)
 */
function ldap_get_entries ($link_identifier, $result_identifier) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Return first attribute
 * @link http://php.net/manual/en/function.ldap-first-attribute.php
 * @param resource $link_identifier <p>
 * An LDAP link identifier, returned by ldap_connect.
 * </p>
 * @param resource $result_entry_identifier <p>
 * </p>
 * @return string the first attribute in the entry on success and false on
 * error.
 */
function ldap_first_attribute ($link_identifier, $result_entry_identifier) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Get the next attribute in result
 * @link http://php.net/manual/en/function.ldap-next-attribute.php
 * @param resource $link_identifier <p>
 * An LDAP link identifier, returned by ldap_connect.
 * </p>
 * @param resource $result_entry_identifier <p>
 * </p>
 * @return string the next attribute in an entry on success and false on
 * error.
 */
function ldap_next_attribute ($link_identifier, $result_entry_identifier) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Get attributes from a search result entry
 * @link http://php.net/manual/en/function.ldap-get-attributes.php
 * @param resource $link_identifier <p>
 * An LDAP link identifier, returned by ldap_connect.
 * </p>
 * @param resource $result_entry_identifier <p>
 * </p>
 * @return array a complete entry information in a multi-dimensional array
 * on success and false on error.
 */
function ldap_get_attributes ($link_identifier, $result_entry_identifier) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Get all values from a result entry
 * @link http://php.net/manual/en/function.ldap-get-values.php
 * @param resource $link_identifier <p>
 * An LDAP link identifier, returned by ldap_connect.
 * </p>
 * @param resource $result_entry_identifier <p>
 * </p>
 * @param string $attribute <p>
 * </p>
 * @return array an array of values for the attribute on success and false on
 * error. The number of values can be found by indexing "count" in the
 * resultant array. Individual values are accessed by integer index in the
 * array. The first index is 0.
 * </p>
 * <p>
 * LDAP allows more than one entry for an attribute, so it can, for example,
 * store a number of email addresses for one person's directory entry all
 * labeled with the attribute "mail"
 * return_value["count"] = number of values for attribute
 * return_value[0] = first value of attribute
 * return_value[i] = ith value of attribute
 */
function ldap_get_values ($link_identifier, $result_entry_identifier, $attribute) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Get all binary values from a result entry
 * @link http://php.net/manual/en/function.ldap-get-values-len.php
 * @param resource $link_identifier <p>
 * An LDAP link identifier, returned by ldap_connect.
 * </p>
 * @param resource $result_entry_identifier <p>
 * </p>
 * @param string $attribute <p>
 * </p>
 * @return array an array of values for the attribute on success and false on
 * error. Individual values are accessed by integer index in the array. The
 * first index is 0. The number of values can be found by indexing "count"
 * in the resultant array. 
 */
function ldap_get_values_len ($link_identifier, $result_entry_identifier, $attribute) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Get the DN of a result entry
 * @link http://php.net/manual/en/function.ldap-get-dn.php
 * @param resource $link_identifier <p>
 * An LDAP link identifier, returned by ldap_connect.
 * </p>
 * @param resource $result_entry_identifier <p>
 * </p>
 * @return string the DN of the result entry and false on error.
 */
function ldap_get_dn ($link_identifier, $result_entry_identifier) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Splits DN into its component parts
 * @link http://php.net/manual/en/function.ldap-explode-dn.php
 * @param string $dn <p>
 * The distinguished name of an LDAP entity.
 * </p>
 * @param int $with_attrib <p>
 * Used to request if the RDNs are returned with only values or their
 * attributes as well. To get RDNs with the attributes (i.e. in
 * attribute=value format) set with_attrib to 0
 * and to get only values set it to 1.
 * </p>
 * @return array an array of all DN components.
 * The first element in this array has count key and
 * represents the number of returned values, next elements are numerically
 * indexed DN components.
 */
function ldap_explode_dn ($dn, $with_attrib) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Convert DN to User Friendly Naming format
 * @link http://php.net/manual/en/function.ldap-dn2ufn.php
 * @param string $dn <p>
 * The distinguished name of an LDAP entity.
 * </p>
 * @return string the user friendly name.
 */
function ldap_dn2ufn ($dn) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Add entries to LDAP directory
 * @link http://php.net/manual/en/function.ldap-add.php
 * @param resource $link_identifier <p>
 * An LDAP link identifier, returned by ldap_connect.
 * </p>
 * @param string $dn <p>
 * The distinguished name of an LDAP entity.
 * </p>
 * @param array $entry <p>
 * An array that specifies the information about the entry. The values in
 * the entries are indexed by individual attributes.
 * In case of multiple values for an attribute, they are indexed using
 * integers starting with 0.
 * ]]>
 * </p>
 * @return bool Returns true on success or false on failure.
 */
function ldap_add ($link_identifier, $dn, array $entry) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Delete an entry from a directory
 * @link http://php.net/manual/en/function.ldap-delete.php
 * @param resource $link_identifier <p>
 * An LDAP link identifier, returned by ldap_connect.
 * </p>
 * @param string $dn <p>
 * The distinguished name of an LDAP entity.
 * </p>
 * @return bool Returns true on success or false on failure.
 */
function ldap_delete ($link_identifier, $dn) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Modify an LDAP entry
 * @link http://php.net/manual/en/function.ldap-modify.php
 * @param resource $link_identifier <p>
 * An LDAP link identifier, returned by ldap_connect.
 * </p>
 * @param string $dn <p>
 * The distinguished name of an LDAP entity.
 * </p>
 * @param array $entry <p>
 * </p>
 * @return bool Returns true on success or false on failure.
 */
function ldap_modify ($link_identifier, $dn, array $entry) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Add attribute values to current attributes
 * @link http://php.net/manual/en/function.ldap-mod-add.php
 * @param resource $link_identifier <p>
 * An LDAP link identifier, returned by ldap_connect.
 * </p>
 * @param string $dn <p>
 * The distinguished name of an LDAP entity.
 * </p>
 * @param array $entry <p>
 * </p>
 * @return bool Returns true on success or false on failure.
 */
function ldap_mod_add ($link_identifier, $dn, array $entry) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Replace attribute values with new ones
 * @link http://php.net/manual/en/function.ldap-mod-replace.php
 * @param resource $link_identifier <p>
 * An LDAP link identifier, returned by ldap_connect.
 * </p>
 * @param string $dn <p>
 * The distinguished name of an LDAP entity.
 * </p>
 * @param array $entry <p>
 * </p>
 * @return bool Returns true on success or false on failure.
 */
function ldap_mod_replace ($link_identifier, $dn, array $entry) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Delete attribute values from current attributes
 * @link http://php.net/manual/en/function.ldap-mod-del.php
 * @param resource $link_identifier <p>
 * An LDAP link identifier, returned by ldap_connect.
 * </p>
 * @param string $dn <p>
 * The distinguished name of an LDAP entity.
 * </p>
 * @param array $entry <p>
 * </p>
 * @return bool Returns true on success or false on failure.
 */
function ldap_mod_del ($link_identifier, $dn, array $entry) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Return the LDAP error number of the last LDAP command
 * @link http://php.net/manual/en/function.ldap-errno.php
 * @param resource $link_identifier <p>
 * An LDAP link identifier, returned by ldap_connect.
 * </p>
 * @return int Return the LDAP error number of the last LDAP command for this
 * link.
 */
function ldap_errno ($link_identifier) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Convert LDAP error number into string error message
 * @link http://php.net/manual/en/function.ldap-err2str.php
 * @param int $errno <p>
 * The error number.
 * </p>
 * @return string the error message, as a string.
 */
function ldap_err2str ($errno) {}

/**
 * (PHP 4, PHP 5)<br/>
 * Return the LDAP error message of the last LDAP command
 * @link http://php.net/manual/en/function.ldap-error.php
 * @param resource $link_identifier <p>
 * An LDAP link identifier, returned by ldap_connect.
 * </p>
 * @return string string error message.
 */
function ldap_error ($link_identifier) {}

/**
 * (PHP 4 &gt;= 4.0.2, PHP 5)<br/>
 * Compare value of attribute found in entry specified with DN
 * @link http://php.net/manual/en/function.ldap-compare.php
 * @param resource $link_identifier <p>
 * An LDAP link identifier, returned by ldap_connect.
 * </p>
 * @param string $dn <p>
 * The distinguished name of an LDAP entity.
 * </p>
 * @param string $attribute <p>
 * The attribute name.
 * </p>
 * @param string $value <p>
 * The compared value.
 * </p>
 * @return mixed true if value matches otherwise returns
 * false. Returns -1 on error.
 */
function ldap_compare ($link_identifier, $dn, $attribute, $value) {}

/**
 * (PHP 4 &gt;= 4.2.0, PHP 5)<br/>
 * Sort LDAP result entries
 * @link http://php.net/manual/en/function.ldap-sort.php
 * @param resource $link 
 * @param resource $result 
 * @param string $sortfilter 
 * @return bool 
 */
function ldap_sort ($link, $result, $sortfilter) {}

/**
 * (PHP 4 &gt;= 4.0.5, PHP 5)<br/>
 * Modify the name of an entry
 * @link http://php.net/manual/en/function.ldap-rename.php
 * @param resource $link_identifier <p>
 * An LDAP link identifier, returned by ldap_connect.
 * </p>
 * @param string $dn <p>
 * The distinguished name of an LDAP entity.
 * </p>
 * @param string $newrdn <p>
 * The new RDN.
 * </p>
 * @param string $newparent <p>
 * The new parent/superior entry.
 * </p>
 * @param bool $deleteoldrdn <p>
 * If true the old RDN value(s) is removed, else the old RDN value(s)
 * is retained as non-distinguished values of the entry.
 * </p>
 * @return bool Returns true on success or false on failure.
 */
function ldap_rename ($link_identifier, $dn, $newrdn, $newparent, $deleteoldrdn) {}

/**
 * (PHP 4 &gt;= 4.0.4, PHP 5)<br/>
 * Get the current value for given option
 * @link http://php.net/manual/en/function.ldap-get-option.php
 * @param resource $link_identifier <p>
 * An LDAP link identifier, returned by ldap_connect.
 * </p>
 * @param int $option <p>
 * The parameter option can be one of:
 * <tr valign="top">
 * <td>Option</td>
 * <td>Type</td>
 * </tr>
 * <tr valign="top">
 * <td>LDAP_OPT_DEREF</td>
 * <td>integer</td>
 * </tr>
 * <tr valign="top">
 * <td>LDAP_OPT_SIZELIMIT</td>
 * <td>integer</td>
 * </tr>
 * <tr valign="top">
 * <td>LDAP_OPT_TIMELIMIT</td>
 * <td>integer</td>
 * </tr>
 * <tr valign="top">
 * <td>LDAP_OPT_NETWORK_TIMEOUT</td>
 * <td>integer</td>
 * </tr>
 * <tr valign="top">
 * <td>LDAP_OPT_PROTOCOL_VERSION</td>
 * <td>integer</td>
 * </tr>
 * <tr valign="top">
 * <td>LDAP_OPT_ERROR_NUMBER</td>
 * <td>integer</td>
 * </tr>
 * <tr valign="top">
 * <td>LDAP_OPT_REFERRALS</td>
 * <td>bool</td>
 * </tr>
 * <tr valign="top">
 * <td>LDAP_OPT_RESTART</td>
 * <td>bool</td>
 * </tr>
 * <tr valign="top">
 * <td>LDAP_OPT_HOST_NAME</td>
 * <td>string</td>
 * </tr>
 * <tr valign="top">
 * <td>LDAP_OPT_ERROR_STRING</td>
 * <td>string</td>
 * </tr>
 * <tr valign="top">
 * <td>LDAP_OPT_MATCHED_DN</td>
 * <td>string</td>
 * </tr>
 * <tr valign="top">
 * <td>LDAP_OPT_SERVER_CONTROLS</td>
 * <td>array</td>
 * </tr>
 * <tr valign="top">
 * <td>LDAP_OPT_CLIENT_CONTROLS</td>
 * <td>array</td>
 * </tr>
 * </p>
 * @param mixed $retval <p>
 * This will be set to the option value.
 * </p>
 * @return bool Returns true on success or false on failure.
 */
function ldap_get_option ($link_identifier, $option, &$retval) {}

/**
 * (PHP 4 &gt;= 4.0.4, PHP 5)<br/>
 * Set the value of the given option
 * @link http://php.net/manual/en/function.ldap-set-option.php
 * @param resource $link_identifier <p>
 * An LDAP link identifier, returned by ldap_connect.
 * </p>
 * @param int $option <p>
 * The parameter option can be one of:
 * <tr valign="top">
 * <td>Option</td>
 * <td>Type</td>
 * <td>Available since</td>
 * </tr>
 * <tr valign="top">
 * <td>LDAP_OPT_DEREF</td>
 * <td>integer</td>
 * <td></td>
 * </tr>
 * <tr valign="top">
 * <td>LDAP_OPT_SIZELIMIT</td>
 * <td>integer</td>
 * <td></td>
 * </tr>
 * <tr valign="top">
 * <td>LDAP_OPT_TIMELIMIT</td>
 * <td>integer</td>
 * <td></td>
 * </tr>
 * <tr valign="top">
 * <td>LDAP_OPT_NETWORK_TIMEOUT</td>
 * <td>integer</td>
 * <td>PHP 5.3.0</td>
 * </tr>
 * <tr valign="top">
 * <td>LDAP_OPT_PROTOCOL_VERSION</td>
 * <td>integer</td>
 * <td></td>
 * </tr>
 * <tr valign="top">
 * <td>LDAP_OPT_ERROR_NUMBER</td>
 * <td>integer</td>
 * <td></td>
 * </tr>
 * <tr valign="top">
 * <td>LDAP_OPT_REFERRALS</td>
 * <td>bool</td>
 * <td></td>
 * </tr>
 * <tr valign="top">
 * <td>LDAP_OPT_RESTART</td>
 * <td>bool</td>
 * <td></td>
 * </tr>
 * <tr valign="top">
 * <td>LDAP_OPT_HOST_NAME</td>
 * <td>string</td>
 * <td></td>
 * </tr>
 * <tr valign="top">
 * <td>LDAP_OPT_ERROR_STRING</td>
 * <td>string</td>
 * <td></td>
 * </tr>
 * <tr valign="top">
 * <td>LDAP_OPT_MATCHED_DN</td>
 * <td>string</td>
 * <td></td>
 * </tr>
 * <tr valign="top">
 * <td>LDAP_OPT_SERVER_CONTROLS</td>
 * <td>array</td>
 * <td></td>
 * </tr>
 * <tr valign="top">
 * <td>LDAP_OPT_CLIENT_CONTROLS</td>
 * <td>array</td>
 * <td></td>
 * </tr>
 * </p>
 * <p>
 * LDAP_OPT_SERVER_CONTROLS and 
 * LDAP_OPT_CLIENT_CONTROLS require a list of
 * controls, this means that the value must be an array of controls. A
 * control consists of an oid identifying the control,
 * an optional value, and an optional flag for
 * criticality. In PHP a control is given by an
 * array containing an element with the key oid
 * and string value, and two optional elements. The optional
 * elements are key value with string value
 * and key iscritical with boolean value.
 * iscritical defaults to false
 * if not supplied. See draft-ietf-ldapext-ldap-c-api-xx.txt
 * for details. See also the second example below.
 * </p>
 * @param mixed $newval <p>
 * The new value for the specified option.
 * </p>
 * @return bool Returns true on success or false on failure.
 */
function ldap_set_option ($link_identifier, $option, $newval) {}

/**
 * (PHP 4 &gt;= 4.0.5, PHP 5)<br/>
 * Return first reference
 * @link http://php.net/manual/en/function.ldap-first-reference.php
 * @param resource $link 
 * @param resource $result 
 * @return resource 
 */
function ldap_first_reference ($link, $result) {}

/**
 * (PHP 4 &gt;= 4.0.5, PHP 5)<br/>
 * Get next reference
 * @link http://php.net/manual/en/function.ldap-next-reference.php
 * @param resource $link 
 * @param resource $entry 
 * @return resource 
 */
function ldap_next_reference ($link, $entry) {}

/**
 * (PHP 4 &gt;= 4.0.5, PHP 5)<br/>
 * Extract information from reference entry
 * @link http://php.net/manual/en/function.ldap-parse-reference.php
 * @param resource $link 
 * @param resource $entry 
 * @param array $referrals 
 * @return bool 
 */
function ldap_parse_reference ($link, $entry, array &$referrals) {}

/**
 * (PHP 4 &gt;= 4.0.5, PHP 5)<br/>
 * Extract information from result
 * @link http://php.net/manual/en/function.ldap-parse-result.php
 * @param resource $link 
 * @param resource $result 
 * @param int $errcode 
 * @param string $matcheddn [optional] 
 * @param string $errmsg [optional] 
 * @param array $referrals [optional] 
 * @return bool 
 */
function ldap_parse_result ($link, $result, &$errcode, &$matcheddn = null, &$errmsg = null, array &$referrals = null) {}

/**
 * (PHP 4 &gt;= 4.2.0, PHP 5)<br/>
 * Start TLS
 * @link http://php.net/manual/en/function.ldap-start-tls.php
 * @param resource $link 
 * @return bool 
 */
function ldap_start_tls ($link) {}

/**
 * (PHP 4 &gt;= 4.2.0, PHP 5)<br/>
 * Set a callback function to do re-binds on referral chasing
 * @link http://php.net/manual/en/function.ldap-set-rebind-proc.php
 * @param resource $link 
 * @param callback $callback 
 * @return bool 
 */
function ldap_set_rebind_proc ($link, $callback) {}

define ('LDAP_DEREF_NEVER', 0);
define ('LDAP_DEREF_SEARCHING', 1);
define ('LDAP_DEREF_FINDING', 2);
define ('LDAP_DEREF_ALWAYS', 3);
define ('LDAP_OPT_DEREF', 2);
define ('LDAP_OPT_SIZELIMIT', 3);
define ('LDAP_OPT_TIMELIMIT', 4);

/**
 * Option for ldap_set_option to allow setting network timeout.
 * (Available as of PHP 5.3.0)
 * @link http://php.net/manual/en/ldap.constants.php
 */
define ('LDAP_OPT_NETWORK_TIMEOUT', 20485);
define ('LDAP_OPT_PROTOCOL_VERSION', 17);
define ('LDAP_OPT_ERROR_NUMBER', 49);
define ('LDAP_OPT_REFERRALS', 8);
define ('LDAP_OPT_RESTART', 9);
define ('LDAP_OPT_HOST_NAME', 48);
define ('LDAP_OPT_ERROR_STRING', 50);
define ('LDAP_OPT_MATCHED_DN', 51);
define ('LDAP_OPT_SERVER_CONTROLS', 18);
define ('LDAP_OPT_CLIENT_CONTROLS', 19);
define ('LDAP_OPT_DEBUG_LEVEL', 20481);
define ('LDAP_OPT_X_SASL_MECH', 24832);
define ('LDAP_OPT_X_SASL_REALM', 24833);
define ('LDAP_OPT_X_SASL_AUTHCID', 24834);
define ('LDAP_OPT_X_SASL_AUTHZID', 24835);

// End of ldap v.
?>
