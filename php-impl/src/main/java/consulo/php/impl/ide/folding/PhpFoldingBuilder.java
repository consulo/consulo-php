package consulo.php.impl.ide.folding;

import com.jetbrains.php.lang.PhpLanguage;
import consulo.annotation.component.ExtensionImpl;
import consulo.document.util.TextRange;
import consulo.language.Language;
import consulo.language.ast.ASTNode;
import consulo.language.editor.folding.FoldingBuilder;
import consulo.language.editor.folding.FoldingDescriptor;
import consulo.document.Document;
import consulo.language.psi.PsiElement;
import consulo.language.ast.IElementType;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import consulo.annotation.access.RequiredReadAction;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.psi.PhpStubElements;
import consulo.php.impl.lang.psi.visitors.PhpRecursiveElementVisitor;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 22.09.13.
 */
@ExtensionImpl
public class PhpFoldingBuilder implements FoldingBuilder
{
	@RequiredReadAction
	@Nonnull
	@Override
	public FoldingDescriptor[] buildFoldRegions(@Nonnull ASTNode node, @Nonnull Document document)
	{
		List<FoldingDescriptor> list = new ArrayList<>();
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
			public void visitFunction(Function phpFunction)
			{
				super.visitFunction(phpFunction);

				addFolding(phpFunction);
			}

			@Override
			public void visitNamespaceStatement(PhpNamespace namespace)
			{
				super.visitNamespaceStatement(namespace);

				addFolding(namespace);
			}

			private void addFolding(PsiElement owner)
			{
				ASTNode lbrace = owner.getNode().findChildByType(PhpTokenTypes.LBRACE);
				ASTNode rbrace = owner.getNode().findChildByType(PhpTokenTypes.RBRACE);

				if(lbrace == null || rbrace == null)
				{
					return;
				}

				PsiElement leftBrace = lbrace.getPsi();
				PsiElement rightBrace = rbrace.getPsi();
				if(leftBrace == null || rightBrace == null)
				{
					return;
				}

				list.add(new FoldingDescriptor(owner, new TextRange(leftBrace.getTextOffset(), rightBrace.getTextOffset() + 1)));

			}
		});
		return list.isEmpty() ? FoldingDescriptor.EMPTY : list.toArray(new FoldingDescriptor[list.size()]);
	}

	@RequiredReadAction
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

	@RequiredReadAction
	@Override
	public boolean isCollapsedByDefault(@Nonnull ASTNode node)
	{
		return false;
	}

	@Nonnull
	@Override
	public Language getLanguage()
	{
		return PhpLanguage.INSTANCE;
	}
}
