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
import com.intellij.psi.tree.IElementTypeAsPsiFactory;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.consulo.php.PhpLanguageLevel;
import org.consulo.php.lang.lexer.PhpFlexAdapter;
import org.consulo.php.lang.lexer.PhpTokenTypes;
import org.consulo.php.lang.psi.PhpStubElements;
import org.consulo.php.lang.psi.impl.PhpFileImpl;
import org.consulo.php.lang.psi.impl.stub.elements.PhpStubElement;
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

	@Override
	@NotNull
	public Lexer createLexer(Project project, @NotNull LanguageVersion languageVersion)
	{
		return new PhpFlexAdapter((PhpLanguageLevel) languageVersion);
	}

	@Override
	@NotNull
	public PsiParser createParser(Project project, @NotNull LanguageVersion languageVersion)
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
		IElementType elementType = node.getElementType();
		if(elementType instanceof PhpStubElement) {
			return ((PhpStubElement) elementType).createPsi(node);
		}
		else if(elementType instanceof IElementTypeAsPsiFactory) {
			return ((IElementTypeAsPsiFactory) elementType).createElement(node);
		}
		return PhpPsiCreator.create(node);
	}

	@Override
	public PsiFile createFile(FileViewProvider viewProvider)
	{
		return new PhpFileImpl(viewProvider);
	}

	@Override
	public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right)
	{
		return null;
	}
}
