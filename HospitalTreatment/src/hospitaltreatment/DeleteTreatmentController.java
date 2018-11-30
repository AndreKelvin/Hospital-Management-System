/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitaltreatment;

import hospitaldialog.HospitalDialog;
import hospitaldb.HospitalDB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class DeleteTreatmentController {

    @FXML
    private Label label;
    private PreparedStatement ps;
    private ResultSet rs;
    private Treatment selectedTreatment;
    private ObservableList obList;

    public void setselectedTreatment(Treatment selectedTreatment) throws SQLException {
        this.selectedTreatment = selectedTreatment;
        label.setText("Are you sure you want to Delete " + selectedTreatment.getTreatment() + "?");
    }
    
    public void setObList(ObservableList obList){
        this.obList=obList;
    }

    @FXML
    private void yesAction() {
        try {
            ps = HospitalDB.getCon().prepareStatement("Delete From Treatment Where Treatment_Name=?");
            ps.setString(1, selectedTreatment.getTreatment());
            ps.executeUpdate();
            
            HospitalDialog.dialog.close();
            HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Delete Successful", label.getScene().getWindow());
            
            //refresh table
            obList.clear();
            ps = HospitalDB.getCon().prepareStatement("Select * From Treatment");
            rs=ps.executeQuery();
            
            while(rs.next()){
                obList.add(new Treatment(rs.getString("Treatment_Name"), rs.getInt("Price")));
            }
        } catch (Exception e) {
        }
    }

    @FXML
    private void noAction() {
        HospitalDialog.dialog.close();
    }

}
