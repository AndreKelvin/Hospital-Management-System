/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalbloodgroup;

import com.jfoenix.controls.JFXButton;
import hospitaldialog.HospitalDialog;
import hospitaldb.HospitalDB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class DeleteBloodGroupController {

    @FXML
    private JFXButton buttonYes;
    private PreparedStatement ps;
    private ObservableList obList;
    private ResultSet rs;
    private BloodGroup selectedValue;

    public void setSelectedValue(BloodGroup selectedValue) throws SQLException {
        this.selectedValue = selectedValue;
    }
    
    public void setObList(ObservableList obList){
        this.obList=obList;
    }
    
    @FXML
    private void yesAction() {
        try {
            ps = HospitalDB.getCon().prepareStatement("Delete From Blood_Group Where Name=?");
            ps.setString(1, selectedValue.getBlood());
            ps.executeUpdate();

            HospitalDialog.dialog.close();
            HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Delete Successful", buttonYes.getScene().getWindow());
            
            //refresh table
            obList.clear();
            ps=HospitalDB.getCon().prepareStatement("Select * From Blood_Group");
            rs=ps.executeQuery();
            
            while(rs.next()){
                obList.add(new BloodGroup(rs.getString("Name"), Integer.parseInt(rs.getString("Bags"))));
            }
            ps.close();
        } catch (Exception e) {
        }
    }
   
    @FXML
    private void noAction() {
        HospitalDialog.dialog.close();
    }
}
