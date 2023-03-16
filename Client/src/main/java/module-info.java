module ba.etf.elections.client {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires lombok;
    requires com.fasterxml.jackson.databind;
    requires itextpdf;
    requires javafx.swing;
    requires java.transaction;
    requires java.naming;

    opens ba.etf.elections.client to javafx.fxml;
    exports ba.etf.elections.client;
}