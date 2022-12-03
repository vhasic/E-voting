package ba.etf.elections;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class BallotController {
    public GridPane gridPane;

    @FXML
    public void initialize() {
        gridPane.add(new Label("Hello"), 0, 0);
    }
}

