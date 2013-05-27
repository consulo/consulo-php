package net.jay.plugins.php.lang.highlighter.hierarchy;

import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.Icon;

import net.jay.plugins.php.PHPBundle;
import net.jay.plugins.php.PHPIcons;
import net.jay.plugins.php.cache.psi.LightPhpClass;
import net.jay.plugins.php.cache.psi.LightPhpElement;
import net.jay.plugins.php.cache.psi.LightPhpInterface;
import net.jay.plugins.php.cache.psi.LightPhpMethod;

import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.project.Project;

/**
 * @author jay
 * @date Jun 25, 2008 1:05:18 AM
 */
public class PhpOverridesGutterInfo extends PhpGutterInfo
{

	private String tooltip;

	public PhpOverridesGutterInfo(final int startOffset, @NotNull Project project, @NotNull List<? extends LightPhpElement> elements)
	{
		super(startOffset, project, elements);
		createToolTip();
	}

	public String getTitle()
	{
		return PHPBundle.message("gutter.overriding.select.title");
	}

	private void createToolTip()
	{
		final StringBuilder buffer = new StringBuilder();

		for(int i = 0; i < elements.size(); i++)
		{
			if(i > 0)
			{
				buffer.append(BREAK);
			}
			LightPhpElement element = elements.get(i);
			if(((LightPhpMethod) element).getModifier().isAbstract())
			{
				buffer.append("Implements");
			}
			else
			{
				buffer.append("Overrides");
			}
			buffer.append(SPACE).append("method");
			buffer.append(SPACE).append("in").append(SPACE);

			LightPhpElement container = element.getParentOfType(LightPhpClass.class);
			if(container == null)
			{
				container = element.getParentOfType(LightPhpInterface.class);
				if(container != null)
				{
					buffer.append("interface").append(SPACE);
				}
			}
			else
			{
				buffer.append("class").append(SPACE);
			}

			if(container != null)
			{
				buffer.append("'").append(container.getName()).append("'");
			}
			else
			{
				buffer.append("unknown place");
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
			for(LightPhpElement element : elements)
			{
				if(element.getParentOfType(LightPhpInterface.class) == null)
				{
					return PHPIcons.OVERRIDES;
				}
			}
			return PHPIcons.IMPLEMENTS;
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
	}

	private class MyNavigateAction extends AnAction
	{
		public void actionPerformed(final AnActionEvent e)
		{
			MouseEvent mouseEvent = (MouseEvent) e.getInputEvent();
			PhpGutterNavigator.browse(mouseEvent, PhpOverridesGutterInfo.this);
		}
	}

}
