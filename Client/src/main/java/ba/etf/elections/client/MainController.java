package ba.etf.elections.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;

import java.io.IOException;

public class MainController {

    /*public Pagination pagination;
    private Integer lastPageAccessed = 0;
    @FXML
    protected void initialize() {
        pagination.setPageCount(3);
        pagination.setPageFactory((Integer pageIndex) -> {
            if (lastPageAccessed != 0 ){
                pagination.getPageFactory().call(lastPageAccessed).setDisable(true);
            }
            lastPageAccessed = pagination.getCurrentPageIndex();
            return getPageContent(pageIndex);
        });
    }

    public Node getPageContent(int pageIndex){
        try{
            //Creating the view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("page" + pageIndex + ".fxml"));
//            ValidationController ctrl = new ValidationController();
//            loader.setController(ctrl);
            Node node = loader.load();
//            GridPane node = new GridPane();
//            node.getChildren().add(new Label("Test + " + pageIndex));
            return node;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }*/

}
