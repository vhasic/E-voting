package ba.etf.elections.client.helper;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PDFHelper implements IPDFHelper {
    public static void printToPDF(String data, String directoryPath) throws DocumentException, FileNotFoundException {
        Document document = new Document();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String pdfFileName = "vote_" + dtf.format(now) + ".pdf";
        PdfWriter.getInstance(document, new FileOutputStream(directoryPath+pdfFileName));
        document.open();
        document.add(new Paragraph(data));
        document.close();
    }
}
