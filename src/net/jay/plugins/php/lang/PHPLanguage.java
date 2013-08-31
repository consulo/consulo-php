package net.jay.plugins.php.lang;

import com.intellij.lang.Language;
import com.intellij.lang.LanguageVersion;
import com.intellij.psi.templateLanguages.TemplateLanguage;
import org.consulo.php.PhpLanguageLevel;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 22.02.2007
 *
 * @author jay
 */
public class PHPLanguage extends Language implements TemplateLanguage {
	public static final PHPLanguage INSTANCE = new PHPLanguage();

	public PHPLanguage() {
		super("PHP");
	}

	@Override
	public boolean isCaseSensitive() {
		return true;
	}

	@NotNull
	@Override
	public LanguageVersion[] findVersions() {
		return PhpLanguageLevel.VALUES;
	}
}
