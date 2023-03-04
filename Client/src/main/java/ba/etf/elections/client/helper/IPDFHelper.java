package ba.etf.elections.client.helper;

import com.itextpdf.text.DocumentException;

import java.io.FileNotFoundException;

public interface IPDFHelper {
    /**
     * Prints given data to PDF file. File name is vote_yyyy-MM-dd_HH-mm-ss.pdf
     * @param data Data to be printed
     */
    void printToPDF(String data) throws DocumentException, FileNotFoundException;
}
