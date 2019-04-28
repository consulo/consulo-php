package consulo.php.lang.parser;

import javax.annotation.Nonnull;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import consulo.application.ApplicationProperties;
import consulo.lang.LanguageVersion;
import consulo.php.lang.parser.parsing.Program;
import consulo.php.lang.parser.util.PhpPsiBuilder;

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
