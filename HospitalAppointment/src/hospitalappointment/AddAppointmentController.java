/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalappointment;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import hospitaldialog.HospitalDialog;
import hospitaldb.HospitalDB;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class AddAppointmentController implements Initializable {

    @FXML
    private JFXComboBox comboDoctor, comboPatient;
    @FXML
    private JFXDatePicker datePicker;
    @FXML
    private JFXTimePicker timePickerFrom, timePickerTo;
    private PreparedStatement ps;
    private ResultSet rs;
    private ObservableList obList;
    private String doc, patient, date, from, to, format;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            populateComboBoxs();

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

    public void setObervableList(ObservableList obList) {
        this.obList = obList;
    }

    @FXML
    private void save() {
        try {
            if (comboDoctor.getValue() == null || comboPatient.getValue() == null || datePicker.getValue() == null || timePickerFrom.getValue() == null || timePickerTo.getValue() == null) {
                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "Please Fill All Required Input", comboDoctor.getScene().getWindow());
            } else {
                doc = comboDoctor.getValue().toString();
                patient = comboPatient.getValue().toString();
                date = datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                from = timePickerFrom.getValue().format(DateTimeFormatter.ofPattern("hh:mm a"));
                format = timePickerFrom.getValue().format(DateTimeFormatter.ofPattern("hh:mm:ss a"));
                to = timePickerTo.getValue().format(DateTimeFormatter.ofPattern("hh:mm a"));

                ps = HospitalDB.getCon().prepareStatement("Insert Into Appointment Values(?,?,?,?,?,?)");
                ps.setString(1, doc);
                ps.setString(2, patient);
                ps.setString(3, date);
                ps.setString(4, from);
                ps.setString(5, to);
                ps.setString(6, "Pending");
                ps.executeUpdate();
                ps.close();

                obList.add(new Appointment(doc, patient, date, from, to, "Pending"));

                ps = HospitalDB.getCon().prepareStatement("Insert Into Notification Values(?,?,?)");
                ps.setString(1, date + " " + format);
                ps.setString(2, doc);
                ps.setString(3, patient);
                ps.executeUpdate();

                HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Save Successfull", comboDoctor.getScene().getWindow());

                comboDoctor.setValue(null);
                comboPatient.setValue(null);
                datePicker.setValue(null);
                timePickerFrom.setValue(null);
                timePickerTo.setValue(null);
            }
        } catch (Exception e) {
        }
    }
}
