/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalblooddonor;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.events.JFXDialogEvent;
import hospitaldialog.HospitalDialog;
import hospitaldb.HospitalDB;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
public class HospitalBloodDonorController implements Initializable {

    @FXML
    private StackPane stackPane;
    @FXML
    private JFXButton buttonEdit, buttonDelete;
    @FXML
    private TableView<BloodDonor> tableView;
    @FXML
    private TableColumn columnID, columnName, columnAge, columnSex, columnBloodGroup, columnDonationDate, columnPhone, columnEmail, columnAddress;
    private PreparedStatement ps;
    private ResultSet rs;
    private ObservableList obList;
    private BloodDonor selectedDonor;
    private FXMLLoader loaderAdd, loaderEdit, loaderDelete;
    private Parent rootAdd, rootEdit, rootDelete;
    private EditDonorController edit;
    private DeleteDonorController del;
    private AddDonorController add;
    private byte age;
    private Calendar calender;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            buttonDelete.setDisable(true);
            buttonEdit.setDisable(true);

            calender = new GregorianCalendar();

            obList = FXCollections.observableArrayList();
            tableView.setItems(obList);

            columnID.setCellValueFactory(new PropertyValueFactory("id"));
            columnAddress.setCellValueFactory(new PropertyValueFactory("address"));
            columnAge.setCellValueFactory(new PropertyValueFactory("age"));
            columnBloodGroup.setCellValueFactory(new PropertyValueFactory("bloodGroup"));
            columnDonationDate.setCellValueFactory(new PropertyValueFactory("donationDate"));
            columnEmail.setCellValueFactory(new PropertyValueFactory("email"));
            columnName.setCellValueFactory(new PropertyValueFactory("name"));
            columnPhone.setCellValueFactory(new PropertyValueFactory("phone"));
            columnSex.setCellValueFactory(new PropertyValueFactory("sex"));

            //Populate Table
            ps = HospitalDB.getCon().prepareStatement("Select * From Blood_Donor");
            rs = ps.executeQuery();

            while (rs.next()) {
                age = (byte) (calender.get(Calendar.YEAR) - rs.getDate("Age").toLocalDate().getYear());
                obList.add(new BloodDonor(rs.getInt("id"), rs.getString("Name"), age + "", rs.getString("Gender"), rs.getString("Blood_Group"),
                        rs.getString("Donation_Date"), rs.getString("Phone"), rs.getString("Email"), rs.getString("Address")));
            }

            tableView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends BloodDonor> observable, BloodDonor oldValue, BloodDonor newValue) -> {
                buttonDelete.setDisable(false);
                buttonEdit.setDisable(false);

                selectedDonor = newValue;
            });
            
            tableView.getFocusModel().focus(-1);
        } catch (Exception e) {
        }
    }

    @FXML
    private void add() {
        try {
            if (loaderAdd == null) {
                loaderAdd = new FXMLLoader(getClass().getResource("AddDonor.fxml"));
                rootAdd = loaderAdd.load();

                add = loaderAdd.getController();
                add.setObservableList(obList);
            }
            add.populateBloodGroupComboBox();
            
            HospitalDialog.showDialog(stackPane, rootAdd);
        } catch (Exception e) {
        }
    }

    @FXML
    private void edit() {
        try {
            if (loaderEdit == null) {
                loaderEdit = new FXMLLoader(getClass().getResource("EditDonor.fxml"));
                rootEdit = loaderEdit.load();

                edit = loaderEdit.getController();
            }
            edit.populateBloodGroupComboBox();
            edit.setSelectedDonor(selectedDonor);

            HospitalDialog.showDialog(stackPane, rootEdit);
            
            HospitalDialog.dialog.setOnDialogClosed((JFXDialogEvent event) -> {
                tableView.getSelectionModel().clearSelection();
                
                buttonDelete.setDisable(true);
                buttonEdit.setDisable(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void delete() {
        try {
            if (loaderDelete == null) {
                loaderDelete = new FXMLLoader(getClass().getResource("DeleteDonor.fxml"));
                rootDelete = loaderDelete.load();

                del = loaderDelete.getController();
                del.setObservableList(obList);
            }
            del.setSelectedDonor(selectedDonor);

            HospitalDialog.showDialog(stackPane, rootDelete);
            
            HospitalDialog.dialog.setOnDialogClosed((JFXDialogEvent event) -> {
                tableView.getSelectionModel().clearSelection();
                
                buttonDelete.setDisable(true);
                buttonEdit.setDisable(true);
            });
        } catch (Exception e) {
        }
    }
    
    public StackPane getStackPane() {
        return stackPane;
    }

}
