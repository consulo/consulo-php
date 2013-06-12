package net.jay.plugins.php.lang.documentation.phpdoc.parser;

import net.jay.plugins.php.lang.PHPFileType;
import net.jay.plugins.php.lang.documentation.phpdoc.lexer.PhpDocLexer;
import net.jay.plugins.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes;
import net.jay.plugins.php.lang.documentation.phpdoc.psi.PhpDocElementType;

import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilderFactory;
import com.intellij.lang.PsiParser;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.ILazyParseableElementType;

/**
 * @author jay
 * @date Jun 26, 2008 10:12:07 PM
 */
public interface PhpDocElementTypes extends PhpDocTokenTypes
{

	final public ILazyParseableElementType DOC_COMMENT = new ILazyParseableElementType("PhpDocComment")
	{
		@NotNull
		public Language getLanguage()
		{
			return PHPFileType.PHP.getLanguage();
		}

		public ASTNode parseContents(ASTNode chameleon)
		{
			PsiBuilderFactory factory = PsiBuilderFactory.getInstance();
			final PsiElement parentElement = chameleon.getTreeParent().getPsi();

			final PsiBuilder builder = factory.createBuilder(parentElement.getProject(), chameleon, new PhpDocLexer(), getLanguage(), Language.UNKNOWN_VERSION, chameleon.getText());
			final PsiParser parser = new PhpDocParser();
			return parser.parse(this, builder, Language.UNKNOWN_VERSION).getFirstChildNode();
		}
	};

	final public PhpDocElementType phpDocText = new PhpDocElementType("PhpDocText");
	final public PhpDocElementType phpDocTag = new PhpDocElementType("PhpDocTag");
	final public PhpDocElementType phpDocInlineTag = new PhpDocElementType("PhpDocInlineTag");
	final public PhpDocElementType phpDocTagValue = new PhpDocElementType("PhpDocTagValue");
	final public PhpDocElementType phpDocType = new PhpDocElementType("PhpDocType");
	final public PhpDocElementType phpDocVariable = new PhpDocElementType("PhpDocVariable");

}
