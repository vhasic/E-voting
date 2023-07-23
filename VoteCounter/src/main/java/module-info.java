module ba.etf.elections.votecounter.votecounter {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens ba.etf.elections.voteCounter to javafx.fxml;
    exports ba.etf.elections.voteCounter;
}
