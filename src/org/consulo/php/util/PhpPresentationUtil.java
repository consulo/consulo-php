package org.consulo.php.util;

import com.intellij.icons.AllIcons;
import com.intellij.ide.IconDescriptorUpdaters;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.RowIcon;
import com.intellij.util.ArrayUtil;
import com.intellij.util.ui.EmptyIcon;
import org.consulo.php.PhpIcons;
import org.consulo.php.lang.psi.PhpFile;
import org.consulo.php.lang.psi.elements.*;
import org.consulo.php.lang.psi.elements.PhpNamedElement;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;

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
		String location = getPresentablePathForClass(klass);
		return new PresentationData(klass.getName(), location, klass.getIcon(), klass.getIcon(), null);
	}

	public static ItemPresentation getFilePresentation(PhpFile phpFile)
	{
		final VirtualFile virtualFile = phpFile.getVirtualFile();
		assert virtualFile != null;
		String location = getPresentablePathForFile(virtualFile, phpFile.getProject());
		return new PresentationData(phpFile.getName(), location, IconDescriptorUpdaters.getIcon(phpFile, 0), IconDescriptorUpdaters.getIcon(phpFile, 0), null);
	}

	public static Icon getIcon(PhpNamedElement element)
	{
		RowIcon result = new RowIcon(2);
		result.setIcon(new EmptyIcon(PhpIcons.CLASS.getIconWidth(), PhpIcons.CLASS.getIconHeight()), 0);

		if(element instanceof PhpClass)
		{
			final PhpClass klass = (PhpClass) element;
			final PhpModifier modifier = klass.getModifier();
			if(klass.isInterface())
			{
				result.setIcon(AllIcons.Nodes.Interface, 0);
			}
			else if(modifier.isAbstract())
			{
				result.setIcon(PhpIcons.ABSTRACT_CLASS, 0);
			}
			else if(modifier.isFinal())
			{
				result.setIcon(PhpIcons.FINAL_CLASS, 0);
			}
			else
			{
				result.setIcon(PhpIcons.CLASS, 0);
			}

		}

		if(element instanceof PhpField)
		{
			final PhpField field = (PhpField) element;
			final PhpModifier modifier = field.getModifier();
			if(modifier.isStatic())
			{
				result.setIcon(PhpIcons.STATIC_FIELD, 0);
			}
			else
			{
				result.setIcon(PhpIcons.FIELD, 0);
			}
			result.setIcon(getAccessIcon(modifier), 1);
		}

		if(element instanceof PhpMethod)
		{
			final PhpMethod method = (PhpMethod) element;
			final PhpModifier modifier = method.getModifier();
			if(modifier.isStatic())
			{
				result.setIcon(PhpIcons.STATIC_METHOD, 0);
			}
			else
			{
				result.setIcon(PhpIcons.METHOD, 0);
			}
			result.setIcon(getAccessIcon(modifier), 1);
		}
		return result;
	}

	public static Icon getAccessIcon(PhpModifier modifier)
	{
		if(modifier.isPublic())
		{
			return PhpIcons.PUBLIC;
		}
		else if(modifier.isProtected())
		{
			return PhpIcons.PROTECTED;
		}
		else
		{
			return PhpIcons.PRIVATE;
		}
	}
}
