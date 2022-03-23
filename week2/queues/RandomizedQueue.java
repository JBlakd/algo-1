/* *****************************************************************************
 *  Name: Ivan Hu
 *  Date: 21 March 2022
 *  Description: Algorithms 1 course, week 2, assignment Queues
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    // initial capacity of underlying resizing array
    private static final int INIT_CAPACITY = 8;

    private Item[] a;         // array of items
    private int n;            // number of elements on stack

    private void resize(int capacity) {
        assert capacity >= n;

        // textbook implementation
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            copy[i] = a[i];
        }
        a = copy;
    }

    // construct an empty randomized queue
    public RandomizedQueue() {
        a = (Item[]) new Object[INIT_CAPACITY];
        n = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return (n == 0);
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        if (n == a.length) {
            resize(2 * a.length);
        }

        a[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int randomIndex = StdRandom.uniform(n);
        Item copy = a[randomIndex];
        a[randomIndex] = a[n - 1];
        a[n - 1] = null;
        n--;

        // shrink size of array if necessary
        if (n > 0 && n == a.length / 4) {
            resize(a.length / 2);
        }

        return copy;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int randomIndex = StdRandom.uniform(n);
        return a[randomIndex];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class RandomizedQueueIterator implements Iterator<Item> {
        private int i;

        public RandomizedQueueIterator() {
            i = StdRandom.uniform(n);
        }

        public boolean hasNext() {
            return !isEmpty() || !(n == 1);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            i = StdRandom.uniform(n);
            return a[i];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        assert (rq.isEmpty());
        assert (rq.size() == 0);
        rq.enqueue("He");
        rq.enqueue("came");
        rq.enqueue("from");
        rq.enqueue("an");
        rq.enqueue("island,");
        rq.enqueue("then");
        rq.enqueue("he");
        rq.enqueue("died");
        rq.enqueue("from");
        rq.enqueue("the");
        rq.enqueue("street.");
        assert (rq.size() == 11);

        int[] wordCount = new int[10];

        // Checking uniformity of sample()
        for (int i = 0; i < 11000; i++) {
            String word = rq.sample();
            switch (word) {
                case "He":
                    wordCount[0]++;
                    break;
                case "came":
                    wordCount[1]++;
                    break;
                case "from":
                    wordCount[2]++;
                    break;
                case "an":
                    wordCount[3]++;
                    break;
                case "island,":
                    wordCount[4]++;
                    break;
                case "then":
                    wordCount[5]++;
                    break;
                case "he":
                    wordCount[6]++;
                    break;
                case "died":
                    wordCount[7]++;
                    break;
                case "the":
                    wordCount[8]++;
                    break;
                case "street.":
                    wordCount[9]++;
                    break;
            }
        }
        for (int count : wordCount) {
            StdOut.println(count);
        }

        assert (rq.size() == 11);

        while (!rq.isEmpty()) {
            StdOut.print(rq.dequeue());
            StdOut.print(" ");
        }
        assert (rq.isEmpty());
        assert (rq.size() == 0);
    }

}
