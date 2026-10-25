package consulo.php.impl.lang.parser;

import java.util.function.Function;
import java.util.Map;
import java.util.HashMap;
import jakarta.annotation.Nonnull;

import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
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
		FACTORIES.put(EXPRESSION_PARAMETER_LIST, PhpExpressionParameterListImpl::new);
		FACTORIES.put(FUNCTION_PARAMETER_LIST, PhpFunctionParameterListImpl::new);
		FACTORIES.put(UNARY_EXPRESSION, PhpUnaryExpressionImpl::new);
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
		FACTORIES.put(STRING, PhpStringLiteralExpressionImpl::new);
		FACTORIES.put(CLASS_REFERENCE, PhpClassReferenceImpl::new);
		FACTORIES.put(VARIABLE_REFERENCE, PhpVariableReferenceImpl::new);
		FACTORIES.put(CLASS_CONSTANT_REFERENCE, PhpClassConstantReferenceImpl::new);
		FACTORIES.put(ANONYMOUS_FUNCTION, PhpFunctionImpl::new);
		FACTORIES.put(RETURN, PhpReturnStatemetImpl::new);
		FACTORIES.put(FUNCTION_REFERENCE, PhpFunctionReferenceImpl::new);
		FACTORIES.put(METHOD_REFERENCE, PhpMethodReferenceImpl::new);
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
