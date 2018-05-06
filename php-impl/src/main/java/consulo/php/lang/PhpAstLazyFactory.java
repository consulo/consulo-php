package consulo.php.lang;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.intellij.psi.impl.source.tree.LazyParseableElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.ILazyParseableElementType;
import consulo.php.lang.parser.PhpElementTypes;
import consulo.php.lang.psi.impl.PhpGroupStatementImpl;
import consulo.psi.tree.ASTLazyFactory;

/**
 * @author VISTALL
 * @since 29.10.13.
 */
public class PhpAstLazyFactory implements ASTLazyFactory
{
	@Nonnull
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
