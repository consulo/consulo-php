package org.consulo.php;

import org.consulo.php.lang.PhpLanguage;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.Language;
import consulo.annotations.Immutable;
import consulo.lang.LanguageVersion;
import consulo.util.pointers.Named;
import consulo.util.pointers.NamedPointer;

/**
 * @author VISTALL
 * @since 07.07.13.
 */
public enum PhpLanguageLevel implements LanguageVersion, Named, NamedPointer<PhpLanguageLevel>
{
	PHP_5_0("5.0", ""),
	PHP_5_3("5.3", ""),
	PHP_5_4("5.4", ""),
	PHP_5_5("5.5", ""),
	PHP_5_6("5.6", ""),
	PHP_7_0("7.0", "");

	public static final PhpLanguageLevel HIGHEST = PHP_5_6;
	@Immutable
	public static final PhpLanguageLevel[] VALUES = values();

	private final String myShortName;
	private final String myDescription;

	PhpLanguageLevel(String shortName, String description)
	{
		myShortName = shortName;
		myDescription = description;
	}

	public boolean isAtLeast(PhpLanguageLevel languageLevel)
	{
		return ordinal() >= languageLevel.ordinal();
	}

	@Override
	public PhpLanguageLevel get()
	{
		return this;
	}

	@NotNull
	@Override
	public String getName()
	{
		return name();
	}

	@Override
	public Language getLanguage()
	{
		return PhpLanguage.INSTANCE;
	}

	@NotNull
	public String getDescription()
	{
		return myDescription;
	}

	@NotNull
	public String getShortName()
	{
		return myShortName;
	}
}
