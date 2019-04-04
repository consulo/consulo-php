package consulo.php.lang.parser;

import com.intellij.psi.tree.TokenSet;
import consulo.php.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import consulo.php.lang.lexer.PhpTokenTypes;

/**
 * @author VISTALL
 * @since 2019-03-27
 */
public interface PhpTokenSets extends PhpTokenTypes, PhpDocElementTypes
{
	TokenSet tsCOMMENTS = TokenSet.create(LINE_COMMENT, PhpDocElementTypes.DOC_COMMENT, C_STYLE_COMMENT);
}
