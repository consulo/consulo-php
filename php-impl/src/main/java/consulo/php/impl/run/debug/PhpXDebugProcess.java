package consulo.php.impl.run.debug;

import consulo.content.bundle.Sdk;
import consulo.execution.debug.XDebugProcess;
import consulo.execution.debug.XDebugSession;
import consulo.execution.debug.XSourcePosition;
import consulo.execution.debug.breakpoint.XBreakpointHandler;
import consulo.execution.debug.breakpoint.XLineBreakpoint;
import consulo.execution.debug.evaluation.XDebuggerEditorsProvider;
import consulo.execution.debug.frame.XSuspendContext;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.execution.ui.console.ConsoleView;
import consulo.execution.ui.console.TextConsoleBuilder;
import consulo.execution.ui.console.TextConsoleBuilderFactory;
import consulo.language.util.ModuleUtilCore;
import consulo.logging.Logger;
import consulo.module.Module;
import consulo.php.impl.run.script.PhpScriptConfiguration;
import consulo.php.impl.xdebug.*;
import consulo.php.impl.xdebug.connection.DbgpClient;
import consulo.php.impl.xdebug.connection.DbgpCommandSender;
import consulo.php.impl.xdebug.connection.DbgpResponse;
import consulo.php.module.extension.PhpModuleExtension;
import consulo.php.sdk.PhpSdkType;
import consulo.process.ExecutionException;
import consulo.process.ProcessHandler;
import consulo.process.event.ProcessEvent;
import consulo.process.event.ProcessListener;
import consulo.process.cmd.GeneralCommandLine;
import consulo.process.local.ProcessHandlerFactory;
import consulo.util.io.FileUtil;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.VirtualFile;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class PhpXDebugProcess extends XDebugProcess implements DbgpClient.Listener, PhpBreakpointListener {
    private static final Logger LOG = Logger.getInstance(PhpXDebugProcess.class);
    private static final int DEFAULT_PORT = 9003;

    private final ExecutionEnvironment myEnvironment;
    private final DbgpClient myClient;
    private final DbgpCommandSender myCommandSender;
    private final PhpDebuggerEditorsProvider myEditorsProvider = new PhpDebuggerEditorsProvider();
    private final XBreakpointHandler<?>[] myBreakpointHandlers;

    // breakpoint ID from Xdebug -> XLineBreakpoint
    private final Map<String, XLineBreakpoint<PhpLineBreakpointProperties>> myDbgpBreakpointIdMap = new ConcurrentHashMap<>();
    // file:line -> XLineBreakpoint (for matching break events)
    private final Map<String, XLineBreakpoint<PhpLineBreakpointProperties>> myPositionBreakpointMap = new ConcurrentHashMap<>();
    // XLineBreakpoint -> Xdebug breakpoint ID
    private final Map<XLineBreakpoint<PhpLineBreakpointProperties>, String> myBreakpointToIdMap = new ConcurrentHashMap<>();

    private volatile ProcessHandler myProcessHandler;
    private volatile boolean myConnected;

    public PhpXDebugProcess(@Nonnull XDebugSession session, @Nonnull ExecutionEnvironment environment) throws ExecutionException {
        super(session);
        myEnvironment = environment;
        myClient = new DbgpClient(DEFAULT_PORT, this);
        myCommandSender = new DbgpCommandSender(myClient);
        myBreakpointHandlers = new XBreakpointHandler[]{new PhpLineBreakpointHandler(this)};

        session.setPauseActionSupported(false);
    }

    @Nonnull
    DbgpCommandSender getCommandSender() {
        return myCommandSender;
    }

    @Nonnull
    @Override
    public XBreakpointHandler<?>[] getBreakpointHandlers() {
        return myBreakpointHandlers;
    }

    @Nonnull
    @Override
    public XDebuggerEditorsProvider getEditorsProvider() {
        return myEditorsProvider;
    }

    @Override
    public void sessionInitialized() {
        try {
            myClient.startListening();
            LOG.info("Xdebug listener started on port " + myClient.getPort());

            launchPhpProcess();
        }
        catch (Exception e) {
            LOG.error("Failed to start debug session", e);
            getSession().reportError("Failed to start debug session: " + e.getMessage());
            getSession().stop();
        }
    }

    private void launchPhpProcess() throws ExecutionException {
        PhpScriptConfiguration config = (PhpScriptConfiguration) myEnvironment.getRunProfile();

        Module module = config.getConfigurationModule().getModule();
        if (module == null) {
            throw new ExecutionException("Module is not selected");
        }

        Sdk sdk = ModuleUtilCore.getSdk(module, PhpModuleExtension.class);
        if (sdk == null) {
            throw new ExecutionException("PHP SDK is not selected");
        }

        String executableFile = PhpSdkType.getExecutableFile(sdk.getHomePath());

        GeneralCommandLine commandLine = new GeneralCommandLine();
        commandLine.withExePath(FileUtil.toSystemDependentName(executableFile));
        commandLine.withWorkDirectory(StringUtil.nullize(config.getWorkingDirectory()));
        commandLine.withEnvironment(config.getEnvs());

        // add xdebug environment variables
        commandLine.withEnvironment("XDEBUG_MODE", "debug");
        commandLine.withEnvironment("XDEBUG_CONFIG",
            "client_host=127.0.0.1 client_port=" + myClient.getPort() + " idekey=consulo");
        commandLine.withEnvironment("XDEBUG_SESSION", "consulo");

        List<String> args = new ArrayList<>();
        args.add(FileUtil.toSystemDependentName(StringUtil.notNullize(config.getScriptPath())));
        args.addAll(StringUtil.split(StringUtil.notNullize(config.getProgramParameters()), " "));
        commandLine.withParameters(args);
        commandLine.withParentEnvironmentType(
            config.isPassParentEnvs()
                ? GeneralCommandLine.ParentEnvironmentType.SYSTEM
                : GeneralCommandLine.ParentEnvironmentType.NONE
        );

        TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(myEnvironment.getProject());
        ConsoleView console = consoleBuilder.getConsole();
        myProcessHandler = ProcessHandlerFactory.getInstance().createProcessHandler(commandLine);
        console.attachToProcess(myProcessHandler);

        getSession().getRunContentDescriptor().setProcessHandler(myProcessHandler);
        myProcessHandler.addProcessListener(new ProcessListener() {
            @Override
            public void processTerminated(ProcessEvent event) {
                // the session may end with the process only, the listener must not keep the port
                myClient.stop();
            }
        });
        myProcessHandler.startNotify();
    }

    // Breakpoint management

    public void addBreakpoint(@Nonnull XLineBreakpoint<PhpLineBreakpointProperties> breakpoint) {
        VirtualFile file = breakpoint.getFile();
        if (file == null) {
            return;
        }

        String fileUri = PhpDebugUtil.toFileUri(file);
        int line = breakpoint.getLine() + 1; // convert 0-based to 1-based
        String key = PhpDebugUtil.makeBreakpointKey(fileUri, line);
        myPositionBreakpointMap.put(key, breakpoint);

        if (myConnected) {
            setRemoteBreakpoint(breakpoint, fileUri, line);
        }
    }

    public void removeBreakpoint(@Nonnull XLineBreakpoint<PhpLineBreakpointProperties> breakpoint) {
        VirtualFile file = breakpoint.getFile();
        if (file == null) {
            return;
        }

        String fileUri = PhpDebugUtil.toFileUri(file);
        int line = breakpoint.getLine() + 1;
        String key = PhpDebugUtil.makeBreakpointKey(fileUri, line);
        myPositionBreakpointMap.remove(key);

        String dbgpId = myBreakpointToIdMap.remove(breakpoint);
        if (dbgpId != null && myConnected) {
            myDbgpBreakpointIdMap.remove(dbgpId);
            myCommandSender.breakpointRemove(dbgpId).whenComplete((response, error) -> {
                if (error != null) {
                    LOG.warn("Failed to remove breakpoint " + dbgpId, error);
                }
            });
        }
    }

    private void setRemoteBreakpoint(@Nonnull XLineBreakpoint<PhpLineBreakpointProperties> breakpoint,
                                     @Nonnull String fileUri, int line) {
        myCommandSender.breakpointSet(fileUri, line).whenComplete((response, error) -> {
            if (error != null) {
                LOG.warn("Failed to set breakpoint at " + fileUri + ":" + line, error);
                getSession().updateBreakpointPresentation(breakpoint, null, "Failed to set");
                return;
            }
            if (response.hasError()) {
                getSession().updateBreakpointPresentation(breakpoint, null, response.getErrorMessage());
                return;
            }
            String bpId = response.getBreakpointId();
            if (bpId != null) {
                myDbgpBreakpointIdMap.put(bpId, breakpoint);
                myBreakpointToIdMap.put(breakpoint, bpId);
                getSession().setBreakpointVerified(breakpoint);
            }
        });
    }

    // Stepping commands

    @Override
    public void startStepOver(@Nullable XSuspendContext context) {
        handleContinuationResponse(myCommandSender.stepOver());
    }

    @Override
    public void startStepInto(@Nullable XSuspendContext context) {
        handleContinuationResponse(myCommandSender.stepInto());
    }

    @Override
    public void startStepOut(@Nullable XSuspendContext context) {
        handleContinuationResponse(myCommandSender.stepOut());
    }

    @Override
    public void resume(@Nullable XSuspendContext context) {
        handleContinuationResponse(myCommandSender.run());
    }

    @Override
    public void runToPosition(@Nonnull XSourcePosition position, @Nullable XSuspendContext context) {
        VirtualFile file = position.getFile();
        String fileUri = PhpDebugUtil.toFileUri(file);
        int line = position.getLine() + 1;

        // set a temporary breakpoint and run
        myCommandSender.breakpointSet(fileUri, line).whenComplete((response, error) -> {
            if (error == null && !response.hasError()) {
                // temporary breakpoint set successfully
            }
            handleContinuationResponse(myCommandSender.run());
        });
    }

    @Override
    public void stop() {
        if (myConnected) {
            try {
                myCommandSender.stop();
            }
            catch (Exception e) {
                LOG.debug("Error sending stop command", e);
            }
        }
        myClient.stop();

        ProcessHandler handler = myProcessHandler;
        if (handler != null && !handler.isProcessTerminated()) {
            handler.destroyProcess();
        }
    }

    private void handleContinuationResponse(CompletableFuture<DbgpResponse> future) {
        // off the Xdebug reader thread: onBreak waits for stack_get, which the reader thread reads
        future.whenCompleteAsync((response, error) -> {
            if (error != null) {
                LOG.warn("Continuation command error", error);
                return;
            }
            String status = response.getStatus();
            if ("break".equals(status)) {
                onBreak(response);
            }
            else if ("stopping".equals(status) || "stopped".equals(status)) {
                // the script ended: let Xdebug finish the PHP process, then end the session
                if ("stopping".equals(status) && myConnected) {
                    myCommandSender.stop();
                }
                getSession().stop();
            }
        });
    }

    private void onBreak(@Nonnull DbgpResponse response) {
        PhpSuspendContext suspendContext = new PhpSuspendContext(myCommandSender);

        // try to match breakpoint
        try {
            DbgpResponse stackResponse = myCommandSender.stackGet().get();
            if (!stackResponse.hasError()) {
                List<Element> stackElements = stackResponse.getStackElements();
                if (!stackElements.isEmpty()) {
                    Element topFrame = stackElements.get(0);
                    String filename = topFrame.getAttribute("filename");
                    String linenoStr = topFrame.getAttribute("lineno");
                    if (filename != null && linenoStr != null) {
                        int lineno = Integer.parseInt(linenoStr);
                        String key = PhpDebugUtil.makeBreakpointKey(filename, lineno);
                        XLineBreakpoint<PhpLineBreakpointProperties> breakpoint = myPositionBreakpointMap.get(key);
                        if (breakpoint != null) {
                            boolean shouldSuspend = getSession().breakpointReached(breakpoint, null, suspendContext);
                            if (!shouldSuspend) {
                                resume(suspendContext);
                            }
                            return;
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            LOG.warn("Error matching breakpoint", e);
        }

        // no breakpoint matched - just position reached (stepping)
        getSession().positionReached(suspendContext);
    }

    // DbgpClient.Listener

    @Override
    public void onConnected(@Nonnull DbgpResponse initPacket) {
        myConnected = true;
        LOG.info("Xdebug connected: " + initPacket.getAttribute("fileuri"));

        // negotiate features
        myCommandSender.featureSet("show_hidden", "1");
        myCommandSender.featureSet("max_depth", "1");
        myCommandSender.featureSet("max_children", "100");
        myCommandSender.featureSet("max_data", "2048");

        // redirect stdout to IDE
        myCommandSender.stdout(1);

        // set all pending breakpoints
        for (Map.Entry<String, XLineBreakpoint<PhpLineBreakpointProperties>> entry : myPositionBreakpointMap.entrySet()) {
            XLineBreakpoint<PhpLineBreakpointProperties> bp = entry.getValue();
            VirtualFile file = bp.getFile();
            if (file != null) {
                String fileUri = PhpDebugUtil.toFileUri(file);
                int line = bp.getLine() + 1;
                setRemoteBreakpoint(bp, fileUri, line);
            }
        }

        // start execution
        handleContinuationResponse(myCommandSender.run());
    }

    @Override
    public void onDisconnected() {
        myConnected = false;
        LOG.info("Xdebug disconnected");
        // one connection per session: free the port for the next debug session
        myClient.stop();
    }

    @Override
    public void onStreamOutput(@Nonnull String type, @Nonnull String data) {
        // stream output is already shown via process handler stdout
    }

    @Override
    public void onStatusChanged(@Nonnull String status, @Nullable String reason, @Nullable String command) {
        // status handling is done in handleContinuationResponse
    }
}
