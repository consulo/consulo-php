package consulo.php.impl.lang.parser;

import com.jetbrains.php.lang.psi.elements.impl.StringLiteralExpressionImpl;
import java.util.function.Function;
import java.util.Map;
import java.util.HashMap;
import jakarta.annotation.Nonnull;

import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.ast.TokenSet;
import consulo.php.impl.lang.psi.PhpStubElements;
import consulo.language.psi.PsiElement;
import consulo.php.lang.documentation.phpdoc.psi.PhpDocElementType;
import consulo.php.impl.lang.documentation.phpdoc.psi.PhpDocPsiCreator;
import consulo.php.impl.lang.psi.impl.*;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 30.03.2007
 *
 * @author jay
 */
@Deprecated
public class PhpPsiCreator implements PhpElementTypes
{
	private static final Map<IElementType, Function<ASTNode, PsiElement>> FACTORIES = new HashMap<>();

	static
	{
		FACTORIES.put(GROUP_STATEMENT, PhpGroupStatementImpl::new);
		FACTORIES.put(STATEMENT, PhpStatementImpl::new);
		FACTORIES.put(MODIFIER_LIST, PhpModifierListImpl::new);
		FACTORIES.put(USE_LIST, PhpUseListStatementImpl::new);
		FACTORIES.put(USE, PhpUseImpl::new);
		FACTORIES.put(RETURN_TYPE, PhpReturnTypeImpl::new);
		FACTORIES.put(ANONYMOUS_CLASS, PhpClassImpl::new);
		// PhpStorm uses one PARAMETER_LIST type for call arguments and declared parameters
		FACTORIES.put(PARAMETER_LIST, node -> isDeclaredParameterList(node) ? new PhpFunctionParameterListImpl(node) : new PhpExpressionParameterListImpl(node));
		FACTORIES.put(UNARY_EXPRESSION, PhpUnaryExpressionImpl::new);
		FACTORIES.put(ARRAY_INDEX, PhpArrayIndexImpl::new);
		FACTORIES.put(PARENTHESIZED_EXPRESSION, PhpParenthesizedExpressionImpl::new);
		FACTORIES.put(TYPE_DECLARATION, PhpTypeDeclarationImpl::new);
		FACTORIES.put(FIELD_TYPE, PhpFieldTypeImpl::new);
		FACTORIES.put(CLASS_FIELDS, PhpClassFieldsListImpl::new);
		FACTORIES.put(ASSIGNMENT_EXPRESSION, PhpAssignmentExpressionImpl::new);
		FACTORIES.put(SELF_ASSIGNMENT_EXPRESSION, PhpSelfAssignmentExpressionImpl::new);
		FACTORIES.put(TERNARY_EXPRESSION, PhpTernaryExpressionImpl::new);
		FACTORIES.put(ELVIS_EXPRESSION, PhpElvisExpressionImpl::new);
		FACTORIES.put(ATTRIBUTES_LIST, PhpAttributesListImpl::new);
		FACTORIES.put(ATTRIBUTE, PhpAttributeImpl::new);
		FACTORIES.put(ENUM_CASE, PhpEnumCaseImpl::new);
		FACTORIES.put(ARRAY_CREATION_EXPRESSION, PhpArrayCreationExpressionImpl::new);
		FACTORIES.put(ARRAY_ACCESS_EXPRESSION, PhpArrayAccessExpressionImpl::new);
		FACTORIES.put(ARRAY_HASH, PhpArrayHashElementImpl::new);
		FACTORIES.put(YIELD, PhpYieldImpl::new);
		FACTORIES.put(STRING, StringLiteralExpressionImpl::new);
		FACTORIES.put(CLASS_REFERENCE, PhpClassReferenceImpl::new);
		FACTORIES.put(VARIABLE_REFERENCE, PhpVariableReferenceImpl::new);
		FACTORIES.put(CLASS_CONSTANT_REFERENCE, PhpClassConstantReferenceImpl::new);
		FACTORIES.put(ANONYMOUS_FUNCTION, PhpFunctionImpl::new);
		FACTORIES.put(CLOSURE, PhpClosureExpressionImpl::new);
		FACTORIES.put(RETURN, PhpReturnStatemetImpl::new);
		FACTORIES.put(FUNCTION_REFERENCE, node -> isFirstClassCallable(node) ? new PhpCallableFunctionImpl(node) : new PhpFunctionReferenceImpl(node));
		FACTORIES.put(METHOD_REFERENCE, node -> isFirstClassCallable(node) ? new PhpCallableMethodImpl(node) : new PhpMethodReferenceImpl(node));
		FACTORIES.put(CONCATENATION_EXPRESSION, PhpConcatenationExpressionImpl::new);
		FACTORIES.put(SWITCH, PhpSwitchImpl::new);
		FACTORIES.put(CASE, PhpCaseImpl::new);
		FACTORIES.put(CASE_DEFAULT, PhpCaseImpl::new);
	}


	private static final TokenSet PARAMETER_LIST_OWNERS = TokenSet.create(FUNCTION, PhpStubElements.CLASS_METHOD, ANONYMOUS_FUNCTION, PROPERTY_HOOK);

	/**
	 * {@code foo(...)}: the arguments are only the callable placeholder
	 */
	private static boolean isFirstClassCallable(ASTNode node)
	{
		ASTNode arguments = node.findChildByType(PARAMETER_LIST);
		ASTNode first = arguments == null ? null : arguments.getFirstChildNode();
		while(first != null && first.getPsi() instanceof consulo.language.psi.PsiWhiteSpace)
		{
			first = first.getTreeNext();
		}
		return first != null && first.getElementType() == CALLABLE_PLACEHOLDER && first.getTreeNext() == null && "...".equals(first.getText());
	}

	private static boolean isDeclaredParameterList(ASTNode node)
	{
		ASTNode parent = node.getTreeParent();
		return parent != null && PARAMETER_LIST_OWNERS.contains(parent.getElementType());
	}

	public static PsiElement create(ASTNode node)
	{
		IElementType type = node.getElementType();
		Function<ASTNode, PsiElement> factory = FACTORIES.get(type);
		if(factory != null)
		{
			return factory.apply(node);
		}

		if(type instanceof PhpDocElementType)
		{
			return PhpDocPsiCreator.createElement(node);
		}

		if(type == STATEMENT)
		{
			return new PhpStatementImpl(node);
		}

		if(type == PARAMETER)
		{
			return new PhpParameterImpl(node);
		}
		if(type == NEW_EXPRESSION)
		{
			return new PhpNewExpressionImpl(node);
		}
		if(BINARY_EXPRESSIONS.contains(type))
		{
			return new PhpBinaryExpressionImpl(node);
		}
		if(type == UNARY_EXPRESSION)
		{
			return new PhpUnaryExpressionImpl(node);
		}
		if(type == ASSIGNMENT_EXPRESSION)
		{
			return new PhpAssignmentExpressionImpl(node);
		}
		if(type == SELF_ASSIGNMENT_EXPRESSION)
		{
			return new PhpSelfAssignmentExpressionImpl(node);
		}

		if(type == CATCH)
		{
			return new PhpCatchStatementImpl(node);
		}
		if(type == FOREACH)
		{
			return new PhpForeachStatementImpl(node);
		}
		if(type == GLOBAL)
		{
			return new PhpGlobalImpl(node);
		}

		if(type == IF)
		{
			return new PhpIfStatementImpl(node);
		}
		if(type == ELSE_IF)
		{
			return new PhpElseIfStatementImpl(node);
		}
		if(type == ELSE)
		{
			return new PhpElseStatementImpl(node);
		}
		if(type == WHILE)
		{
			return new PhpWhileStatementImpl(node);
		}
		if(type == FOR)
		{
			return new PhpForStatementImpl(node);
		}

		if(type == FIELD_REFERENCE)
		{
			return new PhpFieldReferenceImpl(node);
		}

		if(type == EXTENDS_LIST)
		{
			return new PhpExtendsListImpl(node);
		}
		if(type == IMPLEMENTS_LIST)
		{
			return new PhpImplementsListImpl(node);
		}

		if(type == CONSTANT)
		{
			return new PhpConstantReferenceImpl(node);
		}
		if(type == TRY)
		{
			return new PhpTryStatementImpl(node);
		}
		return new PhpElementImpl(node)
		{
			@Override
			public void accept(@Nonnull PhpElementVisitor visitor)
			{
				visitor.visitPhpElement(this);
			}
		};
	}
}
