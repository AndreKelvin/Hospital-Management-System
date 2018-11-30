/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalbirth;

import com.jfoenix.controls.JFXComboBox;
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class EditBirthController implements Initializable {

    @FXML
    private JFXTextField textName, textMName, textFName, textWeight;
    @FXML
    private JFXRadioButton radioM, radioF;
    @FXML
    private ToggleGroup tg1;
    @FXML
    private JFXDatePicker datePicker;
    @FXML
    private JFXTimePicker TimePicker;
    @FXML
    private JFXComboBox comboDoc, comboBGroup;
    private PreparedStatement ps;
    private ResultSet rs;
    private String selectedBaby,baby,mother,father,weight;;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            populateComboBox();

            TimePicker.setEditable(false);
        } catch (Exception e) {
        }
    }
    
    public void populateComboBox() throws SQLException {
        ps = HospitalDB.getCon().prepareStatement("Select Name From Doctor");
        rs = ps.executeQuery();

        comboDoc.getItems().clear();
        while (rs.next()) {
            comboDoc.getItems().add(rs.getString("Name"));
        }

        ps = HospitalDB.getCon().prepareStatement("Select Name From Blood_Group");
        rs = ps.executeQuery();

        comboBGroup.getItems().clear();
        while (rs.next()) {
            comboBGroup.getItems().add(rs.getString("Name"));
        }
    }

    /**
     * Display the selected Baby
     * Details to its component
     * @param selectedBaby
     * @throws java.sql.SQLException
    */
    public void setSelectedBaby(String selectedBaby) throws SQLException {
        this.selectedBaby = selectedBaby;

        ps = HospitalDB.getCon().prepareStatement("Select * From Birth Where Baby=?");
        ps.setString(1, selectedBaby);
        rs = ps.executeQuery();

        if (rs.next()) {
            textName.setText(rs.getString("Baby"));
            textMName.setText(rs.getString("Mother"));
            textFName.setText(rs.getString("Father"));
            datePicker.setValue(rs.getDate("Date").toLocalDate());
            TimePicker.setValue(LocalTime.parse(rs.getString("Time"),DateTimeFormatter.ofPattern("hh:mm a")));
            comboDoc.setValue(rs.getString("Doctor"));
            
            if (rs.getString("Gender").contentEquals("M")) {
                radioM.setSelected(true);
            }else{
                radioF.setSelected(true);
            }
            
            textWeight.setText(rs.getString("Weight"));
            comboBGroup.setValue(rs.getString("Blood_Group"));
        }
    }

    @FXML
    private void save() {
        try {
            baby=textName.getText().trim();
            mother=textMName.getText().trim();
            father=textFName.getText().trim();
            weight=textWeight.getText().trim();
            
            if (baby.isEmpty()||mother.isEmpty()||father.isEmpty()||weight.isEmpty()||comboBGroup.getValue()==null||comboDoc.getValue()==null
                    ||datePicker.getValue()==null||TimePicker.getValue()==null||tg1.getSelectedToggle()==null) {
                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "Please Fiil All Required Input", textFName.getScene().getWindow());
            }
            else{
                ps=HospitalDB.getCon().prepareStatement("Update Birth Set Baby=?,Mother=?,Father=?,Date=?,Time=?,Doctor=?,Gender=?,Weight=?,Blood_Group=? Where Baby=?");
                ps.setString(1, baby);
                ps.setString(2, mother);
                ps.setString(3, father);
                ps.setDate(4, Date.valueOf(datePicker.getValue()));
                ps.setString(5, TimePicker.getValue().format(DateTimeFormatter.ofPattern("hh:mm a")));
                ps.setString(6, comboDoc.getValue().toString());
                
                if (radioM.isSelected()) {
                    ps.setString(7, "M");
                }else{
                    ps.setString(7, "F");
                }
                
                ps.setString(8, weight);
                ps.setString(9, comboBGroup.getValue().toString());
                ps.setString(10, selectedBaby);
                ps.executeUpdate();
                
                HospitalDialog.dialog.close();
                HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Save Successfull", textFName.getScene().getWindow());
                
                textFName.clear();
                textMName.clear();
                textName.clear();
                textName.clear();
                textWeight.clear();
                comboBGroup.setValue(null);
                comboDoc.setValue(null);
                datePicker.setValue(null);
                TimePicker.setValue(null);
                tg1.getSelectedToggle().setSelected(false);
            }
        } catch (Exception e) {e.printStackTrace();
        }
    }

}
