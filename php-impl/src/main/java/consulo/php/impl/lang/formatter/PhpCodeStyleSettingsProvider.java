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

package consulo.php.impl.lang.formatter;

import com.jetbrains.php.lang.PhpLanguage;
import com.jetbrains.php.lang.formatter.PhpCodeStyleSettings;
import consulo.annotation.component.ExtensionImpl;
import consulo.configurable.Configurable;
import consulo.language.Language;
import consulo.language.codeStyle.CodeStyleSettings;
import consulo.language.codeStyle.CustomCodeStyleSettings;
import consulo.language.codeStyle.setting.CodeStyleSettingsProvider;

/**
 * Registers {@link PhpCodeStyleSettings}; there is no settings page yet.
 *
 * @author VISTALL
 * @since 2026-10-30
 */
@ExtensionImpl
public class PhpCodeStyleSettingsProvider extends CodeStyleSettingsProvider
{
	@Override
	public CustomCodeStyleSettings createCustomSettings(CodeStyleSettings settings)
	{
		return new PhpCodeStyleSettings(settings);
	}

	@Override
	public Configurable createSettingsPage(CodeStyleSettings settings, CodeStyleSettings originalSettings)
	{
		throw new UnsupportedOperationException("PHP has no code style page yet");
	}

	@Override
	public boolean hasSettingsPage()
	{
		return false;
	}

	@Override
	public Language getLanguage()
	{
		return PhpLanguage.INSTANCE;
	}
}
