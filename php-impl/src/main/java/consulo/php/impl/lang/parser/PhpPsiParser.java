package consulo.php.impl.lang.parser;

import javax.annotation.Nonnull;

import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.parser.PsiBuilder;
import consulo.application.ApplicationProperties;
import consulo.language.version.LanguageVersion;
import consulo.language.parser.PsiParser;
import consulo.php.impl.lang.parser.parsing.Program;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;

/**
 * User: jay
 * Date: 26.02.2007
 *
 * @author jay
 */
public class PhpPsiParser implements PsiParser
{
	@Override
	@Nonnull
	public ASTNode parse(@Nonnull IElementType root, @Nonnull PsiBuilder builder, @Nonnull LanguageVersion languageVersion)
	{
		builder.setDebugMode(ApplicationProperties.isInSandbox());

		PhpPsiBuilder psiBuilder = new PhpPsiBuilder(builder);

		PsiBuilder.Marker marker = psiBuilder.mark();
		Program.parse(psiBuilder);
		marker.done(root);
		return psiBuilder.getTreeBuilt();
	}
}
