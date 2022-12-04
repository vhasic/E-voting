package ba.etf.elections.controllers;

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
        // todo this doesnt work because radio buttons cannot be deselected if they are in the same group
        // Invert the selection of the radio buttons on click
        /*gridPane.getChildren().foreach(node -> {
            if (node instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) node;
                radioButton.setOnAction(actionEvent -> {
                    radioButton.setSelected(!radioButton.isSelected());
                });
            }
//            return node;
        });*/

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


    public void makeBallotValid() {
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
        // deselect all elements in other vbox-es (parties)
        for (int i = 0; i < vBoxes.size(); i++) {
            VBox vBox = vBoxes.get(i);
            if (i!=numberOfSelected) {
                vBox.getChildren().forEach(node -> {
                    if (node instanceof CheckBox) {
                        ((CheckBox) node).setSelected(false);
                    } else if (node instanceof RadioButton) {
                        ((RadioButton) node).setSelected(false);
                    }
                });
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

/*
public void makeBallotValid() {
        List<VBox> vBoxes = gridPane.getChildren()
                .stream()
                .filter(node -> node instanceof VBox)
                .map(node -> (VBox) node)
                .toList();
        for (VBox vBox : vBoxes) {
            // if any element in one vbox (party) is selected no other elements can be selected for ballot to be valid
            boolean anySelected = vBox.getChildren().stream().anyMatch(node -> {
                if ((node instanceof RadioButton && ((RadioButton) node).isSelected()) || (node instanceof CheckBox && ((CheckBox) node).isSelected())) {
                    return true;
                }
                return false;
            });
            if (anySelected) {
                vBox.getChildren().forEach(node -> {
                    if (node instanceof CheckBox) {
                        ((CheckBox) node).setSelected(false);
                    } else if (node instanceof RadioButton) {
                        ((RadioButton) node).setSelected(false);
                    }
                });
            } else {
                vBox.getChildren()
                        .stream()
                        .filter(node -> node instanceof CheckBox)
                        .forEach(node -> node.setDisable(false));
            }

            /*Node firstChild = vBox.getChildren().get(0);
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
            }*/
        /*}
                }
 */