package anol.step;


import anol.converter.ConcretePart;

import java.awt.geom.PathIterator;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static java.awt.geom.PathIterator.*;
import static java.awt.geom.PathIterator.SEG_CLOSE;

public class ToStep {

    final String fileSchema = "EXPLICIT_DRAUGHTING";
    private int nextEntityId = 10;

    public String getProlog() {
        return "ISO-10303-21;\n";
    }

    public String getEpilog() {
        return "END-ISO-10303-21;\n";
    }

    private String getTimeStamp() {
        // "'2016-11-29T15:18:00+02:00',"
        return "'" + ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT) + "'";
    }

    /*
     * Get the header section
     * @param title The document title
     * @param inputFile The input file name
     * @param outputFile The output file name
     * @return The Header Section as a string
     */
    public String getHeaderSection(String title, String inputFile, String outputFile) {
        String header = "HEADER;\n";
        header += "FILE_DESCRIPTION(('" + title + "'), '2;1');\n";
        header += "FILE_NAME('" + outputFile + "', " + getTimeStamp() +
                ", ('Anders Emil Olsen'), ('NMDF'), 'KLADD-V1', '" + inputFile + "', 'aeolsen@gmail.com');\n";
        header += "FILE_SCHEMA(('" + fileSchema + "'));\n";
        header += "ENDSEC;\n";
        return header;
    }

    public String getDataSection(ConcretePart part, int id) {
        String section = "\nDATA ('DEL_" + id + "', ('" + fileSchema + "'));\n";
        section += convertArea(part, id);
        section += "ENDSEC;\n";
        return section;
    }

    private String getOldId(int age) {
        return "#" + (nextEntityId - age - 1);
    }

    private String addEntity(String entity) {
        String line = "#" + nextEntityId + "=";
        nextEntityId++;
        line += entity + "\n";
        return line;
    }

    private String getDataSectionHeader(ConcretePart part, int id) {
        String outputString = addEntity("PRODUCT('DEL_" + id + "','del','" + part.getName() + "',$);");
        outputString += addEntity("(LENGTH_UNIT(),NAMED_UNIT(*),SI_UNIT(.MILLI.,.METRE.));");
        outputString += "/* Geometry */\n";
        return outputString;
    }

    private String convertArea(ConcretePart part, int id) {
        String outputString = getDataSectionHeader(part, id);
        int skip = 0;
        for (PathIterator pi = part.getPathIterator(); !pi.isDone(); pi.next()) {
            double[] coo = new double[6];
            int type = pi.currentSegment(coo);
            switch (type) {
                case SEG_MOVETO: // 1 point
                    outputString += addEntity("CARTESIAN_POINT('',(0.," + (-coo[0]) + "," + (coo[1]) + ");");
                    skip = 0;
                    break;
                case SEG_LINETO: // 1 point
                    outputString += addEntity("CARTESIAN_POINT('',(0.," + (-coo[0]) + "," + (coo[1]) + ");");
                    outputString += addEntity("LINE(''," + getOldId(2 + skip) + "," + getOldId(1) + ");");
                    skip = 1;
                    break;
                case SEG_QUADTO: // 2 point
                    outputString += "/* SEG_QUADTO */\n";
                    skip = 0;
                    break;
                case SEG_CUBICTO: // 3 points
                    outputString += addEntity("CARTESIAN_POINT('',(0.," + (-coo[0]) + "," + (coo[1]) + ");");
                    outputString += addEntity("CARTESIAN_POINT('',(0.," + (-coo[2]) + "," + (coo[3]) + ");");
                    outputString += addEntity("CARTESIAN_POINT('',(0.," + (-coo[4]) + "," + (coo[5]) + ");n");
                    outputString += addEntity("CURVE('',"
                            + getOldId(4 + skip) + "," + getOldId(3) + ","
                            + getOldId(2) + "," + getOldId(1) + ");");
                    skip = 1;
                    break;
                case SEG_CLOSE: // 0 points
                    outputString += "/* SEQ_CLOSE */\n";
                    skip = 0;
                    break;
                default:
                    skip = 0;
                    break;
            }
        }
        return outputString;
    }

}
