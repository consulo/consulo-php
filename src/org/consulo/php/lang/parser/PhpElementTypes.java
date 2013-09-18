package org.consulo.php.lang.parser;

import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IElementTypeAsPsiFactory;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.consulo.php.lang.PhpLanguage;
import org.consulo.php.lang.psi.PhpElementType;
import org.consulo.php.lang.psi.PhpStubElements;
import org.consulo.php.lang.psi.impl.*;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 26.02.2007
 *
 * @author jay
 */
public interface PhpElementTypes extends PhpStubElements
{

	final IFileElementType FILE = new IFileElementType(Language.findInstance(PhpLanguage.class));
	IElementType EMPTY_INPUT = new PhpElementType("Unrecognised input");

	IElementType HTML = new PhpElementType("HTML");

	IElementType GROUP_STATEMENT = new IElementTypeAsPsiFactory("GROUP_STATEMENT", PhpLanguage.INSTANCE, PhpGroupStatementImpl.class);
	IElementType STATEMENT = new IElementTypeAsPsiFactory("STATEMENT", PhpLanguage.INSTANCE, PhpStatementImpl.class);
	IElementType FUNCTION = new IElementTypeAsPsiFactory("FUNCTION", PhpLanguage.INSTANCE, PhpFunctionImpl.class);
	IElementType PARAMETER_LIST = new IElementTypeAsPsiFactory("PARAMETER_LIST", PhpLanguage.INSTANCE, PhpParameterListImpl.class);
	IElementType PARAMETER = new IElementTypeAsPsiFactory("PARAMETER", PhpLanguage.INSTANCE, PhpParameterImpl.class);

	IElementType UNARY_EXPRESSION = new IElementTypeAsPsiFactory("UNARY_EXPRESSION", PhpLanguage.INSTANCE, PhpUnaryExpressionImpl.class);
	IElementType ASSIGNMENT_EXPRESSION = new IElementTypeAsPsiFactory("ASSIGNMENT_EXPRESSION", PhpLanguage.INSTANCE, PhpAssignmentExpressionImpl.class);
	IElementType SELF_ASSIGNMENT_EXPRESSION = new IElementTypeAsPsiFactory("SELF_ASSIGNMENT_EXPRESSION", PhpLanguage.INSTANCE, PhpSelfAssignmentExpressionImpl.class);
	IElementType TERNARY_EXPRESSION = new PhpElementType("TERNARY_EXPRESSION");
	IElementType INSTANCEOF_EXPRESSION = new PhpElementType("Instanceof expression");
	IElementType PRINT_EXPRESSION = new PhpElementType("Print expression");
	IElementType EXIT_EXPRESSION = new PhpElementType("Exit expression");
	IElementType ISSET_EXPRESSION = new PhpElementType("Isset function");
	IElementType NEW_EXPRESSION = new PhpElementType("New expression");
	IElementType INCLUDE_EXPRESSION = new PhpElementType("Include expression");
	IElementType EMPTY_EXPRESSION = new PhpElementType("Empty expression");
	IElementType EVAL_EXPRESSION = new PhpElementType("Eval expression");
	IElementType CLONE_EXPRESSION = new PhpElementType("Clone expression");
	IElementType MULTIASSIGNMENT_EXPRESSION = new PhpElementType("Multiassignment expression");
	IElementType LITERAL_LOGICAL_EXPRESSION = new PhpElementType("Literal logical expression");
	IElementType LOGICAL_EXPRESSION = new PhpElementType("Logical expression");
	IElementType BIT_EXPRESSION = new PhpElementType("Bint expression");
	IElementType EQUALITY_EXPRESSION = new PhpElementType("Equality expression");
	IElementType RELATIONAL_EXPRESSION = new PhpElementType("Relational expression");
	IElementType SHIFT_EXPRESSION = new PhpElementType("Shift expression");
	IElementType ADDITIVE_EXPRESSION = new PhpElementType("Additive expression");
	IElementType MULTIPLICATIVE_EXPRESSION = new PhpElementType("Multiplicative expression");
	IElementType CAST_EXPRESSION = new PhpElementType("Cast expression");
	IElementType SILENCE_EXPRESSION = new PhpElementType("Silence expression");
	IElementType POSTFIX_EXPRESSION = new PhpElementType("Postfix expression");

	TokenSet BINARY_EXPRESSIONS = TokenSet.create(ADDITIVE_EXPRESSION, MULTIPLICATIVE_EXPRESSION, SHIFT_EXPRESSION, RELATIONAL_EXPRESSION, EQUALITY_EXPRESSION, BIT_EXPRESSION, LOGICAL_EXPRESSION, LITERAL_LOGICAL_EXPRESSION, INSTANCEOF_EXPRESSION);

	IElementType IF = new PhpElementType("If");
	IElementType STRING = new PhpElementType("String");
	IElementType CONSTANT = new PhpElementType("Constant");
	IElementType CLASS_REFERENCE = new IElementTypeAsPsiFactory("CLASS_REFERENCE", PhpLanguage.INSTANCE, PhpClassReferenceImpl.class);
	IElementType VARIABLE_REFERENCE = new IElementTypeAsPsiFactory("VARIABLE_REFERENCE", PhpLanguage.INSTANCE, PhpVariableReferenceImpl.class);
	IElementType ARRAY_INDEX = new PhpElementType("Array index");
	IElementType CLASS_CONSTANT_REFERENCE = new PhpElementType("Class constant reference");
	IElementType ELSE_IF = new PhpElementType("Elseif");
	IElementType ELSE = new PhpElementType("Else");
	IElementType ARRAY = new PhpElementType("Array");
	IElementType ARRAY_KEY = new PhpElementType("Array key");
	IElementType ARRAY_VALUE = new PhpElementType("Array value");
	IElementType FOR = new PhpElementType("For");
	IElementType FOREACH = new PhpElementType("Foreach");
	IElementType WHILE = new PhpElementType("While");
	IElementType DO_WHILE = new PhpElementType("Do while");
	IElementType BREAK = new PhpElementType("Break");
	IElementType CONTINUE = new PhpElementType("Continue");
	IElementType ECHO = new PhpElementType("Echo");
	IElementType GLOBAL = new PhpElementType("Global");
	IElementType UNSET = new PhpElementType("Unset");


	IElementType IS_REFERENCE = new PhpElementType("Is reference");
	IElementType PARAMETER_DEFAULT_VALUE = new PhpElementType("PhpParameter default value");
	IElementType COMMON_SCALAR = new PhpElementType("Common scalar");
	IElementType STATIC_SCALAR = new PhpElementType("Static scalar");
	IElementType EXTENDS_LIST = new PhpElementType("Extends list");
	IElementType INTERFACE = new PhpElementType("Interface");
	IElementType IMPLEMENTS_LIST = new PhpElementType("Implements list");
	IElementType CLASS_CONSTANT = new PhpElementType("Class constant");
	IElementType MODIFIER_LIST = new PhpElementType("Modifier list");
	IElementType CLASS_FIELD = new IElementTypeAsPsiFactory("FIELD", PhpLanguage.INSTANCE, PhpFieldImpl.class);
	IElementType CLASS_METHOD = new PhpElementType("Class method");
	IElementType SWITCH = new PhpElementType("Switch statement");
	IElementType CASE_DEFAULT = new PhpElementType("Default case");
	IElementType CASE = new PhpElementType("Case");
	IElementType RETURN = new PhpElementType("Return");
	IElementType STATIC = new PhpElementType("Static statement");
	IElementType DECLARE = new PhpElementType("Declare statement");
	IElementType DECLARE_DIRECTIVE = new PhpElementType("Declare directive");
	IElementType TRY = new PhpElementType("Try statement");
	IElementType CATCH = new PhpElementType("Catch clause");
	IElementType THROW = new PhpElementType("Throw statement");
	IElementType EXPRESSION = new PhpElementType("Expression");
	IElementType OBSCURE_VARIABLE = new PhpElementType("Obscure variable");
	IElementType FUNCTION_CALL = new PhpElementType("Function call");
	IElementType FIELD_REFERENCE = new PhpElementType("PhpField reference");
	IElementType METHOD_REFERENCE = new IElementTypeAsPsiFactory("METHOD_REFERENCE", PhpLanguage.INSTANCE, PhpMethodReferenceImpl.class);
	IElementType HEREDOC = new PhpElementType("Heredoc");
	IElementType VARIABLE_NAME = new PhpElementType("PhpVariableReference name");
	IElementType NUMBER = new PhpElementType("Number");
	IElementType SHELL_COMMAND = new PhpElementType("Shell command");

	IElementType PHP_OUTER_TYPE = new PhpElementType("PHP outer type in html");
}
