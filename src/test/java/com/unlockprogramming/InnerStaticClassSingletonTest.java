package com.unlockprogramming;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class InnerStaticClassSingletonTest {

    String fileName;

    @BeforeEach
    public void setUp() {
        fileName = "src/test/resources/inner-class-singleton-serialize.ser";
    }

    @Test
    void whenCalledGetInstanceTwoTimesShouldReturnSameObject() {
        assertEquals(InnerClassSingleton.getInstance(), InnerClassSingleton.getInstance());
    }

    @Test
    void whenCalledGetInstanceMultipleTimesShouldReturnSameObject() {
        List<InnerClassSingleton> instanceList = new ArrayList<>();
        instanceList.add(InnerClassSingleton.getInstance());
        instanceList.add(InnerClassSingleton.getInstance());
        instanceList.add(InnerClassSingleton.getInstance());
        instanceList.add(InnerClassSingleton.getInstance());
        instanceList.add(InnerClassSingleton.getInstance());

        Set<Integer> objectCount = new HashSet<>();

        instanceList.stream().forEach(instance -> objectCount.add(instance.hashCode()));

        assertEquals(1, objectCount.size());
    }


    @Test
    void whenCalledPrivateConstructorShouldSingletonRuleViolationException() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        InnerClassSingleton instance = InnerClassSingleton.getInstance();

        Throwable exception = assertThrows(RuntimeException.class, () -> {
            Constructor<InnerClassSingleton> constructor = InnerClassSingleton.class.getDeclaredConstructor();
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
    void whenCalledWithMultipleThreadsShouldReturnSameObject() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2000);
        List<Callable<Integer>> callables = new ArrayList<>(2000);
        IntStream.range(1, 2000).forEach(i -> {
            callables.add(() -> InnerClassSingleton.getInstance().hashCode());
        });

        List<Future<Integer>> hashValues = executor.invokeAll(callables);
        executor.awaitTermination(1, TimeUnit.SECONDS);
        executor.shutdown();

        Set<Integer> objectCount = new HashSet<>();

        hashValues.stream().forEach(integerFuture -> {
            try {
                objectCount.add(integerFuture.get());
            }catch (InterruptedException ie) {
                ie.printStackTrace();
            }catch (ExecutionException ee) {
                ee.printStackTrace();
            }

        });

        assertEquals(1, objectCount.size());
    }

    @Test
    void whenTryingToDeserializeThenShouldReturnSameObject() throws IOException, ClassNotFoundException {
        InnerClassSingleton expectedInstance = fileSerialize();

        File file = new File(fileName);

        ObjectInput input = new ObjectInputStream(new FileInputStream(file));

        InnerClassSingleton actualInstance = (InnerClassSingleton) input.readObject();
        input.close();
        file.delete();

        assertEquals(expectedInstance, actualInstance);
    }

    private InnerClassSingleton fileSerialize() throws IOException {
        InnerClassSingleton expectedInstance = InnerClassSingleton.getInstance();
        ObjectOutput output = new ObjectOutputStream(new FileOutputStream( fileName));
        output.writeObject(expectedInstance);
        output.close();

        return expectedInstance;
    }

}