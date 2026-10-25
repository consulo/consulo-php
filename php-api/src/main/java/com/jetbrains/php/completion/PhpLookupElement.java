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

package com.jetbrains.php.completion;


import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.PhpPresentationUtil;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.editor.completion.lookup.InsertHandler;
import consulo.language.editor.completion.lookup.InsertionContext;
import consulo.language.editor.completion.lookup.LookupElement;
import consulo.language.editor.completion.lookup.LookupElementPresentation;
import consulo.language.icon.IconDescriptorUpdaters;
import consulo.language.psi.PsiElement;
import consulo.language.psi.stub.StubIndexKey;
import consulo.project.Project;
import consulo.ui.image.Image;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Lookup element of a PHP element, in the shape of PhpStorm's {@code PhpLookupElement}: the public fields are the presentation
 * and can be changed by subclasses.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public class PhpLookupElement extends LookupElement
{
	public String lookupString;
	/**
	 * Icon of the lookup, null for the icon of the PHP element computed when rendered
	 */
	@Nullable
	public Image icon;
	@Nullable
	public String typeText;
	@Nullable
	public String tailText;
	public boolean bold;
	public boolean deprecated;
	@Nullable
	@SuppressWarnings("rawtypes")
	public InsertHandler handler;
	@Nullable
	public final Project project;

	@Nullable
	private final PhpNamedElement myNamedElement;
	@Nullable
	private final StubIndexKey<?, ?> myIndexKey;

	@RequiredReadAction
	public PhpLookupElement(@Nonnull PhpNamedElement namedElement)
	{
		myNamedElement = namedElement;
		myIndexKey = null;
		project = namedElement.getProject();
		lookupString = namedElement.getName();
		deprecated = namedElement.isDeprecated();
		if(!(namedElement instanceof PhpClass))
		{
			PhpType type = namedElement.getType();
			typeText = type.isEmpty() ? null : type.toStringResolved();
		}
		if(namedElement instanceof Function function)
		{
			tailText = PhpPresentationUtil.formatParameters(null, function.getParameters()).toString();
		}
	}

	/**
	 * Element without PSI, for example a namespace name
	 *
	 * @param indexKey index the name comes from, may be null
	 */
	@SuppressWarnings("rawtypes")
	public PhpLookupElement(@Nonnull String name,
							@Nullable StubIndexKey indexKey,
							@Nullable Image icon,
							@Nullable String typeText,
							@Nullable Project project,
							@Nullable InsertHandler handler)
	{
		myNamedElement = null;
		myIndexKey = indexKey;
		this.project = project;
		this.lookupString = name;
		this.icon = icon;
		this.typeText = typeText;
		this.handler = handler;
	}

	@Nullable
	public PhpNamedElement getNamedElement()
	{
		return myNamedElement;
	}

	@Nullable
	public StubIndexKey<?, ?> getIndexKey()
	{
		return myIndexKey;
	}

	@Nonnull
	@Override
	public String getLookupString()
	{
		return lookupString;
	}

	@Nonnull
	@Override
	public Object getObject()
	{
		return myNamedElement != null ? myNamedElement : lookupString;
	}

	@Nullable
	@Override
	public PsiElement getPsiElement()
	{
		return myNamedElement;
	}

	@Override
	public boolean isValid()
	{
		return myNamedElement == null || myNamedElement.isValid();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void handleInsert(InsertionContext context)
	{
		if(handler != null)
		{
			handler.handleInsert(context, this);
		}
	}

	@Override
	public void renderElement(LookupElementPresentation presentation)
	{
		presentation.setItemText(getLookupString());
		presentation.setIcon(icon != null || myNamedElement == null ? icon : IconDescriptorUpdaters.getIcon(myNamedElement, 0));
		presentation.setTypeText(typeText);
		presentation.setTailText(tailText, true);
		presentation.setItemTextBold(bold);
		presentation.setStrikeout(deprecated);
	}
}
