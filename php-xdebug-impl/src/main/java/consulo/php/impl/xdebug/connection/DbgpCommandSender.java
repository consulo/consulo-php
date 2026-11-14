package consulo.php.impl.xdebug.connection;

import consulo.platform.Platform;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.concurrent.CompletableFuture;

public class DbgpCommandSender {
    private final DbgpClient myClient;

    /**
     * The machine of the debugged script - its paths are the ones of the file uris.
     */
    @Nonnull
    public Platform getPlatform() {
        return myClient.getPlatform();
    }

    public DbgpCommandSender(@Nonnull DbgpClient client) {
        myClient = client;
    }

    // Feature negotiation

    @Nonnull
    public CompletableFuture<DbgpResponse> featureGet(@Nonnull String name) {
        return myClient.sendCommand("feature_get -n " + name);
    }

    @Nonnull
    public CompletableFuture<DbgpResponse> featureSet(@Nonnull String name, @Nonnull String value) {
        return myClient.sendCommand("feature_set -n " + name + " -v " + value);
    }

    // Continuation commands

    @Nonnull
    public CompletableFuture<DbgpResponse> run() {
        return myClient.sendCommand("run");
    }

    @Nonnull
    public CompletableFuture<DbgpResponse> stepInto() {
        return myClient.sendCommand("step_into");
    }

    @Nonnull
    public CompletableFuture<DbgpResponse> stepOver() {
        return myClient.sendCommand("step_over");
    }

    @Nonnull
    public CompletableFuture<DbgpResponse> stepOut() {
        return myClient.sendCommand("step_out");
    }

    @Nonnull
    public CompletableFuture<DbgpResponse> stop() {
        return myClient.sendCommand("stop");
    }

    @Nonnull
    public CompletableFuture<DbgpResponse> detach() {
        return myClient.sendCommand("detach");
    }

    // Breakpoint commands

    @Nonnull
    public CompletableFuture<DbgpResponse> breakpointSet(@Nonnull String fileUri, int line) {
        return myClient.sendCommand("breakpoint_set -t line -f " + fileUri + " -n " + line);
    }

    @Nonnull
    public CompletableFuture<DbgpResponse> breakpointSetConditional(@Nonnull String fileUri, int line, @Nonnull String expression) {
        return myClient.sendCommand("breakpoint_set -t conditional -f " + fileUri + " -n " + line, expression);
    }

    @Nonnull
    public CompletableFuture<DbgpResponse> breakpointSetException(@Nonnull String exceptionName) {
        return myClient.sendCommand("breakpoint_set -t exception -x " + exceptionName);
    }

    @Nonnull
    public CompletableFuture<DbgpResponse> breakpointRemove(@Nonnull String breakpointId) {
        return myClient.sendCommand("breakpoint_remove -d " + breakpointId);
    }

    @Nonnull
    public CompletableFuture<DbgpResponse> breakpointGet(@Nonnull String breakpointId) {
        return myClient.sendCommand("breakpoint_get -d " + breakpointId);
    }

    @Nonnull
    public CompletableFuture<DbgpResponse> breakpointList() {
        return myClient.sendCommand("breakpoint_list");
    }

    // Stack commands

    @Nonnull
    public CompletableFuture<DbgpResponse> stackGet() {
        return myClient.sendCommand("stack_get");
    }

    @Nonnull
    public CompletableFuture<DbgpResponse> stackGet(int depth) {
        return myClient.sendCommand("stack_get -d " + depth);
    }

    @Nonnull
    public CompletableFuture<DbgpResponse> stackDepth() {
        return myClient.sendCommand("stack_depth");
    }

    // Context and property commands

    @Nonnull
    public CompletableFuture<DbgpResponse> contextNames(int stackDepth) {
        return myClient.sendCommand("context_names -d " + stackDepth);
    }

    @Nonnull
    public CompletableFuture<DbgpResponse> contextGet(int stackDepth, int contextId) {
        return myClient.sendCommand("context_get -d " + stackDepth + " -c " + contextId);
    }

    @Nonnull
    public CompletableFuture<DbgpResponse> propertyGet(@Nonnull String fullName, int stackDepth, int contextId) {
        return myClient.sendCommand("property_get -n " + fullName + " -d " + stackDepth + " -c " + contextId);
    }

    @Nonnull
    public CompletableFuture<DbgpResponse> propertyGet(@Nonnull String fullName, int stackDepth, int contextId, int page) {
        return myClient.sendCommand("property_get -n " + fullName + " -d " + stackDepth + " -c " + contextId + " -p " + page);
    }

    @Nonnull
    public CompletableFuture<DbgpResponse> propertySet(@Nonnull String fullName, int stackDepth, @Nonnull String type, @Nonnull String value) {
        return myClient.sendCommand("property_set -n " + fullName + " -d " + stackDepth + " -t " + type, value);
    }

    @Nonnull
    public CompletableFuture<DbgpResponse> propertyValue(@Nonnull String fullName, int stackDepth) {
        return myClient.sendCommand("property_value -n " + fullName + " -d " + stackDepth);
    }

    // Eval

    @Nonnull
    public CompletableFuture<DbgpResponse> eval(@Nonnull String expression) {
        return myClient.sendCommand("eval", expression);
    }

    // Stream control

    @Nonnull
    public CompletableFuture<DbgpResponse> stdout(int mode) {
        return myClient.sendCommand("stdout -c " + mode);
    }

    // Status

    @Nonnull
    public CompletableFuture<DbgpResponse> status() {
        return myClient.sendCommand("status");
    }
}
