package by.fpmi.osi.second.entities;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentNonBlockedList<E> {

    private volatile Node<E> head;
    private volatile Node<E> tail;
    private final AtomicInteger size = new AtomicInteger(0);

    public ConcurrentNonBlockedList() {
    }

    private static final class Node<E> {
        Node<E> next;
        E value;

        public Node() {

        }

        public Node(E value) {
            this.value = value;
        }
    }

    public void add(E element) {
        size.incrementAndGet();
        synchronized (this) {
            if (head == null) {
                head = new Node<>(element);
                return;
            }
            if (tail == null) {
                tail = new Node<>(element);
                head.next = tail;
                return;
            }
        }
        synchronized (tail) {
            Node<E> newNode = new Node<>(element);
            tail.next = newNode;
            tail = newNode;
        }
    }

    public void remove(int index) {
        size.decrementAndGet();
        if (index == 0) {
            head = head.next;
            return;
        }
        Node<E> parentNode = getNode(index - 1);
        synchronized (parentNode) {
            Node<E> nextNode = parentNode.next;
            Node<E> newNextNode = nextNode.next;
            parentNode.next = newNextNode;
            newNextNode.next = null;
        }
    }

    public E get(int index) {
        return getNode(index).value;
    }

    public Node<E> getNode(int index) {
        Node<E> currentNode = head;
        for (int i = 0; i < index; i++) {
            currentNode = currentNode.next;
        }
        return currentNode;
    }

    public int size() {
        return size.get();
    }

    public Object[] toArray() {
        int size = size();
        E[] array = (E[]) new Object[size];
        for (int i = 0; i < size; i++) {
            array[i] = get(i);
        }
        Arrays.sort(array);
        return array;
    }

}
