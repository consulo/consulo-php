package consulo.php.lang.annotator;

import consulo.php.PhpBundle;
import consulo.php.lang.highlighter.PhpHighlightingData;
import consulo.php.lang.psi.PhpClassReference;
import consulo.php.lang.psi.PhpConstantReference;
import consulo.php.lang.psi.PhpField;
import consulo.php.lang.psi.PhpVariableReference;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;

/**
 * @author jay
 * @date Apr 11, 2008 10:44:55 AM
 */
public class PhpAnnotatorVisitor extends PhpElementVisitor
{

	private AnnotationHolder holder;

	public PhpAnnotatorVisitor(AnnotationHolder holder)
	{
		this.holder = holder;
	}

	@Override
	public void visitField(PhpField phpField)
	{
		if(phpField.isConstant())
		{
			PsiElement nameIdentifier = phpField.getNameIdentifier();
			if(nameIdentifier != null)
			{
				holder.createInfoAnnotation(nameIdentifier, "").setTextAttributes(PhpHighlightingData.CONSTANT);
			}
		}
	}

	@Override
	@SuppressWarnings({"ConstantConditions"})
	public void visitVariableReference(PhpVariableReference variable)
	{
		if(variable.canReadName())
		{
			if(variable.getName() != null && (variable.getName().equals("this")))
			{
				// todo: extract this to colors page maybe
				//        Annotation annotation = holder.createInfoAnnotation(variable, null);
				//        TextAttributes textAttributes = annotation.getTextAttributes().getDefaultAttributes();
				//        textAttributes.setFontType(Font.BOLD);
				//        annotation.setTextAttributes(TextAttributesKey.createTextAttributesKey("php.this_keyword", textAttributes));
			}
		}
		else
		{
			holder.createInfoAnnotation(variable, PhpBundle.message("annotation.variable.variable"));
		}
	}

	@Override
	public void visitClassReference(PhpClassReference classReference)
	{
		if(classReference.getText().equals("self") || classReference.getText().equals("parent"))
		{
			Annotation annotation = holder.createInfoAnnotation(classReference, null);
			TextAttributesKey keyword = PhpHighlightingData.KEYWORD;
			annotation.setTextAttributes(keyword);
		}
	}

	@Override
	public void visitConstant(PhpConstantReference constant)
	{
		if(constant.getText().equalsIgnoreCase("true") || constant.getText().equalsIgnoreCase("false") || constant.getText().equalsIgnoreCase("null"))
		{
			Annotation annotation = holder.createInfoAnnotation(constant, null);
			TextAttributesKey keyword = PhpHighlightingData.KEYWORD;
			annotation.setTextAttributes(keyword);
		}
	}
}
