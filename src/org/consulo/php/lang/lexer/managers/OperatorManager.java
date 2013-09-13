package org.consulo.php.lang.lexer.managers;

import org.consulo.php.lang.lexer.PhpFlexLexer;
import org.consulo.php.lang.lexer.PhpTokenTypes;

import org.jetbrains.annotations.NotNull;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 23.03.2007
 *
 * @author jay
 */
public class OperatorManager
{

	private PhpFlexLexer lexer;
	private boolean afterOperator;
	private boolean afterKeyword;
	private boolean inStatementStart;
	private boolean afterVariable;
	private boolean afterArrow;

	public OperatorManager(@NotNull final PhpFlexLexer lexer)
	{
		this.lexer = lexer;
	}

	public void reset()
	{

	}

	public IElementType process(IElementType type)
	{
		if(PhpTokenTypes.tsWHITE_SPACE_OR_COMMENT.contains(type))
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
