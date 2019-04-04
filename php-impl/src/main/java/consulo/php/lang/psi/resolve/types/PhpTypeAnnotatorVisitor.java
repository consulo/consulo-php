package consulo.php.lang.psi.resolve.types;

import java.util.Collection;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import com.jetbrains.php.lang.psi.elements.Variable;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.php.index.PhpIndexUtil;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import consulo.php.lang.documentation.phpdoc.psi.tags.PhpDocReturnTag;
import consulo.php.lang.documentation.phpdoc.psi.tags.PhpDocVarTag;
import consulo.php.lang.psi.PhpAssignmentExpression;
import consulo.php.lang.psi.PhpCatchStatement;
import consulo.php.lang.psi.PhpFieldReference;
import consulo.php.lang.psi.PhpNewExpression;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author jay
 * @date Jun 18, 2008 12:03:21 PM
 */
public class PhpTypeAnnotatorVisitor extends PhpElementVisitor
{

	public static final Key<PhpType> TYPE_KEY = new Key<PhpType>("PhpTypeKey");

	@Override
	@SuppressWarnings({"ConstantConditions"})
	public void visitVariableReference(Variable variable)
	{
		PhpType type = new PhpType();
		if(variable.getName() != null && variable.getName().equals("this"))
		{
			PhpClass klass = PsiTreeUtil.getParentOfType(variable, PhpClass.class);
			if(klass != null && klass.getName() != null)
			{
				//type.addClasses(PhpIndexUtil.getClassesForName(variable, klass.getName()));
			}
		}
		else if(!variable.isDeclaration())
		{
			ResolveResult[] results = variable.multiResolve(false);
			if(results.length > 0)
			{
				ResolveResult result = results[results.length - 1];
				PsiElement element = result.getElement();
				if(element instanceof PhpTypedElement)
				{
					type.add(((PhpTypedElement) element).getType());
				}
			}
		}
		else
		{
			PsiElement parent = variable.getParent();
			if(parent instanceof PhpAssignmentExpression)
			{
				PsiElement value = ((PhpAssignmentExpression) parent).getValue();
				if(value instanceof PhpTypedElement)
				{
					type.add(((PhpTypedElement) value).getType());
				}
			}
			else if(parent instanceof PhpCatchStatement)
			{
				ClassReference classReference = ((PhpCatchStatement) parent).getExceptionType();
				if(classReference != null)
				{
					//type.addClasses(PhpIndexUtil.getClassesForName(variable, classReference.getReferenceName()));
				}
			}
		}
		variable.putUserData(TYPE_KEY, type);
	}

	@Override
	public void visitAssignmentExpression(PhpAssignmentExpression expr)
	{
		PhpType type = new PhpType();
		PsiElement value = expr.getValue();
		if(value instanceof PhpTypedElement)
		{
			type.add(((PhpTypedElement) value).getType());
		}
		expr.putUserData(TYPE_KEY, type);
	}

	@Override
	public void visitMethodReference(MethodReference reference)
	{
		PhpType type = new PhpType();
		PsiElement element = reference.resolve();
		if(element instanceof Function)
		{
			type.add(((Function) element).getType());
		}
		reference.putUserData(TYPE_KEY, type);
	}

	@Override
	public void visitFunction(Function phpMethod)
	{
		PhpType type = new PhpType();
		PhpDocComment docComment = phpMethod.getDocComment();
		if(docComment != null)
		{
			PhpDocReturnTag returnTag = docComment.getReturnTag();
			if(returnTag != null)
			{
				for(String className : returnTag.getTypes())
				{
					//type.addClasses(PhpIndexUtil.getClassesForName(phpMethod, className));
				}
			}
		}
		phpMethod.putUserData(TYPE_KEY, type);
	}

	@Override
	public void visitFieldReference(PhpFieldReference fieldReference)
	{
		PhpType type = new PhpType();
		PsiElement element = fieldReference.resolve();
		if(element instanceof Field)
		{
			type.add(((Field) element).getType());
		}
		fieldReference.putUserData(TYPE_KEY, type);
	}

	@Override
	public void visitField(Field phpField)
	{
		PhpType type = new PhpType();
		PhpDocComment docComment = phpField.getDocComment();
		if(docComment != null)
		{
			PhpDocVarTag varTag = docComment.getVarTag();
			if(varTag != null && varTag.getType() != null && varTag.getType().length() > 0)
			{
				//type.addClasses(PhpIndexUtil.getClassesForName(phpField, varTag.getType()));
			}
		}
		phpField.putUserData(TYPE_KEY, type);
	}

	@Override
	@SuppressWarnings({"ConstantConditions"})
	public void visitNewExpression(PhpNewExpression expression)
	{
		PhpType type = new PhpType();
		PhpPsiElement classReference = expression.getFirstPsiChild();
		if(classReference instanceof ClassReference)
		{
			PsiElement klass = ((ClassReference) classReference).resolve();
			if(!(klass instanceof PhpClass))
			{
				klass = PsiTreeUtil.getParentOfType(klass, PhpClass.class);
			}
			if(klass != null && ((PhpClass) klass).getName() != null)
			{
				//type.addClasses(PhpIndexUtil.getClassesForName(expression, ((PhpClass) klass).getName()));
			}
		}
		expression.putUserData(TYPE_KEY, type);
	}

	@Override
	public void visitParameter(Parameter parameter)
	{
		PhpType type = new PhpType();
		PhpPsiElement classReference = parameter.getFirstPsiChild();
		if(classReference instanceof ClassReference)
		{
			Collection<PhpClass> classesFor = PhpIndexUtil.getClassesForName(parameter, ((ClassReference) classReference).getReferenceName());
			//type.addClasses(classesFor);
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
