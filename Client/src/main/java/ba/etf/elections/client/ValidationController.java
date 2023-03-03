package ba.etf.elections.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ValidationController {

    public GridPane gridPane; // this name must be exactly the same as the fx:id in the FXML file
    public Button btnSubmit;
    public Button btnSubmitInvalid;

    @FXML
    protected void initialize() {
        btnSubmitInvalid.setOnAction(actionEvent -> {
            // submit results
            System.out.println("Invalid ballot submitted");
            Vote vote = Vote.createInvalidVote();
            writeVoteToFile(vote);
            clearBallot();
        });

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
                // todo fix path to css file
//                 ../../../../resources/css/style.css
                alert.getDialogPane().getStylesheets().add(".\\Client\\src\\main\\resources\\css\\style.css"); // adding stylesheet to the alert to make font bigger
                alert.getDialogPane().getStyleClass().add("dialogClass"); // adding style class to the alert to make font bigger
                alert.setTitle("Error");
                alert.setHeaderText("Invalid ballot");
                alert.setContentText("Please check your ballot and try again.");
                alert.showAndWait();

                /*// Show the warning alert with two buttons: cancel and submit anyway
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Ballot is not valid");
                alert.setContentText("Do you want to submit anyway?");
                alert.showAndWait();*/
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
            votes = mapper.readValue(file, new TypeReference<>() {
            });
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