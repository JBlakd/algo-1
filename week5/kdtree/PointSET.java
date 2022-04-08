/* *****************************************************************************
 *  Name: Ivan Hu
 *  Date: 08/04/2022
 *  Description: Algos 1 kdtrees assignment
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class PointSET {
    SET<Point2D> ps;

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
            StdDraw.point(p.x(), p.y());
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

    }
}
