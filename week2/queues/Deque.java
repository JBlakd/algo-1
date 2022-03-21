/* *****************************************************************************
 *  Name: Ivan Hu
 *  Date: 21 March 2022
 *  Description: Algorithms 1 course, week 2, assignment Queues
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int n;          // size of the deque
    private Node first;     // front of deque
    private Node last;      // back of deque

    // helper linked list class
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        n = 0;
        assert check();
    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null;
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        Node oldFirst = first;
        first = new Node();
        if (n == 0) {
            last = first;
        }
        first.item = item;
        first.next = null;
        first.prev = oldFirst;
        if (oldFirst != null) {
            oldFirst.next = first;
        }
        n++;
        assert check();
    }

    // add the item to the back
    public void addLast(Item item) {
        Node oldLast = last;
        last = new Node();
        if (n == 0) {
            first = last;
        }
        last.item = item;
        last.next = oldLast;
        last.prev = null;
        if (oldLast != null) {
            oldLast.prev = last;
        }
        n++;
        assert check();
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        Item item = first.item; // save oldFirst's item to return
        first = first.prev;     // Reassign first to oldFirst.prev
        first.next = null;      // oldFirst is now unreferenced and garbage-collected
        n--;
        assert check();
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        Item item = last.item;  // save oldLast's item to return
        last = last.next;       // Reassign last to oldLast.next
        last.prev = null;       // oldLast is now unreferenced and garbage-collected
        n--;
        assert check();
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.prev;
            return item;
        }
    }


    // check internal invariants
    private boolean check() {
        if (n < 0) {
            return false;
        }

        if (n == 0) {
            if (first != null || last != null) {
                return false;
            }
        } else if (n == 1) {
            if (first == null || last == null) {
                return false;
            }
            if (first.next != null || first.prev != null) {
                return false;
            }
        } else {
            if (first == null || last == null) {
                return false;
            }
            if (first.next == null || first.prev != null
                    || last.next == null || last.prev != null) {
                return false;
            }
        }

        // check internal consistency of instance variable n
        int numberOfNodes = 0;
        for (Node x = first; x != null && numberOfNodes <= n; x = x.next) {
            numberOfNodes++;
        }
        if (numberOfNodes != n) {
            return false;
        }

        return true;
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();
        assert (deque.isEmpty());
        assert (deque.size() == 0);
        deque.addFirst("island,");
        deque.addLast("then");
        deque.addFirst("an");
        deque.addLast("he");
        deque.addFirst("from");
        deque.addLast("died");
        deque.addFirst("came");
        deque.addLast("from");
        deque.addFirst("He");
        deque.addLast("the");
        deque.addLast("street.");
        assert (deque.size() == 11);
        deque.addLast("Song: Say Hello 2 Heaven by Temple of the Dog");
        StdOut.println(deque.removeLast());
        for (String word : deque) {
            StdOut.print(word);
            StdOut.print(" ");
        }
    }
}
