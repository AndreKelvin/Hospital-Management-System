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
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class AddTreatmentController implements Initializable {

    @FXML
    private JFXTextField textTreat, textPrice;
    private PreparedStatement ps;
    private ObservableList obList;
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

    public void setObList(ObservableList obList) {
        this.obList = obList;
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
                ps = HospitalDB.getCon().prepareStatement("Insert Into Treatment Values(?,?)");
                ps.setString(1, treatment);
                ps.setInt(2, Integer.parseInt(price));
                ps.executeUpdate();

                obList.add(new Treatment(treatment, Integer.parseInt(price)));

                HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Save Successful", textPrice.getScene().getWindow());

                textTreat.clear();
                textPrice.clear();
            }
        } catch (Exception e) {
        }
    }

}
