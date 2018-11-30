/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalbirth;

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
public class HospitalBirthController implements Initializable {
    
    @FXML
    private JFXButton buttonEdit,buttonDelete,buttonCerti;
    @FXML
    private StackPane stackPane;
    @FXML
    private JFXListView listView;
    @FXML
    private Label labelMName,labelGender,labelDoc,labelTime,labelDate,labelFName,labelBGroup,labelWeight;
    @FXML
    private JFXTextField textSearch;
    private PreparedStatement ps;
    private ResultSet rs;
    private List searchList;
    private SuggestionProvider suggestions;
    private FXMLLoader loaderAdd, loaderEdit, loaderDelete, loaderCerti;
    private Parent rootAdd, rootEdit, rootDelete, rootCerti;
    private JFXDialog dialogCerti;
    private String selectedBaby;
    private AddBirthController add;
    private EditBirthController edit;
    private DeleteBirthController del;
    private BirthCertificateController birthCerti;
    
    /**
     * Populate Search Auto Complete Textfield with baby Names
     */
    private void searchBabies() {
        try {
            searchList.clear();
            ps = HospitalDB.getCon().prepareStatement("Select Baby From Birth");
            rs = ps.executeQuery();

            while (rs.next()) {
                searchList.add(rs.getString("Baby"));
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
            searchBabies();

            buttonCerti.setDisable(true);
            buttonDelete.setDisable(true);
            buttonEdit.setDisable(true);
            
            populateListView();
            
            listView.getSelectionModel().selectedItemProperty().addListener((ObservableValue observable, Object oldValue, Object newValue) -> {
                buttonCerti.setDisable(false);
                buttonDelete.setDisable(false);
                buttonEdit.setDisable(false);

                try {
                    selectedBaby=newValue.toString();
                    
                    ps = HospitalDB.getCon().prepareStatement("Select * From Birth Where Baby=?");
                    ps.setString(1, selectedBaby);
                    rs = ps.executeQuery();

                    if (rs.next()) {
                        labelMName.setText(rs.getString("Mother"));
                        labelDate.setText(rs.getDate("Date").toString());
                        labelTime.setText(rs.getString("Time"));
                        labelFName.setText(rs.getString("Father"));
                        labelBGroup.setText(rs.getString("Blood_Group"));
                        labelGender.setText(rs.getString("Gender"));
                        labelDoc.setText(rs.getString("Doctor"));
                        labelWeight.setText(rs.getString("Weight"));
                    }
                } catch (Exception e) {
                }
                //textSearch.clear();
            });
            
            textSearch.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                listView.getSelectionModel().select(newValue);
            });
        } catch (Exception e) {
        }
    }    
    
    /**
     * This Method Refresh the List View With Baby Names
     */
    private void populateListView() {
        try {
            listView.getItems().clear();
            ps = HospitalDB.getCon().prepareStatement("Select Baby From Birth");
            rs = ps.executeQuery();

            while (rs.next()) {
                listView.getItems().add(rs.getString("Baby"));
            }
        } catch (Exception e) {
        }
    }
    
    @FXML
    private void openAddDialog() {
        try {
            if (loaderAdd == null) {
                loaderAdd = new FXMLLoader(getClass().getResource("AddBirth.fxml"));
                rootAdd = loaderAdd.load();
                add=loaderAdd.getController();
            }
            add.populateComboBox();
            HospitalDialog.showDialog(stackPane, rootAdd);

            HospitalDialog.dialog.setOnDialogClosed((JFXDialogEvent event) -> {
                populateListView();
                searchBabies();
            });
        } catch (Exception e) {
        }
    }
    
    @FXML
    private void openEditDialog() {
        try {
            if (loaderEdit == null) {
                loaderEdit = new FXMLLoader(getClass().getResource("EditBirth.fxml"));
                rootEdit = loaderEdit.load();
                edit=loaderEdit.getController();
            }
            edit.populateComboBox();
            edit.setSelectedBaby(selectedBaby);
            
            HospitalDialog.showDialog(stackPane, rootEdit);
            
            HospitalDialog.dialog.setOnDialogClosed((JFXDialogEvent event) -> {
                populateListView();
                searchBabies();
                
                listView.getSelectionModel().select(selectedBaby);
                
                buttonCerti.setDisable(true);
                buttonDelete.setDisable(true);
                buttonEdit.setDisable(true);
            });
        } catch (Exception e) {
        }
    }
    
    @FXML
    private void openDeleteDialog() {
        try {
            if (loaderDelete == null) {
                loaderDelete = new FXMLLoader(getClass().getResource("DeleteBirth.fxml"));
                rootDelete = loaderDelete.load();
                
                del=loaderDelete.getController();
            }
            del.setSelectedBaby(selectedBaby);
            
            HospitalDialog.showDialog(stackPane, rootDelete);
            
            HospitalDialog.dialog.setOnDialogClosed((JFXDialogEvent event) -> {
                populateListView();
                searchBabies();
                
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
                loaderCerti = new FXMLLoader(getClass().getResource("BirthCertificate.fxml"));
                rootCerti = loaderCerti.load();

                dialogCerti = new JFXDialog(stackPane, (Region) rootCerti, JFXDialog.DialogTransition.TOP);
                
                birthCerti=loaderCerti.getController();
            }
            birthCerti.setSelectedBaby(selectedBaby);
            
            dialogCerti.show();
            
            dialogCerti.setOnDialogClosed((JFXDialogEvent event) -> {
                buttonCerti.setDisable(true);
                buttonDelete.setDisable(true);
                buttonEdit.setDisable(true);
            });
        } catch (Exception e) {
        }
    }
    
    public StackPane getStackPane(){
        return stackPane;
    }
    
}
