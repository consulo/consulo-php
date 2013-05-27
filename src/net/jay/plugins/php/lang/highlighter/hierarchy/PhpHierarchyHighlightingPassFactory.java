package net.jay.plugins.php.lang.highlighter.hierarchy;

import net.jay.plugins.php.lang.psi.PHPFile;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.codeHighlighting.TextEditorHighlightingPass;
import com.intellij.codeHighlighting.TextEditorHighlightingPassFactory;
import com.intellij.codeHighlighting.TextEditorHighlightingPassRegistrar;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;

/**
 * @author jay
 * @date Jun 25, 2008 12:03:27 AM
 */
public class PhpHierarchyHighlightingPassFactory implements TextEditorHighlightingPassFactory
{

	private final TextEditorHighlightingPassRegistrar registrar;

	public PhpHierarchyHighlightingPassFactory(final TextEditorHighlightingPassRegistrar passRegistrar)
	{
		registrar = passRegistrar;
	}

	@Nullable
	public TextEditorHighlightingPass createHighlightingPass(final @Nullable PsiFile psiFile, @NotNull final Editor editor)
	{
		if(psiFile instanceof PHPFile)
		{
			return new PhpHierarchyHighlightingPass(psiFile.getProject(), editor, (PHPFile) psiFile);
		}
		return null;
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
		return "PhpSupport.PhpHierarchyHighlightingPassFactory";
	}

	public void initComponent()
	{
		registrar.registerTextEditorHighlightingPass(this, TextEditorHighlightingPassRegistrar.Anchor.LAST, 0, false, false);
	}

	public void disposeComponent()
	{
	}
}