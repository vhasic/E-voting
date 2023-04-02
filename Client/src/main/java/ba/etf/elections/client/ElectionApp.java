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

import java.io.IOException;

public class ElectionApp extends Application {
    private static Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws IOException {
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
                        primaryStage.close();
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        ElectionApp.primaryStage = primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}