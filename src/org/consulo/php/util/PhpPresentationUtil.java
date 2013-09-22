package org.consulo.php.util;

import java.io.File;

import org.consulo.php.PhpLanguageLevel;
import org.consulo.php.lang.psi.PhpClass;
import org.consulo.php.lang.psi.impl.PhpFileImpl;
import org.consulo.php.module.extension.PhpModuleExtension;
import org.jetbrains.annotations.NotNull;
import com.intellij.ide.IconDescriptorUpdaters;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ArrayUtil;

/**
 * @author jay
 * @date May 31, 2008 1:47:22 PM
 */
public class PhpPresentationUtil
{

	@SuppressWarnings({"ConstantConditions"})
	private static String getPresentablePathForClass(@NotNull PhpClass klass)
	{
		VirtualFile classRoot = klass.getContainingFile().getVirtualFile();
		if(klass.getName() != null)
		{
			final String[] fileNames = ArrayUtil.reverseArray(klass.getName().split("_"));
			for(String fileName : fileNames)
			{
				if(!classRoot.getNameWithoutExtension().equals(fileName))
				{
					break;
				}
				classRoot = classRoot.getParent();
			}
		}
		return getPresentablePathForFile(classRoot, klass.getProject());
	}

	private static String getPresentablePathForFile(@NotNull VirtualFile file, Project project)
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

	public static ItemPresentation getClassPresentation(PhpClass klass)
	{
		Module moduleForPsiElement = ModuleUtil.findModuleForPsiElement(klass);

		String location = null;
		if(moduleForPsiElement != null)
		{
			PhpModuleExtension extension = ModuleUtilCore.getExtension(moduleForPsiElement, PhpModuleExtension.class);
			if(extension != null && extension.getLanguageLevel().isAtLeast(PhpLanguageLevel.PHP_5_3))
			{
				location = klass.getNamespace();

				if(location != null)
				{
					location = "(" + location + ")";
				}
			}
		}
		if(location == null)
		{
			location = getPresentablePathForClass(klass);
		}
		return new PresentationData(klass.getName(), location, IconDescriptorUpdaters.getIcon(klass, 0), IconDescriptorUpdaters.getIcon(klass, 0), null);
	}

	public static ItemPresentation getFilePresentation(PhpFileImpl phpFile)
	{
		final VirtualFile virtualFile = phpFile.getVirtualFile();
		assert virtualFile != null;
		String location = getPresentablePathForFile(virtualFile, phpFile.getProject());
		return new PresentationData(phpFile.getName(), location, IconDescriptorUpdaters.getIcon(phpFile, 0), IconDescriptorUpdaters.getIcon(phpFile, 0), null);
	}

}
