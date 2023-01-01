package consulo.php.impl;

import com.jetbrains.php.lang.PhpLanguage;
import consulo.annotation.component.ExtensionImpl;
import consulo.document.util.TextRange;
import consulo.language.inject.MultiHostInjector;
import consulo.language.inject.MultiHostRegistrar;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiLanguageInjectionHost;
import consulo.xml.psi.xml.XmlTag;
import consulo.xml.psi.xml.XmlTagValue;
import consulo.xml.psi.xml.XmlText;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
@ExtensionImpl
public class PhpMultiHostInjector implements MultiHostInjector
{
	@Nonnull
	@Override
	public Class<? extends PsiElement> getElementClass()
	{
		return XmlTag.class;
	}

	@Override
	public void injectLanguages(@Nonnull MultiHostRegistrar multiHostRegistrar, @Nonnull PsiElement element)
	{
		XmlTag xmlTag = (XmlTag) element;
		if(!"script".equals(xmlTag.getName()))
		{
			return;
		}

		String language = xmlTag.getAttributeValue("language");
		if(!"php".equals(language))
		{
			return;
		}

		XmlTagValue value = xmlTag.getValue();
		if(value.getTrimmedText().isEmpty())
		{
			return;
		}
		XmlText[] textElements = value.getTextElements();
		if(textElements.length != 1)
		{
			return;
		}

		XmlText textElement = textElements[0];

		multiHostRegistrar.startInjecting(PhpLanguage.INSTANCE).addPlace("<?php", "?>", (PsiLanguageInjectionHost) textElement, new TextRange(0, textElement.getTextLength())).doneInjecting();
	}
}
