package ba.etf.elections.client.helper;

import ba.etf.elections.client.Vote;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BarcodeQRCode;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;

import java.net.URL;

public class CommonFunctions {
    /**
     * Creates an alert dialog with the given parameters
     *
     * @param title     title of the alert dialog
     * @param header    header of the alert dialog
     * @param content   content of the alert dialog
     * @param alertType type of the alert dialog
     * @return alert dialog
     */
    public static Alert createAlert(String title, String header, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.getDialogPane().setStyle("-fx-font-size: 18px;"); // set font size of alert dialog
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
    }
    /**
     * Loads GridPane from FXML file and returns it.
     * @param fxmlURL URL of FXML file
     * @return GridPane from FXML file
     */
    public static GridPane getGridPaneFromFXML(URL fxmlURL) {
        try {
            FXMLLoader loader = new FXMLLoader(fxmlURL);
            return loader.load();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Formats given vote to string containing all candidates from given vote. Each candidate is on new line.
     * @param vote Vote to be formatted
     * @return Formatted string containing all candidates from given vote
     */
    public static String getFormattedVoteCandidates(Vote vote) {
        StringBuilder sb = new StringBuilder();
        for (String candidate : vote.getVotedCandidates()) {
            sb.append(candidate);
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * @param vote Vote to generate QR code from
     * @return com.itextpdf.text.Image of QR code
     * @throws DocumentException if QR code cannot be created
     */
    public static Image getQRCodeImage(Vote vote) throws DocumentException {
        BarcodeQRCode qrCode = new BarcodeQRCode(vote.getVoteMacHash(), 200, 200, null);
        return qrCode.getImage();
    }

    /**
     * @param vote Vote to generate QR code from
     * @return java.awt.Image of QR code
     * @throws DocumentException if QR code cannot be created
     */
    public static java.awt.Image getAWTQRCodeImage(Vote vote) throws DocumentException {
        BarcodeQRCode qrCode = new BarcodeQRCode(vote.getVoteMacHash(), 200, 200, null);
        return qrCode.createAwtImage(java.awt.Color.BLACK, java.awt.Color.WHITE);
    }
}
