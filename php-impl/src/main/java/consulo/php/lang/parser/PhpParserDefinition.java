package consulo.php.lang.parser;

import javax.annotation.Nonnull;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import consulo.lang.LanguageVersion;
import consulo.php.PhpLanguageLevel;
import consulo.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes;
import consulo.php.lang.lexer.PhpFlexLexer;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.psi.PhpStubElements;
import consulo.php.lang.psi.impl.PhpFileImpl;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 26.02.2007
 *
 * @author jay
 */
public class PhpParserDefinition implements ParserDefinition
{

	@Override
	@Nonnull
	public Lexer createLexer(@Nonnull LanguageVersion languageVersion)
	{
		return new PhpFlexLexer(false, (PhpLanguageLevel) languageVersion);
	}

	@Override
	@Nonnull
	public PsiParser createParser(@Nonnull LanguageVersion languageVersion)
	{
		return new PhpPsiParser();
	}

	@Override
	@Nonnull
	public IFileElementType getFileNodeType()
	{
		return PhpStubElements.FILE;
	}

	@Override
	@Nonnull
	public TokenSet getWhitespaceTokens(@Nonnull LanguageVersion languageVersion)
	{
		return TokenSet.create(PhpTokenTypes.WHITE_SPACE, PhpDocTokenTypes.DOC_WHITESPACE);
	}

	@Override
	@Nonnull
	public TokenSet getCommentTokens(@Nonnull LanguageVersion languageVersion)
	{
		return PhpTokenSets.tsCOMMENTS;
	}

	@Override
	@Nonnull
	public TokenSet getStringLiteralElements(@Nonnull LanguageVersion languageVersion)
	{
		return PhpTokenTypes.tsSTRINGS;
	}

	@Override
	@Nonnull
	public PsiElement createElement(ASTNode node)
	{
		return PhpPsiCreator.create(node);
	}

	@Override
	public PsiFile createFile(FileViewProvider viewProvider)
	{
		return new PhpFileImpl(viewProvider);
	}

	@Nonnull
	@Override
	public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right)
	{
		return SpaceRequirements.MAY;
	}
}
