package ba.etf.elections.models;

import javafx.beans.property.SimpleObjectProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Candidate {
    private int id;
    private int listNumber;
    private String nameSurname;
    private SimpleObjectProperty<PoliticalParty> politicalParty;

    public Candidate() {
        this.politicalParty = new SimpleObjectProperty<>();
    }

    public Candidate(int id, int listNumber, String nameSurname, PoliticalParty politicalParty) {
        this.id = id;
        this.listNumber = listNumber;
        this.nameSurname = nameSurname;
        this.politicalParty = new SimpleObjectProperty<>(politicalParty);
    }
}
