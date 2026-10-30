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

package com.jetbrains.php.lang.documentation;

import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.psi.elements.PhpAttribute;
import com.jetbrains.php.lang.psi.elements.PhpAttributesOwner;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import consulo.annotation.access.RequiredReadAction;
import org.jspecify.annotations.Nullable;

/**
 * Documentation facts of a PHP element.
 *
 * @author VISTALL
 * @since 2026-10-30
 */
public class PhpNamedElementDocSource
{
	private final PhpNamedElement myElement;
	private final boolean myInherited;

	/**
	 * @param inherited also look at the documentation of overridden elements (not supported yet: the element itself only)
	 */
	public PhpNamedElementDocSource(PhpNamedElement element, boolean inherited)
	{
		myElement = element;
		myInherited = inherited;
	}

	/**
	 * @return the text of {@code @deprecated text} or of {@code #[\\Deprecated('text')]}, "" when deprecated without a text,
	 * null when not deprecated
	 */
	@Nullable
	@RequiredReadAction
	public String getDeprecationInfo()
	{
		PhpDocComment docComment = myElement.getDocComment();
		if(docComment != null)
		{
			PhpDocTag[] tags = docComment.getTagElementsByName("@deprecated");
			if(tags.length > 0)
			{
				return tags[0].getTagValue().trim();
			}
		}
		if(myElement instanceof PhpAttributesOwner owner)
		{
			for(PhpAttribute attribute : owner.getAttributes("\\Deprecated"))
			{
				for(PhpAttribute.PhpAttributeArgument argument : attribute.getArguments())
				{
					if(argument.getArgument() instanceof com.jetbrains.php.lang.psi.stubs.indexes.expectedArguments.PhpExpectedFunctionScalarArgument scalar
						&& ("message".equals(argument.getName()) || argument.getIndex() == 0))
					{
						return scalar.getNormalizedValue();
					}
				}
				return "";
			}
		}
		return null;
	}
}
