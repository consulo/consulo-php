package consulo.php.impl.lang.highlighter.hierarchy;

import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import consulo.project.Project;

import javax.annotation.Nonnull;
import java.util.List;

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
