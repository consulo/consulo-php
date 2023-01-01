package consulo.php.impl.lang.lexer.managers;

import java.util.Stack;

import consulo.php.impl.lang.lexer._PhpFlexLexer;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 26.03.2007
 *
 * @author jay
 */
public class StatesManager
{

	private Stack<Integer> statesStack;
	private _PhpFlexLexer lexer;

	public StatesManager(_PhpFlexLexer lexer)
	{
		this.lexer = lexer;
		statesStack = new Stack<Integer>();
	}

	public void reset()
	{
		statesStack.clear();
	}

	public void toPreviousState()
	{
		assert (statesStack.size() >= 2);
		statesStack.pop();
		lexer.yybegin(statesStack.peek());
	}

	public void toState(final int state)
	{
		statesStack.push(state);
		lexer.yybegin(state);
	}

	public int getStackSize()
	{
		return statesStack.size();
	}

}
