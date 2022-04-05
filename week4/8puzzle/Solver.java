/* *****************************************************************************
 *  Name: Ivan Hu
 *  Date: 04/04/2022
 *  Description: Algos 1 Week 4 Assignment slider puzzle
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.ResizingArrayStack;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Comparator;

public class Solver {

    // private MinPQ<SearchNode> pq;
    // private ArrayList<Board> cachedBoards;
    private SearchNode goal;
    // private MinPQ<SearchNode> pqTwin;
    // private ArrayList<Board> cachedBoardsTwin;
    private SearchNode goalTwin;

    private class SearchNode {
        private final Board curBoard;
        private final int moves;
        private final SearchNode prevSearchNode;

        private SearchNode(Board curBoard, int moves, SearchNode prevSearchNode) {
            this.curBoard = curBoard;
            this.moves = moves;
            this.prevSearchNode = prevSearchNode;
        }
    }

    // private class HammingOrder implements Comparator<SearchNode> {
    //     public int compare(SearchNode s1, SearchNode s2) {
    //         return Integer
    //                 .compare(s1.moves + s1.curBoard.hamming(),
    //                          s2.moves + s2.curBoard.hamming());
    //     }
    // }

    private class ManhattanOrder implements Comparator<SearchNode> {
        public int compare(SearchNode s1, SearchNode s2) {
            return Integer
                    .compare(s1.moves + s1.curBoard.manhattan(),
                             s2.moves + s2.curBoard.manhattan());
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        Board initialTwin = initial.twin();

        ArrayList<Board> cachedBoards = new ArrayList<Board>();
        ArrayList<Board> cachedBoardsTwin = new ArrayList<Board>();

        Comparator<SearchNode> manhattanOrder = new ManhattanOrder();

        MinPQ<SearchNode> pq = new MinPQ<SearchNode>(manhattanOrder);
        pq.insert(new SearchNode(initial, 0, null));
        cachedBoards.add(initial);
        MinPQ<SearchNode> pqTwin = new MinPQ<SearchNode>(manhattanOrder);
        pqTwin.insert(new SearchNode(initialTwin, 0, null));
        cachedBoardsTwin.add(initialTwin);

        while (true) {
            SearchNode curNode = pq.delMin();
            SearchNode curNodeTwin = pqTwin.delMin();
            if (curNode.curBoard.isGoal()) {
                goal = curNode;
                break;
            }
            if (curNodeTwin.curBoard.isGoal()) {
                goalTwin = curNodeTwin;
                break;
            }

            // add neighbouring SearchNodes to pq
            for (Board neighborBoard : curNode.curBoard.neighbors()) {
                // Check if cachedBoards contain neighborBoard's equivalent
                boolean cached = false;
                for (Board cachedBoard : cachedBoards) {
                    if (cachedBoard.equals(neighborBoard)) {
                        cached = true;
                        break;
                    }
                }
                if (cached) {
                    continue;
                }

                pq.insert(new SearchNode(neighborBoard, curNode.moves + 1, curNode));
            }

            // add neighbouring SearchNodes to pqTwin
            for (Board neighborBoard : curNodeTwin.curBoard.neighbors()) {
                // Check if cachedBoards contain neighborBoard's equivalent
                boolean cached = false;
                for (Board cachedBoardTwin : cachedBoardsTwin) {
                    if (cachedBoardTwin.equals(neighborBoard)) {
                        cached = true;
                        break;
                    }
                }
                if (cached) {
                    continue;
                }

                pqTwin.insert(new SearchNode(neighborBoard, curNodeTwin.moves + 1, curNodeTwin));
            }
        }

        // the goal should have been calculated now for either initial or initialTwin


        if (isSolvable() && goalTwin != null) {
            throw new RuntimeException("Initial board is solvable but goalTwin is not null.");
        }
        if (!isSolvable() && goalTwin == null) {
            throw new RuntimeException("Initial board is unsolvable but goalTwin is null.");
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return (goal != null);
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        // TODO -1 if unsolvable
        if (!isSolvable()) {
            return -1;
        }

        return goal.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        // TODO null if unsolvable
        if (!isSolvable()) {
            return null;
        }

        ResizingArrayStack<Board> solution = new ResizingArrayStack<>();
        SearchNode curNode = goal;
        while (true) {
            if (curNode.prevSearchNode == null) {
                solution.push(curNode.curBoard);
                break;
            }

            solution.push(curNode.curBoard);
            curNode = curNode.prevSearchNode;
        }

        return solution;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
