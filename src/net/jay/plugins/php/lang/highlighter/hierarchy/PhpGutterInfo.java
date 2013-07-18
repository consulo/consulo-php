package net.jay.plugins.php.lang.highlighter.hierarchy;

import com.intellij.openapi.project.Project;
import net.jay.plugins.php.lang.psi.elements.PhpNamedElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author jay
 * @date Jun 25, 2008 1:52:38 AM
 */
abstract public class PhpGutterInfo extends PhpLineMarkerInfo
{

	protected Project project;
	protected List<? extends PhpNamedElement> elements;

	public PhpGutterInfo(final int startOffset, @NotNull Project project, @NotNull List<? extends PhpNamedElement> elements)
	{
		super(startOffset);
		this.project = project;
		assert elements.size() > 0;
		this.elements = elements;
	}

	public Project getProject()
	{
		return project;
	}

	public List<? extends PhpNamedElement> getElements()
	{
		return elements;
	}

	abstract public String getTitle();
}
