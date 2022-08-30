package consulo.php.impl.lang.commenter;

import com.jetbrains.php.lang.PhpLanguage;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.CodeDocumentationAwareCommenter;
import consulo.language.Language;
import consulo.language.ast.IElementType;
import consulo.language.psi.PsiComment;
import consulo.php.impl.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import consulo.php.lang.lexer.PhpTokenTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author jay
 * @time 21.12.2007 18:41:02
 */
@ExtensionImpl
public class PhpCommenter implements CodeDocumentationAwareCommenter
{
	@Override
	@Nullable
	public String getLineCommentPrefix()
	{
		return "//";
	}

	@Override
	@Nullable
	public String getBlockCommentPrefix()
	{
		return "/*";
	}

	@Override
	@Nullable
	public String getBlockCommentSuffix()
	{
		return "*/";
	}

	@Nullable
	@Override
	public String getCommentedBlockCommentPrefix()
	{
		return null;
	}

	@Nullable
	@Override
	public String getCommentedBlockCommentSuffix()
	{
		return null;
	}

	@Override
	@Nullable
	public IElementType getLineCommentTokenType()
	{
		return PhpTokenTypes.LINE_COMMENT;
	}

	@Override
	@Nullable
	public IElementType getBlockCommentTokenType()
	{
		return PhpTokenTypes.C_STYLE_COMMENT;
	}

	@Override
	@Nullable
	public IElementType getDocumentationCommentTokenType()
	{
		return PhpDocElementTypes.DOC_COMMENT;
	}

	@Override
	@Nullable
	public String getDocumentationCommentPrefix()
	{
		return "/**";
	}

	@Override
	@Nullable
	public String getDocumentationCommentLinePrefix()
	{
		return "*";
	}

	@Override
	@Nullable
	public String getDocumentationCommentSuffix()
	{
		return " */";
	}

	@Override
	public boolean isDocumentationComment(PsiComment element)
	{
		return element.getText().startsWith(getDocumentationCommentPrefix());
	}

	@Nonnull
	@Override
	public Language getLanguage()
	{
		return PhpLanguage.INSTANCE;
	}
}
