package consulo.php.impl.xdebug.connection;

import consulo.logging.Logger;
import consulo.platform.Platform;
import consulo.platform.PlatformServerSocket;
import consulo.platform.PlatformSocket;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class DbgpClient {
    private static final Logger LOG = Logger.getInstance(DbgpClient.class);

    public interface Listener {
        void onConnected(@Nonnull DbgpResponse initPacket);

        void onDisconnected();

        void onStreamOutput(@Nonnull String type, @Nonnull String data);

        void onStatusChanged(@Nonnull String status, @Nullable String reason, @Nullable String command);
    }

    private final int myPort;
    private final Platform myPlatform;
    private final Listener myListener;
    private final AtomicInteger myTransactionId = new AtomicInteger(1);
    private final Map<String, CompletableFuture<DbgpResponse>> myPendingCommands = new ConcurrentHashMap<>();
    private final AtomicBoolean myRunning = new AtomicBoolean(false);

    private volatile PlatformServerSocket myServerSocket;
    private volatile PlatformSocket mySocket;
    private volatile OutputStream myOutputStream;
    private volatile Thread myListenThread;

    /**
     * @param platform the machine of the debugged script - Xdebug connects to a port of its own machine
     */
    public DbgpClient(int port, @Nonnull Platform platform, @Nonnull Listener listener) {
        myPort = port;
        myPlatform = platform;
        myListener = listener;
    }

    @Nonnull
    public Platform getPlatform() {
        return myPlatform;
    }

    public void startListening() throws IOException {
        if (myRunning.getAndSet(true)) {
            return;
        }

        myServerSocket = myPlatform.net().bind("127.0.0.1", myPort);

        myListenThread = new Thread(this::listenLoop, "PHP-Xdebug-Listener-" + myServerSocket.getPort());
        myListenThread.setDaemon(true);
        myListenThread.start();
    }

    private void listenLoop() {
        try {
            while (myRunning.get()) {
                // blocks until a connection comes or the socket is closed by stop()
                PlatformSocket socket = myServerSocket.accept();
                handleConnection(socket);
            }
        }
        catch (IOException e) {
            if (myRunning.get()) {
                LOG.warn("Xdebug listener error", e);
            }
        }
    }

    private void handleConnection(@Nonnull PlatformSocket socket) {
        mySocket = socket;
        try {
            InputStream input = socket.getInputStream();
            myOutputStream = socket.getOutputStream();

            // read init packet
            String xml = readMessage(input);
            if (xml == null) {
                return;
            }
            DbgpResponse initResponse = DbgpResponse.parse(xml);
            if (initResponse.isInit()) {
                myListener.onConnected(initResponse);
            }

            // read loop
            while (myRunning.get() && mySocket == socket) {
                xml = readMessage(input);
                if (xml == null) {
                    break;
                }
                handleResponse(xml);
            }
        }
        catch (Exception e) {
            if (myRunning.get()) {
                LOG.warn("Xdebug connection error", e);
            }
        }
        finally {
            myListener.onDisconnected();
            closeSocket();
        }
    }

    private void handleResponse(@Nonnull String xml) {
        try {
            DbgpResponse response = DbgpResponse.parse(xml);

            if (response.isStream()) {
                String type = response.getAttribute("type");
                String data = response.getStreamData();
                if (type != null && data != null) {
                    myListener.onStreamOutput(type, data);
                }
                return;
            }

            String transactionId = response.getTransactionId();
            if (transactionId != null) {
                CompletableFuture<DbgpResponse> future = myPendingCommands.remove(transactionId);
                if (future != null) {
                    future.complete(response);
                }
            }

            // notify status changes for continuation commands
            String status = response.getStatus();
            if (status != null) {
                myListener.onStatusChanged(status, response.getReason(), response.getCommand());
            }
        }
        catch (Exception e) {
            LOG.warn("Failed to parse Xdebug response", e);
        }
    }

    @Nullable
    private String readMessage(@Nonnull InputStream input) throws IOException {
        // read data_length (ASCII digits until NULL byte)
        StringBuilder lengthStr = new StringBuilder();
        int b;
        while ((b = input.read()) != -1) {
            if (b == 0) {
                break;
            }
            lengthStr.append((char) b);
        }
        if (b == -1 || lengthStr.isEmpty()) {
            return null;
        }

        int dataLength;
        try {
            dataLength = Integer.parseInt(lengthStr.toString());
        }
        catch (NumberFormatException e) {
            LOG.warn("Invalid DBGp message length: " + lengthStr);
            return null;
        }

        // read exactly dataLength bytes
        byte[] data = new byte[dataLength];
        int totalRead = 0;
        while (totalRead < dataLength) {
            int read = input.read(data, totalRead, dataLength - totalRead);
            if (read == -1) {
                return null;
            }
            totalRead += read;
        }

        // consume trailing NULL byte
        input.read();

        return new String(data, StandardCharsets.UTF_8);
    }

    @Nonnull
    public CompletableFuture<DbgpResponse> sendCommand(@Nonnull String command) {
        return sendCommand(command, null);
    }

    @Nonnull
    public CompletableFuture<DbgpResponse> sendCommand(@Nonnull String command, @Nullable String data) {
        int tid = myTransactionId.getAndIncrement();
        CompletableFuture<DbgpResponse> future = new CompletableFuture<>();
        myPendingCommands.put(String.valueOf(tid), future);

        try {
            StringBuilder sb = new StringBuilder();
            sb.append(command);
            sb.append(" -i ").append(tid);
            if (data != null) {
                sb.append(" -- ").append(Base64.getEncoder().encodeToString(data.getBytes(StandardCharsets.UTF_8)));
            }
            sb.append('\0');

            OutputStream out = myOutputStream;
            if (out != null) {
                byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
                synchronized (out) {
                    out.write(bytes);
                    out.flush();
                }
            }
            else {
                future.completeExceptionally(new IOException("Not connected to Xdebug"));
            }
        }
        catch (IOException e) {
            myPendingCommands.remove(String.valueOf(tid));
            future.completeExceptionally(e);
        }

        return future;
    }

    public boolean isConnected() {
        return mySocket != null;
    }

    /**
     * The port Xdebug connects to - on the machine of the script.
     */
    public int getPort() {
        PlatformServerSocket serverSocket = myServerSocket;
        return serverSocket != null ? serverSocket.getPort() : myPort;
    }

    public void stop() {
        myRunning.set(false);

        // complete all pending futures
        for (CompletableFuture<DbgpResponse> future : myPendingCommands.values()) {
            future.cancel(true);
        }
        myPendingCommands.clear();

        closeServerSocket();
        closeSocket();
    }

    private void closeSocket() {
        PlatformSocket socket = mySocket;
        if (socket != null) {
            try {
                socket.close();
            }
            catch (IOException ignored) {
            }
            mySocket = null;
        }
        myOutputStream = null;
    }

    private void closeServerSocket() {
        PlatformServerSocket serverSocket = myServerSocket;
        if (serverSocket != null) {
            try {
                serverSocket.close();
            }
            catch (IOException ignored) {
            }
            myServerSocket = null;
        }
    }
}
