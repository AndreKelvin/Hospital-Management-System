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
public class EditRoomController implements Initializable {

    @FXML
    private JFXTextField textFloor,textRoom;
    private Room selectedValue;
    private PreparedStatement ps;
    private ResultSet rs;
    
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
    
    public void setSelectedValue(Room selectedValue) throws SQLException{
        this.selectedValue=selectedValue;
        
        ps=HospitalDB.getCon().prepareStatement("Select * From Room Where Floor=?");
        ps.setString(1, selectedValue.getFloor());
        rs=ps.executeQuery();
        
        while(rs.next()){
            textFloor.setText(rs.getString("Floor"));
            textRoom.setText(rs.getString("Room"));
        }
        ps.close();
    }
    
    @FXML
    private void saveAction() {
        try {
            String floor=textFloor.getText().trim();
            String room=textRoom.getText().trim();
            
            if (floor.isEmpty()||room.isEmpty()) {
                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "Please Fill All Required Input", textRoom.getScene().getWindow());
            }
            else{
                ps=HospitalDB.getCon().prepareStatement("Update Room Set Floor=?,Room=? Where Floor=?");
                ps.setString(1, floor);
                ps.setInt(2, Integer.parseInt(room));
                ps.setString(3, selectedValue.getFloor());
                ps.executeUpdate();
                ps.close();
                
                selectedValue.setFloor(floor);
                selectedValue.setRoom(Integer.parseInt(room));
                
                HospitalDialog.dialog.close();
                HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Save Successful", textRoom.getScene().getWindow());
            }
        } catch (Exception ex) {
        }
    }
    
}
