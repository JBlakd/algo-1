/* *****************************************************************************
 *  Name: Ivan Hu
 *  Date: 08/04/2022
 *  Description: Algos 1 kdtrees assignment
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;

public class PointSET {
    private SET<Point2D> ps;

    // construct an empty set of points
    public PointSET() {
        ps = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return ps.isEmpty();
    }

    // number of points in the set
    public int size() {
        return ps.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (!ps.contains(p)) {
            ps.add(p);
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return ps.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : ps) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        ArrayList<Point2D> retVal = new ArrayList<Point2D>();
        for (Point2D p : ps) {
            if (rect.contains(p)) {
                retVal.add(p);
            }
        }

        return retVal;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        // This point does lie on the unit square, so is not a valid point in this exercise
        Point2D champion = new Point2D(-1, -1);
        boolean first = true;
        for (Point2D curP : ps) {
            if (first) {
                champion = curP;
                first = false;
                continue;
            }

            // If current point is closer than champion
            if (curP.distanceSquaredTo(p) < champion.distanceSquaredTo(p)) {
                champion = curP;
            }
        }

        return champion;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }

        // draw all of the points
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        brute.draw();

        RectHV myRect = new RectHV(0.11, 0.2, 0.57, 0.8);
        StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
        StdDraw.setPenRadius(0.0025);
        myRect.draw();

        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(0.01);
        StdOut.println("The rectagle contains the following points:");
        for (Point2D containedPoint : brute.range(myRect)) {
            containedPoint.draw();
            StdOut.println(containedPoint.toString());
        }

        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.setPenRadius(0.01);
        Point2D randomPoint = new Point2D(StdRandom.uniform(0.0, 1.0), StdRandom.uniform(0.0, 1.0));
        randomPoint.draw();
        StdDraw.setPenRadius(0.005);
        Point2D nearestPoint = brute.nearest(randomPoint);
        randomPoint.drawTo(nearestPoint);
        StdOut.println("The point nearest to " + randomPoint.toString() + " is " + nearestPoint
                .toString());
    }
}
