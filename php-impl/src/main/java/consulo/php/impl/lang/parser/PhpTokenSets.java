package consulo.php.impl.lang.parser;

import consulo.language.ast.TokenSet;
import consulo.php.impl.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import consulo.php.lang.lexer.PhpTokenTypes;

/**
 * @author VISTALL
 * @since 2019-03-27
 */
public interface PhpTokenSets extends PhpTokenTypes, PhpDocElementTypes
{
	TokenSet tsCOMMENTS = TokenSet.create(LINE_COMMENT, PhpDocElementTypes.DOC_COMMENT, C_STYLE_COMMENT);
}
