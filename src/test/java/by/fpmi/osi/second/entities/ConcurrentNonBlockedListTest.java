package by.fpmi.osi.second.entities;

import org.junit.Assert;
import org.testng.annotations.Test;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class ConcurrentNonBlockedListTest {



    @Test
    public void testGet() {
    }

    @Test
    public void testAdd() throws InterruptedException {
        int expectedSize = 100;
        ConcurrentNonBlockedList<Integer>list = new ConcurrentNonBlockedList<>();
        CopyOnWriteArrayList<Integer> expectedList = new CopyOnWriteArrayList<>();
        ForkJoinPool pool = new ForkJoinPool(expectedSize);
        int e = 0;
        pool.execute(() -> list.add(e = e + 1));
        IntStream.range(0, expectedSize).forEach(e -> list.add(e++));
        IntStream.range(0, expectedSize).forEach(e -> expectedList.add(e++));
        Assert.assertArrayEquals(list.getArray(), expectedList.toArray());
    }

    @Test
    public void testRemove() {

    }

    @Test
    public void testSize() throws InterruptedException {
        int expectedSize = 100;
        ConcurrentNonBlockedList<Integer> list = new ConcurrentNonBlockedList<>();
        Thread[]threadList = new Thread[100];
        for(int i = 0; i < expectedSize; i++) {
            Thread testThread = new Thread(() -> list.add(1));
            threadList[i] = testThread;
            threadList[i].start();
        }
        for(int i = 0; i < expectedSize; i++) {
            threadList[i].join();
        }

        Assert.assertEquals(list.size(), expectedSize);
    }
}