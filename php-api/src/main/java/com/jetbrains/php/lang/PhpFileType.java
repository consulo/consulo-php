package com.jetbrains.php.lang;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NonNls;
import com.intellij.openapi.fileTypes.LanguageFileType;
import consulo.php.PhpBundle;
import consulo.php.PhpIcons;
import consulo.ui.image.Image;

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
	public static final
	@NonNls
	String[] EXTENTIONS = new String[]{
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
	@NonNls
	public String getId()
	{
		return NAME;
	}

	@Override
	@Nonnull
	public String getDescription()
	{
		return PhpBundle.message("filetype.description");
	}

	@Override
	@Nonnull
	@NonNls
	public String getDefaultExtension()
	{
		return DEFAULT_EXTENSION;
	}

	@Override
	@Nullable
	public Image getIcon()
	{
		return PhpIcons.Php;
	}
}
