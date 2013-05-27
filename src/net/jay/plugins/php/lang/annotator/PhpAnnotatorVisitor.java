package net.jay.plugins.php.lang.annotator;

import net.jay.plugins.php.PHPBundle;
import net.jay.plugins.php.lang.highlighter.PHPHighlightingData;
import net.jay.plugins.php.lang.psi.elements.ClassReference;
import net.jay.plugins.php.lang.psi.elements.ConstantReference;
import net.jay.plugins.php.lang.psi.elements.Variable;
import net.jay.plugins.php.lang.psi.visitors.PHPElementVisitor;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.openapi.editor.colors.TextAttributesKey;

/**
 * @author jay
 * @date Apr 11, 2008 10:44:55 AM
 */
public class PhpAnnotatorVisitor extends PHPElementVisitor
{

	private AnnotationHolder holder;

	public PhpAnnotatorVisitor(AnnotationHolder holder)
	{
		this.holder = holder;
	}

	@SuppressWarnings({"ConstantConditions"})
	public void visitPhpVariable(Variable variable)
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
			holder.createInfoAnnotation(variable, PHPBundle.message("annotation.variable.variable"));
		}
	}

	public void visitPhpClassReference(ClassReference classReference)
	{
		if(classReference.getText().equals("self") || classReference.getText().equals("parent"))
		{
			Annotation annotation = holder.createInfoAnnotation(classReference, null);
			TextAttributesKey keyword = PHPHighlightingData.KEYWORD;
			annotation.setTextAttributes(keyword);
		}
	}

	public void visitPhpConstant(ConstantReference constant)
	{
		if(constant.getText().equalsIgnoreCase("true") || constant.getText().equalsIgnoreCase("false") || constant.getText().equalsIgnoreCase("null"))
		{
			Annotation annotation = holder.createInfoAnnotation(constant, null);
			TextAttributesKey keyword = PHPHighlightingData.KEYWORD;
			annotation.setTextAttributes(keyword);
		}
	}
}
