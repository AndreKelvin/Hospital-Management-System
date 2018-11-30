/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalroom;

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
public class HospitalRoomController implements Initializable {
    
    @FXML
    private StackPane stackPane;
    @FXML
    private JFXButton buttonEdit,buttonDelete;
    @FXML
    private TableView<Room> tableView;
    @FXML
    private TableColumn columnFloor,columnRoom;
    private FXMLLoader loaderAdd,loaderEdit,loaderDelete;
    private Parent rootAdd,rootEdit,rootDelete;
    private ObservableList obList;
    private PreparedStatement ps;
    private ResultSet rs;
    private Room selectedValue;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            buttonDelete.setDisable(true);
            buttonEdit.setDisable(true);
            
            columnFloor.setCellValueFactory(new PropertyValueFactory("floor"));
            columnRoom.setCellValueFactory(new PropertyValueFactory("room"));

            obList=FXCollections.observableArrayList();
            tableView.setItems(obList);

            //populate table
            ps=HospitalDB.getCon().prepareStatement("Select * From Room");
            rs=ps.executeQuery();
            
            while(rs.next()){
                obList.add(new Room(rs.getString("Floor"), Integer.parseInt(rs.getString("Room"))));
            }
            ps.close();
            
            tableView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Room> observable, Room oldValue, Room newValue) -> {
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
                loaderAdd=new FXMLLoader(getClass().getResource("AddRoom.fxml"));
                rootAdd=loaderAdd.load();
                
                AddRoomController add=loaderAdd.getController();
                add.setOblist(obList);
            }
            HospitalDialog.showDialog(stackPane, rootAdd);
        } catch (Exception e) {
        }
    }
    
    @FXML
    private void editAction(ActionEvent event) {
        try {
            if (loaderEdit==null) {
                loaderEdit=new FXMLLoader(getClass().getResource("EditRoom.fxml"));
                rootEdit=loaderEdit.load();
            }
            EditRoomController edit=loaderEdit.getController();
            edit.setSelectedValue(selectedValue);
            
            HospitalDialog.showDialog(stackPane, rootEdit);
            
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
                loaderDelete=new FXMLLoader(getClass().getResource("DeleteRoom.fxml"));
                rootDelete=loaderDelete.load();
            }
            DeleteRoomController del=loaderDelete.getController();
            del.setObList(obList);
            del.setSelectedValue(selectedValue);
            
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
