/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitaldepartment;

import com.jfoenix.controls.JFXButton;
import hospitalbridge.HospitalBridge;
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
public class DeleteDepartmentController {

    @FXML
    private JFXButton buttonYes;
    private PreparedStatement ps;
    private ObservableList obList;
    private ResultSet rs;
    private Department selectedValue;
    
    public void setSelectedValue(Department selectedValue) throws SQLException {
        this.selectedValue = selectedValue;
    }
    
    public void setObList(ObservableList obList){
        this.obList=obList;
    }

    @FXML
    private void yesAction() {
        try {
            ps = HospitalDB.getCon().prepareStatement("Delete From Department Where Name=?");
            ps.setString(1, selectedValue.getDepartment());
            ps.executeUpdate();

            HospitalDialog.dialog.close();
            HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Delete Successful", buttonYes.getScene().getWindow());
            
            //Delete Department ComboBox Values in Doctor,Nurse,Pharmacist,LabTech Module 
            HospitalBridge.comboBoxDepartmentDoc.getItems().remove(selectedValue.getDepartment());
            HospitalBridge.comboBoxDepartmentNurse.getItems().remove(selectedValue.getDepartment());
            HospitalBridge.comboBoxDepartmentPhar.getItems().remove(selectedValue.getDepartment());
            HospitalBridge.comboBoxDepartmentLabTech.getItems().remove(selectedValue.getDepartment());
            
            //refresh table
            obList.clear();
            ps=HospitalDB.getCon().prepareStatement("Select * From Department");
            rs=ps.executeQuery();
            
            while(rs.next()){
                obList.add(new Department(rs.getString("Name"), rs.getString("Description")));
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
