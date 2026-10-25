/**
 * @author VISTALL
 * @since 30-Aug-22
 */
open module consulo.php {
    requires consulo.php.api;
    requires consulo.php.xdebug.impl;

    requires consulo.ide.api;
    requires consulo.ide.impl;

    requires consulo.language.impl;
    requires consulo.language.editor.api;
    requires consulo.language.editor.impl;
    requires consulo.language.editor.ui.api;
    requires consulo.language.editor.refactoring.api;
    requires consulo.language.inject.impl;

    requires consulo.code.editor.api;
    requires consulo.color.scheme.api;
    requires consulo.configurable.api;
    requires consulo.datacontext.api;
    requires consulo.disposer.api;
    requires consulo.document.api;
    requires consulo.file.editor.api;
    requires consulo.file.chooser.api;
    requires consulo.file.template.api;

    requires consulo.execution.api;
    requires consulo.execution.impl;
    requires consulo.execution.debug.api;

    requires consulo.compiler.api;

    requires consulo.module.api;
    requires consulo.module.content.api;
    requires consulo.module.ui.api;

    requires consulo.project.api;
    requires consulo.project.ui.api;
    requires consulo.project.ui.view.api;

    requires consulo.ui.api;
    requires consulo.ui.ex.api;

    requires org.apache.commons.compress;

    requires com.intellij.xml.api;
    requires com.intellij.xml.html.api;
    requires com.intellij.xml;

    // TODO remove this dependency in future
    requires java.desktop;

    exports com.jetbrains.php.lang.psi.elements.impl;
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
    exports consulo.php.impl.run.debug;
    exports consulo.php.impl.run.script;
    exports consulo.php.impl.vfs;
    exports org.eclipse.php.internal.core.phar;
    exports org.eclipse.php.internal.core.phar.streams;
}