package net.jay.plugins.php.completion.insert;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.editor.Editor;
import net.jay.plugins.php.completion.PhpLookupItem;
import net.jay.plugins.php.lang.psi.elements.Function;
import net.jay.plugins.php.lang.psi.elements.PhpClass;
import net.jay.plugins.php.lang.psi.elements.PhpNamedElement;

/**
 * @author jay
 * @date Jun 24, 2008 7:34:13 PM
 */
public class PhpClassConstructorInsertHandler extends PhpMethodInsertHandler
{

	private static PhpClassConstructorInsertHandler instance = null;

	public static PhpClassConstructorInsertHandler getInstance()
	{
		if(instance == null)
		{
			instance = new PhpClassConstructorInsertHandler();
		}
		return instance;
	}

	protected Function getMethod(Editor editor, LookupElement element)
	{
		PhpLookupItem item = (PhpLookupItem) element.getObject();
		final PhpNamedElement psiElement = item.getLightElement();
		if(psiElement instanceof PhpClass)
		{
			return (Function) ((PhpClass) psiElement).getConstructor();
		}
		return null;
	}
}
