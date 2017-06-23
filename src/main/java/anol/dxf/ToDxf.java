package anol.dxf;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import static java.lang.Math.*;
import static java.lang.StrictMath.sin;

public class ToDxf {

    private int layer = 0;
    private String name = "P";

    private BufferedWriter writer;

    public ToDxf(BufferedWriter writer) {
        this.writer = writer;
    }

    public void prolog() throws IOException {
        // Header Section
        print(0, "SECTION");
        print(2, "HEADER");
        print(9, "$ACADVER");
        print(1, "AC1009");
        print(0, "ENDSEC");
        // Entities Section
        print(0, "SECTION");
        print(2, "ENTITIES");
    }

    public void epilog() throws IOException {
        // End of Section
        print(0, "ENDSEC");
        // End of File
        print(0, "EOF");
    }

    public void open(String name, int layer) throws IOException {
        this.name = name;
        this.layer = layer;
        // Polyline Entity
        print(0, "POLYLINE");
        print(5, name);
        printint(8, layer);
        printint(66, 1);
        // 39 = Thickness (optional; default = 0)
        printint(39, 3);
        // 70 = Polyline flag (bit-coded); default is 0:
        // 1 = This is a closed polyline (or a polygon mesh closed in the M direction).
        // 2 = Curve-fit vertices have been added
        // 4 = Spline-fit vertices have been added
        printint(70, 1);
        print(10, 0.0);
        print(20, 0.0);
        print(30, 0.0);
    }

    public void moveTo(double dblX, double dblY) throws IOException {
        System.out.println("moveTo: " + dblX + ", " + dblY);
        double dblZ = 0.0;
        print(0, "VERTEX");
        printint(8, layer);
        print(10, dblX);
        print(20, dblY);
        print(30, dblZ);
    }

    public void lineTo(double dblX, double dblY) throws IOException {
        System.out.println("lineTo: " + dblX + ", " + dblY);
        double dblZ = 0.0;
        print(0, "VERTEX");
        printint(8, layer);
        print(10, dblX);
        print(20, dblY);
        print(30, dblZ);
    }

    public void bulgeTo(double bulge, double dblX, double dblY) throws IOException {
        double dblZ = 0.0;
        print(0, "VERTEX");
        printint(8, layer);
        // 42 = Bulge (optional; default is 0).
        // The bulge is the tangent of one fourth the included angle for an arc segment,
        // made negative if the arc goes clockwise from the start point to the endpoint.
        // A bulge of 0 indicates a straight segment, and a bulge of 1 is a semicircle.
        print(42, bulge);
        print(10, dblX);
        print(20, dblY);
        print(30, dblZ);
    }

    public void quadTo(double dblX, double dblY, double dblX2, double dblY2) throws IOException {
        System.out.println("quadTo: " + dblX + ", " + dblY + ", " + dblX2 + ", " + dblY2);
        double dblZ = 0.0;
        print(0, "VERTEX");
        printint(8, layer);
        printint(70, 1);
        print(10, dblX);
        print(20, dblY);
        print(30, dblZ);
        print(0, "VERTEX");
        printint(8, layer);
        print(10, dblX2);
        print(20, dblY2);
        print(30, dblZ);
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
        double dblZ = 0.0;
        print(0, "VERTEX");
        printint(8, layer);
        printint(70, 0);
        print(10, dblX);
        print(20, dblY);
        print(30, dblZ);
        print(0, "VERTEX");
        printint(8, layer);
        printint(70, 0);
        print(10, dblX2);
        print(20, dblY2);
        print(30, dblZ);
        print(0, "VERTEX");
        printint(8, layer);
        print(10, dblX3);
        print(20, dblY3);
        print(30, dblZ);
    }

    public void close() throws IOException {
        // End of Sequence
        print(0, "SEQEND");
        print(5, "End_" + name);
        printint(8, layer);
    }

    private void print(int code, String data) throws IOException {
        this.writer.write(code + "\n" + data + "\n");
    }

    private void printint(int code, int number) throws IOException {
        this.writer.write(code + "\n" + number + "\n");
    }

    private void print(int code, double number) throws IOException {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("0.0", symbols);
        format.setMaximumFractionDigits(6);
        this.writer.write(code + "\n" + format.format(number) + "\n");
    }

    // See: https://www.autodesk.com/techpubs/autocad/acad2000/dxf/polyline_dxf_06.htm

    /*
    WriteDXFPolygon creates a minimal DXF file that only contains
    the ENTITIES section. This subroutine requires five parameters,
    the DXF file name, the number of sides for the polygon, the X
    and Y coordinates for the bottom end of the right-most side
    (it starts in a vertical direction), and the length for each
    side. Note that because this only requests 2D points, it does
    not include the Z coordinates (codes 30 and 31). The lines are
    placed on the layer "Polygon."
    */
    public void polygon(Integer iSides, Double dblX, Double dblY, Double dblLen) throws IOException {
        Double dblZ = 0.0;
        Double dblPI = atan(1) * 4;
        Double dblA1 = (2 * dblPI) / iSides;
        Double dblA = dblPI / 4;
        Double bulge = tan(dblA1 / 4);
        dblA1 = dblA1 / 2;
        Double radius = dblLen / 10;
        Double length = dblLen - 2 * radius;
        dblX -= radius;
        moveTo(0.0, 0.0);
        // Vertex Sub-entities
        for (int i = 0; i < iSides; i++) {
            bulgeTo(bulge, dblX, dblY);
            dblX = radius * cos(dblA) + dblX;
            dblY = radius * sin(dblA) + dblY;
            dblA = dblA + dblA1;
            lineTo(dblX, dblY);
            dblX = length * cos(dblA) + dblX;
            dblY = length * sin(dblA) + dblY;
            dblA = dblA + dblA1;
        }
    }
}
