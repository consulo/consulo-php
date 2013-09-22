package org.consulo.php;

import org.consulo.php.completion.PhpCompletionData;
import org.consulo.php.lang.PhpFileType;
import org.jetbrains.annotations.NotNull;
import com.intellij.codeInsight.completion.CompletionUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 22.02.2007
 *
 * @author jay
 */
public class PhpSupportApplicationComponent implements ApplicationComponent
{

	public PhpSupportApplicationComponent()
	{
	}

	@Override
	public void initComponent()
	{
		ApplicationManager.getApplication().runWriteAction(new Runnable()
		{
			@Override
			public void run()
			{
				CompletionUtil.registerCompletionData(PhpFileType.INSTANCE, new PhpCompletionData());
			}
		});
	}

	@Override
	public void disposeComponent()
	{
	}

	@Override
	@NotNull
	public String getComponentName()
	{
		return "PhpSupportApplicationComponent";
	}
}
