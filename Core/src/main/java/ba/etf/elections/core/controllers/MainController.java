package ba.etf.elections.core.controllers;

import ba.etf.elections.core.dao.ElectionsDAO;
import ba.etf.elections.core.models.Candidate;
import ba.etf.elections.core.models.PoliticalParty;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class MainController {
    private final ElectionsDAO dao = ElectionsDAO.getInstance();

    public GridPane gridPane; // this name must be exactly the same as the fx:id in the FXML file

    @FXML
    protected void initialize() {
//        List<PoliticalParty> politicalParties = dao.getAllPoliticalParties();
//        for (int i = 0; i < politicalParties.size(); i++) {
//            PoliticalParty politicalParty = politicalParties.get(i);
//            List<Candidate> candidates = dao.getAllCandidatesForPoliticalParty(politicalParty.getId());
//            VBox vBox = getVBoxPartyWithCandidates(politicalParty.getPartyName(),candidates);
//            gridPane.add(vBox,i,0);
//        }
    }

    private VBox getVBoxPartyWithCandidates(String partyName, List<Candidate> candidates) {
        VBox vbox = new VBox();
        RadioButton radioButton = new RadioButton(partyName);
        radioButton.setId(partyName); // party name is unique
        vbox.getChildren().add(radioButton);
        for (Candidate candidate : candidates) {
            CheckBox checkBox = new CheckBox(candidate.getNameSurname());
            checkBox.setId(Integer.toString(candidate.getListNumber()));
            vbox.getChildren().add(checkBox);
        }
        return vbox;
    }
}