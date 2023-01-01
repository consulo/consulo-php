package consulo.php.impl.lang.documentation.phpdoc.parser;

import javax.annotation.Nonnull;

import consulo.language.parser.PsiBuilder;
import consulo.language.parser.PsiBuilderFactory;
import consulo.language.ast.ILazyParseableElementType;
import com.jetbrains.php.lang.PhpFileType;
import consulo.language.Language;
import consulo.language.ast.ASTNode;
import consulo.language.parser.PsiParser;
import consulo.language.psi.PsiElement;
import consulo.language.version.LanguageVersion;
import consulo.language.version.LanguageVersionUtil;
import consulo.php.impl.lang.documentation.phpdoc.lexer.PhpDocLexer;
import consulo.php.impl.lang.documentation.phpdoc.lexer.PhpDocTokenTypes;
import consulo.php.impl.lang.documentation.phpdoc.psi.PhpDocElementType;

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
