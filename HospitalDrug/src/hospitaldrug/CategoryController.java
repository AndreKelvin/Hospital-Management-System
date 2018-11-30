/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitaldrug;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class CategoryController implements Initializable {

    @FXML
    private JFXListView listView;
    @FXML
    private JFXTextField textCate;
    @FXML
    private JFXButton buttonEdit, buttonRemove;
    private ObservableList obList;
    private PreparedStatement ps;
    private ResultSet rs;
    private String name,selectedValue;
    private int index;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            obList = FXCollections.observableArrayList();
            listView.setItems(obList);

            ps = HospitalDB.getCon().prepareStatement("Select * From Drug_Category");
            rs=ps.executeQuery();
            
            while(rs.next()){
                obList.add(rs.getString("Category_Name"));
            }
            
            buttonEdit.setDisable(true);
            buttonRemove.setDisable(true);
            
            listView.getSelectionModel().selectedItemProperty().addListener((ObservableValue observable, Object oldValue, Object newValue) -> {
                if (newValue==null) {
                    
                }
                else{
                    buttonEdit.setDisable(false);
                    buttonRemove.setDisable(false);

                    selectedValue=newValue.toString();

                    textCate.setText(selectedValue);
                }
            });
        } catch (Exception e) {
        }
    }

    @FXML
    private void addAction() {
        try {
            name=textCate.getText();
            
            if (name.isEmpty()) {
                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "Please Fill The Required Input", textCate.getScene().getWindow());
            }
            else{
                ps = HospitalDB.getCon().prepareStatement("Insert Into Drug_Category Values(?)");
                ps.setString(1, name);
                ps.executeUpdate();
                
                obList.add(name);
                
                HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Add Successfull", textCate.getScene().getWindow());
                textCate.clear();
            }
            
        } catch (Exception e) {
        }
    }

    @FXML
    private void editAction() {
        try {
            name=textCate.getText();
            
            if (name.isEmpty()) {
                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "Please Fill The Required Input", textCate.getScene().getWindow());
            }
            else{
                ps = HospitalDB.getCon().prepareStatement("Update Drug_Category Set Category_Name=? Where Category_Name=?");
                ps.setString(1, name);
                ps.setString(2, selectedValue);
                ps.executeUpdate();
                
                index=obList.indexOf(selectedValue);
                obList.remove(selectedValue);
                obList.add(index,name);
                
                textCate.clear();
                listView.getSelectionModel().clearSelection();
                HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Edit Successfull", textCate.getScene().getWindow());
                
                buttonEdit.setDisable(true);
                buttonRemove.setDisable(true);
            }
            
        } catch (Exception e) {
        }
    }

    @FXML
    private void removeAction() {
        try {
            ps = HospitalDB.getCon().prepareStatement("Delete From Drug_Category Where Category_Name=?");
            ps.setString(1, selectedValue);
            ps.executeUpdate();
            
            obList.remove(selectedValue);
            
            textCate.clear();
            listView.getSelectionModel().clearSelection();
            HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Remove Successfull", textCate.getScene().getWindow());
            
            buttonEdit.setDisable(true);
            buttonRemove.setDisable(true);
        } catch (Exception e) {
        }
    }

}
