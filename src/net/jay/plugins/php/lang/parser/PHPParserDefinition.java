package net.jay.plugins.php.lang.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LanguageVersion;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import net.jay.plugins.php.lang.lexer.PHPFlexAdapter;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.psi.PHPFile;
import org.consulo.php.PhpLanguageLevel;
import org.consulo.php.psi.PhpInstancableTokenType;
import org.consulo.php.psi.PhpStubElements;
import org.consulo.php.psi.impl.stub.elements.PhpStubElement;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 26.02.2007
 *
 * @author jay
 */
public class PHPParserDefinition implements ParserDefinition
{

	@NotNull
	public Lexer createLexer(Project project, @NotNull LanguageVersion languageVersion)
	{
		return new PHPFlexAdapter((PhpLanguageLevel) languageVersion);
	}

	@NotNull
	public PsiParser createParser(Project project, @NotNull LanguageVersion languageVersion)
	{
		return new PHPPsiParser();
	}

	@NotNull
	public IFileElementType getFileNodeType()
	{
		return PhpStubElements.FILE;
	}

	@NotNull
	public TokenSet getWhitespaceTokens(@NotNull LanguageVersion languageVersion)
	{
		return TokenSet.create(PHPTokenTypes.WHITE_SPACE, PHPTokenTypes.DOC_WHITESPACE);
	}

	@NotNull
	public TokenSet getCommentTokens(@NotNull LanguageVersion languageVersion)
	{
		return PHPTokenTypes.tsCOMMENTS;
	}

	@NotNull
	public TokenSet getStringLiteralElements(@NotNull LanguageVersion languageVersion)
	{
		return PHPTokenTypes.tsSTRINGS;
	}

	@NotNull
	public PsiElement createElement(ASTNode node)
	{
		IElementType elementType = node.getElementType();
		if(elementType instanceof PhpStubElement) {
			return ((PhpStubElement) elementType).createPsi(node);
		}
		else if(elementType instanceof PhpInstancableTokenType) {
			return ((PhpInstancableTokenType) elementType).createPsi(node);
		}
		return PHPPsiCreator.create(node);
	}

	public PsiFile createFile(FileViewProvider viewProvider)
	{
		return new PHPFile(viewProvider);
	}

	public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right)
	{
		return null;
	}
}
