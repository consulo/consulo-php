package org.consulo.php.psi.impl;

import com.intellij.lang.Language;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.file.PsiPackageBase;
import com.intellij.util.ArrayFactory;
import org.consulo.php.lang.PhpLanguage;
import org.consulo.module.extension.ModuleExtension;
import org.consulo.php.psi.PhpPackage;
import org.consulo.psi.PsiPackage;
import org.consulo.psi.PsiPackageManager;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

/**
 * @author VISTALL
 * @since 07.07.13.
 */
public class PhpPackageImpl extends PsiPackageBase implements PhpPackage {
	public PhpPackageImpl(PsiManager manager, PsiPackageManager packageManager, Class<? extends ModuleExtension> extensionClass, String qualifiedName) {
		super(manager, packageManager, extensionClass, qualifiedName);
	}

	@Override
	protected Collection<PsiDirectory> getAllDirectories() {
		return Collections.emptyList();
	}

	@Override
	protected ArrayFactory<? extends PsiPackage> getPackageArrayFactory() {
		return PhpPackage.ARRAY_FACTORY;
	}

	@NotNull
	@Override
	public Language getLanguage() {
		return PhpLanguage.INSTANCE;
	}
}
