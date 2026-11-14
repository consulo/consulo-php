package consulo.php.impl.run.script;

import consulo.platform.Platform;
import consulo.process.ProcessHandlerBuilder;
import consulo.annotation.access.RequiredReadAction;
import consulo.compiler.execution.CompileStepBeforeRun;
import consulo.content.bundle.Sdk;
import consulo.execution.CommonProgramRunConfigurationParameters;
import consulo.execution.DefaultExecutionResult;
import consulo.execution.ExecutionResult;
import consulo.execution.configuration.*;
import consulo.execution.configuration.fragment.EnvironmentFragmentState;
import consulo.execution.configuration.fragment.ProgramParametersFragmentState;
import consulo.execution.configuration.fragment.RunConfigurationFragment;
import consulo.execution.configuration.fragment.WorkingDirectoryFragmentState;
import consulo.execution.executor.Executor;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.execution.runner.ProgramRunner;
import consulo.execution.ui.console.ConsoleView;
import consulo.execution.ui.console.TextConsoleBuilder;
import consulo.execution.ui.console.TextConsoleBuilderFactory;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.module.ModuleManager;
import consulo.php.module.extension.PhpModuleExtension;
import consulo.php.sdk.PhpSdkType;
import consulo.process.ExecutionException;
import consulo.process.ProcessHandler;
import consulo.process.cmd.GeneralCommandLine;
import consulo.project.Project;
import consulo.util.lang.StringUtil;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.io.File;
import java.util.*;

/**
 * @author VISTALL
 * @since 2019-04-21
 */
public class PhpScriptConfiguration extends ModuleBasedConfiguration<RunConfigurationModule> implements CommonProgramRunConfigurationParameters, CompileStepBeforeRun.Suppressor
{
	public PhpScriptConfiguration(Project project, ConfigurationFactory factory)
	{
		super(new RunConfigurationModule(project), factory);
	}

	@Override
	protected Class<PhpScriptConfigurationOptions> getOptionsClass()
	{
		return PhpScriptConfigurationOptions.class;
	}

	@Override
	public PhpScriptConfigurationOptions getOptions()
	{
		return (PhpScriptConfigurationOptions) super.getOptions();
	}

	@Override
	protected void convertLegacyState(LegacyRunConfigurationConverter converter)
	{
		super.convertLegacyState(converter);

		PhpScriptConfigurationLegacyState old = converter.readXml(PhpScriptConfigurationLegacyState.class);

		PhpScriptConfigurationOptions options = new PhpScriptConfigurationOptions();
		options.setScriptPath(old.SCRIPT_PATH);
		converter.setOptions(options);

		converter.addFragment(new ProgramParametersFragmentState(old.PROGRAM_ARGUMENTS));
		converter.addFragment(new WorkingDirectoryFragmentState(old.WORKING_DIRECTORY));
		converter.addFragment(new EnvironmentFragmentState(converter.readEnvs(), old.PASS_PARENT_ENVS));
	}

	@Override
	public List<RunConfigurationFragment<?>> createFragments()
	{
		return List.of(new PhpScriptPathFragment());
	}

	@Nullable
	public String getScriptPath()
	{
		return getOptions().getScriptPath();
	}

	public void setScriptPath(@Nullable String scriptPath)
	{
		getOptions().setScriptPath(scriptPath);
	}

	@Override
	public String suggestedName()
	{
		String scriptPath = getScriptPath();
		if(StringUtil.isEmptyOrSpaces(scriptPath))
		{
			return null;
		}
		return new File(scriptPath).getName();
	}

	@Override
	@RequiredReadAction
	public Collection<Module> getValidModules()
	{
		List<Module> result = new ArrayList<>();
		Module[] modules = ModuleManager.getInstance(getProject()).getModules();
		for(Module module : modules)
		{
			PhpModuleExtension<?> moduleExtension = ModuleUtilCore.getExtension(module, PhpModuleExtension.class);
			if(moduleExtension != null)
			{
				result.add(module);
			}
		}
		return result;
	}

	@Nullable
	@Override
	public RunProfileState getState(@Nonnull Executor executor, @Nonnull ExecutionEnvironment environment) throws ExecutionException
	{
		return new RunProfileState()
		{
			@Nullable
			@Override
			public ExecutionResult execute(Executor executor, @Nonnull ProgramRunner runner) throws ExecutionException
			{
				PhpScriptConfiguration conf = (PhpScriptConfiguration) environment.getRunProfile();

				Module module = conf.getConfigurationModule().getModule();
				if(module == null)
				{
					throw new ExecutionException("Module is not selected");
				}

				Sdk sdk = ModuleUtilCore.getSdk(module, PhpModuleExtension.class);
				if(sdk == null)
				{
					throw new ExecutionException("PHP SDK is not selected");
				}

				GeneralCommandLine commandLine = conf.createCommandLine(sdk);

				TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(environment.getProject());
				ConsoleView console = consoleBuilder.getConsole();
				ProcessHandler processHandler = ProcessHandlerBuilder.create(commandLine).build();
				console.attachToProcess(processHandler);
				return new DefaultExecutionResult(console, processHandler);
			}
		};
	}

	/**
	 * The script with the interpreter of {@code sdk} - on the machine of the SDK, the paths are the ones of that machine
	 */
	public GeneralCommandLine createCommandLine(Sdk sdk)
	{
		Platform platform = sdk.getPlatform();

		GeneralCommandLine commandLine = new GeneralCommandLine();
		commandLine.withPlatform(platform);
		commandLine.withExecutablePath(PhpSdkType.getExecutable(sdk));
		String workingDirectory = StringUtil.nullize(getWorkingDirectory());
		if(workingDirectory != null)
		{
			commandLine.withWorkingDirectory(platform.fs().getPath(workingDirectory));
		}
		commandLine.withEnvironment(getEnvs());
		List<String> args = new ArrayList<>();
		args.add(platform.fs().getPath(StringUtil.notNullize(getScriptPath())).toString());
		args.addAll(StringUtil.split(StringUtil.notNullize(getProgramParameters()), " "));
		commandLine.withParameters(args);
		commandLine.withParentEnvironmentType(isPassParentEnvs() ? GeneralCommandLine.ParentEnvironmentType.SYSTEM : GeneralCommandLine.ParentEnvironmentType.NONE);
		return commandLine;
	}
}
