package net.jay.plugins.php.lang.highlighter.hierarchy;

import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.Icon;

import net.jay.plugins.php.PHPBundle;
import net.jay.plugins.php.PHPIcons;
import net.jay.plugins.php.cache.psi.LightPhpElement;
import net.jay.plugins.php.cache.psi.LightPhpInterface;
import net.jay.plugins.php.lang.psi.elements.PhpClass;
import net.jay.plugins.php.lang.psi.elements.PhpInterface;

import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;

/**
 * @author jay
 * @date Jun 25, 2008 2:40:27 AM
 */
public class PhpOverridenGutterInfo extends PhpGutterInfo
{

	private PsiElement element;
	private String tooltip;

	public PhpOverridenGutterInfo(final int startOffset, @NotNull Project project, @NotNull List<? extends LightPhpElement> elements, PsiElement element)
	{
		super(startOffset, project, elements);
		this.element = element;
		createToolTip();
	}

	public String getTitle()
	{
		if(element instanceof PhpInterface)
		{
			return PHPBundle.message("gutter.overriden.select.implementation.title", ((PhpInterface) element).getName());
		}
		if(element instanceof PhpClass)
		{
			return PHPBundle.message("gutter.overriden.select.subclass.title", ((PhpClass) element).getName());
		}
		return "";
	}


	private void createToolTip()
	{
		final StringBuilder buffer = new StringBuilder();
		if(elements.size() < 6)
		{
			buffer.append("Is").append(SPACE);
			if(element instanceof PhpInterface)
			{
				buffer.append("implemented").append(SPACE).append("by");
			}
			if(element instanceof PhpClass)
			{
				buffer.append("subclassed").append(SPACE).append("by");
			}

			for(LightPhpElement element : elements)
			{
				buffer.append(BREAK).append(SPACE).append(SPACE).append(SPACE).append(SPACE);
				buffer.append("'").append(element.getName()).append("'");
			}
		}
		else
		{
			buffer.append("Has").append(SPACE);
			if(element instanceof PhpInterface)
			{
				buffer.append("implementations");
			}
			if(element instanceof PhpClass)
			{
				buffer.append("subclasses");
			}
		}

		tooltip = buffer.toString();
	}

	public GutterIconRenderer createGutterRenderer()
	{
		return new MyGutterIconRenderer();
	}

	private class MyGutterIconRenderer extends GutterIconRenderer
	{
		@NotNull
		public Icon getIcon()
		{
			if(element instanceof LightPhpInterface)
			{
				return PHPIcons.IMPLEMENTED;
			}
			return PHPIcons.OVERRIDEN;
		}

		public AnAction getClickAction()
		{
			return new MyNavigateAction();
		}

		public boolean isNavigateAction()
		{
			return true;
		}

		public String getTooltipText()
		{
			return tooltip;
		}

		public GutterIconRenderer.Alignment getAlignment()
		{
			return Alignment.LEFT;
		}

		@Override
		public boolean equals(Object o)
		{
			return false;
		}

		@Override
		public int hashCode()
		{
			return 0;
		}
	}

	private class MyNavigateAction extends AnAction
	{
		public void actionPerformed(final AnActionEvent e)
		{
			MouseEvent mouseEvent = (MouseEvent) e.getInputEvent();
			PhpGutterNavigator.browse(mouseEvent, PhpOverridenGutterInfo.this);
		}
	}
}
