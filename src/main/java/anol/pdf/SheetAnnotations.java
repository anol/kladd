package anol.pdf;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class SheetAnnotations {

    private static final Map<String, double[]> pageSizes = new HashMap<String, double[]>() {{
        put("a0", new double[]{2384, 3370, 36, 220, 720, 36});
        put("a1", new double[]{1684, 2384, 36, 160, 580, 24});
        put("a2", new double[]{1190, 1684, 36, 128, 440, 18});
        put("a3", new double[]{842, 1190, 36, 100, 288, 18});
        put("a4", new double[]{595, 842, 18, 72, 144, 12});
    }};

    Annotations annotations;

    public SheetAnnotations(Annotations annotations) {
        this.annotations = annotations;
    }

    private String drawFrame(double[] pageSize) {
        double h = pageSize[0];
        double w = pageSize[1];
        double m = pageSize[2];
        double h2 = pageSize[3];
        double w2 = pageSize[4];
        String outputString = "newpath\n";
        outputString += (m) + " " + (-m) + " moveto\n";
        outputString += (m) + " " + (m - h) + " lineto\n";
        outputString += (w - m) + " " + (m - h) + " lineto\n";
        outputString += (w - m) + " " + (-m) + " lineto\n";
        outputString += "closepath stroke\n";
        outputString += "newpath\n";
        outputString += (w - m - w2) + " " + (m - h) + " moveto\n";
        outputString += (w - m - w2) + " " + (m - h + h2) + " lineto\n";
        outputString += (w - m) + " " + (m - h + h2) + " lineto\n";
        outputString += "stroke\n";
        return outputString;
    }

    private String drawText(String[] text, int lines, double[] pageSize) {
        double h = pageSize[0];
        double w = pageSize[1];
        double m = pageSize[2];
        double h2 = pageSize[3];
        double w2 = pageSize[4];
        int fontSize = ((int) pageSizes.get(annotations.getPageSize())[5]);
        double x = (w - m - w2 + fontSize);
        double y = (m - h + h2 - (1.3 * fontSize));
        String outputString = "";
        outputString += "/Helvetica-Bold findfont " + fontSize + " scalefont setfont\n";
        outputString += "newpath\n";
        for (int line = 0; line < lines; line++) {
            outputString += (x) + " " + (y) + " moveto\n";
            outputString += "(" + text[line] + ") show\n";
            y -= (1.3 * fontSize);
        }
        outputString += "stroke\n";
        return outputString;
    }

    private String getTimeAndPage( int pageNumber, int numberOfPages) {
        String date = ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        return "NMDF/" + date + "/Aeo         Ark  " + (pageNumber + 1);// + " / " + numberOfPages;
    }

    public String print(String title, String sheet1st, String sheet2nd, int pageNumber) {
        String outputString = "0.25 setlinewidth 1 setlinecap [] 0 setdash\n";
        if (annotations.isColors()) {
            outputString += "1 0.2 0.2 setrgbcolor\n";
        } else {
            outputString += "0 0 0 setrgbcolor\n";
        }
        System.out.println("Page size = " + annotations.getPageSize());
        outputString += drawFrame(pageSizes.get(annotations.getPageSize()));
        String[] text = {
                title,
                sheet1st,
                sheet2nd,
                getTimeAndPage(pageNumber, annotations.getNumberOfPages())};
        outputString += drawText(text, 4, pageSizes.get(annotations.getPageSize()));
        outputString += "0 0 0 setrgbcolor\n";
        return outputString;
    }
}
