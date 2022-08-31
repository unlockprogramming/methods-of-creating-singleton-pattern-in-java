package com.unlock_programming;

import com.unlock_programming.exception.SingletonViolationException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class EnumSingletonRectangleTest {

    @Test
    void whenCalledGetInstanceTwoTimesShouldReturnSameObject() {

        assertEquals(EnumSingletonRectangle.INSTANCE, EnumSingletonRectangle.INSTANCE);
    }

    @Test
    void whenCalledInstanceMultipleTimesAndSetFieldsOnlyLatestValueShouldBeAvailable() {
        EnumSingletonRectangle instance1 = EnumSingletonRectangle.INSTANCE;
        instance1.setLength(5.3);
        instance1.setWidth(2.2);

        EnumSingletonRectangle instance2 = EnumSingletonRectangle.INSTANCE;
        instance2.setLength(7.5);
        instance2.setWidth(4.1);

        assertEquals(7.5, instance1.getLength());
        assertEquals(instance1.getLength(), instance2.getLength());
        assertEquals(instance1.getWidth(), instance2.getWidth());
        assertEquals(instance1.findArea(), instance2.findArea());
        assertEquals(instance1.findPerimeter(), instance2.findPerimeter());

    }

    @Test
    void whenCalledWithMultipleThreadsShouldReturnSameObject() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(1000);
        List<Callable<Integer>> callables = new ArrayList<>(1000);
        IntStream.range(1, 1000).forEach(i -> {
            callables.add(() -> EnumSingletonRectangle.INSTANCE.hashCode());
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


}