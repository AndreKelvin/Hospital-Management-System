/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalpatienttreatment;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import hospitaldialog.HospitalDialog;
import hospitaldb.HospitalDB;
import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Andre Kelvin
 */
public class HospitalPatientTreatmentController implements Initializable {

    @FXML
    private StackPane stackPane;
    @FXML
    private JFXTextField textSearch, textPatTreat;
    @FXML
    private JFXListView listView;
    @FXML
    private JFXButton buttonAdd, buttonEdit, buttonDelete;
    @FXML
    private TableView<PatientTreatment> tableView;
    @FXML
    private TableColumn columnPatientName, columnDiagnosis;
    private PreparedStatement ps;
    private ResultSet rs;
    private ObservableList obList;
    private List searchList;
    private SuggestionProvider suggestion;
    private PatientTreatment selectedPatient;
    private FXMLLoader loaderAdd, loaderEdit, loaderDelete;
    private Parent rootAdd, rootEdit, rootDelete;
    private AddPatientTreatmentController add;
    private EditPatientTreatmentController edit;
    private DeletePatientTreatmentController del;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            searchList = new ArrayList();
            suggestion = SuggestionProvider.create(searchList);
            AutoCompletionTextFieldBinding auto = new AutoCompletionTextFieldBinding<>(textSearch, suggestion);

            buttonAdd.setDisable(true);
            buttonDelete.setDisable(true);
            buttonEdit.setDisable(true);

            obList = FXCollections.observableArrayList();
            tableView.setItems(obList);

            columnPatientName.setCellValueFactory(new PropertyValueFactory("patientName"));
            columnDiagnosis.setCellValueFactory(new PropertyValueFactory("diagnosis"));

            populateTableView();

            suggestion.addPossibleSuggestions(searchList);

            //Select the Searched Patient
            textSearch.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                tableView.getItems().stream()
                        .filter(item -> (item.getPatientName() == null ? newValue == null : item.getPatientName().equals(newValue)))
                        .findAny().ifPresent(item -> {
                            tableView.getSelectionModel().select(item);
                            //tableView.scrollTo(item);
                        });
            });

            //Display Patient Treatment Details
            tableView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends PatientTreatment> observable, PatientTreatment oldValue, PatientTreatment newValue) -> {
                try {
                    selectedPatient = newValue;

                    displayPatientTreatmentDetails();

                    buttonAdd.setDisable(false);
                    buttonDelete.setDisable(false);
                    buttonEdit.setDisable(false);

                    if (!textPatTreat.getText().isEmpty()) {
                        buttonAdd.setDisable(true);
                    }

                    textSearch.clear();
                } catch (Exception e) {
                }
            });

            tableView.getFocusModel().focus(-1);

        } catch (Exception e) {
        }
    }

    private void displayPatientTreatmentDetails() {
        try {
            textPatTreat.clear();
            listView.getItems().clear();
            ps = HospitalDB.getCon().prepareStatement("Select Treatment_Name,Drugs From Patient_Treatment Where Patient_Name=?");
            ps.setString(1, selectedPatient.getPatientName());
            rs = ps.executeQuery();

            while (rs.next()) {
                textPatTreat.setText(rs.getString("Treatment_Name"));
                listView.getItems().add(rs.getString("Drugs"));
            }
        } catch (Exception e) {
        }
    }

    /**
     * Populate Table with Patient Name(In and Out) and Diagnosis
     * @throws SQLException 
     */
    private void populateTableView() throws SQLException {
        ps = HospitalDB.getCon().prepareStatement("Select Patient_Name,Diagnosis From Patient Where Category In(?,?)");
        ps.setString(1, "In Patient");
        ps.setString(2, "Out Patient");
        rs = ps.executeQuery();

        obList.clear();
        while (rs.next()) {
            obList.add(new PatientTreatment(rs.getString("Patient_Name"), rs.getString("Diagnosis")));
            searchList.add(rs.getString("Patient_Name"));
        }
    }
    
    @FXML
    private void refreshTable() {
        try {
            populateTableView();
        } catch (Exception e) {
        }
    }

    @FXML
    private void addAction() {
        try {
            if (loaderAdd == null) {
                loaderAdd = new FXMLLoader(getClass().getResource("AddPatientTreatment.fxml"));
                rootAdd = loaderAdd.load();

                add = loaderAdd.getController();
            }
            add.setSelectedPatient(selectedPatient);
            add.populateTreatmentComboBox();
            add.populateCheckListView();

            HospitalDialog.showDialog(stackPane, rootAdd);

            HospitalDialog.dialog.setOnDialogClosed((JFXDialogEvent event) -> {
                displayPatientTreatmentDetails();

                buttonAdd.setDisable(true);
                buttonEdit.setDisable(true);
                buttonDelete.setDisable(true);
            });
        } catch (Exception e) {
        }
    }

    @FXML
    private void editAction() {
        try {
            if (loaderEdit == null) {
                loaderEdit = new FXMLLoader(getClass().getResource("EditPatientTreatment.fxml"));
                rootEdit = loaderEdit.load();

                edit = loaderEdit.getController();
            }
            edit.populateTreatmentComboBox();
            edit.populateCheckListView();
            edit.setSelectedPatient(selectedPatient);
            edit.setPatientTreatmentName(textPatTreat.getText());

            HospitalDialog.showDialog(stackPane, rootEdit);

            HospitalDialog.dialog.setOnDialogClosed((JFXDialogEvent event) -> {
                displayPatientTreatmentDetails();

                buttonAdd.setDisable(true);
                buttonEdit.setDisable(true);
                buttonDelete.setDisable(true);
            });
        } catch (Exception e) {
        }
    }

    @FXML
    private void deleteAction() {
        try {
            if (loaderDelete == null) {
                loaderDelete = new FXMLLoader(getClass().getResource("DeletePatientTreatment.fxml"));
                rootDelete = loaderDelete.load();

                del = loaderDelete.getController();
            }
            del.setSelectedPatient(selectedPatient);

            HospitalDialog.showDialog(stackPane, rootDelete);

            HospitalDialog.dialog.setOnDialogClosed((JFXDialogEvent event) -> {
                displayPatientTreatmentDetails();

                buttonAdd.setDisable(true);
                buttonEdit.setDisable(true);
                buttonDelete.setDisable(true);
            });
        } catch (Exception e) {
        }
    }

    public StackPane getStackPane() {
        return stackPane;
    }

}
