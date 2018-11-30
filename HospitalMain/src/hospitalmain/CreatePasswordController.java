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
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class CreatePasswordController implements Initializable {

    @FXML
    private JFXPasswordField textPass, textComPass;
    @FXML
    private JFXCheckBox checkBox;
    @FXML
    private JFXTextField textUnMaskPassword, textUnMaskComfirmPass;
    private PreparedStatement ps;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Bind properties. Toggle textField and passwordField
        // visibility and managability properties mutually when checkbox's state is changed.
        // Because we want to display only one component (textField or passwordField)
        // on the scene at a time.
        textUnMaskPassword.managedProperty().bind(checkBox.selectedProperty());
        textUnMaskPassword.visibleProperty().bind(checkBox.selectedProperty());
        textUnMaskComfirmPass.managedProperty().bind(checkBox.selectedProperty());
        textUnMaskComfirmPass.visibleProperty().bind(checkBox.selectedProperty());

        textPass.managedProperty().bind(checkBox.selectedProperty().not());
        textPass.visibleProperty().bind(checkBox.selectedProperty().not());
        textComPass.managedProperty().bind(checkBox.selectedProperty().not());
        textComPass.visibleProperty().bind(checkBox.selectedProperty().not());
    }

    @FXML
    private void savePassword() {
        try {
            String password = textPass.getText();
            String confirmPass = textComPass.getText();

            if (password.isEmpty() || confirmPass.isEmpty() || !confirmPass.contentEquals(password)) {
                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "invalid password", textComPass.getScene().getWindow());
            } else {
                ps = HospitalDB.getCon().prepareStatement("Update HospitalLogin Set Password=?");
                ps.setString(1, confirmPass);
                ps.executeUpdate();

                HospitalDialog.dialog.close();
                HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Save Sucessful", textComPass.getScene().getWindow());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showOrHidePassword() {
        // Bind the textField and passwordField text values bidirectionally.
        textUnMaskPassword.textProperty().bindBidirectional(textPass.textProperty());
        textUnMaskComfirmPass.textProperty().bindBidirectional(textComPass.textProperty());
    }

}
