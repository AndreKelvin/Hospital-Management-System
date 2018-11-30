/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalpatient;

import hospitalbridge.HospitalBridge;
import hospitaldb.HospitalDB;
import hospitaldialog.HospitalDialog;
import java.sql.PreparedStatement;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class DeletePatientController {

    @FXML
    private Label label;
    private List takenBedList,takenRoomList;
    private PreparedStatement ps;
    private String selectedPatientName; 
    private int selectedPatientID;
    private Label labelCity, labelAddress, labelMatStatus, labelGender, labelAge, labelDiagnosis, labelRoom, labelFloor, labelBed, labelBloodG, labelDoctor;
    private Label labelID, labelName, labelAdmit, labelWard, labelPhone;
    private String bedValue,roomValue;

    public void setTakenBedandRoomList(List takenBedList, List takenRoomList){
        this.takenBedList=takenBedList;
        this.takenRoomList=takenRoomList;
    }
    
    public void setSelectedPatientName(String selectedPatientName) {
        this.selectedPatientName = selectedPatientName;
        label.setText("Are your sure you want to Delete " + selectedPatientName + "?");
    }
    
    public void setPatientID(int selectedPatientID) {
        this.selectedPatientID = selectedPatientID;
    }

    public void setLabelCity(Label labelCity) {
        this.labelCity = labelCity;
    }

    public void setLabelAddress(Label labelAddress) {
        this.labelAddress = labelAddress;
    }

    public void setLabelMatStatus(Label labelMatStatus) {
        this.labelMatStatus = labelMatStatus;
    }

    public void setLabelGender(Label labelGender) {
        this.labelGender = labelGender;
    }

    public void setLabelAge(Label labelAge) {
        this.labelAge = labelAge;
    }

    public void setLabelDiagnosis(Label labelDiagnosis) {
        this.labelDiagnosis = labelDiagnosis;
    }

    public void setLabelRoom(Label labelRoom) {
        this.labelRoom = labelRoom;
    }

    public void setLabelFloor(Label labelFloor) {
        this.labelFloor = labelFloor;
    }

    public void setLabelBed(Label labelBed) {
        this.labelBed = labelBed;
    }

    public void setLabelBloodG(Label labelBloodG) {
        this.labelBloodG = labelBloodG;
    }

    public void setLabelDoctor(Label labelDoctor) {
        this.labelDoctor = labelDoctor;
    }

    public void setLabelID(Label labelID) {
        this.labelID = labelID;
    }

    public void setLabelName(Label labelName) {
        this.labelName = labelName;
    }

    public void setLabelAdmit(Label labelAdmit) {
        this.labelAdmit = labelAdmit;
    }

    public void setLabelWard(Label labelWard) {
        this.labelWard = labelWard;
    }

    public void setLabelPhone(Label labelPhone) {
        this.labelPhone = labelPhone;
    }
    
    
    public void setSelectedBedOrRoomValue(String bedValue,String roomValue){
        this.bedValue=bedValue;
        this.roomValue=roomValue;
    }
    
    @FXML
    private void yesAction() {
        try {
            ps = HospitalDB.getCon().prepareStatement("Delete From Patient Where Patient_Name=? and ID=?");
            ps.setString(1, selectedPatientName);
            ps.setInt(2, selectedPatientID);
            ps.executeUpdate();
            
            //Delete the Selected Patient Bed or Room Value
            takenBedList.remove(bedValue);
            takenRoomList.remove(roomValue);
            
            ps=HospitalDB.getCon().prepareStatement("Delete From Discharged_Date Where Patient_Name=?");
            ps.setString(1, selectedPatientName);
            ps.executeUpdate();
            
            //Remove Discharged Patient From Bill Module Combo Box
            HospitalBridge.comboBoxBillPatientName.getItems().remove(selectedPatientName);
            
            HospitalDialog.dialog.close();
            
            labelCity.setText("");
            labelAddress.setText("");
            labelMatStatus.setText("");
            labelGender.setText("");
            labelAge.setText("");
            labelDiagnosis.setText("");
            labelRoom.setText("");
            labelFloor.setText("");
            labelBloodG.setText("");
            labelDoctor.setText("");
            labelID.setText("");
            labelName.setText("");
            labelAdmit.setText("");
            labelWard.setText("");
            labelPhone.setText("");
            labelBed.setText("");
            
            HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Delete Succesful", label.getScene().getWindow());
        } catch (Exception e) {
        }
    }
    
    @FXML
    private void noAction() {
        HospitalDialog.dialog.close();
    }
    
}
