package ba.etf.elections.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PopupControl;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("exampleBallot.fxml"));
        ValidationController ctrl = new ValidationController();
//        FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("mainBallot.fxml"));
//        MainController ctrl = new MainController();
        loader.setController(ctrl);
        Parent root = loader.load();
        primaryStage.setTitle("Izbori 2022");
        Scene scene = new Scene(root, PopupControl.USE_COMPUTED_SIZE, PopupControl.USE_COMPUTED_SIZE);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(true);
    }

    public static void main(String[] args) {
        launch();
    }
}