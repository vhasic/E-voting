/*
 * Copyright (c) 2023. Vahidin Hasić
 */

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class MainController {
    private static final Logger logger = LogManager.getLogger(MainController.class);

    private static int currentPage = 0;
    public ScrollPane scrollPane;
    public GridPane innerGridPane; // this name must be exactly the same as the fx:id in the FXML file
    public HBox pageNumbersHBox;
    public Button btnSubmit;
    public Button btnSubmitInvalid;
    public Button btnOpenNewBallot;
    public ArrayList<Button> btnPageList = new ArrayList<>();

    @FXML
    protected void initialize() {
        // at beginning, nw ballot is opened so make btnOpenNewBallot invisible
        btnOpenNewBallot.setVisible(false);

        // create button for each page (ballot) and add it to btnPageList
        bindPageButtons();

        // when one voter finishes voting, new ballot can be opened only by entering password
        btnOpenNewBallot.setOnAction(actionEvent -> {
            // request password in new window
            try {
                ConfirmationController ctrl = new ConfirmationController();
                Stage stage = CommonFunctions.createConfirmationStage(ctrl);

                // if the action was confirmed in the new window (stage is closed), open new ballot
                stage.setOnHidden(event -> {
                    if (ctrl.isActionConfirmed()) {
                        logger.info("Password entered: New ballot opened");
                        prepareNewBallot();
                        btnOpenNewBallot.setVisible(false);
                        openPage(0);
                    }
                });
            } catch (IOException e) {
                logger.error("Error while opening new ballot: " + e.getMessage());
                e.printStackTrace();
            }
        });

        // setting page buttons to open corresponding page when clicked
        for (int i = 0; i < btnPageList.size(); i++) {
            int finalI = i;
            btnPageList.get(i).setOnAction(actionEvent -> openPage(finalI));
        }

        btnSubmitInvalid.setOnAction(actionEvent -> {
            // Wait for the user to press the OK button
            Alert alert = CommonFunctions.createAlert("Obavještenje",
                    "Jeste li sigurni da želite predati nevažeći glasački listić?",
                    "Molimo da potvrdite da želite predati nevažeći glasački listić.",
                    Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> result = alert.showAndWait();

            // if user confirms, submit invalid ballot
            if (result.isPresent() && result.get() == ButtonType.OK) {
//                System.out.println("Invalid ballot submitted");
                Vote vote = Vote.createInvalidVote();
                vote.calculateVoteMacHash(); // calculate vote mac hash to assure vote integrity
                submitVote(vote);
//                logger.info("Invalid ballot submitted " + "page" + currentPage);

                setPageButtonVisibility(currentPage, true);
                openPage(currentPage + 1);
                // if all pages are disabled, then member of voting committee has to enable new ballot
                if (allPageButtonsDisabled()) {
                    finishBallotCasting();
                }
            }

        });

        btnSubmit.setOnAction(actionEvent -> {
            if (isBallotValid()) {
                Vote vote = getVotedCandidates();
                vote.calculateVoteMacHash(); // calculate vote mac hash to assure vote integrity

                // Show exact vote representation that is printed to pdf to ask user to confirm it
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.getDialogPane().setStyle("-fx-font-size: 18px;"); // set font size of alert dialog
                alert.setTitle("Obavještenje");
                alert.setHeaderText("Jeste li sigurni da želite predati glasački listić?");
                // Create a GridPane to hold the text and image
                GridPane grid = new GridPane();
                // Create a label for the text
                Label label = new Label("Molimo da potvrdite da želite predati glasački listić.\n" +
                        "Ovako će izgledati predati listić:\n" +
                        "-------------------------------------\n" + CommonFunctions.getFormattedVoteCandidates(vote));
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
                    logger.error("Error while creating QR code image: " + e.getMessage());
                    throw new RuntimeException(e);
                }
                // Add the label and image view to the grid
                grid.add(label, 0, 0);
                grid.add(imageView, 0, 1);
                grid.setHgap(10);
                grid.setVgap(10);
                // Set the grid as the content for the dialog pane
                alert.getDialogPane().setContent(grid);
                alert.initOwner(ElectionApp.getPrimaryStage());
                Optional<ButtonType> result = alert.showAndWait();

                // wait for the user to confirm the vote
                if (result.isPresent() && result.get() == ButtonType.OK) {
//                    System.out.println("Valid ballot submitted");
                    submitVote(vote);
//                    logger.info("Valid ballot submitted " + "page" + currentPage);
                    setPageButtonVisibility(currentPage, true);
                    openPage(currentPage + 1);
                    // if all pages are disabled, then member of voting committee has to enable new ballot
                    if (allPageButtonsDisabled()) {
                        finishBallotCasting();
                    }
                }
            } else {
                // Show the error alert
                Alert alert = CommonFunctions.createAlert("Greška",
                        "Nevalidno popunjen glasački listić",
                        """
                                Označite samo jednu političku stranku,
                                koaliciju ili nezavisnog kandidata
                                ili onoliko kandidata koliko želite
                                 unutar jedne stranke.""",
                        Alert.AlertType.ERROR);
                alert.showAndWait();
            }
        });

        // at beginning, open first page with voting instructions
        openPage(0);
    }

    /**
     * Creates buttons for each page and adds them to pageNumbersHBox and btnPageList.
     */
    private void bindPageButtons() {
        int i = 0;
        // clear all children of pageNumbersHBox
        pageNumbersHBox.getChildren().clear();
        while (CommonFunctions.getResource("page" + i + ".fxml") != null) {
            // create new button with text for each page
//            Button button = new Button(Integer.toString(i));
            Button button = new Button(CommonFunctions.getBallotTitleKeyword(CommonFunctions.getResource("page" + i + ".fxml")));
            // set fx:id for button to "#btnPage" + i
            button.setId("btnPage" + i);
            // set font size to 18px
            button.setStyle("-fx-font-size: 18px;");
            // add button as child to pageNumbersHBox
            pageNumbersHBox.getChildren().add(button);
//            Button button = (Button) pageNumbersHBox.lookup("#btnPage" + i); // find button with fx:id "#btnPage" + i
            // add button to list of page buttons btnPageList
            btnPageList.add(button);
            i++;
        }
        // set spacing between page buttons to 10px
        pageNumbersHBox.setSpacing(10);
    }

    /**
     * When enabling new vote is confirmed in new window, this method is called to prepare new ballot.
     */
    private void prepareNewBallot() {
        // enable all buttons for new ballot
        for (int i = 0; i < btnPageList.size(); i++) {
            setPageButtonVisibility(i, false);
        }
        btnOpenNewBallot.setVisible(false);
        openPage(0);
    }

    /**
     * When all btnPages are disabled (voter finished voting), this method is called.
     */
    private void finishBallotCasting() {
        btnOpenNewBallot.setVisible(true);
        btnSubmitInvalid.setVisible(false);
        btnSubmit.setVisible(false);
        btnPageList.get(0).setDisable(true);
    }

    /**
     * Checks if all page buttons are disabled. After this function finishBallotCasting() is called.
     *
     * @return true if all page buttons are disabled, false otherwise
     */
    private Boolean allPageButtonsDisabled() {
        for (int i = 1; i < btnPageList.size(); i++) {
            if (!btnPageList.get(i).isDisabled()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Deactivates button with given page number when user submits a vote for that page.
     *
     * @param page     number of page for which button should be deactivated
     * @param disabled true if button should be disabled, false if button should not be disabled
     */
    private void setPageButtonVisibility(Integer page, boolean disabled) {
        btnPageList.get(page).setDisable(disabled);
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
        innerGridPane = CommonFunctions.getGridPaneFromFXML(CommonFunctions.getResource("page" + page + ".fxml"));
        scrollPane.setContent(innerGridPane);
    }

    /**
     * Writes vote to file and prints it to PDF. At the end, clears the ballot.
     *
     * @param vote vote to be written to file and printed to PDF
     */
    private void submitVote(Vote vote) {
        try {
            writeVoteToFile(vote);
//            logger.info("Vote written to file");
            // store PDFs in ./PDFVotes/ folder
            String directoryPath = System.getProperty("user.dir") + File.separator + "PDFVotes" + File.separator;
            // create directory if not exists
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            PDFHelper.printToPDF(vote, directory.getPath() + File.separator);
//            logger.info("Vote printed");
            logger.info("Ballot submitted " + "page" + currentPage); // this is logged only if vote is successfully saved to file and printed
        } catch (Exception e) {
            logger.error("Error while submitting vote" + e.getMessage());
            e.printStackTrace();
        }
        clearBallot();
    }

    /**
     * Writes the vote to file as one object of JSON array format
     *
     * @param vote vote to be written to file
     */
    private void writeVoteToFile(Vote vote) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String folderPath = System.getProperty("user.dir") + File.separator + "JSONVotes" + File.separator;
        // create directory if not exists
        File directory = new File(folderPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
//        String filename = "votes_page" + currentPage + ".json";
        String filename = CommonFunctions.getBallotTitleKeyword(CommonFunctions.getResource("page" + currentPage + ".fxml")) + ".json";
        File file = getFile(directory.getPath() + File.separator + filename);
        // read votes from file into a list
        List<Vote> votes = mapper.readValue(file, new TypeReference<>() {
        });
        // append new vote to the list
        votes.add(vote);
        // write new list to the file
        mapper.writeValue(file, votes);
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
                logger.error("Error while creating file " + filename + ": " + e.getMessage());
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