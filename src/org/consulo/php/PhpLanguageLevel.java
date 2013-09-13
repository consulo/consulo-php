package org.consulo.php;

import com.intellij.lang.Language;
import com.intellij.lang.LanguageVersion;
import org.consulo.annotations.Immutable;
import org.consulo.php.lang.PHPLanguage;
import org.consulo.util.pointers.Named;
import org.consulo.util.pointers.NamedPointer;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 07.07.13.
 */
public enum PhpLanguageLevel implements LanguageVersion, Named, NamedPointer<PhpLanguageLevel> {
	PHP_5_0("5.0", ""),
	PHP_5_3("5.3", "namespaces, etc"), // namespace, use
	PHP_5_4("5.4", "traits, etc"); // trait

	public static final PhpLanguageLevel HIGHEST = PHP_5_0;
	@Immutable
	public static final PhpLanguageLevel[] VALUES = values();

	private final String myShortName;
	private final String myDescription;

	PhpLanguageLevel(String shortName, String description) {
		myShortName = shortName;
		myDescription = description;
	}

	public boolean isAtLeast(PhpLanguageLevel languageLevel) {
		return ordinal() >= languageLevel.ordinal();
	}

	@Override
	public PhpLanguageLevel get() {
		return this;
	}

	@NotNull
	@Override
	public String getName() {
		return name();
	}

	@Override
	public Language getLanguage() {
		return PHPLanguage.INSTANCE;
	}

	@NotNull
	public String getDescription() {
		return myDescription;
	}

	@NotNull
	public String getShortName() {
		return myShortName;
	}
}
