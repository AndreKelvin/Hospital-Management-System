/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalpatient;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.events.JFXDialogEvent;
import hospitaldb.HospitalDB;
import hospitaldialog.HospitalDialog;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class PatientTreatmentInfoController {

    @FXML
    private Label labelPatID, labelPatName, labelDiagnosis, labelTreatment;
    @FXML
    private JFXListView listViewDrugs;
    @FXML
    private StackPane stackPane;
    @FXML
    private JFXButton buttonAdd;
    private PreparedStatement ps;
    private ResultSet rs;
    private FXMLLoader loaderAdd;
    private Parent rootAdd;
    private AddPatientTreatmentController add;
    private String selectedPatient;

    public void initializeValues(String selectedPatient, String diagnosis, String patientID) throws SQLException {
        labelPatName.setText(selectedPatient);
        labelDiagnosis.setText(diagnosis);
        this.labelPatID.setText(patientID);
        this.selectedPatient = selectedPatient;

        listViewDrugs.getItems().clear();
        labelTreatment.setText("null");

        displayPatientTreatmentDetails();
    }

    private void displayPatientTreatmentDetails() {
        try {
            ps = HospitalDB.getCon().prepareStatement("Select Treatment_Name,Drugs From Patient_Treatment Where Patient_Name=?");
            ps.setString(1, selectedPatient);
            rs = ps.executeQuery();

            while (rs.next()) {
                labelTreatment.setText(rs.getString("Treatment_Name"));
                listViewDrugs.getItems().add(rs.getString("Drugs"));
            }

            if (listViewDrugs.getItems().isEmpty()) {
                buttonAdd.setDisable(false);
            } else {
                buttonAdd.setDisable(true);
            }
        } catch (Exception e) {
        }
    }

    @FXML
    private void addPatientTreatment() {
        try {
            if (loaderAdd == null) {
                loaderAdd = new FXMLLoader(getClass().getResource("AddPatientTreatment.fxml"));
                rootAdd = loaderAdd.load();

                add = loaderAdd.getController();
            }
            add.setSelectedPatientAndDiagnosis(labelPatName.getText(), labelDiagnosis.getText());

            HospitalDialog.showDialog(stackPane, rootAdd);

            HospitalDialog.dialog.setOnDialogClosed((JFXDialogEvent event) -> {
                displayPatientTreatmentDetails();
            });
        } catch (Exception e) {
        }
    }
    
}
