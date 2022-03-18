/* *****************************************************************************
 *  Name:              Ivan Hu
 *  Coursera User ID:  449db745b604e09acceeb1b09161636d
 *  Last modified:     17/03/2022
 **************************************************************************** */

import java.util.ArrayList;
import java.util.List;

public class Percolation {
    private int[] grid;
    private boolean[] gridOpen;
    private int gridDim;
    private int gridSize;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        gridDim = n;
        gridSize = n * n;

        // n by n grid, plus top and bottom virtual sites
        grid = new int[gridSize + 2];
        gridOpen = new boolean[gridSize + 2];

        // Top and bottom virtual sites
        grid[0] = 0;
        gridOpen[0] = true;
        grid[gridSize + 1] = gridSize + 1;
        gridOpen[gridSize + 1] = true;

        // non virtual sites has a starting index of 1
        for (int i = 1; i <= gridSize; i++) {
            if (i <= n) {
                // connect top row sites to top virtual site
                grid[i] = 0;
            } else if (i <= gridSize && i > (gridSize - n)) {
                // connect bottom row sites to bottom virtual site
                grid[i] = gridSize + 1;
            } else {
                // connect non-virtual sites to itself
                grid[i] = i;
            }
        }
    }

    private int[] root(int p) {
        int[] retval = new int[2];

        int pRoot = p;
        List<Integer> pVisited = new ArrayList<Integer>();

        // Find root of p
        while (grid[pRoot] != pRoot) {
            pVisited.add(pRoot);
            pRoot = grid[pRoot];
        }
        // Path Compression: just after computing the root, set the id of
        // each examined node to point to that root
        for (int id : pVisited) {
            grid[id] = pRoot;
        }

        retval[0] = pRoot;
        retval[1] = pVisited.size();
        return retval;
    }

    private boolean find(int p, int q) {
        // Index 0 of root() return value is the root itself
        if (root(p)[0] == root(q)[0]) {
            return true;
        } else {
            return false;
        }
    }

    private void union(int p, int q) {
        // Rudimentary weighting based on how many ranks until root
        if (p == q) {
            return;
        }

        int[] pRetval = root(p);
        int[] qRetval = root(q);

        // If p-tree is "smaller"
        if (pRetval[1] <= qRetval[1]) {
            // Make p-tree's root as q-tree's root
            grid[pRetval[0]] = grid[qRetval[0]];
        } else {
            grid[qRetval[0]] = grid[pRetval[0]];
        }
    }

    private int toIndex(int row, int col) {
        return (row - 1) * gridDim + col;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        // Check up neighbour. If neighbour is open, union this site with neighbour
        if (row > 1 && isOpen(row - 1, col)) {
            union(toIndex(row, col), toIndex(row - 1, col));
        }
        // down
        if (row < gridDim && isOpen(row + 1, col)) {
            union(toIndex(row, col), toIndex(row + 1, col));
        }
        // left
        if (col > 1 && isOpen(row, col - 1)) {
            union(toIndex(row, col), toIndex(row, col - 1));
        }
        // right
        if (col < gridDim && isOpen(row, col + 1)) {
            union(toIndex(row, col), toIndex(row, col + 1));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return gridOpen[toIndex(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!isOpen(row, col)) {
            return false;
        }

        return find(0, toIndex(row, col));
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
        return find(0, gridSize + 1);
    }

    public static void main(String[] args) {

    }
}
