package org.consulo.php.lang.parser.parsing.classes;

import org.consulo.php.lang.lexer.PhpTokenTypes;
import org.consulo.php.lang.parser.PhpElementTypes;
import org.consulo.php.lang.parser.util.ListParsingHelper;
import org.consulo.php.lang.parser.util.ParserPart2;
import org.consulo.php.lang.parser.util.PhpParserErrors;
import org.consulo.php.lang.parser.util.PhpPsiBuilder;
import org.consulo.php.lang.psi.PhpModifierList;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 12.10.2007
 * Time: 11:44:37
 */
public class ClassDeclaration implements PhpTokenTypes
{

	//	class_declaration_statement:
	//		class_entry_type IDENTIFIER extends_from
	//			implements_list
	//			'{' class_statement_list '}'
	//		| INTERFACE_KEYWORD IDENTIFIER
	//			interface_extends_list
	//			'{' class_statement_list '}'
	//	;
	public static IElementType parse(PhpPsiBuilder builder)
	{
		IElementType result = parseInterface(builder);
		if(result == PhpElementTypes.EMPTY_INPUT)
		{
			result = parseClass(builder);
		}
		return result;
	}

	//		class_entry_type IDENTIFIER extends_from
	//		implements_list
	//		'{' class_statement_list '}'

	//	class_entry_type:
	//		kwCLASS
	//		| kwABSTRACT kwCLASS
	//		| FINAL_KEYWORD kwCLASS
	//	;
	private static IElementType parseClass(PhpPsiBuilder builder)
	{
		if(!builder.compare(kwCLASS) && !builder.compare(PhpModifierList.MODIFIERS))
		{
			return PhpElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker classMarker = builder.mark();

		ClassMemberModifiers.parseModifiers(builder);

		builder.match(kwCLASS);
		if(!builder.compareAndEat(IDENTIFIER))
		{
			builder.error(PhpParserErrors.expected("class name"));
		}
		parseClassExtends(builder);
		parseClassImplements(builder);
		parseClassStatements(builder);
		classMarker.done(PhpElementTypes.CLASS);
		return PhpElementTypes.CLASS;
	}

	//	extends_from:
	//		/* empty */
	//		| kwEXTENDS fully_qualified_class_name
	//	;
	private static IElementType parseClassExtends(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker extendsMarker = builder.mark();
		if(builder.compareAndEat(kwEXTENDS))
		{
			ClassReference.parse(builder);
		}
		extendsMarker.done(PhpElementTypes.EXTENDS_LIST);
		return null;
	}


	//	implements_list:
	//		/* empty */
	//		| kwIMPLEMENTS interface_list
	//	;
	private static IElementType parseClassImplements(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker implementsList = builder.mark();
		if(builder.compareAndEat(kwIMPLEMENTS))
		{
			parseInterfaceList(builder);
		}
		implementsList.done(PhpElementTypes.IMPLEMENTS_LIST);
		return PhpElementTypes.IMPLEMENTS_LIST;
	}

	//		INTERFACE_KEYWORD IDENTIFIER
	//		interface_extends_list
	//		'{' class_statement_list '}'
	private static IElementType parseInterface(PhpPsiBuilder builder)
	{
		if(!builder.compare(INTERFACE_KEYWORD))
		{
			return PhpElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker interfaceMarker = builder.mark();
		builder.advanceLexer();
		if(!builder.compareAndEat(IDENTIFIER))
		{
			builder.error(PhpParserErrors.expected("interface name"));
		}
		parseInterfaceExtends(builder);
		parseClassStatements(builder);
		interfaceMarker.done(PhpElementTypes.CLASS);
		return PhpElementTypes.CLASS;
	}

	private static void parseClassStatements(PhpPsiBuilder builder)
	{
		builder.match(chLBRACE);
		ClassStatementList.parse(builder);
		builder.match(chRBRACE);
	}

	//	interface_extends_list:
	//		/* empty */
	//		| kwEXTENDS interface_list
	//	;

	//	interface_list:
	//		fully_qualified_class_name
	//		| interface_list ',' fully_qualified_class_name
	//	;
	private static IElementType parseInterfaceExtends(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker extendsList = builder.mark();
		if(builder.compareAndEat(kwEXTENDS))
		{
			parseInterfaceList(builder);
		}
		extendsList.done(PhpElementTypes.EXTENDS_LIST);
		return PhpElementTypes.EXTENDS_LIST;
	}

	private static void parseInterfaceList(PhpPsiBuilder builder)
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
}
