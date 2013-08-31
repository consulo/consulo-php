package org.consulo.php;

import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlTagValue;
import com.intellij.psi.xml.XmlText;
import net.jay.plugins.php.lang.PHPLanguage;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public class PhpMultiHostInjector implements MultiHostInjector {
	@Override
	public void injectLanguages(@NotNull MultiHostRegistrar multiHostRegistrar, @NotNull PsiElement element) {
		XmlTag xmlTag = (XmlTag) element;
		if (!"script".equals(xmlTag.getName())) {
			return;
		}

		String language = xmlTag.getAttributeValue("language");
		if (!"php".equals(language)) {
			return;
		}

		XmlTagValue value = xmlTag.getValue();
		if (value.getTrimmedText().isEmpty()) {
			return;
		}
		XmlText[] textElements = value.getTextElements();
		if (textElements.length != 1) {
			return;
		}

		XmlText textElement = textElements[0];

		multiHostRegistrar
				.startInjecting(PHPLanguage.INSTANCE)
				.addPlace("<?php", "?>", (PsiLanguageInjectionHost) textElement, new TextRange(0, textElement.getTextLength()))
				.doneInjecting();
	}
}
