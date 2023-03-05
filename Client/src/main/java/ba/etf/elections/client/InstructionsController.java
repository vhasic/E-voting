package ba.etf.elections.client;

import ba.etf.elections.client.helper.CommonFunctions;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class InstructionsController {
    public Button btnNext;

    @FXML
    protected void initialize() {
        btnNext.setOnAction(actionEvent -> {
            // get the current stage from the button that was clicked
            Stage currentStage = (Stage) btnNext.getScene().getWindow();
            CommonFunctions.switchToNewScene(ElectionApp.class.getResource("mainBallot.fxml"), currentStage);
        });
    }
}
