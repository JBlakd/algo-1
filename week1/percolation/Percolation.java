/* *****************************************************************************
 *  Name:              Ivan Hu
 *  Coursera User ID:  449db745b604e09acceeb1b09161636d
 *  Last modified:     19/03/2022
 **************************************************************************** */

public class Percolation {

    private int[] grid;
    private boolean[] gridOpen;
    // Very important during the Monte Carlo Simulation where we randomly open sites
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
        grid = new int[gridSize + 2];
        gridOpen = new boolean[gridSize + 2];

        // Top and bottom virtual sites
        grid[0] = 0;
        gridOpen[0] = true;
        grid[gridSize + 1] = gridSize + 1;
        gridOpen[gridSize + 1] = true;

        // Initializing every site
        // non virtual sites has a starting index of 1
        for (int i = 1; i <= gridSize; i++) {
            // if (i <= n) {
            //     // connect top row sites to top virtual site
            //     grid[i] = 0;
            //     // gridOpen[i] = false;
            // } else if (i <= gridSize && i > (gridSize - n)) {
            //     // connect bottom row sites to bottom virtual site
            //     grid[i] = gridSize + 1;
            //     // gridOpen[i] = false;
            // } else {
            //     // connect non-virtual sites to itself
            //     grid[i] = i;
            //     // gridOpen[i] = false;
            // }
            grid[i] = i;
            gridOpen[i] = false;
        }
    }

    private int[] root(int p) {
        int[] retval = new int[2];

        int pRoot = p;
        // List<Integer> pVisited = new ArrayList<Integer>();
        int[] pVisited = new int[gridSize];
        int numVisited = 0;

        // Find root of p
        while (grid[pRoot] != pRoot) {
            // pVisited.add(pRoot);
            pVisited[numVisited] = pRoot;
            numVisited++;
            pRoot = grid[pRoot];
        }
        // Path Compression: just after computing the root, set the id of
        // each examined node to point to that root
        for (int i = 0; i < numVisited; i++) {
            grid[pVisited[i]] = pRoot;
        }
        // for (int id : pVisited) {
        //     grid[id] = pRoot;
        // }

        retval[0] = pRoot;
        // retval[1] = pVisited.size();
        retval[1] = numVisited;
        return retval;
    }

    private boolean find(int p, int q) {
        // Index 0 of root() return value is the root itself

        return (root(p)[0] == root(q)[0]);

        // Readable form:
        // if (root(p)[0] == root(q)[0]) {
        //     return true;
        // } else {
        //     return false;
        // }
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
                union(toIndex(row, col), toIndex(row - 1, col));
            }
        } else {
            // Special case for checking up if at row 1
            // Union with top virtual site
            union(toIndex(row, col), 0);
        }

        // down
        if (row < gridDim) {
            if (isOpen(row + 1, col)) {
                union(toIndex(row, col), toIndex(row + 1, col));
            }
        } else {
            // Special case for checking down if at final row
            // Union with bottom virtual site
            union(toIndex(row, col), gridSize + 1);
        }

        // left
        if (col > 1 && isOpen(row, col - 1)) {
            union(toIndex(row, col), toIndex(row, col - 1));
        }
        // right
        if (col < gridDim && isOpen(row, col + 1)) {
            union(toIndex(row, col), toIndex(row, col + 1));
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

    // public static void main(String[] args) {
    //
    // }
}
