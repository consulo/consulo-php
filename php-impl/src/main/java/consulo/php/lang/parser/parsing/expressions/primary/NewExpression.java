package consulo.php.lang.parser.parsing.expressions.primary;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.parser.PhpElementTypes;
import consulo.php.lang.parser.parsing.calls.Function;
import consulo.php.lang.parser.parsing.calls.Variable;
import consulo.php.lang.parser.parsing.classes.ClassDeclaration;
import consulo.php.lang.parser.parsing.classes.ClassReference;
import consulo.php.lang.parser.util.PhpParserErrors;
import consulo.php.lang.parser.util.PhpPsiBuilder;

/**
 * @author jay
 * @time 21.12.2007 16:42:00
 */
public class NewExpression implements PhpTokenTypes
{

	// kwNEW class_name_reference ctor_arguments
	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker newExpr = builder.mark();
		if(builder.compareAndEat(kwNEW))
		{
			PsiBuilder.Marker classMarker = null;

			if(builder.getTokenType() == kwCLASS)
			{
				builder.advanceLexer();
				classMarker = builder.mark();
			}
			else if(ClassReference.parseClassNameReference(builder, null, ClassReference.ALLOW_STATIC) == null)
			{
			}
			else if(Variable.parse(builder) != PhpElementTypes.EMPTY_INPUT)
			{
				builder.error(PhpParserErrors.expected("class name"));
			}

			if(builder.compare(LPAREN))
			{
				Function.parseFunctionCallParameterList(builder);
			}

			if(classMarker != null)
			{
				ClassDeclaration.parseTypeList(builder, kwEXTENDS, PhpElementTypes.EXTENDS_LIST);
				ClassDeclaration.parseTypeList(builder, kwIMPLEMENTS, PhpElementTypes.IMPLEMENTS_LIST);
				ClassDeclaration.parseClassStatements(builder);
				classMarker.done(PhpElementTypes.ANONYMOUS_CLASS);
			}

			newExpr.done(PhpElementTypes.NEW_EXPRESSION);
			return PhpElementTypes.NEW_EXPRESSION;
		}
		newExpr.drop();
		return PhpElementTypes.EMPTY_INPUT;
	}
}
