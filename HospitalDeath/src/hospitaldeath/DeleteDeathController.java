/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitaldeath;

import hospitaldb.HospitalDB;
import hospitaldialog.HospitalDialog;
import java.sql.PreparedStatement;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class DeleteDeathController {

    @FXML
    private Label label;
    private PreparedStatement ps;
    private String selectedDeadPatient;

    public void getSelectedDeadPatient(String selectedDeadPatient) {
        this.selectedDeadPatient=selectedDeadPatient;
        label.setText("Are you sure you want to Delete "+selectedDeadPatient+"?");
    }    
    
    @FXML
    private void yesAction() {
        try {
            ps=HospitalDB.getCon().prepareStatement("Delete From Death Where Name=?");
            ps.setString(1, selectedDeadPatient);
            ps.executeUpdate();
            
            HospitalDialog.dialog.close();
            HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Delete Successfull", label.getScene().getWindow());
        } catch (Exception e) {
        }
    }
    
    @FXML
    private void noAction() {
        HospitalDialog.dialog.close();
    }
    
}
