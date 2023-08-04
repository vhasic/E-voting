/*
 * Copyright (c) 2023. Vahidin Hasić
 */

package ba.etf.elections.voteCounter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainController {
    public Button btnPathToJsonVotes;
    public Button btnPathToFileToSaveVotes;
    public Button btnCountVotes;
    public Label labelJSONPath;
    public Label labelTXTPath;

    private Path pathToJsonVotes;
    private Path pathToFileToSaveVotes;

    @FXML
    protected void initialize() {
        btnPathToJsonVotes.setOnAction(actionEvent -> {
            pathToJsonVotes = createFileChooser();
            labelJSONPath.setText(pathToJsonVotes.toString());
        });
        btnPathToFileToSaveVotes.setOnAction(actionEvent -> {
            pathToFileToSaveVotes = createFileChooser();
            labelTXTPath.setText(pathToFileToSaveVotes.toString());
        });
        btnCountVotes.setOnAction(actionEvent -> {
            countVotes();
        });
    }

    private Path createFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File selectedFile = fileChooser.showOpenDialog(VoteCounterApplication.getStage());
        Path filePath = null;
        if (selectedFile != null) {
            filePath = selectedFile.toPath();
        }
        return filePath;
    }

    private void countVotes() {
        try {
            Map<String, Integer> voteCountHashMap = CountVotes(pathToJsonVotes);
            printMap(voteCountHashMap);
            saveMapToTxtFile(pathToFileToSaveVotes, voteCountHashMap);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Saves counted votes to specified file
     *
     * @param pathToFileToSaveVotes path and name of file where to save counted votes
     * @param voteCountHashMap      map of counted votes
     * @throws IOException if file cannot be created
     */
    private void saveMapToTxtFile(Path pathToFileToSaveVotes, Map<String, Integer> voteCountHashMap) throws IOException {
        File file = new File(pathToFileToSaveVotes.toUri());
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        for (Map.Entry<String, Integer> entry : voteCountHashMap.entrySet()) {
            fileWriter.write(entry.getKey() + " => " + entry.getValue() + " glasova" + "\r\n");
        }
        fileWriter.close();
    }

    /**
     * Counts votes from specified file
     *
     * @param pathToJsonVotes path and name of file where votes are stored
     * @return map of counted votes
     */
    private Map<String, Integer> CountVotes(Path pathToJsonVotes) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        File file = new File(pathToJsonVotes.toUri()); // open file that contains votes as JSON array of objects
        // read votes from file into a list of Vote objects
        ObjectMapper mapper = new ObjectMapper();
        List<Vote> votes = mapper.readValue(file, new TypeReference<>() {
        });
        // create hash map: name of candidate String and number of votes Integer
        Map<String, Integer> voteCountHashMap = new LinkedHashMap<>();

        for (Vote vote : votes) {
            Boolean macHashMatch = CryptographyHelper.validateMACHash(vote.getVotedCandidates().toString(), vote.getVoteMacHash());
            if (!macHashMatch) {
                throw new RuntimeException("MAC hash se ne podudara. Integritet glasova je kompromitovan!");
            }
            for (String candidate : vote.getVotedCandidates()) {
                if (voteCountHashMap.containsKey(candidate)) {
                    voteCountHashMap.put(candidate, voteCountHashMap.get(candidate) + 1);
                } else {
                    voteCountHashMap.put(candidate, 1);
                }
            }
        }
        // sort map by value in descending order
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        voteCountHashMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

        return sortedMap;
    }

    /**
     * Prints map to console
     *
     * @param map map to print
     */
    private void printMap(Map<String, Integer> map) {
        System.out.println("Ukupni glasovi:\n");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue() + " glasova");
        }
    }
}
