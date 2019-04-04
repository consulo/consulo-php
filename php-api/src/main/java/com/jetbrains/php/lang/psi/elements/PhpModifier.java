// Copyright 2000-2018 JetBrains s.r.o.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.jetbrains.php.lang.psi.elements;

import java.io.Serializable;

import javax.annotation.Nonnull;

public class PhpModifier implements Serializable
{
	public static final PhpModifier PRIVATE_ABSTRACT_DYNAMIC = new PhpModifier(Access.PRIVATE, Abstractness.ABSTRACT, State.DYNAMIC);
	public static final PhpModifier PRIVATE_ABSTRACT_STATIC = new PhpModifier(Access.PRIVATE, Abstractness.ABSTRACT, State.STATIC);
	public static final PhpModifier PRIVATE_IMPLEMENTED_DYNAMIC = new PhpModifier(Access.PRIVATE, Abstractness.IMPLEMENTED, State.DYNAMIC);
	public static final PhpModifier PRIVATE_IMPLEMENTED_STATIC = new PhpModifier(Access.PRIVATE, Abstractness.IMPLEMENTED, State.STATIC);
	public static final PhpModifier PRIVATE_FINAL_DYNAMIC = new PhpModifier(Access.PRIVATE, Abstractness.FINAL, State.DYNAMIC);
	public static final PhpModifier PRIVATE_FINAL_STATIC = new PhpModifier(Access.PRIVATE, Abstractness.FINAL, State.STATIC);

	public static final PhpModifier PROTECTED_ABSTRACT_DYNAMIC = new PhpModifier(Access.PROTECTED, Abstractness.ABSTRACT, State.DYNAMIC);
	public static final PhpModifier PROTECTED_ABSTRACT_STATIC = new PhpModifier(Access.PROTECTED, Abstractness.ABSTRACT, State.STATIC);
	public static final PhpModifier PROTECTED_IMPLEMENTED_DYNAMIC = new PhpModifier(Access.PROTECTED, Abstractness.IMPLEMENTED, State.DYNAMIC);
	public static final PhpModifier PROTECTED_IMPLEMENTED_STATIC = new PhpModifier(Access.PROTECTED, Abstractness.IMPLEMENTED, State.STATIC);
	public static final PhpModifier PROTECTED_FINAL_DYNAMIC = new PhpModifier(Access.PROTECTED, Abstractness.FINAL, State.DYNAMIC);
	public static final PhpModifier PROTECTED_FINAL_STATIC = new PhpModifier(Access.PROTECTED, Abstractness.FINAL, State.STATIC);

	public static final PhpModifier PUBLIC_ABSTRACT_DYNAMIC = new PhpModifier(Access.PUBLIC, Abstractness.ABSTRACT, State.DYNAMIC);
	public static final PhpModifier PUBLIC_ABSTRACT_STATIC = new PhpModifier(Access.PUBLIC, Abstractness.ABSTRACT, State.STATIC);
	public static final PhpModifier PUBLIC_IMPLEMENTED_DYNAMIC = new PhpModifier(Access.PUBLIC, Abstractness.IMPLEMENTED, State.DYNAMIC);
	public static final PhpModifier PUBLIC_IMPLEMENTED_STATIC = new PhpModifier(Access.PUBLIC, Abstractness.IMPLEMENTED, State.STATIC);
	public static final PhpModifier PUBLIC_FINAL_DYNAMIC = new PhpModifier(Access.PUBLIC, Abstractness.FINAL, State.DYNAMIC);
	public static final PhpModifier PUBLIC_FINAL_STATIC = new PhpModifier(Access.PUBLIC, Abstractness.FINAL, State.STATIC);

	@Nonnull
	public static PhpModifier instance(@Nonnull Access access, @Nonnull Abstractness abstractness, @Nonnull State state)
	{
		PhpModifier instance = null;
		switch(access)
		{
			case PRIVATE:
				switch(abstractness)
				{
					case ABSTRACT:
						switch(state)
						{
							case DYNAMIC:
								instance = PRIVATE_ABSTRACT_DYNAMIC;
								break;
							case STATIC:
								instance = PRIVATE_ABSTRACT_STATIC;
								break;
						}
						break;
					case IMPLEMENTED:
						switch(state)
						{
							case DYNAMIC:
								instance = PRIVATE_IMPLEMENTED_DYNAMIC;
								break;
							case STATIC:
								instance = PRIVATE_IMPLEMENTED_STATIC;
								break;
						}
						break;
					case FINAL:
						switch(state)
						{
							case DYNAMIC:
								instance = PRIVATE_FINAL_DYNAMIC;
								break;
							case STATIC:
								instance = PRIVATE_FINAL_STATIC;
								break;
						}
						break;
				}
				break;
			case PROTECTED:
				switch(abstractness)
				{
					case ABSTRACT:
						switch(state)
						{
							case DYNAMIC:
								instance = PROTECTED_ABSTRACT_DYNAMIC;
								break;
							case STATIC:
								instance = PROTECTED_ABSTRACT_STATIC;
								break;
						}
						break;
					case IMPLEMENTED:
						switch(state)
						{
							case DYNAMIC:
								instance = PROTECTED_IMPLEMENTED_DYNAMIC;
								break;
							case STATIC:
								instance = PROTECTED_IMPLEMENTED_STATIC;
								break;
						}
						break;
					case FINAL:
						switch(state)
						{
							case DYNAMIC:
								instance = PROTECTED_FINAL_DYNAMIC;
								break;
							case STATIC:
								instance = PROTECTED_FINAL_STATIC;
								break;
						}
						break;
				}
				break;
			case PUBLIC:
				switch(abstractness)
				{
					case ABSTRACT:
						switch(state)
						{
							case DYNAMIC:
								instance = PUBLIC_ABSTRACT_DYNAMIC;
								break;
							case STATIC:
								instance = PUBLIC_ABSTRACT_STATIC;
								break;
						}
						break;
					case IMPLEMENTED:
						switch(state)
						{
							case DYNAMIC:
								instance = PUBLIC_IMPLEMENTED_DYNAMIC;
								break;
							case STATIC:
								instance = PUBLIC_IMPLEMENTED_STATIC;
								break;
						}
						break;
					case FINAL:
						switch(state)
						{
							case DYNAMIC:
								instance = PUBLIC_FINAL_DYNAMIC;
								break;
							case STATIC:
								instance = PUBLIC_FINAL_STATIC;
								break;
						}
						break;
				}
				break;
		}
		assert instance != null;
		return instance;
	}

	public enum Access
	{
		PUBLIC(3),
		PROTECTED(2),
		PRIVATE(1);
		private final int myLevel;

		Access(int level)
		{
			myLevel = level;
		}

		public int getLevel()
		{
			return myLevel;
		}

		@Override
		public String toString()
		{
			return super.toString().toLowerCase();
		}

		public boolean isProtected()
		{
			return this == PROTECTED;
		}

		public boolean isPrivate()
		{
			return this == PRIVATE;
		}

		public boolean isPublic()
		{
			return this == PUBLIC;
		}

		public boolean isWeakerThan(Access access)
		{
			return getLevelDiff(access) > 0;
		}

		public boolean isEqualOrWeakerThan(Access access)
		{
			return getLevelDiff(access) >= 0;
		}

		private int getLevelDiff(Access access)
		{
			return this.myLevel - access.getLevel();
		}
	}

	public enum State
	{
		STATIC,
		PARENT,
		DYNAMIC;

		public boolean isStatic()
		{
			return this == STATIC;
		}

		public boolean isDynamic()
		{
			return this == DYNAMIC;
		}
	}

	public enum Abstractness
	{
		ABSTRACT,
		IMPLEMENTED,
		FINAL
	}

	private Access access = Access.PUBLIC;
	private State state = State.DYNAMIC;
	private Abstractness abstractness = Abstractness.IMPLEMENTED;

	private PhpModifier(@Nonnull Access access, @Nonnull Abstractness abstractness, @Nonnull State state)
	{
		this.access = access;
		this.abstractness = abstractness;
		this.state = state;
	}

	@Nonnull
	public Abstractness getAbstractness()
	{
		return abstractness;
	}

	@Nonnull
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

	@Nonnull
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

	public boolean isFinal()
	{
		return abstractness == Abstractness.FINAL;
	}

	public boolean isAbstract()
	{
		return abstractness == Abstractness.ABSTRACT;
	}

	@Override
	public String toString()
	{
		StringBuilder buf = new StringBuilder();
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
