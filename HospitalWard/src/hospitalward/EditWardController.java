/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalward;

import com.jfoenix.controls.JFXTextField;
import hospitaldialog.HospitalDialog;
import hospitaldb.HospitalDB;
import java.awt.Toolkit;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class EditWardController implements Initializable {

    @FXML
    private JFXTextField textWardName,textBeds;
    private Ward selectedValue;
    private PreparedStatement ps;
    private ResultSet rs;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Force the text field to be numeric only
        textBeds.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                textBeds.setText(newValue.replaceAll("[^\\d]", ""));
                Toolkit.getDefaultToolkit().beep();
            }
        });
    }  
    
    public void setSelectedValue(Ward selectedValue) throws SQLException{
        this.selectedValue=selectedValue;
        
        ps=HospitalDB.getCon().prepareStatement("Select * From Ward Where Ward=?");
        ps.setString(1, selectedValue.getWard());
        rs=ps.executeQuery();
        
        while(rs.next()){
            textWardName.setText(rs.getString("Ward"));
            textBeds.setText(rs.getString("Beds"));
        }
        ps.close();
    }
    
    @FXML
    private void saveAction() {
        try {
            String ward=textWardName.getText().trim();
            String bed=textBeds.getText().trim();
            
            if (ward.isEmpty()||bed.isEmpty()) {
                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "Please Fiil All Required Input", textBeds.getScene().getWindow());
            }
            else{
                ps=HospitalDB.getCon().prepareStatement("Update Ward Set Ward=?,Beds=? Where Ward=?");
                ps.setString(1, ward);
                ps.setInt(2, Integer.parseInt(bed));
                ps.setString(3, selectedValue.getWard());
                ps.executeUpdate();
                ps.close();
                
                selectedValue.setWard(ward);
                selectedValue.setBed(Integer.parseInt(bed));
                
                HospitalDialog.dialog.close();
                HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Save Successful", textBeds.getScene().getWindow());
            }
        } catch (Exception ex) {
        }
    }
    
}
