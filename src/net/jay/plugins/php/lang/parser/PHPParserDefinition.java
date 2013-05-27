package net.jay.plugins.php.lang.parser;

import net.jay.plugins.php.lang.lexer.PHPFlexAdapter;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.psi.PHPFile;

import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;

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
	public Lexer createLexer(Project project, Module module)
	{
		return new PHPFlexAdapter();
	}

	public PsiParser createParser(Project project)
	{
		return new PHPPsiParser();
	}

	@NotNull
	public IFileElementType getFileNodeType()
	{
		return PHPElementTypes.FILE;
	}

	@NotNull
	public TokenSet getWhitespaceTokens()
	{
		return TokenSet.create(PHPTokenTypes.WHITE_SPACE, PHPTokenTypes.DOC_WHITESPACE);
	}

	@NotNull
	public TokenSet getCommentTokens()
	{
		return PHPTokenTypes.tsCOMMENTS;
	}

	@NotNull
	public TokenSet getStringLiteralElements()
	{
		return PHPTokenTypes.tsSTRINGS;
	}

	@NotNull
	public PsiElement createElement(ASTNode node)
	{
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
