package org.consulo.php.lang.parser;

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
import org.consulo.php.lang.lexer.PhpFlexAdapter;
import org.consulo.php.lang.lexer.PhpTokenTypes;
import org.consulo.php.lang.psi.PhpFile;
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
public class PhpParserDefinition implements ParserDefinition
{

	@NotNull
	public Lexer createLexer(Project project, @NotNull LanguageVersion languageVersion)
	{
		return new PhpFlexAdapter((PhpLanguageLevel) languageVersion);
	}

	@NotNull
	public PsiParser createParser(Project project, @NotNull LanguageVersion languageVersion)
	{
		return new PhpPsiParser();
	}

	@NotNull
	public IFileElementType getFileNodeType()
	{
		return PhpStubElements.FILE;
	}

	@NotNull
	public TokenSet getWhitespaceTokens(@NotNull LanguageVersion languageVersion)
	{
		return TokenSet.create(PhpTokenTypes.WHITE_SPACE, PhpTokenTypes.DOC_WHITESPACE);
	}

	@NotNull
	public TokenSet getCommentTokens(@NotNull LanguageVersion languageVersion)
	{
		return PhpTokenTypes.tsCOMMENTS;
	}

	@NotNull
	public TokenSet getStringLiteralElements(@NotNull LanguageVersion languageVersion)
	{
		return PhpTokenTypes.tsSTRINGS;
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
		return PhpPsiCreator.create(node);
	}

	public PsiFile createFile(FileViewProvider viewProvider)
	{
		return new PhpFile(viewProvider);
	}

	public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right)
	{
		return null;
	}
}
