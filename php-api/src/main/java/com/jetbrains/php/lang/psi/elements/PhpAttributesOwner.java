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

package com.jetbrains.php.lang.psi.elements;

import consulo.language.psi.PsiElement;
import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Declaration that can carry PHP 8 attributes: class, function, method, field, parameter, enum case.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public interface PhpAttributesOwner extends PsiElement
{
	/**
	 * @return attributes of all {@code #[...]} groups, in source order
	 */
	@Nonnull
	default Collection<PhpAttribute> getAttributes()
	{
		List<PhpAttribute> result = new ArrayList<>();
		for(PsiElement child = getFirstChild(); child != null; child = child.getNextSibling())
		{
			if(child instanceof PhpAttributesList attributesList)
			{
				result.addAll(attributesList.getAttributes());
			}
		}
		return result;
	}

	/**
	 * @param fqn attribute class, with or without the leading backslash
	 */
	@Nonnull
	default Collection<PhpAttribute> getAttributes(@Nonnull String fqn)
	{
		String normalized = fqn.startsWith("\\") ? fqn : "\\" + fqn;
		List<PhpAttribute> result = new ArrayList<>();
		for(PhpAttribute attribute : getAttributes())
		{
			if(normalized.equalsIgnoreCase(attribute.getFQN()))
			{
				result.add(attribute);
			}
		}
		return result;
	}
}
