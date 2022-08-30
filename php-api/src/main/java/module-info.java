/**
 * @author VISTALL
 * @since 30-Aug-22
 */
module consulo.php.api
{
	requires transitive consulo.ide.api;

	exports com.jetbrains.php;
	exports com.jetbrains.php.codeInsight;
	exports com.jetbrains.php.lang;
	exports com.jetbrains.php.lang.documentation.phpdoc.psi;
	exports com.jetbrains.php.lang.lexer;
	exports com.jetbrains.php.lang.patterns;
	exports com.jetbrains.php.lang.psi;
	exports com.jetbrains.php.lang.psi.elements;
	exports com.jetbrains.php.lang.psi.resolve.types;

	exports consulo.php;
	exports consulo.php.icon;
	exports consulo.php.lang.documentation.phpdoc.psi;
	exports consulo.php.lang.documentation.phpdoc.psi.tags;
	exports consulo.php.lang.lexer;
	exports consulo.php.lang.psi;
	exports consulo.php.localize;
	exports consulo.php.module.extension;
	exports consulo.php.module.util;
	exports consulo.php.sdk;
	exports com.jetbrains.php.lang.psi.stubs;
}