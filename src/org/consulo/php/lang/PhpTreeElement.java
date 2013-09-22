package org.consulo.php.lang;

import java.util.ArrayList;
import java.util.List;

import org.consulo.php.lang.psi.PhpClass;
import org.consulo.php.lang.psi.PhpConstantReference;
import org.consulo.php.lang.psi.PhpFunction;
import org.consulo.php.lang.psi.PhpNamedElement;
import org.consulo.php.lang.psi.PhpParameter;
import org.consulo.php.lang.psi.PhpVariableReference;
import org.consulo.php.lang.psi.impl.PhpFileImpl;
import com.intellij.ide.IconDescriptorUpdaters;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;

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
		if(myElement instanceof PhpFunction)
		{
			PhpFunction e = (PhpFunction) myElement;
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

	private void listParameters(StringBuilder b, PhpParameter[] parameters)
	{
		b.append('(');
		for(int i = 0; i < parameters.length; i++)
		{
			PhpParameter parameter = parameters[i];
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
				if(!(element instanceof PhpParameter) && (!(element instanceof PhpVariableReference) || ((PhpVariableReference) element).isDeclaration()) && !(element instanceof PhpConstantReference))
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
