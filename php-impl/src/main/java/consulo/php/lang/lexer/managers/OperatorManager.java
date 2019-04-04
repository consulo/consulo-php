package consulo.php.lang.lexer.managers;

import javax.annotation.Nonnull;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import consulo.php.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.lexer._PhpFlexLexer;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 23.03.2007
 *
 * @author jay
 */
public class OperatorManager
{
	private static final TokenSet tsWHITE_SPACE_OR_COMMENT = TokenSet.create(PhpDocElementTypes.DOC_COMMENT, PhpTokenTypes.C_STYLE_COMMENT, PhpTokenTypes.LINE_COMMENT, PhpTokenTypes.WHITE_SPACE);

	private boolean afterOperator;
	private boolean afterKeyword;
	private boolean inStatementStart;
	private boolean afterVariable;
	private boolean afterArrow;

	public OperatorManager(@Nonnull final _PhpFlexLexer lexer)
	{
	}

	public void reset()
	{

	}

	public IElementType process(IElementType type)
	{
		if(tsWHITE_SPACE_OR_COMMENT.contains(type))
		{
			return type;
		}

		afterArrow = PhpTokenTypes.ARROW == type;

		if(afterVariable && PhpTokenTypes.tsUNARY_POSTFIX_OPS.contains(type))
		{

		}
		else
		{
			afterOperator = PhpTokenTypes.tsOPERATORS.contains(type);
		}
		afterKeyword = PhpTokenTypes.KEYWORDS.contains(type);
		afterVariable = PhpTokenTypes.VARIABLE == type;
		inStatementStart = (PhpTokenTypes.opSEMICOLON == type || PhpTokenTypes.tsPHP_OPENING_TAGS.contains(type) || PhpTokenTypes.tsOPENING_BRACKETS.contains(type));
		return type;
	}

	public boolean unaryAllowed()
	{
		boolean unaryAllowed = afterOperator || afterKeyword || inStatementStart;
		//		System.out.println("asked for unary allowed: " + unaryAllowed);
		return unaryAllowed;
	}

	public boolean isAfterArrow()
	{
		return afterArrow;
	}
}
