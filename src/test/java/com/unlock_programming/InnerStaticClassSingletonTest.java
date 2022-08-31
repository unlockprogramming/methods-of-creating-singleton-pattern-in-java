package com.unlock_programming;

import com.unlock_programming.exception.SingletonViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InnerStaticClassSingletonTest {

    String fileName;

    @BeforeEach
    public void setUp() {
        fileName = "src/test/resources/inner-class-singleton-serialize.ser";
    }

    @Test
    void whenCalledGetInstanceTwoTimesShouldReturnSameObject() {
        assertEquals(InnerStaticClassSingleton.getInstance(), InnerStaticClassSingleton.getInstance());
    }

    @Test
    void whenCalledGetInstanceMultipleTimesShouldReturnSameObject() {
        List<InnerStaticClassSingleton> instanceList = new ArrayList<>();
        instanceList.add(InnerStaticClassSingleton.getInstance());
        instanceList.add(InnerStaticClassSingleton.getInstance());
        instanceList.add(InnerStaticClassSingleton.getInstance());
        instanceList.add(InnerStaticClassSingleton.getInstance());
        instanceList.add(InnerStaticClassSingleton.getInstance());

        Set<Integer> objectCount = new HashSet<>();

        instanceList.stream().forEach(instance -> objectCount.add(instance.hashCode()));

        assertEquals(1, objectCount.size());
    }


    @Test
    void whenCalledPrivateConstructorShouldSingletonRuleViolationException() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        InnerStaticClassSingleton instance = InnerStaticClassSingleton.getInstance();

        Throwable exception = assertThrows(SingletonViolationException.class, () -> {
            Constructor<InnerStaticClassSingleton> constructor = InnerStaticClassSingleton.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            try {
                constructor.newInstance();
            }
            catch (InvocationTargetException ite ) {
                throw (Exception) ite.getCause();
            }

        });
        assertEquals("This Singleton Class is already initialized. Use getInstance Method instead", exception.getMessage());
    }

    @Test
    void whenTryingToDeserializeThenShouldReturnSameObject() throws IOException, ClassNotFoundException {
        InnerStaticClassSingleton expectedInstance = fileSerialize();

        File file = new File(fileName);

        ObjectInput input = new ObjectInputStream(new FileInputStream(file));

        InnerStaticClassSingleton actualInstance = (InnerStaticClassSingleton) input.readObject();
        input.close();
        file.delete();

        assertEquals(expectedInstance, actualInstance);
    }

    private InnerStaticClassSingleton fileSerialize() throws IOException {
        InnerStaticClassSingleton expectedInstance = InnerStaticClassSingleton.getInstance();
        ObjectOutput output = new ObjectOutputStream(new FileOutputStream( fileName));
        output.writeObject(expectedInstance);
        output.close();

        return expectedInstance;
    }

}