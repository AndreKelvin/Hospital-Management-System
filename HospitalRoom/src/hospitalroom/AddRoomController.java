/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalroom;

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
public class AddRoomController implements Initializable {

    @FXML
    private JFXTextField textFloor,textRoom;
    private ObservableList obList;
    private PreparedStatement ps;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Force the text field to be numeric only
        textRoom.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                textRoom.setText(newValue.replaceAll("[^\\d]", ""));
                Toolkit.getDefaultToolkit().beep();
            }
        });
    }    
    
    public void setOblist(ObservableList obList) {
        this.obList = obList;
    }
    
    @FXML
    private void saveAction() {
        try {
            String floor = textFloor.getText().trim();
            String room = textRoom.getText().trim();

            if (floor.isEmpty() || room.isEmpty()) {
                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "Please Fill All Required Input", textRoom.getScene().getWindow());
            } else {
                ps = HospitalDB.getCon().prepareStatement("Insert Into Room values(?,?)");
                ps.setString(1, floor);
                ps.setInt(2, Integer.parseInt(room));
                ps.executeUpdate();

                obList.add(new Room(floor, Integer.parseInt(room)));
                ps.close();

                HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Save Successful", textRoom.getScene().getWindow());
                textFloor.clear();
                textRoom.clear();
            }
        } catch (Exception ex) {
        }
    }
}
