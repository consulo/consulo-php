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

package com.jetbrains.php.completion.insert;


import consulo.codeEditor.Editor;
import consulo.document.Document;
import jakarta.annotation.Nonnull;

/**
 * Text edits at the caret for insert handlers, in the shape of PhpStorm's {@code PhpInsertHandlerUtil}.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public final class PhpInsertHandlerUtil
{
	private PhpInsertHandlerUtil()
	{
	}

	/**
	 * Inserts the text at the caret and moves the caret after it
	 */
	public static void insertStringAtCaret(@Nonnull Editor editor, @Nonnull String text)
	{
		int offset = editor.getCaretModel().getOffset();
		editor.getDocument().insertString(offset, text);
		editor.getCaretModel().moveToOffset(offset + text.length());
	}

	/**
	 * @return whether the document continues with the text at the caret
	 */
	public static boolean isStringAtCaret(@Nonnull Editor editor, @Nonnull String text)
	{
		Document document = editor.getDocument();
		int offset = editor.getCaretModel().getOffset();
		return offset + text.length() <= document.getTextLength()
			&& document.getCharsSequence().subSequence(offset, offset + text.length()).toString().equals(text);
	}
}
