package org.consulo.php.lang.parser;

import org.consulo.php.lang.parser.parsing.Program;
import org.consulo.php.lang.parser.util.PhpPsiBuilder;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.lang.LanguageVersion;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 26.02.2007
 *
 * @author jay
 */
public class PhpPsiParser implements PsiParser
{

	@Override
	@NotNull
	public ASTNode parse(IElementType root, PsiBuilder builder, LanguageVersion languageVersion)
	{
		builder.setDebugMode(true);
		PhpPsiBuilder psiBuilder = new PhpPsiBuilder(builder);

		PsiBuilder.Marker marker = psiBuilder.mark();
		//    long startTime = System.currentTimeMillis();
		Program.parse(psiBuilder);
		//    System.out.println("parsing time: " + (System.currentTimeMillis() - startTime));
		marker.done(root);
		return psiBuilder.getTreeBuilt();
	}
}
