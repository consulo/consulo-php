package consulo.php.lang.psi.visitors;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import consulo.php.lang.psi.*;
import consulo.php.lang.psi.impl.PhpArrayExpressionImpl;
import consulo.php.lang.psi.impl.PhpClassConstantReferenceImpl;
import consulo.php.lang.psi.impl.PhpFileImpl;
import consulo.php.lang.psi.impl.PhpTryStatementImpl;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 27.02.2007
 *
 * @author jay
 */
public abstract class PhpElementVisitor extends PsiElementVisitor
{

	public void visitPhpElement(PhpElement element)
	{
		visitElement(element);
	}

	public void visitClass(PhpClass clazz)
	{
		visitPhpElement(clazz);
	}

	public void visitFunction(PhpFunction phpFunction)
	{
		visitPhpElement(phpFunction);
	}

	public void visitNewExpression(PhpNewExpression expression)
	{
		visitPhpElement(expression);
	}

	public void visitAssignmentExpression(PhpAssignmentExpression expr)
	{
		visitPhpElement(expr);
	}

	public void visitBinaryExpression(PhpBinaryExpression expr)
	{
		visitPhpElement(expr);
	}

	public void visitUnaryExpression(PhpUnaryExpression expr)
	{
		visitPhpElement(expr);
	}

	public void visitForeachStatement(PhpForeachStatement foreach)
	{
		visitPhpElement(foreach);
	}

	public void visitCatchStatement(PhpCatchStatement phpCatch)
	{
		visitPhpElement(phpCatch);
	}

	public void visitParameterList(PhpParameterList list)
	{
		visitPhpElement(list);
	}

	public void visitParameter(PhpParameter parameter)
	{
		visitPhpElement(parameter);
	}

	public void visitVariableReference(PhpVariableReference variable)
	{
		visitPhpElement(variable);
	}

	@Override
	public void visitElement(PsiElement element)
	{
	}

	public void visitPhpFile(PhpFileImpl phpFile)
	{
		visitFile(phpFile);
	}

	public void visitIfStatement(PhpIfStatement ifStatement)
	{
		visitPhpElement(ifStatement);
	}

	public void visitClassReference(PhpClassReference classReference)
	{
		visitPhpElement(classReference);
	}

	public void visitMethodReference(PhpMethodReference reference)
	{
		visitPhpElement(reference);
	}

	public void visitClassConstantReference(PhpClassConstantReferenceImpl constantReference)
	{
		visitPhpElement(constantReference);
	}

	public void visitFieldReference(PhpFieldReference fieldReference)
	{
		visitPhpElement(fieldReference);
	}

	public void visitField(PhpField phpField)
	{
		visitPhpElement(phpField);
	}

	public void visitConstant(PhpConstantReference constant)
	{
		visitPhpElement(constant);
	}

	public void visitFunctionCall(PhpFunctionCall call)
	{
		visitPhpElement(call);
	}

	public void visitTryStatement(PhpTryStatementImpl tryStatement)
	{
		visitPhpElement(tryStatement);
	}

	public void visitElseIfStatement(PhpElseIfStatement elseIf)
	{
		visitPhpElement(elseIf);
	}

	public void visitElseStatement(PhpElseStatement elseStatement)
	{
		visitPhpElement(elseStatement);
	}

	public void visitForStatement(PhpForStatement forStatement)
	{
		visitPhpElement(forStatement);
	}

	public void visitModifierList(PhpModifierList modifierList)
	{
		visitPhpElement(modifierList);
	}

	public void visitStatement(PhpStatement phpStatement)
	{
		visitPhpElement(phpStatement);
	}

	public void visitWhileStatement(PhpWhileStatement whileStatement)
	{
		visitPhpElement(whileStatement);
	}

	public void visitNamespaceStatement(PhpNamespaceStatement phpNamespaceStatement)
	{
		visitPhpElement(phpNamespaceStatement);
	}

	public void visitUse(PhpUse phpUse)
	{
		visitPhpElement(phpUse);
	}

	public void visitTernaryExpression(PhpTernaryExpression phpTernaryExpression)
	{
		visitPhpElement(phpTernaryExpression);
	}

	public void visitElvisExpression(PhpElvisExpression phpElvisExpression)
	{
		visitPhpElement(phpElvisExpression);
	}

	public void visitUseList(PhpUseListStatement list)
	{
		visitPhpElement(list);
	}

	public void visitArrayExpression(PhpArrayExpression phpArrayExpression)
	{
		visitPhpElement(phpArrayExpression);
	}
}
