/*
 * Copyright 2013-2026 consulo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package consulo.php.impl.lang.documentation.phpdoc.parser;

import consulo.language.ast.IElementType;
import consulo.language.ast.TokenSet;
import consulo.language.ast.TokenType;
import consulo.language.parser.PsiBuilder;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;

import java.util.Locale;
import java.util.Set;

/**
 * PHPDoc comment: text, tags with typed values ({@code @param}, {@code @var}, ...) and annotations with attribute lists.
 * <p>
 * Tree shapes follow PhpStorm: {@code phpDocTag(DOC_TAG_NAME phpDocAttributeList(DOC_LPAREN ... phpDocString(DOC_STRING) ... DOC_RPAREN))}.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public final class PhpDocParsing implements PhpDocElementTypes
{
	private static final Set<String> TYPED_TAGS = Set.of("@param", "@var", "@return", "@throws", "@property", "@property-read", "@property-write",
			"@psalm-param", "@psalm-var", "@psalm-return", "@phpstan-param", "@phpstan-var", "@phpstan-return");

	private static final TokenSet OPENING = TokenSet.create(DOC_LAB, DOC_LBRACE, DOC_LPAREN, DOC_LBRACKET);
	private static final TokenSet CLOSING = TokenSet.create(DOC_RAB, DOC_RBRACE, DOC_RPAREN, DOC_RBRACKET);

	private PhpDocParsing()
	{
	}

	public static void parse(PhpPsiBuilder builder)
	{
		builder.match(DOC_COMMENT_START);
		while(!builder.eof() && !builder.compare(DOC_COMMENT_END))
		{
			if(builder.compare(DOC_TAG_NAME))
			{
				parseTag(builder, false);
			}
			else
			{
				builder.advanceLexer();
			}
		}
		builder.match(DOC_COMMENT_END);
	}

	/**
	 * @param nested annotation inside an attribute list: only the name and its own attribute list
	 */
	private static void parseTag(PhpPsiBuilder builder, boolean nested)
	{
		PsiBuilder.Marker tag = builder.mark();
		String name = builder.getTokenText();
		builder.advanceLexer();

		if(builder.compare(DOC_LPAREN) && isAnnotationArguments(builder, name))
		{
			parseAttributeList(builder);
		}
		else if(!nested && name != null && TYPED_TAGS.contains(name.toLowerCase(Locale.ROOT)))
		{
			parseTypedValue(builder, name.toLowerCase(Locale.ROOT));
		}
		else if(!nested && name != null && isTypeAliasTag(name.toLowerCase(Locale.ROOT)))
		{
			parseTypeAlias(builder, name.toLowerCase(Locale.ROOT).endsWith("-import-type"));
		}

		if(!nested)
		{
			parseTagValue(builder);
		}
		tag.done(phpDocTag);
	}

	/**
	 * {@code @Foo(} - adjacent, or an annotation-like name ({@code @ORM\\Column (}, {@code @Route (})
	 */
	private static boolean isAnnotationArguments(PhpPsiBuilder builder, String name)
	{
		if(builder.rawLookup(-1) != TokenType.WHITE_SPACE)
		{
			return true;
		}
		return name != null && name.length() > 1 && (Character.isUpperCase(name.charAt(1)) || name.indexOf('\\') >= 0);
	}

	/**
	 * Description up to the next tag at the start of a line
	 */
	private static void parseTagValue(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker value = builder.mark();
		boolean empty = true;
		while(!builder.eof() && !builder.compare(DOC_COMMENT_END) && !(builder.compare(DOC_TAG_NAME) && isAtLineStart(builder)))
		{
			// the asterisk of the next tag's line belongs to the comment, not to this tag
			if(builder.compare(DOC_LEADING_ASTERISK) && (builder.lookAhead(1) == DOC_TAG_NAME || builder.lookAhead(1) == DOC_COMMENT_END
					|| builder.lookAhead(1) == null))
			{
				break;
			}
			builder.advanceLexer();
			empty = false;
		}
		if(empty)
		{
			value.drop();
		}
		else
		{
			value.done(phpDocTagValue);
		}
	}

	private static boolean isAtLineStart(PhpPsiBuilder builder)
	{
		for(int i = -1; ; i--)
		{
			IElementType previous = builder.rawLookup(i);
			if(previous == TokenType.WHITE_SPACE)
			{
				continue;
			}
			return previous == null || previous == DOC_LEADING_ASTERISK || previous == DOC_COMMENT_START;
		}
	}

	/**
	 * {@code (name="x", {@Nested}, {"a", "b"}, Foo::BAR)}
	 */
	private static void parseAttributeList(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker list = builder.mark();
		builder.advanceLexer();
		int depth = 1;
		while(!builder.eof() && !builder.compare(DOC_COMMENT_END))
		{
			if(builder.compare(DOC_RPAREN))
			{
				builder.advanceLexer();
				if(--depth == 0)
				{
					break;
				}
			}
			else if(builder.compare(DOC_LPAREN))
			{
				depth++;
				builder.advanceLexer();
			}
			else if(builder.compare(DOC_TAG_NAME))
			{
				parseTag(builder, true);
			}
			else if(builder.compare(DOC_STRING))
			{
				PsiBuilder.Marker string = builder.mark();
				builder.advanceLexer();
				string.done(phpDocString);
			}
			else
			{
				builder.advanceLexer();
			}
		}
		list.done(phpDocAttributeList);
	}

	/**
	 * {@code @param Type $name}, {@code @param $name}, {@code @var Type $name}, {@code @var $name Type}, {@code @return Type}
	 */
	private static void parseTypedValue(PhpPsiBuilder builder, String name)
	{
		boolean hasVariable = name.endsWith("param") || name.endsWith("var") || name.startsWith("@property");
		if(hasVariable && parseVariable(builder))
		{
			// the legacy {@code @var $name Type}; after {@code @param $name} is the description
			if(name.endsWith("var") && isTypeStart(builder))
			{
				parseTypes(builder);
			}
			return;
		}
		if(!isTypeStart(builder))
		{
			return;
		}
		parseTypes(builder);
		if(hasVariable)
		{
			parseVariable(builder);
		}
	}

	/**
	 * {@code @phpstan-type Name Type}, {@code @psalm-type Name = Type}, {@code @psalm-import-type Name from Class as Alias}
	 */
	private static boolean isTypeAliasTag(String name)
	{
		return (name.startsWith("@phpstan-") || name.startsWith("@psalm-") || name.startsWith("@phan-"))
			&& (name.endsWith("-type") && !name.endsWith("-return-type") && !name.endsWith("-var-type"));
	}

	private static void parseTypeAlias(PhpPsiBuilder builder, boolean importTag)
	{
		if(!builder.compareAndEat(DOC_IDENTIFIER))
		{
			return;
		}
		if(importTag)
		{
			// Name from Class [as Alias]: the class is the type of the tag
			if(builder.compare(DOC_IDENTIFIER) && "from".equals(builder.getTokenText()))
			{
				builder.advanceLexer();
				if(isTypeStart(builder))
				{
					parseTypes(builder);
				}
			}
			return;
		}
		if(builder.compare(DOC_TEXT) && "=".equals(builder.getTokenText()))
		{
			builder.advanceLexer();
		}
		if(isTypeStart(builder))
		{
			parseTypes(builder);
		}
	}

	private static boolean parseVariable(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker variable = builder.mark();
		if(builder.compare(DOC_AMPERSAND))
		{
			builder.advanceLexer();
		}
		if(builder.compare(DOC_DOT) || builder.compare(DOC_TEXT) && "...".equals(builder.getTokenText()))
		{
			builder.advanceLexer();
		}
		if(!builder.compareAndEat(DOC_VARIABLE))
		{
			variable.rollbackTo();
			return false;
		}
		variable.done(phpDocVariable);
		return true;
	}

	private static boolean isTypeStart(PhpPsiBuilder builder)
	{
		IElementType type = builder.getTokenType();
		if(type == DOC_IDENTIFIER || type == DOC_NAMESPACE || type == DOC_LPAREN || type == DOC_STRING || type == DOC_INTEGER)
		{
			return true;
		}
		return type == DOC_TEXT && "?".equals(builder.getTokenText());
	}

	/**
	 * {@code ?Foo|Bar[]|array<int, Baz>|(A&B)} - one {@code phpDocType} per union member
	 */
	private static void parseTypes(PhpPsiBuilder builder)
	{
		while(true)
		{
			PsiBuilder.Marker type = builder.mark();
			parseTypePart(builder);
			type.done(phpDocType);
			if(builder.compare(DOC_PIPE) && !isWhitespaceBefore(builder))
			{
				builder.advanceLexer();
				if(isTypeStart(builder))
				{
					continue;
				}
			}
			return;
		}
	}

	private static void parseTypePart(PhpPsiBuilder builder)
	{
		int depth = 0;
		boolean first = true;
		while(!builder.eof() && !builder.compare(DOC_COMMENT_END))
		{
			if(!first && depth == 0 && (isWhitespaceBefore(builder) || builder.compare(DOC_PIPE)))
			{
				return;
			}
			if(builder.compare(OPENING))
			{
				depth++;
			}
			else if(builder.compare(CLOSING))
			{
				if(depth == 0)
				{
					return;
				}
				depth--;
			}
			builder.advanceLexer();
			first = false;
		}
	}

	private static boolean isWhitespaceBefore(PhpPsiBuilder builder)
	{
		IElementType previous = builder.rawLookup(-1);
		return previous == TokenType.WHITE_SPACE || previous == DOC_LEADING_ASTERISK;
	}
}
