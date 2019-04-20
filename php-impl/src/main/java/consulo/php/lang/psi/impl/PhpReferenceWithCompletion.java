package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;

import com.intellij.util.Processor;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import consulo.annotations.RequiredReadAction;
import consulo.php.completion.ClassUsageContext;
import consulo.php.completion.UsageContext;

/**
 * @author VISTALL
 * @since 2019-04-14
 */
public interface PhpReferenceWithCompletion
{
	@RequiredReadAction
	void processForCompletion(@RequiredReadAction @Nonnull Processor<PhpNamedElement> elementProcessor);

	default UsageContext createUsageContext()
	{
		return new UsageContext();
	}

	default ClassUsageContext createClassUsageContext()
	{
		return new ClassUsageContext();
	}
}
