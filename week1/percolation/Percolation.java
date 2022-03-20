/* *****************************************************************************
 *  Name:              Ivan Hu
 *  Coursera User ID:  449db745b604e09acceeb1b09161636d
 *  Last modified:     19/03/2022
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private WeightedQuickUnionUF grid;
    private boolean[] gridOpen;
    private final int gridDim;
    private final int gridSize;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        gridDim = n;
        gridSize = n * n;

        // n by n grid, plus top and bottom virtual sites
        grid = new WeightedQuickUnionUF(gridSize + 2);
        gridOpen = new boolean[gridSize + 2];

        // Top and bottom virtual sites
        gridOpen[0] = true;
        gridOpen[gridSize + 1] = true;

        // Initializing every site
        // non virtual sites has a starting index of 1
        for (int i = 1; i <= gridSize; i++) {
            gridOpen[i] = false;
        }
    }

    private int toIndex(int row, int col) {
        return (row - 1) * gridDim + col;
    }

    // private int[] toCoord(int index) {
    //     int[] coord = new int[2];
    //     coord[0] = (index / gridDim) + 1;
    //     coord[1] = index % gridDim;
    //     return coord;
    // }

    // public void openRandomBlocked() {
    //     int[] coord = toCoord(blockedSites.get(StdRandom.uniform(blockedSites.size())));
    //     open(coord[0], coord[1]);
    // }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row <= 0 || col <= 0 || row > gridDim || col > gridDim) {
            throw new IllegalArgumentException();
        }

        if (isOpen(row, col)) {
            return;
        }

        // Checking up neighbour. If neighbour is open, union this site with neighbour
        if (row > 1) {
            if (isOpen(row - 1, col)) {
                grid.union(toIndex(row, col), toIndex(row - 1, col));
            }
        } else {
            // Special case for checking up if at row 1
            // Union with top virtual site
            grid.union(toIndex(row, col), 0);
        }

        // down
        if (row < gridDim) {
            if (isOpen(row + 1, col)) {
                grid.union(toIndex(row, col), toIndex(row + 1, col));
            }
        } else {
            // Special case for checking down if at final row
            // Union with bottom virtual site
            grid.union(toIndex(row, col), gridSize + 1);
        }

        // left
        if (col > 1 && isOpen(row, col - 1)) {
            grid.union(toIndex(row, col), toIndex(row, col - 1));
        }
        // right
        if (col < gridDim && isOpen(row, col + 1)) {
            grid.union(toIndex(row, col), toIndex(row, col + 1));
        }

        gridOpen[toIndex(row, col)] = true;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row <= 0 || col <= 0 || row > gridDim || col > gridDim) {
            throw new IllegalArgumentException();
        }

        return gridOpen[toIndex(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row <= 0 || col <= 0 || row > gridDim || col > gridDim) {
            throw new IllegalArgumentException();
        }

        if (!isOpen(row, col)) {
            return false;
        }

        return (grid.find(0) == grid.find(toIndex(row, col)));
        // return grid.find(0, toIndex(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int retval = 0;
        for (int i = 1; i <= gridSize; i++) {
            if (gridOpen[i]) {
                retval++;
            }
        }
        return retval;
    }

    // does the system percolate?
    public boolean percolates() {
        return (grid.find(0) == grid.find(gridSize + 1));
        // return grid.find(0, gridSize + 1);
    }

    // public static void main(String[] args) {
    //
    // }
}
