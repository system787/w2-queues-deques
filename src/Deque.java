import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int size;
    private Node head;
    private Node tail;

    private class Node<Item> {
        Item item;
        Node next;
        Node prev;
    }

    // construct an empty deque
    public Deque() {
        head = null;
        tail = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return (size == 0);
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node temp = new Node();
        temp.item = item;
        temp.prev = null;
        if (!isEmpty()) {
            temp.next = head;
            head = temp;
            head.next.prev = head;
        } else {
            head = temp;
            tail = temp;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node temp = new Node();
        temp.item = item;
        temp.next = null;
        if (!isEmpty()) {
            temp.prev = tail;
            tail = temp;
            tail.prev.next = temp;
        } else {
            head = temp;
            tail = temp;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = (Item) head.item;
        if (size() == 1) {
            head = null;
            tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }
        size--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = (Item) tail.item;
        if (size() == 1) {
            head = null;
            tail = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }
        size--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        Node cursor = head;
        @Override
        public Item next() {
            if (cursor.next == null) {
                throw new NoSuchElementException();
            }
            Item item = (Item) cursor.item;
            cursor = cursor.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return (cursor.next != null);
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        new Deque().run();
    }

    private void run() {
        String[] testInputStrings =
                new String[]{ "elden", "ring", "is", "a", "great", "game,", "albeit", "quite", "overrated" };
        Deque test1 = new Deque();

        for (String s : testInputStrings) {
            test1.addFirst(s);
        }

        System.out.println("Test 1 Deque object size matches input size = " + (test1.size() == testInputStrings.length));
        test1.removeFirst();
        test1.removeLast();

        String[] testRemoveFirstAndLast = new String[testInputStrings.length - 2];

        for (int i = 0; i < testRemoveFirstAndLast.length; i++) {
            testRemoveFirstAndLast[i] = (String) test1.head.item;
            test1.removeFirst();
        }

        boolean testResult = false;
        for (int i = 0; i < testRemoveFirstAndLast.length; i++) {
            if (testRemoveFirstAndLast[i] != testInputStrings[testRemoveFirstAndLast.length - i]) {
                testResult = true;
            }
        }

        System.out.println("Test 1 Deque object removed first and last strings = " + !testResult);

        while (!test1.isEmpty()) {
            test1.removeLast();
        }

        for (int i = testInputStrings.length - 1; i >= 0; i--) {
            test1.addLast(testInputStrings[i]);
        }

        String[] thirdTest = new String[2];

        StringBuffer sb = new StringBuffer();
        for (String s : testInputStrings) {
            sb.append(s).append(" ");
        }
        thirdTest[0] = sb.toString();
        sb = new StringBuffer();

        Node cursor = test1.tail;
        sb.append(cursor.item).append(" ");
        while (cursor.prev != null) {
            sb.append((String) cursor.prev.item).append(" ");
            cursor = cursor.prev;
        }
        thirdTest[1] = sb.toString();

        System.out.println("Compare contents of Test 1 Deque object to original input = " + thirdTest[0].equals(thirdTest[1]));
    }

}
