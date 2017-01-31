package anol.dxf;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import static java.lang.Math.*;
import static java.lang.StrictMath.sin;

public class ToDxf {

    private static void print(BufferedWriter writer, int code, String data) throws IOException {
        writer.write(code + "\n" + data + "\n");
    }

    private static void print(BufferedWriter writer, int code, int number) throws IOException {
        writer.write(code + "\n" + number + "\n");
    }

    private static void print(BufferedWriter writer, int code, double number) throws IOException {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("0.0", symbols);
        format.setMaximumFractionDigits(6);
        writer.write(code + "\n" + format.format(number) + "\n");
    }

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
    public static void WriteDXFPolygon(BufferedWriter writer, Integer iSides, Double dblX, Double dblY, Double dblLen) throws IOException {
        Double dblZ = 0.0;
        Double dblPI = atan(1) * 4;
        Double dblA1 = (2 * dblPI) / iSides;
        Double dblA = dblPI / 4;
        Double bulge = tan(dblA1 / 4);
        dblA1 = dblA1 / 2;
        int layer = 0;
        int extraVertexCreatedByCurveFitting = 1 + 3 + 8;
        Double radius = dblLen / 10;
        Double length = dblLen - 2 * radius;
        dblX -= radius;
        String name = "P";
        // Header Section
        print(writer, 0, "SECTION");
        print(writer, 2, "HEADER");
        print(writer, 9, "$ACADVER");
        print(writer, 1, "AC1009");
        print(writer, 0, "ENDSEC");
        // Entities Section
        print(writer, 0, "SECTION");
        print(writer, 2, "ENTITIES");
        // Polyline Entity
        print(writer, 0, "POLYLINE");
        print(writer, 5, name);
        print(writer, 8, layer);
        print(writer, 66, 1);
        print(writer, 70, 1);
        print(writer, 10, 0.0);
        print(writer, 20, 0.0);
        print(writer, 30, 0.0);
        // Vertex Sub-entities
        for (int i = 0; i < iSides; i++) {
            print(writer, 0, "VERTEX");
            //       print(writer, 5, name + i);
            print(writer, 42, bulge);
            print(writer, 8, layer);
            print(writer, 10, dblX);
            print(writer, 20, dblY);
            print(writer, 30, dblZ);
            dblX = radius * cos(dblA) + dblX;
            dblY = radius * sin(dblA) + dblY;
            dblA = dblA + dblA1;
            print(writer, 0, "VERTEX");
            //       print(writer, 5, name + i);
            print(writer, 8, layer);
            print(writer, 10, dblX);
            print(writer, 20, dblY);
            print(writer, 30, dblZ);
            dblX = length * cos(dblA) + dblX;
            dblY = length * sin(dblA) + dblY;
            dblA = dblA + dblA1;
        }
        // End of Sequence
        print(writer, 0, "SEQEND");
        print(writer, 5, "End_" + name);
        print(writer, 8, layer);
        // End of Section
        print(writer, 0, "ENDSEC");
        // End of File
        print(writer, 0, "EOF");
    }
}
