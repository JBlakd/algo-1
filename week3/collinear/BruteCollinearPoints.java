/* *****************************************************************************
 *  Name: Ivan Hu
 *  Date: 27/03/2022
 *  Description: Algos 1 course Collinear assignment
 ******************************************************************************/

import edu.princeton.cs.algs4.ResizingArrayStack;

public class BruteCollinearPoints {
    private class LineSegmentSingleRecord {
        private final Point endPoint;
        private final double slope;

        private LineSegmentSingleRecord(Point endPoint, double slope) {
            this.endPoint = endPoint;
            this.slope = slope;
        }
    }

    private final ResizingArrayStack<LineSegment> segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        for (Point pt : points) {
            if (pt == null) {
                throw new IllegalArgumentException();
            }
        }

        segments = new ResizingArrayStack<LineSegment>();
        ResizingArrayStack<LineSegmentSingleRecord> lineSegmentRecord =
                new ResizingArrayStack<LineSegmentSingleRecord>();

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
            for (int j = 0; j < points.length; j++) {
                if (i == j) {
                    continue;
                }
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }

                // Establish slope. Bigger nest must be invoking point
                double slope = points[i].slopeTo(points[j]);
                // Establish direction. Bigger nest must be invoking point
                int direction = points[i].compareTo(points[j]);

                // Check this line's starting point is not already the endpoint
                // of an established lineSegment
                boolean lineAlreadyExists = false;
                for (LineSegmentSingleRecord lssr : lineSegmentRecord) {
                    if (points[i] == lssr.endPoint && Double.compare(lssr.slope, slope) == 0) {
                        lineAlreadyExists = true;
                    }
                }
                if (lineAlreadyExists) {
                    continue;
                }

                for (int k = 0; k < points.length; k++) {
                    // Check not same point
                    if (i == k || j == k) {
                        continue;
                    }

                    // Check direction
                    if (!((points[j].compareTo(points[k]) > 0 && direction > 0) || (
                            points[j].compareTo(points[k]) < 0 && direction < 0))) {
                        continue;
                    }

                    // Check slope
                    if (points[j].slopeTo(points[k]) != slope) {
                        continue;
                    }

                    for (int m = 0; m < points.length; m++) {
                        // Check not same point
                        if (i == m || j == m || k == m) {
                            continue;
                        }

                        // Check direction
                        if (!((points[k].compareTo(points[m]) > 0 && direction > 0) || (
                                points[k].compareTo(points[m]) < 0 && direction < 0))) {
                            continue;
                        }

                        // Check slope
                        if (points[k].slopeTo(points[m]) != slope) {
                            continue;
                        }

                        segments.push(new LineSegment(points[i], points[m]));
                        lineSegmentRecord.push(new LineSegmentSingleRecord(points[m], slope));
                    }
                }
            }
        }
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
