package org.consulo.php.lang.parser.parsing.classes;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PHPElementTypes;
import org.consulo.php.lang.parser.util.PHPParserErrors;
import org.consulo.php.lang.parser.util.PHPPsiBuilder;

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
	public static IElementType parseVariableModifiers(PHPPsiBuilder builder)
	{
		if(!builder.compare(tsVARIABLE_MODIFIERS))
		{
			return PHPElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker modifiers = builder.mark();
		if(builder.compareAndEat(kwVAR))
		{
			modifiers.done(PHPElementTypes.MODIFIER_LIST);
			return PHPElementTypes.MODIFIER_LIST;
		}
		parseModifiers(builder);
		modifiers.done(PHPElementTypes.MODIFIER_LIST);
		return PHPElementTypes.MODIFIER_LIST;
	}

	//	method_modifiers:
	//		/* empty */
	//		| non_empty_member_modifiers
	//	;
	public static IElementType parseMethodModifiers(PHPPsiBuilder builder)
	{
		PsiBuilder.Marker modifiers = builder.mark();
		if(builder.compare(tsMODIFIERS))
		{
			parseModifiers(builder);
		}
		modifiers.done(PHPElementTypes.MODIFIER_LIST);
		return PHPElementTypes.MODIFIER_LIST;
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
	private static void parseModifiers(PHPPsiBuilder builder)
	{
		if(!builder.compare(tsMODIFIERS))
		{
			builder.error(PHPParserErrors.expected("modifier"));
		}
		while(builder.compare(tsMODIFIERS))
		{
			builder.advanceLexer();
		}
	}

}
