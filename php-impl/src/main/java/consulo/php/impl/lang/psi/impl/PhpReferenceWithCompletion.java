package consulo.php.impl.lang.psi.impl;

import javax.annotation.Nonnull;

import consulo.application.util.function.Processor;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import consulo.annotation.access.RequiredReadAction;
import consulo.php.impl.completion.ClassUsageContext;
import consulo.php.impl.completion.UsageContext;

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
