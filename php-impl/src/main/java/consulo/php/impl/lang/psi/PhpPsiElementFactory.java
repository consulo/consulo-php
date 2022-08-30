package consulo.php.impl.lang.psi;

import com.jetbrains.php.lang.PhpFileType;
import com.jetbrains.php.lang.psi.elements.*;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiFileFactory;
import consulo.php.impl.lang.psi.impl.PhpFileImpl;
import consulo.project.Project;

import javax.annotation.Nonnull;

/**
 * @author jay
 * @date Jul 1, 2008 4:10:20 PM
 */
public class PhpPsiElementFactory
{
	public static ConstantReference createConstantReference(Project project, @Nonnull String constantName)
	{
		assert constantName.length() > 0;
		final PsiFile psiFile = createFile(project, constantName);
		final PsiElement child = psiFile.getFirstChild();
		assert child instanceof GroupStatement;
		final PhpPsiElement psiElement = ((GroupStatement) child).getFirstPsiChild();
		assert psiElement instanceof ConstantReference;
		return (ConstantReference) psiElement;
	}

	public static Variable createVariable(Project project, @Nonnull String variableName)
	{
		assert variableName.length() > 0;
		final PsiFile psiFile = createFile(project, "$" + variableName);
		final PsiElement child = psiFile.getFirstChild();
		assert child instanceof GroupStatement;
		final PhpPsiElement psiElement = ((GroupStatement) child).getFirstPsiChild();
		assert psiElement instanceof Variable;
		return (Variable) psiElement;
	}

	public static PhpClass createClass(Project project, @Nonnull String text)
	{
		final PsiFile psiFile = createFile(project, text);
		final PsiElement child = psiFile.getFirstChild();
		assert child instanceof GroupStatement;
		final PhpPsiElement psiElement = ((GroupStatement) child).getFirstPsiChild();
		assert psiElement instanceof PhpClass;
		return (PhpClass) psiElement;
	}

	public static PsiFile createFile(Project project, String fileText)
	{
		return createFile(project, fileText, false);
	}

	public static PhpFileImpl createFile(Project project, String fileText, boolean isPhysical)
	{
		return createDummyFile(project, fileText, isPhysical);
	}

	private static PhpFileImpl createDummyFile(Project project, String fileText, boolean isPhysical)
	{
		return (PhpFileImpl) PsiFileFactory.getInstance(project).createFileFromText("DUMMY__." + PhpFileType.INSTANCE.getDefaultExtension(), PhpFileType.INSTANCE, "<?php\n" + fileText + "\n?>", System.currentTimeMillis(), isPhysical);
	}
}
