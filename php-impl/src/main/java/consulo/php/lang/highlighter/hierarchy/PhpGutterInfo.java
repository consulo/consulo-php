package consulo.php.lang.highlighter.hierarchy;

import java.util.List;

import javax.annotation.Nonnull;

import consulo.php.lang.psi.PhpNamedElement;
import com.intellij.openapi.project.Project;

/**
 * @author jay
 * @date Jun 25, 2008 1:52:38 AM
 */
abstract public class PhpGutterInfo extends PhpLineMarkerInfo
{

	protected Project project;
	protected List<? extends PhpNamedElement> elements;

	public PhpGutterInfo(final int startOffset, @Nonnull Project project, @Nonnull List<? extends PhpNamedElement> elements)
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
