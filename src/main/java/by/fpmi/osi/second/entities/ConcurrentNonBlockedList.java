package by.fpmi.osi.second.entities;

import java.util.*;



public class ConcurrentNonBlockedList<T> {

    final transient Object lock = new Object();
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

    public boolean add(T element) {
        synchronized (lock) {
            T[] prevArray = toArray();
            int len = prevArray.length;
            prevArray = Arrays.copyOf(prevArray, len + 1);
            prevArray[len] = element;
            setArray((T[]) prevArray);
            return true;
        }
    }

    public T remove(int index) {
        synchronized (lock) {
            T[] prevArray = toArray();
            int len = prevArray.length;
            T oldValue = prevArray[index];
            int numMoved = len - index - 1;
            T[] newElements;
            if (numMoved == 0)
                newElements = Arrays.copyOf(prevArray, len - 1);
            else {
                newElements = (T[]) new Object[len - 1];
                System.arraycopy(prevArray, 0, newElements, 0, index);
                System.arraycopy(prevArray, index + 1, newElements, index, numMoved);
            }
            setArray(newElements);
            return oldValue;
        }
    }

    public int size() {
        return toArray().length;
    }

}
