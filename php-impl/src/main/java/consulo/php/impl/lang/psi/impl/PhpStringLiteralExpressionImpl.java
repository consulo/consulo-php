package consulo.php.impl.lang.psi.impl;

import jakarta.annotation.Nonnull;

import consulo.document.util.TextRange;
import consulo.language.psi.PsiLanguageInjectionHost;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import consulo.language.ast.ASTNode;
import consulo.language.psi.LiteralTextEscaper;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;

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
