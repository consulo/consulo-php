package com.jetbrains.php.lang.psi.elements.impl;

import consulo.php.impl.lang.psi.impl.PhpElementImpl;
import jakarta.annotation.Nonnull;

import consulo.document.util.TextRange;
import consulo.language.psi.ContributedReferenceHost;
import consulo.language.psi.PsiLanguageInjectionHost;
import consulo.language.psi.PsiReference;
import consulo.language.psi.ReferenceProvidersRegistry;
import jakarta.annotation.Nullable;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import consulo.language.ast.ASTNode;
import consulo.language.psi.LiteralTextEscaper;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;

/**
 * @author VISTALL
 * @since 2019-04-10
 */
public class StringLiteralExpressionImpl extends PhpElementImpl implements StringLiteralExpression, ContributedReferenceHost
{
	public StringLiteralExpressionImpl(ASTNode node)
	{
		super(node);
	}

	/**
	 * Range of the contents inside the quotes, relative to this element
	 */
	@Nonnull
	@Override
	public TextRange getValueRange()
	{
		String text = getText();
		int start = 0;
		if(text.length() > 1 && (text.charAt(0) == 'b' || text.charAt(0) == 'B') && isQuote(text.charAt(1)))
		{
			start = 1;
		}
		if(start >= text.length() || !isQuote(text.charAt(start)))
		{
			return new TextRange(0, text.length());
		}
		char quote = text.charAt(start);
		int end = text.length();
		if(end - start >= 2 && text.charAt(end - 1) == quote)
		{
			end--;
		}
		return new TextRange(start + 1, end);
	}

	private static boolean isQuote(char c)
	{
		return c == '\'' || c == '"';
	}

	@Nonnull
	@Override
	public String getContents()
	{
		return getValueRange().substring(getText());
	}

	@Override
	public boolean isHeredoc()
	{
		return false;
	}

	@Override
	public boolean isSingleQuote()
	{
		TextRange range = getValueRange();
		return range.getStartOffset() > 0 && getText().charAt(range.getStartOffset() - 1) == '\'';
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

	/**
	 * A string carries the references of the registered {@link consulo.language.psi.PsiReferenceContributor}s,
	 * like the route or the service names of the Symfony plugin
	 */
	@Nonnull
	@Override
	public PsiReference[] getReferences()
	{
		return ReferenceProvidersRegistry.getReferencesFromProviders(this);
	}

	@Nullable
	@Override
	public PsiReference getReference()
	{
		PsiReference[] references = getReferences();
		return references.length == 0 ? null : references[0];
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitPhpStringLiteralExpression(this);
	}
}
