/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalbloodgroup;

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
public class AddBloodGroupController implements Initializable {

    @FXML
    private JFXTextField textBlood,textBags;
    private ObservableList obList;
    private PreparedStatement ps;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Force the text field to be numeric only
        textBags.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                textBags.setText(newValue.replaceAll("[^\\d]", ""));
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
            String blood = textBlood.getText().trim();
            String bags = textBags.getText().trim();

            if (blood.isEmpty() || bags.isEmpty()) {
                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "Please Fiil All Required Input", textBags.getScene().getWindow());
            } 
            else {
                ps = HospitalDB.getCon().prepareStatement("Insert Into Blood_Group values(?,?)");
                ps.setString(1, blood);
                ps.setInt(2, Integer.parseInt(bags));
                ps.executeUpdate();

                obList.add(new BloodGroup(blood, Integer.parseInt(bags)));
                ps.close();

                HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Save Successful", textBags.getScene().getWindow());
                
                textBlood.clear();
                textBags.clear();
            }
        } catch (Exception ex) {
        }
    }
}
