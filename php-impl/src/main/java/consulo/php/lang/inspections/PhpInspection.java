package consulo.php.lang.inspections;

import javax.annotation.Nonnull;

import consulo.php.PhpBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import com.intellij.codeHighlighting.HighlightDisplayLevel;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;

/**
 * @author jay
 * @date May 9, 2008 2:25:31 PM
 */
abstract public class PhpInspection extends LocalInspectionTool
{
	@Override
	@Nls
	@Nonnull
	public String getGroupDisplayName()
	{
		return PhpBundle.message("php.inspections.group");
	}

	@Override
	@NonNls
	@Nonnull
	public String getShortName()
	{
		return getClass().getSimpleName();
	}

	@Override
	public boolean isEnabledByDefault()
	{
		return true;
	}

	/**
	 * @return highlighting level for this inspection tool that is used in default settings
	 */
	@Override
	@Nonnull
	public HighlightDisplayLevel getDefaultLevel()
	{
		return HighlightDisplayLevel.WARNING;
	}

	@Override
	@Nonnull
	abstract public PsiElementVisitor buildVisitor(@Nonnull ProblemsHolder holder, boolean isOnTheFly);

}
