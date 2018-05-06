package consulo.php.lang.documentation.phpdoc.parser;

import javax.annotation.Nonnull;

import consulo.php.lang.documentation.phpdoc.parser.tags.PhpDocDefaultTagParser;
import consulo.php.lang.documentation.phpdoc.parser.tags.PhpDocReturnTagParser;
import consulo.php.lang.documentation.phpdoc.parser.tags.PhpDocVarTagParser;
import consulo.php.lang.parser.util.PhpPsiBuilder;
import com.intellij.lang.ASTNode;
import consulo.lang.LanguageVersion;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;

/**
 * @author jay
 * @date Jun 28, 2008 4:38:44 PM
 */
public class PhpDocParser implements PsiParser
{

	public PhpDocParser()
	{
		PhpDocDefaultTagParser.register();
		PhpDocVarTagParser.register();
		PhpDocReturnTagParser.register();
	}

	@Override
	@Nonnull
	public ASTNode parse(IElementType root, PsiBuilder builder, LanguageVersion languageVersion)
	{
		PhpPsiBuilder phpBuilder = new PhpPsiBuilder(builder);
		PsiBuilder.Marker rootMarker = phpBuilder.mark();
		new PhpDocParsing().parse(phpBuilder);
		rootMarker.done(root);
		return phpBuilder.getTreeBuilt();
	}
}
