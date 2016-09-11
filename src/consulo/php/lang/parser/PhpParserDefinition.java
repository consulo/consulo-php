package consulo.php.lang.parser;

import consulo.php.PhpLanguageLevel;
import consulo.php.lang.lexer.PhpFlexAdapter;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.psi.PhpStubElements;
import consulo.php.lang.psi.impl.PhpFileImpl;
import org.jetbrains.annotations.NotNull;
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
	@NotNull
	public Lexer createLexer(@NotNull LanguageVersion languageVersion)
	{
		return new PhpFlexAdapter((PhpLanguageLevel) languageVersion);
	}

	@Override
	@NotNull
	public PsiParser createParser(@NotNull LanguageVersion languageVersion)
	{
		return new PhpPsiParser();
	}

	@Override
	@NotNull
	public IFileElementType getFileNodeType()
	{
		return PhpStubElements.FILE;
	}

	@Override
	@NotNull
	public TokenSet getWhitespaceTokens(@NotNull LanguageVersion languageVersion)
	{
		return TokenSet.create(PhpTokenTypes.WHITE_SPACE, PhpTokenTypes.DOC_WHITESPACE);
	}

	@Override
	@NotNull
	public TokenSet getCommentTokens(@NotNull LanguageVersion languageVersion)
	{
		return PhpTokenTypes.tsCOMMENTS;
	}

	@Override
	@NotNull
	public TokenSet getStringLiteralElements(@NotNull LanguageVersion languageVersion)
	{
		return PhpTokenTypes.tsSTRINGS;
	}

	@Override
	@NotNull
	public PsiElement createElement(ASTNode node)
	{
		return PhpPsiCreator.create(node);
	}

	@Override
	public PsiFile createFile(FileViewProvider viewProvider)
	{
		return new PhpFileImpl(viewProvider);
	}

	@NotNull
	@Override
	public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right)
	{
		return SpaceRequirements.MAY;
	}
}
