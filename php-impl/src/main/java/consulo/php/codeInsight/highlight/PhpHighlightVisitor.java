package consulo.php.codeInsight.highlight;

import javax.annotation.Nonnull;

import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.PhpFile;
import consulo.php.lang.psi.visitors.PhpRecursiveElementVisitor;
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
	public boolean suitableForFile(@Nonnull PsiFile psiFile)
	{
		return psiFile instanceof PhpFile;
	}

	@Override
	public void visit(@Nonnull PsiElement element)
	{
		element.accept(this);
	}

	@Override
	public boolean analyze(@Nonnull PsiFile psiFile, boolean b, @Nonnull HighlightInfoHolder highlightInfoHolder, @Nonnull Runnable runnable)
	{
		myHolder = highlightInfoHolder;
		runnable.run();
		return true;
	}

	@Override
	public void visitClassReference(ClassReference classReference)
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

	@Nonnull
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
