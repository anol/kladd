package anol.dxf;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class ToDxfTest {

    @Test
    void writeDXFPolygon() {
        // Using nio.file
        Path path = Paths.get("output.dxf");
        Charset charset = Charset.forName("US-ASCII");
        try (BufferedWriter writer = Files.newBufferedWriter(path, charset)) {
            Integer iSides = 4;
            Double dblX = 0.0;
            Double dblY = 0.0;
            Double dblLen = 100.0;
            anol.dxf.ToDxf.WriteDXFPolygon(writer, iSides, dblX, dblY, dblLen);
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }

}