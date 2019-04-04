package consulo.php.ide.presentation;

import java.io.File;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.ItemPresentationProvider;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import consulo.ide.IconDescriptorUpdaters;
import com.jetbrains.php.lang.psi.PhpFile;
import consulo.ui.image.Image;

/**
 * @author VISTALL
 * @since 22.09.13.
 */
public class PhpFilePresentation implements ItemPresentationProvider<PhpFile>
{
	@Override
	public ItemPresentation getPresentation(final PhpFile item)
	{
		return new ItemPresentation()
		{
			@Nullable
			@Override
			public String getPresentableText()
			{
				return item.getName();
			}

			@Nullable
			@Override
			public String getLocationString()
			{
				return getPresentablePathForFile(item.getVirtualFile(), item.getProject());
			}

			@Nullable
			@Override
			public Image getIcon()
			{
				return IconDescriptorUpdaters.getIcon(item, 0);
			}
		};
	}

	public static String getPresentablePathForFile(@Nonnull VirtualFile file, Project project)
	{
		Module module = ModuleUtil.findModuleForFile(file, project);
		VirtualFile[] roots;
		if(module == null)
		{
			roots = new VirtualFile[]{project.getBaseDir()};
		}
		else
		{
			roots = ModuleRootManager.getInstance(module).getContentRoots();
		}
		String location = "";
		for(VirtualFile root : roots)
		{
			if(file.getUrl().startsWith(root.getUrl()))
			{
				location = VfsUtil.getRelativePath(file, root, File.separatorChar);
			}
		}
		location = ".../" + location;
		return location;
	}
}
