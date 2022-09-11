package com.unlockprogramming;

import java.io.Serializable;

public class InnerClassSingleton implements Serializable {

    private static final long serialVersionUID = 3L;

    private InnerClassSingleton() {
        if (getInstance() != null) {
            throw new RuntimeException("This Singleton Class is already initialized. Use getInstance Method instead");
        }
    }

    private static class Helper {
        private static final InnerClassSingleton INSTANCE = new InnerClassSingleton();
    }

    public static InnerClassSingleton getInstance() {
        return Helper.INSTANCE;
    }

    protected Object readResolve() {
        return getInstance();
    }

    // other method implementation
}
