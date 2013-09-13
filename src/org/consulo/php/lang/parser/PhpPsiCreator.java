package org.consulo.php.lang.parser;

import org.consulo.php.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import org.consulo.php.lang.documentation.phpdoc.psi.PhpDocElementType;
import org.consulo.php.lang.documentation.phpdoc.psi.PhpDocPsiCreator;
import org.consulo.php.lang.documentation.phpdoc.psi.impl.PhpDocCommentImpl;
import org.consulo.php.lang.psi.elements.impl.*;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.consulo.php.lang.psi.elements.impl.IfImpl;
import org.consulo.php.lang.psi.elements.impl.ImplementsListImpl;
import org.consulo.php.lang.psi.elements.impl.PhpParameterImpl;
import org.consulo.php.lang.psi.elements.impl.StatementImpl;

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
			return new StatementImpl(node);
		}
		if(type == GROUP_STATEMENT)
		{
			return new GroupStatementImpl(node);
		}
		if(type == FUNCTION)
		{
			return new FunctionImpl(node);
		}
		if(type == PARAMETER_LIST)
		{
			return new PhpParameterListImpl(node);
		}
		if(type == PARAMETER)
		{
			return new PhpParameterImpl(node);
		}
		if(type == NEW_EXPRESSION)
		{
			return new NewExpressionImpl(node);
		}
		if(BINARY_EXPRESSIONS.contains(type))
		{
			return new BinaryExpressionImpl(node);
		}
		if(type == UNARY_EXPRESSION)
		{
			return new UnaryExpressionImpl(node);
		}
		if(type == ASSIGNMENT_EXPRESSION)
		{
			return new AssignmentExpressionImpl(node);
		}
		if(type == SELF_ASSIGNMENT_EXPRESSION)
		{
			return new SelfAssignmentExpressionImpl(node);
		}
		if(type == CLASS_METHOD)
		{
			return new PhpMethodImpl(node);
		}
		if(type == CATCH)
		{
			return new CatchImpl(node);
		}
		if(type == FOREACH)
		{
			return new ForeachImpl(node);
		}
		if(type == GLOBAL)
		{
			return new GlobalImpl(node);
		}
		if(type == CLASS_FIELD)
		{
			return new PhpFieldImpl(node);
		}
		if(type == IF)
		{
			return new IfImpl(node);
		}
		if(type == ELSE_IF)
		{
			return new ElseIfImpl(node);
		}
		if(type == ELSE)
		{
			return new ElseImpl(node);
		}
		if(type == FOR)
		{
			return new ForImpl(node);
		}

		if(type == FIELD_REFERENCE)
		{
			return new FieldReferenceImpl(node);
		}
		if(type == FUNCTION_CALL)
		{
			return new FunctionCallImpl(node);
		}

		if(type == EXTENDS_LIST)
		{
			return new ExtendsListImpl(node);
		}
		if(type == IMPLEMENTS_LIST)
		{
			return new ImplementsListImpl(node);
		}
		if(type == CLASS_CONSTANT_REFERENCE)
		{
			return new ClassConstantReferenceImpl(node);
		}
		if(type == CONSTANT)
		{
			return new ConstantReferenceImpl(node);
		}
		if(type == TRY)
		{
			return new TryImpl(node);
		}
		return new PHPPsiElementImpl(node);
	}
}
