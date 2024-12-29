package consulo.php.impl.lang.psi.impl;

import com.jetbrains.php.lang.PhpLanguage;
import consulo.application.util.query.Query;
import consulo.language.Language;
import consulo.language.impl.psi.PsiPackageBase;
import consulo.language.psi.PsiDirectory;
import consulo.language.psi.PsiManager;
import consulo.language.psi.PsiPackage;
import consulo.language.psi.PsiPackageManager;
import consulo.module.content.DirectoryIndex;
import consulo.module.extension.ModuleExtension;
import consulo.php.impl.lang.psi.PhpPackage;
import consulo.util.collection.ArrayFactory;
import consulo.util.lang.lazy.LazyValue;
import consulo.virtualFileSystem.VirtualFile;

import jakarta.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author VISTALL
 * @since 07.07.13.
 */
public class PhpPackageImpl extends PsiPackageBase implements PhpPackage
{
	private Supplier<String> myNamespaceName;

	public PhpPackageImpl(PsiManager manager, PsiPackageManager packageManager, Class<? extends ModuleExtension> extensionClass, String qualifiedName)
	{
		super(manager, packageManager, extensionClass, qualifiedName);
		myNamespaceName = LazyValue.notNull(() ->
		{
			String q = getQualifiedName();
			return q.replace(".", "\\");
		});
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
		return myNamespaceName.get();
	}
}
