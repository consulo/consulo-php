package consulo.php.impl.lang.parser.parsing.classes;

import consulo.language.parser.PsiBuilder;
import consulo.language.ast.IElementType;
import consulo.language.ast.TokenSet;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.php.impl.lang.parser.util.ListParsingHelper;
import consulo.php.impl.lang.parser.util.ParserPart2;
import consulo.php.impl.lang.parser.util.PhpParserErrors;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 12.10.2007
 * Time: 11:44:37
 */
public class ClassDeclaration implements PhpTokenTypes
{
	private static final TokenSet CLASS_START_TYPES = TokenSet.create(kwCLASS, INTERFACE_KEYWORD, TRAIT_KEYWORD);

	private static final TokenSet MODIFIERS = TokenSet.create(PhpTokenTypes.PUBLIC_KEYWORD, PhpTokenTypes.PROTECTED_KEYWORD, PhpTokenTypes.PRIVATE_KEYWORD,
			PhpTokenTypes.STATIC_KEYWORD, PhpTokenTypes.FINAL_KEYWORD, PhpTokenTypes.ABSTRACT_KEYWORD);

	public static IElementType parse(PhpPsiBuilder builder)
	{
		if(!builder.compare(CLASS_START_TYPES) && !builder.compare(MODIFIERS))
		{
			return PhpElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker classMarker = builder.mark();

		ClassMemberModifiers.parseModifiers(builder);

		if(!builder.compare(CLASS_START_TYPES))
		{
			classMarker.rollbackTo();
			return PhpElementTypes.EMPTY_INPUT;
		}

		builder.match(CLASS_START_TYPES);

		if(!builder.compareAndEat(IDENTIFIER))
		{
			builder.error(PhpParserErrors.expected("class name"));
		}
		parseTypeList(builder, kwEXTENDS, PhpElementTypes.EXTENDS_LIST);
		parseTypeList(builder, kwIMPLEMENTS, PhpElementTypes.IMPLEMENTS_LIST);
		parseClassStatements(builder);
		classMarker.done(PhpElementTypes.CLASS);
		return PhpElementTypes.CLASS;
	}

	public static IElementType parseTypeList(PhpPsiBuilder builder, IElementType start, IElementType doneTo)
	{
		PsiBuilder.Marker implementsList = builder.mark();
		if(builder.compareAndEat(start))
		{
			ParserPart2 interfaceParser = builder1 -> ClassReference.parseClassNameReference(builder1, null, 0);
			ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder, interfaceParser.parse(builder), interfaceParser, false);
		}
		implementsList.done(doneTo);
		return doneTo;
	}

	public static void parseClassStatements(PhpPsiBuilder builder)
	{
		builder.match(LBRACE);
		ClassStatementList.parse(builder);
		builder.match(RBRACE);
	}
}
