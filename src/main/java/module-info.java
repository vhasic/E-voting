module ba.etf.elections {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens ba.etf.elections to javafx.fxml;
    exports ba.etf.elections;
}