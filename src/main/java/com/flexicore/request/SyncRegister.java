package com.flexicore.request;

import com.flexicore.interfaces.Syncable;

import java.util.function.BiFunction;
import java.util.function.Function;

public class SyncRegister<E, T extends Syncable> {
    private final Class<T> c;
    private final BiFunction<T, E, Void> postMergeCallback;
    private final Function<T, E> containerConverter;

    public SyncRegister(Class<T> c, BiFunction<T, E, Void> postMergeCallback, Function<T, E> containerConverter) {
        this.c = c;
        this.postMergeCallback = postMergeCallback;
        this.containerConverter = containerConverter;
    }

    public Class<T> getC() {
        return c;
    }

    public BiFunction<T, E, Void> getPostMergeCallback() {
        return postMergeCallback;
    }

    public Function<T, E> getContainerConverter() {
        return containerConverter;
    }
}
