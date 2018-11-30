/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalblooddonor;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import hospitaldialog.HospitalDialog;
import hospitaldb.HospitalDB;
import java.awt.Toolkit;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class AddDonorController implements Initializable {

    @FXML
    private JFXDatePicker datePickerBirth;
    @FXML
    private JFXRadioButton radioM, radioF;
    @FXML
    private ToggleGroup tg;
    @FXML
    private JFXComboBox comboBloodGroup;
    @FXML
    private JFXDatePicker datePickerDonation;
    @FXML
    private JFXTextField textPhone, textEmail, textAddress, textName;
    private PreparedStatement ps;
    private ResultSet rs;
    private ObservableList obList;
    private String name, phone, email, address, selectedGender, bloodGroup, dateDonation;
    private byte age;
    private Calendar calender;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            calender = new GregorianCalendar();

            populateBloodGroupComboBox();

            //Force the text field to be numeric only
            textPhone.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (!newValue.matches("\\d*")) {
                    textPhone.setText(newValue.replaceAll("[^\\d]", ""));
                    Toolkit.getDefaultToolkit().beep();
                }
            });
        } catch (Exception e) {
        }
    }

    public void populateBloodGroupComboBox() throws SQLException {
        ps = HospitalDB.getCon().prepareStatement("Select Name From Blood_Group");
        rs = ps.executeQuery();

        comboBloodGroup.getItems().clear();
        while (rs.next()) {
            comboBloodGroup.getItems().add(rs.getString("Name"));
        }
    }

    public void setObservableList(ObservableList obList) {
        this.obList = obList;
    }

    @FXML
    private void save() {
        try {
            name = textName.getText().trim();
            address = textAddress.getText().trim();
            phone = textPhone.getText().trim();
            email = textEmail.getText().trim();

            if (name.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty() || comboBloodGroup.getValue() == null || datePickerBirth.getValue() == null
                    || datePickerDonation.getValue() == null || tg.getSelectedToggle() == null) {
                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "Please Fill All Required Input", textAddress.getScene().getWindow());
            } else {
                bloodGroup = comboBloodGroup.getValue().toString();
                age = (byte) (calender.get(Calendar.YEAR) - datePickerBirth.getValue().getYear());
                dateDonation = datePickerDonation.getValue().toString();

                ps = HospitalDB.getCon().prepareStatement("Insert Into Blood_Donor (Name,Age,Gender,Blood_Group,Donation_Date,Phone,Email,Address) "
                        + "Values(?,?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, name);
                ps.setDate(2, Date.valueOf(datePickerBirth.getValue()));

                if (radioM.isSelected()) {
                    ps.setString(3, radioM.getText());
                    selectedGender = radioM.getText();
                } else {
                    ps.setString(3, radioF.getText());
                    selectedGender = radioF.getText();
                }

                ps.setString(4, bloodGroup);
                ps.setDate(5, Date.valueOf(datePickerDonation.getValue()));
                ps.setString(6, phone);
                ps.setString(7, email);
                ps.setString(8, address);
                ps.executeUpdate();

                //Get Auto Generated ID and Display it with the newly inserted data in the table view 
                rs = ps.getGeneratedKeys();
                rs.next();

                obList.add(new BloodDonor(rs.getInt(1), name, age+"", selectedGender, bloodGroup, dateDonation, phone, email, address));

                HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Save Successful", textAddress.getScene().getWindow());

                textAddress.clear();
                textEmail.clear();
                textName.clear();
                textPhone.clear();
                comboBloodGroup.setValue(null);
                datePickerBirth.setValue(null);
                datePickerDonation.setValue(null);
                tg.getSelectedToggle().setSelected(false);
            }
        } catch (Exception e) {
        }
    }

}
