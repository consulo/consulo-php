package consulo.php.lang.psi;

import javax.annotation.Nonnull;
import com.intellij.navigation.NavigationItem;

/**
 * @author jay
 * @date Apr 8, 2008 1:53:42 PM
 */
public interface PhpClass extends PhpModifierListOwner, NavigationItem, PhpBraceOwner
{
	PhpClass[] EMPTY_ARRAY = new PhpClass[0];

	static String CONSTRUCTOR = "__construct";

	String getNamespace();

	@Nonnull
	PhpFunction[] getFunctions();

	@Nonnull
	PhpField[] getFields();

	PhpClass getSuperClass();

	PhpClass[] getImplementedInterfaces();

	PhpFunction getConstructor();

	boolean isInterface();

	boolean isTrait();
}
