package com.jetbrains.php.config;

import com.jetbrains.php.lang.PhpLanguage;
import consulo.component.util.pointer.Named;
import consulo.component.util.pointer.NamedPointer;
import consulo.language.version.LanguageVersion;
import consulo.module.Module;
import consulo.module.ModuleManager;
import consulo.php.module.extension.PhpModuleExtension;
import consulo.project.Project;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 07.07.13.
 */
public class PhpLanguageLevel extends LanguageVersion implements Named, NamedPointer<PhpLanguageLevel>
{
	public static final PhpLanguageLevel PHP_5_0 = new PhpLanguageLevel(0, "PHP_5_0", "5.0", "");
	public static final PhpLanguageLevel PHP_5_3 = new PhpLanguageLevel(1, "PHP_5_3", "5.3", "");
	public static final PhpLanguageLevel PHP_5_4 = new PhpLanguageLevel(2, "PHP_5_4", "5.4", "");
	public static final PhpLanguageLevel PHP_5_5 = new PhpLanguageLevel(3, "PHP_5_5", "5.5", "");
	public static final PhpLanguageLevel PHP_5_6 = new PhpLanguageLevel(4, "PHP_5_6", "5.6", "");
	public static final PhpLanguageLevel PHP_7_0 = new PhpLanguageLevel(5, "PHP_7_0", "7.0", "");
	public static final PhpLanguageLevel PHP_7_1 = new PhpLanguageLevel(6, "PHP_7_1", "7.1", "");
	public static final PhpLanguageLevel PHP_7_2 = new PhpLanguageLevel(7, "PHP_7_2", "7.2", "");
	public static final PhpLanguageLevel PHP_7_3 = new PhpLanguageLevel(8, "PHP_7_3", "7.3", "");
	public static final PhpLanguageLevel PHP_7_4 = new PhpLanguageLevel(9, "PHP_7_4", "7.4", "");
	public static final PhpLanguageLevel PHP_8_0 = new PhpLanguageLevel(10, "PHP_8_0", "8.0", "");
	public static final PhpLanguageLevel PHP_8_1 = new PhpLanguageLevel(11, "PHP_8_1", "8.1", "");
	public static final PhpLanguageLevel PHP_8_2 = new PhpLanguageLevel(12, "PHP_8_2", "8.2", "");
	public static final PhpLanguageLevel PHP_8_3 = new PhpLanguageLevel(13, "PHP_8_3", "8.3", "");
	public static final PhpLanguageLevel PHP_8_4 = new PhpLanguageLevel(14, "PHP_8_4", "8.4", "");
	public static final PhpLanguageLevel PHP_8_5 = new PhpLanguageLevel(15, "PHP_8_5", "8.5", "");

	public static final PhpLanguageLevel HIGHEST = PHP_8_5;

	/**
	 * Level of code without a PHP module, see {@link #current(Project)}
	 */
	public static final PhpLanguageLevel DEFAULT = HIGHEST;

	public static final PhpLanguageLevel[] VALUES = new PhpLanguageLevel[]{
			PHP_5_0,
			PHP_5_3,
			PHP_5_4,
			PHP_5_5,
			PHP_5_6,
			PHP_7_0,
			PHP_7_1,
			PHP_7_2,
			PHP_7_3,
			PHP_7_4,
			PHP_8_0,
			PHP_8_1,
			PHP_8_2,
			PHP_8_3,
			PHP_8_4,
			PHP_8_5
	};

	private final int myIndex;
	private final String myShortName;
	private final String myDescription;

	PhpLanguageLevel(int index, String id, String shortName, String description)
	{
		super(id, shortName, PhpLanguage.INSTANCE);
		myIndex = index;
		myShortName = shortName;
		myDescription = description;
	}

	/**
	 * Language level is per module: the project level is the highest level of its PHP modules
	 */
	@Nonnull
	public static PhpLanguageLevel current(@Nonnull Project project)
	{
		PhpLanguageLevel result = null;
		for(Module module : ModuleManager.getInstance(project).getModules())
		{
			PhpModuleExtension<?> extension = module.getExtension(PhpModuleExtension.class);
			if(extension != null && (result == null || extension.getLanguageLevel().isAtLeast(result)))
			{
				result = extension.getLanguageLevel();
			}
		}
		return result == null ? DEFAULT : result;
	}

	public boolean isAtLeast(PhpLanguageLevel languageLevel)
	{
		return ordinal() >= languageLevel.ordinal();
	}

	public int compareTo(@Nonnull PhpLanguageLevel languageLevel)
	{
		return Integer.compare(ordinal(), languageLevel.ordinal());
	}

	public boolean hasFeature(@Nonnull PhpLanguageFeature feature)
	{
		return isAtLeast(feature.getMinimumLevel());
	}

	public int ordinal()
	{
		return myIndex;
	}

	@Override
	public PhpLanguageLevel get()
	{
		return this;
	}

	@Nonnull
	public String getDescription()
	{
		return myDescription;
	}

	@Nonnull
	public String getShortName()
	{
		return myShortName;
	}
}
