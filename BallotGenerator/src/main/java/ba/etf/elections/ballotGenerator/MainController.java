/*
 * Copyright (c) 2023. Vahidin Hasić
 */

package ba.etf.elections.ballotGenerator;

import ba.etf.elections.ballotGenerator.helper.PartyCandidates;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static ba.etf.elections.ballotGenerator.helper.BallotGeneratorHelper.*;

public class MainController {
    public TextField fieldBallotTitle;
    public Spinner<Integer> spinnerNumberOfColumns;
    public Button btnPathToJsonCandidates;
    public Button btnPathToSaveFXML;
    public Button btnSubmit;
    public Label labelJSONPath;
    public Label labelFXMLPath;

    private Path pathToJsonCandidates;
    private Path pathToFxmlFile;

    @FXML
    protected void initialize() {
        // Value factory.
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        spinnerNumberOfColumns.setValueFactory(valueFactory);

        btnPathToJsonCandidates.setOnAction(actionEvent -> {
            pathToJsonCandidates = createFileChooser();
            labelJSONPath.setText(pathToJsonCandidates.toString());
        });
        btnPathToSaveFXML.setOnAction(actionEvent -> {
            pathToFxmlFile = createFileChooser();
            labelFXMLPath.setText(pathToFxmlFile.toString());
        });
        btnSubmit.setOnAction(actionEvent -> {
            try {
                List<PartyCandidates> partyCandidatesList = GetPartiesCandidates(pathToJsonCandidates.toString());
                createFXMLFile(spinnerNumberOfColumns.getValue(), fieldBallotTitle.getText(),
                                pathToFxmlFile.toString(), partyCandidatesList);
                // notify user of successfull action
                Alert alert = createAlert("INFORMATION","Ballot generated successfully", "", Alert.AlertType.INFORMATION);
                Optional<ButtonType> result = alert.showAndWait();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                // notify user of unsuccessfull action
                Alert alert = createAlert("ERROR","Ballot generation failed", e.getMessage(), Alert.AlertType.ERROR);
                Optional<ButtonType> result = alert.showAndWait();
            }
        });
    }

    private Path createFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File selectedFile = fileChooser.showOpenDialog(BallotGeneratorApplication.getStage());
        Path filePath = null;
        if (selectedFile != null) {
            filePath = selectedFile.toPath();
        }
        return filePath;
    }
}
