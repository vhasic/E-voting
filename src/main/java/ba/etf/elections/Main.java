package ba.etf.elections;

import ba.etf.elections.controllers.MainController;
import ba.etf.elections.controllers.ValidationController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PopupControl;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException, ClassNotFoundException {
//        Class.forName("org.sqlite.JDBC"); //force Java ClassLoader to load class

/*        FXMLLoader loader = new FXMLLoader(Main.class.getResource("main.fxml"));
        MainController ctrl = new MainController();
        loader.setController(ctrl);
        Parent root = loader.load();
        primaryStage.setTitle("Izbori 2022");
        primaryStage.setScene(new Scene(root, PopupControl.USE_COMPUTED_SIZE, PopupControl.USE_COMPUTED_SIZE));
        primaryStage.show();
        primaryStage.setResizable(false);*/

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("exampleBallot.fxml"));
        ValidationController ctrl = new ValidationController();
        loader.setController(ctrl);
        Parent root = loader.load();
        primaryStage.setTitle("Izbori 2022");
        Scene scene = new Scene(root, PopupControl.USE_COMPUTED_SIZE, PopupControl.USE_COMPUTED_SIZE);
        scene.setOnMouseClicked(mouseEvent -> {
            ctrl.makeBallotValid();
        });
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    public static void main(String[] args) {
        launch();
    }
}