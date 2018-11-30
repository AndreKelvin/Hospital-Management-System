/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalbloodgroup;

import com.jfoenix.controls.JFXButton;
import hospitaldialog.HospitalDialog;
import hospitaldb.HospitalDB;
import java.io.IOException;
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
public class HospitalBloodGroupController implements Initializable {
    
    @FXML
    private StackPane stackPane;
    @FXML
    private JFXButton buttonEdit,buttonDelete;
    @FXML
    private TableView<BloodGroup> tableView;
    @FXML
    private TableColumn columnBlood,columnBags;
    private FXMLLoader loaderAdd,loaderEdit,loaderDelete;
    private Parent rootAdd,rootEdit,rootDelete;
    private ObservableList obList;
    private PreparedStatement ps;
    private ResultSet rs;
    private BloodGroup selectedValue;
    private DeleteBloodGroupController del;
    private EditBloodGroupController edit;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            buttonDelete.setDisable(true);
            buttonEdit.setDisable(true);
            
            columnBlood.setCellValueFactory(new PropertyValueFactory("blood"));
            columnBags.setCellValueFactory(new PropertyValueFactory("bags"));

            obList=FXCollections.observableArrayList();
            tableView.setItems(obList);

            //populate table
            ps=HospitalDB.getCon().prepareStatement("Select * From Blood_Group");
            rs=ps.executeQuery();
            while(rs.next()){
                obList.add(new BloodGroup(rs.getString("Name"), Integer.parseInt(rs.getString("Bags"))));
            }
            ps.close();
            
            tableView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends BloodGroup> observable, BloodGroup oldValue, BloodGroup newValue) -> {
                selectedValue=newValue;
                buttonDelete.setDisable(false);
                buttonEdit.setDisable(false);
            });
            tableView.getFocusModel().focus(-1);
        } catch (Exception e) {
        }
    }    
    
    @FXML
    private void addAction() {
        try {
            if (loaderAdd==null) {
                loaderAdd=new FXMLLoader(getClass().getResource("AddBloodGroup.fxml"));
                rootAdd=loaderAdd.load();
                
                AddBloodGroupController add=loaderAdd.getController();
                add.setOblist(obList);
            }
            HospitalDialog.showDialog(stackPane,rootAdd);
        } catch (IOException ex) {
        }
    }
    
    @FXML
    private void editAction() {
        try {
            if (loaderEdit==null) {
                loaderEdit=new FXMLLoader(getClass().getResource("EditBloodGroup.fxml"));
                rootEdit=loaderEdit.load();
                
                edit=loaderEdit.getController();
            }
            edit.setSelectedValue(selectedValue);
            
            HospitalDialog.showDialog(stackPane,rootEdit);
            
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
                loaderDelete=new FXMLLoader(getClass().getResource("DeleteBloodGroup.fxml"));
                rootDelete=loaderDelete.load();
                del=loaderDelete.getController();
            }
            
            del.setSelectedValue(selectedValue);
            del.setObList(obList);
            
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
