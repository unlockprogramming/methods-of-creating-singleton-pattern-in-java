package com.unlock_programming;

import com.unlock_programming.exception.SingletonViolationException;

import java.io.Serializable;

public class InnerStaticClassSingleton implements Serializable {

    private static final long serialVersionUID = 3L;

    private InnerStaticClassSingleton() {
        if (getInstance() != null) {
            throw new SingletonViolationException("This Singleton Class is already initialized. Use getInstance Method instead");
        }
    }

    private static class Helper {
        private static final InnerStaticClassSingleton INSTANCE = new InnerStaticClassSingleton();
    }

    public static InnerStaticClassSingleton getInstance() {
        return Helper.INSTANCE;
    }

    protected Object readResolve() {
        return getInstance();
    }

}
