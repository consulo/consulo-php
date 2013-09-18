package org.consulo.php.lang.psi.visitors;

import org.consulo.php.lang.psi.*;
import org.consulo.php.lang.psi.impl.PhpClassConstantReferenceImpl;
import org.consulo.php.lang.psi.impl.PhpFileImpl;
import org.consulo.php.lang.psi.impl.PhpTryStatementImpl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;

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

	public void visitPhpClass(PhpClass clazz)
	{
		visitPhpElement(clazz);
	}

	public void visitPhpMethod(PhpMethod phpMethod)
	{
		visitPhpElement(phpMethod);
	}

	public void visitPhpFunction(PhpFunction phpFunction)
	{
		visitPhpElement(phpFunction);
	}

	public void visitPhpNewExpression(PhpNewExpression expression)
	{
		visitPhpElement(expression);
	}

	public void visitPhpAssignmentExpression(PhpAssignmentExpression expr)
	{
		visitPhpElement(expr);
	}

	public void visitPhpBinaryExpression(PhpBinaryExpression expr)
	{
		visitPhpElement(expr);
	}

	public void visitPhpUnaryExpression(PhpUnaryExpression expr)
	{
		visitPhpElement(expr);
	}

	public void visitPhpForeach(PhpForeachStatement foreach)
	{
		visitPhpElement(foreach);
	}

	public void visitPhpCatch(PhpCatchStatement phpCatch)
	{
		visitPhpElement(phpCatch);
	}

	public void visitPhpParameterList(PhpParameterList list)
	{
		visitPhpElement(list);
	}

	public void visitPhpParameter(PhpParameter parameter)
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

	public void visitPhpIf(PhpIfStatement ifStatement)
	{
		visitPhpElement(ifStatement);
	}

	public void visitPhpClassReference(PhpClassReference classReference)
	{
		visitPhpElement(classReference);
	}

	public void visitPhpMethodReference(PhpMethodReference reference)
	{
		visitPhpElement(reference);
	}

	public void visitPhpClassConstantReference(PhpClassConstantReferenceImpl constantReference)
	{
		visitPhpElement(constantReference);
	}

	public void visitPhpFieldReference(PhpFieldReference fieldReference)
	{
		visitPhpElement(fieldReference);
	}

	public void visitPhpField(PhpField phpField)
	{
		visitPhpElement(phpField);
	}

	public void visitPhpConstant(PhpConstantReference constant)
	{
		visitPhpElement(constant);
	}

	public void visitPhpFunctionCall(PhpFunctionCall call)
	{
		visitPhpElement(call);
	}

	public void visitPhpTry(PhpTryStatementImpl tryStatement)
	{
		visitPhpElement(tryStatement);
	}

	public void visitPhpElseIf(PhpElseIfStatement elseIf)
	{
		visitPhpElement(elseIf);
	}

	public void visitPhpElse(PhpElseStatement elseStatement)
	{
		visitPhpElement(elseStatement);
	}

	public void visitPhpFor(PhpForStatement forStatement)
	{
		visitPhpElement(forStatement);
	}
}
