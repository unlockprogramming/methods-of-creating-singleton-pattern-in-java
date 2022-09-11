package com.unlockprogramming;

import java.io.Serializable;

public class EagerInitializedSingleton implements Serializable {

    public static final EagerInitializedSingleton INSTANCE = new EagerInitializedSingleton();

    private static final long serialVersionUID = 1L;

    private EagerInitializedSingleton() {
        if (INSTANCE != null) {
            throw new RuntimeException("This Singleton Class is already initialized.");
        }
    }

    protected Object readResolve() {
        return INSTANCE;
    }

//    other methods implementation
}
