package by.fpmi.osi.second.entities;

import java.util.*;

public class ConcurrentNonBlockedList<T> {

    private final transient Object lock = new Object();
    private transient volatile T[] array;

    public ConcurrentNonBlockedList() {
        array = (T[]) (new Object[0]);
    }

    public final T[] toArray() {
        return array;
    }

    final void setArray(T[] a) {
        array = a;
    }

    public T get(int index) {
        return array[index];
    }

    public void add(T element) {
        synchronized (lock) {
            T[] prevArray = toArray();
            int len = prevArray.length;
            prevArray = Arrays.copyOf(prevArray, len + 1);
            prevArray[len] = element;
            setArray((T[]) prevArray);
        }
    }

    public void remove(int index) {
        synchronized (lock) {
            T[] previousArray = toArray();
            int len = previousArray.length;
            T oldValue = previousArray[index];
            int offset = len - index - 1;
            T[] newElements;
            if (offset == 0) {
                newElements = Arrays.copyOf(previousArray, len - 1);
            }
            else {
                newElements = (T[]) new Object[len - 1];
                System.arraycopy(previousArray, 0, newElements, 0, index);
                System.arraycopy(previousArray, index + 1, newElements, index, offset);
            }
            setArray(newElements);
        }
    }

    public int size() {
        return toArray().length;
    }

}
