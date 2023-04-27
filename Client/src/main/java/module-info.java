module ba.etf.elections.client {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.fasterxml.jackson.databind;
    requires itextpdf;
    requires javafx.swing;
    requires java.naming;
    requires jbcrypt;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;

    opens ba.etf.elections.client to javafx.fxml;
    exports ba.etf.elections.client;
}