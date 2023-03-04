module ba.etf.elections.core {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires lombok;

    opens ba.etf.elections.core to javafx.fxml;
    exports ba.etf.elections.core;
    exports ba.etf.elections.core.controllers;
    opens ba.etf.elections.core.controllers to javafx.fxml;
    exports ba.etf.elections.core.dao;
    opens ba.etf.elections.core.dao to javafx.fxml;
}