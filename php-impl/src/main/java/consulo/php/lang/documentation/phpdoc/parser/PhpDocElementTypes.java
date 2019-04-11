package consulo.php.lang.documentation.phpdoc.parser;

import javax.annotation.Nonnull;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilderFactory;
import com.intellij.lang.PsiParser;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.ILazyParseableElementType;
import consulo.lang.LanguageVersion;
import consulo.lang.util.LanguageVersionUtil;
import com.jetbrains.php.lang.PhpFileType;
import consulo.php.lang.documentation.phpdoc.lexer.PhpDocLexer;
import consulo.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes;
import consulo.php.lang.documentation.phpdoc.psi.PhpDocElementType;

/**
 * @author jay
 * @date Jun 26, 2008 10:12:07 PM
 */
public interface PhpDocElementTypes extends PhpDocTokenTypes
{

	final public ILazyParseableElementType DOC_COMMENT = new ILazyParseableElementType("PhpDocComment")
	{
		@Override
		@Nonnull
		public Language getLanguage()
		{
			return PhpFileType.INSTANCE.getLanguage();
		}

		@Override
		public ASTNode parseContents(ASTNode chameleon)
		{
			PsiBuilderFactory factory = PsiBuilderFactory.getInstance();
			final PsiElement parentElement = chameleon.getTreeParent().getPsi();

			LanguageVersion defaultVersion = LanguageVersionUtil.findDefaultVersion(getLanguage());
			final PsiBuilder builder = factory.createBuilder(parentElement.getProject(), chameleon, new PhpDocLexer(), getLanguage(), defaultVersion, chameleon.getText());
			final PsiParser parser = new PhpDocParser();
			return parser.parse(this, builder, defaultVersion).getFirstChildNode();
		}
	};

	final public PhpDocElementType phpDocText = new PhpDocElementType("PhpDocText");
	final public PhpDocElementType phpDocTag = new PhpDocElementType("PhpDocTag");
	final public PhpDocElementType phpDocInlineTag = new PhpDocElementType("PhpDocInlineTag");
	final public PhpDocElementType phpDocTagValue = new PhpDocElementType("PhpDocTagValue");
	final public PhpDocElementType phpDocType = new PhpDocElementType("PhpDocType");
	final public PhpDocElementType phpDocVariable = new PhpDocElementType("PhpDocVariable");

}
