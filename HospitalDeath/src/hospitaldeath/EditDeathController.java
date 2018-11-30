/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitaldeath;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import hospitaldb.HospitalDB;
import hospitaldialog.HospitalDialog;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class EditDeathController implements Initializable {
    
    @FXML
    private JFXDatePicker datePicker;
    @FXML
    private JFXTimePicker TimePicker;
    @FXML
    private JFXTextField textDeath,textid,textPatientName;
    @FXML
    private JFXRadioButton radioYes,radioNo;
    @FXML
    private ToggleGroup tg;
    private PreparedStatement ps;
    private ResultSet rs,rs2;
    private String selectedDeadPatient,patient,death;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            //Display Patient ID when its Selected
            textPatientName.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                try {
                    ps=HospitalDB.getCon().prepareStatement("Select ID From Patient Where Patient_Name=?");
                    ps.setString(1, newValue);
                    rs=ps.executeQuery();
                    
                    if (rs.next()) {
                        textid.setText(rs.getString("ID"));
                    }
                } catch (Exception e) {
                }
            });
            
            TimePicker.setEditable(false);
            
        } catch (Exception e) {
        }
    }
    
    /**
     * Display the selected Dead Patient
     * Details to its component
     * @param selectedDeadPatient
     * @throws java.sql.SQLException
    */
    public void getSelectedDeadPatient(String selectedDeadPatient) throws SQLException{
        this.selectedDeadPatient=selectedDeadPatient;
        
        ps = HospitalDB.getCon().prepareStatement("Select * From Death Where Name=?");
        ps.setString(1, selectedDeadPatient);
        rs2=ps.executeQuery();
        
        if(rs2.next()){
            textid.setText(rs2.getString("ID"));
            textPatientName.setText(rs2.getString("Name"));
            datePicker.setValue(rs2.getDate("Date").toLocalDate());
            TimePicker.setValue(LocalTime.parse(rs2.getString("Time"),DateTimeFormatter.ofPattern("hh:mm a")));
            textDeath.setText(rs2.getString("Course_Of_Death"));
            
            if (rs2.getString("Treatment").contentEquals("Yes")) {
                radioYes.setSelected(true);
            }else{
                radioNo.setSelected(true);
            }
        }
    }
    
    @FXML
    private void save() {
        try {
            patient=textPatientName.getText();
            death=textDeath.getText();
            
            if (patient.isEmpty()||datePicker.getValue()==null||TimePicker.getValue()==null||death.isEmpty()||tg.getSelectedToggle()==null) {
                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "Please Fiil All Required Input", textDeath.getScene().getWindow());
            }
            else{
                ps=HospitalDB.getCon().prepareStatement("Update Death Set ID=?,Name=?,Date=?,Time=?,Course_Of_Death=?,Treatment=? Where Name=?");
                if (textid.getText().isEmpty()) {
                    ps.setString(1, "null");
                }else{
                    ps.setString(1, textid.getText().trim());
                }
                ps.setString(2, patient);
                ps.setDate(3, Date.valueOf(datePicker.getValue()));
                ps.setString(4, TimePicker.getValue().format(DateTimeFormatter.ofPattern("hh:mm a")));
                ps.setString(5, death);
                
                if (radioYes.isSelected()) {
                    ps.setString(6, radioYes.getText());
                }else{
                     ps.setString(6, radioNo.getText());
                }
                ps.setString(7, selectedDeadPatient);
                ps.executeUpdate();
                
                HospitalDialog.dialog.close();
                HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Save Successfull", textDeath.getScene().getWindow());
                
                textid.clear();
                textPatientName.clear();
                datePicker.setValue(null);
                TimePicker.setValue(null);
                textDeath.clear();
                tg.getSelectedToggle().setSelected(false);
            }
        } catch (Exception e) {e.printStackTrace();
        }
    }
    
}
