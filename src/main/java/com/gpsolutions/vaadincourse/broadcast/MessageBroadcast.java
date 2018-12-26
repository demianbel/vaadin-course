package com.gpsolutions.vaadincourse.broadcast;

import com.gpsolutions.vaadincourse.dbo.Email;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Service
public class MessageBroadcast implements Broadcast<Email> {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final List<Consumer<Email>> listeners = new ArrayList<>();

    @Override public synchronized void register(final Consumer<Email> listener) {
        listeners.add(listener);
    }

    @Override public synchronized void unregister(final Consumer<Email> listener) {
        listeners.remove(listener);
    }

    @Override public synchronized void broadcast(final Email message) {
        for (final Consumer<Email> listener : listeners)
            executorService.execute(() -> listener.accept(message));
    }
}
