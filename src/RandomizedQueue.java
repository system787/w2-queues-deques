import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int size;
    private Node head;
    private Node tail;

    private final String[] testInputStrings = new String[]{ "elden", "ring", "is", "a", "great", "game,", "albeit", "quite", "overrated" };

    private class Node<Item> {
        Node next = null;
        Node prev = null;
        Item item;
    }

    // construct an empty randomized queue
    public RandomizedQueue() {
        head = null;
        tail = null;
        size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return (size == 0);
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node node = new Node();
        node.item = item;
        if (isEmpty()) {
            head = node;
            tail = head;
        } else {
            node.next = head;
            head = node;
            head.next.prev = head;
        }
        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item;
        if (size() == 1) {
            item = (Item) head.item;
            size--;
            head = null;
            tail = null;
            return item;
        }

        int index = StdRandom.uniform(1, size + 1);
        Node cursor;

        if (size() / 2 > index) {
            cursor = tail;
            int distance = size() - index;
            for (int i = 1; i <= distance; i++) {
                cursor = cursor.prev;
            }
            item = (Item) cursor.item;
            if (cursor == head) {
                head = cursor.next;
            }

            if (cursor == tail) {
                tail = cursor.prev;
            }

            if (cursor.next != null) {
                cursor.next.prev = cursor.prev;
            }

            if (cursor.prev != null) {
                cursor.prev.next = cursor.next;
            }
        } else {
            cursor = head;
            for (int i = 1; i < index; i++) {
                cursor = cursor.next;
            }
            item = (Item) cursor.item;
            if (cursor == head) {
                head = cursor.next;
            }

            if (cursor == tail) {
                tail = cursor.prev;
            }

            if (cursor.next != null) {
                cursor.next.prev = cursor.prev;
            }

            if (cursor.prev != null) {
                cursor.prev.next = cursor.next;
            }
        }

        size--;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node cursor;
        int index = StdRandom.uniform(1, size + 1);

        if (size() / 2 > index) {
            cursor = tail;
            int distance = size() - index;
            for (int i = 1; i <= distance; i++) {
                cursor = cursor.prev;
            }
            return (Item) cursor.item;
        } else {
            cursor = head;
            for (int i = 1; i < index; i++) {
                cursor = cursor.next;
            }
            return (Item) cursor.item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        new RandomizedQueue().run();
    }

    private void run() {
        RandomizedQueue testQueue = new RandomizedQueue();
        for (String s : testInputStrings) {
            testQueue.enqueue(s);
        }

        // Test 1
        System.out.println("Test 1: Queue length matches input array length = "
                + (testInputStrings.length == testQueue.size()));

        Node cursor = testQueue.head;

        // Test 2
        while (cursor.next != null) {
            cursor = cursor.next;
        }
        System.out.println("Test 2: node.next objects are properly connected = "
                + !cursor.item.equals(testQueue.head.item));

        // Test 3
        cursor = testQueue.tail;
        while (cursor.prev != null) {
            cursor = cursor.prev;
        }
        System.out.println("Test 3: node.prev objects are properly connected = "
                + !cursor.item.equals(testQueue.tail.item));

        // Test 4
        Iterator testIterator = testQueue.iterator();
        System.out.println("Test 4: iterator hasNext() returns true = " + testIterator.hasNext());

        // Test 5
        String test5string = "---";
        int test5int = 0;
        while (testIterator.hasNext()) {
            String current = (String) testIterator.next();
            if (!current.equals(test5string)) {
                test5int++;
                test5string = current;
            }

            if (!testIterator.hasNext()) {
                test5int++;
            }
        }

        System.out.println("Test 5: iterator next() returns results correctly and randomly = "
                + (test5int == testInputStrings.length) + ", "
                + !testIterator.hasNext());

        // Test 6
        System.out.println("Test 6: String output of calling sample() 20 times");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 20; i++) {
            if (i % 5 == 0 && i != 0) {
                sb.append("\n");
            }
            sb.append(testQueue.sample()).append(" ");
        }
        System.out.println(sb);

        // Test 7
        while (!testQueue.isEmpty()) {
            testQueue.dequeue();
        }
        System.out.println("Test 7: dequeue until queue is empty = " + testQueue.isEmpty());
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        RandomizedQueue iteratorQueue;

        ListIterator() {
            iteratorQueue = new RandomizedQueue();

            Node cursor = head;
            while (cursor.next != null) {
                iteratorQueue.enqueue(cursor.item);
                cursor = cursor.next;
            }
        }

        @Override
        public boolean hasNext() {
            return (!iteratorQueue.isEmpty());
        }

        @Override
        public Item next() {
            return (Item) iteratorQueue.dequeue();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}