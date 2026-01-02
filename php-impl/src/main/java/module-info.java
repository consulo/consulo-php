/**
 * @author VISTALL
 * @since 30-Aug-22
 */
open module consulo.php {
    requires consulo.php.api;

    requires org.apache.commons.compress;

    requires com.intellij.xml;

    // TODO remove this dependency in future
    requires java.desktop;

    exports consulo.php.impl;
    exports consulo.php.impl.actions;
    exports consulo.php.impl.actions.generate;
    exports consulo.php.impl.codeInsight.highlight;
    exports consulo.php.impl.completion;
    exports consulo.php.impl.completion.insert;
    exports consulo.php.impl.completion.keywords;
    exports consulo.php.impl.gotoByName;
    exports consulo.php.impl.ide.folding;
    exports consulo.php.impl.ide.presentation;
    exports consulo.php.impl.ide.projectView;
    exports consulo.php.impl.index;
    exports consulo.php.impl.lang;
    exports consulo.php.impl.lang.braceMatcher;
    exports consulo.php.impl.lang.commenter;
    exports consulo.php.impl.lang.documentation.params;
    exports consulo.php.impl.lang.documentation.phpdoc.lexer;
    exports consulo.php.impl.lang.documentation.phpdoc.parser;
    exports consulo.php.impl.lang.documentation.phpdoc.parser.tags;
    exports consulo.php.impl.lang.documentation.phpdoc.psi;
    exports consulo.php.impl.lang.documentation.phpdoc.psi.impl;
    exports consulo.php.impl.lang.documentation.phpdoc.psi.impl.tags;
    exports consulo.php.impl.lang.findUsages;
    exports consulo.php.impl.lang.highlighter;
    exports consulo.php.impl.lang.highlighter.hierarchy;
    exports consulo.php.impl.lang.inspections;
    exports consulo.php.impl.lang.inspections.classes;
    exports consulo.php.impl.lang.lexer;
    exports consulo.php.impl.lang.lexer.managers;
    exports consulo.php.impl.lang.parser;
    exports consulo.php.impl.lang.parser.parsing;
    exports consulo.php.impl.lang.parser.parsing.calls;
    exports consulo.php.impl.lang.parser.parsing.classes;
    exports consulo.php.impl.lang.parser.parsing.expressions;
    exports consulo.php.impl.lang.parser.parsing.expressions.bit;
    exports consulo.php.impl.lang.parser.parsing.expressions.comparition;
    exports consulo.php.impl.lang.parser.parsing.expressions.logical;
    exports consulo.php.impl.lang.parser.parsing.expressions.math;
    exports consulo.php.impl.lang.parser.parsing.expressions.primary;
    exports consulo.php.impl.lang.parser.parsing.functions;
    exports consulo.php.impl.lang.parser.parsing.statements;
    exports consulo.php.impl.lang.parser.util;
    exports consulo.php.impl.lang.psi;
    exports consulo.php.impl.lang.psi.impl;
    exports consulo.php.impl.lang.psi.impl.light;
    exports consulo.php.impl.lang.psi.impl.stub;
    exports consulo.php.impl.lang.psi.impl.stub.elements;
    exports consulo.php.impl.lang.psi.resolve;
    exports consulo.php.impl.lang.psi.search;
    exports consulo.php.impl.lang.psi.util;
    exports consulo.php.impl.lang.psi.visitors;
    exports consulo.php.impl.library;
    exports consulo.php.impl.module;
    exports consulo.php.impl.module.packageSupport;
    exports consulo.php.impl.refactoring;
    exports consulo.php.impl.run.script;
    exports consulo.php.impl.vfs;
    exports org.eclipse.php.internal.core.phar;
    exports org.eclipse.php.internal.core.phar.streams;
}