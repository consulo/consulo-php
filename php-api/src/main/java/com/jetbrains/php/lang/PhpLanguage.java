package com.jetbrains.php.lang;

import consulo.language.Language;
import consulo.language.template.TemplateLanguage;
import consulo.language.version.LanguageVersion;
import consulo.php.PhpLanguageLevel;

import jakarta.annotation.Nonnull;

/**
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
		super("PHP", "text/x-php", "text/php");
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
