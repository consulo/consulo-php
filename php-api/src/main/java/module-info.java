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
	requires transitive consulo.language.editor.api;
	requires transitive consulo.code.editor.api;
	requires transitive consulo.color.scheme.api;

	requires consulo.application.api;
	requires consulo.language.editor.ui.api;
	requires consulo.component.api;
	requires consulo.container.api;
	requires consulo.index.io;
	requires consulo.platform.api;
	requires consulo.util.collection;
	requires consulo.util.lang;
	requires consulo.virtual.file.system.api;

	exports com.jetbrains.php;
	exports com.jetbrains.php.codeInsight;
	exports com.jetbrains.php.codeInsight.controlFlow;
	exports com.jetbrains.php.codeInsight.controlFlow.instructions;
	exports com.jetbrains.php.lang;
	exports com.jetbrains.php.completion;
	exports com.jetbrains.php.completion.insert;
	exports com.jetbrains.php.lang.highlighter;
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
	exports com.jetbrains.php.lang.psi.resolve.types.shapes;
	exports com.jetbrains.php.refactoring;
	exports consulo.php.composer.importer;
	exports com.jetbrains.php.refactoring.changeSignature;
	exports com.jetbrains.php.refactoring.introduce.introduceField;
	exports com.jetbrains.php.util;
	exports com.jetbrains.php.phpunit;
	exports com.jetbrains.php.lang.documentation;

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
	exports com.jetbrains.php.lang.psi.stubs.indexes;
	exports com.jetbrains.php.lang.psi.stubs.indexes.expectedArguments;
}