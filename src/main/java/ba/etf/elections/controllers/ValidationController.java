package ba.etf.elections.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;

public class ValidationController {

    public GridPane gridPane; // this name must be exactly the same as the fx:id in the FXML file

    @FXML
    protected void initialize() {
/*        gridPane.getChildren()
                .stream()
                .filter(node -> node instanceof CheckBox)
                .map(node -> (CheckBox)node)
                .map(checkBox -> {
                    checkBox.setOnAction(actionEvent -> {
                        makeBallotValid();
                    });
                    return checkBox;
                });*/
/*        for (CheckBox checkBox : checkBoxes) {
            checkBox.setOnMouseClicked(mouseEvent -> {
                makeBallotValid();
            });
//            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
//                makeBallotValid();
//            });
        }*/
        /*gridPane.setOnMouseClicked(event -> {
            makeBallotValid();
        });*/
    }

    public void makeBallotValid(){
        List<VBox> vBoxes = gridPane.getChildren()
                .stream()
                .filter(node -> node instanceof VBox)
                .map(node -> (VBox)node)
                .toList();
        for (VBox vBox : vBoxes) {
            // get first child of vBox, which is a RadioButton
            // if it is disabled then disable all checkboxes in the vBox
            Node firstChild = vBox.getChildren().get(0);
            if (firstChild instanceof RadioButton && !((RadioButton)firstChild).isSelected()) {
                vBox.getChildren()
                        .stream()
                        .filter(node -> node instanceof CheckBox)
                        .forEach(node -> {
                            ((CheckBox) node).setSelected(false);
                            node.setDisable(true);
                        });
            } else {
                vBox.getChildren()
                        .stream()
                        .filter(node -> node instanceof CheckBox)
                        .forEach(node -> node.setDisable(false));
            }
        }
    }
}

/*        RadioButton radioButton = new RadioButton("Enable check boxes");

        CheckBox checkBox1 = new CheckBox("Check box 1");
        checkBox1.disableProperty().bind(radioButton.selectedProperty().not());*/

// getting all check boxes from the grid pane
/*        List<CheckBox> checkBoxes = gridPane.getChildren()
                .stream()
                .filter(node -> node instanceof CheckBox)
                .map(node -> (CheckBox)node)
                .toList();*/