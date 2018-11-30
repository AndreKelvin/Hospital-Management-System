/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalappointment;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import hospitaldialog.HospitalDialog;
import hospitaldb.HospitalDB;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
public class EditAppointmentController implements Initializable {

    @FXML
    private JFXComboBox comboDoctor, comboPatient;
    @FXML
    private JFXDatePicker datePicker;
    @FXML
    private JFXTimePicker timePickerFrom, timePickerTo;
    @FXML
    private JFXCheckBox checkBoxPending, checkBoxDone;
    private PreparedStatement ps;
    private ResultSet rs;
    private Appointment selectedAppointment;
    private String doc,patient,date,from,to,checkDone,checkPending,format;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            populateComboBoxs();
            
            //Manually Apply a Toggle Group to the Check Boxes
            checkBoxPending.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (newValue) {
                    checkBoxDone.setSelected(false);
                }
            });
            
            checkBoxDone.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (newValue) {
                    checkBoxPending.setSelected(false);
                }
            });

            timePickerFrom.setEditable(false);
            timePickerTo.setEditable(false);
        } catch (Exception e) {
        }
    }
    
    public void populateComboBoxs() throws SQLException {
        ps = HospitalDB.getCon().prepareStatement("Select Name From Doctor");
        rs = ps.executeQuery();

        comboDoctor.getItems().clear();
        while (rs.next()) {
            comboDoctor.getItems().add(rs.getString("Name"));
        }

        ps = HospitalDB.getCon().prepareStatement("Select Patient_Name From Patient");
        rs = ps.executeQuery();

        comboPatient.getItems().clear();
        while (rs.next()) {
            comboPatient.getItems().add(rs.getString("Patient_Name"));
        }
    }

    public void setSelectedAppointment(Appointment selectedAppointment) throws SQLException {
        this.selectedAppointment = selectedAppointment;

        ps = HospitalDB.getCon().prepareStatement("Select * From Appointment Where Patient=?");
        ps.setString(1, selectedAppointment.getPatient());
        rs = ps.executeQuery();

        while (rs.next()) {
            comboDoctor.setValue(rs.getString("Doctor"));
            comboPatient.setValue(rs.getString("Patient"));
            datePicker.setValue(LocalDate.parse(rs.getString("Date"),DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            timePickerFrom.setValue(LocalTime.parse(rs.getString("Start"),DateTimeFormatter.ofPattern("hh:mm a")));
            timePickerTo.setValue(LocalTime.parse(rs.getString("Ending"),DateTimeFormatter.ofPattern("hh:mm a")));
            
            if (rs.getString("Status").contentEquals("Pending")) {
                checkBoxPending.setSelected(true);
            }else{
                checkBoxDone.setSelected(true);
            }
        }
    }

    @FXML
    private void save() {
        try {
            if (comboDoctor.getValue()==null||comboPatient.getValue()==null||datePicker.getValue()==null||timePickerFrom.getValue()==null||timePickerTo.getValue()==null) {
                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "Please Fill All Required Input", comboDoctor.getScene().getWindow());
            }
            else{
                doc = comboDoctor.getValue().toString();
                patient = comboPatient.getValue().toString();
                date = datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                from = timePickerFrom.getValue().format(DateTimeFormatter.ofPattern("hh:mm a"));
                format=timePickerFrom.getValue().format(DateTimeFormatter.ofPattern("hh:mm:ss a"));
                to = timePickerTo.getValue().format(DateTimeFormatter.ofPattern("hh:mm a"));
                
                ps = HospitalDB.getCon().prepareStatement("Update Appointment Set Doctor=?,Patient=?,Date=?,Start=?,Ending=?,Status=? Where Patient=?");
                ps.setString(1, doc);
                ps.setString(2, patient);
                ps.setString(3, date);
                ps.setString(4, from);
                ps.setString(5, to);
                
                if (checkBoxPending.isSelected()) {
                    checkPending=checkBoxPending.getText();
                    ps.setString(6, checkPending);
                    selectedAppointment.setStatus(checkPending);
                }else{
                    checkDone=checkBoxDone.getText();
                    ps.setString(6, checkDone);
                    selectedAppointment.setStatus(checkDone);
                }
                ps.setString(7, selectedAppointment.getPatient());
                ps.executeUpdate();
                ps.close();
                
                ps = HospitalDB.getCon().prepareStatement("Update Notification Set Reminder=?,Doctor=?,Patient=? Where Patient=? And Doctor=?");
                ps.setString(1, date+" "+format);
                ps.setString(2, doc);
                ps.setString(3, patient);
                ps.setString(4, selectedAppointment.getPatient());
                ps.setString(5, selectedAppointment.getDoctor());
                ps.executeUpdate();
                
                selectedAppointment.setDoctor(doc);
                selectedAppointment.setPatient(patient);
                selectedAppointment.setDate(date);
                selectedAppointment.setFrom(from);
                selectedAppointment.setTo(to);
                
                HospitalDialog.dialog.close();
                HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Save Successfull", comboDoctor.getScene().getWindow());
                
                comboDoctor.setValue(null);
                comboPatient.setValue(null);
                datePicker.setValue(null);
                timePickerFrom.setValue(null);
                timePickerTo.setValue(null);
                checkBoxDone.setSelected(false);
                checkBoxPending.setSelected(false);
            }
        } catch (Exception e) {
        }
    }

}
