package ba.etf.elections;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class MainController {
    private final ElectionsDAO dao = ElectionsDAO.getInstance();

/*    @FXML
    protected void initialize() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/about.fxml"));
            BallotController ctrl = new BallotController();
            loader.setController(ctrl);
            Parent root = loader.load();
            Stage stage=new Stage();
            stage.setTitle("Glasački listić");
            stage.setResizable(false);
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}