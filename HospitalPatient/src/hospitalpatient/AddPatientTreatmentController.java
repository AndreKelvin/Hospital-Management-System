/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalpatient;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import hospitaldb.HospitalDB;
import hospitaldialog.HospitalDialog;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
public class AddPatientTreatmentController implements Initializable {

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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            value=0;
            
            //Populate Treatment Combo Box
            ps = HospitalDB.getCon().prepareStatement("Select * From Treatment");
            rs = ps.executeQuery();

            while (rs.next()) {
                comboTreat.getItems().add(rs.getString("Treatment_Name"));
            }
            ps.close();

            //Populate Check List View
            ps = HospitalDB.getCon().prepareStatement("Select Name From Drug");
            rs = ps.executeQuery();

            while (rs.next()) {
                checkListView.getItems().add(rs.getString("Name"));
            }
            
            //Display Selected/Checked Items on List View
            checkListView.getCheckModel().getCheckedItems().addListener((ListChangeListener.Change c) -> {
                listView.getItems().clear();
                while(value<c.getList().size()){
                    listView.getItems().add(checkListView.getCheckModel().getCheckedItems().get(value));
                    value++;
                }value=0;
            });
            
        } catch (Exception e) {
        }
    }  
    
    public void setSelectedPatientAndDiagnosis(String selectedPatient, String diagnosis) {
        textPatName.setText(selectedPatient);
        textDiagnosis.setText(diagnosis);
    }

    @FXML
    private void save() {
        try {

            if (comboTreat.getValue() == null || listView.getItems().isEmpty()) {
                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "Please Fill All Required Input", textDiagnosis.getScene().getWindow());
            } else {
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

                comboTreat.setValue(null);
                listView.getItems().clear();
                checkListView.getCheckModel().clearChecks();
            }
        } catch (Exception e) {
        }
    }
    
}
