package consulo.php.impl.lang.parser.util;

import javax.annotation.Nonnull;

import consulo.language.parser.PsiBuilder;
import consulo.language.parser.PsiBuilderAdapter;
import consulo.language.ast.IElementType;
import consulo.language.ast.TokenSet;

/**
 * @author markov
 * @since 13.10.2007
 */
public class PhpPsiBuilder extends PsiBuilderAdapter
{
	public PhpPsiBuilder(@Nonnull PsiBuilder builder)
	{
		super(builder);
	}

	public boolean compare(final IElementType type)
	{
		return getTokenType() == type;
	}

	public boolean compare(final TokenSet types)
	{
		return types.contains(getTokenType());
	}

	public boolean compareAndEat(final IElementType type)
	{
		boolean found = compare(type);
		if(found)
		{
			advanceLexer();
		}
		return found;
	}

	public boolean compareAndEat(final TokenSet types)
	{
		boolean found = compare(types);
		if(found)
		{
			advanceLexer();
		}
		return found;
	}

	public void match(final IElementType token)
	{
		match(token, PhpParserErrors.expected(token));
	}

	public void match(final IElementType token, final String errorMessage)
	{
		if(!compareAndEat(token))
		{
			error(errorMessage);
		}
	}

	public void match(final TokenSet tokens)
	{
		match(tokens, PhpParserErrors.expected(tokens));
	}

	public void match(final TokenSet tokens, final String errorMessage)
	{
		if(!compareAndEat(tokens))
		{
			error(errorMessage);
		}
	}

	public void eatAsErrorUntil(IElementType elementType)
	{
		while(!eof())
		{
			IElementType tokenType = getTokenType();
			if(tokenType  == elementType)
			{
				break;
			}
			else
			{
				PsiBuilder.Marker mark = mark();
				advanceLexer();
				mark.error("Unexpected token");
			}
		}
	}
}
