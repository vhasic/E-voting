module ba.etf.elections {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires lombok;

    opens ba.etf.elections to javafx.fxml;
    exports ba.etf.elections;
    exports ba.etf.elections.controllers;
    opens ba.etf.elections.controllers to javafx.fxml;
}