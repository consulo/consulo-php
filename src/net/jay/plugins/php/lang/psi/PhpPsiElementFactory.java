package net.jay.plugins.php.lang.psi;

import net.jay.plugins.php.lang.PHPFileType;
import net.jay.plugins.php.lang.psi.elements.ConstantReference;
import net.jay.plugins.php.lang.psi.elements.GroupStatement;
import net.jay.plugins.php.lang.psi.elements.PHPPsiElement;
import net.jay.plugins.php.lang.psi.elements.Variable;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;

/**
 * @author jay
 * @date Jul 1, 2008 4:10:20 PM
 */
public class PhpPsiElementFactory implements ProjectComponent
{

	private Project project;

	public PhpPsiElementFactory(Project project)
	{
		this.project = project;
	}

	public static PhpPsiElementFactory getInstance(Project project)
	{
		return project.getComponent(PhpPsiElementFactory.class);
	}

	public ConstantReference createConstantReference(@NotNull String constantName)
	{
		assert constantName.length() > 0;
		final PsiFile psiFile = createFile(constantName);
		final PsiElement child = psiFile.getFirstChild();
		assert child instanceof GroupStatement;
		final PHPPsiElement psiElement = ((GroupStatement) child).getFirstPsiChild();
		assert psiElement instanceof ConstantReference;
		return (ConstantReference) psiElement;
	}

	public Variable createVariable(@NotNull String variableName)
	{
		assert variableName.length() > 0;
		final PsiFile psiFile = createFile("$" + variableName);
		final PsiElement child = psiFile.getFirstChild();
		assert child instanceof GroupStatement;
		final PHPPsiElement psiElement = ((GroupStatement) child).getFirstPsiChild();
		assert psiElement instanceof Variable;
		return (Variable) psiElement;
	}

	public PsiFile createFile(String fileText)
	{
		return createFile(fileText, false);
	}

	public PHPFile createFile(String fileText, boolean isPhisical)
	{
		return createDummyFile(fileText, isPhisical);
	}

	private PHPFile createDummyFile(String fileText, boolean isPhisical)
	{
		return (PHPFile) PsiFileFactory.getInstance(project).createFileFromText("DUMMY__." + PHPFileType.PHP.getDefaultExtension(), PHPFileType.PHP, "<?php\n" + fileText, System.currentTimeMillis(), isPhisical);
	}


	public void projectOpened()
	{
	}

	public void projectClosed()
	{
	}

	@NonNls
	@NotNull
	public String getComponentName()
	{
		return "PhpSupport.PhpPsiElementFactory";
	}

	public void initComponent()
	{
	}

	public void disposeComponent()
	{
	}
}
