package net.jay.plugins.php.lang.parser;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import net.jay.plugins.php.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import net.jay.plugins.php.lang.documentation.phpdoc.psi.PhpDocElementType;
import net.jay.plugins.php.lang.documentation.phpdoc.psi.PhpDocPsiCreator;
import net.jay.plugins.php.lang.documentation.phpdoc.psi.impl.PhpDocCommentImpl;
import net.jay.plugins.php.lang.psi.elements.impl.*;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 30.03.2007
 *
 * @author jay
 */
public class PHPPsiCreator implements PHPElementTypes {

  public static PsiElement create(ASTNode node) {
    IElementType type = node.getElementType();

    if (type instanceof PhpDocElementType) {
      return PhpDocPsiCreator.createElement(node);
    }
    if (type == PhpDocElementTypes.DOC_COMMENT) {
      return new PhpDocCommentImpl();
    }

    if (type == VARIABLE) {
      return new VariableImpl(node);
    }
    if (type == STATEMENT) {
      return new StatementImpl(node);
    }
    if (type == GROUP_STATEMENT) {
      return new GroupStatementImpl(node);
    }
    if (type == FUNCTION) {
      return new FunctionImpl(node);
    }
    if (type == PARAMETER_LIST) {
      return new ParameterListImpl(node);
    }
    if (type == PARAMETER) {
      return new ParameterImpl(node);
    }
    if (type == NEW_EXPRESSION) {
      return new NewExpressionImpl(node);
    }
    if (BINARY_EXPRESSIONS.contains(type)) {
      return new BinaryExpressionImpl(node);
    }
    if (type == UNARY_EXPRESSION) {
      return new UnaryExpressionImpl(node);
    }
    if (type == ASSIGNMENT_EXPRESSION) {
      return new AssignmentExpressionImpl(node);
    }
    if (type == SELF_ASSIGNMENT_EXPRESSION) {
      return new SelfAssignmentExpressionImpl(node);
    }
    if (type == CLASS_METHOD) {
      return new MethodImpl(node);
    }
    if (type == CLASS) {
      return new PhpClassImpl(node);
    }
    if (type == INTERFACE) {
      return new PhpInterfaceImpl(node);
    }
    if (type == CATCH) {
      return new CatchImpl(node);
    }
    if (type == FOREACH) {
      return new ForeachImpl(node);
    }
    if (type == GLOBAL) {
      return new GlobalImpl(node);
    }
    if (type == CLASS_FIELD) {
      return new FieldImpl(node);
    }
    if (type == IF) {
      return new IfImpl(node);
    }
    if (type == ELSE_IF) {
      return new ElseIfImpl(node);
    }
    if (type == ELSE) {
      return new ElseImpl(node);
    }
    if (type == FOR) {
      return new ForImpl(node);
    }
    if (type == CLASS_REFERENCE) {
      return new ClassReferenceImpl(node);
    }
    if (type == FIELD_REFERENCE) {
      return new FieldReferenceImpl(node);
    }
    if (type == FUNCTION_CALL) {
      return new FunctionCallImpl(node);
    }
    if (type == METHOD_REFERENCE) {
      return new MethodReferenceImpl(node);
    }
    if (type == EXTENDS_LIST) {
      return new ExtendsListImpl(node);
    }
    if (type == IMPLEMENTS_LIST) {
      return new ImplementsListImpl(node);
    }
    if (type == CLASS_CONSTANT_REFERENCE) {
      return new ClassConstantReferenceImpl(node);
    }
    if (type == CONSTANT) {
      return new ConstantReferenceImpl(node);
    }
    if (type == TRY) {
      return new TryImpl(node);
    }
    return new PHPPsiElementImpl(node);
  }
}
