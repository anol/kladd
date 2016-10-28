package anol;

import org.ghost4j.converter.PDFConverter;
import org.ghost4j.document.PSDocument;
import org.ghost4j.document.PaperSize;

import java.io.File;
import java.io.FileOutputStream;

import static org.ghost4j.converter.PDFConverter.OPTION_AUTOROTATEPAGES_ALL;

public class ToPdf {

    public void convert(String inputFileName,
                        File outputFile) throws Throwable {
        System.out.println("ps2pdf input=\"" + inputFileName + " output=\"" + outputFile.getCanonicalPath() + "\"");
        //load PostScript document
        PSDocument document = new PSDocument();
        document.load(new File(inputFileName));
        //create OutputStream
        FileOutputStream fos = new FileOutputStream(outputFile);
        //create converter
        PDFConverter converter = new PDFConverter();
        //set options
        converter.setPDFSettings(PDFConverter.OPTION_PDFSETTINGS_DEFAULT);
        converter.setPaperSize(PaperSize.A0);
        converter.setAutoRotatePages(OPTION_AUTOROTATEPAGES_ALL);
        //convert
        converter.convert(document, fos);
    }

}
