package ba.etf.elections.client.helper;

import ba.etf.elections.client.Vote;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BarcodeQRCode;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.Optional;

public class CommonFunctions {
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

/*    public static Alert getCustomConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().setStyle("-fx-font-size: 18px;"); // set font size of alert dialog
        alert.setTitle("Obavještenje");
        alert.setHeaderText("Molimo da potvrdite željenu akciju");
        // Create a GridPane to hold the text and image
        GridPane grid = new GridPane();
        // Create a label for the text
        Label label = new Label("Unesite lozinku:");
        label.setWrapText(true);
        PasswordField passwordField = new PasswordField();
        // add elements to grid
        grid.add(label, 0, 0);
        grid.add(passwordField, 1, 0);
        grid.setHgap(10);
        grid.setVgap(10);
        // Set the grid as the content for the dialog pane
        alert.getDialogPane().setContent(grid);
        return alert;
    }

    public static void customWindowClose(Stage stage) {
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                // consume event
                event.consume();

                // execute your own shutdown procedure
                // todo not implemented
                Alert alert = getCustomConfirmationDialog();
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    if (checkPassword(passwordField.getText())) {
                        stage.close();
                    } else {
                        Alert alert1 = new Alert(Alert.AlertType.ERROR);
                        alert1.setTitle("Greška");
                        alert1.setHeaderText("Unijeli ste pogrešnu lozinku");
                        alert1.showAndWait();
                    }
                }
            }
        });
    }

    public static Boolean checkPassword(String password) {
        String passHash = System.getenv("passHash");
        // todo not implemented
        return passHash.equals(password);
    }*/
}
