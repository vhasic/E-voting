/*
 * Copyright (c) 2023. Vahidin Hasić
 */

package ba.etf.elections.client;

import ba.etf.elections.client.helper.CommonFunctions;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

public class ConfirmationController {
    public PasswordField passwordField;
    public Button buttonConfirm;
    private Boolean actionConfirmed = false;

    @FXML
    protected void initialize() {
        buttonConfirm.setOnAction(event -> {
            String password = passwordField.getText();
            String systemPassword = CommonFunctions.getEnvironmentVariable("systemPassword");

            // Compare input password with the hashed password given in environment variable
            if (BCrypt.checkpw(password, systemPassword)) {
                actionConfirmed = true;
                // close the window
                Stage stage = (Stage) passwordField.getScene().getWindow();
                try {
                    stage.close();
                } catch (Exception e) {
                    // if window is already closed, ignore exception
                }
            } else {
                actionConfirmed = false;
                // Show the error alert
                Alert alert = CommonFunctions.createAlert("Greška", "Unesena je pogrešna lozinka", "Unesite ispravnu lozinku", Alert.AlertType.ERROR);
                alert.showAndWait();
            }
        });
    }

    public Boolean isActionConfirmed() {
        return actionConfirmed;
    }
}
