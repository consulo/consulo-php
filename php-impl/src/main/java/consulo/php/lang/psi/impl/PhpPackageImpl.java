package consulo.php.lang.psi.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import com.jetbrains.php.lang.PhpLanguage;
import consulo.php.lang.psi.PhpPackage;
import com.intellij.lang.Language;
import com.intellij.openapi.roots.impl.DirectoryIndex;
import com.intellij.openapi.util.NotNullLazyValue;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.file.PsiPackageBase;
import com.intellij.util.ArrayFactory;
import com.intellij.util.Query;
import consulo.module.extension.ModuleExtension;
import consulo.psi.PsiPackage;
import consulo.psi.PsiPackageManager;

/**
 * @author VISTALL
 * @since 07.07.13.
 */
public class PhpPackageImpl extends PsiPackageBase implements PhpPackage
{
	private NotNullLazyValue<String> myNamespaceName = new NotNullLazyValue<String>()
	{
		@Nonnull
		@Override
		protected String compute()
		{
			String qualifiedName = getQualifiedName();
			return qualifiedName.replace(".", "\\");
		}
	};

	public PhpPackageImpl(PsiManager manager, PsiPackageManager packageManager, Class<? extends ModuleExtension> extensionClass, String qualifiedName)
	{
		super(manager, packageManager, extensionClass, qualifiedName);
	}

	@Override
	protected Collection<PsiDirectory> getAllDirectories(boolean inLibrarySources)
	{
		List<PsiDirectory> directories = new ArrayList<PsiDirectory>();
		PsiManager manager = PsiManager.getInstance(getProject());
		Query<VirtualFile> directoriesByPackageName = DirectoryIndex.getInstance(getProject()).getDirectoriesByPackageName(getQualifiedName(), inLibrarySources);
		for(VirtualFile virtualFile : directoriesByPackageName)
		{
			PsiDirectory directory = manager.findDirectory(virtualFile);
			if(directory != null)
			{
				directories.add(directory);
			}
		}

		return directories;
	}

	@Override
	protected ArrayFactory<? extends PsiPackage> getPackageArrayFactory()
	{
		return PhpPackage.ARRAY_FACTORY;
	}

	@Nonnull
	@Override
	public Language getLanguage()
	{
		return PhpLanguage.INSTANCE;
	}

	@Nonnull
	@Override
	public String getNamespaceName()
	{
		return myNamespaceName.getValue();
	}
}
