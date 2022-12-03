package ba.etf.elections.models;

import javafx.beans.property.SimpleObjectProperty;
import lombok.Data;

@Data
public class Ballot {
    private int id;
    private int ballotNumber;
    private SimpleObjectProperty<Candidate> candidate;
    private SimpleObjectProperty<PoliticalParty> politicalParty;

    public Ballot() {
        this.candidate = new SimpleObjectProperty<>();
        this.politicalParty = new SimpleObjectProperty<>();
    }

    public Ballot(int id, int ballotNumber, Candidate candidate, PoliticalParty politicalParty) {
        this.id = id;
        this.ballotNumber = ballotNumber;
        this.candidate = new SimpleObjectProperty<>(candidate);
        this.politicalParty = new SimpleObjectProperty<>(politicalParty);
    }
}

/*@Data
public class Ballot {
    private int id;
    private int ballotNumber;
    private SimpleListProperty<Candidate> candidate;
    private SimpleListProperty<PoliticalParty> politicalParty;

    public Ballot() {
        this.candidate = new SimpleListProperty<>();
        this.politicalParty = new SimpleListProperty<>();
    }

    public Ballot(int id, int ballotNumber, ObservableList<Candidate> candidate, ObservableList<PoliticalParty> politicalParty) {
        this.id = id;
        this.ballotNumber = ballotNumber;
        this.candidate = new SimpleListProperty<>(candidate);
        this.politicalParty = new SimpleListProperty<>(politicalParty);
    }
}*/