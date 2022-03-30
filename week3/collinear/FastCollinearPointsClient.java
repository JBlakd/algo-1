/* *****************************************************************************
 *  Name: Ivan Hu
 *  Date: 28/03/2022
 *  Description: Algos 1 course Collinear assignment
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPointsClient {
    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);

        // check more cases that the number of segments are ok
        // if (args[0].equals("inputivan.txt")) {
        //     // StdOut.println(args[0] + " numberOfSegments = " + collinear.numberOfSegments());
        //     if (collinear.numberOfSegments() != 3) {
        //         throw new RuntimeException(
        //                 "inputivan.txt failed number of segments. Expected 3, got " + collinear
        //                         .numberOfSegments());
        //     }
        // }
        
        StdOut.println("Number of segments: " + collinear.numberOfSegments());
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
