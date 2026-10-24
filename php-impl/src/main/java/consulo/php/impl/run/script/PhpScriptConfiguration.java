package consulo.php.impl.run.script;

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
import consulo.process.local.ProcessHandlerFactory;
import consulo.project.Project;
import consulo.util.io.FileUtil;
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

				String executableFile = PhpSdkType.getExecutableFile(sdk.getHomePath());

				GeneralCommandLine commandLine = new GeneralCommandLine();
				commandLine.withExePath(FileUtil.toSystemDependentName(executableFile));
				commandLine.withWorkDirectory(StringUtil.nullize(conf.getWorkingDirectory()));
				commandLine.withEnvironment(conf.getEnvs());
				List<String> args = new ArrayList<>();
				args.add(FileUtil.toSystemDependentName(conf.getScriptPath()));
				args.addAll(StringUtil.split(StringUtil.notNullize(conf.getProgramParameters()), " "));
				commandLine.withParameters(args);
				commandLine.withParentEnvironmentType(conf.isPassParentEnvs() ? GeneralCommandLine.ParentEnvironmentType.SYSTEM : GeneralCommandLine.ParentEnvironmentType.NONE);

				TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(environment.getProject());
				ConsoleView console = consoleBuilder.getConsole();
				ProcessHandler processHandler = ProcessHandlerFactory.getInstance().createProcessHandler(commandLine);
				console.attachToProcess(processHandler);
				return new DefaultExecutionResult(console, processHandler);
			}
		};
	}
}
