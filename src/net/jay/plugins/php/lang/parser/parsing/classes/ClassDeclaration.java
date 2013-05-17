package net.jay.plugins.php.lang.parser.parsing.classes;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import net.jay.plugins.php.lang.parser.util.PHPParserErrors;
import net.jay.plugins.php.lang.parser.util.PHPPsiBuilder;
import net.jay.plugins.php.lang.parser.util.ParserPart;
import net.jay.plugins.php.lang.parser.util.ListParsingHelper;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 12.10.2007
 * Time: 11:44:37
 */
public class ClassDeclaration implements PHPTokenTypes {

	//	class_declaration_statement:
	//		class_entry_type IDENTIFIER extends_from
	//			implements_list
	//			'{' class_statement_list '}'
	//		| kwINTERFACE IDENTIFIER
	//			interface_extends_list
	//			'{' class_statement_list '}'
	//	;
	public static IElementType parse(PHPPsiBuilder builder) {
		IElementType result = parseInterface(builder);
		if (result == PHPElementTypes.EMPTY_INPUT) {
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
	//		| kwFINAL kwCLASS
	//	;
	private static IElementType parseClass(PHPPsiBuilder builder) {
		if (!builder.compare(kwCLASS)
			&& !builder.compare(kwABSTACT)
			&& !builder.compare(kwFINAL)) {
			return PHPElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker classMarker = builder.mark();
		IElementType currentClass = PHPElementTypes.CLASS;
		if (builder.compareAndEat(kwABSTACT)) {
			currentClass = PHPElementTypes.CLASS;
		} else {
			if (builder.compareAndEat(kwFINAL)) {
				currentClass = PHPElementTypes.CLASS;
			}
		}
		builder.match(kwCLASS);
		if (!builder.compareAndEat(IDENTIFIER)) {
			builder.error(PHPParserErrors.expected("class name"));
		}
		parseClassExtends(builder);
		parseClassImplements(builder);
		parseClassStatements(builder);
		classMarker.done(currentClass);
		return currentClass;
	}

	//	extends_from:
	//		/* empty */
	//		| kwEXTENDS fully_qualified_class_name
	//	;
	private static IElementType parseClassExtends(PHPPsiBuilder builder) {
		PsiBuilder.Marker extendsMarker = builder.mark();
		if (builder.compareAndEat(kwEXTENDS)) {
			ClassReference.parse(builder);
		}
		extendsMarker.done(PHPElementTypes.EXTENDS_LIST);
		return null;
	}


	//	implements_list:
	//		/* empty */
	//		| kwIMPLEMENTS interface_list
	//	;
	private static IElementType parseClassImplements(PHPPsiBuilder builder) {
		PsiBuilder.Marker implementsList = builder.mark();
		if (builder.compareAndEat(kwIMPLEMENTS)) {
			parseInterfaceList(builder);
		}
		implementsList.done(PHPElementTypes.IMPLEMENTS_LIST);
		return PHPElementTypes.IMPLEMENTS_LIST;
	}

	//		kwINTERFACE IDENTIFIER
	//		interface_extends_list
	//		'{' class_statement_list '}'
	private static IElementType parseInterface(PHPPsiBuilder builder) {
		if (!builder.compare(kwINTERFACE)) {
			return PHPElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker interfaceMarker = builder.mark();
		builder.advanceLexer();
		if (!builder.compareAndEat(IDENTIFIER)) {
			builder.error(PHPParserErrors.expected("interface name"));
		}
		parseInterfaceExtends(builder);
		parseClassStatements(builder);
		interfaceMarker.done(PHPElementTypes.INTERFACE);
		return PHPElementTypes.INTERFACE;
	}

	private static void parseClassStatements(PHPPsiBuilder builder) {
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
	private static IElementType parseInterfaceExtends(PHPPsiBuilder builder) {
		PsiBuilder.Marker extendsList = builder.mark();
		if (builder.compareAndEat(kwEXTENDS)) {
			parseInterfaceList(builder);
		}
		extendsList.done(PHPElementTypes.EXTENDS_LIST);
		return PHPElementTypes.EXTENDS_LIST;
	}

	private static void parseInterfaceList(PHPPsiBuilder builder) {
		ParserPart interfaceParser = new ParserPart() {
			public IElementType parse(PHPPsiBuilder builder) {
				return ClassReference.parse(builder);
			}
		};
		ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder,
				interfaceParser.parse(builder),
				interfaceParser,
				false);
	}
}
