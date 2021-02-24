package by.fpmi.osi.second.entities;

import org.junit.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;


public class ConcurrentNonBlockedListTest {


    @Test
    public void testAdd() throws InterruptedException {
        int expectedSize = 1000;
        ConcurrentNonBlockedList<Integer> list = new ConcurrentNonBlockedList<>();
        Integer[]expectedElementsSorted = new Integer[expectedSize];
        Thread[] threadList = new Thread[expectedSize];
        for (int i = 0; i < expectedSize; i++) {
            int numberToAdd = i;
            Thread testThread = new Thread(() -> list.add(numberToAdd));
            threadList[i] = testThread;
            threadList[i].start();
            expectedElementsSorted[i] = i;
        }
        for (int i = 0; i < expectedSize; i++) {
            threadList[i].join();
        }

        Object[] elementsActual = list.toArray();
        Arrays.sort(elementsActual);
        Assert.assertArrayEquals(elementsActual, expectedElementsSorted);
    }

    @Test
    public void testRemove() throws InterruptedException {
        int expectedSize = 1000;
        ConcurrentNonBlockedList<Integer> list = new ConcurrentNonBlockedList<>();
        for (int i = 0; i < expectedSize; i++) {
            list.add(i);
        }
        Thread[] threadList = new Thread[expectedSize];
        for (int i = 0; i < expectedSize - 1; i++) {
            Thread testThread = new Thread(() -> list.remove(0));
            threadList[i] = testThread;
            threadList[i].start();
        }
        for (int i = 0; i < expectedSize - 1; i++) {
            threadList[i].join();
        }
        Assert.assertEquals( 1, list.size());
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