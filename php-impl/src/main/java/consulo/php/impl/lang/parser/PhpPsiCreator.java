package consulo.php.impl.lang.parser;

import javax.annotation.Nonnull;

import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.psi.PsiElement;
import consulo.php.impl.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import consulo.php.impl.lang.documentation.phpdoc.psi.PhpDocElementType;
import consulo.php.impl.lang.documentation.phpdoc.psi.PhpDocPsiCreator;
import consulo.php.impl.lang.documentation.phpdoc.psi.impl.PhpDocCommentImpl;
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

	public static PsiElement create(ASTNode node)
	{
		IElementType type = node.getElementType();

		if(type instanceof PhpDocElementType)
		{
			return PhpDocPsiCreator.createElement(node);
		}
		if(type == PhpDocElementTypes.DOC_COMMENT)
		{
			return new PhpDocCommentImpl();
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
