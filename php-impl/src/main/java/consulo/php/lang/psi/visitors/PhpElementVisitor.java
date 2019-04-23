package consulo.php.lang.psi.visitors;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.jetbrains.php.lang.psi.elements.*;
import consulo.php.lang.psi.*;
import consulo.php.lang.psi.impl.PhpArrayHashElementImpl;
import consulo.php.lang.psi.impl.PhpFileImpl;
import consulo.php.lang.psi.impl.PhpReturnStatemetImpl;
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

	public void visitPhpElement(PhpPsiElement element)
	{
		visitElement(element);
	}

	public void visitClass(PhpClass clazz)
	{
		visitPhpElement(clazz);
	}

	public void visitMethod(Method method)
	{
		visitFunction(method);
	}

	public void visitFunction(Function phpFunction)
	{
		visitPhpElement(phpFunction);
	}

	public void visitNewExpression(NewExpression expression)
	{
		visitPhpElement(expression);
	}

	public void visitAssignmentExpression(PhpAssignmentExpression expr)
	{
		visitPhpElement(expr);
	}

	public void visitBinaryExpression(BinaryExpression expr)
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

	public void visitCatchStatement(Catch phpCatch)
	{
		visitPhpElement(phpCatch);
	}

	public void visitParameterList(ParameterList list)
	{
		visitPhpElement(list);
	}

	public void visitParameter(Parameter parameter)
	{
		visitPhpElement(parameter);
	}

	public void visitVariableReference(Variable variable)
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

	public void visitClassReference(ClassReference classReference)
	{
		visitPhpElement(classReference);
	}

	public void visitMethodReference(MethodReference reference)
	{
		visitPhpElement(reference);
	}

	public void visitClassConstantReference(ClassConstantReference constantReference)
	{
		visitPhpElement(constantReference);
	}

	public void visitFieldReference(FieldReference fieldReference)
	{
		visitPhpElement(fieldReference);
	}

	public void visitField(Field phpField)
	{
		visitPhpElement(phpField);
	}

	public void visitConstant(ConstantReference constant)
	{
		visitPhpElement(constant);
	}

	public void visitFunctionReference(FunctionReference reference)
	{
		visitPhpElement(reference);
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

	public void visitStatement(Statement statement)
	{
		visitPhpElement(statement);
	}

	public void visitWhileStatement(PhpWhileStatement whileStatement)
	{
		visitPhpElement(whileStatement);
	}

	public void visitNamespaceStatement(PhpNamespace phpNamespaceStatement)
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

	public void visitArrayExpression(ArrayCreationExpression arrayCreationExpression)
	{
		visitPhpElement(arrayCreationExpression);
	}

	public void visitReturnType(PhpReturnType type)
	{
		visitElement(type);
	}

	public void visitPhpStringLiteralExpression(StringLiteralExpression expression)
	{
		visitPhpElement(expression);
	}

	public void visitArrayHashElement(PhpArrayHashElementImpl element)
	{
		visitPhpElement(element);
	}

	public void visitPhpReturn(PhpReturnStatemetImpl statemet)
	{
		visitPhpElement(statemet);
	}

	public void visitPhpYield(PhpYield yield)
	{
		visitPhpElement(yield);
	}
}
