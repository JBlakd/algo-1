/* *****************************************************************************
 *  Name: Ivan Hu
 *  Date: 28/03/2022
 *  Description: Algos 1 course Collinear assignment
 ******************************************************************************/

import edu.princeton.cs.algs4.ResizingArrayStack;

import java.util.Comparator;

public class FastCollinearPoints {
    // private class LineSegmentSingleRecord {
    //     private Point endPoint;
    //     private double slope;
    //
    //     private LineSegmentSingleRecord(Point endPoint, double slope) {
    //         this.endPoint = endPoint;
    //         this.slope = slope;
    //     }
    // }

    private final ResizingArrayStack<LineSegment> segments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        segments = new ResizingArrayStack<LineSegment>();
        // ResizingArrayStack<LineSegmentSingleRecord> lineSegmentRecord =
        //         new ResizingArrayStack<LineSegmentSingleRecord>();

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }

            // Array of otherPoints, to be sorted according to slope to points[i]
            Point[] otherPoints = new Point[points.length - 1];

            // Populate otherPoints
            int otherPointIndex = 0;
            for (int j = 0; j < points.length; j++) {
                if (i == j) {
                    continue;
                }

                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }

                otherPoints[otherPointIndex] = points[j];
                otherPointIndex++;
            }

            Point[] aux = new Point[otherPoints.length];
            Comparator<Point> slopeOrder = points[i].slopeOrder();
            mergeSort(otherPoints, aux, 0, otherPoints.length - 1, slopeOrder);

            // For this point, print out its sorted otherPoints and the slope of each otherPoint
            // StdOut.print(points[i]);
            // StdOut.print(" => ");
            // for (Point otherPoint : otherPoints) {
            //     StdOut.print(otherPoint + ", ");
            // }
            // StdOut.println();
            // StdOut.print(points[i]);
            // StdOut.print(" => ");
            // for (Point otherPoint : otherPoints) {
            //     StdOut.print(points[i].slopeTo(otherPoint) + ", ");
            // }
            // StdOut.println();
            // StdOut.println();

            // otherPoints are sorted now, iterate through this array to obtain the linesegments
            // Since we are starting at j = 1, gotta process j = 0 first
            double previousPointSlope = points[i].slopeTo(otherPoints[0]);
            int consecutive = 1;
            Point lowestPoint;
            if (points[i].compareTo(otherPoints[0]) < 0) {
                lowestPoint = points[i];
            }
            else {
                lowestPoint = otherPoints[0];
            }
            Point highestPoint;
            if (points[i].compareTo(otherPoints[0]) > 0) {
                highestPoint = points[i];
            }
            else {
                highestPoint = otherPoints[0];
            }
            for (int j = 1; j < otherPoints.length; j++) {
                double thisPointSlope = points[i].slopeTo(otherPoints[j]);
                if (Double.compare(thisPointSlope, previousPointSlope) == 0) {
                    // otherPoints[j] still on the same slope
                    // if the current lowestPoint is bigger than otherPoints[j] then otherPoints[j] becomes the new lowestPoint
                    if (lowestPoint.compareTo(otherPoints[j]) > 0) {
                        lowestPoint = otherPoints[j];
                    }

                    // if the current highestPoint is smaller than otherPoints[j] then otherPoints[j] becomes the new highestPoint
                    if (highestPoint.compareTo(otherPoints[j]) < 0) {
                        highestPoint = otherPoints[j];
                    }
                    consecutive++;
                }
                else {
                    // now otherPoints[j] has reached the next slope
                    // we ONLY add a segment if points[i] is the lowestPoint and consecutive >= 3
                    if (consecutive >= 3 && points[i].compareTo(lowestPoint) == 0) {
                        segments.push(new LineSegment(points[i], highestPoint));
                    }
                    // reset consecutive
                    consecutive = 1;
                    // reset lowestPoint
                    if (points[i].compareTo(otherPoints[j]) < 0) {
                        lowestPoint = points[i];
                    }
                    else {
                        lowestPoint = otherPoints[j];
                    }
                    // reset highestPoint
                    if (points[i].compareTo(otherPoints[j]) > 0) {
                        highestPoint = points[i];
                    }
                    else {
                        highestPoint = otherPoints[j];
                    }
                    // reset previousPointSlope
                    previousPointSlope = points[i].slopeTo(otherPoints[j]);
                }
            }

        }

    }

    private static void merge(Point[] a, Point[] aux, int lo, int mid, int hi,
                              Comparator<Point> slopeOrder) {
        // Copy data into aux array
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }

        // Left aux tracker
        int i = lo;
        // Right aux tracker
        int j = mid + 1;
        // For every item to be populated in a[
        for (int k = lo; k <= hi; k++) {
            // If the left aux tracker is exhausted
            if (i > mid) {
                a[k] = aux[j];
                j++;
            }
            // If the right aux tracker is exhausted
            else if (j > hi) {
                a[k] = aux[i];
                i++;
            }
            // If aux[j] has a lower slopeOrder than aux[i]
            else if (slopeOrder.compare(aux[j], aux[i]) < 0) {
                a[k] = aux[j];
                j++;
            }
            // If aux[i] has a lower slopeOrder than aux[j]
            else {
                a[k] = aux[i];
                i++;
            }
        }
    }

    private static void mergeSort(Point[] a, Point[] aux, int lo, int hi,
                                  Comparator<Point> slopeOrder) {
        // Base case. When hi = lo, then there is only 1 element.
        if (hi <= lo) {
            return;
        }

        int mid = lo + (hi - lo) / 2;
        mergeSort(a, aux, lo, mid, slopeOrder);
        mergeSort(a, aux, mid + 1, hi, slopeOrder);
        // Check to save some effort:
        // already sorted if the biggest item in Left half is <= smallest item in Right half
        // if (slopeOrder.compare(a[mid], a[mid + 1]) <= 0) {
        //     return;
        // }
        merge(a, aux, lo, mid, hi, slopeOrder);
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] segmentsArray = new LineSegment[numberOfSegments()];
        int i = 0;
        for (LineSegment ls : segments) {
            segmentsArray[i] = ls;
            i++;
        }
        assert (i == numberOfSegments());

        return segmentsArray;
    }
}
