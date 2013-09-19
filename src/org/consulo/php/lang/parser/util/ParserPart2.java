package org.consulo.php.lang.parser.util;

import com.intellij.lang.PsiBuilder;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public interface ParserPart2
{
	PsiBuilder.Marker parse(PhpPsiBuilder builder);
}
