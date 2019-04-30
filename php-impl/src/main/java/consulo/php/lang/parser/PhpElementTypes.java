package consulo.php.lang.parser;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.jetbrains.php.lang.PhpLanguage;
import consulo.php.lang.psi.PhpElementType;
import consulo.php.lang.psi.PhpStubElements;
import consulo.php.lang.psi.impl.*;
import consulo.psi.tree.ElementTypeAsPsiFactory;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 26.02.2007
 *
 * @author jay
 */
public interface PhpElementTypes
{
	IElementType EMPTY_INPUT = new PhpElementType("Unrecognised input");

	IElementType HTML = new PhpElementType("HTML");

	IElementType GROUP_STATEMENT = new ElementTypeAsPsiFactory("GROUP_STATEMENT", PhpLanguage.INSTANCE, PhpGroupStatementImpl.class);
	IElementType STATEMENT = new ElementTypeAsPsiFactory("STATEMENT", PhpLanguage.INSTANCE, PhpStatementImpl.class);
	IElementType PARAMETER = PhpStubElements.PARAMETER;
	IElementType MODIFIER_LIST = new ElementTypeAsPsiFactory("MODIFIER_LIST", PhpLanguage.INSTANCE, PhpModifierListImpl.class);
	IElementType NAMESPACE = PhpStubElements.NAMESPACE;
	IElementType DEFINE = PhpStubElements.DEFINE;
	IElementType USE_LIST = new ElementTypeAsPsiFactory("USE_LIST_STATEMENT", PhpLanguage.INSTANCE, PhpUseListStatementImpl.class);
	IElementType USE = new ElementTypeAsPsiFactory("USE", PhpLanguage.INSTANCE, PhpUseImpl.class);
	IElementType RETURN_TYPE = new ElementTypeAsPsiFactory("RETURN_TYPE", PhpLanguage.INSTANCE, PhpReturnTypeImpl.class);
	IElementType ANONYMOUS_CLASS = new ElementTypeAsPsiFactory("ANONYMOUS_CLASS", PhpLanguage.INSTANCE, PhpClassImpl.class);

	IElementType EXPRESSION_PARAMETER_LIST = new ElementTypeAsPsiFactory("EXPRESSION_PARAMETER_LIST", PhpLanguage.INSTANCE, PhpExpressionParameterListImpl.class);
	IElementType FUNCTION_PARAMETER_LIST = new ElementTypeAsPsiFactory("FUNCTION_PARAMETER_LIST", PhpLanguage.INSTANCE, PhpFunctionParameterListImpl.class);

	IElementType UNARY_EXPRESSION = new ElementTypeAsPsiFactory("UNARY_EXPRESSION", PhpLanguage.INSTANCE, PhpUnaryExpressionImpl.class);
	IElementType ASSIGNMENT_EXPRESSION = new ElementTypeAsPsiFactory("ASSIGNMENT_EXPRESSION", PhpLanguage.INSTANCE, PhpAssignmentExpressionImpl.class);
	IElementType SELF_ASSIGNMENT_EXPRESSION = new ElementTypeAsPsiFactory("SELF_ASSIGNMENT_EXPRESSION", PhpLanguage.INSTANCE, PhpSelfAssignmentExpressionImpl.class);
	IElementType TERNARY_EXPRESSION = new ElementTypeAsPsiFactory("TERNARY_EXPRESSION", PhpLanguage.INSTANCE, PhpTernaryExpressionImpl.class);
	IElementType ELVIS_EXPRESSION = new ElementTypeAsPsiFactory("ELVIS_EXPRESSION", PhpLanguage.INSTANCE, PhpElvisExpressionImpl.class);
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
	IElementType TYPE_CAST_EXPRESSION = new PhpElementType("TYPE_CAST_EXPRESSION");
	IElementType SILENCE_EXPRESSION = new PhpElementType("Silence expression");
	IElementType POSTFIX_EXPRESSION = new PhpElementType("Postfix expression");
	IElementType ARRAY_CREATION_EXPRESSION = new ElementTypeAsPsiFactory("ARRAY_CREATION_EXPRESSION", PhpLanguage.INSTANCE, PhpArrayCreationExpressionImpl.class);
	IElementType ARRAY_ACCESS_EXPRESSION = new ElementTypeAsPsiFactory("ARRAY_ACCESS_EXPRESSION", PhpLanguage.INSTANCE, PhpArrayAccessExpressionImpl.class);
	IElementType ARRAY_HASH = new ElementTypeAsPsiFactory("ARRAY_HASH", PhpLanguage.INSTANCE, PhpArrayHashElementImpl.class);
	IElementType YIELD = new ElementTypeAsPsiFactory("YIELD", PhpLanguage.INSTANCE, PhpYieldImpl.class);

	TokenSet BINARY_EXPRESSIONS = TokenSet.create(ADDITIVE_EXPRESSION, MULTIPLICATIVE_EXPRESSION, SHIFT_EXPRESSION, RELATIONAL_EXPRESSION, EQUALITY_EXPRESSION, BIT_EXPRESSION, LOGICAL_EXPRESSION, LITERAL_LOGICAL_EXPRESSION, INSTANCEOF_EXPRESSION);

	IElementType IF = new PhpElementType("If");
	IElementType STRING = new ElementTypeAsPsiFactory("STRING_EXPRESSION", PhpLanguage.INSTANCE, PhpStringLiteralExpressionImpl.class);
	IElementType CONSTANT = new PhpElementType("Constant");
	IElementType CLASS_REFERENCE = new ElementTypeAsPsiFactory("CLASS_REFERENCE", PhpLanguage.INSTANCE, PhpClassReferenceImpl.class);
	IElementType VARIABLE_REFERENCE = new ElementTypeAsPsiFactory("VARIABLE_REFERENCE", PhpLanguage.INSTANCE, PhpVariableReferenceImpl.class);
	IElementType ARRAY_INDEX = new PhpElementType("Array index");
	IElementType CLASS_CONSTANT_REFERENCE = new ElementTypeAsPsiFactory("CLASS_CONSTANT_REFERENCE", PhpLanguage.INSTANCE, PhpClassConstantReferenceImpl.class);
	IElementType FINALLY_STATEMENT = new PhpElementType("FINALLY_STATEMENT");
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
	IElementType EXTENDS_LIST = new PhpElementType("Extends list");

	IElementType IMPLEMENTS_LIST = new PhpElementType("Implements list");
	IElementType CLASS_CONSTANT = PhpStubElements.CLASS_FIELD;

	IElementType CLASS = PhpStubElements.CLASS;
	IElementType FUNCTION = PhpStubElements.FUNCTION;
	IElementType ANONYMOUS_FUNCTION = new ElementTypeAsPsiFactory("PHP_ANONYMOUS_FUNCTION", PhpLanguage.INSTANCE, PhpFunctionImpl.class);
	IElementType CLASS_FIELD = PhpStubElements.CLASS_FIELD;
	IElementType SWITCH = new PhpElementType("Switch statement");
	IElementType CASE_DEFAULT = new PhpElementType("Default case");
	IElementType CASE = new PhpElementType("Case");
	IElementType RETURN = new ElementTypeAsPsiFactory("RETURN_STATEMENT", PhpLanguage.INSTANCE, PhpReturnStatemetImpl.class);
	IElementType STATIC = new PhpElementType("Static statement");
	IElementType DECLARE = new PhpElementType("Declare statement");
	IElementType DECLARE_DIRECTIVE = new PhpElementType("Declare directive");
	IElementType TRY = new PhpElementType("Try statement");
	IElementType CATCH = new PhpElementType("Catch clause");
	IElementType THROW = new PhpElementType("Throw statement");
	IElementType EXPRESSION = new PhpElementType("Expression");
	IElementType OBSCURE_VARIABLE = new PhpElementType("Obscure variable");
	IElementType FUNCTION_REFERENCE = new ElementTypeAsPsiFactory("FUNCTION_REFERENCE", PhpLanguage.INSTANCE, PhpFunctionReferenceImpl.class);
	IElementType FIELD_REFERENCE = new PhpElementType("PhpField reference");
	IElementType METHOD_REFERENCE = new ElementTypeAsPsiFactory("METHOD_REFERENCE", PhpLanguage.INSTANCE, PhpMethodReferenceImpl.class);
	IElementType HEREDOC = new PhpElementType("Heredoc");
	IElementType VARIABLE_NAME = new PhpElementType("PhpVariableReference name");
	IElementType NUMBER = new PhpElementType("Number");
	IElementType SHELL_COMMAND = new PhpElementType("Shell command");

	IElementType PHP_OUTER_TYPE = new PhpElementType("PHP outer type in html");
}
