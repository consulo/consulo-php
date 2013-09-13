package org.consulo.php.lang.parser.parsing.classes;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PhpElementTypes;
import org.consulo.php.lang.parser.util.PhpParserErrors;
import org.consulo.php.lang.parser.util.PhpPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 28.10.2007
 */
public class ClassMemberModifiers implements PHPTokenTypes
{

	//	variable_modifiers:
	//		non_empty_member_modifiers
	//		| kwVAR
	//	;
	public static IElementType parseVariableModifiers(PhpPsiBuilder builder)
	{
		if(!builder.compare(tsVARIABLE_MODIFIERS))
		{
			return PhpElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker modifiers = builder.mark();
		if(builder.compareAndEat(kwVAR))
		{
			modifiers.done(PhpElementTypes.MODIFIER_LIST);
			return PhpElementTypes.MODIFIER_LIST;
		}
		parseModifiers(builder);
		modifiers.done(PhpElementTypes.MODIFIER_LIST);
		return PhpElementTypes.MODIFIER_LIST;
	}

	//	method_modifiers:
	//		/* empty */
	//		| non_empty_member_modifiers
	//	;
	public static IElementType parseMethodModifiers(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker modifiers = builder.mark();
		if(builder.compare(tsMODIFIERS))
		{
			parseModifiers(builder);
		}
		modifiers.done(PhpElementTypes.MODIFIER_LIST);
		return PhpElementTypes.MODIFIER_LIST;
	}

	//	non_empty_member_modifiers:
	//		member_modifier
	//		| non_empty_member_modifiers member_modifier
	//	;
	//
	//	member_modifier:
	//		kwPUBLIC
	//		| kwPROTECTED
	//		| kwPRIVATE
	//		| kwSTATIC
	//		| kwABSTRACT
	//		| kwFINAL
	//	;
	private static void parseModifiers(PhpPsiBuilder builder)
	{
		if(!builder.compare(tsMODIFIERS))
		{
			builder.error(PhpParserErrors.expected("modifier"));
		}
		while(builder.compare(tsMODIFIERS))
		{
			builder.advanceLexer();
		}
	}

}
