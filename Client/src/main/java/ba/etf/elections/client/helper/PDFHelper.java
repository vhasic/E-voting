package ba.etf.elections.client.helper;

import ba.etf.elections.client.Vote;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PDFHelper {
    /**
     * Prints given vote to PDF file. File name is vote_yyyy-MM-dd_HH-mm-ss.pdf
     *
     * @param vote Vote to be printed
     */
    public static void printToPDF(Vote vote, String directoryPath) throws DocumentException, FileNotFoundException {
        String formattedVote = CommonFunctions.getFormattedVoteCandidates(vote);
        Document document = new Document();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String pdfFileName = "vote_" + dtf.format(now) + ".pdf";
        PdfWriter.getInstance(document, new FileOutputStream(directoryPath+pdfFileName));
        document.open();
        document.add(new Paragraph(formattedVote));
        // appending MAC hash as QR code
        document.add(CommonFunctions.getQRCodeImage(vote));
        document.close();
    }



}
