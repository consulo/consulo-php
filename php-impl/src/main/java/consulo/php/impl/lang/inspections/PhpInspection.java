package consulo.php.impl.lang.inspections;

import com.jetbrains.php.lang.PhpLanguage;
import consulo.language.Language;
import consulo.language.editor.inspection.LocalInspectionTool;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.language.psi.PsiElementVisitor;
import org.jetbrains.annotations.Nls;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
		return "General";
	}

	@Override
	@Nonnull
	public String getShortName()
	{
		return getClass().getSimpleName();
	}

	@Nullable
	@Override
	public Language getLanguage()
	{
		return PhpLanguage.INSTANCE;
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
