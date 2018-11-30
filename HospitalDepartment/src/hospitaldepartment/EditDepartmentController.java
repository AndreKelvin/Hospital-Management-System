/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitaldepartment;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hospitalbridge.HospitalBridge;
import hospitaldialog.HospitalDialog;
import hospitaldb.HospitalDB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class EditDepartmentController {

    @FXML
    private JFXTextField textDepart;
    @FXML
    private JFXTextArea textDescrip;
    private PreparedStatement ps;
    private ResultSet rs;
    private Department selectedValue;
    
    public void setSelectedValue(Department selectedValue) throws SQLException{
        this.selectedValue=selectedValue;
        
        ps=HospitalDB.getCon().prepareStatement("Select * From Department Where Name=?");
        ps.setString(1, selectedValue.getDepartment());
        rs=ps.executeQuery();
        
        while(rs.next()){
            textDepart.setText(rs.getString("Name"));
            textDescrip.setText(rs.getString("Description"));
        }
    }    
    
    @FXML
    private void saveAction() {
        try {
            String department=textDepart.getText().trim();
            String descrip=textDescrip.getText().trim();
            
            if (department.isEmpty()||descrip.isEmpty()) {
                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "Please Fill All Required Input", textDepart.getScene().getWindow());
            }
            else{
                ps=HospitalDB.getCon().prepareStatement("Update Department Set Name=?,Description=? Where Name=?");
                ps.setString(1, department);
                ps.setString(2, descrip);
                ps.setString(3, selectedValue.getDepartment());
                ps.executeUpdate();
                
                //Edit Department Combo Box Values in Doctor,Nurse,Pharmacist,LabTech Module 
                HospitalBridge.comboBoxDepartmentDoc.getItems().remove(selectedValue.getDepartment());
                HospitalBridge.comboBoxDepartmentNurse.getItems().remove(selectedValue.getDepartment());
                HospitalBridge.comboBoxDepartmentPhar.getItems().remove(selectedValue.getDepartment());
                HospitalBridge.comboBoxDepartmentLabTech.getItems().remove(selectedValue.getDepartment());
                
                HospitalBridge.comboBoxDepartmentDoc.getItems().add(department);
                HospitalBridge.comboBoxDepartmentNurse.getItems().add(department);
                HospitalBridge.comboBoxDepartmentPhar.getItems().add(department);
                HospitalBridge.comboBoxDepartmentLabTech.getItems().add(department);
                
                selectedValue.setDepartment(department);
                selectedValue.setDescription(descrip);
                
                HospitalDialog.dialog.close();
                HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Save Successful", textDepart.getScene().getWindow());
                
            }
        } catch (Exception ex) {
        }
    }
}
