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
import hospitalbridge.HospitalBridge;
import hospitaldb.HospitalDB;
import hospitaldialog.HospitalDialog;
import java.awt.Toolkit;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;
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
public class EditPatientController implements Initializable {

    @FXML
    private JFXDatePicker date, admitDate;
    @FXML
    private ToggleGroup tg, tg1;
    @FXML
    private JFXRadioButton radioM, radioF, radioSingle, radioMar, radioDiv;
    @FXML
    private JFXTextField textPatientName, textPhone, textAddress, textCity, textDiag;
    @FXML
    private JFXComboBox comboPatientCat, comboDoctor, comboBlood;
    @FXML
    private ComboBox comboWard, comboBed, comboFloor, comboRoom;
    private List takenBedList, takenRoomList;
    private PreparedStatement ps;
    private ResultSet rs, rs2;
    private ChangeListener comboFloorChange, comboWardChange;
    private int i, selectedPatientID;
    private String selectedPatientName, patientName, phone, address, city, diagnosis, bedValue, roomValue;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            //Populate Patient Category Combo box
            comboPatientCat.getItems().addAll("In Patient", "Out Patient", "Discharge Patient");

            populateComboBoxs();

            /*
             When Ward OR Floor is Selected Populate Bed OR Room Combo Box
             Loop true Total Bed OR Room value
             And Add the Looped values to Bed OR Room Combo Box
             */
            comboWardChange = (ChangeListener) (ObservableValue observable, Object oldValue, Object newValue) -> {
                try {
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
                    //ps.close();

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
                    //ps.close();

                    //To make sure only a Room in a Floor is Assigned to a Patient
                    comboWard.getSelectionModel().selectedItemProperty().removeListener(comboWardChange);
                    comboWard.setValue("null");
                    comboBed.getItems().clear();
                    comboBed.setValue(0);
                    comboWard.getSelectionModel().selectedItemProperty().addListener(comboWardChange);
                } catch (Exception e) {//e.printStackTrace();
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
                    comboWard.setDisable(true);
                    comboBed.setDisable(true);
                    comboFloor.setDisable(true);
                    comboRoom.setDisable(true);

                    comboWard.setValue("null");
                    comboBed.setValue(0);
                    comboFloor.setValue("null");
                    comboRoom.setValue(0);
                } else {
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

        comboBlood.getItems().clear();
        ps = HospitalDB.getCon().prepareStatement("Select Name From Blood_Group");
        rs = ps.executeQuery();

        while (rs.next()) {
            comboBlood.getItems().add(rs.getString("Name"));
        }
        ps.close();

        comboWard.getItems().clear();
        ps = HospitalDB.getCon().prepareStatement("Select Ward From Ward");
        rs = ps.executeQuery();

        while (rs.next()) {
            comboWard.getItems().add(rs.getString("Ward"));
        }
        ps.close();

        comboFloor.getItems().clear();
        ps = HospitalDB.getCon().prepareStatement("Select Floor From Room");
        rs = ps.executeQuery();

        while (rs.next()) {
            comboFloor.getItems().add(rs.getString("Floor"));
        }
    }

    public void setTakenBedandRoomList(List takenBedList, List takenRoomList) {
        this.takenBedList = takenBedList;
        this.takenRoomList = takenRoomList;
    }

    public void setSelectedPatientNameAndID(String selectedPatientName, int selectedPatientID) throws SQLException {
        this.selectedPatientName = selectedPatientName;
        this.selectedPatientID = selectedPatientID;

        ps = HospitalDB.getCon().prepareStatement("Select * From Patient Where Patient_Name=? and ID=?");
        ps.setString(1, selectedPatientName);
        ps.setInt(2, selectedPatientID);
        rs2 = ps.executeQuery();

        if (rs2.next()) {
            textPatientName.setText(rs2.getString("Patient_Name"));
            admitDate.setValue(rs2.getDate("Admited_Date").toLocalDate());
            date.setValue(rs2.getDate("Date_Of_Birth").toLocalDate());

            if (rs2.getString("Gender").contentEquals("M")) {
                radioM.setSelected(true);
            } else {
                radioF.setSelected(true);
            }

            switch (rs2.getString("Marital_Status")) {
                case "Single":
                    radioSingle.setSelected(true);
                    break;
                case "Married":
                    radioMar.setSelected(true);
                    break;
                default:
                    radioDiv.setSelected(true);
                    break;
            }

            textAddress.setText(rs2.getString("Address"));
            textCity.setText(rs2.getString("City"));
            textPhone.setText(rs2.getString("Phone"));
            comboPatientCat.setValue(rs2.getString("Category"));
            comboWard.setValue(rs2.getString("Ward"));
            comboBed.setValue(rs2.getInt("Bed"));

            bedValue = (rs2.getInt("Bed") + "");

            comboFloor.setValue(rs2.getString("Floor"));
            comboRoom.setValue(rs2.getInt("Room"));

            roomValue = (rs2.getInt("Room") + "");

            textDiag.setText(rs2.getString("Diagnosis"));
            comboDoctor.setValue(rs2.getString("Doctor"));
            comboBlood.setValue(rs2.getString("Blood_Group"));
        }
        ps.close();
    }

    @FXML
    private void saveAction() {
        try {
            patientName = textPatientName.getText().trim();
            phone = textPhone.getText().trim();
            address = textAddress.getText().trim();
            city = textCity.getText().trim();
            diagnosis = textDiag.getText().trim();

            if (patientName.isEmpty() || phone.isEmpty() || address.isEmpty() || city.isEmpty() || diagnosis.isEmpty() || date.getValue() == null
                    || admitDate.getValue() == null || tg.getSelectedToggle() == null || tg1.getSelectedToggle() == null || comboPatientCat.getValue() == null
                    || comboBlood.getValue() == null || comboDoctor.getValue() == null || comboWard.getValue() == null || comboBed.getValue() == null
                    || comboFloor.getValue() == null || comboRoom.getValue() == null) {

                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "Please Fill All Requied Input", textAddress.getScene().getWindow());
            } else {
                ps = HospitalDB.getCon().prepareStatement("Update Patient Set Patient_Name=?,Admited_Date=?,Date_Of_Birth=?,Gender=?,Marital_Status=?,"
                        + "Address=?,City=?,Phone=?,Category=?,Ward=?,Bed=?,Floor=?,Room=?,Diagnosis=?,Doctor=?,Blood_Group=? Where Patient_Name=? and ID=?");
                ps.setString(1, patientName);
                ps.setDate(2, Date.valueOf(admitDate.getValue()));
                ps.setDate(3, Date.valueOf(date.getValue()));

                if (radioM.isSelected()) {
                    ps.setString(4, radioM.getText());
                } else {
                    ps.setString(4, radioF.getText());
                }

                if (radioSingle.isSelected()) {
                    ps.setString(5, radioSingle.getText());
                } else if (radioMar.isSelected()) {
                    ps.setString(5, radioMar.getText());
                } else if (radioDiv.isSelected()) {
                    ps.setString(5, radioDiv.getText());
                }

                ps.setString(6, address);
                ps.setString(7, city);
                ps.setString(8, phone);
                ps.setString(9, comboPatientCat.getValue().toString());
                ps.setString(10, comboWard.getValue().toString());
                ps.setInt(11, Integer.parseInt(comboBed.getValue().toString()));
                ps.setString(12, comboFloor.getValue().toString());
                ps.setInt(13, Integer.parseInt(comboRoom.getValue().toString()));
                ps.setString(14, diagnosis);
                ps.setString(15, comboDoctor.getValue().toString());
                ps.setString(16, comboBlood.getValue().toString());

                ps.setString(17, selectedPatientName);
                ps.setInt(18, selectedPatientID);
                ps.executeUpdate();
                ps.close();

                //Remove the Selected Taken Bed OR Room Value
                //And Replace it With the Inputed one
                if (!comboBed.getValue().equals(0)) {
                    takenBedList.remove(bedValue);
                    takenBedList.add(comboBed.getValue());
                } else {
                    takenRoomList.remove(roomValue);
                    takenRoomList.add(comboRoom.getValue());
                }

                //Save Discharge Patient Name and Date
                if (comboPatientCat.getValue().toString().contentEquals("Discharge Patient")) {
                    ps = HospitalDB.getCon().prepareStatement("Insert Into Discharged_Date Values(?,?)");
                    ps.setString(1, selectedPatientName);
                    ps.setDate(2, Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date())));
                    ps.executeUpdate();
                    
                    //Add Discharged Patient To Bill Module Combo Box
                    HospitalBridge.comboBoxBillPatientName.getItems().add(selectedPatientName);
                } else {
                    ps = HospitalDB.getCon().prepareStatement("Delete From Discharged_Date Where Patient_Name=?");
                    ps.setString(1, selectedPatientName);
                    ps.executeUpdate();
                    
                    //Remove Discharged Patient From Bill Module Combo Box
                    HospitalBridge.comboBoxBillPatientName.getItems().remove(selectedPatientName);
                }

                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "Save Successfull", textAddress.getScene().getWindow());

                HospitalDialog.dialog.close();
                textAddress.clear();
                textCity.clear();
                textDiag.clear();
                textPatientName.clear();
                textPhone.clear();
                tg.getSelectedToggle().setSelected(false);
                tg1.getSelectedToggle().setSelected(false);
                comboWard.setValue(null);
                comboFloor.setValue(null);
                comboDoctor.setValue(null);
                comboBlood.setValue(null);
                comboPatientCat.setValue("");
                date.setValue(null);
                admitDate.setValue(null);
            }
        } catch (Exception e) {
        }
    }

}
