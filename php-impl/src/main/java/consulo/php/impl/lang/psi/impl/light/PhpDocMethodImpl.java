/*
 * Copyright 2013-2026 consulo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package consulo.php.impl.lang.psi.impl.light;

import com.jetbrains.php.lang.PhpLanguage;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.codeInsight.controlFlow.PhpControlFlow;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.ParameterList;
import com.jetbrains.php.lang.psi.elements.PhpModifier;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.annotation.access.RequiredReadAction;
import consulo.application.util.function.Processor;
import consulo.language.ast.ASTNode;
import consulo.language.impl.psi.LightElement;
import consulo.language.psi.PsiElement;
import consulo.language.util.IncorrectOperationException;
import consulo.php.impl.codeInsight.controlFlow.PhpControlFlowBuilder;
import consulo.php.impl.lang.psi.resolve.types.PhpTypeInference;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * A method declared by a {@code @method [static] [Type] name[(arguments)] [description]} tag of the doc comment of a class: a
 * magic method like one handled by {@code __call}. It navigates to the tag.
 *
 * @author VISTALL
 * @since 2026-11-01
 */
public class PhpDocMethodImpl extends LightElement implements Method
{
	private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z_\\x80-\\xff][a-zA-Z0-9_\\x80-\\xff]*");

	private final PhpClass myClass;
	private final PhpDocTag myTag;
	private final String myName;
	private final boolean myStatic;
	private final String myTypeText;

	private PhpDocMethodImpl(PhpClass phpClass, PhpDocTag tag, String name, boolean isStatic, String typeText)
	{
		super(phpClass.getManager(), PhpLanguage.INSTANCE);
		myClass = phpClass;
		myTag = tag;
		myName = name;
		myStatic = isStatic;
		myTypeText = typeText;
	}

	/**
	 * @return the method of a {@code @method} tag, {@code null} if the tag declares none
	 */
	@Nullable
	@RequiredReadAction
	public static PhpDocMethodImpl create(PhpClass phpClass, PhpDocTag tag)
	{
		String value = tag.getTagValue().trim();
		int parenthesis = value.indexOf('(');
		String[] parts;
		if(parenthesis > 0)
		{
			// [static] [Type] name(...) description
			parts = value.substring(0, parenthesis).trim().split("\\s+");
		}
		else
		{
			// [static] [Type] name description: the words after the name are the description
			String[] words = value.split("\\s+");
			int start = words.length > 2 && words[0].equalsIgnoreCase("static") ? 1 : 0;
			// a single word is the name, else the type comes first
			int nameIndex = words.length - start >= 2 ? start + 1 : start;
			parts = Arrays.copyOf(words, nameIndex + 1);
		}
		String name = parts[parts.length - 1];
		if(!NAME_PATTERN.matcher(name).matches())
		{
			return null;
		}
		boolean isStatic = parts.length > 1 && parts[0].equalsIgnoreCase("static");
		int typeStart = isStatic ? 1 : 0;
		StringBuilder type = new StringBuilder();
		for(int i = typeStart; i < parts.length - 1; i++)
		{
			type.append(parts[i]);
		}
		return new PhpDocMethodImpl(phpClass, tag, name, isStatic, type.toString());
	}

	public PhpDocTag getTag()
	{
		return myTag;
	}

	@Nonnull
	@Override
	public String getName()
	{
		return myName;
	}

	@Override
	public PhpClass getContainingClass()
	{
		return myClass;
	}

	@Override
	public boolean isStatic()
	{
		return myStatic;
	}

	@Nonnull
	@Override
	@RequiredReadAction
	public PhpType getType()
	{
		if(myTypeText.isEmpty())
		{
			return PhpType.EMPTY;
		}
		PhpType.PhpTypeBuilder builder = PhpType.builder();
		for(String type : myTypeText.split("\\|"))
		{
			builder.add(PhpTypeInference.resolveDocType(myTag, type));
		}
		return builder.build();
	}

	@Override
	public PsiElement getNavigationElement()
	{
		return myTag;
	}

	@Override
	public PsiElement getParent()
	{
		return myClass;
	}

	@Override
	public boolean isValid()
	{
		return myTag.isValid();
	}

	@Override
	public boolean canNavigateToSource()
	{
		return true;
	}

	@Nullable
	@Override
	public PhpDocComment getDocComment()
	{
		return null;
	}

	@Nullable
	@Override
	public MethodType getMethodType(boolean allowAmbiguity)
	{
		return MethodType.REGULAR_METHOD;
	}

	@Override
	public boolean isFinal()
	{
		return false;
	}

	@Override
	public boolean isAbstract()
	{
		return false;
	}

	@Override
	public PhpModifier.Access getAccess()
	{
		return PhpModifier.Access.PUBLIC;
	}

	@Nonnull
	@Override
	public PhpModifier getModifier()
	{
		return PhpModifier.instance(PhpModifier.Access.PUBLIC, PhpModifier.Abstractness.IMPLEMENTED,
				myStatic ? PhpModifier.State.STATIC : PhpModifier.State.DYNAMIC);
	}

	@Nonnull
	@Override
	public Parameter[] getParameters()
	{
		return Parameter.EMPTY_ARRAY;
	}

	@Nullable
	@Override
	public ParameterList getParameterList()
	{
		return null;
	}

	@Override
	public PhpControlFlow getControlFlow()
	{
		return PhpControlFlowBuilder.getControlFlow(this);
	}

	@Nonnull
	@Override
	public String getNamespaceName()
	{
		return myClass.getNamespaceName();
	}

	@Override
	public boolean isDeprecated()
	{
		return false;
	}

	@Override
	public boolean isInternal()
	{
		return false;
	}

	@Override
	public void processDocs(Processor<PhpDocComment> processor)
	{
	}

	@Nullable
	@Override
	public ASTNode getNameNode()
	{
		return null;
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		return null;
	}

	@Override
	public PsiElement setName(String name) throws IncorrectOperationException
	{
		throw new IncorrectOperationException("the name of a @method tag can not be changed");
	}

	@Nullable
	@Override
	public PhpPsiElement getFirstPsiChild()
	{
		return null;
	}

	@Nullable
	@Override
	public PhpPsiElement getNextPsiSibling()
	{
		return null;
	}

	@Nullable
	@Override
	public PhpPsiElement getPrevPsiSibling()
	{
		return null;
	}

	@Override
	public String toString()
	{
		return "PhpDocMethod:" + myName;
	}
}
