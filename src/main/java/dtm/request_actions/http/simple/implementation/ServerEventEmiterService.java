package dtm.request_actions.http.simple.implementation;

import dtm.request_actions.http.simple.core.ServerEventEmiter;
import dtm.request_actions.http.simple.core.ServerEventEmiterDispacher;
import dtm.request_actions.http.simple.core.StreamReader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ServerEventEmiterService implements ServerEventEmiterDispacher {

    private final AtomicBoolean isRunning;
    private final ExecutorService executorService;
    private final StreamReader streamReader;
    private final ServerEventEmiter serverEventEmiter;

    public static ServerEventEmiterDispacher defineServerEventEmiter(StreamReader streamReader, ServerEventEmiter serverEventEmiter) {
        ServerEventEmiterService service = new ServerEventEmiterService(streamReader, serverEventEmiter);
        service.dispatch();
        return service;
    }

    private ServerEventEmiterService(StreamReader streamReader, ServerEventEmiter serverEventEmiter) {
        if (serverEventEmiter == null) {
            throw new IllegalArgumentException("ServerEventEmiter cannot be null");
        }
        if (streamReader == null) {
            throw new IllegalArgumentException("StreamReader cannot be null");
        }

        this.isRunning = new AtomicBoolean(true);
        this.streamReader = streamReader;
        this.serverEventEmiter = serverEventEmiter;
        this.executorService = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "ServerEventEmiter-Worker-"+System.identityHashCode(serverEventEmiter));
            t.setDaemon(true);
            return t;
        });
    }

    @Override
    public void stop() {
        isRunning.set(false);
        executorService.shutdownNow();
        try {
            streamReader.close();
        } catch (Exception ignored) {}
    }

    private void dispatch() {
        executorService.submit(() -> {
            SseContext context = new SseContext();
            StringBuilder lineBuffer = new StringBuilder();

            try (streamReader) {
                while (isRunning.get()) {
                    if (streamReader.hasRemainingBytes(1)) {
                        byte[] bytes = streamReader.readBytes(1);
                        char c = (char) bytes[0];

                        if (c == '\n') {
                            String line = lineBuffer.toString().trim();
                            lineBuffer.setLength(0);
                            processLine(line, context);
                        } else {
                            lineBuffer.append(c);
                        }

                    } else {
                        if (streamReader.isFinished()) {
                            break;
                        }
                        try {
                            TimeUnit.MILLISECONDS.sleep(50);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                if (isRunning.get()) {
                    serverEventEmiter.onError(e);
                }
            } finally {
                if (!executorService.isShutdown()) {
                    executorService.shutdown();
                }
            }
        });
    }

    private void processLine(String line, SseContext context) {
        if (line.isEmpty()) {
            emitEventIfReady(context);
        } else if (line.startsWith("event:")) {
            extractEventName(line, context);
        } else if (line.startsWith("data:")) {
            appendEventData(line, context);
        }
    }

    private void extractEventName(String line, SseContext context) {
        context.currentEvent = line.substring(6).trim();
    }

    private void appendEventData(String line, SseContext context) {
        if (!context.currentData.isEmpty()) {
            context.currentData.append("\n");
        }
        context.currentData.append(line.substring(5).trim());
    }

    private void emitEventIfReady(SseContext context) {
        if (!context.currentData.isEmpty()) {
            serverEventEmiter.onEvent(context.currentEvent, context.currentData.toString());
            context.reset();
        }
    }

    private static class SseContext {
        String currentEvent = "message";
        StringBuilder currentData = new StringBuilder();

        void reset() {
            this.currentEvent = "message";
            this.currentData.setLength(0);
        }
    }
}