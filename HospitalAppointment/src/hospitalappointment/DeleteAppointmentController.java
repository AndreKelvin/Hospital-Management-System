/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalappointment;

import com.jfoenix.controls.JFXButton;
import hospitaldialog.HospitalDialog;
import hospitaldb.HospitalDB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class DeleteAppointmentController {

    @FXML
    private JFXButton buttonYes;
    private PreparedStatement ps;
    private ResultSet rs;
    private ObservableList obList;
    private Appointment selectedAppointment;
    
    public void setSelectedAppointment(Appointment selectedAppointment) {
        this.selectedAppointment = selectedAppointment;
    }

    public void setObservableList(ObservableList obList) {
        this.obList = obList;
    }

    @FXML
    private void yesAction() {
        try {
            ps = HospitalDB.getCon().prepareStatement("Delete From Appointment Where Patient=?");
            ps.setString(1, selectedAppointment.getPatient());
            ps.executeUpdate();
            ps.close();
            
            ps = HospitalDB.getCon().prepareStatement("Delete From Notification Where Patient=? And Doctor=?");
            ps.setString(1, selectedAppointment.getPatient());
            ps.setString(2, selectedAppointment.getDoctor());
            ps.executeUpdate();

            obList.clear();

            ps = HospitalDB.getCon().prepareStatement("Select * From Appointment");
            rs = ps.executeQuery();

            while (rs.next()) {
                obList.add(new Appointment(rs.getString("Doctor"), rs.getString("Patient"), rs.getString("Date"), rs.getString("Start"), rs.getString("Ending"), rs.getString("Status")));
            }

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
