package ba.etf.elections.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PopupControl;
import javafx.stage.Stage;

import java.io.IOException;

public class ElectionApp extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(ElectionApp.class.getResource("mainBallot.fxml"));
        MainController ctrl = new MainController();
        loader.setController(ctrl);
        Parent root = loader.load();
        primaryStage.setTitle("Izbori 2022");
        Scene scene = new Scene(root, PopupControl.USE_COMPUTED_SIZE, PopupControl.USE_COMPUTED_SIZE);
//        primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
        primaryStage.show();
//        primaryStage.setAlwaysOnTop(true);
        primaryStage.setResizable(true);

//        // custom minimize and closing (request password)
//        primaryStage.setOnHiding(event -> {
//
////            event.consume(); // Prevent default behavior
//        });
    }

    public static void main(String[] args) {
        launch();
    }
}
// todo at the end of development, use jDeploy to deploy app to npm or github artifacts: https://www.jdeploy.com/docs/manual/#_getting_started