<?php

// Start of dom v.20031129

/**
 * DOM operations raise exceptions under particular circumstances, i.e.,
 * when an operation is impossible to perform for logical reasons.
 * @link http://php.net/manual/en/class.domexception.php
 */
class DOMException extends Exception  {
        protected $message;

    /**
     * @var
     * (PHP 5)<br/>
     * An integer indicating the type of error generated
     * @link http://php.net/manual/en/class.domexception.php#domexception.props.code
     */
        public $code;
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

class DOMStringList  {

        /**
	 * @param $index
         */
        public function item ($index) {}

}

/**
 * @link http://php.net/manual/en/ref.dom.php
 */
class DOMNameList  {

        /**
	 * @param $index
         */
        public function getName ($index) {}

        /**
	 * @param $index
         */
        public function getNamespaceURI ($index) {}

}

class DOMImplementationList  {

        /**
	 * @param $index
         */
        public function item ($index) {}

}

class DOMImplementationSource  {

        /**
	 * @param $features
         */
        public function getDomimplementation ($features) {}

        /**
	 * @param $features
         */
        public function getDomimplementations ($features) {}

}

/**
 * The DOMImplementation interface provides a number
 * of methods for performing operations that are independent of any 
 * particular instance of the document object model.
 * @link http://php.net/manual/en/class.domimplementation.php
 */
class DOMImplementation  {

    /**
     * (PHP 5)<br/>
     * Creates a new DOMImplementation object
     * @link http://http://php.net/manual/en/domimplementation.construct.php
     */
    public function __construct(){}

        /**
	 * @param $feature
	 * @param $version
         */
        public function getFeature ($feature, $version) {}

	/**
	 * (PHP 5)<br/>
	 * Test if the DOM implementation implements a specific feature
	 * @link http://php.net/manual/en/domimplementation.hasfeature.php
	 * @param string $feature <p>
	 * The feature to test.
	 * </p>
	 * @param string $version <p>
	 * The version number of the feature to test. In 
	 * level 2, this can be either 2.0 or
	 * 1.0.
	 * </p>
	 * @return bool Returns true on success or false on failure.
	 */
	public function hasFeature ($feature, $version) {}

        /**
	 * (PHP 5)<br/>
	 * Creates an empty DOMDocumentType object
	 * @link http://php.net/manual/en/domimplementation.createdocumenttype.php
	 * @param string $qualifiedName [optional] <p>
	 * The qualified name of the document type to create.
	 * </p>
	 * @param string $publicId [optional] <p>
	 * The external subset public identifier.
	 * </p>
	 * @param string $systemId [optional] <p>
	 * The external subset system identifier.
	 * </p>
	 * @return DOMDocumentType A new DOMDocumentType node with its 
	 * ownerDocument set to &null;.
         */
	public function createDocumentType ($qualifiedName = null, $publicId = null, $systemId = null) {}

        /**
	 * (PHP 5)<br/>
	 * Creates a DOMDocument object of the specified type with its document element
	 * @link http://php.net/manual/en/domimplementation.createdocument.php
	 * @param string $namespaceURI [optional] <p>
	 * The namespace URI of the document element to create.
	 * </p>
	 * @param string $qualifiedName [optional] <p>
	 * The qualified name of the document element to create.
	 * </p>
	 * @param DOMDocumentType $doctype [optional] <p>
	 * The type of document to create or &null;.
	 * </p>
	 * @return DOMDocument A new DOMDocument object. If 
	 * namespaceURI, qualifiedName, 
	 * and doctype are null, the returned 
	 * DOMDocument is empty with no document element
         */
	public function createDocument ($namespaceURI = null, $qualifiedName = null, DOMDocumentType $doctype = null) {}

}

/**
 * The DOMNode class
 * @link http://php.net/manual/en/class.domnode.php
 */
class DOMNode  {

    /**
     * @var string
     * (PHP 5)<br/>
     * Returns the most accurate name for the current node type
     * @link http://php.net/manual/en/class.domnode.php#domnode.props.nodename
     */
    public $nodeName;

    /**
     * @var string
     * (PHP 5)<br/>
     * The value of this node, depending on its type
     * @link http://php.net/manual/en/class.domnode.php#domnode.props.nodevalue
     */
    public $nodeValue;

    /**
     * @var int
     * (PHP 5)<br/>
     * Gets the type of the node. One of the predefined XML_xxx_NODE constants
     * @link http://php.net/manual/en/class.domnode.php#domnode.props.nodetype
     */
    public $nodeType;

    /**
     * @var DOMNode
     * (PHP 5)<br/>
     * The parent of this node
     * @link http://php.net/manual/en/class.domnode.php#domnode.props.parentnode
     */
    public $parentNode;

    /**
     * @var DOMNodeList
     * (PHP 5)<br/>
     * A <classname>DOMNodeList</classname> that contains all children of this node. If there are no children, this is an empty <classname>DOMNodeList</classname>.
     * @link http://php.net/manual/en/class.domnode.php#domnode.props.childnodes
     */
    public $childNodes;

    /**
     * @var DOMNode
     * (PHP 5)<br/>
     * The first child of this node. If there is no such node, this returns NULL.
     * @link http://php.net/manual/en/class.domnode.php#domnode.props.firstchild
     */
    public $firstChild;

    /**
     * @var DOMNode
     * (PHP 5)<br/>
     * The last child of this node. If there is no such node, this returns NULL.
     * @link http://php.net/manual/en/class.domnode.php#domnode.props.lastchild
     */
    public $lastChild;

    /**
     * @var DOMNode
     * (PHP 5)<br/>
     * The node immediately preceding this node. If there is no such node, this returns NULL.
     * @link http://php.net/manual/en/class.domnode.php#domnode.props.previoussibling
     */
    public $previousSibling;

    /**
     * @var DOMNode
     * (PHP 5)<br/>
     * The node immediately following this node. If there is no such node, this returns NULL.
     * @link http://php.net/manual/en/class.domnode.php#domnode.props.nextsibling
     */
    public $nextSibling;

    /**
     * @var DOMNamedNodeMap
     * (PHP 5)<br/>
     * A <classname>DOMNamedNodeMap</classname> containing the attributes of this node (if it is a <classname>DOMElement</classname>) or NULL otherwise.
     * @link http://php.net/manual/en/class.domnode.php#domnode.props.attributes
     */
    public $attributes;

    /**
     * @var DOMDocument
     * (PHP 5)<br/>
     * The <classname>DOMDocument</classname> object associated with this node.
     * @link http://php.net/manual/en/class.domnode.php#domnode.props.ownerdocument
     */
    public $ownerDocument;

    /**
     * @var string
     * (PHP 5)<br/>
     * The namespace URI of this node, or NULL if it is unspecified.
     * @link http://php.net/manual/en/class.domnode.php#domnode.props.namespaceuri
     */
    public $namespaceURI;

    /**
     * @var string
     * (PHP 5)<br/>
     * The namespace prefix of this node, or NULL if it is unspecified.
     * @link http://php.net/manual/en/class.domnode.php#domnode.props.prefix
     */
    public $prefix;

    /**
     * @var string
     * (PHP 5)<br/>
     * Returns the local part of the qualified name of this node.
     * @link http://php.net/manual/en/class.domnode.php#domnode.props.localname
     */
    public $localName;

    /**
     * @var string
     * (PHP 5)<br/>
     * The absolute base URI of this node or NULL if the implementation wasn't able to obtain an absolute URI.
     * @link http://php.net/manual/en/class.domnode.php#domnode.props.baseuri
     */
    public $baseURI;

    /**
     * @var string
     * (PHP 5)<br/>
     * This attribute returns the text content of this node and its descendants.
     * @link http://php.net/manual/en/class.domnode.php#domnode.props.textcontent
     */
    public $textContent;

        /**
	 * (PHP 5)<br/>
	 * Adds a new child before a reference node
	 * @link http://php.net/manual/en/domnode.insertbefore.php
	 * @param DOMNode $newnode <p>
	 * The new node.
	 * </p>
	 * @param DOMNode $refnode [optional] <p>
	 * The reference node. If not supplied, newnode is
	 * appended to the children.
	 * </p>
	 * @return DOMNode The inserted node.
         */
	public function insertBefore (DOMNode $newnode, DOMNode $refnode = null) {}

        /**
	 * (PHP 5)<br/>
	 * Replaces a child
	 * @link http://php.net/manual/en/domnode.replacechild.php
	 * @param DOMNode $newnode <p>
	 * The new node. It must be a member of the target document, i.e.
	 * created by one of the DOMDocument->createXXX() methods or imported in
	 * the document by .
	 * </p>
	 * @param DOMNode $oldnode <p>
	 * The old node.
	 * </p>
	 * @return DOMNode The old node or false if an error occur.
         */
	public function replaceChild (DOMNode $newnode , DOMNode $oldnode ) {}

        /**
	 * (PHP 5)<br/>
	 * Removes child from list of children
	 * @link http://php.net/manual/en/domnode.removechild.php
	 * @param DOMNode $oldnode <p>
	 * The removed child.
	 * </p>
	 * @return DOMNode If the child could be removed the functions returns the old child.
         */
	public function removeChild (DOMNode $oldnode ) {}

        /**
	 * (PHP 5)<br/>
	 * Adds new child at the end of the children
	 * @link http://php.net/manual/en/domnode.appendchild.php
	 * @param DOMNode $newnode <p>
	 * The appended child.
	 * </p>
	 * @return DOMNode The node added.
         */
	public function appendChild (DOMNode $newnode ) {}

	/**
	 * (PHP 5)<br/>
	 * Checks if node has children
	 * @link http://php.net/manual/en/domnode.haschildnodes.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function hasChildNodes () {}

        /**
	 * (PHP 5)<br/>
	 * Clones a node
	 * @link http://php.net/manual/en/domnode.clonenode.php
	 * @param bool $deep [optional] <p>
	 * Indicates whether to copy all descendant nodes. This parameter is 
	 * defaulted to false.
	 * </p>
	 * @return DOMNode The cloned node.
         */
	public function cloneNode ($deep = null) {}

	/**
	 * (PHP 5)<br/>
	 * Normalizes the node
	 * @link http://php.net/manual/en/domnode.normalize.php
	 * @return void 
	 */
        public function normalize () {}

        /**
	 * (PHP 5)<br/>
	 * Checks if feature is supported for specified version
	 * @link http://php.net/manual/en/domnode.issupported.php
	 * @param string $feature <p>
	 * The feature to test. See the example of 
	 * DOMImplementation::hasFeature for a
	 * list of features.
	 * </p>
	 * @param string $version <p>
	 * The version number of the feature to test.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
        public function isSupported ($feature, $version) {}

	/**
	 * (PHP 5)<br/>
	 * Checks if node has attributes
	 * @link http://php.net/manual/en/domnode.hasattributes.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function hasAttributes () {}

        /**
	 * @param DOMNode $other
         */
        public function compareDocumentPosition (DOMNode $other) {}

        /**
	 * (PHP 5)<br/>
	 * Indicates if two nodes are the same node
	 * @link http://php.net/manual/en/domnode.issamenode.php
	 * @param DOMNode $node <p>
	 * The compared node.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
	public function isSameNode (DOMNode $node ) {}

        /**
	 * (PHP 5)<br/>
	 * Gets the namespace prefix of the node based on the namespace URI
	 * @link http://php.net/manual/en/domnode.lookupprefix.php
	 * @param string $namespaceURI <p>
	 * The namespace URI.
	 * </p>
	 * @return string The prefix of the namespace.
         */
        public function lookupPrefix ($namespaceURI) {}

        /**
	 * (PHP 5)<br/>
	 * Checks if the specified namespaceURI is the default namespace or not
	 * @link http://php.net/manual/en/domnode.isdefaultnamespace.php
	 * @param string $namespaceURI <p>
	 * The namespace URI to look for.
	 * </p>
	 * @return bool Return true if namespaceURI is the default
	 * namespace, false otherwise.
         */
        public function isDefaultNamespace ($namespaceURI) {}

        /**
	 * (PHP 5)<br/>
	 * Gets the namespace URI of the node based on the prefix
	 * @link http://php.net/manual/en/domnode.lookupnamespaceuri.php
	 * @param string $prefix <p>
	 * The prefix of the namespace.
	 * </p>
	 * @return string The namespace URI of the node.
         */
        public function lookupNamespaceUri ($prefix) {}

        /**
	 * @param DOMNode $arg
         */
        public function isEqualNode (DOMNode $arg) {}

        /**
	 * @param $feature
	 * @param $version
         */
        public function getFeature ($feature, $version) {}

        /**
	 * @param $key
	 * @param $data
	 * @param $handler
         */
        public function setUserData ($key, $data, $handler) {}

        /**
	 * @param $key
         */
        public function getUserData ($key) {}

        public function getNodePath () {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Get line number for a node
	 * @link http://php.net/manual/en/domnode.getlineno.php
	 * @return int Always returns the line number where the node was defined in.
	 */
        public function getLineNo () {}

        /**
	 * @param $exclusive [optional]
	 * @param $with_comments [optional]
	 * @param $xpath [optional]
	 * @param $ns_prefixes [optional]
         */
        public function C14N ($exclusive, array $with_comments, array $xpath = null, $ns_prefixes = null) {}

        /**
	 * @param $uri
	 * @param $exclusive [optional]
	 * @param $with_comments [optional]
	 * @param $xpath [optional]
	 * @param $ns_prefixes [optional]
         */
        public function C14NFile ($uri, $exclusive, array $with_comments, array $xpath = null, $ns_prefixes = null) {}

}

class DOMNameSpaceNode  {
}

/**
 * The DOMDocumentFragment class
 * @link http://php.net/manual/en/class.domdocumentfragment.php
 */
class DOMDocumentFragment extends DOMNode  {

        public function __construct () {}

        /**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Append raw XML data
	 * @link http://php.net/manual/en/domdocumentfragment.appendxml.php
	 * @param string $data <p>
	 * XML to append.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
        public function appendXML ($data) {}

        /**
	 * (PHP 5)<br/>
	 * Adds a new child before a reference node
	 * @link http://php.net/manual/en/domnode.insertbefore.php
	 * @param DOMNode $newnode <p>
	 * The new node.
	 * </p>
	 * @param DOMNode $refnode [optional] <p>
	 * The reference node. If not supplied, newnode is
	 * appended to the children.
	 * </p>
	 * @return DOMNode The inserted node.
         */
	public function insertBefore (DOMNode $newnode, DOMNode $refnode = null) {}

        /**
	 * (PHP 5)<br/>
	 * Replaces a child
	 * @link http://php.net/manual/en/domnode.replacechild.php
	 * @param DOMNode $newnode <p>
	 * The new node. It must be a member of the target document, i.e.
	 * created by one of the DOMDocument->createXXX() methods or imported in
	 * the document by .
	 * </p>
	 * @param DOMNode $oldnode <p>
	 * The old node.
	 * </p>
	 * @return DOMNode The old node or false if an error occur.
         */
	public function replaceChild (DOMNode $newnode, DOMNode $oldnode) {}

        /**
	 * (PHP 5)<br/>
	 * Removes child from list of children
	 * @link http://php.net/manual/en/domnode.removechild.php
	 * @param DOMNode $oldnode <p>
	 * The removed child.
	 * </p>
	 * @return DOMNode If the child could be removed the functions returns the old child.
         */
	public function removeChild (DOMNode $oldnode ) {}

        /**
	 * (PHP 5)<br/>
	 * Adds new child at the end of the children
	 * @link http://php.net/manual/en/domnode.appendchild.php
	 * @param DOMNode $newnode <p>
	 * The appended child.
	 * </p>
	 * @return DOMNode The node added.
         */
	public function appendChild (DOMNode $newnode ) {}

	/**
	 * (PHP 5)<br/>
	 * Checks if node has children
	 * @link http://php.net/manual/en/domnode.haschildnodes.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function hasChildNodes () {}

        /**
	 * (PHP 5)<br/>
	 * Clones a node
	 * @link http://php.net/manual/en/domnode.clonenode.php
	 * @param bool $deep [optional] <p>
	 * Indicates whether to copy all descendant nodes. This parameter is 
	 * defaulted to false.
	 * </p>
	 * @return DOMNode The cloned node.
         */
	public function cloneNode ($deep = null) {}

	/**
	 * (PHP 5)<br/>
	 * Normalizes the node
	 * @link http://php.net/manual/en/domnode.normalize.php
	 * @return void 
	 */
        public function normalize () {}

        /**
	 * (PHP 5)<br/>
	 * Checks if feature is supported for specified version
	 * @link http://php.net/manual/en/domnode.issupported.php
	 * @param string $feature <p>
	 * The feature to test. See the example of 
	 * DOMImplementation::hasFeature for a
	 * list of features.
	 * </p>
	 * @param string $version <p>
	 * The version number of the feature to test.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
        public function isSupported ($feature, $version) {}

	/**
	 * (PHP 5)<br/>
	 * Checks if node has attributes
	 * @link http://php.net/manual/en/domnode.hasattributes.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function hasAttributes () {}

        /**
	 * @param DOMNode $other
         */
        public function compareDocumentPosition (DOMNode $other) {}

        /**
	 * (PHP 5)<br/>
	 * Indicates if two nodes are the same node
	 * @link http://php.net/manual/en/domnode.issamenode.php
	 * @param DOMNode $node <p>
	 * The compared node.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
	public function isSameNode (DOMNode $node ) {}

        /**
	 * (PHP 5)<br/>
	 * Gets the namespace prefix of the node based on the namespace URI
	 * @link http://php.net/manual/en/domnode.lookupprefix.php
	 * @param string $namespaceURI <p>
	 * The namespace URI.
	 * </p>
	 * @return string The prefix of the namespace.
         */
        public function lookupPrefix ($namespaceURI) {}

        /**
	 * (PHP 5)<br/>
	 * Checks if the specified namespaceURI is the default namespace or not
	 * @link http://php.net/manual/en/domnode.isdefaultnamespace.php
	 * @param string $namespaceURI <p>
	 * The namespace URI to look for.
	 * </p>
	 * @return bool Return true if namespaceURI is the default
	 * namespace, false otherwise.
         */
        public function isDefaultNamespace ($namespaceURI) {}

        /**
	 * (PHP 5)<br/>
	 * Gets the namespace URI of the node based on the prefix
	 * @link http://php.net/manual/en/domnode.lookupnamespaceuri.php
	 * @param string $prefix <p>
	 * The prefix of the namespace.
	 * </p>
	 * @return string The namespace URI of the node.
         */
        public function lookupNamespaceUri ($prefix) {}

        /**
	 * @param DOMNode $arg
         */
        public function isEqualNode (DOMNode $arg) {}

        /**
	 * @param $feature
	 * @param $version
         */
        public function getFeature ($feature, $version) {}

        /**
	 * @param $key
	 * @param $data
	 * @param $handler
         */
        public function setUserData ($key, $data, $handler) {}

        /**
	 * @param $key
         */
        public function getUserData ($key) {}

        public function getNodePath () {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Get line number for a node
	 * @link http://php.net/manual/en/domnode.getlineno.php
	 * @return int Always returns the line number where the node was defined in.
	 */
        public function getLineNo () {}

        /**
	 * @param $exclusive [optional]
	 * @param $with_comments [optional]
	 * @param $xpath [optional]
	 * @param $ns_prefixes [optional]
         */
        public function C14N ($exclusive, array $with_comments, array $xpath = null, $ns_prefixes = null) {}

        /**
	 * @param $uri
	 * @param $exclusive [optional]
	 * @param $with_comments [optional]
	 * @param $xpath [optional]
	 * @param $ns_prefixes [optional]
         */
        public function C14NFile ($uri, $exclusive, array $with_comments, array $xpath = null, $ns_prefixes = null) {}

}

/**
 * The DOMDocument class represents an entire HTML or XML
 * document; serves as the root of the document tree.
 * @link http://php.net/manual/en/class.domdocument.php
 */
class DOMDocument extends DOMNode  {

    /**
     * @var string
     * (PHP 5)<br/>
     * Deprecated. Actual encoding of the document, is a readonly equivalent to encoding.
     * @link http://php.net/manual/en/class.domdocument.php#domdocument.props.actualencoding
     * @deprecated
     */
    public $actualEncoding;

    /**
     * @var DOMConfiguration
     * (PHP 5)<br/>
     * Deprecated. Configuration used when {@link DOMDocument::normalizeDocument()} is invoked.
     * @link http://php.net/manual/en/class.domdocument.php#domdocument.props.config
     * @deprecated
     */
    public $config;

    /**
     * @var DOMDocumentType
     * (PHP 5)<br/>
     * The Document Type Declaration associated with this document.
     * @link http://php.net/manual/en/class.domdocument.php#domdocument.props.doctype
     */
    public $doctype;

    /**
     * @var DOMElement
     * (PHP 5)<br/>
     * This is a convenience attribute that allows direct access to the child node
     * that is the document element of the document.
     * @link http://php.net/manual/en/class.domdocument.php#domdocument.props.documentelement
     */
    public $documentElement;

    /**
     * @var string
     * (PHP 5)<br/>
     * The location of the document or NULL if undefined.
     * @link http://php.net/manual/en/class.domdocument.php#domdocument.props.documenturi
     */
    public $documentURI ;

    /**
     * @var string
     * (PHP 5)<br/>
     * Encoding of the document, as specified by the XML declaration. This attribute is not present
     * in the final DOM Level 3 specification, but is the only way of manipulating XML document
     * encoding in this implementation.
     * @link http://php.net/manual/en/class.domdocument.php#domdocument.props.encoding
     */
    public $encoding ;

    /**
     * @var bool
     * (PHP 5)<br/>
     * Nicely formats output with indentation and extra space.
     * @link http://php.net/manual/en/class.domdocument.php#domdocument.props.formatoutput
     */
    public $formatOutput ;

    /**
     * @var DOMImplementation
     * (PHP 5)<br/>
     * The <classname>DOMImplementation</classname> object that handles this document.
     * @link http://php.net/manual/en/class.domdocument.php#domdocument.props.implementation
     */
    public $implementation ;

    /**
     * @var bool
     * (PHP 5)<br/>
     * Do not remove redundant white space. Default to TRUE.
     * @link http://php.net/manual/en/class.domdocument.php#domdocument.props.preservewhitespace
     */
    public $preserveWhiteSpace = true ;

    /**
     * @var bool
     * (PHP 5)<br/>
     * Proprietary. Enables recovery mode, i.e. trying to parse non-well formed documents.
     * This attribute is not part of the DOM specification and is specific to libxml.
     * @link http://php.net/manual/en/class.domdocument.php#domdocument.props.recover
     */
    public $recover ;

    /**
     * @var bool
     * (PHP 5)<br/>
     * Set it to TRUE to load external entities from a doctype declaration. This is useful for
     * including character entities in your XML document.
     * @link http://php.net/manual/en/class.domdocument.php#domdocument.props.resolveexternals
     */
    public $resolveExternals ;

    /**
     * @var bool
     * (PHP 5)<br/>
     * Deprecated. Whether or not the document is standalone, as specified by the XML declaration,
     * corresponds to xmlStandalone.
     * @link http://php.net/manual/en/class.domdocument.php#domdocument.props.standalone
     * @deprecated
     */
    public $standalone ;

    /**
     * @var bool
     * (PHP 5)<br/>
     * Throws <classname>DOMException</classname> on errors. Default to TRUE.
     * @link http://php.net/manual/en/class.domdocument.php#domdocument.props.stricterrorchecking
     */
    public $strictErrorChecking = true ;

    /**
     * @var bool
     * (PHP 5)<br/>
     * Proprietary. Whether or not to substitute entities. This attribute is not part of the DOM
     * specification and is specific to libxml.
     * @link http://php.net/manual/en/class.domdocument.php#domdocument.props.substituteentities
     */
    public $substituteEntities ;

    /**
     * @var bool
     * (PHP 5)<br/>
     * Loads and validates against the DTD. Default to FALSE.
     * @link http://php.net/manual/en/class.domdocument.php#domdocument.props.validateonparse
     */
    public $validateOnParse = false ;

    /**
     * @var string
     * (PHP 5)<br/>
     * Deprecated. Version of XML, corresponds to xmlVersion
     * @link http://php.net/manual/en/class.domdocument.php#domdocument.props.version
     */
    public $version ;

    /**
     * @var string
     * (PHP 5)<br/>
     * An attribute specifying, as part of the XML declaration, the encoding of this document. This is NULL when
     * unspecified or when it is not known, such as when the Document was created in memory.
     * @link http://php.net/manual/en/class.domdocument.php#domdocument.props.xmlencoding
     */
    public $xmlEncoding ;

    /**
     * @var bool
     * (PHP 5)<br/>
     * An attribute specifying, as part of the XML declaration, whether this document is standalone.
     * This is FALSE when unspecified.
     * @link http://php.net/manual/en/class.domdocument.php#domdocument.props.xmlstandalone
     */
    public $xmlStandalone ;

    /**
     * @var string
     * (PHP 5)<br/>
     * An attribute specifying, as part of the XML declaration, the version number of this document. If there is no
     * declaration and if this document supports the "XML" feature, the value is "1.0".
     * @link http://php.net/manual/en/class.domdocument.php#domdocument.props.xmlversion
     */
    public $xmlVersion ;

        /**
	 * (PHP 5)<br/>
	 * Create new element node
	 * @link http://php.net/manual/en/domdocument.createelement.php
	 * @param string $name <p>
	 * The tag name of the element.
	 * </p>
	 * @param string $value [optional] <p>
	 * The value of the element. By default, an empty element will be created.
	 * You can also set the value later with DOMElement->nodeValue.
	 * </p>
	 * @return DOMElement a new instance of class DOMElement or false
	 * if an error occured.
         */
	public function createElement ($name, $value = null) {}

	/**
	 * (PHP 5)<br/>
	 * Create new document fragment
	 * @link http://php.net/manual/en/domdocument.createdocumentfragment.php
	 * @return DOMDocumentFragment The new DOMDocumentFragment or false if an error occured.
	 */
        public function createDocumentFragment () {}

        /**
	 * (PHP 5)<br/>
	 * Create new text node
	 * @link http://php.net/manual/en/domdocument.createtextnode.php
	 * @param string $content <p>
	 * The content of the text.
	 * </p>
	 * @return DOMText The new DOMText or false if an error occured.
         */
	public function createTextNode ($content) {}

        /**
	 * (PHP 5)<br/>
	 * Create new comment node
	 * @link http://php.net/manual/en/domdocument.createcomment.php
	 * @param string $data <p>
	 * The content of the comment.
	 * </p>
	 * @return DOMComment The new DOMComment or false if an error occured.
         */
        public function createComment ($data) {}

        /**
	 * (PHP 5)<br/>
	 * Create new cdata node
	 * @link http://php.net/manual/en/domdocument.createcdatasection.php
	 * @param string $data <p>
	 * The content of the cdata.
	 * </p>
	 * @return DOMCDATASection The new DOMCDATASection or false if an error occured.
         */
        public function createCDATASection ($data) {}

        /**
	 * (PHP 5)<br/>
	 * Creates new PI node
	 * @link http://php.net/manual/en/domdocument.createprocessinginstruction.php
	 * @param string $target <p>
	 * The target of the processing instruction.
	 * </p>
	 * @param string $data [optional] <p>
	 * The content of the processing instruction.
	 * </p>
	 * @return DOMProcessingInstruction The new DOMProcessingInstruction or false if an error occured.
         */
	public function createProcessingInstruction ($target, $data = null) {}

        /**
	 * (PHP 5)<br/>
	 * Create new attribute
	 * @link http://php.net/manual/en/domdocument.createattribute.php
	 * @param string $name <p>
	 * The name of the attribute.
	 * </p>
	 * @return DOMAttr The new DOMAttr or false if an error occured.
         */
        public function createAttribute ($name) {}

        /**
	 * (PHP 5)<br/>
	 * Create new entity reference node
	 * @link http://php.net/manual/en/domdocument.createentityreference.php
	 * @param string $name <p>
	 * The content of the entity reference, e.g. the entity reference minus
	 * the leading &amp; and the trailing
	 * ; characters.
	 * </p>
	 * @return DOMEntityReference The new DOMEntityReference or false if an error
	 * occured.
         */
        public function createEntityReference ($name) {}

        /**
	 * (PHP 5)<br/>
	 * Searches for all elements with given tag name
	 * @link http://php.net/manual/en/domdocument.getelementsbytagname.php
	 * @param string $name <p>
	 * The name of the tag to match on. The special value *
	 * matches all tags.
	 * </p>
	 * @return DOMNodeList A new DOMNodeList object containing all the matched 
	 * elements.
         */
	public function getElementsByTagName ($name) {}

        /**
	 * (PHP 5)<br/>
	 * Import node into current document
	 * @link http://php.net/manual/en/domdocument.importnode.php
	 * @param DOMNode $importedNode <p>
	 * The node to import.
	 * </p>
	 * @param bool $deep [optional] <p>
	 * If set to true, this method will recursively import the subtree under
	 * the importedNode.
	 * </p>
	 * <p>
	 * To copy the nodes attributes deep needs to be set to true
	 * </p>
	 * @return DOMNode The copied node or false, if it cannot be copied.
         */
	public function importNode (DOMNode $importedNode , $deep = null) {}

        /**
	 * (PHP 5)<br/>
	 * Create new element node with an associated namespace
	 * @link http://php.net/manual/en/domdocument.createelementns.php
	 * @param string $namespaceURI <p>
	 * The URI of the namespace.
	 * </p>
	 * @param string $qualifiedName <p>
	 * The qualified name of the element, as prefix:tagname.
	 * </p>
	 * @param string $value [optional] <p>
	 * The value of the element. By default, an empty element will be created.
	 * You can also set the value later with DOMElement->nodeValue.
	 * </p>
	 * @return DOMElement The new DOMElement or false if an error occured.
         */
	public function createElementNS ($namespaceURI, $qualifiedName, $value = null) {}

        /**
	 * (PHP 5)<br/>
	 * Create new attribute node with an associated namespace
	 * @link http://php.net/manual/en/domdocument.createattributens.php
	 * @param string $namespaceURI <p>
	 * The URI of the namespace.
	 * </p>
	 * @param string $qualifiedName <p>
	 * The tag name and prefix of the attribute, as prefix:tagname.
	 * </p>
	 * @return DOMAttr The new DOMAttr or false if an error occured.
         */
        public function createAttributeNS ($namespaceURI, $qualifiedName) {}

        /**
	 * (PHP 5)<br/>
	 * Searches for all elements with given tag name in specified namespace
	 * @link http://php.net/manual/en/domdocument.getelementsbytagnamens.php
	 * @param string $namespaceURI <p>
	 * The namespace URI of the elements to match on. 
	 * The special value * matches all namespaces.
	 * </p>
	 * @param string $localName <p>
	 * The local name of the elements to match on. 
	 * The special value * matches all local names.
	 * </p>
	 * @return DOMNodeList A new DOMNodeList object containing all the matched 
	 * elements.
         */
        public function getElementsByTagNameNS ($namespaceURI, $localName) {}

        /**
	 * (PHP 5)<br/>
	 * Searches for an element with a certain id
	 * @link http://php.net/manual/en/domdocument.getelementbyid.php
	 * @param string $elementId <p>
	 * The unique id value for an element.
	 * </p>
	 * @return DOMElement the DOMElement or &null; if the element is
	 * not found.
         */
        public function getElementById ($elementId) {}

        /**
	 * @param DOMNode $source
         */
        public function adoptNode (DOMNode $source) {}

	/**
	 * (PHP 5)<br/>
	 * Normalizes the document
	 * @link http://php.net/manual/en/domdocument.normalizedocument.php
	 * @return void 
	 */
        public function normalizeDocument () {}

        /**
	 * @param DOMNode $node
	 * @param $namespaceURI
	 * @param $qualifiedName
         */
        public function renameNode (DOMNode $node, $namespaceURI, $qualifiedName) {}

        /**
	 * (PHP 5)<br/>
	 * Load XML from a file
	 * @link http://php.net/manual/en/domdocument.load.php
	 * @param string $filename <p>
	 * The path to the XML document.
	 * </p>
	 * @param int $options [optional] <p>
	 * Bitwise OR
	 * of the libxml option constants.
	 * </p>
	 * @return mixed Returns true on success or false on failure. If called statically, returns a
	 * DOMDocument and issues E_STRICT
	 * warning.
         */
	public function load ($filename, $options = null) {}

        /**
	 * (PHP 5)<br/>
	 * Dumps the internal XML tree back into a file
	 * @link http://php.net/manual/en/domdocument.save.php
	 * @param string $filename <p>
	 * The path to the saved XML document.
	 * </p>
	 * @param int $options [optional] <p>
	 * Additional Options. Currently only LIBXML_NOEMPTYTAG is supported.
	 * </p>
	 * @return int the number of bytes written or false if an error occurred.
         */
	public function save ($filename, $options = null) {}

        /**
	 * (PHP 5)<br/>
	 * Load XML from a string
	 * @link http://php.net/manual/en/domdocument.loadxml.php
	 * @param string $source <p>
	 * The string containing the XML.
	 * </p>
	 * @param int $options [optional] <p>
	 * Bitwise OR
	 * of the libxml option constants.
	 * </p>
	 * @return mixed Returns true on success or false on failure. If called statically, returns a
	 * DOMDocument and issues E_STRICT
	 * warning.
         */
	public function loadXML ($source, $options = null) {}

        /**
	 * (PHP 5)<br/>
	 * Dumps the internal XML tree back into a string
	 * @link http://php.net/manual/en/domdocument.savexml.php
	 * @param DOMNode $node [optional] <p>
	 * Use this parameter to output only a specific node without XML declaration
	 * rather than the entire document.
	 * </p>
	 * @param int $options [optional] <p>
	 * Additional Options. Currently only LIBXML_NOEMPTYTAG is supported.
	 * </p>
	 * @return string the XML, or false if an error occurred.
         */
	public function saveXML ($node = null , $options = null) {}

        /**
	 * (PHP 5)<br/>
	 * Creates a new DOMDocument object
	 * @link http://php.net/manual/en/domdocument.construct.php
	 * @param $version [optional] The version number of the document as part of the XML declaration.
	 * @param $encoding [optional] The encoding of the document as part of the XML declaration.
         */
        public function __construct ($version, $encoding) {}

	/**
	 * (PHP 5)<br/>
	 * Validates the document based on its DTD
	 * @link http://php.net/manual/en/domdocument.validate.php
	 * @return bool Returns true on success or false on failure.
	 * If the document have no DTD attached, this method will return false.
	 */
        public function validate () {}

        /**
	 * (PHP 5)<br/>
	 * Substitutes XIncludes in a DOMDocument Object
	 * @link http://php.net/manual/en/domdocument.xinclude.php
	 * @param int $options [optional] <p>
	 * libxml parameters. Available
	 * since PHP 5.1.0 and Libxml 2.6.7.
	 * </p>
	 * @return int the number of XIncludes in the document.
         */
	public function xinclude ($options = null) {}

        /**
	 * (PHP 5)<br/>
	 * Load HTML from a string
	 * @link http://php.net/manual/en/domdocument.loadhtml.php
	 * @param string $source <p>
	 * The HTML string.
	 * </p>
	 * @return bool Returns true on success or false on failure. If called statically, returns a
	 * DOMDocument and issues E_STRICT
	 * warning.
         */
        public function loadHTML ($source) {}

        /**
	 * (PHP 5)<br/>
	 * Load HTML from a file
	 * @link http://php.net/manual/en/domdocument.loadhtmlfile.php
	 * @param string $filename <p>
	 * The path to the HTML file.
	 * </p>
	 * @return bool Returns true on success or false on failure. If called statically, returns a
	 * DOMDocument and issues E_STRICT
	 * warning.
         */
	public function loadHTMLFile ($filename) {}

	/**
	 * (PHP 5)<br/>
	 * Dumps the internal document into a string using HTML formatting
	 * @link http://php.net/manual/en/domdocument.savehtml.php
	 * @return string the HTML, or false if an error occurred.
	 */
        public function saveHTML () {}

        /**
	 * (PHP 5)<br/>
	 * Dumps the internal document into a file using HTML formatting
	 * @link http://php.net/manual/en/domdocument.savehtmlfile.php
	 * @param string $filename <p>
	 * The path to the saved HTML document.
	 * </p>
	 * @return int the number of bytes written or false if an error occurred.
         */
	public function saveHTMLFile ($filename) {}

        /**
	 * (PHP 5)<br/>
	 * Validates a document based on a schema
	 * @link http://php.net/manual/en/domdocument.schemavalidate.php
	 * @param string $filename <p>
	 * The path to the schema.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
        public function schemaValidate ($filename) {}

        /**
	 * (PHP 5)<br/>
	 * Validates a document based on a schema
	 * @link http://php.net/manual/en/domdocument.schemavalidatesource.php
	 * @param string $source <p>
	 * A string containing the schema.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
        public function schemaValidateSource ($source) {}

        /**
	 * (PHP 5)<br/>
	 * Performs relaxNG validation on the document
	 * @link http://php.net/manual/en/domdocument.relaxngvalidate.php
	 * @param string $filename <p>
	 * The RNG file.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
        public function relaxNGValidate ($filename) {}

        /**
	 * (PHP 5)<br/>
	 * Performs relaxNG validation on the document
	 * @link http://php.net/manual/en/domdocument.relaxngvalidatesource.php
	 * @param string $source <p>
	 * A string containing the RNG schema.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
        public function relaxNGValidateSource ($source) {}

        /**
	 * (PHP 5 &gt;= 5.2.0)<br/>
	 * Register extended class used to create base node type
	 * @link http://php.net/manual/en/domdocument.registernodeclass.php
	 * @param string $baseclass <p>
	 * The DOM class that you want to extend. You can find a list of these 
	 * classes in the chapter introduction.
	 * </p>
	 * @param string $extendedclass <p>
	 * Your extended class name. If &null; is provided, any previously 
	 * registered class extending baseclass will
	 * be removed.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
	public function registerNodeClass ($baseclass, $extendedclass) {}

        /**
	 * (PHP 5)<br/>
	 * Adds a new child before a reference node
	 * @link http://php.net/manual/en/domnode.insertbefore.php
	 * @param DOMNode $newnode <p>
	 * The new node.
	 * </p>
	 * @param DOMNode $refnode [optional] <p>
	 * The reference node. If not supplied, newnode is
	 * appended to the children.
	 * </p>
	 * @return DOMNode The inserted node.
         */
	public function insertBefore (DOMNode $newnode , $refnode = null) {}

        /**
	 * (PHP 5)<br/>
	 * Replaces a child
	 * @link http://php.net/manual/en/domnode.replacechild.php
	 * @param DOMNode $newnode <p>
	 * The new node. It must be a member of the target document, i.e.
	 * created by one of the DOMDocument->createXXX() methods or imported in
	 * the document by .
	 * </p>
	 * @param DOMNode $oldnode <p>
	 * The old node.
	 * </p>
	 * @return DOMNode The old node or false if an error occur.
         */
	public function replaceChild (DOMNode $newnode , DOMNode $oldnode ) {}

        /**
	 * (PHP 5)<br/>
	 * Removes child from list of children
	 * @link http://php.net/manual/en/domnode.removechild.php
	 * @param DOMNode $oldnode <p>
	 * The removed child.
	 * </p>
	 * @return DOMNode If the child could be removed the functions returns the old child.
         */
	public function removeChild (DOMNode $oldnode ) {}

        /**
	 * (PHP 5)<br/>
	 * Adds new child at the end of the children
	 * @link http://php.net/manual/en/domnode.appendchild.php
	 * @param DOMNode $newnode <p>
	 * The appended child.
	 * </p>
	 * @return DOMNode The node added.
         */
	public function appendChild (DOMNode $newnode ) {}

	/**
	 * (PHP 5)<br/>
	 * Checks if node has children
	 * @link http://php.net/manual/en/domnode.haschildnodes.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function hasChildNodes () {}

        /**
	 * (PHP 5)<br/>
	 * Clones a node
	 * @link http://php.net/manual/en/domnode.clonenode.php
	 * @param bool $deep [optional] <p>
	 * Indicates whether to copy all descendant nodes. This parameter is 
	 * defaulted to false.
	 * </p>
	 * @return DOMNode The cloned node.
         */
	public function cloneNode ($deep = null) {}

	/**
	 * (PHP 5)<br/>
	 * Normalizes the node
	 * @link http://php.net/manual/en/domnode.normalize.php
	 * @return void 
	 */
        public function normalize () {}

        /**
	 * (PHP 5)<br/>
	 * Checks if feature is supported for specified version
	 * @link http://php.net/manual/en/domnode.issupported.php
	 * @param string $feature <p>
	 * The feature to test. See the example of 
	 * DOMImplementation::hasFeature for a
	 * list of features.
	 * </p>
	 * @param string $version <p>
	 * The version number of the feature to test.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
        public function isSupported ($feature, $version) {}

	/**
	 * (PHP 5)<br/>
	 * Checks if node has attributes
	 * @link http://php.net/manual/en/domnode.hasattributes.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function hasAttributes () {}

        /**
	 * @param DOMNode $other
         */
        public function compareDocumentPosition (DOMNode $other) {}

        /**
	 * (PHP 5)<br/>
	 * Indicates if two nodes are the same node
	 * @link http://php.net/manual/en/domnode.issamenode.php
	 * @param DOMNode $node <p>
	 * The compared node.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
	public function isSameNode (DOMNode $node ) {}

        /**
	 * (PHP 5)<br/>
	 * Gets the namespace prefix of the node based on the namespace URI
	 * @link http://php.net/manual/en/domnode.lookupprefix.php
	 * @param string $namespaceURI <p>
	 * The namespace URI.
	 * </p>
	 * @return string The prefix of the namespace.
         */
        public function lookupPrefix ($namespaceURI) {}

        /**
	 * (PHP 5)<br/>
	 * Checks if the specified namespaceURI is the default namespace or not
	 * @link http://php.net/manual/en/domnode.isdefaultnamespace.php
	 * @param string $namespaceURI <p>
	 * The namespace URI to look for.
	 * </p>
	 * @return bool Return true if namespaceURI is the default
	 * namespace, false otherwise.
         */
        public function isDefaultNamespace ($namespaceURI) {}

        /**
	 * (PHP 5)<br/>
	 * Gets the namespace URI of the node based on the prefix
	 * @link http://php.net/manual/en/domnode.lookupnamespaceuri.php
	 * @param string $prefix <p>
	 * The prefix of the namespace.
	 * </p>
	 * @return string The namespace URI of the node.
         */
        public function lookupNamespaceUri ($prefix) {}

        /**
	 * @param DOMNode $arg
         */
        public function isEqualNode (DOMNode $arg) {}

        /**
	 * @param $feature
	 * @param $version
         */
        public function getFeature ($feature, $version) {}

        /**
	 * @param $key
	 * @param $data
	 * @param $handler
         */
        public function setUserData ($key, $data, $handler) {}

        /**
	 * @param $key
         */
        public function getUserData ($key) {}

        public function getNodePath () {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Get line number for a node
	 * @link http://php.net/manual/en/domnode.getlineno.php
	 * @return int Always returns the line number where the node was defined in.
	 */
        public function getLineNo () {}

        /**
	 * @param $exclusive [optional]
	 * @param $with_comments [optional]
	 * @param $xpath [optional]
	 * @param $ns_prefixes [optional]
         */
        public function C14N ($exclusive, array $with_comments, array $xpath = null, $ns_prefixes = null) {}

        /**
	 * @param $uri
	 * @param $exclusive [optional]
	 * @param $with_comments [optional]
	 * @param $xpath [optional]
	 * @param $ns_prefixes [optional]
         */
        public function C14NFile ($uri, $exclusive, array $with_comments, array $xpath = null, $ns_prefixes = null) {}

}

/**
 * The DOMNodeList class
 * @link http://php.net/manual/en/class.domnodelist.php
 */
class DOMNodeList  {

    /**
     * @var int
     * (PHP 5)<br/>
     * The number of nodes in the list. The range of valid child node indices is 0 to length - 1 inclusive.
     * @link http://php.net/manual/en/class.domnodelist.php#domnodelist.props.length
     */
    public $length;

  /**
	 * (PHP 5)<br/>
	 * Retrieves a node specified by index
	 * @link http://php.net/manual/en/domnodelist.item.php
	 * @param int $index <p>
	 * Index of the node into the collection.
	 * </p>
	 * @return DOMNode The node at the indexth position in the 
	 * DOMNodeList, or &null; if that is not a valid
	 * index.
   */
        public function item ($index) {}

}

/**
 * The DOMNamedNodeMap class
 * @link http://php.net/manual/en/class.domnamednodemap.php
 */
class DOMNamedNodeMap  {

        /**
	 * (PHP 5)<br/>
	 * Retrieves a node specified by name
	 * @link http://php.net/manual/en/domnamednodemap.getnameditem.php
	 * @param string $name <p>
	 * The nodeName of the node to retrieve.
	 * </p>
	 * @return DOMNode A node (of any type) with the specified nodeName, or 
	 * &null; if no node is found.
         */
        public function getNamedItem ($name) {}

        /**
	 * @param DOMNode $arg
         */
        public function setNamedItem (DOMNode $arg) {}

        /**
	 * @param $name [optional]
         */
        public function removeNamedItem ($name) {}

        /**
	 * (PHP 5)<br/>
	 * Retrieves a node specified by index
	 * @link http://php.net/manual/en/domnamednodemap.item.php
	 * @param int $index <p>
	 * Index into this map.
	 * </p>
	 * @return DOMNode The node at the indexth position in the map, or &null;
	 * if that is not a valid index (greater than or equal to the number of nodes 
	 * in this map).
         */
        public function item ($index) {}

        /**
	 * (PHP 5)<br/>
	 * Retrieves a node specified by local name and namespace URI
	 * @link http://php.net/manual/en/domnamednodemap.getnameditemns.php
	 * @param string $namespaceURI <p>
	 * The namespace URI of the node to retrieve.
	 * </p>
	 * @param string $localName <p>
	 * The local name of the node to retrieve.
	 * </p>
	 * @return DOMNode A node (of any type) with the specified local name and namespace URI, or 
	 * &null; if no node is found.
         */
        public function getNamedItemNS ($namespaceURI, $localName) {}

        /**
	 * @param DOMNode $arg [optional]
         */
        public function setNamedItemNS (DOMNode $arg) {}

        /**
	 * @param $namespaceURI [optional]
	 * @param $localName [optional]
         */
        public function removeNamedItemNS ($namespaceURI, $localName) {}

}

/**
 * The DOMCharacterData class represents nodes with character data.
 * No nodes directly correspond to this class, but other nodes do inherit from it.
 * @link http://php.net/manual/en/class.domcharacterdata.php
 */
class DOMCharacterData extends DOMNode  {


    /**
     * @var string
     * (PHP 5)<br/>
     * The contents of the node.
     * @link http://php.net/manual/en/class.domcharacterdata.php#domcharacterdata.props.data
     */
    public $data;

    /**
     * @var int
     * (PHP 5)<br/>
     * The length of the contents.
     * @link http://php.net/manual/en/class.domcharacterdata.php#domcharacterdata.props.length
     */
    public $length;

        /**
	 * (PHP 5)<br/>
	 * Extracts a range of data from the node
	 * @link http://php.net/manual/en/domcharacterdata.substringdata.php
	 * @param int $offset <p>
	 * Start offset of substring to extract.
	 * </p>
	 * @param int $count <p>
	 * The number of characters to extract.
	 * </p>
	 * @return string The specified substring. If the sum of offset 
	 * and count exceeds the length, then all 16-bit units 
	 * to the end of the data are returned.
         */
        public function substringData ($offset, $count) {}

        /**
	 * (PHP 5)<br/>
	 * Append the string to the end of the character data of the node
	 * @link http://php.net/manual/en/domcharacterdata.appenddata.php
	 * @param string $data <p>
	 * The string to append.
	 * </p>
	 * @return void 
         */
	public function appendData ($data) {}

        /**
	 * (PHP 5)<br/>
	 * Insert a string at the specified 16-bit unit offset
	 * @link http://php.net/manual/en/domcharacterdata.insertdata.php
	 * @param int $offset <p>
	 * The character offset at which to insert.
	 * </p>
	 * @param string $data <p>
	 * The string to insert.
	 * </p>
	 * @return void 
         */
	public function insertData ($offset, $data) {}

        /**
	 * (PHP 5)<br/>
	 * Remove a range of characters from the node
	 * @link http://php.net/manual/en/domcharacterdata.deletedata.php
	 * @param int $offset <p>
	 * The offset from which to start removing.
	 * </p>
	 * @param int $count <p>
	 * The number of characters to delete. If the sum of 
	 * offset and count exceeds
	 * the length, then all characters to the end of the data are deleted.
	 * </p>
	 * @return void 
         */
        public function deleteData ($offset, $count) {}

        /**
	 * (PHP 5)<br/>
	 * Replace a substring within the DOMCharacterData node
	 * @link http://php.net/manual/en/domcharacterdata.replacedata.php
	 * @param int $offset <p>
	 * The offset from which to start replacing.
	 * </p>
	 * @param int $count <p>
	 * The number of characters to replace. If the sum of 
	 * offset and count exceeds
	 * the length, then all characters to the end of the data are replaced.
	 * </p>
	 * @param string $data <p>
	 * The string with which the range must be replaced.
	 * </p>
	 * @return void 
         */
	public function replaceData ($offset, $count, $data) {}

        /**
	 * (PHP 5)<br/>
	 * Adds a new child before a reference node
	 * @link http://php.net/manual/en/domnode.insertbefore.php
	 * @param DOMNode $newnode <p>
	 * The new node.
	 * </p>
	 * @param DOMNode $refnode [optional] <p>
	 * The reference node. If not supplied, newnode is
	 * appended to the children.
	 * </p>
	 * @return DOMNode The inserted node.
         */
	public function insertBefore (DOMNode $newnode , $refnode = null) {}

        /**
	 * (PHP 5)<br/>
	 * Replaces a child
	 * @link http://php.net/manual/en/domnode.replacechild.php
	 * @param DOMNode $newnode <p>
	 * The new node. It must be a member of the target document, i.e.
	 * created by one of the DOMDocument->createXXX() methods or imported in
	 * the document by .
	 * </p>
	 * @param DOMNode $oldnode <p>
	 * The old node.
	 * </p>
	 * @return DOMNode The old node or false if an error occur.
         */
	public function replaceChild (DOMNode $newnode , DOMNode $oldnode ) {}

        /**
	 * (PHP 5)<br/>
	 * Removes child from list of children
	 * @link http://php.net/manual/en/domnode.removechild.php
	 * @param DOMNode $oldnode <p>
	 * The removed child.
	 * </p>
	 * @return DOMNode If the child could be removed the functions returns the old child.
         */
	public function removeChild (DOMNode $oldnode ) {}

        /**
	 * (PHP 5)<br/>
	 * Adds new child at the end of the children
	 * @link http://php.net/manual/en/domnode.appendchild.php
	 * @param DOMNode $newnode <p>
	 * The appended child.
	 * </p>
	 * @return DOMNode The node added.
         */
	public function appendChild (DOMNode $newnode ) {}

	/**
	 * (PHP 5)<br/>
	 * Checks if node has children
	 * @link http://php.net/manual/en/domnode.haschildnodes.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function hasChildNodes () {}

        /**
	 * (PHP 5)<br/>
	 * Clones a node
	 * @link http://php.net/manual/en/domnode.clonenode.php
	 * @param bool $deep [optional] <p>
	 * Indicates whether to copy all descendant nodes. This parameter is 
	 * defaulted to false.
	 * </p>
	 * @return DOMNode The cloned node.
         */
	public function cloneNode ($deep = null) {}

	/**
	 * (PHP 5)<br/>
	 * Normalizes the node
	 * @link http://php.net/manual/en/domnode.normalize.php
	 * @return void 
	 */
        public function normalize () {}

        /**
	 * (PHP 5)<br/>
	 * Checks if feature is supported for specified version
	 * @link http://php.net/manual/en/domnode.issupported.php
	 * @param string $feature <p>
	 * The feature to test. See the example of 
	 * DOMImplementation::hasFeature for a
	 * list of features.
	 * </p>
	 * @param string $version <p>
	 * The version number of the feature to test.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
        public function isSupported ($feature, $version) {}

	/**
	 * (PHP 5)<br/>
	 * Checks if node has attributes
	 * @link http://php.net/manual/en/domnode.hasattributes.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function hasAttributes () {}

        /**
	 * @param DOMNode $other
         */
        public function compareDocumentPosition (DOMNode $other) {}

        /**
	 * (PHP 5)<br/>
	 * Indicates if two nodes are the same node
	 * @link http://php.net/manual/en/domnode.issamenode.php
	 * @param DOMNode $node <p>
	 * The compared node.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
	public function isSameNode (DOMNode $node ) {}

        /**
	 * (PHP 5)<br/>
	 * Gets the namespace prefix of the node based on the namespace URI
	 * @link http://php.net/manual/en/domnode.lookupprefix.php
	 * @param string $namespaceURI <p>
	 * The namespace URI.
	 * </p>
	 * @return string The prefix of the namespace.
         */
        public function lookupPrefix ($namespaceURI) {}

        /**
	 * (PHP 5)<br/>
	 * Checks if the specified namespaceURI is the default namespace or not
	 * @link http://php.net/manual/en/domnode.isdefaultnamespace.php
	 * @param string $namespaceURI <p>
	 * The namespace URI to look for.
	 * </p>
	 * @return bool Return true if namespaceURI is the default
	 * namespace, false otherwise.
         */
        public function isDefaultNamespace ($namespaceURI) {}

        /**
	 * (PHP 5)<br/>
	 * Gets the namespace URI of the node based on the prefix
	 * @link http://php.net/manual/en/domnode.lookupnamespaceuri.php
	 * @param string $prefix <p>
	 * The prefix of the namespace.
	 * </p>
	 * @return string The namespace URI of the node.
         */
        public function lookupNamespaceUri ($prefix) {}

        /**
	 * @param DOMNode $arg
         */
        public function isEqualNode (DOMNode $arg) {}

        /**
	 * @param $feature
	 * @param $version
         */
        public function getFeature ($feature, $version) {}

        /**
	 * @param $key
	 * @param $data
	 * @param $handler
         */
        public function setUserData ($key, $data, $handler) {}

        /**
	 * @param $key
         */
        public function getUserData ($key) {}

        public function getNodePath () {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Get line number for a node
	 * @link http://php.net/manual/en/domnode.getlineno.php
	 * @return int Always returns the line number where the node was defined in.
	 */
        public function getLineNo () {}

        /**
	 * @param $exclusive [optional]
	 * @param $with_comments [optional]
	 * @param $xpath [optional]
	 * @param $ns_prefixes [optional]
         */
        public function C14N ($exclusive, array $with_comments, array $xpath = null, $ns_prefixes = null) {}

        /**
	 * @param $uri
	 * @param $exclusive [optional]
	 * @param $with_comments [optional]
	 * @param $xpath [optional]
	 * @param $ns_prefixes [optional]
         */
        public function C14NFile ($uri, $exclusive, array $with_comments, array $xpath = null, $ns_prefixes = null) {}

}

/**
 * The DOMAttr interface represents an attribute in an DOMElement object.
 * @link http://php.net/manual/en/class.domattr.php
 */
class DOMAttr extends DOMNode  {

    /**
     * @var string
     * (PHP5)<br/>
     * The name of the attribute
     * @link http://php.net/manual/en/class.domattr.php#domattr.props.name
     */
    public $name;

    /**
     * @var DOMElement
     * (PHP5)<br/>
     * The element which contains the attribute
     * @link http://php.net/manual/en/class.domattr.php#domattr.props.ownerelement
     */
    public $ownerElement;

    /**
     * @var bool
     * (PHP5)<br/>
     * Not implemented yet, always is NULL
     * @link http://php.net/manual/en/class.domattr.php#domattr.props.schematypeinfo
     */
    public $schemaTypeInfo;

    /**
     * @var bool
     * (PHP5)<br/>
     * Not implemented yet, always is NULL
     * @link http://php.net/manual/en/class.domattr.php#domattr.props.specified
     */
    public $specified;

    /**
     * @var string
     * (PHP5)<br/>
     * The value of the attribute
     * @link http://php.net/manual/en/class.domattr.php#domattr.props.value
     */
    public $value;

    /**
	 * (PHP 5)<br/>
	 * Checks if attribute is a defined ID
	 * @link http://php.net/manual/en/domattr.isid.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function isId () {}

        /**
	 * (PHP 5)<br/>
	 * Creates a new <classname>DOMAttr</classname> object
	 * @link http://php.net/manual/en/domattr.construct.php
	 * @param $name
	 * @param $value [optional]
         */
        public function __construct ($name, $value) {}

        /**
	 * (PHP 5)<br/>
	 * Adds a new child before a reference node
	 * @link http://php.net/manual/en/domnode.insertbefore.php
	 * @param DOMNode $newnode <p>
	 * The new node.
	 * </p>
	 * @param DOMNode $refnode [optional] <p>
	 * The reference node. If not supplied, newnode is
	 * appended to the children.
	 * </p>
	 * @return DOMNode The inserted node.
         */
	public function insertBefore (DOMNode $newnode , $refnode = null) {}

        /**
	 * (PHP 5)<br/>
	 * Replaces a child
	 * @link http://php.net/manual/en/domnode.replacechild.php
	 * @param DOMNode $newnode <p>
	 * The new node. It must be a member of the target document, i.e.
	 * created by one of the DOMDocument->createXXX() methods or imported in
	 * the document by .
	 * </p>
	 * @param DOMNode $oldnode <p>
	 * The old node.
	 * </p>
	 * @return DOMNode The old node or false if an error occur.
         */
	public function replaceChild (DOMNode $newnode , DOMNode $oldnode ) {}

        /**
	 * (PHP 5)<br/>
	 * Removes child from list of children
	 * @link http://php.net/manual/en/domnode.removechild.php
	 * @param DOMNode $oldnode <p>
	 * The removed child.
	 * </p>
	 * @return DOMNode If the child could be removed the functions returns the old child.
         */
	public function removeChild (DOMNode $oldnode ) {}

        /**
	 * (PHP 5)<br/>
	 * Adds new child at the end of the children
	 * @link http://php.net/manual/en/domnode.appendchild.php
	 * @param DOMNode $newnode <p>
	 * The appended child.
	 * </p>
	 * @return DOMNode The node added.
         */
	public function appendChild (DOMNode $newnode ) {}

	/**
	 * (PHP 5)<br/>
	 * Checks if node has children
	 * @link http://php.net/manual/en/domnode.haschildnodes.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function hasChildNodes () {}

        /**
	 * (PHP 5)<br/>
	 * Clones a node
	 * @link http://php.net/manual/en/domnode.clonenode.php
	 * @param bool $deep [optional] <p>
	 * Indicates whether to copy all descendant nodes. This parameter is 
	 * defaulted to false.
	 * </p>
	 * @return DOMNode The cloned node.
         */
	public function cloneNode ($deep = null) {}

	/**
	 * (PHP 5)<br/>
	 * Normalizes the node
	 * @link http://php.net/manual/en/domnode.normalize.php
	 * @return void 
	 */
        public function normalize () {}

        /**
	 * (PHP 5)<br/>
	 * Checks if feature is supported for specified version
	 * @link http://php.net/manual/en/domnode.issupported.php
	 * @param string $feature <p>
	 * The feature to test. See the example of 
	 * DOMImplementation::hasFeature for a
	 * list of features.
	 * </p>
	 * @param string $version <p>
	 * The version number of the feature to test.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
        public function isSupported ($feature, $version) {}

	/**
	 * (PHP 5)<br/>
	 * Checks if node has attributes
	 * @link http://php.net/manual/en/domnode.hasattributes.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function hasAttributes () {}

        /**
	 * @param DOMNode $other
         */
        public function compareDocumentPosition (DOMNode $other) {}

        /**
	 * (PHP 5)<br/>
	 * Indicates if two nodes are the same node
	 * @link http://php.net/manual/en/domnode.issamenode.php
	 * @param DOMNode $node <p>
	 * The compared node.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
	public function isSameNode (DOMNode $node ) {}

        /**
	 * (PHP 5)<br/>
	 * Gets the namespace prefix of the node based on the namespace URI
	 * @link http://php.net/manual/en/domnode.lookupprefix.php
	 * @param string $namespaceURI <p>
	 * The namespace URI.
	 * </p>
	 * @return string The prefix of the namespace.
         */
        public function lookupPrefix ($namespaceURI) {}

        /**
	 * (PHP 5)<br/>
	 * Checks if the specified namespaceURI is the default namespace or not
	 * @link http://php.net/manual/en/domnode.isdefaultnamespace.php
	 * @param string $namespaceURI <p>
	 * The namespace URI to look for.
	 * </p>
	 * @return bool Return true if namespaceURI is the default
	 * namespace, false otherwise.
         */
        public function isDefaultNamespace ($namespaceURI) {}

        /**
	 * (PHP 5)<br/>
	 * Gets the namespace URI of the node based on the prefix
	 * @link http://php.net/manual/en/domnode.lookupnamespaceuri.php
	 * @param string $prefix <p>
	 * The prefix of the namespace.
	 * </p>
	 * @return string The namespace URI of the node.
         */
        public function lookupNamespaceUri ($prefix) {}

        /**
	 * @param DOMNode $arg
         */
        public function isEqualNode (DOMNode $arg) {}

        /**
	 * @param $feature
	 * @param $version
         */
        public function getFeature ($feature, $version) {}

        /**
	 * @param $key
	 * @param $data
	 * @param $handler
         */
        public function setUserData ($key, $data, $handler) {}

        /**
	 * @param $key
         */
        public function getUserData ($key) {}

        public function getNodePath () {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Get line number for a node
	 * @link http://php.net/manual/en/domnode.getlineno.php
	 * @return int Always returns the line number where the node was defined in.
	 */
        public function getLineNo () {}

        /**
	 * @param $exclusive [optional]
	 * @param $with_comments [optional]
	 * @param $xpath [optional]
	 * @param $ns_prefixes [optional]
         */
        public function C14N ($exclusive, array $with_comments, array $xpath = null, $ns_prefixes = null) {}

        /**
	 * @param $uri
	 * @param $exclusive [optional]
	 * @param $with_comments [optional]
	 * @param $xpath [optional]
	 * @param $ns_prefixes [optional]
         */
        public function C14NFile ($uri, $exclusive, array $with_comments, array $xpath = null, $ns_prefixes = null) {}

}

/**
 * The DOMElement class
 * @link http://php.net/manual/en/class.domelement.php
 */
class DOMElement extends DOMNode  {



    /**
     * @var bool
     * (PHP 5)<br/>
     * Not implemented yet, always return NULL
     * @link http://php.net/manual/en/class.domelement.php#domelement.props.schematypeinfo
     */
    public $schemaTypeInfo ;

    /**
     * @var string
     * (PHP 5)<br/>
     * The element name
     * @link http://php.net/manual/en/class.domelement.php#domelement.props.tagname
     */
    public $tagName ;

        /**
	 * (PHP 5)<br/>
	 * Returns value of attribute
	 * @link http://php.net/manual/en/domelement.getattribute.php
	 * @param string $name <p>
	 * The name of the attribute.
	 * </p>
	 * @return string The value of the attribute, or an empty string if no attribute with the
	 * given name is found.
         */
        public function getAttribute ($name) {}

        /**
	 * (PHP 5)<br/>
	 * Adds new attribute
	 * @link http://php.net/manual/en/domelement.setattribute.php
	 * @param string $name <p>
	 * The name of the attribute.
	 * </p>
	 * @param string $value <p>
	 * The value of the attribute.
	 * </p>
	 * @return DOMAttr The new DOMAttr or false if an error occured.
         */
        public function setAttribute ($name, $value) {}

        /**
	 * (PHP 5)<br/>
	 * Removes attribute
	 * @link http://php.net/manual/en/domelement.removeattribute.php
	 * @param string $name <p>
	 * The name of the attribute.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
        public function removeAttribute ($name) {}

        /**
	 * (PHP 5)<br/>
	 * Returns attribute node
	 * @link http://php.net/manual/en/domelement.getattributenode.php
	 * @param string $name <p>
	 * The name of the attribute.
	 * </p>
	 * @return DOMAttr The attribute node.
         */
        public function getAttributeNode ($name) {}

        /**
	 * (PHP 5)<br/>
	 * Adds new attribute node to element
	 * @link http://php.net/manual/en/domelement.setattributenode.php
	 * @param DOMAttr $attr <p>
	 * The attribute node.
	 * </p>
	 * @return DOMAttr old node if the attribute has been replaced or &null;.
         */
	public function setAttributeNode ($attrDOMAttr ) {}

        /**
	 * (PHP 5)<br/>
	 * Removes attribute
	 * @link http://php.net/manual/en/domelement.removeattributenode.php
	 * @param DOMAttr $oldnode <p>
	 * The attribute node.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
	public function removeAttributeNode ($oldnodeDOMAttr ) {}

        /**
	 * (PHP 5)<br/>
	 * Gets elements by tagname
	 * @link http://php.net/manual/en/domelement.getelementsbytagname.php
	 * @param string $name <p>
	 * The tag name. Use * to return all elements within 
	 * the element tree.
	 * </p>
	 * @return DOMNodeList This function returns a new instance of the class
	 * DOMNodeList of all matched elements.
         */
        public function getElementsByTagName ($name) {}

        /**
	 * (PHP 5)<br/>
	 * Returns value of attribute
	 * @link http://php.net/manual/en/domelement.getattributens.php
	 * @param string $namespaceURI <p>
	 * The namespace URI.
	 * </p>
	 * @param string $localName <p>
	 * The local name.
	 * </p>
	 * @return string The value of the attribute, or an empty string if no attribute with the
	 * given localName and namespaceURI 
	 * is found.
         */
        public function getAttributeNS ($namespaceURI, $localName) {}

        /**
	 * (PHP 5)<br/>
	 * Adds new attribute
	 * @link http://php.net/manual/en/domelement.setattributens.php
	 * @param string $namespaceURI <p>
	 * The namespace URI.
	 * </p>
	 * @param string $qualifiedName <p>
	 * The qualified name of the attribute, as prefix:tagname.
	 * </p>
	 * @param string $value <p>
	 * The value of the attribute.
	 * </p>
	 * @return void 
         */
        public function setAttributeNS ($namespaceURI, $qualifiedName, $value) {}

        /**
	 * (PHP 5)<br/>
	 * Removes attribute
	 * @link http://php.net/manual/en/domelement.removeattributens.php
	 * @param string $namespaceURI <p>
	 * The namespace URI.
	 * </p>
	 * @param string $localName <p>
	 * The local name.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
        public function removeAttributeNS ($namespaceURI, $localName) {}

        /**
	 * (PHP 5)<br/>
	 * Returns attribute node
	 * @link http://php.net/manual/en/domelement.getattributenodens.php
	 * @param string $namespaceURI <p>
	 * The namespace URI.
	 * </p>
	 * @param string $localName <p>
	 * The local name.
	 * </p>
	 * @return DOMAttr The attribute node.
         */
        public function getAttributeNodeNS ($namespaceURI, $localName) {}

        /**
	 * (PHP 5)<br/>
	 * Adds new attribute node to element
	 * @link http://php.net/manual/en/domelement.setattributenodens.php
	 * @param DOMAttr $attr 
	 * @return DOMAttr the old node if the attribute has been replaced.
         */
	public function setAttributeNodeNS ($attrDOMAttr ) {}

        /**
	 * (PHP 5)<br/>
	 * Get elements by namespaceURI and localName
	 * @link http://php.net/manual/en/domelement.getelementsbytagnamens.php
	 * @param string $namespaceURI <p>
	 * The namespace URI.
	 * </p>
	 * @param string $localName <p>
	 * The local name. Use * to return all elements within 
	 * the element tree.
	 * </p>
	 * @return DOMNodeList This function returns a new instance of the class
	 * DOMNodeList of all matched elements in the order in
	 * which they are encountered in a preorder traversal of this element tree.
         */
        public function getElementsByTagNameNS ($namespaceURI, $localName) {}

        /**
	 * (PHP 5)<br/>
	 * Checks to see if attribute exists
	 * @link http://php.net/manual/en/domelement.hasattribute.php
	 * @param string $name <p>
	 * The attribute name.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
        public function hasAttribute ($name) {}

        /**
	 * (PHP 5)<br/>
	 * Checks to see if attribute exists
	 * @link http://php.net/manual/en/domelement.hasattributens.php
	 * @param string $namespaceURI <p>
	 * The namespace URI.
	 * </p>
	 * @param string $localName <p>
	 * The local name.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
        public function hasAttributeNS ($namespaceURI, $localName) {}

        /**
	 * (PHP 5)<br/>
	 * Declares the attribute specified by name to be of type ID
	 * @link http://php.net/manual/en/domelement.setidattribute.php
	 * @param string $name <p>
	 * The name of the attribute.
	 * </p>
	 * @param bool $isId <p>
	 * Set it to true if you want name to be of type
	 * ID, false otherwise.
	 * </p>
	 * @return void 
         */
        public function setIdAttribute ($name, $isId) {}

        /**
	 * (PHP 5)<br/>
	 * Declares the attribute specified by local name and namespace URI to be of type ID
	 * @link http://php.net/manual/en/domelement.setidattributens.php
	 * @param string $namespaceURI <p>
	 * The namespace URI of the attribute.
	 * </p>
	 * @param string $localName <p>
	 * The local name of the attribute, as prefix:tagname.
	 * </p>
	 * @param bool $isId <p>
	 * Set it to true if you want name to be of type
	 * ID, false otherwise.
	 * </p>
	 * @return void 
         */
        public function setIdAttributeNS ($namespaceURI, $localName, $isId) {}

        /**
	 * (PHP 5)<br/>
	 * Declares the attribute specified by node to be of type ID
	 * @link http://php.net/manual/en/domelement.setidattributenode.php
	 * @param DOMAttr $attr <p>
	 * The attribute node.
	 * </p>
	 * @param bool $isId <p>
	 * Set it to true if you want name to be of type
	 * ID, false otherwise.
	 * </p>
	 * @return void 
         */
	public function setIdAttributeNode ($attrDOMAttr , $isId) {}

        /**
	 * (PHP 5)<br/>
	 * Creates a new DOMElement object
	 * @link http://php.net/manual/en/domelement.construct.php
	 * @param $name string The tag name of the element. When also passing in namespaceURI, the element name may take a prefix to be associated with the URI.
	 * @param $value string [optional] The value of the element.
	 * @param $uri string [optional] A namespace URI to create the element within a specific namespace.
         */
        public function __construct ($name, $value, $uri) {}

        /**
	 * (PHP 5)<br/>
	 * Adds a new child before a reference node
	 * @link http://php.net/manual/en/domnode.insertbefore.php
	 * @param DOMNode $newnode <p>
	 * The new node.
	 * </p>
	 * @param DOMNode $refnode [optional] <p>
	 * The reference node. If not supplied, newnode is
	 * appended to the children.
	 * </p>
	 * @return DOMNode The inserted node.
         */
	public function insertBefore (DOMNode $newnode , $refnode = null) {}

        /**
	 * (PHP 5)<br/>
	 * Replaces a child
	 * @link http://php.net/manual/en/domnode.replacechild.php
	 * @param DOMNode $newnode <p>
	 * The new node. It must be a member of the target document, i.e.
	 * created by one of the DOMDocument->createXXX() methods or imported in
	 * the document by .
	 * </p>
	 * @param DOMNode $oldnode <p>
	 * The old node.
	 * </p>
	 * @return DOMNode The old node or false if an error occur.
         */
	public function replaceChild (DOMNode $newnode , DOMNode $oldnode ) {}

        /**
	 * (PHP 5)<br/>
	 * Removes child from list of children
	 * @link http://php.net/manual/en/domnode.removechild.php
	 * @param DOMNode $oldnode <p>
	 * The removed child.
	 * </p>
	 * @return DOMNode If the child could be removed the functions returns the old child.
         */
	public function removeChild (DOMNode $oldnode ) {}

        /**
	 * (PHP 5)<br/>
	 * Adds new child at the end of the children
	 * @link http://php.net/manual/en/domnode.appendchild.php
	 * @param DOMNode $newnode <p>
	 * The appended child.
	 * </p>
	 * @return DOMNode The node added.
         */
	public function appendChild (DOMNode $newnode ) {}

	/**
	 * (PHP 5)<br/>
	 * Checks if node has children
	 * @link http://php.net/manual/en/domnode.haschildnodes.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function hasChildNodes () {}

        /**
	 * (PHP 5)<br/>
	 * Clones a node
	 * @link http://php.net/manual/en/domnode.clonenode.php
	 * @param bool $deep [optional] <p>
	 * Indicates whether to copy all descendant nodes. This parameter is 
	 * defaulted to false.
	 * </p>
	 * @return DOMNode The cloned node.
         */
	public function cloneNode ($deep = null) {}

	/**
	 * (PHP 5)<br/>
	 * Normalizes the node
	 * @link http://php.net/manual/en/domnode.normalize.php
	 * @return void 
	 */
        public function normalize () {}

        /**
	 * (PHP 5)<br/>
	 * Checks if feature is supported for specified version
	 * @link http://php.net/manual/en/domnode.issupported.php
	 * @param string $feature <p>
	 * The feature to test. See the example of 
	 * DOMImplementation::hasFeature for a
	 * list of features.
	 * </p>
	 * @param string $version <p>
	 * The version number of the feature to test.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
        public function isSupported ($feature, $version) {}

	/**
	 * (PHP 5)<br/>
	 * Checks if node has attributes
	 * @link http://php.net/manual/en/domnode.hasattributes.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function hasAttributes () {}

        /**
	 * @param DOMNode $other
         */
        public function compareDocumentPosition (DOMNode $other) {}

        /**
	 * (PHP 5)<br/>
	 * Indicates if two nodes are the same node
	 * @link http://php.net/manual/en/domnode.issamenode.php
	 * @param DOMNode $node <p>
	 * The compared node.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
	public function isSameNode (DOMNode $node ) {}

        /**
	 * (PHP 5)<br/>
	 * Gets the namespace prefix of the node based on the namespace URI
	 * @link http://php.net/manual/en/domnode.lookupprefix.php
	 * @param string $namespaceURI <p>
	 * The namespace URI.
	 * </p>
	 * @return string The prefix of the namespace.
         */
        public function lookupPrefix ($namespaceURI) {}

        /**
	 * (PHP 5)<br/>
	 * Checks if the specified namespaceURI is the default namespace or not
	 * @link http://php.net/manual/en/domnode.isdefaultnamespace.php
	 * @param string $namespaceURI <p>
	 * The namespace URI to look for.
	 * </p>
	 * @return bool Return true if namespaceURI is the default
	 * namespace, false otherwise.
         */
        public function isDefaultNamespace ($namespaceURI) {}

        /**
	 * (PHP 5)<br/>
	 * Gets the namespace URI of the node based on the prefix
	 * @link http://php.net/manual/en/domnode.lookupnamespaceuri.php
	 * @param string $prefix <p>
	 * The prefix of the namespace.
	 * </p>
	 * @return string The namespace URI of the node.
         */
        public function lookupNamespaceUri ($prefix) {}

        /**
	 * @param DOMNode $arg
         */
        public function isEqualNode (DOMNode $arg) {}

        /**
	 * @param $feature
	 * @param $version
         */
        public function getFeature ($feature, $version) {}

        /**
	 * @param $key
	 * @param $data
	 * @param $handler
         */
        public function setUserData ($key, $data, $handler) {}

        /**
	 * @param $key
         */
        public function getUserData ($key) {}

        public function getNodePath () {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Get line number for a node
	 * @link http://php.net/manual/en/domnode.getlineno.php
	 * @return int Always returns the line number where the node was defined in.
	 */
        public function getLineNo () {}

        /**
	 * @param $exclusive [optional]
	 * @param $with_comments [optional]
	 * @param $xpath [optional]
	 * @param $ns_prefixes [optional]
         */
        public function C14N ($exclusive, array $with_comments, array $xpath = null, $ns_prefixes = null) {}

        /**
	 * @param $uri
	 * @param $exclusive [optional]
	 * @param $with_comments [optional]
	 * @param $xpath [optional]
	 * @param $ns_prefixes [optional]
         */
        public function C14NFile ($uri, $exclusive, array $with_comments, array $xpath = null, $ns_prefixes = null) {}

}

/**
 * The DOMText class inherits from <classname>DOMCharacterData</classname> and represents the textual content of
 * a <classname>DOMElement</classname> or <classname>DOMAttr</classname>.
 * @link http://php.net/manual/en/class.domtext.php
 */
class DOMText extends DOMCharacterData  {

    /**
     * @var
     * (PHP 5)<br/>
     * Holds all the text of logically-adjacent (not separated by Element, Comment or Processing Instruction) Text nodes.
     * @link http://php.net/manual/en/class.domtext.php#domtext.props.wholeText
     */
    public $wholeText;

        /**
	 * (PHP 5)<br/>
	 * Breaks this node into two nodes at the specified offset
	 * @link http://php.net/manual/en/domtext.splittext.php
	 * @param int $offset <p>
	 * The offset at which to split, starting from 0.
	 * </p>
	 * @return DOMText The new node of the same type, which contains all the content at and after the 
	 * offset.
         */
        public function splitText ($offset) {}

	/**
	 * (PHP 5)<br/>
	 * Indicates whether this text node contains whitespace
	 * @link http://php.net/manual/en/domtext.iswhitespaceinelementcontent.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function isWhitespaceInElementContent () {}

        public function isElementContentWhitespace () {}

        /**
	 * @param $content
         */
        public function replaceWholeText ($content) {}

        /**
	 * (PHP 5)<br/>
	 * Creates a new <classname>DOMText</classname> object
	 * @link http://php.net/manual/en/domtext.construct.php
	 * @param $value [optional] The value of the text node. If not supplied an empty text node is created.
         */
        public function __construct ($value) {}

        /**
	 * (PHP 5)<br/>
	 * Extracts a range of data from the node
	 * @link http://php.net/manual/en/domcharacterdata.substringdata.php
	 * @param int $offset <p>
	 * Start offset of substring to extract.
	 * </p>
	 * @param int $count <p>
	 * The number of characters to extract.
	 * </p>
	 * @return string The specified substring. If the sum of offset 
	 * and count exceeds the length, then all 16-bit units 
	 * to the end of the data are returned.
         */
        public function substringData ($offset, $count) {}

        /**
	 * (PHP 5)<br/>
	 * Append the string to the end of the character data of the node
	 * @link http://php.net/manual/en/domcharacterdata.appenddata.php
	 * @param string $data <p>
	 * The string to append.
	 * </p>
	 * @return void 
         */
	public function appendData ($data) {}

        /**
	 * (PHP 5)<br/>
	 * Insert a string at the specified 16-bit unit offset
	 * @link http://php.net/manual/en/domcharacterdata.insertdata.php
	 * @param int $offset <p>
	 * The character offset at which to insert.
	 * </p>
	 * @param string $data <p>
	 * The string to insert.
	 * </p>
	 * @return void 
         */
	public function insertData ($offset, $data) {}

        /**
	 * (PHP 5)<br/>
	 * Remove a range of characters from the node
	 * @link http://php.net/manual/en/domcharacterdata.deletedata.php
	 * @param int $offset <p>
	 * The offset from which to start removing.
	 * </p>
	 * @param int $count <p>
	 * The number of characters to delete. If the sum of 
	 * offset and count exceeds
	 * the length, then all characters to the end of the data are deleted.
	 * </p>
	 * @return void 
         */
        public function deleteData ($offset, $count) {}

        /**
	 * (PHP 5)<br/>
	 * Replace a substring within the DOMCharacterData node
	 * @link http://php.net/manual/en/domcharacterdata.replacedata.php
	 * @param int $offset <p>
	 * The offset from which to start replacing.
	 * </p>
	 * @param int $count <p>
	 * The number of characters to replace. If the sum of 
	 * offset and count exceeds
	 * the length, then all characters to the end of the data are replaced.
	 * </p>
	 * @param string $data <p>
	 * The string with which the range must be replaced.
	 * </p>
	 * @return void 
         */
	public function replaceData ($offset, $count, $data) {}

        /**
	 * (PHP 5)<br/>
	 * Adds a new child before a reference node
	 * @link http://php.net/manual/en/domnode.insertbefore.php
	 * @param DOMNode $newnode <p>
	 * The new node.
	 * </p>
	 * @param DOMNode $refnode [optional] <p>
	 * The reference node. If not supplied, newnode is
	 * appended to the children.
	 * </p>
	 * @return DOMNode The inserted node.
         */
	public function insertBefore (DOMNode $newnode , $refnode = null ) {}

        /**
	 * (PHP 5)<br/>
	 * Replaces a child
	 * @link http://php.net/manual/en/domnode.replacechild.php
	 * @param DOMNode $newnode <p>
	 * The new node. It must be a member of the target document, i.e.
	 * created by one of the DOMDocument->createXXX() methods or imported in
	 * the document by .
	 * </p>
	 * @param DOMNode $oldnode <p>
	 * The old node.
	 * </p>
	 * @return DOMNode The old node or false if an error occur.
         */
	public function replaceChild (DOMNode $newnode , DOMNode $oldnode ) {}

        /**
	 * (PHP 5)<br/>
	 * Removes child from list of children
	 * @link http://php.net/manual/en/domnode.removechild.php
	 * @param DOMNode $oldnode <p>
	 * The removed child.
	 * </p>
	 * @return DOMNode If the child could be removed the functions returns the old child.
         */
	public function removeChild (DOMNode $oldnode ) {}

        /**
	 * (PHP 5)<br/>
	 * Adds new child at the end of the children
	 * @link http://php.net/manual/en/domnode.appendchild.php
	 * @param DOMNode $newnode <p>
	 * The appended child.
	 * </p>
	 * @return DOMNode The node added.
         */
	public function appendChild (DOMNode $newnode ) {}

	/**
	 * (PHP 5)<br/>
	 * Checks if node has children
	 * @link http://php.net/manual/en/domnode.haschildnodes.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function hasChildNodes () {}

        /**
	 * (PHP 5)<br/>
	 * Clones a node
	 * @link http://php.net/manual/en/domnode.clonenode.php
	 * @param bool $deep [optional] <p>
	 * Indicates whether to copy all descendant nodes. This parameter is 
	 * defaulted to false.
	 * </p>
	 * @return DOMNode The cloned node.
         */
	public function cloneNode ($deep = null) {}

	/**
	 * (PHP 5)<br/>
	 * Normalizes the node
	 * @link http://php.net/manual/en/domnode.normalize.php
	 * @return void 
	 */
        public function normalize () {}

        /**
	 * (PHP 5)<br/>
	 * Checks if feature is supported for specified version
	 * @link http://php.net/manual/en/domnode.issupported.php
	 * @param string $feature <p>
	 * The feature to test. See the example of 
	 * DOMImplementation::hasFeature for a
	 * list of features.
	 * </p>
	 * @param string $version <p>
	 * The version number of the feature to test.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
        public function isSupported ($feature, $version) {}

	/**
	 * (PHP 5)<br/>
	 * Checks if node has attributes
	 * @link http://php.net/manual/en/domnode.hasattributes.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function hasAttributes () {}

        /**
	 * @param DOMNode $other
         */
        public function compareDocumentPosition (DOMNode $other) {}

        /**
	 * (PHP 5)<br/>
	 * Indicates if two nodes are the same node
	 * @link http://php.net/manual/en/domnode.issamenode.php
	 * @param DOMNode $node <p>
	 * The compared node.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
	public function isSameNode (DOMNode $node ) {}

        /**
	 * (PHP 5)<br/>
	 * Gets the namespace prefix of the node based on the namespace URI
	 * @link http://php.net/manual/en/domnode.lookupprefix.php
	 * @param string $namespaceURI <p>
	 * The namespace URI.
	 * </p>
	 * @return string The prefix of the namespace.
         */
        public function lookupPrefix ($namespaceURI) {}

        /**
	 * (PHP 5)<br/>
	 * Checks if the specified namespaceURI is the default namespace or not
	 * @link http://php.net/manual/en/domnode.isdefaultnamespace.php
	 * @param string $namespaceURI <p>
	 * The namespace URI to look for.
	 * </p>
	 * @return bool Return true if namespaceURI is the default
	 * namespace, false otherwise.
         */
        public function isDefaultNamespace ($namespaceURI) {}

        /**
	 * (PHP 5)<br/>
	 * Gets the namespace URI of the node based on the prefix
	 * @link http://php.net/manual/en/domnode.lookupnamespaceuri.php
	 * @param string $prefix <p>
	 * The prefix of the namespace.
	 * </p>
	 * @return string The namespace URI of the node.
         */
        public function lookupNamespaceUri ($prefix) {}

        /**
	 * @param DOMNode $arg
         */
        public function isEqualNode (DOMNode $arg) {}

        /**
	 * @param $feature
	 * @param $version
         */
        public function getFeature ($feature, $version) {}

        /**
	 * @param $key
	 * @param $data
	 * @param $handler
         */
        public function setUserData ($key, $data, $handler) {}

        /**
	 * @param $key
         */
        public function getUserData ($key) {}

        public function getNodePath () {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Get line number for a node
	 * @link http://php.net/manual/en/domnode.getlineno.php
	 * @return int Always returns the line number where the node was defined in.
	 */
        public function getLineNo () {}

        /**
	 * @param $exclusive [optional]
	 * @param $with_comments [optional]
	 * @param $xpath [optional]
	 * @param $ns_prefixes [optional]
         */
        public function C14N ($exclusive, array $with_comments, array $xpath = null, $ns_prefixes = null) {}

        /**
	 * @param $uri
	 * @param $exclusive [optional]
	 * @param $with_comments [optional]
	 * @param $xpath [optional]
	 * @param $ns_prefixes [optional]
         */
        public function C14NFile ($uri, $exclusive, array $with_comments, array $xpath = null, $ns_prefixes = null) {}

}

/**
 * The DOMComment class represents comment nodes,
 * characters delimited by lt;!-- and --&gt;.
 * @link http://php.net/manual/en/class.domcomment.php
 */
class DOMComment extends DOMCharacterData  {

        /**
	 * (PHP 5)<br/>
	 * Creates a new DOMComment object
	 * @link http://php.net/manual/en/domcomment.construct.php
	 * @param $value [optional] The value of the comment
         */
        public function __construct ($value) {}

        /**
	 * (PHP 5)<br/>
	 * Extracts a range of data from the node
	 * @link http://php.net/manual/en/domcharacterdata.substringdata.php
	 * @param int $offset <p>
	 * Start offset of substring to extract.
	 * </p>
	 * @param int $count <p>
	 * The number of characters to extract.
	 * </p>
	 * @return string The specified substring. If the sum of offset 
	 * and count exceeds the length, then all 16-bit units 
	 * to the end of the data are returned.
         */
        public function substringData ($offset, $count) {}

        /**
	 * (PHP 5)<br/>
	 * Append the string to the end of the character data of the node
	 * @link http://php.net/manual/en/domcharacterdata.appenddata.php
	 * @param string $data <p>
	 * The string to append.
	 * </p>
	 * @return void 
         */
	public function appendData ($data) {}

        /**
	 * (PHP 5)<br/>
	 * Insert a string at the specified 16-bit unit offset
	 * @link http://php.net/manual/en/domcharacterdata.insertdata.php
	 * @param int $offset <p>
	 * The character offset at which to insert.
	 * </p>
	 * @param string $data <p>
	 * The string to insert.
	 * </p>
	 * @return void 
         */
	public function insertData ($offset, $data) {}

        /**
	 * (PHP 5)<br/>
	 * Remove a range of characters from the node
	 * @link http://php.net/manual/en/domcharacterdata.deletedata.php
	 * @param int $offset <p>
	 * The offset from which to start removing.
	 * </p>
	 * @param int $count <p>
	 * The number of characters to delete. If the sum of 
	 * offset and count exceeds
	 * the length, then all characters to the end of the data are deleted.
	 * </p>
	 * @return void 
         */
        public function deleteData ($offset, $count) {}

        /**
	 * (PHP 5)<br/>
	 * Replace a substring within the DOMCharacterData node
	 * @link http://php.net/manual/en/domcharacterdata.replacedata.php
	 * @param int $offset <p>
	 * The offset from which to start replacing.
	 * </p>
	 * @param int $count <p>
	 * The number of characters to replace. If the sum of 
	 * offset and count exceeds
	 * the length, then all characters to the end of the data are replaced.
	 * </p>
	 * @param string $data <p>
	 * The string with which the range must be replaced.
	 * </p>
	 * @return void 
         */
	public function replaceData ($offset, $count, $data) {}

        /**
	 * (PHP 5)<br/>
	 * Adds a new child before a reference node
	 * @link http://php.net/manual/en/domnode.insertbefore.php
	 * @param DOMNode $newnode <p>
	 * The new node.
	 * </p>
	 * @param DOMNode $refnode [optional] <p>
	 * The reference node. If not supplied, newnode is
	 * appended to the children.
	 * </p>
	 * @return DOMNode The inserted node.
         */
	public function insertBefore (DOMNode $newnode , $refnode = null) {}

        /**
	 * (PHP 5)<br/>
	 * Replaces a child
	 * @link http://php.net/manual/en/domnode.replacechild.php
	 * @param DOMNode $newnode <p>
	 * The new node. It must be a member of the target document, i.e.
	 * created by one of the DOMDocument->createXXX() methods or imported in
	 * the document by .
	 * </p>
	 * @param DOMNode $oldnode <p>
	 * The old node.
	 * </p>
	 * @return DOMNode The old node or false if an error occur.
         */
	public function replaceChild (DOMNode $newnode , DOMNode $oldnode ) {}

        /**
	 * (PHP 5)<br/>
	 * Removes child from list of children
	 * @link http://php.net/manual/en/domnode.removechild.php
	 * @param DOMNode $oldnode <p>
	 * The removed child.
	 * </p>
	 * @return DOMNode If the child could be removed the functions returns the old child.
         */
	public function removeChild (DOMNode $oldnode ) {}

        /**
	 * (PHP 5)<br/>
	 * Adds new child at the end of the children
	 * @link http://php.net/manual/en/domnode.appendchild.php
	 * @param DOMNode $newnode <p>
	 * The appended child.
	 * </p>
	 * @return DOMNode The node added.
         */
	public function appendChild (DOMNode $newnode ) {}

	/**
	 * (PHP 5)<br/>
	 * Checks if node has children
	 * @link http://php.net/manual/en/domnode.haschildnodes.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function hasChildNodes () {}

        /**
	 * (PHP 5)<br/>
	 * Clones a node
	 * @link http://php.net/manual/en/domnode.clonenode.php
	 * @param bool $deep [optional] <p>
	 * Indicates whether to copy all descendant nodes. This parameter is 
	 * defaulted to false.
	 * </p>
	 * @return DOMNode The cloned node.
         */
	public function cloneNode ($deep = null) {}

	/**
	 * (PHP 5)<br/>
	 * Normalizes the node
	 * @link http://php.net/manual/en/domnode.normalize.php
	 * @return void 
	 */
        public function normalize () {}

        /**
	 * (PHP 5)<br/>
	 * Checks if feature is supported for specified version
	 * @link http://php.net/manual/en/domnode.issupported.php
	 * @param string $feature <p>
	 * The feature to test. See the example of 
	 * DOMImplementation::hasFeature for a
	 * list of features.
	 * </p>
	 * @param string $version <p>
	 * The version number of the feature to test.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
        public function isSupported ($feature, $version) {}

	/**
	 * (PHP 5)<br/>
	 * Checks if node has attributes
	 * @link http://php.net/manual/en/domnode.hasattributes.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function hasAttributes () {}

        /**
	 * @param DOMNode $other
         */
        public function compareDocumentPosition (DOMNode $other) {}

        /**
	 * (PHP 5)<br/>
	 * Indicates if two nodes are the same node
	 * @link http://php.net/manual/en/domnode.issamenode.php
	 * @param DOMNode $node <p>
	 * The compared node.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
	public function isSameNode (DOMNode $node ) {}

        /**
	 * (PHP 5)<br/>
	 * Gets the namespace prefix of the node based on the namespace URI
	 * @link http://php.net/manual/en/domnode.lookupprefix.php
	 * @param string $namespaceURI <p>
	 * The namespace URI.
	 * </p>
	 * @return string The prefix of the namespace.
         */
        public function lookupPrefix ($namespaceURI) {}

        /**
	 * (PHP 5)<br/>
	 * Checks if the specified namespaceURI is the default namespace or not
	 * @link http://php.net/manual/en/domnode.isdefaultnamespace.php
	 * @param string $namespaceURI <p>
	 * The namespace URI to look for.
	 * </p>
	 * @return bool Return true if namespaceURI is the default
	 * namespace, false otherwise.
         */
        public function isDefaultNamespace ($namespaceURI) {}

        /**
	 * (PHP 5)<br/>
	 * Gets the namespace URI of the node based on the prefix
	 * @link http://php.net/manual/en/domnode.lookupnamespaceuri.php
	 * @param string $prefix <p>
	 * The prefix of the namespace.
	 * </p>
	 * @return string The namespace URI of the node.
         */
        public function lookupNamespaceUri ($prefix) {}

        /**
	 * @param DOMNode $arg
         */
        public function isEqualNode (DOMNode $arg) {}

        /**
	 * @param $feature
	 * @param $version
         */
        public function getFeature ($feature, $version) {}

        /**
	 * @param $key
	 * @param $data
	 * @param $handler
         */
        public function setUserData ($key, $data, $handler) {}

        /**
	 * @param $key
         */
        public function getUserData ($key) {}

        public function getNodePath () {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Get line number for a node
	 * @link http://php.net/manual/en/domnode.getlineno.php
	 * @return int Always returns the line number where the node was defined in.
	 */
        public function getLineNo () {}

        /**
	 * @param $exclusive [optional]
	 * @param $with_comments [optional]
	 * @param $xpath [optional]
	 * @param $ns_prefixes [optional]
         */
        public function C14N ($exclusive, array $with_comments, array $xpath = null, $ns_prefixes = null) {}

        /**
	 * @param $uri
	 * @param $exclusive [optional]
	 * @param $with_comments [optional]
	 * @param $xpath [optional]
	 * @param $ns_prefixes [optional]
         */
        public function C14NFile ($uri, $exclusive, array $with_comments, array $xpath = null, $ns_prefixes = null) {}

}

class DOMTypeinfo  {
}

class DOMUserDataHandler  {

        public function handle () {}

}

class DOMDomError  {
}

class DOMErrorHandler  {

        /**
	 * @param DOMDomError $error
         */
        public function handleError (DOMDomError $error) {}

}

class DOMLocator  {
}

class DOMConfiguration  {

        /**
	 * @param $name
	 * @param $value
         */
        public function setParameter ($name, $value) {}

        /**
	 * @param $name [optional]
         */
        public function getParameter ($name) {}

        /**
	 * @param $name [optional]
	 * @param $value [optional]
         */
        public function canSetParameter ($name, $value) {}

}

class DOMCdataSection extends DOMText  {

        /**
	 * @param $value
         */
        public function __construct ($value) {}

        /**
	 * (PHP 5)<br/>
	 * Breaks this node into two nodes at the specified offset
	 * @link http://php.net/manual/en/domtext.splittext.php
	 * @param int $offset <p>
	 * The offset at which to split, starting from 0.
	 * </p>
	 * @return DOMText The new node of the same type, which contains all the content at and after the 
	 * offset.
         */
        public function splitText ($offset) {}

	/**
	 * (PHP 5)<br/>
	 * Indicates whether this text node contains whitespace
	 * @link http://php.net/manual/en/domtext.iswhitespaceinelementcontent.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function isWhitespaceInElementContent () {}

        public function isElementContentWhitespace () {}

        /**
	 * @param $content
         */
        public function replaceWholeText ($content) {}

        /**
	 * (PHP 5)<br/>
	 * Extracts a range of data from the node
	 * @link http://php.net/manual/en/domcharacterdata.substringdata.php
	 * @param int $offset <p>
	 * Start offset of substring to extract.
	 * </p>
	 * @param int $count <p>
	 * The number of characters to extract.
	 * </p>
	 * @return string The specified substring. If the sum of offset 
	 * and count exceeds the length, then all 16-bit units 
	 * to the end of the data are returned.
         */
        public function substringData ($offset, $count) {}

        /**
	 * (PHP 5)<br/>
	 * Append the string to the end of the character data of the node
	 * @link http://php.net/manual/en/domcharacterdata.appenddata.php
	 * @param string $data <p>
	 * The string to append.
	 * </p>
	 * @return void 
         */
	public function appendData ($data) {}

        /**
	 * (PHP 5)<br/>
	 * Insert a string at the specified 16-bit unit offset
	 * @link http://php.net/manual/en/domcharacterdata.insertdata.php
	 * @param int $offset <p>
	 * The character offset at which to insert.
	 * </p>
	 * @param string $data <p>
	 * The string to insert.
	 * </p>
	 * @return void 
         */
	public function insertData ($offset, $data) {}

        /**
	 * (PHP 5)<br/>
	 * Remove a range of characters from the node
	 * @link http://php.net/manual/en/domcharacterdata.deletedata.php
	 * @param int $offset <p>
	 * The offset from which to start removing.
	 * </p>
	 * @param int $count <p>
	 * The number of characters to delete. If the sum of 
	 * offset and count exceeds
	 * the length, then all characters to the end of the data are deleted.
	 * </p>
	 * @return void 
         */
        public function deleteData ($offset, $count) {}

        /**
	 * (PHP 5)<br/>
	 * Replace a substring within the DOMCharacterData node
	 * @link http://php.net/manual/en/domcharacterdata.replacedata.php
	 * @param int $offset <p>
	 * The offset from which to start replacing.
	 * </p>
	 * @param int $count <p>
	 * The number of characters to replace. If the sum of 
	 * offset and count exceeds
	 * the length, then all characters to the end of the data are replaced.
	 * </p>
	 * @param string $data <p>
	 * The string with which the range must be replaced.
	 * </p>
	 * @return void 
         */
	public function replaceData ($offset, $count, $data) {}

        /**
	 * (PHP 5)<br/>
	 * Adds a new child before a reference node
	 * @link http://php.net/manual/en/domnode.insertbefore.php
	 * @param DOMNode $newnode <p>
	 * The new node.
	 * </p>
	 * @param DOMNode $refnode [optional] <p>
	 * The reference node. If not supplied, newnode is
	 * appended to the children.
	 * </p>
	 * @return DOMNode The inserted node.
         */
	public function insertBefore (DOMNode $newnode , $refnode = null) {}

        /**
	 * (PHP 5)<br/>
	 * Replaces a child
	 * @link http://php.net/manual/en/domnode.replacechild.php
	 * @param DOMNode $newnode <p>
	 * The new node. It must be a member of the target document, i.e.
	 * created by one of the DOMDocument->createXXX() methods or imported in
	 * the document by .
	 * </p>
	 * @param DOMNode $oldnode <p>
	 * The old node.
	 * </p>
	 * @return DOMNode The old node or false if an error occur.
         */
	public function replaceChild (DOMNode $newnode , DOMNode $oldnode ) {}

        /**
	 * (PHP 5)<br/>
	 * Removes child from list of children
	 * @link http://php.net/manual/en/domnode.removechild.php
	 * @param DOMNode $oldnode <p>
	 * The removed child.
	 * </p>
	 * @return DOMNode If the child could be removed the functions returns the old child.
         */
	public function removeChild (DOMNode $oldnode ) {}

        /**
	 * (PHP 5)<br/>
	 * Adds new child at the end of the children
	 * @link http://php.net/manual/en/domnode.appendchild.php
	 * @param DOMNode $newnode <p>
	 * The appended child.
	 * </p>
	 * @return DOMNode The node added.
         */
	public function appendChild (DOMNode $newnode ) {}

	/**
	 * (PHP 5)<br/>
	 * Checks if node has children
	 * @link http://php.net/manual/en/domnode.haschildnodes.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function hasChildNodes () {}

        /**
	 * (PHP 5)<br/>
	 * Clones a node
	 * @link http://php.net/manual/en/domnode.clonenode.php
	 * @param bool $deep [optional] <p>
	 * Indicates whether to copy all descendant nodes. This parameter is 
	 * defaulted to false.
	 * </p>
	 * @return DOMNode The cloned node.
         */
	public function cloneNode ($deep = null) {}

	/**
	 * (PHP 5)<br/>
	 * Normalizes the node
	 * @link http://php.net/manual/en/domnode.normalize.php
	 * @return void 
	 */
        public function normalize () {}

        /**
	 * (PHP 5)<br/>
	 * Checks if feature is supported for specified version
	 * @link http://php.net/manual/en/domnode.issupported.php
	 * @param string $feature <p>
	 * The feature to test. See the example of 
	 * DOMImplementation::hasFeature for a
	 * list of features.
	 * </p>
	 * @param string $version <p>
	 * The version number of the feature to test.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
        public function isSupported ($feature, $version) {}

	/**
	 * (PHP 5)<br/>
	 * Checks if node has attributes
	 * @link http://php.net/manual/en/domnode.hasattributes.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function hasAttributes () {}

        /**
	 * @param DOMNode $other
         */
        public function compareDocumentPosition (DOMNode $other) {}

        /**
	 * (PHP 5)<br/>
	 * Indicates if two nodes are the same node
	 * @link http://php.net/manual/en/domnode.issamenode.php
	 * @param DOMNode $node <p>
	 * The compared node.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
	public function isSameNode (DOMNode $node ) {}

        /**
	 * (PHP 5)<br/>
	 * Gets the namespace prefix of the node based on the namespace URI
	 * @link http://php.net/manual/en/domnode.lookupprefix.php
	 * @param string $namespaceURI <p>
	 * The namespace URI.
	 * </p>
	 * @return string The prefix of the namespace.
         */
        public function lookupPrefix ($namespaceURI) {}

        /**
	 * (PHP 5)<br/>
	 * Checks if the specified namespaceURI is the default namespace or not
	 * @link http://php.net/manual/en/domnode.isdefaultnamespace.php
	 * @param string $namespaceURI <p>
	 * The namespace URI to look for.
	 * </p>
	 * @return bool Return true if namespaceURI is the default
	 * namespace, false otherwise.
         */
        public function isDefaultNamespace ($namespaceURI) {}

        /**
	 * (PHP 5)<br/>
	 * Gets the namespace URI of the node based on the prefix
	 * @link http://php.net/manual/en/domnode.lookupnamespaceuri.php
	 * @param string $prefix <p>
	 * The prefix of the namespace.
	 * </p>
	 * @return string The namespace URI of the node.
         */
        public function lookupNamespaceUri ($prefix) {}

        /**
	 * @param DOMNode $arg
         */
        public function isEqualNode (DOMNode $arg) {}

        /**
	 * @param $feature
	 * @param $version
         */
        public function getFeature ($feature, $version) {}

        /**
	 * @param $key
	 * @param $data
	 * @param $handler
         */
        public function setUserData ($key, $data, $handler) {}

        /**
	 * @param $key
         */
        public function getUserData ($key) {}

        public function getNodePath () {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Get line number for a node
	 * @link http://php.net/manual/en/domnode.getlineno.php
	 * @return int Always returns the line number where the node was defined in.
	 */
        public function getLineNo () {}

        /**
	 * @param $exclusive [optional]
	 * @param $with_comments [optional]
	 * @param $xpath [optional]
	 * @param $ns_prefixes [optional]
         */
        public function C14N ($exclusive, array $with_comments, array $xpath = null, $ns_prefixes = null) {}

        /**
	 * @param $uri
	 * @param $exclusive [optional]
	 * @param $with_comments [optional]
	 * @param $xpath [optional]
	 * @param $ns_prefixes [optional]
         */
        public function C14NFile ($uri, $exclusive, array $with_comments, array $xpath = null, $ns_prefixes = null) {}

}

/**
 * The DOMDocumentType class
 * @link http://php.net/manual/en/class.domdocumenttype.php
 */
class DOMDocumentType extends DOMNode  {

    /**
     * @var string
     * (PHP 5)<br/>
     * The public identifier of the external subset.
     * @link http://php.net/manual/en/class.domdocumenttype.php#domdocumenttype.props.publicid
     */
    public $publicId ;

    /**
     * @var string
     * (PHP 5)<br/>
     * The system identifier of the external subset. This may be an absolute URI or not.
     * @link http://php.net/manual/en/class.domdocumenttype.php#domdocumenttype.props.systemid
     */
    public $systemId ;

    /**
     * @var string
     * (PHP 5)<br/>
     * The name of DTD; i.e., the name immediately following the DOCTYPE keyword.
     * @link http://php.net/manual/en/class.domdocumenttype.php#domdocumenttype.props.name
     */
    public $name ;

    /**
     * @var DOMNamedNodeMap
     * (PHP 5)<br/>
     * A <classname>DOMNamedNodeMap</classname> containing the general entities, both external and internal, declared in the DTD.
     * @link http://php.net/manual/en/class.domdocumenttype.php#domdocumenttype.props.entities
     */
    public $entities ;

    /**
     * @var DOMNamedNodeMap
     * (PHP 5)<br/>
     * A <clasname>DOMNamedNodeMap</classname> containing the notations declared in the DTD.
     * @link http://php.net/manual/en/class.domdocumenttype.php#domdocumenttype.props.notations
     */
    public $notations ;

    /**
     * @var string
     * (PHP 5)<br/>
     * The internal subset as a string, or null if there is none. This is does not contain the delimiting square brackets.
     * @link http://php.net/manual/en/class.domdocumenttype.php#domdocumenttype.props.internalsubset
     */
    public $internalSubset ;


        /**
	 * (PHP 5)<br/>
	 * Adds a new child before a reference node
	 * @link http://php.net/manual/en/domnode.insertbefore.php
	 * @param DOMNode $newnode <p>
	 * The new node.
	 * </p>
	 * @param DOMNode $refnode [optional] <p>
	 * The reference node. If not supplied, newnode is
	 * appended to the children.
	 * </p>
	 * @return DOMNode The inserted node.
         */
	public function insertBefore (DOMNode $newnode , $refnode = null) {}

        /**
	 * (PHP 5)<br/>
	 * Replaces a child
	 * @link http://php.net/manual/en/domnode.replacechild.php
	 * @param DOMNode $newnode <p>
	 * The new node. It must be a member of the target document, i.e.
	 * created by one of the DOMDocument->createXXX() methods or imported in
	 * the document by .
	 * </p>
	 * @param DOMNode $oldnode <p>
	 * The old node.
	 * </p>
	 * @return DOMNode The old node or false if an error occur.
         */
	public function replaceChild (DOMNode $newnode , DOMNode $oldnode ) {}

        /**
	 * (PHP 5)<br/>
	 * Removes child from list of children
	 * @link http://php.net/manual/en/domnode.removechild.php
	 * @param DOMNode $oldnode <p>
	 * The removed child.
	 * </p>
	 * @return DOMNode If the child could be removed the functions returns the old child.
         */
	public function removeChild (DOMNode $oldnode ) {}

        /**
	 * (PHP 5)<br/>
	 * Adds new child at the end of the children
	 * @link http://php.net/manual/en/domnode.appendchild.php
	 * @param DOMNode $newnode <p>
	 * The appended child.
	 * </p>
	 * @return DOMNode The node added.
         */
	public function appendChild (DOMNode $newnode ) {}

	/**
	 * (PHP 5)<br/>
	 * Checks if node has children
	 * @link http://php.net/manual/en/domnode.haschildnodes.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function hasChildNodes () {}

        /**
	 * (PHP 5)<br/>
	 * Clones a node
	 * @link http://php.net/manual/en/domnode.clonenode.php
	 * @param bool $deep [optional] <p>
	 * Indicates whether to copy all descendant nodes. This parameter is 
	 * defaulted to false.
	 * </p>
	 * @return DOMNode The cloned node.
         */
	public function cloneNode ($deep = null) {}

	/**
	 * (PHP 5)<br/>
	 * Normalizes the node
	 * @link http://php.net/manual/en/domnode.normalize.php
	 * @return void 
	 */
        public function normalize () {}

        /**
	 * (PHP 5)<br/>
	 * Checks if feature is supported for specified version
	 * @link http://php.net/manual/en/domnode.issupported.php
	 * @param string $feature <p>
	 * The feature to test. See the example of 
	 * DOMImplementation::hasFeature for a
	 * list of features.
	 * </p>
	 * @param string $version <p>
	 * The version number of the feature to test.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
        public function isSupported ($feature, $version) {}

	/**
	 * (PHP 5)<br/>
	 * Checks if node has attributes
	 * @link http://php.net/manual/en/domnode.hasattributes.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function hasAttributes () {}

        /**
	 * @param DOMNode $other
         */
        public function compareDocumentPosition (DOMNode $other) {}

        /**
	 * (PHP 5)<br/>
	 * Indicates if two nodes are the same node
	 * @link http://php.net/manual/en/domnode.issamenode.php
	 * @param DOMNode $node <p>
	 * The compared node.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
	public function isSameNode (DOMNode $node ) {}

        /**
	 * (PHP 5)<br/>
	 * Gets the namespace prefix of the node based on the namespace URI
	 * @link http://php.net/manual/en/domnode.lookupprefix.php
	 * @param string $namespaceURI <p>
	 * The namespace URI.
	 * </p>
	 * @return string The prefix of the namespace.
         */
        public function lookupPrefix ($namespaceURI) {}

        /**
	 * (PHP 5)<br/>
	 * Checks if the specified namespaceURI is the default namespace or not
	 * @link http://php.net/manual/en/domnode.isdefaultnamespace.php
	 * @param string $namespaceURI <p>
	 * The namespace URI to look for.
	 * </p>
	 * @return bool Return true if namespaceURI is the default
	 * namespace, false otherwise.
         */
        public function isDefaultNamespace ($namespaceURI) {}

        /**
	 * (PHP 5)<br/>
	 * Gets the namespace URI of the node based on the prefix
	 * @link http://php.net/manual/en/domnode.lookupnamespaceuri.php
	 * @param string $prefix <p>
	 * The prefix of the namespace.
	 * </p>
	 * @return string The namespace URI of the node.
         */
        public function lookupNamespaceUri ($prefix) {}

        /**
	 * @param DOMNode $arg
         */
        public function isEqualNode (DOMNode $arg) {}

        /**
	 * @param $feature
	 * @param $version
         */
        public function getFeature ($feature, $version) {}

        /**
	 * @param $key
	 * @param $data
	 * @param $handler
         */
        public function setUserData ($key, $data, $handler) {}

        /**
	 * @param $key
         */
        public function getUserData ($key) {}

        public function getNodePath () {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Get line number for a node
	 * @link http://php.net/manual/en/domnode.getlineno.php
	 * @return int Always returns the line number where the node was defined in.
	 */
        public function getLineNo () {}

        /**
	 * @param $exclusive [optional]
	 * @param $with_comments [optional]
	 * @param $xpath [optional]
	 * @param $ns_prefixes [optional]
         */
        public function C14N ($exclusive, array $with_comments, array $xpath = null, $ns_prefixes = null) {}

        /**
	 * @param $uri
	 * @param $exclusive [optional]
	 * @param $with_comments [optional]
	 * @param $xpath [optional]
	 * @param $ns_prefixes [optional]
         */
        public function C14NFile ($uri, $exclusive, array $with_comments, array $xpath = null, $ns_prefixes = null) {}

}

/**
 * The DOMNotation class
 * @link http://php.net/manual/en/class.domnotation.php
 */
class DOMNotation  {


    /**
     * @var string
     * (PHP 5)<br/>
     *
     * @link http://php.net/manual/en/class.domnotation.php#domnotation.props.publicid
     */
    public $publicId;

    /**
     * @var string
     * (PHP 5)<br/>
     *
     * @link http://php.net/manual/en/class.domnotation.php#domnotation.props.systemid
     */
    public $systemId;

}

/**
 * The DOMEntity class represents a known entity, either parsed or unparsed, in an XML document.
 * @link http://php.net/manual/en/class.domentity.php
 */
class DOMEntity extends DOMNode  {

    /**
     * @var string
     * (PHP 5)<br/>
     * The public identifier associated with the entity if specified, and NULL otherwise.
     * @link http://php.net/manual/en/class.domentity.php#domentity.props.publicid
     */
    public $publicId ;

    /**
     * @var string
     * (PHP 5)<br/>
     * The system identifier associated with the entity if specified, and NULL otherwise. This may be an
     * absolute URI or not.
     * @link http://php.net/manual/en/class.domentity.php#domentity.props.systemid
     */
    public $systemId ;

    /**
     * @var string
     * (PHP 5)<br/>
     * For unparsed entities, the name of the notation for the entity. For parsed entities, this is NULL.
     * @link http://php.net/manual/en/class.domentity.php#domentity.props.notationname
     */
    public $notationName ;

    /**
     * @var string
     * (PHP 5)<br/>
     * An attribute specifying the encoding used for this entity at the time of parsing, when it is an external
     * parsed entity. This is NULL if it an entity from the internal subset or if it is not known.
     * @link http://php.net/manual/en/class.domentity.php#domentity.props.actualencoding
     */
    public $actualEncoding ;

    /**
     * @var string
     * (PHP 5)<br/>
     * An attribute specifying, as part of the text declaration, the encoding of this entity, when it is an external
     * parsed entity. This is NULL otherwise.
     * @link http://php.net/manual/en/class.domentity.php#domentity.props.encoding
     */
    public $encoding ;

    /**
     * @var string
     * (PHP 5)<br/>
     * An attribute specifying, as part of the text declaration, the version number of this entity, when it is an
     * external parsed entity. This is NULL otherwise.
     * @link http://php.net/manual/en/class.domentity.php#domentity.props.version
     */
    public $version ;


        /**
	 * (PHP 5)<br/>
	 * Adds a new child before a reference node
	 * @link http://php.net/manual/en/domnode.insertbefore.php
	 * @param DOMNode $newnode <p>
	 * The new node.
	 * </p>
	 * @param DOMNode $refnode [optional] <p>
	 * The reference node. If not supplied, newnode is
	 * appended to the children.
	 * </p>
	 * @return DOMNode The inserted node.
         */
	public function insertBefore (DOMNode $newnode , $refnode = null) {}

        /**
	 * (PHP 5)<br/>
	 * Replaces a child
	 * @link http://php.net/manual/en/domnode.replacechild.php
	 * @param DOMNode $newnode <p>
	 * The new node. It must be a member of the target document, i.e.
	 * created by one of the DOMDocument->createXXX() methods or imported in
	 * the document by .
	 * </p>
	 * @param DOMNode $oldnode <p>
	 * The old node.
	 * </p>
	 * @return DOMNode The old node or false if an error occur.
         */
	public function replaceChild (DOMNode $newnode , DOMNode $oldnode ) {}

        /**
	 * (PHP 5)<br/>
	 * Removes child from list of children
	 * @link http://php.net/manual/en/domnode.removechild.php
	 * @param DOMNode $oldnode <p>
	 * The removed child.
	 * </p>
	 * @return DOMNode If the child could be removed the functions returns the old child.
         */
	public function removeChild (DOMNode $oldnode ) {}

        /**
	 * (PHP 5)<br/>
	 * Adds new child at the end of the children
	 * @link http://php.net/manual/en/domnode.appendchild.php
	 * @param DOMNode $newnode <p>
	 * The appended child.
	 * </p>
	 * @return DOMNode The node added.
         */
	public function appendChild (DOMNode $newnode ) {}

	/**
	 * (PHP 5)<br/>
	 * Checks if node has children
	 * @link http://php.net/manual/en/domnode.haschildnodes.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function hasChildNodes () {}

        /**
	 * (PHP 5)<br/>
	 * Clones a node
	 * @link http://php.net/manual/en/domnode.clonenode.php
	 * @param bool $deep [optional] <p>
	 * Indicates whether to copy all descendant nodes. This parameter is 
	 * defaulted to false.
	 * </p>
	 * @return DOMNode The cloned node.
         */
	public function cloneNode ($deep = null) {}

	/**
	 * (PHP 5)<br/>
	 * Normalizes the node
	 * @link http://php.net/manual/en/domnode.normalize.php
	 * @return void 
	 */
        public function normalize () {}

        /**
	 * (PHP 5)<br/>
	 * Checks if feature is supported for specified version
	 * @link http://php.net/manual/en/domnode.issupported.php
	 * @param string $feature <p>
	 * The feature to test. See the example of 
	 * DOMImplementation::hasFeature for a
	 * list of features.
	 * </p>
	 * @param string $version <p>
	 * The version number of the feature to test.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
        public function isSupported ($feature, $version) {}

	/**
	 * (PHP 5)<br/>
	 * Checks if node has attributes
	 * @link http://php.net/manual/en/domnode.hasattributes.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function hasAttributes () {}

        /**
	 * @param DOMNode $other
         */
        public function compareDocumentPosition (DOMNode $other) {}

        /**
	 * (PHP 5)<br/>
	 * Indicates if two nodes are the same node
	 * @link http://php.net/manual/en/domnode.issamenode.php
	 * @param DOMNode $node <p>
	 * The compared node.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
	public function isSameNode (DOMNode $node ) {}

        /**
	 * (PHP 5)<br/>
	 * Gets the namespace prefix of the node based on the namespace URI
	 * @link http://php.net/manual/en/domnode.lookupprefix.php
	 * @param string $namespaceURI <p>
	 * The namespace URI.
	 * </p>
	 * @return string The prefix of the namespace.
         */
        public function lookupPrefix ($namespaceURI) {}

        /**
	 * (PHP 5)<br/>
	 * Checks if the specified namespaceURI is the default namespace or not
	 * @link http://php.net/manual/en/domnode.isdefaultnamespace.php
	 * @param string $namespaceURI <p>
	 * The namespace URI to look for.
	 * </p>
	 * @return bool Return true if namespaceURI is the default
	 * namespace, false otherwise.
         */
        public function isDefaultNamespace ($namespaceURI) {}

        /**
	 * (PHP 5)<br/>
	 * Gets the namespace URI of the node based on the prefix
	 * @link http://php.net/manual/en/domnode.lookupnamespaceuri.php
	 * @param string $prefix <p>
	 * The prefix of the namespace.
	 * </p>
	 * @return string The namespace URI of the node.
         */
        public function lookupNamespaceUri ($prefix) {}

        /**
	 * @param DOMNode $arg
         */
        public function isEqualNode (DOMNode $arg) {}

        /**
	 * @param $feature
	 * @param $version
         */
        public function getFeature ($feature, $version) {}

        /**
	 * @param $key
	 * @param $data
	 * @param $handler
         */
        public function setUserData ($key, $data, $handler) {}

        /**
	 * @param $key
         */
        public function getUserData ($key) {}

        public function getNodePath () {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Get line number for a node
	 * @link http://php.net/manual/en/domnode.getlineno.php
	 * @return int Always returns the line number where the node was defined in.
	 */
        public function getLineNo () {}

        /**
	 * @param $exclusive [optional]
	 * @param $with_comments [optional]
	 * @param $xpath [optional]
	 * @param $ns_prefixes [optional]
         */
        public function C14N ($exclusive, array $with_comments, array $xpath = null, $ns_prefixes = null) {}

        /**
	 * @param $uri
	 * @param $exclusive [optional]
	 * @param $with_comments [optional]
	 * @param $xpath [optional]
	 * @param $ns_prefixes [optional]
         */
        public function C14NFile ($uri, $exclusive, array $with_comments, array $xpath = null, $ns_prefixes = null) {}

}

/**
 * Extends DOMNode.
 * @link http://php.net/manual/en/class.domentityreference.php
 */
class DOMEntityReference extends DOMNode  {

        /**
	 * (PHP 5)<br/>
	 * Creates a new DOMEntityReference object
	 * @link http://php.net/manual/en/domentityreference.construct.php
	 * @param $name string The name of the entity reference.
         */
        public function __construct ($name) {}

        /**
	 * (PHP 5)<br/>
	 * Adds a new child before a reference node
	 * @link http://php.net/manual/en/domnode.insertbefore.php
	 * @param DOMNode $newnode <p>
	 * The new node.
	 * </p>
	 * @param DOMNode $refnode [optional] <p>
	 * The reference node. If not supplied, newnode is
	 * appended to the children.
	 * </p>
	 * @return DOMNode The inserted node.
         */
	public function insertBefore (DOMNode $newnode , $refnode = null) {}

        /**
	 * (PHP 5)<br/>
	 * Replaces a child
	 * @link http://php.net/manual/en/domnode.replacechild.php
	 * @param DOMNode $newnode <p>
	 * The new node. It must be a member of the target document, i.e.
	 * created by one of the DOMDocument->createXXX() methods or imported in
	 * the document by .
	 * </p>
	 * @param DOMNode $oldnode <p>
	 * The old node.
	 * </p>
	 * @return DOMNode The old node or false if an error occur.
         */
	public function replaceChild (DOMNode $newnode , DOMNode $oldnode ) {}

        /**
	 * (PHP 5)<br/>
	 * Removes child from list of children
	 * @link http://php.net/manual/en/domnode.removechild.php
	 * @param DOMNode $oldnode <p>
	 * The removed child.
	 * </p>
	 * @return DOMNode If the child could be removed the functions returns the old child.
         */
	public function removeChild (DOMNode $oldnode ) {}

        /**
	 * (PHP 5)<br/>
	 * Adds new child at the end of the children
	 * @link http://php.net/manual/en/domnode.appendchild.php
	 * @param DOMNode $newnode <p>
	 * The appended child.
	 * </p>
	 * @return DOMNode The node added.
         */
	public function appendChild (DOMNode $newnode ) {}

	/**
	 * (PHP 5)<br/>
	 * Checks if node has children
	 * @link http://php.net/manual/en/domnode.haschildnodes.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function hasChildNodes () {}

        /**
	 * (PHP 5)<br/>
	 * Clones a node
	 * @link http://php.net/manual/en/domnode.clonenode.php
	 * @param bool $deep [optional] <p>
	 * Indicates whether to copy all descendant nodes. This parameter is 
	 * defaulted to false.
	 * </p>
	 * @return DOMNode The cloned node.
         */
	public function cloneNode ($deep = null) {}

	/**
	 * (PHP 5)<br/>
	 * Normalizes the node
	 * @link http://php.net/manual/en/domnode.normalize.php
	 * @return void 
	 */
        public function normalize () {}

        /**
	 * (PHP 5)<br/>
	 * Checks if feature is supported for specified version
	 * @link http://php.net/manual/en/domnode.issupported.php
	 * @param string $feature <p>
	 * The feature to test. See the example of 
	 * DOMImplementation::hasFeature for a
	 * list of features.
	 * </p>
	 * @param string $version <p>
	 * The version number of the feature to test.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
        public function isSupported ($feature, $version) {}

	/**
	 * (PHP 5)<br/>
	 * Checks if node has attributes
	 * @link http://php.net/manual/en/domnode.hasattributes.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function hasAttributes () {}

        /**
	 * @param DOMNode $other
         */
        public function compareDocumentPosition (DOMNode $other) {}

        /**
	 * (PHP 5)<br/>
	 * Indicates if two nodes are the same node
	 * @link http://php.net/manual/en/domnode.issamenode.php
	 * @param DOMNode $node <p>
	 * The compared node.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
	public function isSameNode (DOMNode $node ) {}

        /**
	 * (PHP 5)<br/>
	 * Gets the namespace prefix of the node based on the namespace URI
	 * @link http://php.net/manual/en/domnode.lookupprefix.php
	 * @param string $namespaceURI <p>
	 * The namespace URI.
	 * </p>
	 * @return string The prefix of the namespace.
         */
        public function lookupPrefix ($namespaceURI) {}

        /**
	 * (PHP 5)<br/>
	 * Checks if the specified namespaceURI is the default namespace or not
	 * @link http://php.net/manual/en/domnode.isdefaultnamespace.php
	 * @param string $namespaceURI <p>
	 * The namespace URI to look for.
	 * </p>
	 * @return bool Return true if namespaceURI is the default
	 * namespace, false otherwise.
         */
        public function isDefaultNamespace ($namespaceURI) {}

        /**
	 * (PHP 5)<br/>
	 * Gets the namespace URI of the node based on the prefix
	 * @link http://php.net/manual/en/domnode.lookupnamespaceuri.php
	 * @param string $prefix <p>
	 * The prefix of the namespace.
	 * </p>
	 * @return string The namespace URI of the node.
         */
        public function lookupNamespaceUri ($prefix) {}

        /**
	 * @param DOMNode $arg
         */
        public function isEqualNode (DOMNode $arg) {}

        /**
	 * @param $feature
	 * @param $version
         */
        public function getFeature ($feature, $version) {}

        /**
	 * @param $key
	 * @param $data
	 * @param $handler
         */
        public function setUserData ($key, $data, $handler) {}

        /**
	 * @param $key
         */
        public function getUserData ($key) {}

        public function getNodePath () {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Get line number for a node
	 * @link http://php.net/manual/en/domnode.getlineno.php
	 * @return int Always returns the line number where the node was defined in.
	 */
        public function getLineNo () {}

        /**
	 * @param $exclusive [optional]
	 * @param $with_comments [optional]
	 * @param $xpath [optional]
	 * @param $ns_prefixes [optional]
         */
        public function C14N ($exclusive, array $with_comments, array $xpath = null, $ns_prefixes = null) {}

        /**
	 * @param $uri
	 * @param $exclusive [optional]
	 * @param $with_comments [optional]
	 * @param $xpath [optional]
	 * @param $ns_prefixes [optional]
         */
        public function C14NFile ($uri, $exclusive, array $with_comments, array $xpath = null, $ns_prefixes = null) {}

}

/**
 * The DOMProcessingInstruction class
 * @link http://php.net/manual/en/class.domprocessinginstruction.php
 */
class DOMProcessingInstruction extends DOMNode  {

    /**
     * @var
     * (PHP 5)<br/>
     *
     * @link http://php.net/manual/en/class.domprocessinginstruction.php#domprocessinginstruction.props.target
     */
    public $target;

    /**
     * @var
     * (PHP 5)<br/>
     *
     * @link http://php.net/manual/en/class.domprocessinginstruction.php#domprocessinginstruction.props.data
     */
    public $data;

        /**
	 * (PHP 5)<br/>
	 * Creates a new <classname>DOMProcessingInstruction</classname> object
	 * @link http://php.net/manual/en/domprocessinginstruction.construct.php
	 * @param $name string The tag name of the processing instruction.
	 * @param $value string [optional] The value of the processing instruction.
         */
        public function __construct ($name, $value) {}

        /**
	 * (PHP 5)<br/>
	 * Adds a new child before a reference node
	 * @link http://php.net/manual/en/domnode.insertbefore.php
	 * @param DOMNode $newnode <p>
	 * The new node.
	 * </p>
	 * @param DOMNode $refnode [optional] <p>
	 * The reference node. If not supplied, newnode is
	 * appended to the children.
	 * </p>
	 * @return DOMNode The inserted node.
         */
	public function insertBefore (DOMNode $newnode , $refnode = null) {}

        /**
	 * (PHP 5)<br/>
	 * Replaces a child
	 * @link http://php.net/manual/en/domnode.replacechild.php
	 * @param DOMNode $newnode <p>
	 * The new node. It must be a member of the target document, i.e.
	 * created by one of the DOMDocument->createXXX() methods or imported in
	 * the document by .
	 * </p>
	 * @param DOMNode $oldnode <p>
	 * The old node.
	 * </p>
	 * @return DOMNode The old node or false if an error occur.
         */
	public function replaceChild (DOMNode $newnode , DOMNode $oldnode ) {}

        /**
	 * (PHP 5)<br/>
	 * Removes child from list of children
	 * @link http://php.net/manual/en/domnode.removechild.php
	 * @param DOMNode $oldnode <p>
	 * The removed child.
	 * </p>
	 * @return DOMNode If the child could be removed the functions returns the old child.
         */
	public function removeChild (DOMNode $oldnode ) {}

        /**
	 * (PHP 5)<br/>
	 * Adds new child at the end of the children
	 * @link http://php.net/manual/en/domnode.appendchild.php
	 * @param DOMNode $newnode <p>
	 * The appended child.
	 * </p>
	 * @return DOMNode The node added.
         */
	public function appendChild (DOMNode $newnode ) {}

	/**
	 * (PHP 5)<br/>
	 * Checks if node has children
	 * @link http://php.net/manual/en/domnode.haschildnodes.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function hasChildNodes () {}

        /**
	 * (PHP 5)<br/>
	 * Clones a node
	 * @link http://php.net/manual/en/domnode.clonenode.php
	 * @param bool $deep [optional] <p>
	 * Indicates whether to copy all descendant nodes. This parameter is 
	 * defaulted to false.
	 * </p>
	 * @return DOMNode The cloned node.
         */
	public function cloneNode ($deep = null) {}

	/**
	 * (PHP 5)<br/>
	 * Normalizes the node
	 * @link http://php.net/manual/en/domnode.normalize.php
	 * @return void 
	 */
        public function normalize () {}

        /**
	 * (PHP 5)<br/>
	 * Checks if feature is supported for specified version
	 * @link http://php.net/manual/en/domnode.issupported.php
	 * @param string $feature <p>
	 * The feature to test. See the example of 
	 * DOMImplementation::hasFeature for a
	 * list of features.
	 * </p>
	 * @param string $version <p>
	 * The version number of the feature to test.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
        public function isSupported ($feature, $version) {}

	/**
	 * (PHP 5)<br/>
	 * Checks if node has attributes
	 * @link http://php.net/manual/en/domnode.hasattributes.php
	 * @return bool Returns true on success or false on failure.
	 */
        public function hasAttributes () {}

        /**
	 * @param DOMNode $other
         */
        public function compareDocumentPosition (DOMNode $other) {}

        /**
	 * (PHP 5)<br/>
	 * Indicates if two nodes are the same node
	 * @link http://php.net/manual/en/domnode.issamenode.php
	 * @param DOMNode $node <p>
	 * The compared node.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
	public function isSameNode (DOMNode $node ) {}

        /**
	 * (PHP 5)<br/>
	 * Gets the namespace prefix of the node based on the namespace URI
	 * @link http://php.net/manual/en/domnode.lookupprefix.php
	 * @param string $namespaceURI <p>
	 * The namespace URI.
	 * </p>
	 * @return string The prefix of the namespace.
         */
        public function lookupPrefix ($namespaceURI) {}

        /**
	 * (PHP 5)<br/>
	 * Checks if the specified namespaceURI is the default namespace or not
	 * @link http://php.net/manual/en/domnode.isdefaultnamespace.php
	 * @param string $namespaceURI <p>
	 * The namespace URI to look for.
	 * </p>
	 * @return bool Return true if namespaceURI is the default
	 * namespace, false otherwise.
         */
        public function isDefaultNamespace ($namespaceURI) {}

        /**
	 * (PHP 5)<br/>
	 * Gets the namespace URI of the node based on the prefix
	 * @link http://php.net/manual/en/domnode.lookupnamespaceuri.php
	 * @param string $prefix <p>
	 * The prefix of the namespace.
	 * </p>
	 * @return string The namespace URI of the node.
         */
        public function lookupNamespaceUri ($prefix) {}

        /**
	 * @param DOMNode $arg
         */
        public function isEqualNode (DOMNode $arg) {}

        /**
	 * @param $feature
	 * @param $version
         */
        public function getFeature ($feature, $version) {}

        /**
	 * @param $key
	 * @param $data
	 * @param $handler
         */
        public function setUserData ($key, $data, $handler) {}

        /**
	 * @param $key
         */
        public function getUserData ($key) {}

        public function getNodePath () {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Get line number for a node
	 * @link http://php.net/manual/en/domnode.getlineno.php
	 * @return int Always returns the line number where the node was defined in.
	 */
        public function getLineNo () {}

        /**
	 * @param $exclusive [optional]
	 * @param $with_comments [optional]
	 * @param $xpath [optional]
	 * @param $ns_prefixes [optional]
         */
        public function C14N ($exclusive, array $with_comments, array $xpath = null, $ns_prefixes = null) {}

        /**
	 * @param $uri
	 * @param $exclusive [optional]
	 * @param $with_comments [optional]
	 * @param $xpath [optional]
	 * @param $ns_prefixes [optional]
         */
        public function C14NFile ($uri, $exclusive, array $with_comments, array $xpath = null, $ns_prefixes = null) {}

}

class DOMStringExtend  {

        /**
	 * @param $offset32
         */
        public function findOffset16 ($offset32) {}

        /**
	 * @param $offset16
         */
        public function findOffset32 ($offset16) {}

}

/**
 * The DOMXPath class (supports XPath 1.0)
 * @link http://php.net/manual/en/class.domxpath.php
 */
class DOMXPath  {



    /**
     * @var DOMDocument
     * (PHP 5)<br/>
     *
     * @link http://php.net/manual/en/class.domxpath.php#domxpath.props.document
     */
    public $document;

        /**
	 * (PHP 5)<br/>
	 * Creates a new <classname>DOMXPath</classname> object
	 * @link http://php.net/manual/en/domxpath.construct.php
	 * @param DOMDocument $doc The <classname>DOMDocument</classname> associated with the <classname>DOMXPath</classname>.
         */
        public function __construct (DOMDocument $doc) {}

        /**
	 * (PHP 5)<br/>
	 * Registers the namespace with the <classname>DOMXPath</classname> object
	 * @link http://php.net/manual/en/domxpath.registernamespace.php
	 * @param string $prefix <p>
	 * The prefix.
	 * </p>
	 * @param string $namespaceURI <p>
	 * The URI of the namespace.
	 * </p>
	 * @return bool Returns true on success or false on failure.
         */
	public function registerNamespace ($prefix, $namespaceURI) {}

        /**
	 * (PHP 5)<br/>
	 * Evaluates the given XPath expression
	 * @link http://php.net/manual/en/domxpath.query.php
	 * @param string $expression <p>
	 * The XPath expression to execute.
	 * </p>
	 * @param DOMNode $contextnode [optional] <p>
	 * The optional contextnode can be specified for
	 * doing relative XPath queries. By default, the queries are relative to 
	 * the root element.
	 * </p>
	 * @return DOMNodeList a DOMNodeList containing all nodes matching 
	 * the given XPath expression. Any expression which do
	 * not return nodes will return an empty DOMNodeList.
         */
	public function query ($expression, $contextnode = null) {}

        /**
	 * (PHP 5 &gt;= 5.1.0)<br/>
	 * Evaluates the given XPath expression and returns a typed result if possible.
	 * @link http://php.net/manual/en/domxpath.evaluate.php
	 * @param string $expression <p>
	 * The XPath expression to execute.
	 * </p>
	 * @param DOMNode $contextnode [optional] <p>
	 * The optional contextnode can be specified for
	 * doing relative XPath queries. By default, the queries are relative to 
	 * the root element.
	 * </p>
	 * @return mixed a typed result if possible or a DOMNodeList 
	 * containing all nodes matching the given XPath expression.
         */
	public function evaluate ($expression, $contextnode = null) {}

	/**
	 * (PHP 5 &gt;= 5.3.0)<br/>
	 * Register PHP functions as XPath functions
	 * @link http://php.net/manual/en/domxpath.registerphpfunctions.php
	 * @param mixed $restrict [optional] <p>
	 * Use this parameter to only allow certain functions to be called from XPath.
	 * </p>
	 * <p>
	 * This parameter can be either a string (a function name) or 
	 * an array of function names.
	 * </p>
	 * @return void 
	 */
	public function registerPhpFunctions ($restrict = null) {}

}

// End of dom v.20031129
?>