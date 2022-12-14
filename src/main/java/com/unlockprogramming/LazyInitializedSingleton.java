package com.unlockprogramming;

import java.io.Serializable;

public class LazyInitializedSingleton implements Serializable {

    private static final long serialVersionUID = 2L;

    private static volatile LazyInitializedSingleton instance = null;

    private LazyInitializedSingleton() {
        if (instance != null) {
            throw new RuntimeException("This Singleton Class is already initialized. Use getInstance Method instead");
        }
    }

    public static LazyInitializedSingleton getInstance() {
        if (instance == null){
            synchronized (LazyInitializedSingleton.class){
                if (instance == null)  instance = new LazyInitializedSingleton();
            }
        }
        return instance;
    }

    protected Object readResolve() {
        return instance;
    }

//    Other method implementation
}
