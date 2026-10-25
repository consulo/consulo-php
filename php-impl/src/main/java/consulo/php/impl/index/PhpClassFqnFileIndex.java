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
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.stubs.indexes.PhpClassFqnIndex;
import consulo.annotation.component.ExtensionImpl;
import consulo.index.io.ID;
import jakarta.annotation.Nonnull;

import java.util.function.Consumer;

/**
 * @author VISTALL
 * @see PhpClassFqnIndex
 * @since 2026-10-25
 */
@ExtensionImpl
public class PhpClassFqnFileIndex extends PhpFqnFileIndexBase
{
	@Nonnull
	@Override
	public ID<String, Void> getName()
	{
		return PhpClassFqnIndex.KEY;
	}

	@Override
	protected void collect(@Nonnull PhpFile file, @Nonnull Consumer<String> consumer)
	{
		for(PhpClass phpClass : PhpPsiUtil.findAllClasses(file))
		{
			if(!phpClass.isInterface() && !phpClass.isTrait())
			{
				consumer.accept(phpClass.getFQN());
			}
		}
	}
}
