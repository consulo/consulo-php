package consulo.php;

import javax.annotation.Nonnull;
import consulo.annotations.Immutable;
import consulo.lang.LanguageVersion;
import com.jetbrains.php.lang.PhpLanguage;
import consulo.util.pointers.Named;
import consulo.util.pointers.NamedPointer;

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

	public static final PhpLanguageLevel HIGHEST = PHP_7_3;

	@Immutable
	public static final PhpLanguageLevel[] VALUES = new PhpLanguageLevel[]{
			PHP_5_0,
			PHP_5_3,
			PHP_5_4,
			PHP_5_5,
			PHP_5_6,
			PHP_7_0,
			PHP_7_1,
			PHP_7_2,
			PHP_7_3
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

	public boolean isAtLeast(PhpLanguageLevel languageLevel)
	{
		return ordinal() >= languageLevel.ordinal();
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
