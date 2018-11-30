/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalmain;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import hospitaldb.HospitalDB;
import hospitaldialog.HospitalDialog;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class ChangePasswordController implements Initializable {

    @FXML
    private JFXPasswordField textOldPass, textNewPass, textComNewPass;
    @FXML
    private JFXCheckBox checkBox;
    @FXML
    private JFXTextField textUnMaskOldPass, textUnMaskNewPass, textUnMaskComfirmPass;
    private PreparedStatement ps;
    private ResultSet rs;
    private String dbPassword;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Bind properties. Toggle textField and passwordField
        // visibility and managability properties mutually when checkbox's state is changed.
        // Because we want to display only one component (textField or passwordField)
        // on the scene at a time.
        textUnMaskOldPass.managedProperty().bind(checkBox.selectedProperty());
        textUnMaskOldPass.visibleProperty().bind(checkBox.selectedProperty());

        textUnMaskComfirmPass.managedProperty().bind(checkBox.selectedProperty());
        textUnMaskComfirmPass.visibleProperty().bind(checkBox.selectedProperty());

        textUnMaskNewPass.managedProperty().bind(checkBox.selectedProperty());
        textUnMaskNewPass.visibleProperty().bind(checkBox.selectedProperty());

        textOldPass.managedProperty().bind(checkBox.selectedProperty().not());
        textOldPass.visibleProperty().bind(checkBox.selectedProperty().not());

        textNewPass.managedProperty().bind(checkBox.selectedProperty().not());
        textNewPass.visibleProperty().bind(checkBox.selectedProperty().not());

        textComNewPass.managedProperty().bind(checkBox.selectedProperty().not());
        textComNewPass.visibleProperty().bind(checkBox.selectedProperty().not());
    }

    private String getPasswordValue() throws SQLException {
        ps = HospitalDB.getCon().prepareStatement("Select * from HospitalLogin");
        rs = ps.executeQuery();

        if (rs.next()) {
            dbPassword = rs.getString("Password");
        }
        return dbPassword;
    }

    @FXML
    private void saveNewPassword() {
        try {
            String newPassword = textNewPass.getText();
            String confirmPassword = textComNewPass.getText();
            String oldPassword = textOldPass.getText();

            if (confirmPassword.isEmpty() || oldPassword.isEmpty() || newPassword.isEmpty() || !confirmPassword.contentEquals(newPassword) || !getPasswordValue().contentEquals(oldPassword)) {
                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "invalid password", textOldPass.getScene().getWindow());
            } else {
                ps = HospitalDB.getCon().prepareStatement("Update HospitalLogin Set Password=?");
                ps.setString(1, confirmPassword);
                ps.executeUpdate();

                HospitalDialog.dialog.close();
                HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Save Sucessful", textOldPass.getScene().getWindow());
            }
        } catch (Exception e) {
        }
    }

    @FXML
    private void showOrHidePassword() {
        // Bind the textField and passwordField text values bidirectionally.
        textUnMaskComfirmPass.textProperty().bindBidirectional(textComNewPass.textProperty());
        textUnMaskNewPass.textProperty().bindBidirectional(textNewPass.textProperty());
        textUnMaskOldPass.textProperty().bindBidirectional(textOldPass.textProperty());
    }

}
