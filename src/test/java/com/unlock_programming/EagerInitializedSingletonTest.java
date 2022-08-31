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
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class EagerInitializedSingletonTest {

    String fileName;

    @BeforeEach
    void setUp() {
        fileName = "src/test/resources/lazy-singleton-serialize.ser";
    }

    @Test
    void whenCalledGetInstanceTwoTimesShouldReturnSameObject() {
        assertEquals(EagerInitializedSingleton.INSTANCE, EagerInitializedSingleton.INSTANCE);
    }

    @Test
    void whenCalledGetInstanceMultipleTimesShouldReturnSameObject() {
        List<EagerInitializedSingleton> instanceList = new ArrayList<>();
        instanceList.add(EagerInitializedSingleton.INSTANCE);
        instanceList.add(EagerInitializedSingleton.INSTANCE);
        instanceList.add(EagerInitializedSingleton.INSTANCE);
        instanceList.add(EagerInitializedSingleton.INSTANCE);
        instanceList.add(EagerInitializedSingleton.INSTANCE);
        instanceList.add(EagerInitializedSingleton.INSTANCE);
        instanceList.add(EagerInitializedSingleton.INSTANCE);

        Set<Integer> objectCount = new HashSet<>();

        instanceList.stream().forEach(instance -> objectCount.add(instance.hashCode()));

        assertEquals(1, objectCount.size());
    }

    @Test
    void whenCalledPrivateConstructorShouldSingletonRuleViolationException() {
        Throwable exception = assertThrows(SingletonViolationException.class, () -> {
            Constructor<EagerInitializedSingleton> constructor = EagerInitializedSingleton.class.getDeclaredConstructor();
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
            callables.add(() -> EagerInitializedSingleton.INSTANCE.hashCode());
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
        EagerInitializedSingleton expectedInstance = fileSerialize();

        File file = new File(fileName);

        ObjectInput input = new ObjectInputStream(new FileInputStream(file));

        EagerInitializedSingleton actualInstance = (EagerInitializedSingleton) input.readObject();
        input.close();
        file.delete();

        assertEquals(expectedInstance, actualInstance);
    }

    private EagerInitializedSingleton fileSerialize() throws IOException {
        EagerInitializedSingleton expectedInstance = EagerInitializedSingleton.INSTANCE;
        ObjectOutput output = new ObjectOutputStream(new FileOutputStream(fileName));
        output.writeObject(expectedInstance);
        output.close();

        return expectedInstance;
    }

}