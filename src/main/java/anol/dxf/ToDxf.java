package anol.dxf;

import java.io.BufferedWriter;
import java.io.IOException;

import static java.lang.Math.*;
import static java.lang.StrictMath.sin;

public class ToDxf {

    private BufferedWriter writer;
    DxfHeaderSection dxfHeaderSection;
    DxfEntitiesSection dxfEntitiesSection;

    public ToDxf(BufferedWriter writer) throws IOException {
        this.writer = writer;
        dxfHeaderSection = new DxfHeaderSectionAC1009(writer);
        dxfEntitiesSection = new DxfEntitiesSection(writer);
        //
        dxfHeaderSection.printSection();
    }

    public void prolog() throws Exception {
        dxfEntitiesSection.prolog();
    }

    public void epilog() throws IOException {
        dxfEntitiesSection.epilog();
    }

    public void open(String name, int layer) throws IOException {
        dxfEntitiesSection.open(name, layer);
    }

    public void moveTo(double dblX, double dblY) throws IOException {
        dxfEntitiesSection.moveTo(dblX, dblY);
    }

    public void lineTo(double dblX, double dblY) throws IOException {
        dxfEntitiesSection.lineTo(dblX, dblY);
    }

    public void bulgeTo(double bulge, double dblX, double dblY) throws IOException {
        dxfEntitiesSection.bulgeTo(bulge, dblX, dblY);
    }

    public void quadTo(double dblX, double dblY, double dblX2, double dblY2) throws IOException {
        dxfEntitiesSection.quadTo(dblX, dblY, dblX2, dblY2);
    }

    public void roundTo(double dblX, double dblY, double dblX2, double dblY2, double dblX3, double dblY3, double dblX4, double dblY4) throws IOException {
        dxfEntitiesSection.roundTo(dblX, dblY, dblX2, dblY2, dblX3, dblY3, dblX4, dblY4);
    }

    public void close() throws IOException {
        dxfEntitiesSection.close();
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
