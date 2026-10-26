package consulo.php.impl.lang.psi.impl;

import org.jspecify.annotations.Nullable;
import consulo.language.psi.PsiElement;
import com.jetbrains.php.lang.lexer.PhpTokenTypes;
import consulo.language.ast.ASTNode;
import consulo.php.impl.lang.psi.PhpSelfAssignmentExpression;

/**
 * @author jay
 * @date Apr 4, 2008 2:03:58 PM
 */
public class PhpSelfAssignmentExpressionImpl extends PhpAssignmentExpressionImpl implements PhpSelfAssignmentExpression
{
	public PhpSelfAssignmentExpressionImpl(ASTNode node)
	{
		super(node);
	}

	@Nullable
	@Override
	public PsiElement getOperation()
	{
		return PhpPsiChildren.token(this, PhpTokenTypes.tsASGN_OPS);
	}
}
