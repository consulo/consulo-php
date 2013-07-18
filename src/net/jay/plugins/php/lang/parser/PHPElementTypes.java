package net.jay.plugins.php.lang.parser;

import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import net.jay.plugins.php.lang.PHPLanguage;
import net.jay.plugins.php.lang.psi.PHPElementType;
import net.jay.plugins.php.lang.psi.elements.impl.PhpFieldImpl;
import net.jay.plugins.php.lang.psi.elements.impl.PhpMethodReferenceImpl;
import net.jay.plugins.php.lang.psi.elements.impl.PhpVariableReferenceImpl;
import org.consulo.php.psi.PhpInstancableTokenType;
import org.consulo.php.psi.PhpStubElements;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 26.02.2007
 *
 * @author jay
 */
public interface PHPElementTypes extends PhpStubElements
{

	final IFileElementType FILE = new IFileElementType(Language.findInstance(PHPLanguage.class));
	IElementType EMPTY_INPUT = new PHPElementType("Unrecognised input");

	IElementType HTML = new PHPElementType("HTML");

	IElementType GROUP_STATEMENT = new PHPElementType("Group statement");
	IElementType STATEMENT = new PHPElementType("Statement");
	IElementType FUNCTION = new PHPElementType("Function");
	IElementType PARAMETER_LIST = new PHPElementType("PhpParameter list");
	IElementType PARAMETER = new PHPElementType("PhpParameter");

	IElementType UNARY_EXPRESSION = new PHPElementType("Unary expression");
	IElementType ASSIGNMENT_EXPRESSION = new PHPElementType("Assignment expression");
	IElementType SELF_ASSIGNMENT_EXPRESSION = new PHPElementType("Self assignment expression");
	IElementType TERNARY_EXPRESSION = new PHPElementType("Ternary expression");
	IElementType INSTANCEOF_EXPRESSION = new PHPElementType("Instanceof expression");
	IElementType PRINT_EXPRESSION = new PHPElementType("Print expression");
	IElementType EXIT_EXPRESSION = new PHPElementType("Exit expression");
	IElementType ISSET_EXPRESSION = new PHPElementType("Isset function");
	IElementType NEW_EXPRESSION = new PHPElementType("New expression");
	IElementType INCLUDE_EXPRESSION = new PHPElementType("Include expression");
	IElementType EMPTY_EXPRESSION = new PHPElementType("Empty expression");
	IElementType EVAL_EXPRESSION = new PHPElementType("Eval expression");
	IElementType CLONE_EXPRESSION = new PHPElementType("Clone expression");
	IElementType MULTIASSIGNMENT_EXPRESSION = new PHPElementType("Multiassignment expression");
	IElementType LITERAL_LOGICAL_EXPRESSION = new PHPElementType("Literal logical expression");
	IElementType LOGICAL_EXPRESSION = new PHPElementType("Logical expression");
	IElementType BIT_EXPRESSION = new PHPElementType("Bint expression");
	IElementType EQUALITY_EXPRESSION = new PHPElementType("Equality expression");
	IElementType RELATIONAL_EXPRESSION = new PHPElementType("Relational expression");
	IElementType SHIFT_EXPRESSION = new PHPElementType("Shift expression");
	IElementType ADDITIVE_EXPRESSION = new PHPElementType("Additive expression");
	IElementType MULTIPLICATIVE_EXPRESSION = new PHPElementType("Multiplicative expression");
	IElementType CAST_EXPRESSION = new PHPElementType("Cast expression");
	IElementType SILENCE_EXPRESSION = new PHPElementType("Silence expression");
	IElementType POSTFIX_EXPRESSION = new PHPElementType("Postfix expression");

	TokenSet BINARY_EXPRESSIONS = TokenSet.create(ADDITIVE_EXPRESSION, MULTIPLICATIVE_EXPRESSION, SHIFT_EXPRESSION, RELATIONAL_EXPRESSION, EQUALITY_EXPRESSION, BIT_EXPRESSION, LOGICAL_EXPRESSION, LITERAL_LOGICAL_EXPRESSION, INSTANCEOF_EXPRESSION);

	IElementType IF = new PHPElementType("If");
	IElementType STRING = new PHPElementType("String");
	IElementType CONSTANT = new PHPElementType("Constant");
	IElementType CLASS_REFERENCE = new PHPElementType("Class reference");
	IElementType VARIABLE_REFERENCE = new PhpInstancableTokenType("VARIABLE_REFERENCE", PhpVariableReferenceImpl.class);
	IElementType ARRAY_INDEX = new PHPElementType("Array index");
	IElementType CLASS_CONSTANT_REFERENCE = new PHPElementType("Class constant reference");
	IElementType ELSE_IF = new PHPElementType("Elseif");
	IElementType ELSE = new PHPElementType("Else");
	IElementType ARRAY = new PHPElementType("Array");
	IElementType ARRAY_KEY = new PHPElementType("Array key");
	IElementType ARRAY_VALUE = new PHPElementType("Array value");
	IElementType FOR = new PHPElementType("For");
	IElementType FOREACH = new PHPElementType("Foreach");
	IElementType WHILE = new PHPElementType("While");
	IElementType DO_WHILE = new PHPElementType("Do while");
	IElementType BREAK = new PHPElementType("Break");
	IElementType CONTINUE = new PHPElementType("Continue");
	IElementType ECHO = new PHPElementType("Echo");
	IElementType GLOBAL = new PHPElementType("Global");
	IElementType UNSET = new PHPElementType("Unset");


	IElementType IS_REFERENCE = new PHPElementType("Is reference");
	IElementType PARAMETER_DEFAULT_VALUE = new PHPElementType("PhpParameter default value");
	IElementType COMMON_SCALAR = new PHPElementType("Common scalar");
	IElementType STATIC_SCALAR = new PHPElementType("Static scalar");
	IElementType EXTENDS_LIST = new PHPElementType("Extends list");
	IElementType INTERFACE = new PHPElementType("Interface");
	IElementType IMPLEMENTS_LIST = new PHPElementType("Implements list");
	IElementType CLASS_CONSTANT = new PHPElementType("Class constant");
	IElementType MODIFIER_LIST = new PHPElementType("Modifier list");
	IElementType CLASS_FIELD = new PhpInstancableTokenType("FIELD", PhpFieldImpl.class);
	IElementType CLASS_METHOD = new PHPElementType("Class method");
	IElementType SWITCH = new PHPElementType("Switch statement");
	IElementType CASE_DEFAULT = new PHPElementType("Default case");
	IElementType CASE = new PHPElementType("Case");
	IElementType RETURN = new PHPElementType("Return");
	IElementType STATIC = new PHPElementType("Static statement");
	IElementType DECLARE = new PHPElementType("Declare statement");
	IElementType DECLARE_DIRECTIVE = new PHPElementType("Declare directive");
	IElementType TRY = new PHPElementType("Try statement");
	IElementType CATCH = new PHPElementType("Catch clause");
	IElementType THROW = new PHPElementType("Throw statement");
	IElementType EXPRESSION = new PHPElementType("Expression");
	IElementType OBSCURE_VARIABLE = new PHPElementType("Obscure variable");
	IElementType FUNCTION_CALL = new PHPElementType("Function call");
	IElementType FIELD_REFERENCE = new PHPElementType("PhpField reference");
	IElementType METHOD_REFERENCE = new PhpInstancableTokenType("METHOD_REFERENCE", PhpMethodReferenceImpl.class);
	IElementType HEREDOC = new PHPElementType("Heredoc");
	IElementType VARIABLE_NAME = new PHPElementType("PhpVariableReference name");
	IElementType NUMBER = new PHPElementType("Number");
	IElementType SHELL_COMMAND = new PHPElementType("Shell command");

	IElementType PHP_OUTER_TYPE = new PHPElementType("PHP outer type in html");
}
