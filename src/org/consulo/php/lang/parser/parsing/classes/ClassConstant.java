package org.consulo.php.lang.parser.parsing.classes;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import org.consulo.php.lang.parser.parsing.expressions.StaticScalar;
import org.consulo.php.lang.parser.util.ListParsingHelper;
import org.consulo.php.lang.parser.util.PHPPsiBuilder;
import org.consulo.php.lang.parser.util.ParserPart;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 27.10.2007
 */
public class ClassConstant implements PHPTokenTypes
{

	//	class_constant_declaration:
	//		class_constant_declaration ',' IDENTIFIER '=' static_scalar
	//		| kwCONST IDENTIFIER '=' static_scalar
	//	;
	public static IElementType parse(PHPPsiBuilder builder)
	{
		if(!builder.compareAndEat(kwCONST))
		{
			return PHPElementTypes.EMPTY_INPUT;
		}

		ParserPart constantParser = new ParserPart()
		{
			public IElementType parse(PHPPsiBuilder builder)
			{
				if(!builder.compare(IDENTIFIER))
				{
					return PHPElementTypes.EMPTY_INPUT;
				}
				PsiBuilder.Marker classConstant = builder.mark();
				builder.match(IDENTIFIER);
				builder.match(opASGN);
				StaticScalar.parse(builder);
				classConstant.done(PHPElementTypes.CLASS_CONSTANT);
				return PHPElementTypes.CLASS_CONSTANT;
			}
		};
		ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder, constantParser.parse(builder), constantParser, false);
		return PHPElementTypes.CLASS_CONSTANT;
	}

}
