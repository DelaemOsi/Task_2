package by.fpmi.osi.second.entities;

import org.junit.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.stream.IntStream;


public class ConcurrentNonBlockedListTest {


    @Test
    public void testAdd() throws InterruptedException {
        int expectedSize = 1000;
        ConcurrentNonBlockedList<Integer> list = new ConcurrentNonBlockedList<>();
        Integer[] expectedElementsSorted = new Integer[expectedSize];
        Thread[] threadList = new Thread[expectedSize];

        IntStream.range(0, expectedSize).forEach(i -> {
            Thread testThread = new Thread(() -> list.add(i));
            threadList[i] = testThread;
            threadList[i].start();
            expectedElementsSorted[i] = i;
        });

        for (int i = 0; i < expectedSize; i++) {
            threadList[i].join();
        }

        Object[] elementsActual = list.toArray();
        Arrays.sort(elementsActual);
        Assert.assertArrayEquals(elementsActual, expectedElementsSorted);
    }

    @Test
    public void testRemove() throws InterruptedException {
        int startSize = 1000;
        ConcurrentNonBlockedList<Integer> list = new ConcurrentNonBlockedList<>();
        for (int i = 0; i < startSize; i++) {
            list.add(i);
        }
        Thread[] threadList = new Thread[startSize];
        for (int i = 0; i < startSize - 1; i++) {
            Thread testThread = new Thread(() -> list.remove(0));
            threadList[i] = testThread;
            threadList[i].start();
        }
        for (int i = 0; i < startSize - 1; i++) {
            threadList[i].join();
        }
        int expectedSize = 1;
        Assert.assertEquals(expectedSize, list.size());
    }

    @Test
    public void testSize() throws InterruptedException {
        int expectedSize = 100;
        ConcurrentNonBlockedList<Integer> list = new ConcurrentNonBlockedList<>();
        Thread[] threadList = new Thread[100];
        for (int i = 0; i < expectedSize; i++) {
            Thread testThread = new Thread(() -> list.add(1));
            threadList[i] = testThread;
            threadList[i].start();
        }
        for (int i = 0; i < expectedSize; i++) {
            threadList[i].join();
        }
        Assert.assertEquals(list.size(), expectedSize);
    }
}