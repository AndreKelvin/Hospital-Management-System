/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitaldeath;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import hospitaldb.HospitalDB;
import hospitaldialog.HospitalDialog;
import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Andre Kelvin
 */
public class HospitalDeathController implements Initializable {

    @FXML
    private StackPane stackPane;
    @FXML
    private JFXListView listView;
    @FXML
    private JFXButton buttonEdit, buttonDelete, buttonCerti;
    @FXML
    private Label labelID, labelTreat, labelDeath, labelTime, labelDate, labelGender;
    @FXML
    private JFXTextField textSearch;
    private PreparedStatement ps;
    private ResultSet rs;
    private List searchList;
    private SuggestionProvider suggestions;
    private FXMLLoader loaderAdd, loaderEdit, loaderDelete, loaderCerti;
    private Parent rootAdd, rootEdit, rootDelete, rootCerti;
    private JFXDialog dialogCerti;
    private AddDeathController add;
    private EditDeathController edit;
    private DeleteDeathController del;
    private DeathCeritificateController death;
    private String selectedDeadPatient;

    /**
     * Populate Search Auto Complete Textfield with Death Patient Name
     */
    private void searchDeadPatient() {
        try {
            searchList.clear();
            ps = HospitalDB.getCon().prepareStatement("Select Name From Death");
            rs = ps.executeQuery();

            while (rs.next()) {
                searchList.add(rs.getString("Name"));
            }
            ps.close();

            suggestions.clearSuggestions();
            suggestions.addPossibleSuggestions(searchList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            searchList = new ArrayList();
            suggestions = SuggestionProvider.create(searchList);
            AutoCompletionTextFieldBinding autoCompletionTextFieldBinding = new AutoCompletionTextFieldBinding<>(textSearch, suggestions);
            searchDeadPatient();

            buttonCerti.setDisable(true);
            buttonDelete.setDisable(true);
            buttonEdit.setDisable(true);
            
            populateListView();

            listView.getSelectionModel().selectedItemProperty().addListener((ObservableValue observable, Object oldValue, Object newValue) -> {
                buttonCerti.setDisable(false);
                buttonDelete.setDisable(false);
                buttonEdit.setDisable(false);

                try {
                    selectedDeadPatient=newValue.toString();
                    
                    ps = HospitalDB.getCon().prepareStatement("Select * From Death Where Name=?");
                    ps.setString(1, selectedDeadPatient);
                    rs = ps.executeQuery();

                    if (rs.next()) {
                        labelID.setText(rs.getString("ID"));
                        labelDate.setText(rs.getDate("Date").toString());
                        labelTime.setText(rs.getString("Time"));
                        labelDeath.setText(rs.getString("Course_Of_Death"));
                        labelTreat.setText(rs.getString("Treatment"));
                        labelGender.setText(rs.getString("Gender"));
                    }
                } catch (Exception e) {
                }
                //textSearch.clear();
            });
            
            textSearch.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                listView.getSelectionModel().select(newValue);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This Method Refresh the List View With Dead Patient Names
     */
    private void populateListView() {
        try {
            listView.getItems().clear();
            ps = HospitalDB.getCon().prepareStatement("Select Name From Death");
            rs = ps.executeQuery();

            while (rs.next()) {
                listView.getItems().add(rs.getString("Name"));
            }
        } catch (Exception e) {
        }
    }

    @FXML
    private void openAddDialog() {
        try {
            if (loaderAdd == null) {
                loaderAdd = new FXMLLoader(getClass().getResource("AddDeath.fxml"));
                rootAdd = loaderAdd.load();
                add=loaderAdd.getController();
            }
            add.populateComboBox();
            HospitalDialog.showDialog(stackPane, rootAdd);

            HospitalDialog.dialog.setOnDialogClosed((JFXDialogEvent event) -> {
                populateListView();
                searchDeadPatient();
            });
        } catch (Exception e) {
        }
    }

    @FXML
    private void openEditDialog() {
        try {
            if (loaderEdit == null) {
                loaderEdit = new FXMLLoader(getClass().getResource("EditDeath.fxml"));
                rootEdit = loaderEdit.load();
                edit=loaderEdit.getController();
            }
            edit.getSelectedDeadPatient(selectedDeadPatient);
            
            HospitalDialog.showDialog(stackPane, rootEdit);
            
            HospitalDialog.dialog.setOnDialogClosed((JFXDialogEvent event) -> {
                populateListView();
                searchDeadPatient();
                
                listView.getSelectionModel().select(selectedDeadPatient);
                
                buttonCerti.setDisable(true);
                buttonDelete.setDisable(true);
                buttonEdit.setDisable(true);
            });
        } catch (Exception e) {e.printStackTrace();
        }
    }

    @FXML
    private void openDeleteDialog() {
        try {
            if (loaderDelete == null) {
                loaderDelete = new FXMLLoader(getClass().getResource("DeleteDeath.fxml"));
                rootDelete = loaderDelete.load();
                
                del=loaderDelete.getController();
            }
            del.getSelectedDeadPatient(selectedDeadPatient);
            
            HospitalDialog.showDialog(stackPane, rootDelete);
            
            HospitalDialog.dialog.setOnDialogClosed((JFXDialogEvent event) -> {
                populateListView();
                searchDeadPatient();
                
                buttonCerti.setDisable(true);
                buttonDelete.setDisable(true);
                buttonEdit.setDisable(true);
            });
        } catch (Exception e) {
        }
    }

    @FXML
    private void openCartificateDialog() {
        try {
            if (loaderCerti == null) {
                loaderCerti = new FXMLLoader(getClass().getResource("DeathCeritificate.fxml"));
                rootCerti = loaderCerti.load();

                dialogCerti = new JFXDialog(stackPane, (Region) rootCerti, JFXDialog.DialogTransition.TOP);
                
                death=loaderCerti.getController();
            }
            death.getSelectedDeadPatient(selectedDeadPatient);
            
            dialogCerti.show();
            
            dialogCerti.setOnDialogClosed((JFXDialogEvent event) -> {
                buttonCerti.setDisable(true);
                buttonDelete.setDisable(true);
                buttonEdit.setDisable(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Passing the Root(Stack Pane) to HospitalDeath class
     * @return 
     */
    public StackPane getStackPane(){
        return stackPane;
    }

}
