/* *****************************************************************************
 *  Name: Ivan Hu
 *  Date: 08/04/2022
 *  Description: Algos 1 kdtrees assignment
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class KdTree {

    private Node root;
    private int n;

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        private Node(Point2D p) {
            if (p == null) {
                throw new IllegalArgumentException();
            }

            Node.this.p = p;
            rect = null;        // TODO calc rect
            lb = null;          // TODO
            rt = null;          // TODO
        }
    }

    private static class IteratedCount {
        private int val;

        private IteratedCount(int val) {
            IteratedCount.this.val = val;
        }

        private void increment() {
            val++;
        }

        private int get() {
            return val;
        }
    }

    // construct an empty set of points
    public KdTree() {
        n = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return (n == 0);
    }

    // number of points in the set
    public int size() {
        return n;
    }

    private Node insertHelper(Node curNode, Point2D p, boolean isVert, RectHV rect) {
        // recursion base case. Found the correct null spot for the new node.
        if (curNode == null) {
            Node newNode = new Node(p);
            newNode.rect = rect;
            n++;
            return newNode;
        }

        // Doing the correct comparison based on rank
        int cmp;
        if (isVert) {
            cmp = Point2D.X_ORDER.compare(p, curNode.p);
        }
        else {
            cmp = Point2D.Y_ORDER.compare(p, curNode.p);
        }

        // Recursively explore tree, inverting isVert when we go down one rank
        if (cmp < 0) {
            // Rectangle calculation
            RectHV childRect;
            if (isVert) {
                // Horizontal child will be left of the vertical current node
                childRect = new RectHV(curNode.rect.xmin(), curNode.rect.ymin(), curNode.p.x(),
                                       curNode.rect.ymax());
            }
            else {
                // Vertical child will be below the horizontal current node
                childRect = new RectHV(curNode.rect.xmin(), curNode.rect.ymin(),
                                       curNode.rect.xmax(), curNode.p.y());
            }
            curNode.lb = insertHelper(curNode.lb, p, !isVert, childRect);
        }
        else if (cmp > 0) {
            // Rectangle calculation
            RectHV childRect;
            if (isVert) {
                // Horizontal child will be right of the vertical current node
                childRect = new RectHV(curNode.p.x(), curNode.rect.ymin(), curNode.rect.xmax(),
                                       curNode.rect.ymax());
            }
            else {
                // Vertical child will be above the horizontal current node
                childRect = new RectHV(curNode.rect.xmin(), curNode.p.y(), curNode.rect.xmax(),
                                       curNode.rect.ymax());
            }
            curNode.rt = insertHelper(curNode.rt, p, !isVert, childRect);
        }
        else {
            // we have somehow found a duplicate point
            curNode.p = p;
        }

        // No harm done case. Assign the current node to itself.
        return curNode;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        // If root is null, then we hit the base case of insertHelper and root is set to contain p
        // If root is not null, this means it already exists and it is assigned to itself. No harm done.
        root = insertHelper(root, p, true, new RectHV(0, 0, 1, 1));
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        Node curNode = root;
        boolean isVert = true;
        while (curNode != null) {
            int cmp;
            if (isVert) {
                cmp = Point2D.X_ORDER.compare(p, curNode.p);
            }
            else {
                cmp = Point2D.Y_ORDER.compare(p, curNode.p);
            }

            if (cmp < 0) {
                curNode = curNode.lb;
            }
            else if (cmp > 0) {
                curNode = curNode.rt;
            }
            else {
                return true;
            }

            isVert = !isVert;
        }
        return false;
    }

    private void drawHelper(Node curNode, boolean isVert) {
        if (curNode == null) {
            return;
        }

        // draw point
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        curNode.p.draw();

        // draw line
        if (isVert) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(0.005);
            StdDraw.line(curNode.p.x(), curNode.rect.ymin(), curNode.p.x(), curNode.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius(0.005);
            StdDraw.line(curNode.rect.xmin(), curNode.p.y(), curNode.rect.xmax(), curNode.p.y());
        }

        // recursively draw for children
        if (curNode.lb != null) {
            drawHelper(curNode.lb, !isVert);
        }

        if (curNode.rt != null) {
            drawHelper(curNode.rt, !isVert);
        }
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.clear();
        drawHelper(root, true);
    }

    private void rangeHelper(RectHV rect, Node curNode, ArrayList<Point2D> arr, IteratedCount ic) {
        if (curNode == null) {
            return;
        }

        ic.increment();

        if (rect.contains(curNode.p)) {
            arr.add(curNode.p);
        }

        // pruning
        if (curNode.lb != null && rect.intersects(curNode.lb.rect)) {
            rangeHelper(rect, curNode.lb, arr, ic);
        }
        if (curNode.rt != null && rect.intersects(curNode.rt.rect)) {
            rangeHelper(rect, curNode.rt, arr, ic);
        }

    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        ArrayList<Point2D> retVal = new ArrayList<Point2D>();
        IteratedCount ic = new IteratedCount(0);
        rangeHelper(rect, root, retVal, ic);

        // StdOut.println(
        //         "KdTree.range() iterated through " + ic.get() + " points out of " + size());
        // StdOut.println("KdTree.range() iterated through " + ((double) ic.get() / size()) * 100
        //                        + "% of points");

        return retVal;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        // TODO
        return null;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        KdTree kdTree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdTree.insert(p);
        }

        // draw all
        kdTree.draw();

        // draw rectangle
        RectHV myRect = new RectHV(0.11, 0.2, 0.57, 0.8);
        StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
        StdDraw.setPenRadius(0.0075);
        myRect.draw();

        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.setPenRadius(0.02);
        StdOut.println("The rectagle contains the following points:");
        for (Point2D containedPoint : kdTree.range(myRect)) {
            containedPoint.draw();
            StdOut.println(containedPoint.toString());
        }
    }
}
