package consulo.php.codeInsight.highlight;

import consulo.php.lang.psi.PhpClassReference;
import consulo.php.lang.psi.PhpFile;
import consulo.php.lang.psi.visitors.PhpRecursiveElementVisitor;
import org.jetbrains.annotations.NotNull;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.daemon.impl.HighlightInfoType;
import com.intellij.codeInsight.daemon.impl.HighlightVisitor;
import com.intellij.codeInsight.daemon.impl.analysis.HighlightInfoHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public class PhpHighlightVisitor extends PhpRecursiveElementVisitor implements HighlightVisitor
{
	private HighlightInfoHolder myHolder;

	@Override
	public boolean suitableForFile(@NotNull PsiFile psiFile)
	{
		return psiFile instanceof PhpFile;
	}

	@Override
	public void visit(@NotNull PsiElement element)
	{
		element.accept(this);
	}

	@Override
	public boolean analyze(@NotNull PsiFile psiFile, boolean b, @NotNull HighlightInfoHolder highlightInfoHolder, @NotNull Runnable runnable)
	{
		myHolder = highlightInfoHolder;
		runnable.run();
		return true;
	}

	@Override
	public void visitClassReference(PhpClassReference classReference)
	{
		super.visitPhpElement(classReference);

		PsiElement resolve = classReference.resolve();
		if(resolve == null)
		{
			registerWrongRef(classReference.getReferenceElement());
		}
	}

	private void registerWrongRef(PsiElement range)
	{
		if(range == null)
		{
			return;
		}
		HighlightInfo.Builder builder = HighlightInfo.newHighlightInfo(HighlightInfoType.WRONG_REF);
		builder.range(range);
		builder.descriptionAndTooltip("'" + range.getText() + " is not resolved");

		myHolder.add(builder.create());
	}

	@NotNull
	@Override
	public HighlightVisitor clone()
	{
		return new PhpHighlightVisitor();
	}

	@Override
	public int order()
	{
		return 0;
	}
}
