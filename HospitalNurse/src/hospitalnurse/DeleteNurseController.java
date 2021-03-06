/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalnurse;

import hospitaldb.HospitalDB;
import hospitaldialog.HospitalDialog;
import hospitalstaff.DeleteStaff;
import java.io.File;
import java.sql.PreparedStatement;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class DeleteNurseController /*extends DeleteStaff*/ {
    
//    public DeleteNurseController(){
//        super.setTableName("Nurse");
//    }

    @FXML
    private Label label;
    private String selectedNurseName;
    private int selectedNurseID;
    private PreparedStatement ps;
    private File nurseImageFile;
    private Label labelName, labelPhone, labelCity, labelAddress, labelMatStatus, labelGender, labelAge, labelEmail, labelID, labelDateJoined, labelSalary, labelDegree;
    private ImageView imageView;
    private Image defaultImage;

    public void setSelectedNurseName(String selectedNurseName) {
        this.selectedNurseName = selectedNurseName;
        label.setText("Are your sure you want to Delete " + selectedNurseName + "?");
    }

    public void setNurseImageFile(File nurseImageFile) {
        this.nurseImageFile = nurseImageFile;
    }

    public void setNurseID(int selectedNurseID) {
        this.selectedNurseID = selectedNurseID;
    }
    
    public void setLabelName(Label labelName) {
        this.labelName = labelName;
    }

    public void setLabelPhone(Label labelPhone) {
        this.labelPhone = labelPhone;
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

    public void setLabelEmail(Label labelEmail) {
        this.labelEmail = labelEmail;
    }

    public void setLabelID(Label labelID) {
        this.labelID = labelID;
    }

    public void setLabelDateJoined(Label labelDateJoined) {
        this.labelDateJoined = labelDateJoined;
    }

    public void setLabelSalary(Label labelSalary) {
        this.labelSalary = labelSalary;
    }

    public void setLabelDegree(Label labelDegree) {
        this.labelDegree = labelDegree;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setDefaultImage(Image defaultImage) {
        this.defaultImage = defaultImage;
    }

    @FXML
    private void yesAction() {
        try {
            ps = HospitalDB.getCon().prepareStatement("Delete From Nurse Where Name=? and ID=?");
            ps.setString(1, selectedNurseName);
            ps.setInt(2, selectedNurseID);
            ps.executeUpdate();

            //Delete the Selected Nurse Image
            nurseImageFile.delete();

            HospitalDialog.dialog.close();

            //Empty All Info
            labelID.setText("");
            labelName.setText("");
            labelAge.setText("");
            labelGender.setText("");
            labelAddress.setText("");
            labelCity.setText("");
            labelPhone.setText("");
            labelMatStatus.setText("");
            labelDateJoined.setText("");
            labelSalary.setText("");
            labelEmail.setText("");
            labelDegree.setText("");
            imageView.setImage(defaultImage);

            HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Delete Succesful", label.getScene().getWindow());
        } catch (Exception e) {
        }
    }

    @FXML
    private void noAction() {
        HospitalDialog.dialog.close();
    }
    
}
