package org.consulo.php.lang;

import com.intellij.lang.Language;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.FileViewProviderFactory;
import com.intellij.psi.PsiManager;

/**
 * @author Maxim.Mossienko
 *         Date: 29.12.2008
 *         Time: 22:56:06
 */
public class PhpFileViewProviderFactory implements FileViewProviderFactory
{
	public FileViewProvider createFileViewProvider(final VirtualFile file, final Language language, final PsiManager manager, final boolean physical)
	{
		return new PhpFileViewProvider(manager, file, physical);
	}
}