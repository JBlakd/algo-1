/* *****************************************************************************
 *  Name:              Ivan Hu
 *  Coursera User ID:  449db745b604e09acceeb1b09161636d
 *  Last modified:     March 17, 2022
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        int i = 1;
        String champion = "";
        while (!StdIn.isEmpty()) {
            String currentArg = StdIn.readString();
            if (StdRandom.bernoulli((double) 1 / i)) {
                champion = currentArg;
            }
            i++;
        }
        StdOut.println(champion);
    }
}
