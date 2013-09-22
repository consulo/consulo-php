package org.consulo.php.lang;

import org.consulo.php.PhpLanguageLevel;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.Language;
import com.intellij.lang.LanguageVersion;
import com.intellij.psi.templateLanguages.TemplateLanguage;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 22.02.2007
 *
 * @author jay
 */
public class PhpLanguage extends Language implements TemplateLanguage
{
	public static final PhpLanguage INSTANCE = new PhpLanguage();

	public PhpLanguage()
	{
		super("PHP");
	}

	@Override
	public boolean isCaseSensitive()
	{
		return true;
	}

	@NotNull
	@Override
	public LanguageVersion[] findVersions()
	{
		return PhpLanguageLevel.VALUES;
	}
}
