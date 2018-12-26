package com.gpsolutions.vaadincourse.broadcast;

import java.util.function.Consumer;

public interface Broadcast<T> {

    void register(Consumer<T> listener);

    void unregister(Consumer<T> listener);

    void broadcast(final T message);
}
