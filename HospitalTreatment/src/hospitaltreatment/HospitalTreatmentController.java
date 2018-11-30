/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitaltreatment;

import com.jfoenix.controls.JFXButton;
import hospitaldialog.HospitalDialog;
import hospitaldb.HospitalDB;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
public class HospitalTreatmentController implements Initializable {

    @FXML
    private StackPane stackPane;
    @FXML
    private JFXButton buttonEdit, buttonDelete;
    @FXML
    private TableView<Treatment> tableView;
    @FXML
    private TableColumn columnTreat, columnPrice;
    private PreparedStatement ps;
    private ResultSet rs;
    private ObservableList obList;
    private FXMLLoader loaderAdd,loaderEdit,loaderDelete;
    private Parent rootAdd,rootEdit,rootDelete;
    private Treatment selectedTreatment;
    private EditTreatmentController edit;
    private DeleteTreatmentController del;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            
            buttonDelete.setDisable(true);
            buttonEdit.setDisable(true);
            
            obList = FXCollections.observableArrayList();
            tableView.setItems(obList);

            columnTreat.setCellValueFactory(new PropertyValueFactory("treatment"));
            columnPrice.setCellValueFactory(new PropertyValueFactory("price"));

            //Populate Table
            obList.clear();
            ps = HospitalDB.getCon().prepareStatement("Select * From Treatment");
            rs=ps.executeQuery();
            
            while(rs.next()){
                obList.add(new Treatment(rs.getString("Treatment_Name"), rs.getInt("Price")));
            }
            
            tableView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Treatment> observable, Treatment oldValue, Treatment newValue) -> {
                buttonDelete.setDisable(false);
                buttonEdit.setDisable(false);
                
                selectedTreatment=newValue;
            });
            
            tableView.getFocusModel().focus(-1);
            
        } catch (Exception e) {
        }

    }

    @FXML
    private void addAction() {
        try {
            if (loaderAdd==null) {
                loaderAdd=new FXMLLoader(getClass().getResource("AddTreatment.fxml"));
                rootAdd=loaderAdd.load();
                
                AddTreatmentController add=loaderAdd.getController();
                add.setObList(obList);
            }
            HospitalDialog.showDialog(stackPane, rootAdd);
        } catch (Exception e) {
        }
    }

    @FXML
    private void editAction() {
        try {
            if (loaderEdit==null) {
                loaderEdit=new FXMLLoader(getClass().getResource("EditTreatment.fxml"));
                rootEdit=loaderEdit.load();
                
                edit=loaderEdit.getController();
            }
            
            edit.setselectedTreatment(selectedTreatment);
            
            HospitalDialog.showDialog(stackPane, rootEdit);
            
            tableView.getSelectionModel().clearSelection();
            
            buttonDelete.setDisable(true);
            buttonEdit.setDisable(true);
        } catch (Exception e) {
        }
    }

    @FXML
    private void deleteAction() {
        try {
            if (loaderDelete==null) {
                loaderDelete=new FXMLLoader(getClass().getResource("DeleteTreatment.fxml"));
                rootDelete=loaderDelete.load();
                
                del=loaderDelete.getController();
                del.setObList(obList);
            }
            
            del.setselectedTreatment(selectedTreatment);
            
            HospitalDialog.showDialog(stackPane, rootDelete);
            
            tableView.getSelectionModel().clearSelection();
            
            buttonDelete.setDisable(true);
            buttonEdit.setDisable(true);
        } catch (Exception e) {
        }
    }

    public StackPane getStackPane() {
        return stackPane;
    }

}
