package consulo.php.impl.gotoByName;

import com.jetbrains.php.lang.psi.elements.PhpClass;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.util.function.Processor;
import consulo.content.scope.SearchScope;
import consulo.ide.navigation.GotoClassOrTypeContributor;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.search.FindSymbolParameters;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.language.psi.stub.IdFilter;
import consulo.language.psi.stub.StubIndex;
import consulo.navigation.NavigationItem;
import consulo.php.impl.index.PhpIndexKeys;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author jay
 * @date May 30, 2008 3:44:59 PM
 */
@ExtensionImpl
public class PhpClassContributor implements GotoClassOrTypeContributor
{
	@Override
	public void processNames(@Nonnull Processor<String> processor, @Nonnull SearchScope searchScope, @Nullable IdFilter idFilter)
	{
		FileBasedIndex.getInstance().processAllKeys(PhpIndexKeys.FULL_FQ_CLASSES, processor, searchScope, idFilter);
	}

	@Override
	public void processElementsWithName(@Nonnull String s, @Nonnull Processor<NavigationItem> processor, @Nonnull FindSymbolParameters findSymbolParameters)
	{
		StubIndex.getInstance().processElements(PhpIndexKeys.FULL_FQ_CLASSES, s, findSymbolParameters.getProject(), (GlobalSearchScope) findSymbolParameters.getSearchScope(), findSymbolParameters
						.getIdFilter(), PhpClass.class, processor);
	}
}
