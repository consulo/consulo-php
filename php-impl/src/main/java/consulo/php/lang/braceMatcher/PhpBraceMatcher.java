package consulo.php.lang.braceMatcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import consulo.php.lang.lexer.PhpTokenTypes;

/**
 * @author jay
 * @time 21.12.2007 18:36:34
 */
public class PhpBraceMatcher implements PairedBraceMatcher, PhpTokenTypes
{
	private static final BracePair[] ourPairs = new BracePair[]{
			new BracePair(LBRACE, RBRACE, true),
			new BracePair(PHP_OPENING_TAG, PHP_CLOSING_TAG, false),
			new BracePair(PHP_ECHO_OPENING_TAG, PHP_CLOSING_TAG, false),
			new BracePair(LBRACKET, RBRACKET, false),
			new BracePair(LPAREN, RPAREN, false)
	};

	@Override
	public BracePair[] getPairs()
	{
		return ourPairs;
	}

	@Override
	public boolean isPairedBracesAllowedBeforeType(@Nonnull IElementType lbraceType, @Nullable IElementType contextType)
	{
		return true;
	}

	@Override
	public int getCodeConstructStart(PsiFile file, int openingBraceOffset)
	{
		return 0;
	}
}
