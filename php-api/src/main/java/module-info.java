/**
 * @author VISTALL
 * @since 30-Aug-22
 */
module consulo.php.api
{
	requires transitive consulo.language.api;
	requires transitive consulo.project.api;
	requires transitive consulo.module.api;
	requires transitive consulo.navigation.api;
	requires transitive consulo.application.content.api;
	requires transitive consulo.process.api;
	requires transitive consulo.document.api;
	requires transitive consulo.ui.api;

	requires consulo.application.api;
	requires consulo.component.api;
	requires consulo.container.api;
	requires consulo.index.io;
	requires consulo.platform.api;
	requires consulo.util.collection;
	requires consulo.util.lang;
	requires consulo.virtual.file.system.api;

	exports com.jetbrains.php;
	exports com.jetbrains.php.codeInsight;
	exports com.jetbrains.php.lang;
	exports com.jetbrains.php.lang.documentation.phpdoc;
	exports com.jetbrains.php.lang.documentation.phpdoc.lexer;
	exports com.jetbrains.php.lang.documentation.phpdoc.parser;
	exports com.jetbrains.php.lang.documentation.phpdoc.psi;
	exports com.jetbrains.php.lang.documentation.phpdoc.psi.tags;
	exports com.jetbrains.php.lang.lexer;
	exports com.jetbrains.php.lang.parser;
	exports com.jetbrains.php.lang.patterns;
	exports com.jetbrains.php.lang.psi;
	exports com.jetbrains.php.lang.psi.elements;
	exports com.jetbrains.php.lang.psi.resolve.types;
	exports com.jetbrains.php.refactoring;

	exports com.jetbrains.php.config;
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
	exports com.jetbrains.php.lang.psi.stubs.indexes.expectedArguments;
}