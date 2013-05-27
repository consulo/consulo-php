package net.jay.plugins.php.cache.psi;

/**
 * @author jay
 * @date Jun 6, 2008 12:25:52 PM
 */
abstract public class RecursiveLightPhpElementVisitor extends LightPhpElementVisitor
{
	public void visitElement(LightPhpElement element)
	{
		for(LightPhpElement phpElement : element.getChildren())
		{
			phpElement.accept(this);
		}
	}
}
