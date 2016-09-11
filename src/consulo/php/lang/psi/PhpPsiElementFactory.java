package consulo.php.lang.psi;

import consulo.php.lang.PhpFileType;
import consulo.php.lang.psi.impl.PhpFileImpl;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;

/**
 * @author jay
 * @date Jul 1, 2008 4:10:20 PM
 */
public class PhpPsiElementFactory
{
	public static PhpConstantReference createConstantReference(Project project, @NotNull String constantName)
	{
		assert constantName.length() > 0;
		final PsiFile psiFile = createFile(project, constantName);
		final PsiElement child = psiFile.getFirstChild();
		assert child instanceof PhpGroupStatement;
		final PhpElement psiElement = ((PhpGroupStatement) child).getFirstPsiChild();
		assert psiElement instanceof PhpConstantReference;
		return (PhpConstantReference) psiElement;
	}

	public static PhpVariableReference createVariable(Project project, @NotNull String variableName)
	{
		assert variableName.length() > 0;
		final PsiFile psiFile = createFile(project, "$" + variableName);
		final PsiElement child = psiFile.getFirstChild();
		assert child instanceof PhpGroupStatement;
		final PhpElement psiElement = ((PhpGroupStatement) child).getFirstPsiChild();
		assert psiElement instanceof PhpVariableReference;
		return (PhpVariableReference) psiElement;
	}

	public static PhpClass createClass(Project project, @NotNull String text)
	{
		final PsiFile psiFile = createFile(project, text);
		final PsiElement child = psiFile.getFirstChild();
		assert child instanceof PhpGroupStatement;
		final PhpElement psiElement = ((PhpGroupStatement) child).getFirstPsiChild();
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
