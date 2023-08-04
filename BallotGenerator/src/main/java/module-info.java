module ba.etf.elections.ballotGenerator {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens ba.etf.elections.ballotGenerator to javafx.fxml;
    exports ba.etf.elections.ballotGenerator;
    exports ba.etf.elections.ballotGenerator.helper;
}
