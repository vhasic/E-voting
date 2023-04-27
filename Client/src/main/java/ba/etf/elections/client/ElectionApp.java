/*
 * Copyright (c) 2023. Vahidin Hasić
 */

package ba.etf.elections.client;

import ba.etf.elections.client.helper.CommonFunctions;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PopupControl;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ElectionApp extends Application {
    private static final Logger logger = LogManager.getLogger(ElectionApp.class);
    private static Stage primaryStage;

    public static void main(String[] args) {
        launch();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // configure logger
        String log4j2ConfigFile = ElectionApp.class.getResource("/logging/fileLogging.xml").getFile();
        Configurator.initialize(null, log4j2ConfigFile);

        FXMLLoader loader = new FXMLLoader(ElectionApp.class.getResource("mainBallot.fxml"));
        MainController ctrl = new MainController();
        loader.setController(ctrl);
        Parent root = loader.load();
        primaryStage.setTitle("Izbori 2023");
        Scene scene = new Scene(root, PopupControl.USE_COMPUTED_SIZE, PopupControl.USE_COMPUTED_SIZE);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setFullScreen(true);
        primaryStage.setResizable(true);
//        primaryStage.setMaximized(true);
//        // forbid exiting fullscreen
//        primaryStage.fullScreenProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue) {
//                primaryStage.setFullScreen(true);
//            }
//        });
        // forbid minimizing
        primaryStage.iconifiedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                primaryStage.setIconified(false);
            }
        });

        // custom minimize and closing (request password)
        primaryStage.setOnCloseRequest(event -> {
            event.consume(); // Prevent default behavior
            ConfirmationController confirmationController = new ConfirmationController();
            Stage stage = null;
            try {
                stage = CommonFunctions.createConfirmationStage(confirmationController);
                // if the action was confirmed in the new window (stage is closed), open new ballot
                stage.setOnHidden(e -> {
                    if (confirmationController.isActionConfirmed()) {
                        if (submittedVotesMatchEnteredPassword("logs/app.log")) {
                            logger.info("Application closed: Number of submitted votes matches number of opened ballots");
                            System.exit(0);
                        } else {
                            logger.error("Application closed: Number of submitted votes does not match number of opened ballots");
                            System.exit(1);
                        }
//                        primaryStage.close();
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        ElectionApp.primaryStage = primaryStage;
    }

    /**
     * Check if the number of submitted votes matches the number of entered password to open ballots
     * @param logFilePath path to the log file
     * @return true if the number of submitted votes matches the number of opened ballots
     */
    private boolean submittedVotesMatchEnteredPassword(String logFilePath) {
        Map<String, Integer> pageCounts = new HashMap<>();
        int newBallotCount = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(logFilePath));
            String line = reader.readLine();

            while (line != null) {
                if (line.contains("Ballot submitted page")) {
                    String[] parts = line.split(" ");
                    String page = parts[parts.length - 1];
                    int count = pageCounts.getOrDefault(page, 0) + 1;
                    pageCounts.put(page, count);
                } else if (line.contains("Password entered: New ballot opened")) {
                    newBallotCount++;
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String page : pageCounts.keySet()) {
            int submittedCount = pageCounts.get(page);
            if (submittedCount != newBallotCount + 1) {
                return false;
            }
        }

        return true;
    }
}