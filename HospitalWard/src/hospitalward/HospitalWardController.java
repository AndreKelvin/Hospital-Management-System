/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalward;

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
import javafx.event.ActionEvent;
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
public class HospitalWardController implements Initializable {
    
    @FXML
    private StackPane stackPane;
    @FXML
    private TableView<Ward> tableView;
    @FXML
    private TableColumn columnWard,columnBeds;
    @FXML
    private JFXButton buttonEdit,buttonDelete;
    private FXMLLoader loaderAdd,loaderEdit,loaderDelete;
    private Parent rootAdd,rootEdit,rootDelete;
    private ObservableList obList;
    private PreparedStatement ps;
    private ResultSet rs;
    private Ward selectedValue;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            buttonDelete.setDisable(true);
            buttonEdit.setDisable(true);
            
            columnWard.setCellValueFactory(new PropertyValueFactory("ward"));
            columnBeds.setCellValueFactory(new PropertyValueFactory("bed"));

            obList=FXCollections.observableArrayList();
            tableView.setItems(obList);

            //populate table
            ps=HospitalDB.getCon().prepareStatement("Select * From Ward");
            rs=ps.executeQuery();
            
            while(rs.next()){
                obList.add(new Ward(rs.getString("Ward"), Integer.parseInt(rs.getString("Beds"))));
            }
            ps.close();
            
            tableView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Ward> observable, Ward oldValue, Ward newValue) -> {
                selectedValue=newValue;
                buttonDelete.setDisable(false);
                buttonEdit.setDisable(false);
            });
            tableView.getFocusModel().focus(-1);
        } catch (Exception e) {
        }
    }    
    
    @FXML
    private void addAction(ActionEvent event) {
        try {
            if (loaderAdd==null) {
                loaderAdd=new FXMLLoader(getClass().getResource("AddWard.fxml"));
                rootAdd=loaderAdd.load();
                
                AddWardController add=loaderAdd.getController();
                add.setOblist(obList);
            }
            HospitalDialog.showDialog(stackPane,rootAdd);
        } catch (IOException ex) {
        }
    }

    @FXML
    private void editAction(ActionEvent event) {
        try {
            if (loaderEdit==null) {
                loaderEdit=new FXMLLoader(getClass().getResource("EditWard.fxml"));
                rootEdit=loaderEdit.load();
            }
            EditWardController edit=loaderEdit.getController();
            edit.setSelectedValue(selectedValue);
            
            HospitalDialog.showDialog(stackPane,rootEdit);
            
            tableView.getSelectionModel().clearSelection();
            
            buttonDelete.setDisable(true);
            buttonEdit.setDisable(true);
        } catch (Exception e) {
        }
    }
    
    @FXML
    private void deleteAction(ActionEvent event) {
        try {
            if (loaderDelete==null) {
                loaderDelete=new FXMLLoader(getClass().getResource("DeleteWard.fxml"));
                rootDelete=loaderDelete.load();
            }
            DeleteWardController del=loaderDelete.getController();
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
