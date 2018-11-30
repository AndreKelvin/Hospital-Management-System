/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalpatient;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import hospitaldb.HospitalDB;
import hospitaldialog.HospitalDialog;
import java.awt.Toolkit;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.LogManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class AddPatientController implements Initializable {

    @FXML
    private JFXDatePicker datePicker;
    @FXML
    private ToggleGroup tg, tg1;
    @FXML
    private JFXRadioButton radioM, radioF, radioSingle, radioMar, radioDiv;
    @FXML
    private JFXTextField textPatientName, textPhone, textAdmit, textAddress, textCity, textDiagnosis;
    @FXML
    private JFXComboBox comboPatientCat, comboDoctor, comboBlood;
    @FXML
    private ComboBox comboWard, comboBed, comboFloor, comboRoom;
    private PreparedStatement ps;
    private ResultSet rs;
    private int i;
    private List takenBedList, takenRoomList;
    private String patientName, phone, address, city, diagnosis;
    private ChangeListener comboFloorChange, comboWardChange;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            //Populate Patient Category Combo box
            comboPatientCat.getItems().addAll("In Patient", "Out Patient");

            populateComboBoxs();

            SimpleDateFormat date = new SimpleDateFormat("Y-MM-d");
            textAdmit.setText(date.format(new Date()));

            /*
             When Ward OR Floor is Selected Populate Bed OR Room Combo Box
             Loop true Total Bed OR Room value
             And Add the Looped values to Bed OR Room Combo Box
             */
            comboWardChange = (ChangeListener) (ObservableValue observable, Object oldValue, Object newValue) -> {
                try {
                    //LogManager.getLogManager().reset();
                    comboBed.getItems().clear();

                    ps = HospitalDB.getCon().prepareStatement("Select Beds From Ward Where Ward=?");
                    ps.setString(1, newValue.toString());
                    rs = ps.executeQuery();

                    if (rs.next()) {

                        for (i = 1; i <= rs.getInt("Beds"); i++) {

                            //Filter Taken Beds out of Availabel Beds
                            if (!takenBedList.contains(i + "")) {
                                comboBed.getItems().add(i);
                            }
                        }
                        i = 0;
                    }
                    ps.close();

                    //To make sure only a Bed in a Ward is Assigned to a Patient
                    comboFloor.getSelectionModel().selectedItemProperty().removeListener(comboFloorChange);
                    comboFloor.setValue("null");
                    comboRoom.getItems().clear();
                    comboRoom.setValue(0);
                    comboFloor.getSelectionModel().selectedItemProperty().addListener(comboFloorChange);
                } catch (Exception e) {
                }
            };

            comboFloorChange = (ChangeListener) (ObservableValue observable, Object oldValue, Object newValue) -> {
                try {
                    //LogManager.getLogManager().reset();
                    comboRoom.getItems().clear();

                    ps = HospitalDB.getCon().prepareStatement("Select Room From Room Where Floor=?");
                    ps.setString(1, newValue.toString());
                    rs = ps.executeQuery();

                    if (rs.next()) {

                        for (i = 1; i <= rs.getInt("Room"); i++) {

                            //Filter Taken Rooms out of Availabel Rooms
                            if (!takenRoomList.contains(i + "")) {
                                comboRoom.getItems().add(i);
                            }
                        }
                        i = 0;
                    }
                    ps.close();

                    //To make sure only a Room in a Floor is Assigned to a Patient
                    comboWard.getSelectionModel().selectedItemProperty().removeListener(comboWardChange);
                    comboWard.setValue("null");
                    comboBed.getItems().clear();
                    comboBed.setValue(0);
                    comboWard.getSelectionModel().selectedItemProperty().addListener(comboWardChange);
                } catch (Exception e) {
                }
            };

            comboWard.getSelectionModel().selectedItemProperty().addListener(comboWardChange);
            comboFloor.getSelectionModel().selectedItemProperty().addListener(comboFloorChange);

            //Force the Textfield to be Numeric only
            textPhone.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (!newValue.matches("\\d*")) {
                    textPhone.setText(newValue.replaceAll("[^\\d]", ""));
                    Toolkit.getDefaultToolkit().beep();
                }
            });

            //Only In Patient will be Assign to a Ward,Bed and Floor,Room
            comboPatientCat.getSelectionModel().selectedItemProperty().addListener((ObservableValue observable, Object oldValue, Object newValue) -> {
                
                if (!newValue.equals("In Patient")) {
                    System.out.println("Not equal to In Patient");
                    comboWard.setDisable(true);
                    comboBed.setDisable(true);
                    comboFloor.setDisable(true);
                    comboRoom.setDisable(true);

                    comboWard.setValue("null");
                    comboBed.setValue(0);
                    comboFloor.setValue("null");
                    comboRoom.setValue(0);
                } else {
                    System.out.println("Equal to In Patient");
                    comboWard.setDisable(false);
                    comboBed.setDisable(false);
                    comboFloor.setDisable(false);
                    comboRoom.setDisable(false);

                    comboWard.setValue(null);
                    comboBed.setValue(null);
                    comboFloor.setValue(null);
                    comboRoom.setValue(null);
                }
            });
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
        ps.close();

        ps = HospitalDB.getCon().prepareStatement("Select Name From Blood_Group");
        rs = ps.executeQuery();

        comboBlood.getItems().clear();
        while (rs.next()) {
            comboBlood.getItems().add(rs.getString("Name"));
        }
        ps.close();

        ps = HospitalDB.getCon().prepareStatement("Select Ward From Ward");
        rs = ps.executeQuery();

        comboWard.getItems().clear();
        while (rs.next()) {
            comboWard.getItems().add(rs.getString("Ward"));
        }
        ps.close();

        ps = HospitalDB.getCon().prepareStatement("Select Floor From Room");
        rs = ps.executeQuery();

        comboFloor.getItems().clear();
        while (rs.next()) {
            comboFloor.getItems().add(rs.getString("Floor"));
        }
        ps.close();
    }

    public void setTakenBedandRoomList(List takenBedList, List takenRoomList) {
        this.takenBedList = takenBedList;
        this.takenRoomList = takenRoomList;
    }

    @FXML
    private void saveAction() {
        try {
            patientName = textPatientName.getText().trim();
            phone = textPhone.getText().trim();
            address = textAddress.getText().trim();
            city = textCity.getText().trim();
            diagnosis = textDiagnosis.getText().trim();

            if (patientName.isEmpty() || phone.isEmpty() || address.isEmpty() || city.isEmpty() || diagnosis.isEmpty() || datePicker.getValue() == null
                    || tg.getSelectedToggle() == null || tg1.getSelectedToggle() == null || comboBlood.getValue() == null || comboDoctor.getValue() == null
                    || comboWard.getValue() == null || comboBed.getValue() == null || comboFloor.getValue() == null || comboRoom.getValue() == null
                    || comboPatientCat.getValue() == null) {

                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "Please Fill All Requied Input", textAddress.getScene().getWindow());
            } else {
                ps = HospitalDB.getCon().prepareStatement("Insert Into Patient (Patient_Name,Admited_Date,Date_Of_Birth,Gender,Marital_Status,Address,City,"
                        + "Phone,Category,Ward,Bed,Room,Floor,Diagnosis,Doctor,Blood_Group) Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                ps.setString(1, patientName);
                ps.setDate(2, java.sql.Date.valueOf(textAdmit.getText()));
                ps.setDate(3, java.sql.Date.valueOf(datePicker.getValue()));

                if (radioM.isSelected()) {
                    ps.setString(4, radioM.getText());
                } else {
                    ps.setString(4, radioF.getText());
                }

                if (radioSingle.isSelected()) {
                    ps.setString(5, radioSingle.getText());
                } else if (radioMar.isSelected()) {
                    ps.setString(5, radioMar.getText());
                } else {
                    ps.setString(5, radioDiv.getText());
                }

                ps.setString(6, address);
                ps.setString(7, city);
                ps.setString(8, phone);
                ps.setString(9, comboPatientCat.getValue().toString());
                ps.setString(10, comboWard.getValue().toString());
                ps.setString(11, comboBed.getValue().toString());
                ps.setString(12, comboRoom.getValue().toString());
                ps.setString(13, comboFloor.getValue().toString());
                ps.setString(14, diagnosis);
                ps.setString(15, comboDoctor.getValue().toString());
                ps.setString(16, comboBlood.getValue().toString());
                ps.executeUpdate();

                //Add the Choosen Bed or Room to taken List
                if (!comboBed.getValue().equals(0)) {
                    takenBedList.add(comboBed.getValue() + "");
                } else {
                    takenRoomList.add(comboRoom.getValue() + "");
                }

                HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Save Successfull", textAddress.getScene().getWindow());

                textAddress.clear();
                textCity.clear();
                textDiagnosis.clear();
                textPatientName.clear();
                textPhone.clear();
                tg.getSelectedToggle().setSelected(false);
                tg1.getSelectedToggle().setSelected(false);
                comboWard.setValue(null);
                comboFloor.setValue(null);
                comboDoctor.setValue(null);
                comboBlood.setValue(null);
                comboPatientCat.setValue("");
                datePicker.setValue(null);
            }
        } catch (Exception e) {
        }
    }

}
