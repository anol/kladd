package anol.dxf;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import static java.lang.Math.*;
import static java.lang.StrictMath.sin;

public class ToDxf {

    private BufferedWriter writer;

    public ToDxf(BufferedWriter writer) {
        this.writer = writer;
    }

    public void prolog() throws IOException {
        int layer = 0;
        String name = "P";
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

    public void moveTo(double coord, double coord1) {
    }

    public void lineTo(double coord, double coord1) {
    }

    public void quadTo(double coord, double coord1, double coord2, double coord3) {
    }

    public void cubicTo(double coord, double coord1, double coord2, double coord3, double coord4, double coord5) {
    }

    public void close() {
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
        //
        int layer = 0;
        String name = "P";
        //
        Double dblZ = 0.0;
        Double dblPI = atan(1) * 4;
        Double dblA1 = (2 * dblPI) / iSides;
        Double dblA = dblPI / 4;
        Double bulge = tan(dblA1 / 4);
        dblA1 = dblA1 / 2;
        Double radius = dblLen / 10;
        Double length = dblLen - 2 * radius;
        dblX -= radius;
        // Polyline Entity
        print(0, "POLYLINE");
        print(5, name);
        printint(8, layer);
        printint(66, 1);
        printint(39, 3); // Thickness (optional; default = 0)
        printint(70, 1); // 70 = Polyline flag (bit-coded); default is 0:
        // 1 = This is a closed polyline (or a polygon mesh closed in the M direction).
        print(10, 0.0);
        print(20, 0.0);
        print(30, 0.0);
        // Vertex Sub-entities
        for (int i = 0; i < iSides; i++) {
            print(0, "VERTEX");
            //       print(5, name + i);
            print(42, bulge);
            printint(8, layer);
            print(10, dblX);
            print(20, dblY);
            print(30, dblZ);
            dblX = radius * cos(dblA) + dblX;
            dblY = radius * sin(dblA) + dblY;
            dblA = dblA + dblA1;
            print(0, "VERTEX");
            //       print(5, name + i);
            printint(8, layer);
            print(10, dblX);
            print(20, dblY);
            print(30, dblZ);
            dblX = length * cos(dblA) + dblX;
            dblY = length * sin(dblA) + dblY;
            dblA = dblA + dblA1;
        }
        // End of Sequence
        print(0, "SEQEND");
        print(5, "End_" + name);
        printint(8, layer);
    }

}
