package consulo.php.impl.run.script;

import consulo.annotation.access.RequiredReadAction;
import consulo.compiler.execution.CompileStepBeforeRun;
import consulo.content.bundle.Sdk;
import consulo.execution.CommonProgramRunConfigurationParameters;
import consulo.execution.DefaultExecutionResult;
import consulo.execution.ExecutionResult;
import consulo.execution.configuration.*;
import consulo.execution.configuration.ui.SettingsEditor;
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
import consulo.util.xml.serializer.DefaultJDOMExternalizer;
import consulo.util.xml.serializer.InvalidDataException;
import consulo.util.xml.serializer.WriteExternalException;
import org.jdom.Element;

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
	public String SCRIPT_PATH;
	public String PROGRAM_ARGUMENTS;
	public String WORKING_DIRECTORY;

	public Map<String, String> ENVS = new HashMap<>();
	public boolean PASS_PARENT_ENVS = true;

	public PhpScriptConfiguration(Project project, ConfigurationFactory factory)
	{
		super(new RunConfigurationModule(project), factory);
	}

	@Override
	public String suggestedName()
	{
		if(StringUtil.isEmptyOrSpaces(SCRIPT_PATH))
		{
			return null;
		}
		File file = new File(SCRIPT_PATH);
		return file.getName();
	}

	@Override
	public void readExternal(Element element) throws InvalidDataException
	{
		super.readExternal(element);
		DefaultJDOMExternalizer.readExternal(this, element);
	}

	@Override
	public void writeExternal(Element element) throws WriteExternalException
	{
		super.writeExternal(element);
		DefaultJDOMExternalizer.writeExternal(this, element);
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

	@Nonnull
	@Override
	public SettingsEditor<? extends RunConfiguration> getConfigurationEditor()
	{
		return new PhpScriptSettingsEditor(getProject());
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
				args.add(FileUtil.toSystemDependentName(conf.SCRIPT_PATH));
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

	@Override
	public void setProgramParameters(@Nullable String s)
	{
		PROGRAM_ARGUMENTS = s;
	}

	@Nullable
	@Override
	public String getProgramParameters()
	{
		return PROGRAM_ARGUMENTS;
	}

	@Override
	public void setWorkingDirectory(@Nullable String s)
	{
		WORKING_DIRECTORY = s;
	}

	@Nullable
	@Override
	public String getWorkingDirectory()
	{
		return WORKING_DIRECTORY;
	}

	@Override
	public void setEnvs(@Nonnull Map<String, String> map)
	{
		ENVS = map;
	}

	@Nonnull
	@Override
	public Map<String, String> getEnvs()
	{
		return ENVS;
	}

	@Override
	public void setPassParentEnvs(boolean b)
	{
		PASS_PARENT_ENVS = b;
	}

	@Override
	public boolean isPassParentEnvs()
	{
		return PASS_PARENT_ENVS;
	}
}
