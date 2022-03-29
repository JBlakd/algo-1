/* *****************************************************************************
 *  Name: Ivan Hu
 *  Date: 28/03/2022
 *  Description: Algos 1 course Collinear assignment
 ******************************************************************************/

import edu.princeton.cs.algs4.ResizingArrayStack;

import java.util.Comparator;

public class FastCollinearPoints {
    private class LineSegmentSingleRecord {
        private Point endPoint;
        private double slope;

        private LineSegmentSingleRecord(Point endPoint, double slope) {
            this.endPoint = endPoint;
            this.slope = slope;
        }
    }

    private ResizingArrayStack<LineSegment> segments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        segments = new ResizingArrayStack<LineSegment>();
        ResizingArrayStack<LineSegmentSingleRecord> lineSegmentRecord =
                new ResizingArrayStack<LineSegmentSingleRecord>();

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
            double previousPointSlope = points[i].slopeTo(otherPoints[0]);
            int consecutive = 1;
            for (int j = 1; j < otherPoints.length; j++) {
                Double thisPointSlope = points[i].slopeTo(otherPoints[j]);
                if (thisPointSlope.equals(previousPointSlope)) {
                    consecutive++;
                }
                else {
                    // it's come to a new slope

                    // Blacklist nonEndPoints
                    for (int k = j - 2; k > (j - consecutive); k--) {
                        lineSegmentRecord.push(new LineSegmentSingleRecord(otherPoints[k],
                                                                           previousPointSlope));
                    }

                    if (consecutive >= 3) {
                        // Check if not in blacklist
                        for (LineSegmentSingleRecord lssr : lineSegmentRecord) {
                            if (points[i].compareTo(lssr.endPoint) == 0
                                    && previousPointSlope == lssr.slope) {
                                return;
                            }
                            if (otherPoints[j - 1].compareTo(lssr.endPoint) == 0
                                    && previousPointSlope == lssr.slope) {
                                return;
                            }
                        }

                        segments.push(new LineSegment(points[i], otherPoints[j - 1]));
                    }
                    consecutive = 1;
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
