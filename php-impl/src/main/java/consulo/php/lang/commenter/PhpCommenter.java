package consulo.php.lang.commenter;

import consulo.php.lang.lexer.PhpTokenTypes;
import javax.annotation.Nullable;
import com.intellij.lang.CodeDocumentationAwareCommenter;
import com.intellij.psi.PsiComment;
import com.intellij.psi.tree.IElementType;

/**
 * @author jay
 * @time 21.12.2007 18:41:02
 */
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
		return PhpTokenTypes.DOC_COMMENT;
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
}
