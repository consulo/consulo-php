package consulo.php.ide.folding;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import consulo.annotations.RequiredReadAction;
import consulo.php.lang.psi.PhpBraceOwner;
import consulo.php.lang.psi.PhpClass;
import consulo.php.lang.psi.PhpFunction;
import consulo.php.lang.psi.PhpStubElements;
import consulo.php.lang.psi.visitors.PhpRecursiveElementVisitor;

/**
 * @author VISTALL
 * @since 22.09.13.
 */
public class PhpFoldingBuilder implements FoldingBuilder
{
	@RequiredReadAction
	@Nonnull
	@Override
	public FoldingDescriptor[] buildFoldRegions(@Nonnull ASTNode node, @Nonnull Document document)
	{
		List<FoldingDescriptor> list = new ArrayList<FoldingDescriptor>();
		PsiElement psi = node.getPsi();

		psi.acceptChildren(new PhpRecursiveElementVisitor()
		{
			@Override
			public void visitClass(PhpClass clazz)
			{
				super.visitClass(clazz);

				addFolding(clazz);
			}

			@Override
			public void visitFunction(PhpFunction phpFunction)
			{
				super.visitFunction(phpFunction);

				addFolding(phpFunction);
			}

			private void addFolding(PhpBraceOwner owner)
			{
				PsiElement leftBrace = owner.getLeftBrace();
				PsiElement rightBrace = owner.getRightBrace();
				if(leftBrace == null || rightBrace == null)
				{
					return;
				}

				list.add(new FoldingDescriptor(owner, new TextRange(leftBrace.getTextOffset(), rightBrace.getTextOffset() + 1)));

			}
		});
		return list.isEmpty() ? FoldingDescriptor.EMPTY : list.toArray(new FoldingDescriptor[list.size()]);
	}

	@Nullable
	@Override
	public String getPlaceholderText(@Nonnull ASTNode node)
	{
		IElementType elementType = node.getElementType();
		if(elementType == PhpStubElements.CLASS || elementType == PhpStubElements.FUNCTION)
		{
			return "{...}";
		}
		return null;
	}

	@Override
	public boolean isCollapsedByDefault(@Nonnull ASTNode node)
	{
		return false;
	}
}