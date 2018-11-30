/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalward;

import com.jfoenix.controls.JFXTextField;
import hospitaldialog.HospitalDialog;
import hospitaldb.HospitalDB;
import java.awt.Toolkit;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class AddWardController implements Initializable {

    @FXML
    private JFXTextField textWardName, textBeds;
    private ObservableList obList;
    private PreparedStatement ps;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Force the text field to be numeric only
        textBeds.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                textBeds.setText(newValue.replaceAll("[^\\d]", ""));
                Toolkit.getDefaultToolkit().beep();
            }
        });
    }

    public void setOblist(ObservableList obList) {
        this.obList = obList;
    }

    @FXML
    private void saveAction() {
        try {
            String ward = textWardName.getText().trim();
            String bed = textBeds.getText().trim();

            if (ward.isEmpty() || bed.isEmpty()) {
                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "Please Fill All Required Input", textBeds.getScene().getWindow());
            } else {
                ps = HospitalDB.getCon().prepareStatement("Insert Into Ward values(?,?)");
                ps.setString(1, ward);
                ps.setInt(2, Integer.parseInt(bed));
                ps.executeUpdate();

                obList.add(new Ward(ward, Integer.parseInt(bed)));
                ps.close();

                HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Save Successful", textBeds.getScene().getWindow());
                textWardName.clear();
                textBeds.clear();
            }
        } catch (Exception ex) {
        }
    }
    
}
