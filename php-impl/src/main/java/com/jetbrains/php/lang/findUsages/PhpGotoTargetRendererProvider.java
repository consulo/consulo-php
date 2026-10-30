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

package com.jetbrains.php.lang.findUsages;

import com.jetbrains.php.lang.psi.elements.PhpClassMember;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import consulo.annotation.access.RequiredReadAction;
import consulo.component.util.Iconable;
import consulo.language.editor.ui.PsiElementListCellRenderer;
import org.jspecify.annotations.Nullable;

/**
 * Presentation of PHP elements in navigation popups.
 *
 * @author VISTALL
 * @since 2026-10-30
 */
public final class PhpGotoTargetRendererProvider
{
	private PhpGotoTargetRendererProvider()
	{
	}

	/**
	 * Name of the element, with its class or namespace as container text: {@code send (\\App\\Mailer)}
	 */
	public static class PhpNamedElementPsiElementListCellRenderer extends PsiElementListCellRenderer<PhpNamedElement>
	{
		private final boolean myShowMethodNames;

		/**
		 * @param showMethodNames show {@code Class::member} as element text instead of the member name
		 */
		public PhpNamedElementPsiElementListCellRenderer(boolean showMethodNames)
		{
			myShowMethodNames = showMethodNames;
		}

		@Override
		@RequiredReadAction
		public String getElementText(PhpNamedElement element)
		{
			if(myShowMethodNames && element instanceof PhpClassMember member && member.getContainingClass() != null)
			{
				return member.getContainingClass().getName() + "::" + element.getName();
			}
			return element.getName();
		}

		@Nullable
		@Override
		@RequiredReadAction
		protected String getContainerText(PhpNamedElement element, String name)
		{
			if(element instanceof PhpClassMember member && member.getContainingClass() != null)
			{
				return "(" + member.getContainingClass().getFQN() + ")";
			}
			if(element instanceof PhpClass)
			{
				String namespace = element.getNamespaceName();
				return namespace.isEmpty() ? null : "(" + namespace + ")";
			}
			String fqn = element.getFQN();
			return fqn == null ? null : "(" + fqn + ")";
		}

		@Override
		protected int getIconFlags()
		{
			return Iconable.ICON_FLAG_VISIBILITY;
		}
	}
}
