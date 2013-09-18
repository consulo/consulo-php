package org.consulo.php.completion.insert;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.editor.Editor;
import org.consulo.php.completion.PhpLookupItem;
import org.consulo.php.lang.psi.PhpFunction;
import org.consulo.php.lang.psi.PhpClass;
import org.consulo.php.lang.psi.PhpNamedElement;

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

	@Override
	protected PhpFunction getMethod(Editor editor, LookupElement element)
	{
		PhpLookupItem item = (PhpLookupItem) element.getObject();
		final PhpNamedElement psiElement = item.getLightElement();
		if(psiElement instanceof PhpClass)
		{
			return (PhpFunction) ((PhpClass) psiElement).getConstructor();
		}
		return null;
	}
}
