/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalpatienttreatment;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import hospitaldialog.HospitalDialog;
import hospitaldb.HospitalDB;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import org.controlsfx.control.CheckListView;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class EditPatientTreatmentController implements Initializable {

    @FXML
    private JFXTextField textPatName, textDiagnosis;
    @FXML
    private JFXComboBox comboTreat;
    @FXML
    private CheckListView checkListView;
    @FXML
    private ListView listView;
    private PreparedStatement ps;
    private ResultSet rs;
    private byte loopListValue;
    private int value;
    private PatientTreatment selectedPatient;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            value = 0;

            populateTreatmentComboBox();

            populateCheckListView();

            //Display Selected/Checked Items on List View
            checkListView.getCheckModel().getCheckedItems().addListener((ListChangeListener.Change c) -> {
                listView.getItems().clear();
                while (value < c.getList().size()) {
                    listView.getItems().add(checkListView.getCheckModel().getCheckedItems().get(value));
                    value++;
                }
                value = 0;
            });

        } catch (Exception e) {
        }
    }
    
    public void populateTreatmentComboBox() throws SQLException {
        ps = HospitalDB.getCon().prepareStatement("Select * From Treatment");
        rs = ps.executeQuery();
        
        comboTreat.getItems().clear();
        while (rs.next()) {
            comboTreat.getItems().add(rs.getString("Treatment_Name"));
        }
        ps.close();
    }

    public void populateCheckListView() throws SQLException {
        ps = HospitalDB.getCon().prepareStatement("Select Name From Drug");
        rs = ps.executeQuery();
        
        checkListView.getItems().clear();
        while (rs.next()) {
            checkListView.getItems().add(rs.getString("Name"));
        }
    }

    public void setSelectedPatient(PatientTreatment selectedPatient) {
        try {
            this.selectedPatient=selectedPatient;
            
            textPatName.setText(selectedPatient.getPatientName());
            textDiagnosis.setText(selectedPatient.getDiagnosis());

            listView.getItems().clear();
            checkListView.getCheckModel().clearChecks();
            
            ps = HospitalDB.getCon().prepareStatement("Select Drugs From Patient_Treatment Where Patient_Name=?");
            ps.setString(1, selectedPatient.getPatientName());
            rs=ps.executeQuery();
            
            while(rs.next()){
                checkListView.getCheckModel().check(rs.getString("Drugs"));
            }
    
        } catch (Exception e) {
        }
    }

    public void setPatientTreatmentName(String patientTreatmentName) {
        comboTreat.setValue(patientTreatmentName);
    }

    @FXML
    private void save() {
        try {

            if (comboTreat.getValue() == null || listView.getItems().isEmpty()) {
                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "Please Fill All Required Input", textDiagnosis.getScene().getWindow());
            } else {
                /*
                  Delete the Selected Values from Database
                  Before inputing new values
                  Because using the "Update" Statement in a Loop will always
                  Update the Database value with the Last Looped value
                */
                ps = HospitalDB.getCon().prepareStatement("Delete From Patient_Treatment Where Patient_Name=?");
                ps.setString(1, selectedPatient.getPatientName());
                ps.executeUpdate();
                
                loopListValue = 0;
                while (listView.getItems().size() > loopListValue) {
                    ps = HospitalDB.getCon().prepareStatement("Insert Into Patient_Treatment Values(?,?,?,?)");
                    ps.setString(1, textPatName.getText());
                    ps.setString(2, textDiagnosis.getText());
                    ps.setString(3, comboTreat.getValue().toString());
                    ps.setString(4, listView.getItems().get(loopListValue).toString());
                    ps.executeUpdate();

                    loopListValue++;
                }

                HospitalDialog.dialog.close();
                HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Save Successful", textDiagnosis.getScene().getWindow());
            }
        } catch (Exception e) {
        }
    }

}
