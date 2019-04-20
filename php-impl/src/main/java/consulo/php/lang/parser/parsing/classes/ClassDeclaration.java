package consulo.php.lang.parser.parsing.classes;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.jetbrains.php.lang.psi.elements.PhpModifierList;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.parser.PhpElementTypes;
import consulo.php.lang.parser.util.ListParsingHelper;
import consulo.php.lang.parser.util.ParserPart2;
import consulo.php.lang.parser.util.PhpParserErrors;
import consulo.php.lang.parser.util.PhpPsiBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 12.10.2007
 * Time: 11:44:37
 */
public class ClassDeclaration implements PhpTokenTypes
{
	private static final TokenSet CLASS_START_TYPES = TokenSet.create(kwCLASS, INTERFACE_KEYWORD, TRAIT_KEYWORD);

	public static IElementType parse(PhpPsiBuilder builder)
	{
		if(!builder.compare(CLASS_START_TYPES) && !builder.compare(PhpModifierList.MODIFIERS))
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

	private static IElementType parseTypeList(PhpPsiBuilder builder, IElementType start, IElementType doneTo)
	{
		PsiBuilder.Marker implementsList = builder.mark();
		if(builder.compareAndEat(start))
		{
			ParserPart2 interfaceParser = new ParserPart2()
			{
				@Override
				public PsiBuilder.Marker parse(PhpPsiBuilder builder)
				{
					return ClassReference.parseClassNameReference(builder, null, false, false, false);
				}
			};
			ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder, interfaceParser.parse(builder), interfaceParser, false);
		}
		implementsList.done(doneTo);
		return doneTo;
	}

	private static void parseClassStatements(PhpPsiBuilder builder)
	{
		builder.match(LBRACE);
		ClassStatementList.parse(builder);
		builder.match(RBRACE);
	}
}
