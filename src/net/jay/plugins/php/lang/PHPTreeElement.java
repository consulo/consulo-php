package net.jay.plugins.php.lang;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.ui.RowIcon;
import net.jay.plugins.php.lang.psi.PHPFile;
import net.jay.plugins.php.lang.psi.elements.*;
import net.jay.plugins.php.util.PhpPresentationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author AG
 */
class PHPTreeElement implements StructureViewTreeElement
{
	private PsiElement myElement;

	public PHPTreeElement(PsiElement psiElement)
	{
		this.myElement = psiElement;
	}

	public Object getValue()
	{
		return myElement.isValid() ? myElement : null;
	}

	public ItemPresentation getPresentation()
	{
		if(myElement instanceof PHPFile)
		{
			PHPFile e = (PHPFile) myElement;
			return e.getPresentation();
		}
		if(myElement instanceof PhpClass)
		{
			PhpClass e = (PhpClass) myElement;
			return new PresentationData(e.getName(), null, e.getIcon(),  null);
		}
		if(myElement instanceof Method)
		{
			Method e = (Method) myElement;
			StringBuilder b = new StringBuilder().append(e.getName());
			listParameters(b, e.getParameters());
			RowIcon rowIcon = new RowIcon(2);
			rowIcon.setIcon(e.getIcon(), 0);
			rowIcon.setIcon(PhpPresentationUtil.getAccessIcon(e.getModifier()), 1);
			return new PresentationData(b.toString(), null, rowIcon, null);
		}
		if(myElement instanceof Function)
		{
			Function e = (Function) myElement;
			StringBuilder b = new StringBuilder().append(e.getName());
			listParameters(b, e.getParameters());
			RowIcon rowIcon = new RowIcon(2);
			rowIcon.setIcon(e.getIcon(), 0);
			rowIcon.setIcon(PhpPresentationUtil.getAccessIcon(PhpModifier.PUBLIC), 1);
			return new PresentationData(b.toString(), null, rowIcon, null);
		}
		if(myElement instanceof PhpNamedElement)
		{
			PhpNamedElement e = (PhpNamedElement) myElement;
			RowIcon rowIcon = new RowIcon(2);
			rowIcon.setIcon(e.getIcon(), 0);
			rowIcon.setIcon(PhpPresentationUtil.getAccessIcon(PhpModifier.PUBLIC), 1);
			return new PresentationData(e.getName(), null, rowIcon, null);
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
				b.append(", ");
		}
		b.append(')');
	}

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
			return;
		for(PsiElement element : elements)
		{
			if(element instanceof PhpNamedElement)
			{
				if(!(element instanceof Parameter) && (!(element instanceof Variable) || ((Variable) element).isDeclaration()) && !(element instanceof ConstantReference))
					children.add(new PHPTreeElement(element));
			}
			else
				collectChildren(children, element);
		}
	}

	public void navigate(boolean requestFocus)
	{
		((Navigatable) myElement).navigate(requestFocus);
	}

	public boolean canNavigate()
	{
		return ((Navigatable) myElement).canNavigate();
	}

	public boolean canNavigateToSource()
	{
		return ((Navigatable) myElement).canNavigateToSource();
	}
}
