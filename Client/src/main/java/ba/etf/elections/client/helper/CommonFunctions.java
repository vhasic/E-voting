package ba.etf.elections.client.helper;

import ba.etf.elections.client.ValidationController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PopupControl;
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
}
