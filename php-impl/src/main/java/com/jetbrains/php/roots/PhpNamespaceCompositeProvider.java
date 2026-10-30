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

package com.jetbrains.php.roots;

import com.intellij.json.psi.JsonArray;
import com.intellij.json.psi.JsonFile;
import com.intellij.json.psi.JsonObject;
import com.intellij.json.psi.JsonProperty;
import com.intellij.json.psi.JsonStringLiteral;
import com.intellij.json.psi.JsonValue;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.psi.PsiDirectory;
import consulo.language.psi.PsiFile;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.util.VirtualFileUtil;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Namespaces of a directory, from the PSR-4 {@code autoload} and {@code autoload-dev} of the nearest {@code composer.json}:
 * {@code "App\\": "src/"} makes {@code src/Controller} the namespace {@code App\Controller}.
 *
 * @author VISTALL
 * @since 2026-10-30
 */
public final class PhpNamespaceCompositeProvider
{
	public static final PhpNamespaceCompositeProvider INSTANCE = new PhpNamespaceCompositeProvider();

	private PhpNamespaceCompositeProvider()
	{
	}

	/**
	 * @return namespaces without leading backslash, empty when no PSR-4 root contains the directory
	 */
	@RequiredReadAction
	public List<String> suggestNamespaces(PsiDirectory directory)
	{
		VirtualFile target = directory.getVirtualFile();
		Set<String> result = new LinkedHashSet<>();
		for(PsiDirectory current = directory; current != null; current = current.getParentDirectory())
		{
			PsiFile composer = current.findFile("composer.json");
			if(composer instanceof JsonFile jsonFile && jsonFile.getTopLevelValue() instanceof JsonObject root)
			{
				collect(root, "autoload", current.getVirtualFile(), target, result);
				collect(root, "autoload-dev", current.getVirtualFile(), target, result);
				break;
			}
		}
		return new ArrayList<>(result);
	}

	@RequiredReadAction
	private static void collect(JsonObject root, String section, VirtualFile composerDirectory, VirtualFile target, Set<String> result)
	{
		JsonObject psr4 = object(object(root, section), "psr-4");
		if(psr4 == null)
		{
			return;
		}
		for(JsonProperty property : psr4.getPropertyList())
		{
			String prefix = trimSlashes(property.getName());
			for(String path : paths(property.getValue()))
			{
				VirtualFile base = path.isEmpty() ? composerDirectory : composerDirectory.findFileByRelativePath(path);
				if(base == null || !VirtualFileUtil.isAncestor(base, target, false))
				{
					continue;
				}
				String relative = VirtualFileUtil.getRelativePath(target, base, '\\');
				String namespace = relative == null || relative.isEmpty() ? prefix : (prefix.isEmpty() ? relative : prefix + "\\" + relative);
				result.add(namespace);
			}
		}
	}

	@Nullable
	@RequiredReadAction
	private static JsonObject object(@Nullable JsonObject object, String name)
	{
		JsonProperty property = object == null ? null : object.findProperty(name);
		return property != null && property.getValue() instanceof JsonObject value ? value : null;
	}

	@RequiredReadAction
	private static List<String> paths(@Nullable JsonValue value)
	{
		List<String> result = new ArrayList<>();
		if(value instanceof JsonStringLiteral literal)
		{
			result.add(trimSlashes(literal.getValue()));
		}
		else if(value instanceof JsonArray array)
		{
			for(JsonValue item : array.getValueList())
			{
				if(item instanceof JsonStringLiteral literal)
				{
					result.add(trimSlashes(literal.getValue()));
				}
			}
		}
		return result;
	}

	private static String trimSlashes(String text)
	{
		int start = 0;
		int end = text.length();
		while(start < end && (text.charAt(start) == '/' || text.charAt(start) == '\\'))
		{
			start++;
		}
		while(end > start && (text.charAt(end - 1) == '/' || text.charAt(end - 1) == '\\'))
		{
			end--;
		}
		return text.substring(start, end);
	}
}
