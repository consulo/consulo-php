package org.consulo.php.ide.presentation;

import java.io.File;

import javax.swing.Icon;

import org.consulo.php.lang.psi.PhpFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.ide.IconDescriptorUpdaters;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.ItemPresentationProvider;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;

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
			public Icon getIcon(boolean unused)
			{
				return IconDescriptorUpdaters.getIcon(item, 0);
			}
		};
	}

	public static String getPresentablePathForFile(@NotNull VirtualFile file, Project project)
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
