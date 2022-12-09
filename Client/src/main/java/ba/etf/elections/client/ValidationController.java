package ba.etf.elections.client;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class ValidationController {

    public GridPane gridPane; // this name must be exactly the same as the fx:id in the FXML file
    public Button btnSubmit;

    @FXML
    protected void initialize() {
        btnSubmit.setOnAction(actionEvent -> {
            if (isBallotValid()) {
                // submit results
                System.out.println("Ballot is valid");
                return;
            } else {
                // Show the error alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Greška");
                alert.setHeaderText("Pogrešno popunjen listić");
                alert.setContentText("Ne može se glasati za različite kandidate i stranke");
                alert.showAndWait();
            }
        });
    }

    private boolean isBallotValid() {
        List<VBox> vBoxes = gridPane.getChildren()
                .stream()
                .filter(node -> node instanceof VBox)
                .map(node -> (VBox) node)
                .toList();
        int numberOfSelected = 0;
        for (int i = 0; i < vBoxes.size(); i++) {
            VBox vBox = vBoxes.get(i);
            // if any element in one vbox (party) is selected no other elements can be selected for ballot to be valid
            boolean anySelected = vBox.getChildren().stream().anyMatch(node -> {
                if ((node instanceof RadioButton && ((RadioButton) node).isSelected()) || (node instanceof CheckBox && ((CheckBox) node).isSelected())) {
                    return true;
                }
                return false;
            });
            if (anySelected) {
                numberOfSelected=i;
                break;
            }
        }
        // check other vboxes (parties) to see if any other elements are selected
        for (int i = 0; i < vBoxes.size(); i++) {
            VBox vBox = vBoxes.get(i);
            if (i != numberOfSelected) {
                boolean anySelected = vBox.getChildren().stream().anyMatch(node -> {
                    if ((node instanceof RadioButton && ((RadioButton) node).isSelected()) || (node instanceof CheckBox && ((CheckBox) node).isSelected())) {
                        return true;
                    }
                    return false;
                });
                if (anySelected) {
                    return false;
                }
            }
        }
        return true;
    }
}