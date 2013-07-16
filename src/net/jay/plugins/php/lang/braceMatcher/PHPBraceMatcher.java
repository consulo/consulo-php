package net.jay.plugins.php.lang.braceMatcher;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author jay
 * @time 21.12.2007 18:36:34
 */
public class PHPBraceMatcher implements PairedBraceMatcher, PHPTokenTypes {
	private static final BracePair[] ourPairs = new BracePair[] {
			new BracePair(chLBRACE, chRBRACE, true),
			new BracePair(PHP_OPENING_TAG, PHP_CLOSING_TAG, true),
			new BracePair(PHP_ECHO_OPENING_TAG, PHP_CLOSING_TAG, true),
			new BracePair(chLBRACKET, chRBRACKET, false),
			new BracePair(chLPAREN, chRPAREN, false)
	};

	public BracePair[] getPairs() {
		return ourPairs;
	}

	public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, @Nullable IElementType contextType) {
		return true;
	}

	public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
		return 0;
	}
}
