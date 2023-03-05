package ba.etf.elections.client;

import ba.etf.elections.client.helper.CommonFunctions;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PopupControl;
import javafx.stage.Stage;

public class InstructionsController {
    public Button btnNext;

    @FXML
    protected void initialize() {
        btnNext.setOnAction(actionEvent -> {
            // get the current stage from the button that was clicked
            Stage currentStage = (Stage) btnNext.getScene().getWindow();
            CommonFunctions.switchToNewScene(ClientApplication.class.getResource("mainBallot.fxml"), currentStage);
        });
    }
}
