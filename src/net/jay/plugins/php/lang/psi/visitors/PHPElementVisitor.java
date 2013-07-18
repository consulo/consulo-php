package net.jay.plugins.php.lang.psi.visitors;

import net.jay.plugins.php.lang.psi.PHPFile;
import net.jay.plugins.php.lang.psi.elements.*;
import net.jay.plugins.php.lang.psi.elements.impl.ClassConstantReferenceImpl;
import net.jay.plugins.php.lang.psi.elements.impl.TryImpl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 27.02.2007
 *
 * @author jay
 */
public abstract class PHPElementVisitor extends PsiElementVisitor
{

	public void visitPhpElement(PHPPsiElement element)
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

	public void visitPhpFunction(Function function)
	{
		visitPhpElement(function);
	}

	public void visitPhpNewExpression(NewExpression expression)
	{
		visitPhpElement(expression);
	}

	public void visitPhpAssignmentExpression(AssignmentExpression expr)
	{
		visitPhpElement(expr);
	}

	public void visitPhpBinaryExpression(BinaryExpression expr)
	{
		visitPhpElement(expr);
	}

	public void visitPhpUnaryExpression(UnaryExpression expr)
	{
		visitPhpElement(expr);
	}

	public void visitPhpForeach(Foreach foreach)
	{
		visitPhpElement(foreach);
	}

	public void visitPhpCatch(Catch phpCatch)
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

	public void visitElement(PsiElement element)
	{
	}

	public void visitPhpFile(PHPFile phpFile)
	{
		visitFile(phpFile);
	}

	public void visitPhpIf(If ifStatement)
	{
		visitPhpElement(ifStatement);
	}

	public void visitPhpClassReference(ClassReference classReference)
	{
		visitPhpElement(classReference);
	}

	public void visitPhpMethodReference(PhpMethodReference reference)
	{
		visitPhpElement(reference);
	}

	public void visitPhpClassConstantReference(ClassConstantReferenceImpl constantReference)
	{
		visitPhpElement(constantReference);
	}

	public void visitPhpFieldReference(FieldReference fieldReference)
	{
		visitPhpElement(fieldReference);
	}

	public void visitPhpField(PhpField phpField)
	{
		visitPhpElement(phpField);
	}

	public void visitPhpConstant(ConstantReference constant)
	{
		visitPhpElement(constant);
	}

	public void visitPhpFunctionCall(FunctionCall call)
	{
		visitPhpElement(call);
	}

	public void visitPhpTry(TryImpl tryStatement)
	{
		visitPhpElement(tryStatement);
	}

	public void visitPhpElseIf(ElseIf elseIf)
	{
		visitPhpElement(elseIf);
	}

	public void visitPhpElse(Else elseStatement)
	{
		visitPhpElement(elseStatement);
	}

	public void visitPhpFor(For forStatement)
	{
		visitPhpElement(forStatement);
	}
}
