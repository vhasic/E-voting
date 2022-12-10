package ba.etf.elections.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
//                List<Vote> votes = getVotedCandidates();
                Vote vote = getVotedCandidates();
                writeVoteToFile(vote);
                clearBallot();
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

    // method that writes the votes to file in JSON array format
    private void writeVoteToFile(Vote vote) {
        ObjectMapper mapper = new ObjectMapper();
        List<Vote> votes;
//        File file = new File(".\\Client\\Votes.json");
        File file = getFile(".\\Client\\Votes.json");

        // read votes from file into a list
        try {
            votes = mapper.readValue(file, new TypeReference<List<Vote>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // append new vote to the list
        votes.add(vote);

        // write new list to the file
        try {
            mapper.writeValue(file, votes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            try {
                FileWriter fileWriter = new FileWriter(file,false);
                fileWriter.write("[]");
                fileWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }


    private Vote getVotedCandidates() {
        Vote vote = new Vote();
        gridPane.getChildren()
                .stream()
                .filter(node -> node instanceof VBox)
                .forEach(nodeVBox -> {
                    VBox vBox = (VBox) nodeVBox;
                    // get vBox that has votes in it
                    boolean anySelected = vBox.getChildren().stream().anyMatch(node -> {
                        if ((node instanceof RadioButton && ((RadioButton) node).isSelected()) || (node instanceof CheckBox && ((CheckBox) node).isSelected())) {
                            return true;
                        }
                        return false;
                    });
                    if (anySelected) {
                        vBox.getChildren().forEach(node -> {
                            if (node instanceof RadioButton radioButton && radioButton.isSelected()) {// if party was selected
                                vote.getVotedCandidates().add(radioButton.getText());
                            } else if (node instanceof CheckBox checkBox && checkBox.isSelected()) { // if candidate was selected
                                vote.getVotedCandidates().add(checkBox.getText());
                            }
                        });
                    }
                });
        return vote;
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
                numberOfSelected = i;
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

    private void clearBallot() {
        gridPane.getChildren().forEach(node -> {
            if (node instanceof VBox vBox) {
                vBox.getChildren().forEach(node1 -> {
                    if (node1 instanceof RadioButton) {
                        ((RadioButton) node1).setSelected(false);
                    } else if (node1 instanceof CheckBox) {
                        ((CheckBox) node1).setSelected(false);
                    }
                });
            }
        });
    }
}

/*
private List<Vote> getVotedCandidates() {
        List<Vote> votedCandidates = new ArrayList<>();
        gridPane.getChildren()
                .stream()
                .filter(node -> node instanceof VBox)
                .forEach(nodeVBox -> {
                    VBox vBox = (VBox) nodeVBox;
                    // get vBox that has votes in it
                    boolean anySelected = vBox.getChildren().stream().anyMatch(node -> {
                        if ((node instanceof RadioButton && ((RadioButton) node).isSelected()) || (node instanceof CheckBox && ((CheckBox) node).isSelected())) {
                            return true;
                        }
                        return false;
                    });
                    if (anySelected) {
                        vBox.getChildren().forEach(node -> {
                            if (node instanceof RadioButton radioButton && radioButton.isSelected()) {// if party was selected
                                Vote vote = new Vote(radioButton.getText());
                                votedCandidates.add(vote);
                            } else if (node instanceof CheckBox checkBox && checkBox.isSelected()) { // if candidate was selected
                                Vote vote = new Vote(checkBox.getText());
                                votedCandidates.add(vote);
                            }
                        });
                    }
                });
        return votedCandidates;
    }
 */