package com.jetbrains.php.lang;

import consulo.language.file.LanguageFileType;
import consulo.localize.LocalizeValue;
import consulo.php.icon.PhpIconGroup;
import consulo.php.localize.PhpLocalize;
import consulo.ui.image.Image;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 22.02.2007
 *
 * @author jay
 */
public class PhpFileType extends LanguageFileType
{
	public static final PhpFileType INSTANCE = new PhpFileType();

	public static final String DEFAULT_EXTENSION = "php";
	public static final String NAME = "PHP";
	public static final String[] EXTENTIONS = new String[]{
			DEFAULT_EXTENSION,
			"inc",
			"phtml",
			"php3"
	};

	private PhpFileType()
	{
		super(PhpLanguage.INSTANCE);
	}

	@Override
	@Nonnull
	public String getId()
	{
		return NAME;
	}

	@Override
	@Nonnull
	public LocalizeValue getDescription()
	{
		return PhpLocalize.filetypeDescription();
	}

	@Override
	@Nonnull
	public String getDefaultExtension()
	{
		return DEFAULT_EXTENSION;
	}

	@Override
	@Nullable
	public Image getIcon()
	{
		return PhpIconGroup.filetypesPhp();
	}
}
