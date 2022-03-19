/* *****************************************************************************
 *  Name:              Ivan Hu
 *  Coursera User ID:  449db745b604e09acceeb1b09161636d
 *  Last modified:     19/03/2022
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] thresholdArray;
    private int trials;
    private int gridDim;

    private int[] toCoord(int index) {
        int[] coord = new int[2];
        coord[0] = (index / gridDim) + 1;
        coord[1] = index % gridDim;
        return coord;
    }

    private void openRandomBlocked(Percolation percolation) {
        int[] coord = toCoord(
                percolation.blockedSites.get(StdRandom.uniform(percolation.blockedSites.size())));
        percolation.open(coord[0], coord[1]);
    }

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }

        thresholdArray = new double[trials];
        this.trials = trials;
        gridDim = n;

        int gridSize = n * n;
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                openRandomBlocked(percolation);
            }
            // Should percolate by now
            thresholdArray[i] = (double) percolation.numberOfOpenSites() / gridSize;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholdArray);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholdArray);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (1.96 * stddev()) / java.lang.Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (1.96 * stddev()) / java.lang.Math.sqrt(trials);
    }

    // test client (see below)
    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }

        int[] argsInt = { Integer.parseInt(args[0]), Integer.parseInt(args[1]) };

        PercolationStats percolationStats = new PercolationStats(argsInt[0], argsInt[1]);

        StdOut.println("mean\t\t\t\t= " + percolationStats.mean());
        StdOut.println("stddev\t\t\t\t= " + percolationStats.stddev());
        StdOut.println("95% confidence interval\t= [" + percolationStats.confidenceLo()
                               + ", " + percolationStats.confidenceHi() + "]");
    }
}
