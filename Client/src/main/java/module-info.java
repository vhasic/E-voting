module ba.etf.elections.client {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires lombok;
    requires com.fasterxml.jackson.databind;

    opens ba.etf.elections.client to javafx.fxml;
    exports ba.etf.elections.client;
}