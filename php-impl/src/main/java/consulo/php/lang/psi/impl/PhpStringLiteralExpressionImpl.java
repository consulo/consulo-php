package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.LiteralTextEscaper;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author VISTALL
 * @since 2019-04-10
 */
public class PhpStringLiteralExpressionImpl extends PhpElementImpl implements StringLiteralExpression
{
	public PhpStringLiteralExpressionImpl(ASTNode node)
	{
		super(node);
	}

	@Nonnull
	@Override
	public TextRange getValueRange()
	{
		return getTextRange();
	}

	@Nonnull
	@Override
	public String getContents()
	{
		return getText();
	}

	@Override
	public boolean isHeredoc()
	{
		return false;
	}

	@Override
	public boolean isSingleQuote()
	{
		return false;
	}

	@Override
	public boolean isValidHost()
	{
		return true;
	}

	@Override
	public PsiLanguageInjectionHost updateText(@Nonnull String text)
	{
		return null;
	}

	@Nonnull
	@Override
	public LiteralTextEscaper<? extends PsiLanguageInjectionHost> createLiteralTextEscaper()
	{
		return new LiteralTextEscaper<PsiLanguageInjectionHost>(this)
		{
			@Override
			public boolean decode(@Nonnull TextRange rangeInsideHost, @Nonnull StringBuilder outChars)
			{
				return false;
			}

			@Override
			public int getOffsetInHost(int offsetInDecoded, @Nonnull TextRange rangeInsideHost)
			{
				return 0;
			}

			@Override
			public boolean isOneLine()
			{
				return false;
			}
		};
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitPhpStringLiteralExpression(this);
	}
}
