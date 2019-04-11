package com.jetbrains.php.lang;

import javax.annotation.Nonnull;

import com.intellij.lang.Language;
import com.intellij.psi.templateLanguages.TemplateLanguage;
import consulo.lang.LanguageVersion;
import consulo.php.PhpLanguageLevel;

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

	@Nonnull
	@Override
	public LanguageVersion[] findVersions()
	{
		return PhpLanguageLevel.VALUES;
	}
}
