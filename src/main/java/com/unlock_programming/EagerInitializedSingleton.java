package com.unlock_programming;

import com.unlock_programming.exception.SingletonViolationException;

import java.io.Serializable;

public class EagerInitializedSingleton implements Serializable {

    private static final EagerInitializedSingleton INSTANCE = new EagerInitializedSingleton();

    private static final long serialVersionUID = 1L;

    private EagerInitializedSingleton() {
        if (INSTANCE != null) {
            throw new SingletonViolationException("This Singleton Class is already initialized. Use getInstance Method instead");
        }
    }

    public static EagerInitializedSingleton getInstance() {
        return INSTANCE;
    }

    protected Object readResolve() {
        return INSTANCE;
    }

//    other methods implementation
}
