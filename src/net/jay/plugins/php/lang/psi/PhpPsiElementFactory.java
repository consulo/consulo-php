package net.jay.plugins.php.lang.psi;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import net.jay.plugins.php.lang.PHPFileType;
import net.jay.plugins.php.lang.psi.elements.*;
import org.jetbrains.annotations.NotNull;

/**
 * @author jay
 * @date Jul 1, 2008 4:10:20 PM
 */
public class PhpPsiElementFactory
{
	public static ConstantReference createConstantReference(Project project, @NotNull String constantName)
	{
		assert constantName.length() > 0;
		final PsiFile psiFile = createFile(project, constantName);
		final PsiElement child = psiFile.getFirstChild();
		assert child instanceof GroupStatement;
		final PHPPsiElement psiElement = ((GroupStatement) child).getFirstPsiChild();
		assert psiElement instanceof ConstantReference;
		return (ConstantReference) psiElement;
	}

	public static Variable createVariable(Project project, @NotNull String variableName)
	{
		assert variableName.length() > 0;
		final PsiFile psiFile = createFile(project, "$" + variableName);
		final PsiElement child = psiFile.getFirstChild();
		assert child instanceof GroupStatement;
		final PHPPsiElement psiElement = ((GroupStatement) child).getFirstPsiChild();
		assert psiElement instanceof Variable;
		return (Variable) psiElement;
	}

	public static PhpClass createClass(Project project, @NotNull String text)
	{
		final PsiFile psiFile = createFile(project, text);
		final PsiElement child = psiFile.getFirstChild();
		assert child instanceof GroupStatement;
		final PHPPsiElement psiElement = ((GroupStatement) child).getFirstPsiChild();
		assert psiElement instanceof PhpClass;
		return (PhpClass) psiElement;
	}

	public static PsiFile createFile(Project project, String fileText)
	{
		return createFile(project, fileText, false);
	}

	public static PHPFile createFile(Project project, String fileText, boolean isPhysical)
	{
		return createDummyFile(project, fileText, isPhysical);
	}

	private static PHPFile createDummyFile(Project project, String fileText, boolean isPhysical)
	{
		return (PHPFile) PsiFileFactory.getInstance(project).createFileFromText("DUMMY__." + PHPFileType.INSTANCE.getDefaultExtension(), PHPFileType.INSTANCE, "<?php\n" + fileText + "\n?>",
				System.currentTimeMillis(), isPhysical);
	}
}
