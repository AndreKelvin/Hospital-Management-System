/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalblooddonor;

import com.jfoenix.controls.JFXButton;
import hospitaldialog.HospitalDialog;
import hospitaldb.HospitalDB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class DeleteDonorController {

    @FXML
    private JFXButton buttonYes;
    private BloodDonor selectedDonor;
    private PreparedStatement ps;
    private ResultSet rs;
    private ObservableList obList;
    private byte age;
    private Calendar calender;

    public void setSelectedDonor(BloodDonor selectedDonor) throws SQLException {
        this.selectedDonor = selectedDonor;
    }

    public void setObservableList(ObservableList obList) {
        this.obList = obList;
        calender = new GregorianCalendar();
    }

    @FXML
    private void yesAction() {
        try {
            ps = HospitalDB.getCon().prepareStatement("Delete From Blood_Donor Where Name=?");
            ps.setString(1, selectedDonor.getName());
            ps.executeUpdate();

            obList.clear();

            ps = HospitalDB.getCon().prepareStatement("Select * From Blood_Donor");
            rs = ps.executeQuery();

            while (rs.next()) {
                age = (byte) (calender.get(Calendar.YEAR) - rs.getDate("Age").toLocalDate().getYear());
                obList.add(new BloodDonor(rs.getInt("Id"), rs.getString("Name"), age + "", rs.getString("Gender"), rs.getString("Blood_Group"), rs.getString("Donation_Date"),
                        rs.getString("Phone"), rs.getString("Email"), rs.getString("Address")));
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
