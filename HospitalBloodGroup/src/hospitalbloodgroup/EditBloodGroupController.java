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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class EditBloodGroupController implements Initializable {

    @FXML
    private JFXTextField textBlood,textBags;
    private PreparedStatement ps;
    private ResultSet rs;
    private BloodGroup selectedValue;
    
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
    
    public void setSelectedValue(BloodGroup selectedValue) throws SQLException{
        this.selectedValue=selectedValue;
        
        ps=HospitalDB.getCon().prepareStatement("Select * From Blood_Group Where Name=?");
        ps.setString(1, selectedValue.getBlood());
        rs=ps.executeQuery();
        
        while(rs.next()){
            textBlood.setText(rs.getString("Name"));
            textBags.setText(rs.getString("Bags"));
        }
        ps.close();
    }
    
    @FXML
    private void saveAction() {
        try {
            String blood=textBlood.getText().trim();
            String bags=textBags.getText().trim();
            
            if (blood.isEmpty()||bags.isEmpty()) {
                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "Please Fiil All Required Input", textBags.getScene().getWindow());
            }
            else{
                ps=HospitalDB.getCon().prepareStatement("Update Blood_Group Set Name=?,Bags=? Where Name=?");
                ps.setString(1, blood);
                ps.setInt(2, Integer.parseInt(bags));
                ps.setString(3, selectedValue.getBlood());
                ps.executeUpdate();
                ps.close();
                
                selectedValue.setBlood(blood);
                selectedValue.setBags(Integer.parseInt(bags));
                
                HospitalDialog.dialog.close();
                
                HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Save Successful", textBags.getScene().getWindow());
            }
        } catch (Exception ex) {
        }
    }
}
