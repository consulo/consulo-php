package consulo.php.lang;

import java.util.ArrayList;
import java.util.List;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.Variable;
import consulo.annotations.RequiredReadAction;
import consulo.ide.IconDescriptorUpdaters;
import com.jetbrains.php.lang.psi.elements.ConstantReference;
import consulo.php.lang.psi.impl.PhpFileImpl;

/**
 * @author AG
 */
class PhpTreeElement implements StructureViewTreeElement
{
	private PsiElement myElement;

	public PhpTreeElement(PsiElement psiElement)
	{
		this.myElement = psiElement;
	}

	@Override
	public Object getValue()
	{
		return myElement.isValid() ? myElement : null;
	}

	@Override
	@RequiredReadAction
	public ItemPresentation getPresentation()
	{
		if(myElement instanceof PhpFileImpl)
		{
			PhpFileImpl e = (PhpFileImpl) myElement;
			return e.getPresentation();
		}
		if(myElement instanceof PhpClass)
		{
			PhpClass e = (PhpClass) myElement;
			return new PresentationData(e.getName(), null, IconDescriptorUpdaters.getIcon(myElement, 0), null);
		}
		if(myElement instanceof Function)
		{
			Function e = (Function) myElement;
			StringBuilder b = new StringBuilder().append(e.getName());
			listParameters(b, e.getParameters());

			return new PresentationData(b.toString(), null, IconDescriptorUpdaters.getIcon(myElement, 0), null);
		}
		if(myElement instanceof PhpNamedElement)
		{
			PhpNamedElement e = (PhpNamedElement) myElement;

			return new PresentationData(e.getName(), null, IconDescriptorUpdaters.getIcon(myElement, 0), null);
		}
		return null;
	}

	private void listParameters(StringBuilder b, Parameter[] parameters)
	{
		b.append('(');
		for(int i = 0; i < parameters.length; i++)
		{
			Parameter parameter = parameters[i];
			b.append(parameter.getName());
			if(parameters.length - i > 1)
			{
				b.append(", ");
			}
		}
		b.append(')');
	}

	@Override
	public TreeElement[] getChildren()
	{
		List<StructureViewTreeElement> children = new ArrayList<StructureViewTreeElement>();
		collectChildren(children, myElement);
		return children.toArray(EMPTY_ARRAY);
	}

	private void collectChildren(List<StructureViewTreeElement> children, PsiElement myElement)
	{
		final PsiElement[] elements = myElement.getChildren();
		if(elements.length == 0)
		{
			return;
		}
		for(PsiElement element : elements)
		{
			if(element instanceof PhpNamedElement)
			{
				if(!(element instanceof Parameter) && (!(element instanceof Variable) || ((Variable) element).isDeclaration()) && !(element instanceof ConstantReference))
				{
					children.add(new PhpTreeElement(element));
				}
			}
			else
			{
				collectChildren(children, element);
			}
		}
	}

	@Override
	public void navigate(boolean requestFocus)
	{
		((Navigatable) myElement).navigate(requestFocus);
	}

	@Override
	public boolean canNavigate()
	{
		return ((Navigatable) myElement).canNavigate();
	}

	@Override
	public boolean canNavigateToSource()
	{
		return ((Navigatable) myElement).canNavigateToSource();
	}
}
