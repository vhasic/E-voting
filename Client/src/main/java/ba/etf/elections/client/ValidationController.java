package ba.etf.elections.client;

import ba.etf.elections.client.helper.CommonFunctions;
import ba.etf.elections.client.helper.PDFHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ValidationController {

    public GridPane gridPane; // this name must be exactly the same as the fx:id in the FXML file
    public Button btnSubmit;
    public Button btnSubmitInvalid;
    public Button btnNext;
    private static int CURRENT_PAGE = 0;
    private static final List<String> FILES_TO_STORE_VOTES = Arrays.asList(".\\Client\\Votes_Predstavnicki_Dom.json",".\\Client\\Votes_Skupstina.json");


    @FXML
    protected void initialize() {
        // initialize pagination
//        pagination.setPageFactory((Integer pageIndex) -> getPageContent(pageIndex));

        btnNext.setOnAction(actionEvent -> {
            Alert alert = createAlert("Obavještenje", "Jeste li sigurni da želite preći na sljedeći glasački listić?", "Molimo da potvrdite da želite preći na sljedeći glasački listić,\ntrenutni će biti predat kao nevažeći.", Alert.AlertType.WARNING);
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                // hand in the current ballot as invalid
                System.out.println("Invalid ballot submitted");
                Vote vote = Vote.createInvalidVote();
                vote.calculateVoteMacHash(); // calculate vote mac hash to assure vote integrity
                submitVote(vote);

                openNextPage();
            }
        });

        btnSubmitInvalid.setOnAction(actionEvent -> {
            // Wait for the user to press the OK button
            Alert alert = createAlert("Obavještenje", "Jeste li sigurni da želite predati nevažeći glasački listić?", "Molimo da potvrdite da želite predati nevažeći glasački listić.", Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> result = alert.showAndWait();

            if(result.isPresent() && result.get() == ButtonType.OK){
                System.out.println("Invalid ballot submitted");
                Vote vote = Vote.createInvalidVote();
                vote.calculateVoteMacHash(); // calculate vote mac hash to assure vote integrity
                submitVote(vote);

                openNextPage();
            }

        });

        btnSubmit.setOnAction(actionEvent -> {
            if (isBallotValid()) {
                // Wait for the user to press the OK button
                Alert alert = createAlert("Obavještenje", "Jeste li sigurni da želite predati glasački listić?", "Molimo da potvrdite da želite predati glasački listić.", Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> result = alert.showAndWait();

                if(result.isPresent() && result.get() == ButtonType.OK){
                    System.out.println("Valid ballot submitted");
                    Vote vote = getVotedCandidates();
                    vote.calculateVoteMacHash(); // calculate vote mac hash to assure vote integrity
                    submitVote(vote);

                    openNextPage();
                }
            } else {
                // Show the error alert
                Alert alert = createAlert("Greška", "Nevalidno popunjen glasački listić", "Molimo da popunite glasački listić ispravno.", Alert.AlertType.ERROR);
                alert.showAndWait();
            }
        });
    }

    private void openNextPage(){
        // get the current stage from the button that was clicked
        Stage currentStage = (Stage) btnNext.getScene().getWindow();
        CURRENT_PAGE++; // on button next click, increment the current page and open the next page
        CommonFunctions.switchToNewScene(ElectionApp.class.getResource("mainBallot" + CURRENT_PAGE + ".fxml"), currentStage);
    }

    private void submitVote(Vote vote){
        writeVoteToFile(vote);
        try {
            PDFHelper.printToPDF(vote.toString(), ".\\Client\\PDFVotes\\");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        clearBallot();
    }

    private Alert createAlert(String title, String header, String content, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.getDialogPane().setStyle("-fx-font-size: 18px;"); // set font size of alert dialog
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
    }

    // todo: these environment variables have problem because of some special characters: folderPathToStorePDFs=.\\Client\\PDFVotes\\;pathToJsonVotesFile=.\\Client\\Votes.json;
    /**
     * Writes the vote to file as one object of JSON array format
     * @param vote vote to be written to file
     */
    private void writeVoteToFile(Vote vote) {
        ObjectMapper mapper = new ObjectMapper();
        List<Vote> votes;
//        File file = getFile(System.getenv("pathToJsonVotesFile"));
        File file = getFile(FILES_TO_STORE_VOTES.get(CURRENT_PAGE));
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