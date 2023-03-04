package ba.etf.elections.client;

import ba.etf.elections.client.helper.IPDFHelper;
import ba.etf.elections.client.helper.PDFHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.DocumentException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ValidationController {

    public GridPane gridPane; // this name must be exactly the same as the fx:id in the FXML file
    public Pagination pagination;
    public Button btnSubmit;
    public Button btnSubmitInvalid;


    @FXML
    protected void initialize() {
        // todo: fix this loads all pages at once
        // initialize pagination
//        pagination.setPageCount(2);
//        pagination.setPageFactory(pageIndex -> {
//            try {
//                return switch (pageIndex) {
//                    case 0 -> FXMLLoader.load(getClass().getResource("votingInstructions.fxml"));
//                    case 1 -> FXMLLoader.load(getClass().getResource("exampleBallot.fxml"));
//                    default -> null;
//                };
//                // FXMLLoader loader = new FXMLLoader(getClass().getResource("page" + pageIndex + ".fxml"));
//                // return loader.load();
//            } catch (IOException e) {
//                e.printStackTrace();
//                return null;
//            }
//        });


        btnSubmitInvalid.setOnAction(actionEvent -> {
            System.out.println("Invalid ballot submitted");
            Vote vote = Vote.createInvalidVote();
            vote.calculateVoteMacHash(); // calculate vote mac hash to assure vote integrity
            writeVoteToFile(vote);
            try {
                PDFHelper.printToPDF(vote.toString(), ".\\Client\\PDFVotes\\");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            clearBallot();
        });

        btnSubmit.setOnAction(actionEvent -> {
            if (isBallotValid()) {
                System.out.println("Valid ballot submitted");
                Vote vote = getVotedCandidates();
                vote.calculateVoteMacHash(); // calculate vote mac hash to assure vote integrity
                writeVoteToFile(vote);
                try {
                    PDFHelper.printToPDF(vote.toString(),".\\Client\\PDFVotes\\");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                clearBallot();
            } else {
                // Show the error alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.getDialogPane().setStyle("-fx-font-size: 18px;"); // set font size of alert dialog
                alert.setTitle("Greška");
                alert.setHeaderText("Nevalidno popunjen glasački listić");
                alert.setContentText("Molimo da popunite glasački listić ispravno.");
                alert.showAndWait();
            }
        });
    }

    /**
     * Writes the vote to file as one object of JSON array format
     * @param vote vote to be written to file
     */
    private void writeVoteToFile(Vote vote) {
        ObjectMapper mapper = new ObjectMapper();
        List<Vote> votes;
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

    /**
     * Returns opened file with given name. If file doesn't exist, creates it and writes empty JSON array to it.
     * @param filename name of the file to be created if it doesn't exist
     * @return file with given name
     */
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


    /**
     * Gets all selected fields from the ballot
     * @return vote that has list of strings that represent voted candidates
     */
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
                        String candidateParty = ((RadioButton) vBox.getChildren().get(0)).getText();
                        vBox.getChildren().forEach(node -> {
                            if (node instanceof RadioButton radioButton && radioButton.isSelected()) {// if party was selected
                                vote.getVotedCandidates().add(radioButton.getText());
                            } else if (node instanceof CheckBox checkBox && checkBox.isSelected()) { // if candidate was selected
                                vote.getVotedCandidates().add(candidateParty+":"+checkBox.getText()); // i.e "Party A:1. Candidate 1"
                            }
                        });
                    }
                });
        return vote;
    }

    /**
     * Checks if ballot is valid: Only party or multiple candidates from one group on ballot can be selected
     * @return true if ballot is valid, false otherwise
     */
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

    /**
     * Clears all markings from the ballot
     */
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