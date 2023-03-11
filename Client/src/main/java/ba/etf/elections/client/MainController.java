package ba.etf.elections.client;

import ba.etf.elections.client.helper.CommonFunctions;
import ba.etf.elections.client.helper.PDFHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.DocumentException;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MainController {
    private static final List<String> FILES_TO_STORE_VOTES = Arrays.asList("Votes_Predstavnicki_Dom.json", "Votes_Skupstina.json");
    private static int currentPage = 0;
    public ScrollPane scrollPane;
    public GridPane innerGridPane; // this name must be exactly the same as the fx:id in the FXML file
    public Button btnSubmit;
    public Button btnSubmitInvalid;
    public Button btnPage0;
    public Button btnPage1;
    public Button btnPage2;
    public Button btnPage3;
    public Button btnPage4;

    @FXML
    protected void initialize() {
        btnPage0.setOnAction(actionEvent -> openPage(0));
        btnPage1.setOnAction(actionEvent -> openPage(1));
        btnPage2.setOnAction(actionEvent -> openPage(2));
        btnPage3.setOnAction(actionEvent -> openPage(3));
        btnPage4.setOnAction(actionEvent -> openPage(4));

        btnSubmitInvalid.setOnAction(actionEvent -> {
            // Wait for the user to press the OK button
            Alert alert = createAlert("Obavještenje",
                    "Jeste li sigurni da želite predati nevažeći glasački listić?",
                    "Molimo da potvrdite da želite predati nevažeći glasački listić.",
                    Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                System.out.println("Invalid ballot submitted");
                Vote vote = Vote.createInvalidVote();
                vote.calculateVoteMacHash(); // calculate vote mac hash to assure vote integrity
                submitVote(vote);

                deactivateButton(currentPage);
                openPage(currentPage + 1);
            }

        });

        btnSubmit.setOnAction(actionEvent -> {
            if (isBallotValid()) {
                Vote vote = getVotedCandidates();
                vote.calculateVoteMacHash(); // calculate vote mac hash to assure vote integrity

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.getDialogPane().setStyle("-fx-font-size: 18px;"); // set font size of alert dialog
                alert.setTitle("Obavještenje");
                alert.setHeaderText("Jeste li sigurni da želite predati glasački listić?");


                // Create a GridPane to hold the text and image
                GridPane grid = new GridPane();
                // Create a label for the text
                Label label = new Label("Molimo da potvrdite da želite predati glasački listić.\n" +
                        "Ovako će izgledati predati listić:\n" + CommonFunctions.getFormattedVoteCandidates(vote));
                label.setWrapText(true);
                // Create an image view for the image
                ImageView imageView;
                try {
                    // Convert java.awt.Image to javafx.scene.image.Image
                    java.awt.Image awtImage = CommonFunctions.getAWTQRCodeImage(vote);
                    BufferedImage bImage = new BufferedImage(awtImage.getWidth(null), awtImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                    bImage.getGraphics().drawImage(awtImage, 0, 0, null);
                    javafx.scene.image.Image fxImage = SwingFXUtils.toFXImage(bImage, null);
                    imageView = new ImageView(fxImage);
                } catch (DocumentException e) {
                    throw new RuntimeException(e);
                }
                // Add the label and image view to the grid
                grid.add(label, 0, 0);
                grid.add(imageView, 0, 1);
                grid.setHgap(10);
                grid.setVgap(10);
                // Set the grid as the content for the dialog pane
                alert.getDialogPane().setContent(grid);


                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    System.out.println("Valid ballot submitted");
                    submitVote(vote);
                    deactivateButton(currentPage);
                    openPage(currentPage + 1);
                }
            } else {
                // Show the error alert
                Alert alert = createAlert("Greška",
                        "Nevalidno popunjen glasački listić",
                        "Označite samo jednu političku stranku,\nkoaliciju ili nezavisnog kandidata \nili onoliko kandidata koliko želite\n unutar jednog okvira",
                        Alert.AlertType.ERROR);
                alert.showAndWait();
            }
        });

        // at beginning open page 0
        openPage(0);
    }

    /**
     * Deactivates button with given page number when user submits a vote for that page.
     *
     * @param page number of page for which button should be deactivated
     */
    private void deactivateButton(Integer page) {
        switch (page) {
            case 1 -> btnPage1.setDisable(true);
            case 2 -> btnPage2.setDisable(true);
            case 3 -> btnPage3.setDisable(true);
            case 4 -> btnPage4.setDisable(true);
        }
    }

    /**
     * Opens page with given number and sets innerGridPane to the page's GridPane.
     * Visible buttons are set to visible or invisible depending on the page number.
     * Page 0 is instructions page, so buttons are invisible.
     *
     * @param page number of page to be opened
     */
    private void openPage(Integer page) {
        if (page == 0) {
            btnSubmitInvalid.setVisible(false);
            btnSubmit.setVisible(false);
        } else {
            btnSubmitInvalid.setVisible(true);
            btnSubmit.setVisible(true);
        }
        currentPage = page;
        innerGridPane = CommonFunctions.getGridPaneFromFXML(ElectionApp.class.getResource("page" + page + ".fxml"));
        scrollPane.setContent(innerGridPane);
    }

    /**
     * Writes vote to file and prints it to PDF. At the end, clears the ballot.
     *
     * @param vote vote to be written to file and printed to PDF
     */
    private void submitVote(Vote vote) {
        writeVoteToFile(vote);
        try {
            // store PDFs in ./PDFVotes/ folder
            PDFHelper.printToPDF(vote, "." + File.separator + "PDFVotes" + File.separator);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        clearBallot();
    }

    /**
     * Creates an alert dialog with the given parameters
     *
     * @param title     title of the alert dialog
     * @param header    header of the alert dialog
     * @param content   content of the alert dialog
     * @param alertType type of the alert dialog
     * @return alert dialog
     */
    private Alert createAlert(String title, String header, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.getDialogPane().setStyle("-fx-font-size: 18px;"); // set font size of alert dialog
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
    }

    /**
     * Writes the vote to file as one object of JSON array format
     *
     * @param vote vote to be written to file
     */
    private void writeVoteToFile(Vote vote) {
        ObjectMapper mapper = new ObjectMapper();
        List<Vote> votes;
//        String folderPath = "." + File.separator + "JSONVotes" + File.separator;
        String folderPath = "";
        File file = getFile(folderPath + FILES_TO_STORE_VOTES.get(currentPage));
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
     *
     * @param filename name of the file to be created if it doesn't exist
     * @return file with given name
     */
    private File getFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            try {
                FileWriter fileWriter = new FileWriter(file, false);
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
     *
     * @return vote that has list of strings that represent voted candidates
     */
    private Vote getVotedCandidates() {
        Vote vote = new Vote();
        innerGridPane.getChildren().stream().filter(node -> node instanceof VBox).forEach(nodeVBox -> {
            VBox vBox = (VBox) nodeVBox;
            // get vBox that has votes in it
            boolean anySelected = vBox.getChildren().stream().anyMatch(node -> (node instanceof RadioButton && ((RadioButton) node).isSelected()) || (node instanceof CheckBox && ((CheckBox) node).isSelected()));
            if (anySelected) {
                String candidateParty = ((RadioButton) vBox.getChildren().get(0)).getText();
                vBox.getChildren().forEach(node -> {
                    if (node instanceof RadioButton radioButton && radioButton.isSelected()) {// if party was selected
                        vote.getVotedCandidates().add(radioButton.getText());
                    } else if (node instanceof CheckBox checkBox && checkBox.isSelected()) { // if candidate was selected
                        vote.getVotedCandidates().add(candidateParty + ":" + checkBox.getText()); // i.e "Party A:1. Candidate 1"
                    }
                });
            }
        });
        return vote;
    }

    /**
     * Checks if ballot is valid: Only party or multiple candidates from one group on ballot can be selected
     *
     * @return true if ballot is valid, false otherwise
     */
    private boolean isBallotValid() {
        List<VBox> vBoxes = innerGridPane.getChildren().stream().filter(node -> node instanceof VBox).map(node -> (VBox) node).toList();
        int numberOfSelected = 0;
        for (int i = 0; i < vBoxes.size(); i++) {
            VBox vBox = vBoxes.get(i);
            // if any element in one vbox (party) is selected no other elements can be selected for ballot to be valid
            boolean anySelected = vBox.getChildren().stream().anyMatch(node -> (node instanceof RadioButton && ((RadioButton) node).isSelected()) || (node instanceof CheckBox && ((CheckBox) node).isSelected()));
            if (anySelected) {
                numberOfSelected = i;
                break;
            }
        }
        // check other vboxes (parties) to see if any other elements are selected
        for (int i = 0; i < vBoxes.size(); i++) {
            VBox vBox = vBoxes.get(i);
            if (i != numberOfSelected) {
                boolean anySelected = vBox.getChildren().stream().anyMatch(node -> (node instanceof RadioButton && ((RadioButton) node).isSelected()) || (node instanceof CheckBox && ((CheckBox) node).isSelected()));
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
        innerGridPane.getChildren().forEach(node -> {
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
