import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int size;
    private Node head;
    private Node tail;

    private class Node {
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
            item = head.item;
            size--;
            head = null;
            tail = null;
            return item;
        }

        int index = StdRandom.uniform(1, size + 1);
        Node cursor;

        if (size() / 2 < index) {
            cursor = tail;
            int distance = size() - index;
            for (int i = 1; i <= distance; i++) {
                cursor = cursor.prev;
            }

        } else {
            cursor = head;
            for (int i = 1; i < index; i++) {
                cursor = cursor.next;
            }

        }
        item = cursor.item;
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

        if (size() / 2 < index) {
            cursor = tail;
            int distance = size() - index;
            for (int i = 1; i <= distance; i++) {
                cursor = cursor.prev;
            }
        } else {
            cursor = head;
            for (int i = 1; i < index; i++) {
                cursor = cursor.next;
            }
        }
        return cursor.item;
    }

    // unit testing (required)
    public static void main(String[] args) {
        new RandomizedQueue<>().run();
    }

    private void run() {
        String[] testInputStrings = { "elden", "ring", "is", "a", "great", "game,", "albeit", "quite", "overrated" };

        RandomizedQueue<String> testQueue = new RandomizedQueue<>();
        for (String s : testInputStrings) {
            testQueue.enqueue(s);
        }

        // Test 1
        System.out.println("Test 1: Queue length matches input array length = "
                + (testInputStrings.length == testQueue.size()));

        RandomizedQueue<String>.Node cursor = testQueue.head;

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
        Iterator<String> testIterator = testQueue.iterator();
        System.out.println("Test 4: iterator hasNext() returns true = " + testIterator.hasNext());

        // Test 5
        String test5string = "---";
        int test5int = 0;
        while (testIterator.hasNext()) {
            String current = testIterator.next();
            if (!current.equals(test5string)) {
                test5int++;
                test5string = current;
            }
        }

        System.out.println("Test 5: iterator next() returns results correctly and randomly = "
                + (test5int == testInputStrings.length));

        // Test 6
        while (!testQueue.isEmpty()) {
            testQueue.dequeue();
        }
        System.out.println("Test 6: dequeue until queue is empty = " + testQueue.isEmpty());
        System.out.println(sample());
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        Item[] itemArray;
        int i;

        ListIterator() {
            itemArray = (Item[]) new Object[size()];
            i = 0;

            if (!isEmpty()) {
                int j = 0;
                Node cursor = head;
                while (j < size()) {
                    itemArray[j++] = cursor.item;
                    cursor = cursor.next;
                }
                StdRandom.shuffle(itemArray);
            }
        }

        @Override
        public boolean hasNext() {
            return (i < size());
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return itemArray[i++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}