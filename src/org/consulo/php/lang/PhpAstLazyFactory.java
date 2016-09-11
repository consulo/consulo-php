package org.consulo.php.lang;

import org.consulo.php.lang.parser.PhpElementTypes;
import org.consulo.php.lang.psi.impl.PhpGroupStatementImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.psi.impl.source.tree.LazyParseableElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.ILazyParseableElementType;
import consulo.psi.tree.ASTLazyFactory;

/**
 * @author VISTALL
 * @since 29.10.13.
 */
public class PhpAstLazyFactory implements ASTLazyFactory
{
	@NotNull
	@Override
	public LazyParseableElement createLazy(ILazyParseableElementType iLazyParseableElementType, CharSequence charSequence)
	{
		if(iLazyParseableElementType == PhpElementTypes.GROUP_STATEMENT)
		{
			return new PhpGroupStatementImpl(charSequence);
		}
		return null;
	}

	@Override
	public boolean apply(@Nullable IElementType elementType)
	{
		return elementType == PhpElementTypes.GROUP_STATEMENT;
	}
}
