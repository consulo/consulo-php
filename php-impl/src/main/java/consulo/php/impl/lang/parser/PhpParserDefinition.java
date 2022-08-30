package consulo.php.impl.lang.parser;

import com.jetbrains.php.lang.PhpLanguage;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IFileElementType;
import consulo.language.ast.TokenSet;
import consulo.language.file.FileViewProvider;
import consulo.language.lexer.Lexer;
import consulo.language.parser.ParserDefinition;
import consulo.language.parser.PsiParser;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.version.LanguageVersion;
import consulo.php.PhpLanguageLevel;
import consulo.php.impl.lang.documentation.phpdoc.lexer.PhpDocTokenTypes;
import consulo.php.impl.lang.lexer.PhpFlexLexer;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.psi.PhpStubElements;
import consulo.php.impl.lang.psi.impl.PhpFileImpl;

import javax.annotation.Nonnull;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 26.02.2007
 *
 * @author jay
 */
@ExtensionImpl
public class PhpParserDefinition implements ParserDefinition
{
	@Nonnull
	@Override
	public Language getLanguage()
	{
		return PhpLanguage.INSTANCE;
	}

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
