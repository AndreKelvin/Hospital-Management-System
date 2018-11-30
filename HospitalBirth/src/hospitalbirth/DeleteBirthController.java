/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalbirth;

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
public class DeleteBirthController {

    @FXML
    private Label label;
    private PreparedStatement ps;
    private String selectedBaby;
    
    public void setSelectedBaby(String selectedBaby) {
        this.selectedBaby=selectedBaby;
        label.setText("Are you sure you want to Delete "+selectedBaby+"?");
    }    

    @FXML
    private void yesAction() {
        try {
            ps=HospitalDB.getCon().prepareStatement("Delete From Birth Where Baby=?");
            ps.setString(1, selectedBaby);
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
