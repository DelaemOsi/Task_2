package by.fpmi.osi.second.entities;

import org.junit.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.stream.IntStream;


public class ConcurrentNonBlockedListTest {

    @Test
    public void testAdd() throws InterruptedException {
        int expectedSize = 1000;
        ConcurrentNonBlockedList<Integer> concurrentList = new ConcurrentNonBlockedList<>();
        Integer[] expectedElementsSorted = new Integer[expectedSize];
        Thread[] threadList = new Thread[expectedSize];

        IntStream.range(0, expectedSize).forEach(i -> {
            Thread testThread = new Thread(() -> concurrentList.add(i));
            threadList[i] = testThread;
            threadList[i].start();
            expectedElementsSorted[i] = i;
        });

        for (int i = 0; i < expectedSize; i++) {
            threadList[i].join();
        }

        Object[] elementsActual = concurrentList.toArray();
        Arrays.sort(elementsActual);
        Assert.assertArrayEquals(elementsActual, expectedElementsSorted);

    }

    @Test
    public void testRemove() throws InterruptedException {
        int startSize = 1000;
        ConcurrentNonBlockedList<Integer> concurrentList = new ConcurrentNonBlockedList<>();
        for (int i = 0; i < startSize; i++) {
            concurrentList.add(i);
        }

        Thread[] threadList = new Thread[startSize];

        for (int i = 0; i < startSize - 1; i++) {
            Thread testThread = new Thread(() -> concurrentList.remove(0));
            threadList[i] = testThread;
            threadList[i].start();
        }

        for (int i = 0; i < startSize - 1; i++) {
            threadList[i].join();
        }
        int expectedSize = 1;
        Assert.assertEquals(expectedSize, concurrentList.size());
    }

    @Test
    public void testSize() throws InterruptedException {
        int expectedSize = 100;
        ConcurrentNonBlockedList<Integer> concurrentList = new ConcurrentNonBlockedList<>();
        Thread[] threadList = new Thread[100];
        for (int i = 0; i < expectedSize; i++) {
            Thread testThread = new Thread(() -> concurrentList.add(1));
            threadList[i] = testThread;
            threadList[i].start();
        }
        for (int i = 0; i < expectedSize; i++) {
            threadList[i].join();
        }
        Assert.assertEquals(concurrentList.size(), expectedSize);
    }
}