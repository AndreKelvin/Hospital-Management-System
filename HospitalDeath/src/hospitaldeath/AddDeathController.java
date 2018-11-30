/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitaldeath;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import hospitaldb.HospitalDB;
import hospitaldialog.HospitalDialog;
import java.awt.Toolkit;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class AddDeathController implements Initializable {

    @FXML
    private JFXComboBox comboPatName;
    @FXML
    private JFXDatePicker datePicker;
    @FXML
    private JFXTimePicker TimePicker;
    @FXML
    private JFXTextField textDeath, textid;
    @FXML
    private JFXRadioButton radioYes, radioNo, radioM, radioF;
    @FXML
    private ToggleGroup tg, tg1;
    private PreparedStatement ps;
    private ResultSet rs;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            populateComboBox();

            //Display Patient ID and Gender  when its Selected
            comboPatName.getSelectionModel().selectedItemProperty().addListener((ObservableValue observable, Object oldValue, Object newValue) -> {
                try {
                    ps = HospitalDB.getCon().prepareStatement("Select ID,Gender From Patient Where Patient_Name=?");
                    ps.setString(1, newValue.toString());
                    rs = ps.executeQuery();

                    if (rs.next()) {
                        textid.setText(rs.getString("ID"));
                        if (rs.getString("Gender").contentEquals("M")) {
                            radioM.setSelected(true);
                        } else {
                            radioF.setSelected(true);
                        }
                    }
                } catch (Exception e) {
                }
            });

            TimePicker.setEditable(false);

            //Force the text field to be numeric only
            textid.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (!newValue.matches("\\d*")) {
                    textid.setText(newValue.replaceAll("[^\\d]", ""));
                    Toolkit.getDefaultToolkit().beep();
                }
            });

        } catch (Exception e) {
        }
    }

    public void populateComboBox() throws SQLException {
        ps = HospitalDB.getCon().prepareStatement("Select Patient_Name From Patient Where Category In(?,?)");
        ps.setString(1, "In Patient");
        ps.setString(2, "Out Patient");
        rs = ps.executeQuery();

        comboPatName.getItems().clear();
        while (rs.next()) {
            comboPatName.getItems().add(rs.getString("Patient_Name"));
        }
    }

    @FXML
    private void save() {
        try {
            if (comboPatName.getValue() == null || datePicker.getValue() == null || TimePicker.getValue() == null || textDeath.getText().isEmpty() || tg.getSelectedToggle() == null) {
                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "Please Fiil All Required Input", textDeath.getScene().getWindow());
            } else {
                ps = HospitalDB.getCon().prepareStatement("Insert Into Death Values(?,?,?,?,?,?,?)");
                if (textid.getText().isEmpty()) {
                    ps.setString(1, "null");
                } else {
                    ps.setString(1, textid.getText().trim());
                }
                ps.setString(2, comboPatName.getValue().toString());
                ps.setDate(3, Date.valueOf(datePicker.getValue()));
                ps.setString(4, TimePicker.getValue().format(DateTimeFormatter.ofPattern("hh:mm a")));
                ps.setString(5, textDeath.getText().trim());

                if (radioYes.isSelected()) {
                    ps.setString(6, radioYes.getText());
                } else {
                    ps.setString(6, radioNo.getText());
                }

                if (radioM.isSelected()) {
                    ps.setString(7, radioM.getText());
                } else {
                    ps.setString(7, radioF.getText());
                }
                ps.executeUpdate();

                //Remove (Dead) Patient Details From (Alive) Patient Table
                ps = HospitalDB.getCon().prepareStatement("Delete From Patient Where Patient_Name=?");
                ps.setString(1, comboPatName.getValue().toString());
                ps.executeUpdate();

                HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Save Successfull", textDeath.getScene().getWindow());

                textid.clear();
                comboPatName.setValue(null);
                datePicker.setValue(null);
                TimePicker.setValue(null);
                textDeath.clear();
                tg.getSelectedToggle().setSelected(false);
                tg1.getSelectedToggle().setSelected(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
