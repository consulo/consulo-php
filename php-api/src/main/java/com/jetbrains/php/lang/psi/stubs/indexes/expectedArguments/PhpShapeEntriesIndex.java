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

package com.jetbrains.php.lang.psi.stubs.indexes.expectedArguments;

import java.util.List;

/**
 * Entries of array shapes: {@code array{size?: 'sm'|'md', options: array{label: string}}}.
 *
 * @author VISTALL
 * @since 2026-10-30
 */
public final class PhpShapeEntriesIndex
{
	private PhpShapeEntriesIndex()
	{
	}

	/**
	 * One key of a shape with one member of its value type: a union value gives one entry per member
	 */
	public static final class PhpShapeEntry
	{
		private final PhpExpectedFunctionArgument myKey;
		private final String myValueType;
		private final boolean myOptional;
		private final List<PhpShapeEntry> myChildren;

		public PhpShapeEntry(PhpExpectedFunctionArgument key, String valueType, boolean optional, List<PhpShapeEntry> children)
		{
			myKey = key;
			myValueType = valueType;
			myOptional = optional;
			myChildren = List.copyOf(children);
		}

		/**
		 * @return the key: a scalar ({@code 'size'}, {@code 0}) or {@link PhpAnyExpressionArgument} for a computed one
		 */
		public PhpExpectedFunctionArgument getKey()
		{
			return myKey;
		}

		/**
		 * @return the value type text: {@code 'sm'}, {@code string}, {@code array{label: string}}
		 */
		public String getValueType()
		{
			return myValueType;
		}

		/**
		 * @return true for {@code size?:}
		 */
		public boolean isOptional()
		{
			return myOptional;
		}

		/**
		 * @return the entries of a nested shape value
		 */
		public List<PhpShapeEntry> getChildren()
		{
			return myChildren;
		}

		@Override
		public String toString()
		{
			return myKey.getValue() + (myOptional ? "?" : "") + ": " + myValueType;
		}
	}
}
