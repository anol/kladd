package anol.dxf;

import java.io.BufferedWriter;
import java.io.IOException;

import static java.lang.Math.abs;
import static java.lang.Math.floorDiv;


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
        // printIntVariable(39, 3);
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

    public void semicircleTo(double dblX, double dblY, double dblX2, double dblY2, double dblX3, double dblY3, double dblX4, double dblY4) throws IOException {
        double dblDX = dblX - dblX4;
        double dblDY = dblY4 - dblY;
        if (0.1 > abs(dblDX)) {
            System.out.print("  X--> ");
            lineTo(dblX4, dblY4);
        } else if (0.1 > abs(dblDY)) {
            System.out.print("  Y--> ");
            lineTo(dblX4, dblY4);
        } else {
            System.out.print("  2~~> ");
            lineTo(dblX4, dblY4);
        }
    }

    public void arcTo(double dblX, double dblDX, double dblY, double dblDY) throws IOException {
        int segments = new Double(abs(dblDX) / 2).intValue();
        if (100 < segments) {
            lineTo(dblX + (dblDX * 0.92), dblY + (dblDY * 0.38));
            lineTo(dblX + (dblDX * 0.71), dblY + (dblDY * 0.71));
            lineTo(dblX + (dblDX * 0.38), dblY + (dblDY * 0.92));
        } else {
            lineTo(dblX + (dblDX * 0.71), dblY + (dblDY * 0.71));
        }
    }

    public void quartcircleTo(int octant, double dblX, double dblY, double dblX4, double dblY4) throws IOException {
        switch (octant) {
            case 1:
                arcTo(dblX4, dblX - dblX4, dblY, dblY4 - dblY);
                break;
            case 2:
                arcTo(dblX, dblX4 - dblX, dblY4, dblY - dblY4);
                break;
            case 3:
                arcTo(dblX, dblX4 - dblX, dblY4, dblY - dblY4);
                break;
            case 4:
                arcTo(dblX4, dblX - dblX4, dblY, dblY4 - dblY);
                break;
            case 5:
                arcTo(dblX, dblX4 - dblX, dblY4, dblY - dblY4);
                break;
            case 6:
                arcTo(dblX4, dblX - dblX4, dblY, dblY4 - dblY);
                break;
            case 7:
                arcTo(dblX4, dblX - dblX4, dblY, dblY4 - dblY);
                break;
            case 8:
                arcTo(dblX, dblX4 - dblX, dblY4, dblY - dblY4);
                break;
            default:
                System.out.print("  1~~> ");
                break;
        }
        lineTo(dblX4, dblY4);
    }

    private boolean near(double a, double b) {
        return 0.01 > abs(a - b);
    }

    private int getOctant(double dblX, double dblY, double dblX2, double dblY2, double dblX3, double dblY3, double dblX4, double dblY4) {
        double dblDX = dblX - dblX4;
        double dblDY = dblY4 - dblY;
        int octant = 0;
        boolean reverse = false;
        if (1 < dblDX && 1 < dblDY) {
            octant = 1;
            if ((near(dblX, dblX2)) && (near(dblY, dblY2))) {
                reverse = false; // 0
            } else if ((near(dblX, dblX2)) && (dblY < dblY2)) {
                reverse = false; // 64
            } else if ((dblX < dblX2) && (dblY == dblY2)) {
                reverse = false; // 0
            } else if ((dblX > dblX2) && (dblY == dblY2)) {
                reverse = false; // <<<< 2
            }
        } else if (-1 > dblDX && 1 < dblDY) {
            octant = 2;
            if ((near(dblX, dblX2)) && (near(dblY, dblY2))) {
                reverse = false; // 0
            } else if ((near(dblX, dblX2)) && (dblY < dblY2)) {
                reverse = true; // <<<< 2
            } else if ((dblX < dblX2) && (dblY == dblY2)) {
                reverse = false; // 62
            } else if ((dblX > dblX2) && (dblY == dblY2)) {
                reverse = false; // 0
            }
        } else if (1 < dblDX && -1 > dblDY) {
            octant = 3;
            if ((near(dblX, dblX2)) && (near(dblY, dblY2))) {
                reverse = true; // <<<< 1
            } else if ((near(dblX, dblX2)) && (dblY < dblY2)) {
                reverse = false; // 0
            } else if ((dblX < dblX2) && (dblY == dblY2)) {
                reverse = false; // 0
            } else if ((dblX > dblX2) && (dblY == dblY2)) {
                reverse = false; // 63
            }
        } else if (-1 > dblDX && -1 > dblDY) {
            octant = 4;
            if ((near(dblX, dblX2)) && (near(dblY, dblY2))) {
                reverse = false; // 62
            } else if ((near(dblX, dblX2)) && (dblY < dblY2)) {
                reverse = false; // 0
            } else if ((dblX < dblX2) && (dblY == dblY2)) {
                reverse = true; // <<<< 2
            } else if ((dblX > dblX2) && (dblY == dblY2)) {
                reverse = false; // 0
            }
        }
        if (0 < octant && reverse) {
            System.out.println("vvvvvvvvvvv");
            octant += 4;
        }
        return octant;
    }

    public void roundTo(double dblX, double dblY, double dblX2, double dblY2, double dblX3, double dblY3, double dblX4, double dblY4) throws IOException {
        double dblDX = dblX - dblX4;
        double dblDY = dblY4 - dblY;
        int octant = getOctant(dblX, dblY, dblX2, dblY2, dblX3, dblY3, dblX4, dblY4);
        System.out.println("roundTo: " + octant + "," + dblX + ", " + dblY + ", " + dblX2 + ", " + dblY2 + ", " + dblX3 + ", " + dblY3 + ", " + dblX4 + ", " + dblY4 + ", dx=" + dblDX + ", dy=" + dblDY);
        if ((1.1 > abs(dblDX)) && (1.1 > abs(dblDY))) {
            System.out.print("  0--> ");
            lineTo(dblX4, dblY4);
        } else if ((0.1 > abs(dblDX)) || (0.1 > abs(dblDY))) {
            semicircleTo(dblX, dblY, dblX2, dblY2, dblX3, dblY3, dblX4, dblY4);
        } else {
            quartcircleTo(octant, dblX, dblY, dblX4, dblY4);
        }
    }

    public void close() throws IOException {
        // End of Sequence
        printVariable(0, "SEQEND");
    }

}
