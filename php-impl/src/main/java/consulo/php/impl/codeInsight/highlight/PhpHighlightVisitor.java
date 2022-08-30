package consulo.php.impl.codeInsight.highlight;

import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.codeEditor.DefaultLanguageHighlighterColors;
import consulo.colorScheme.TextAttributesKey;
import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.editor.rawHighlight.HighlightInfo;
import consulo.language.editor.rawHighlight.HighlightInfoHolder;
import consulo.language.editor.rawHighlight.HighlightInfoType;
import consulo.language.editor.rawHighlight.HighlightVisitor;
import consulo.language.psi.*;
import consulo.php.impl.lang.highlighter.PhpHighlightingData;
import consulo.php.impl.lang.parser.PhpTokenSets;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import consulo.util.collection.ContainerUtil;
import consulo.util.lang.StringUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
@ExtensionImpl
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
	public void visitElement(PsiElement element)
	{
		super.visitElement(element);

		IElementType elementType = PsiUtilCore.getElementType(element);
		if(PhpTokenSets.SOFT_KEYWORDS.contains(elementType))
		{
			createHighlighing(HighlightInfoType.INFORMATION, element.getTextRange(), null, PhpHighlightingData.KEYWORD);
		}
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

		PhpHightlightUtil.checkIsStaticForSelf(myHolder, constantReference);
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

		CharSequence text = constant.getNode().getChars();
		if(StringUtil.equalsIgnoreCase(text, "true") || StringUtil.equalsIgnoreCase(text, "false") || StringUtil.equalsIgnoreCase(text, "null"))
		{
			createHighlighing(HighlightInfoType.INFORMATION, constant, null, PhpHighlightingData.KEYWORD);
		}
		else
		{
			PsiElement resolved = constant.resolve();
			if(resolved == null)
			{
				registerWrongRef(constant.getNameIdentifier(), constant);
			}
			else
			{
				createHighlighing(HighlightInfoType.INFORMATION, constant, null, PhpHighlightingData.CONSTANT);
			}
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

			PhpHightlightUtil.checkIsStaticForSelf(myHolder, reference);
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

		String text = classReference.getText();
		if(text.equals(PhpClass.SELF) || text.equals(PhpClass.PARENT))
		{
			createHighlighing(HighlightInfoType.INFORMATION, classReference, null, PhpHighlightingData.KEYWORD);
			return;
		}

		if(PhpType.isPrimitiveType(text))
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

		PhpHightlightUtil.checkIsStaticForSelf(myHolder, fieldReference);
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
}
