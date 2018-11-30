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
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class AddDepartmentController {

    @FXML
    private JFXTextField textDepart;
    @FXML
    private JFXTextArea textDescrip;
    private ObservableList obList;
    private PreparedStatement ps;
    private String department;
    private String descrip;
    
    public void setOblist(ObservableList obList) {
        this.obList = obList;
    }    
    
    @FXML
    private void saveAction() {
        try {
            department = textDepart.getText().trim();
            descrip = textDescrip.getText().trim();

            if (department.isEmpty() || descrip.isEmpty()) {
                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "Please Fill All Required Input", textDepart.getScene().getWindow());
            } else {
                ps = HospitalDB.getCon().prepareStatement("Insert Into Department Values(?,?)");
                ps.setString(1, department);
                ps.setString(2, descrip);
                ps.executeUpdate();

                obList.add(new Department(department, descrip));

                HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Save Successful", textDepart.getScene().getWindow());
                
                //Add Department Combo Box Values in Doctor,Nurse,Pharmacist,LabTech Module
                HospitalBridge.comboBoxDepartmentDoc.getItems().add(department);
                HospitalBridge.comboBoxDepartmentNurse.getItems().add(department);
                HospitalBridge.comboBoxDepartmentPhar.getItems().add(department);
                HospitalBridge.comboBoxDepartmentLabTech.getItems().add(department);
                
                textDepart.clear();
                textDescrip.clear();
            }
        } catch (Exception ex) {
        }
    }
}
