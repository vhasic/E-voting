package ba.etf.elections.client.helper;

import ba.etf.elections.client.ElectionApp;
import ba.etf.elections.client.Vote;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class PDFHelper {
    /**
     * Prints given vote to PDF file. File name is vote_yyyy-MM-dd_HH-mm-ss.pdf
     *
     * @param vote Vote to be printed
     */
    public static void printToPDF(Vote vote, String directoryPath) throws DocumentException, IOException {
        String formattedVote = CommonFunctions.getFormattedVoteCandidates(vote);
        Document document = new Document();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String pdfFileName = "vote_" + dtf.format(now) + ".pdf";
        PdfWriter.getInstance(document, new FileOutputStream(directoryPath+pdfFileName));
        document.open();
        // Set font that supports the special characters (č,ć,š,ž,đ)
        BaseFont bf = BaseFont.createFont("Client/src/main/resources/arial/Arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(bf, 12);
        document.add(new Paragraph(formattedVote,font));
        // appending MAC hash as QR code
        document.add(CommonFunctions.getQRCodeImage(vote));
        document.close();
    }


}
