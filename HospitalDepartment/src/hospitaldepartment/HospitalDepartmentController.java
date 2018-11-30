/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitaldepartment;

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
public class HospitalDepartmentController implements Initializable {
    
    @FXML
    private StackPane stackPane;
    @FXML
    private JFXButton buttonEdit,buttonDelete;
    @FXML
    private TableView<Department> tableView;
    @FXML
    private TableColumn columnDepart,columnDes;
    private FXMLLoader loaderAdd,loaderEdit,loaderDelete;
    private Parent rootAdd,rootEdit,rootDelete;
    private ObservableList obList;
    private PreparedStatement ps;
    private ResultSet rs;
    private Department selectedValue;
    private DeleteDepartmentController del;
    private EditDepartmentController edit;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            buttonDelete.setDisable(true);
            buttonEdit.setDisable(true);
            
            columnDepart.setCellValueFactory(new PropertyValueFactory("department"));
            columnDes.setCellValueFactory(new PropertyValueFactory("description"));

            obList=FXCollections.observableArrayList();
            tableView.setItems(obList);

            //populate table
            ps=HospitalDB.getCon().prepareStatement("Select * From Department");
            rs=ps.executeQuery();
            while(rs.next()){
                obList.add(new Department(rs.getString("Name"), rs.getString("Description")));
            }
            ps.close();
            
            tableView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Department> observable, Department oldValue, Department newValue) -> {
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
                loaderAdd=new FXMLLoader(getClass().getResource("AddDepartment.fxml"));
                rootAdd=loaderAdd.load();
                AddDepartmentController add=loaderAdd.getController();
                add.setOblist(obList);
            }
            HospitalDialog.showDialog(stackPane, rootAdd);
        } catch (Exception e) {
        }
    }

    @FXML
    private void editAction() {
        try {
            if (loaderEdit==null) {
                loaderEdit=new FXMLLoader(getClass().getResource("EditDepartment.fxml"));
                rootEdit=loaderEdit.load();
                
                edit=loaderEdit.getController();
            }
            
            edit.setSelectedValue(selectedValue);
            
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
                loaderDelete=new FXMLLoader(getClass().getResource("DeleteDepartment.fxml"));
                rootDelete=loaderDelete.load();
                
                del=loaderDelete.getController();
            }
            
            del.setObList(obList);
            del.setSelectedValue(selectedValue);
            
            HospitalDialog.showDialog(stackPane, rootDelete);
            
            tableView.getSelectionModel().clearSelection();
            
            buttonDelete.setDisable(true);
            buttonEdit.setDisable(true);
        } catch (Exception e) {
        }
    }
}
