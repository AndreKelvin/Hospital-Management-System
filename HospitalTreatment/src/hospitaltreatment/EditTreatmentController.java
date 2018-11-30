/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitaltreatment;

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
public class EditTreatmentController implements Initializable {

    @FXML
    private JFXTextField textTreat,textPrice;
    private Treatment selectedTreatment;
    private PreparedStatement ps;
    private ResultSet rs;
    private String treatment, price;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Force the Textfield to be Numeric only
        textPrice.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                textPrice.setText(newValue.replaceAll("[^\\d]", ""));
                Toolkit.getDefaultToolkit().beep();
            }
        });
    }   
    
    public void setselectedTreatment(Treatment selectedTreatment) throws SQLException{
        this.selectedTreatment=selectedTreatment;
        
        ps=HospitalDB.getCon().prepareStatement("Select * From Treatment Where Treatment_Name=?");
        ps.setString(1, selectedTreatment.getTreatment());
        rs=ps.executeQuery();
        
        if (rs.next()) {
            textTreat.setText(rs.getString("Treatment_Name"));
            textPrice.setText(rs.getInt("Price")+"");
        }
    }
    
    @FXML
    private void saveAction() {
        try {
            treatment = textTreat.getText().trim();
            price = textPrice.getText().trim();

            if (treatment.isEmpty() || price.isEmpty()) {
                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "Please Fill All Required Input", textPrice.getScene().getWindow());
            } 
            else {
                ps=HospitalDB.getCon().prepareStatement("Update Treatment Set Treatment_Name=?,Price=? Where Treatment_Name=?");
                ps.setString(1, treatment);
                ps.setInt(2, Integer.parseInt(price));
                ps.setString(3, selectedTreatment.getTreatment());
                ps.executeUpdate();
                
                selectedTreatment.setTreatment(treatment);
                selectedTreatment.setPrice(Integer.parseInt(price));
                
                HospitalDialog.dialog.close();
                HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Save Successful", textPrice.getScene().getWindow());

                textTreat.clear();
                textPrice.clear();
            }
        } catch (Exception e) {
        }
    }
    
}
