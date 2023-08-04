/*
 * Copyright (c) 2023. Vahidin Hasić
 */

package ba.etf.elections.ballotGenerator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PopupControl;
import javafx.stage.Stage;

import java.io.IOException;

public class BallotGeneratorApplication extends Application {
    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage stage) throws IOException {
        BallotGeneratorApplication.stage = stage;
        FXMLLoader loader = new FXMLLoader(BallotGeneratorApplication.class.getResource("main.fxml"));
        MainController ctrl = new MainController();
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle("BallotGenerator");
        Scene scene = new Scene(root, PopupControl.USE_COMPUTED_SIZE, PopupControl.USE_COMPUTED_SIZE);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
