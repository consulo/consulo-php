package net.jay.plugins.php.lang.psi.elements;

import net.jay.plugins.php.cache.psi.LightPhpElement;

import org.jetbrains.annotations.NotNull;

/**
 * @author jay
 * @date Jun 5, 2008 4:30:43 PM
 */
public interface LightCopyContainer
{
	@NotNull
	LightPhpElement getLightCopy(LightPhpElement parent);
}
