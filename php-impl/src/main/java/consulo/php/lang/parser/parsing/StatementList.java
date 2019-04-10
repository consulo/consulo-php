package consulo.php.lang.parser.parsing;

import javax.annotation.Nullable;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.parser.PhpElementTypes;
import consulo.php.lang.parser.parsing.classes.ClassConstant;
import consulo.php.lang.parser.parsing.classes.ClassDeclaration;
import consulo.php.lang.parser.parsing.classes.ClassReference;
import consulo.php.lang.parser.parsing.functions.Function;
import consulo.php.lang.parser.util.PhpParserErrors;
import consulo.php.lang.parser.util.PhpPsiBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 12.10.2007
 * Time: 10:31:27
 */
public class StatementList implements PhpTokenTypes
{

	//	statement_list:
	//		statement_list top_statement
	//		| /* empty */
	//	;
	//
	//	top_statement:
	//		statement
	//		| function_declaration_statement
	//		| class_declaration_statement
	//	;
	public static void parse(PhpPsiBuilder builder, IElementType... endDelimeters)
	{
		parse(builder, TokenSet.create(endDelimeters));
	}

	private static void parse(PhpPsiBuilder builder, TokenSet whereToStop)
	{
		PsiBuilder.Marker statementList = builder.mark();

		if(!whereToStop.contains(builder.getTokenType()))
		{
			int previous;
			while(true)
			{
				previous = builder.getCurrentOffset();
				if(!parseTopStatement(builder))
				{
					final IElementType tokenType = builder.getTokenType();
					if(tokenType != null)
					{
						builder.error("Unexpected token: " + tokenType);
						builder.advanceLexer();
					}
				}

				if(builder.eof() || whereToStop.contains(builder.getTokenType()))
				{
					break;
				}
				if(previous == builder.getCurrentOffset())
				{
					builder.error(PhpParserErrors.unexpected(builder.getTokenType()));
					builder.advanceLexer();
				}
			}
		}

		statementList.done(PhpElementTypes.GROUP_STATEMENT);
	}

	private static boolean parseTopStatement(PhpPsiBuilder builder)
	{
		if(builder.compare(PhpTokenTypes.PHP_OPENING_TAG))
		{
			builder.advanceLexer();
		}
		else if(builder.compare(PhpTokenTypes.PHP_ECHO_OPENING_TAG))
		{
			builder.advanceLexer();
		}

		if(builder.getTokenType() == NAMESPACE_KEYWORD)
		{
			parseNamespaceStatement(builder);
		}

		while(builder.getTokenType() == USE_KEYWORD)
		{
			parseUseStatement(builder);
		}

		IElementType parsed = Function.parse(builder);

		if(parsed == PhpElementTypes.EMPTY_INPUT)
		{
			parsed = ClassConstant.parse(builder);
		}

		if(parsed == PhpElementTypes.EMPTY_INPUT)
		{
			parsed = ClassDeclaration.parse(builder);
		}

		if(parsed == PhpElementTypes.EMPTY_INPUT)
		{
			parsed = Statement.parse(builder);
		}

		if(builder.compare(PhpTokenTypes.PHP_CLOSING_TAG))
		{
			builder.advanceLexer();
		}
		return parsed != PhpElementTypes.EMPTY_INPUT;
	}

	private static void parseNamespaceStatement(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();

		builder.match(NAMESPACE_KEYWORD);

		PsiBuilder.Marker identifierNameMarker = builder.mark();
		if(builder.getTokenType() == IDENTIFIER)
		{
			boolean failed = false;

			builder.advanceLexer();

			while(builder.getTokenType() == SLASH)
			{
				builder.advanceLexer();

				if(builder.getTokenType() == IDENTIFIER)
				{
					builder.advanceLexer();
				}
				else
				{
					identifierNameMarker.error("Namespace name expected");
					failed = true;
					break;
				}
			}

			if(!failed)
			{
				identifierNameMarker.collapse(IDENTIFIER);
			}
		}
		else
		{
			identifierNameMarker.error("Namespace name expected");
		}

		builder.match(opSEMICOLON);
		marker.done(PhpElementTypes.NAMESPACE_STATEMENT);
	}

	private static void parseUseStatement(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();

		builder.match(USE_KEYWORD);

		while(parseUse(builder) != null)
		{
			if(builder.getTokenType() == opCOMMA)
			{
				builder.advanceLexer();
			}
			else
			{
				break;
			}
		}

		builder.match(opSEMICOLON);
		marker.done(PhpElementTypes.USE_LIST);
	}

	@Nullable
	private static PsiBuilder.Marker parseUse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker mark = builder.mark();

		if(ClassReference.parseClassNameReference(builder, null, false, false, true) == null)
		{
			mark.error("Reference expected");
			return null;
		}
		else
		{
			if(builder.getTokenType() == kwAS)
			{
				builder.advanceLexer();
			}

			mark.done(PhpElementTypes.USE);
			return mark;
		}
	}
}
