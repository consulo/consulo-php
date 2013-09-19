package org.consulo.php.lang.parser.parsing;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.consulo.php.lang.lexer.PhpTokenTypes;
import org.consulo.php.lang.parser.PhpElementTypes;
import org.consulo.php.lang.parser.parsing.classes.ClassDeclaration;
import org.consulo.php.lang.parser.parsing.classes.ClassReference;
import org.consulo.php.lang.parser.parsing.functions.Function;
import org.consulo.php.lang.parser.util.PhpParserErrors;
import org.consulo.php.lang.parser.util.PhpPsiBuilder;

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

		if(builder.getTokenType() == NAMESPACE_KEYWORD) {
			parseNamespaceStatement(builder);
		}

		while (builder.getTokenType() == USE_KEYWORD) {
			parseUseStatement(builder);
		}

		IElementType parsed = Function.parse(builder);
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

	private static void parseNamespaceStatement(PhpPsiBuilder builder) {
		PsiBuilder.Marker marker = builder.mark();

		builder.match(NAMESPACE_KEYWORD);

		if(ClassReference.parseClassNameReference(builder, null, false, false, false) == null) {
			builder.error("Namespace expected");
		}

		builder.match(opSEMICOLON);
		marker.done(PhpElementTypes.NAMESPACE_STATEMENT);
	}

	private static void parseUseStatement(PhpPsiBuilder builder) {
		PsiBuilder.Marker marker = builder.mark();

		builder.match(USE_KEYWORD);

		if(ClassReference.parseClassNameReference(builder, null, false, false, true) == null) {
			builder.error("Reference expected");
		}
		else {
			while (builder.getTokenType() == opCOMMA) {
				builder.advanceLexer();

				if(ClassReference.parseClassNameReference(builder, null, false, false, true) == null) {
					builder.error("Reference expected");
					break;
				}
			}
		}

		builder.match(opSEMICOLON);
		marker.done(PhpElementTypes.USE_STATEMENT);
	}
}
