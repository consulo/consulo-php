package org.consulo.php.lang.psi.resolve.types;

import java.util.Collection;

import org.consulo.php.index.PhpIndexUtil;
import org.consulo.php.lang.psi.*;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import lombok.val;

/**
 * @author jay
 * @date Jun 18, 2008 12:03:21 PM
 */
public class PhpTypeAnnotatorVisitor extends PhpElementVisitor
{

	public static final Key<PhpType> TYPE_KEY = new Key<PhpType>("PhpTypeKey");

	@Override
	@SuppressWarnings({"ConstantConditions"})
	public void visitVariableReference(PhpVariableReference variable)
	{
		PhpType type = new PhpType();
		if(variable.getName() != null && variable.getName().equals("this"))
		{
			val klass = PsiTreeUtil.getParentOfType(variable, PhpClass.class);
			if(klass != null && klass.getName() != null)
			{
				type.addClasses(PhpIndexUtil.getClassesForName(variable, klass.getName()));
			}
		}
		else if(!variable.isDeclaration())
		{
			val results = variable.multiResolve(false);
			if(results.length > 0)
			{
				ResolveResult result = results[results.length - 1];
				val element = result.getElement();
				if(element instanceof PhpTypeOwner)
				{
					type.addClasses(((PhpTypeOwner) element).getType().getTypes());
				}
			}
		}
		else
		{
			val parent = variable.getParent();
			if(parent instanceof PhpAssignmentExpression)
			{
				val value = ((PhpAssignmentExpression) parent).getValue();
				if(value instanceof PhpTypeOwner)
				{
					type.addClasses(((PhpTypeOwner) value).getType().getTypes());
				}
			}
			else if(parent instanceof PhpCatchStatement)
			{
				val classReference = ((PhpCatchStatement) parent).getExceptionType();
				if(classReference != null)
				{
					type.addClasses(PhpIndexUtil.getClassesForName(variable, classReference.getReferenceName()));
				}
			}
		}
		variable.putUserData(TYPE_KEY, type);
	}

	@Override
	public void visitAssignmentExpression(PhpAssignmentExpression expr)
	{
		PhpType type = new PhpType();
		val value = expr.getValue();
		if(value instanceof PhpTypeOwner)
		{
			type.addClasses(((PhpTypeOwner) value).getType().getTypes());
		}
		expr.putUserData(TYPE_KEY, type);
	}

	@Override
	public void visitMethodReference(PhpMethodReference reference)
	{
		PhpType type = new PhpType();
		val element = reference.resolve();
		if(element instanceof PhpFunction)
		{
			type.addClasses(((PhpFunction) element).getType().getTypes());
		}
		reference.putUserData(TYPE_KEY, type);
	}

	@Override
	public void visitFunction(PhpFunction phpMethod)
	{
		PhpType type = new PhpType();
		val docComment = phpMethod.getDocComment();
		if(docComment != null)
		{
			val returnTag = docComment.getReturnTag();
			if(returnTag != null)
			{
				for(String className : returnTag.getTypes())
				{
					type.addClasses(PhpIndexUtil.getClassesForName(phpMethod, className));
				}
			}
		}
		phpMethod.putUserData(TYPE_KEY, type);
	}

	@Override
	public void visitFieldReference(PhpFieldReference fieldReference)
	{
		PhpType type = new PhpType();
		val element = fieldReference.resolve();
		if(element instanceof PhpField)
		{
			type.addClasses(((PhpField) element).getType().getTypes());
		}
		fieldReference.putUserData(TYPE_KEY, type);
	}

	@Override
	public void visitField(PhpField phpField)
	{
		PhpType type = new PhpType();
		val docComment = phpField.getDocComment();
		if(docComment != null)
		{
			val varTag = docComment.getVarTag();
			if(varTag != null && varTag.getType() != null && varTag.getType().length() > 0)
			{
				type.addClasses(PhpIndexUtil.getClassesForName(phpField, varTag.getType()));
			}
		}
		phpField.putUserData(TYPE_KEY, type);
	}

	@Override
	@SuppressWarnings({"ConstantConditions"})
	public void visitNewExpression(PhpNewExpression expression)
	{
		PhpType type = new PhpType();
		val classReference = expression.getFirstPsiChild();
		if(classReference instanceof PhpClassReference)
		{
			PsiElement klass = ((PhpClassReference) classReference).resolve();
			if(!(klass instanceof PhpClass))
			{
				klass = PsiTreeUtil.getParentOfType(klass, PhpClass.class);
			}
			if(klass != null && ((PhpClass) klass).getName() != null)
			{
				type.addClasses(PhpIndexUtil.getClassesForName(expression, ((PhpClass) klass).getName()));
			}
		}
		expression.putUserData(TYPE_KEY, type);
	}

	@Override
	public void visitParameter(PhpParameter parameter)
	{
		PhpType type = new PhpType();
		val classReference = parameter.getFirstPsiChild();
		if(classReference instanceof PhpClassReference)
		{
			Collection<PhpClass> classesFor = PhpIndexUtil.getClassesForName(parameter, ((PhpClassReference) classReference).getReferenceName());
			type.addClasses(classesFor);
		}
		parameter.putUserData(TYPE_KEY, type);
	}

	private static PhpTypeAnnotatorVisitor instance = null;

	public static PhpTypeAnnotatorVisitor getInstance()
	{
		if(instance == null)
		{
			instance = new PhpTypeAnnotatorVisitor();
		}
		return instance;
	}

	public static void process(PsiElement element)
	{
		element.accept(getInstance());
	}
}
