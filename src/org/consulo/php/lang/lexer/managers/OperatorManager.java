package org.consulo.php.lang.lexer.managers;

import net.jay.plugins.php.lang.lexer.PHPFlexLexer;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;

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

	private PHPFlexLexer lexer;
	private boolean afterOperator;
	private boolean afterKeyword;
	private boolean inStatementStart;
	private boolean afterVariable;
	private boolean afterArrow;

	public OperatorManager(@NotNull final PHPFlexLexer lexer)
	{
		this.lexer = lexer;
	}

	public void reset()
	{

	}

	public IElementType process(IElementType type)
	{
		if(PHPTokenTypes.tsWHITE_SPACE_OR_COMMENT.contains(type))
		{
			return type;
		}

		afterArrow = PHPTokenTypes.ARROW == type;

		if(afterVariable && PHPTokenTypes.tsUNARY_POSTFIX_OPS.contains(type))
		{

		}
		else
		{
			afterOperator = PHPTokenTypes.tsOPERATORS.contains(type);
		}
		afterKeyword = PHPTokenTypes.KEYWORDS.contains(type);
		afterVariable = PHPTokenTypes.VARIABLE == type;
		inStatementStart = (PHPTokenTypes.opSEMICOLON == type || PHPTokenTypes.tsPHP_OPENING_TAGS.contains(type) || PHPTokenTypes.tsOPENING_BRACKETS.contains(type));
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
