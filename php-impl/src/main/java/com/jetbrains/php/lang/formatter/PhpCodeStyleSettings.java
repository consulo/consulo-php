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

package com.jetbrains.php.lang.formatter;

import com.jetbrains.php.refactoring.PhpNameStyle;
import consulo.language.codeStyle.CodeStyleSettings;
import consulo.language.codeStyle.CustomCodeStyleSettings;

/**
 * PHP code style settings, read through {@code CodeStyle.getCustomSettings(file, PhpCodeStyleSettings.class)}.
 *
 * @author VISTALL
 * @since 2026-10-30
 */
public class PhpCodeStyleSettings extends CustomCodeStyleSettings
{
	/**
	 * Style of generated variable and property names: {@link PhpNameStyle#CAMEL_CASE} or {@link PhpNameStyle#SNAKE_CASE}
	 */
	public String VARIABLE_NAMING_STYLE = PhpNameStyle.CAMEL_CASE;

	public PhpCodeStyleSettings(CodeStyleSettings container)
	{
		super("PhpCodeStyleSettings", container);
	}
}
