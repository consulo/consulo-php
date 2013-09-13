package org.consulo.php.lang.lexer.managers;

import java.util.Stack;

import com.intellij.openapi.diagnostic.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 13.03.2007
 *
 * @author jay
 */
public class BraceManager
{

	private static final Logger LOG = Logger.getInstance(BraceManager.class.getName());

	private enum BRACE
	{
		OPEN, CLOSE
	}

	private Stack<BRACE> myBraceStack;


	public static BraceManager getInstance(final int balance)
	{
		return new BraceManager(balance);
	}

	private BraceManager(final int balance)
	{
		assert (balance >= 0);
		myBraceStack = new Stack<BRACE>();
		for(int i = 0; i < balance; i++)
		{
			processOpenBrace();
		}
	}

	public boolean isEmpty()
	{
		return myBraceStack.size() == 0;
	}

	public void processOpenBrace()
	{
		myBraceStack.push(BRACE.OPEN);
	}

	public void processCloseBrace()
	{
		if(myBraceStack.isEmpty())
		{
			return;
		}
		BRACE previous = myBraceStack.peek();
		if(previous == BRACE.OPEN)
		{
			myBraceStack.pop();
		}
		else
		{
			LOG.error("No open brace for this close");
		}
	}
}
