package consulo.php.impl.lang.documentation.phpdoc.parser;

import javax.annotation.Nonnull;

import consulo.language.ast.ASTNode;
import consulo.language.parser.PsiBuilder;
import consulo.php.impl.lang.documentation.phpdoc.parser.tags.PhpDocDefaultTagParser;
import consulo.php.impl.lang.documentation.phpdoc.parser.tags.PhpDocReturnTagParser;
import consulo.php.impl.lang.documentation.phpdoc.parser.tags.PhpDocVarTagParser;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;
import consulo.language.version.LanguageVersion;
import consulo.language.parser.PsiParser;
import consulo.language.ast.IElementType;

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
