package org.consulo.php.lang.annotator;

import org.consulo.php.PhpBundle;
import org.consulo.php.lang.highlighter.PhpHighlightingData;
import org.consulo.php.lang.psi.elements.PhpClassReference;
import org.consulo.php.lang.psi.elements.ConstantReference;
import org.consulo.php.lang.psi.elements.PhpVariableReference;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.openapi.editor.colors.TextAttributesKey;

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

	public void visitPhpClassReference(PhpClassReference classReference)
	{
		if(classReference.getText().equals("self") || classReference.getText().equals("parent"))
		{
			Annotation annotation = holder.createInfoAnnotation(classReference, null);
			TextAttributesKey keyword = PhpHighlightingData.KEYWORD;
			annotation.setTextAttributes(keyword);
		}
	}

	public void visitPhpConstant(ConstantReference constant)
	{
		if(constant.getText().equalsIgnoreCase("true") || constant.getText().equalsIgnoreCase("false") || constant.getText().equalsIgnoreCase("null"))
		{
			Annotation annotation = holder.createInfoAnnotation(constant, null);
			TextAttributesKey keyword = PhpHighlightingData.KEYWORD;
			annotation.setTextAttributes(keyword);
		}
	}
}
