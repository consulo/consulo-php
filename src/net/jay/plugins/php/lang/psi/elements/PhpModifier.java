package net.jay.plugins.php.lang.psi.elements;

import java.io.Serializable;

/**
 * @author jay
 * @date May 16, 2008 11:55:51 PM
 */
public class PhpModifier implements Serializable
{

	public enum Access
	{
		PUBLIC, PROTECTED, PRIVATE
	}

	public enum State
	{
		STATIC, DYNAMIC
	}

	public enum Abstractness
	{
		ABSTRACT, IMPLEMENTED, FINAL
	}

	public static PhpModifier PUBLIC = new PhpModifier();

	private Access access = Access.PUBLIC;
	private State state = State.DYNAMIC;
	private Abstractness abstractness = Abstractness.IMPLEMENTED;

	public PhpModifier()
	{
	}

	public PhpModifier(Access access)
	{
		this.access = access;
	}

	public PhpModifier(Access access, State state)
	{
		this.access = access;
		this.state = state;
	}

	public PhpModifier(Access access, State state, Abstractness abstractness)
	{
		this.access = access;
		this.state = state;
		this.abstractness = abstractness;
	}

	public Access getAccess()
	{
		return access;
	}

	public boolean isPublic()
	{
		return access == Access.PUBLIC;
	}

	public boolean isProtected()
	{
		return access == Access.PROTECTED;
	}

	public boolean isPrivate()
	{
		return access == Access.PRIVATE;
	}

	public void setAccess(Access access)
	{
		this.access = access;
	}

	public State getState()
	{
		return state;
	}

	public boolean isStatic()
	{
		return state == State.STATIC;
	}

	public boolean isDynamic()
	{
		return state == State.DYNAMIC;
	}

	public void setState(State state)
	{
		this.state = state;
	}

	public Abstractness getAbstractness()
	{
		return abstractness;
	}

	public boolean isFinal()
	{
		return abstractness == Abstractness.FINAL;
	}

	public boolean isAbstract()
	{
		return abstractness == Abstractness.ABSTRACT;
	}

	public void setAbstractness(Abstractness abstractness)
	{
		this.abstractness = abstractness;
	}

	public String toString()
	{
		StringBuffer buf = new StringBuffer();
		if(isAbstract())
		{
			buf.append("abstract ");
		}
		if(isPrivate())
		{
			buf.append("private ");
		}
		else if(isProtected())
		{
			buf.append("protected ");
		}
		else if(isPublic())
		{
			buf.append("public ");
		}
		if(isStatic())
		{
			buf.append("static ");
		}
		if(isFinal())
		{
			buf.append("final ");
		}
		return buf.toString().trim();
	}
}
