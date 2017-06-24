package anol.dxf;

import java.io.BufferedWriter;
import java.io.IOException;

public class DxfHeaderSectionAC1009 extends DxfHeaderSection{

    public DxfHeaderSectionAC1009(BufferedWriter writer) {
        super(writer);
    }

    public void printSection() throws IOException {
        printVariable(0, "SECTION");
        printVariable(2, "HEADER");
        printVariable(9, "$ACADVER", 1, "AC1009");
        printVariable(0, "ENDSEC");
    }
}
