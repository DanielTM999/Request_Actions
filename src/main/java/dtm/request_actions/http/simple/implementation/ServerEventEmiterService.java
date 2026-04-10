package dtm.request_actions.http.simple.implementation;

import dtm.request_actions.http.simple.core.ServerEventEmiter;
import dtm.request_actions.http.simple.core.ServerEventEmiterDispacher;
import dtm.request_actions.http.simple.core.StreamReader;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ServerEventEmiterService implements ServerEventEmiterDispacher {

    private final AtomicBoolean isRunning;
    private final ExecutorService executorService;
    private final StreamReader streamReader;
    private final ServerEventEmiter serverEventEmiter;
    private final Charset charset;

    public static ServerEventEmiterDispacher defineServerEventEmiter(StreamReader streamReader, ServerEventEmiter serverEventEmiter) {
        return defineServerEventEmiter(streamReader, serverEventEmiter, StandardCharsets.UTF_8);
    }

    public static ServerEventEmiterDispacher defineServerEventEmiter(StreamReader streamReader, ServerEventEmiter serverEventEmiter, Charset charset) {
        ServerEventEmiterService service = new ServerEventEmiterService(streamReader, serverEventEmiter, charset);
        service.dispatch();
        return service;
    }

    private ServerEventEmiterService(StreamReader streamReader, ServerEventEmiter serverEventEmiter, Charset charset) {
        if (serverEventEmiter == null) {
            throw new IllegalArgumentException("ServerEventEmiter cannot be null");
        }
        if (streamReader == null) {
            throw new IllegalArgumentException("StreamReader cannot be null");
        }

        this.charset = (charset == null) ? StandardCharsets.UTF_8 : charset;
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
            List<Byte> lineBuffer = new ArrayList<>();

            try (streamReader) {
                while (isRunning.get()) {
                    try{
                        if(!streamReader.isAlive()) break;

                        if (streamReader.hasRemainingBytes(1)) {
                            byte[] bytes = streamReader.readBytes(1);
                            byte byteChar = bytes[0];

                            if (byteChar == '\n') {
                                byte[] lineBytes = new byte[lineBuffer.size()];
                                for (int i = 0; i < lineBuffer.size(); i++) lineBytes[i] = lineBuffer.get(i);
                                String line = new String(lineBytes, charset).trim();
                                lineBuffer.clear();
                                processLine(line, context);
                            } else {
                                lineBuffer.add(byteChar);
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
                    } catch (Exception e) {
                        serverEventEmiter.onError(e);
                    }
                }
            } catch (Exception e) {
                if (isRunning.get()) {
                    serverEventEmiter.onError(e);
                }
            } finally {
                serverEventEmiter.onDisconected();
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
        String event = line.substring(6);
        if (!event.isEmpty() && event.charAt(0) == ' ') {
            event = event.substring(1);
        }
        context.currentEvent = event;
    }

    private void appendEventData(String line, SseContext context) {
        if (!context.currentData.isEmpty()) {
            context.currentData.append("\n");
        }
        String data = line.substring(5);
        if (!data.isEmpty() && data.charAt(0) == ' ') {
            data = data.substring(1);
        }
        context.currentData.append(data);
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