package consulo.php.codeInsight.highlight;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.daemon.impl.HighlightInfoType;
import com.intellij.codeInsight.daemon.impl.HighlightVisitor;
import com.intellij.codeInsight.daemon.impl.analysis.HighlightInfoHolder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ReferenceRange;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.elements.ClassConstantReference;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.ConstantReference;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.FieldReference;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.Variable;
import consulo.annotations.RequiredReadAction;
import consulo.php.lang.highlighter.PhpHighlightingData;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public class PhpHighlightVisitor extends PhpElementVisitor implements HighlightVisitor
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
	@RequiredReadAction
	public void visitVariableReference(Variable variable)
	{
		super.visitVariableReference(variable);

		if(variable.canReadName())
		{
			if(variable.getName().equals(Variable.$THIS) || variable.isDeclaration())
			{
				return;
			}

			PsiElement resolved = variable.resolve();
			if(resolved == null)
			{
				registerWrongRef(variable, variable);
			}
		}
	}

	@Override
	@RequiredReadAction
	public void visitClassConstantReference(ClassConstantReference constantReference)
	{
		super.visitClassConstantReference(constantReference);

		PsiElement element = constantReference.resolve();
		if(element == null)
		{
			if(constantReference.isSoft())
			{
				return;
			}

			registerWrongRef(constantReference.getNameIdentifier(), constantReference);
		}
		else
		{
			PsiElement nameIdentifier = constantReference.getNameIdentifier();
			createHighlighing(HighlightInfoType.INFORMATION, nameIdentifier.getTextRange(), null, PhpHighlightingData.CONSTANT);
		}
	}

	@Override
	@RequiredReadAction
	public void visitField(Field phpField)
	{
		super.visitField(phpField);

		if(phpField.isConstant())
		{
			PsiElement nameIdentifier = phpField.getNameIdentifier();
			if(nameIdentifier != null)
			{
				createHighlighing(HighlightInfoType.INFORMATION, nameIdentifier.getTextRange(), null, PhpHighlightingData.CONSTANT);
			}
		}
	}

	@Override
	@RequiredReadAction
	public void visitConstant(ConstantReference constant)
	{
		super.visitConstant(constant);

		String text = constant.getText();
		if(text.equalsIgnoreCase("true") || text.equalsIgnoreCase("false") || text.equalsIgnoreCase("null"))
		{
			createHighlighing(HighlightInfoType.INFORMATION, constant, null, PhpHighlightingData.KEYWORD);
		}
	}

	@Override
	@RequiredReadAction
	public void visitMethodReference(MethodReference reference)
	{
		if(reference.canReadName())
		{
			final PsiElement element = reference.resolve();
			if(element == null)
			{
				registerWrongRef(reference.getNameIdentifier(), reference);
			}
		}
	}

	@Override
	@RequiredReadAction
	public void visitFunctionReference(FunctionReference reference)
	{
		final PsiElement element = reference.resolve();
		ASTNode node = reference.getNameNode();
		if(element == null && node != null)
		{
			registerWrongRef(node.getPsi(), reference);
		}
	}

	@Override
	@RequiredReadAction
	public void visitClassReference(ClassReference classReference)
	{
		super.visitPhpElement(classReference);

		if(classReference.getText().equals(PhpClass.SELF) || classReference.getText().equals(PhpClass.PARENT))
		{
			createHighlighing(HighlightInfoType.INFORMATION, classReference, null, PhpHighlightingData.KEYWORD);
			return;
		}

		PsiElement resolved = classReference.resolve();
		if(resolved == null)
		{
			registerWrongRef(classReference.getReferenceElement(), classReference);
		}
	}

	@Override
	@RequiredReadAction
	public void visitFieldReference(FieldReference fieldReference)
	{
		super.visitFieldReference(fieldReference);

		PsiElement resolvedElement = fieldReference.resolve();
		if(resolvedElement == null)
		{
			registerWrongRef(fieldReference.getElement(), fieldReference);
		}
		else
		{
			createHighlighing(HighlightInfoType.INFORMATION, fieldReference, null, DefaultLanguageHighlighterColors.INSTANCE_FIELD);
		}
	}

	@RequiredReadAction
	private void registerWrongRef(@Nullable PsiElement element, @Nonnull PsiReference reference)
	{
		if(element == null)
		{
			return;
		}

		createHighlighing(HighlightInfoType.WARNING, reference, "'" + element.getText() + "' is not resolved", null);
	}

	@RequiredReadAction
	private void createHighlighing(@Nonnull HighlightInfoType type, @Nonnull PsiReference reference, @Nullable String description, @Nullable TextAttributesKey key)
	{
		createHighlighing(type, ContainerUtil.getFirstItem(ReferenceRange.getAbsoluteRanges(reference)), description, key);
	}

	@RequiredReadAction
	private void createHighlighing(@Nonnull HighlightInfoType type, @Nonnull TextRange range, @Nullable String description, @Nullable TextAttributesKey key)
	{
		HighlightInfo.Builder builder = HighlightInfo.newHighlightInfo(type);
		builder.range(range);
		if(description != null)
		{
			builder.descriptionAndTooltip(description);
		}

		if(key != null)
		{
			builder.textAttributes(key);
		}
		myHolder.add(builder.createUnconditionally());
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
