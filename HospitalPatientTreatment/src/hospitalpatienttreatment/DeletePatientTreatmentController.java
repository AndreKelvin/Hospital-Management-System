/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalpatienttreatment;

import com.jfoenix.controls.JFXButton;
import hospitaldialog.HospitalDialog;
import hospitaldb.HospitalDB;
import java.sql.PreparedStatement;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class DeletePatientTreatmentController {

    @FXML
    private JFXButton buttonYes;
    private PatientTreatment selectedPatient;
    private PreparedStatement ps;

    public void setSelectedPatient(PatientTreatment selectedPatient) {
        this.selectedPatient = selectedPatient;
    }

    @FXML
    private void yesAction() {
        try {
            ps = HospitalDB.getCon().prepareStatement("Delete From Patient_Treatment Where Patient_Name=?");
            ps.setString(1, selectedPatient.getPatientName());
            ps.executeUpdate();
            
            HospitalDialog.dialog.close();
            HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Delete Successful", buttonYes.getScene().getWindow());
        } catch (Exception e) {
        }
    }

    @FXML
    private void noAction() {
        HospitalDialog.dialog.close();
    }

}
