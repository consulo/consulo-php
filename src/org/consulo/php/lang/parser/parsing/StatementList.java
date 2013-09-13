package org.consulo.php.lang.parser.parsing;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PHPElementTypes;
import org.consulo.php.lang.parser.parsing.classes.ClassDeclaration;
import org.consulo.php.lang.parser.parsing.functions.Function;
import org.consulo.php.lang.parser.util.PHPParserErrors;
import org.consulo.php.lang.parser.util.PHPPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 12.10.2007
 * Time: 10:31:27
 */
public class StatementList
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
	public static void parse(PHPPsiBuilder builder, IElementType... endDelimeters)
	{
		parse(builder, TokenSet.create(endDelimeters));
	}

	private static void parse(PHPPsiBuilder builder, TokenSet whereToStop)
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
					builder.error(PHPParserErrors.unexpected(builder.getTokenType()));
					builder.advanceLexer();
				}
			}
		}

		statementList.done(PHPElementTypes.GROUP_STATEMENT);
	}

	private static boolean parseTopStatement(PHPPsiBuilder builder)
	{
		if(builder.compare(PHPTokenTypes.PHP_OPENING_TAG))
		{
			builder.advanceLexer();
		}
		else if(builder.compare(PHPTokenTypes.PHP_ECHO_OPENING_TAG))
		{
			builder.advanceLexer();
		}

		IElementType parsed = Function.parse(builder);
		if(parsed == PHPElementTypes.EMPTY_INPUT)
		{
			parsed = ClassDeclaration.parse(builder);
		}
		if(parsed == PHPElementTypes.EMPTY_INPUT)
		{
			parsed = Statement.parse(builder);
		}

		if(builder.compare(PHPTokenTypes.PHP_CLOSING_TAG))
		{
			builder.advanceLexer();
		}
		return parsed != PHPElementTypes.EMPTY_INPUT;
	}

}
