package consulo.php.impl.refactoring;

import com.jetbrains.php.lang.PhpLanguage;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.refactoring.RefactoringSupportProvider;

import jakarta.annotation.Nonnull;

/**
 * @author jay
 * @date Jul 1, 2008 2:52:51 PM
 */
@ExtensionImpl
public class PhpRefactoringSupportProvider extends RefactoringSupportProvider
{
	@Nonnull
	@Override
	public Language getLanguage()
	{
		return PhpLanguage.INSTANCE;
	}
}
