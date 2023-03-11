package ba.etf.elections.client.helper;

import ba.etf.elections.client.ValidationController;
import ba.etf.elections.client.Vote;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BarcodeQRCode;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PopupControl;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;

public class CommonFunctions {
    public static void switchToNewScene(URL fxmlURL, Stage currentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(fxmlURL);
            ValidationController ctrl = new ValidationController();
            loader.setController(ctrl);
            Parent root = loader.load();
            // create a new instance of the scene you want to switch to
            Scene newScene = new Scene(root, PopupControl.USE_COMPUTED_SIZE, PopupControl.USE_COMPUTED_SIZE);
            // set the new scene to the stage
            currentStage.setScene(newScene);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static GridPane getGridPaneFromFXML(URL fxmlURL) {
        try {
            FXMLLoader loader = new FXMLLoader(fxmlURL);
            return loader.load();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static String getFormattedVoteCandidates(Vote vote) {
        StringBuilder sb = new StringBuilder();
        for (String candidate : vote.getVotedCandidates()) {
            sb.append(candidate);
            sb.append("\n");
        }
        return sb.toString();
    }

    public static Image getQRCodeImage(Vote vote) throws DocumentException {
        BarcodeQRCode qrCode = new BarcodeQRCode(vote.getVoteMacHash(), 200, 200, null);
        return qrCode.getImage();
    }

    public static java.awt.Image getAWTQRCodeImage(Vote vote) throws DocumentException {
        BarcodeQRCode qrCode = new BarcodeQRCode(vote.getVoteMacHash(), 200, 200, null);
        return qrCode.createAwtImage(java.awt.Color.BLACK, java.awt.Color.WHITE);
    }
}
