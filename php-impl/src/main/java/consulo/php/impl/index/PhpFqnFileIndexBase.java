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

package consulo.php.impl.index;

import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.stubs.indexes.PhpConstantNameIndex;
import consulo.index.io.DataIndexer;
import consulo.index.io.EnumeratorStringDescriptor;
import consulo.index.io.KeyDescriptor;
import consulo.language.psi.PsiFile;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.language.psi.stub.FileContent;
import consulo.language.psi.stub.ScalarIndexExtension;
import jakarta.annotation.Nonnull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * File index of FQNs collected from one PHP file, without values.
 * The FQNs come from the file alone: no index access while indexing.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public abstract class PhpFqnFileIndexBase extends ScalarIndexExtension<String>
{
	protected abstract void collect(@Nonnull PhpFile file, @Nonnull Consumer<String> consumer);

	@Nonnull
	@Override
	public DataIndexer<String, Void, FileContent> getIndexer()
	{
		return inputData -> {
			PsiFile file = inputData.getPsiFile();
			if(!(file instanceof PhpFile phpFile))
			{
				return Collections.emptyMap();
			}
			Map<String, Void> result = new HashMap<>();
			collect(phpFile, fqn -> result.put(fqn, null));
			return result;
		};
	}

	@Nonnull
	@Override
	public KeyDescriptor<String> getKeyDescriptor()
	{
		return EnumeratorStringDescriptor.INSTANCE;
	}

	@Nonnull
	@Override
	public FileBasedIndex.InputFilter getInputFilter()
	{
		return PhpConstantNameIndex.PHP_INPUT_FILTER;
	}

	@Override
	public boolean dependsOnFileContent()
	{
		return true;
	}

	@Override
	public int getVersion()
	{
		return 1;
	}
}
