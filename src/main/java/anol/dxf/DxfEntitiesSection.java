package anol.dxf;

import java.io.BufferedWriter;
import java.io.IOException;


public class DxfEntitiesSection extends DxfSection {

    private int layer = 0;
    private String name = "P";

    public DxfEntitiesSection(BufferedWriter writer) {
        super(writer);
    }

    public void printSection() {
    }


    public void prolog() throws Exception {
        printVariable(0, "SECTION");
        printVariable(2, "ENTITIES");
    }

    public void epilog() throws IOException {
        printVariable(0, "ENDSEC");
        printVariable(0, "EOF");
    }

    public void open(String name, int layer) throws IOException {
        this.name = name;
        this.layer = layer;
        // Polyline Entity
        printVariable(0, "POLYLINE");
        printVariable(5, name);
        printIntVariable(8, layer);
        printIntVariable(66, 1);
        // 39 = Thickness (optional; default = 0)
        printIntVariable(39, 3);
        // 70 = Polyline flag (bit-coded); default is 0:
        // 1 = This is a closed polyline (or a polygon mesh closed in the M direction).
        // 2 = Curve-fit vertices have been added
        // 4 = Spline-fit vertices have been added
        printIntVariable(70, 1);
        printVariable(10, 0.0, 20, 0.0, 30, 0.0);
    }

    public void moveTo(double dblX, double dblY) throws IOException {
        System.out.println("moveTo: " + dblX + ", " + dblY);
        printVariable(0, "VERTEX");
        printIntVariable(8, layer);
        printVariable(10, dblX, 20, dblY, 30, 0.0);
    }

    public void lineTo(double dblX, double dblY) throws IOException {
        System.out.println("lineTo: " + dblX + ", " + dblY);
        printVariable(0, "VERTEX");
        printIntVariable(8, layer);
        printVariable(10, dblX, 20, dblY, 30, 0.0);
    }

    public void bulgeTo(double bulge, double dblX, double dblY) throws IOException {
        printVariable(0, "VERTEX");
        printIntVariable(8, layer);
        // 42 = Bulge (optional; default is 0).
        // The bulge is the tangent of one fourth the included angle for an arc segment,
        // made negative if the arc goes clockwise from the start point to the endpoint.
        // A bulge of 0 indicates a straight segment, and a bulge of 1 is a semicircle.
        printVariable(42, bulge);
        printVariable(10, dblX, 20, dblY, 30, 0.0);
    }

    public void quadTo(double dblX, double dblY, double dblX2, double dblY2) throws IOException {
        System.out.println("quadTo: " + dblX + ", " + dblY + ", " + dblX2 + ", " + dblY2);
        printVariable(0, "VERTEX");
        printIntVariable(8, layer);
        printIntVariable(70, 1);
        printVariable(10, dblX, 20, dblY, 30, 0.0);
        printVariable(0, "VERTEX");
        printIntVariable(8, layer);
        printVariable(10, dblX2, 20, dblY2, 30, 0.0);
    }

    public void cubicTo(double dblX, double dblY, double dblX2, double dblY2, double dblX3, double dblY3) throws IOException {
        // 70 = Vertex flags(optional; default = 0)
        //   1 = Extra vertex created by curve-fitting
        //   2 = Curve-fit tangent defined for this vertex.
        //   A curve-fit tangent direction of 0 may be omitted from the DXF output, but is significant if this bit is set
        //   4 = Unused (never set in DXF files)
        //   8 = Spline vertex created by spline-fitting
        //  16 = Spline frame control point
        //  32 = 3D Polyline vertex
        //  64 = 3D polygon mesh vertex
        // 128 = Polyface mesh vertex
        System.out.println("cubicTo: " + dblX + ", " + dblY + ", " + dblX2 + ", " + dblY2 + ", " + dblX3 + ", " + dblY3);
        printVariable(0, "VERTEX");
        printIntVariable(8, layer);
        printIntVariable(70, 0);
        printVariable(10, dblX, 20, dblY, 30, 0.0);
        printVariable(0, "VERTEX");
        printIntVariable(8, layer);
        printIntVariable(70, 0);
        printVariable(10, dblX2, 20, dblY2, 30, 0.0);
        printVariable(0, "VERTEX");
        printIntVariable(8, layer);
        printVariable(10, dblX3, 20, dblY3, 30, 0.0);
    }

    public void splineTo(double dblX, double dblY, double dblX2, double dblY2, double dblX3, double dblY3, double dblX4, double dblY4) throws IOException {
        System.out.println("splineTo: " + dblX + ", " + dblY + ", " + dblX2 + ", " + dblY2 + ", " + dblX3 + ", " + dblY3 + ", " + dblX4 + ", " + dblY4);
        printVariable(0, "SPLINE");
        printVariable(5, "38");
        printIntVariable(8, layer);
        printVariable(210, 0.0, 220, 0.0, 230, 1.0);
        // 70 = Spline flag (bit coded):
        // 1 = Closed spline
        // 2 = Periodic spline
        // 4 = Rational spline
        // 8 = Planar
        // 16 = Linear (planar bit is also set)
        printIntVariable(70, 9);
        printIntVariable(71, 3); // 71 = Degree of the spline curve
        printIntVariable(72, 8); // 72 = Number of knots
        printIntVariable(73, 4); // 73 = Number of control points
        printIntVariable(74, 0); // 74 = Number of fit points
        printVariable(42, 0.000000001); // 42 = Knot tolerance (default = 0.0000001)
        printVariable(43, 0.001); // 43 = Control-point tolerance (default = 0.0000001)
        printVariable(40, 0.0); // 40 = Knot value (one entry per knot)
        printVariable(40, 0.0);
        printVariable(40, 0.0);
        printVariable(40, 0.0);
        printVariable(40, 1.0); // 40 = Knot value (one entry per knot)
        printVariable(40, 1.0);
        printVariable(40, 1.0);
        printVariable(40, 1.0);
        printVariable(10, dblX, 20, dblY, 30, 0.0);
        printVariable(10, dblX2, 20, dblY2, 30, 0.0);
        printVariable(10, dblX3, 20, dblY3, 30, 0.0);
        printVariable(10, dblX4, 20, dblY, 30, 0.0);
    }

    public void close() throws IOException {
        // End of Sequence
        printVariable(0, "SEQEND");
    }

}
