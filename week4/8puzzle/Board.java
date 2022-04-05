/* *****************************************************************************
 *  Name: Ivan Hu
 *  Date: 02/04/2022
 *  Description: Algos 1 Week 4 Assignment slider puzzle
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {

    private int[] a;
    private int squareSide;
    private int squareSize;
    private int hamming;
    private int manhattan;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        checkBoard(tiles);

        squareSide = tiles.length;
        squareSize = squareSide * squareSide;

        // +1 because 0 index is ignored
        // a has a useful range of 1 to squareSize
        a = new int[squareSize + 1];

        // populating a
        int aIndex = 1;
        for (int[] row : tiles) {
            for (int element : row) {
                a[aIndex] = element;
                aIndex++;
            }
        }

        hamming = calcHamming();
        manhattan = calcManhattan();
    }

    private void checkBoard(int[][] tiles) {
        if (tiles == null) {
            throw new IllegalArgumentException();
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder(Integer.toString(squareSide) + "\n");
        for (int i = 1; i < a.length; i++) {
            sb.append(a[i]);
            if (i % squareSide == 0) {
                sb.append("\n");
            }
            else {
                sb.append(" ");
            }
        }

        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return squareSide;
    }

    // number of tiles out of place
    private int calcHamming() {
        int retVal = 0;
        // Loop until penultimate because the last element should be 0 and not needed to consider
        for (int i = 1; i < squareSize; i++) {
            if (a[i] != i) {
                retVal++;
            }
        }
        return retVal;
    }

    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    private int calcManhattan() {
        int manhattanSum = 0;

        for (int i = 1; i <= squareSize; i++) {
            if (a[i] == i || a[i] == 0) {
                continue;
            }

            int curManhattan = 0;

            int curPosCopy = i;
            if (i < a[i]) {
                // while (curPosCopy + squareSide <= a[i]) {
                //     curPosCopy += squareSide;
                //     curManhattan++;
                // }
                // while (curPosCopy < a[i]) {
                //     curPosCopy++;
                //     // Check special case when incrementing by one results in next row
                //     if (curPosCopy % squareSide == 1) {
                //         curManhattan += squareSide;
                //     }
                //     else {
                //         curManhattan++;
                //     }
                // }

                // New method: change row until same row, and then in/decrement by one
                // Loop to add row
                while (true) {
                    int curRowStart;
                    int curRowEnd;
                    if (curPosCopy % squareSide == 0) {
                        curRowEnd = curPosCopy;
                        curRowStart = curRowEnd - squareSide + 1;
                    }
                    else {
                        curRowStart = curPosCopy - (curPosCopy % squareSide) + 1;
                        curRowEnd = curRowStart + squareSide - 1;
                    }
                    // Check if destination is at the same row as curPosCopy
                    if (a[i] >= curRowStart && a[i] <= curRowEnd) {
                        break;
                    }
                    curPosCopy += squareSide;
                    curManhattan++;
                }
                // Loop to increment or decrement by 1
                while (curPosCopy != a[i]) {
                    if (curPosCopy > a[i]) {
                        curPosCopy--;
                    }
                    else {
                        curPosCopy++;
                    }
                    curManhattan++;
                }
            }
            else {
                // while (curPosCopy - squareSide >= a[i]) {
                //     curPosCopy -= squareSide;
                //     curManhattan++;
                // }
                // while (curPosCopy > a[i]) {
                //     curPosCopy--;
                //     // Check special case when decrementing by one results in prev row
                //     if (curPosCopy % squareSide == 0) {
                //         curManhattan += squareSide;
                //     }
                //     else {
                //         curManhattan++;
                //     }
                // }

                // New method: change row until same row, and then in/decrement by one
                // Loop to subtract row
                while (true) {
                    // calculate current row
                    int curRowStart;
                    int curRowEnd;
                    if (curPosCopy % squareSide == 0) {
                        curRowEnd = curPosCopy;
                        curRowStart = curRowEnd - squareSide + 1;
                    }
                    else {
                        curRowStart = curPosCopy - (curPosCopy % squareSide) + 1;
                        curRowEnd = curRowStart + squareSide - 1;
                    }
                    // Check if destination is at the same row as curPosCopy
                    if (a[i] >= curRowStart && a[i] <= curRowEnd) {
                        break;
                    }
                    curPosCopy -= squareSide;
                    curManhattan++;
                }
                // Loop to increment or decrement by 1
                while (curPosCopy != a[i]) {
                    if (curPosCopy > a[i]) {
                        curPosCopy--;
                    }
                    else {
                        curPosCopy++;
                    }
                    curManhattan++;
                }
            }

            manhattanSum += curManhattan;
        }

        return manhattanSum;
    }

    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 1; i < squareSize - 1; i++) {
            if (a[i] != i) {
                return false;
            }
        }

        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;

        if (this.getClass() != y.getClass()) {
            return false;
        }

        Board that = (Board) y;

        if (that.dimension() != this.dimension()) {
            return false;
        }

        for (int i = 1; i <= squareSize; i++) {
            if (this.a[i] != that.a[i]) {
                return false;
            }
        }

        return true;
    }

    private int[][] toSquare(int[] arr, int dim) {
        int[][] square = new int[dim][dim];

        int arrIdx = 1;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                square[i][j] = arr[arrIdx];
                arrIdx++;
            }
        }

        return square;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<Board>();
        // Finding index of 0
        int zeroIndex = -1;
        for (int i = 1; i <= squareSize; i++) {
            if (a[i] == 0) {
                zeroIndex = i;
            }
        }
        if (zeroIndex == -1) {
            throw new RuntimeException();
        }

        int[] reuseableCopy1D;

        // top neighbour. Check if not in top row.
        if (zeroIndex - squareSide >= 1) {
            reuseableCopy1D = Arrays.copyOf(a, a.length);
            reuseableCopy1D[zeroIndex] = reuseableCopy1D[zeroIndex - squareSide];
            reuseableCopy1D[zeroIndex - squareSide] = 0;
            neighbors.add(new Board(toSquare(reuseableCopy1D, squareSide)));
        }

        // bottom neighbour. Check if not in bottom row.
        if (zeroIndex + squareSide <= squareSize) {
            reuseableCopy1D = Arrays.copyOf(a, a.length);
            reuseableCopy1D[zeroIndex] = reuseableCopy1D[zeroIndex + squareSide];
            reuseableCopy1D[zeroIndex + squareSide] = 0;
            neighbors.add(new Board(toSquare(reuseableCopy1D, squareSide)));
        }

        // left neighbour. Check if not in column 1
        if (!(zeroIndex % squareSide == 1)) {
            reuseableCopy1D = Arrays.copyOf(a, a.length);
            reuseableCopy1D[zeroIndex] = reuseableCopy1D[zeroIndex - 1];
            reuseableCopy1D[zeroIndex - 1] = 0;
            neighbors.add(new Board(toSquare(reuseableCopy1D, squareSide)));
        }

        // right neighbour. Check if not in last column
        if (!(zeroIndex % squareSide == 0)) {
            reuseableCopy1D = Arrays.copyOf(a, a.length);
            reuseableCopy1D[zeroIndex] = reuseableCopy1D[zeroIndex + 1];
            reuseableCopy1D[zeroIndex + 1] = 0;
            neighbors.add(new Board(toSquare(reuseableCopy1D, squareSide)));
        }

        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        // Choose random tile index
        int randIdx = StdRandom.uniform(1, squareSize + 1);
        while (a[randIdx] == 0) {
            randIdx = StdRandom.uniform(1, squareSize + 1);
        }

        // determine which directions are swappable
        // Left is 0, Top is 1, Right is 2, Bottom is 3
        // boolean[] swappable = new boolean[4];
        ArrayList<Integer> swappable = new ArrayList<Integer>();
        // left
        if (!(randIdx % squareSide == 1) && a[randIdx - 1] != 0) {
            swappable.add(0);
        }
        // top
        if (randIdx - squareSide >= 1 && a[randIdx - squareSide] != 0) {
            swappable.add(1);
        }
        // right
        if (!(randIdx % squareSide == 0) && a[randIdx + 1] != 0) {
            swappable.add(2);
        }
        // bottom
        if (randIdx + squareSide <= squareSize && a[randIdx + squareSide] != 0) {
            swappable.add(3);
        }

        int randDir = swappable.get(StdRandom.uniform(swappable.size()));
        int[] reuseableCopy1D = Arrays.copyOf(a, a.length);
        Board retVal;

        switch (randDir) {
            case 0:
                reuseableCopy1D[randIdx] = a[randIdx - 1];
                reuseableCopy1D[randIdx - 1] = a[randIdx];
                break;
            case 1:
                reuseableCopy1D[randIdx] = a[randIdx - squareSide];
                reuseableCopy1D[randIdx - squareSide] = a[randIdx];
                break;
            case 2:
                reuseableCopy1D[randIdx] = a[randIdx + 1];
                reuseableCopy1D[randIdx + 1] = a[randIdx];
                break;
            case 3:
                reuseableCopy1D[randIdx] = a[randIdx + squareSide];
                reuseableCopy1D[randIdx + squareSide] = a[randIdx];
                break;
        }

        retVal = new Board(toSquare(reuseableCopy1D, squareSide));
        return retVal;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] reuseable2D = {
                { 5, 11, 14, 9 },
                { 15, 3, 4, 1 },
                { 10, 6, 2, 12 },
                { 7, 0, 8, 13 }
        };

        // int[][] reuseable2D = {
        //         { 5, 1, 8 },
        //         { 2, 7, 3 },
        //         { 4, 0, 6 }
        // };

        // int[][] reuseable2D = {
        //         { 0, 3 },
        //         { 2, 1 }
        // };
        Board reuseableBoard = new Board(reuseable2D);
        StdOut.println(reuseableBoard.toString());
        StdOut.println("Dimension: " + reuseableBoard.dimension());
        StdOut.println("Hamming: " + reuseableBoard.hamming());
        StdOut.println("Manhattan: " + reuseableBoard.manhattan());

        StdOut.println("Neighbors: ");
        for (Board neighbor : reuseableBoard.neighbors()) {
            StdOut.println(neighbor.toString());
        }

        StdOut.println("4 example random twins: ");
        for (int i = 0; i < 4; i++) {
            StdOut.println(reuseableBoard.twin().toString());
        }

        int[][] reuseable2D2 = {
                { 5, 11, 14, 9 },
                { 15, 3, 4, 1 },
                { 10, 6, 2, 12 },
                { 7, 0, 8, 13 }
        };

        // int[][] reuseable2D2 = {
        //         { 5, 1, 8 },
        //         { 2, 7, 3 },
        //         { 4, 0, 6 }
        // };
        // int[][] reuseable2D2 = {
        //         { 0, 3 },
        //         { 2, 1 }
        //
        // };
        Board reuseableBoard2 = new Board(reuseable2D2);
        if (reuseableBoard.equals(reuseableBoard2)) {
            StdOut.println("reuseableBoard is equal to reuseableBoard2.");
        }
        else {
            StdOut.println("reuseableBoard is NOT equal to reuseableBoard2.");
        }

        int[][] goalBoard2D = {
                { 1, 2, 3 },
                { 4, 5, 6 },
                { 7, 8, 0 }
        };
        Board goalBoard = new Board(goalBoard2D);
        if (goalBoard.isGoal()) {
            StdOut.println("goalBoard is a valid goal board.");
        }
        else {
            StdOut.println("goalBoard is NOT a valid goal board.");
        }

        int[][] notEqual1 = {
                { 1, 3 },
                { 0, 2 }
        };
        Board notEqual1Board = new Board(notEqual1);
        int[][] notEqual2 = {
                { 1, 3 },
                { 2, 0 }
        };
        Board notEqual2Board = new Board(notEqual2);
        if (notEqual1Board.equals(notEqual2Board)) {
            StdOut.println("notEqual1 equals notEqual2.");
        }
        else {
            StdOut.println("notEqual1 does not equal notEqual2.");
        }
    }
}
