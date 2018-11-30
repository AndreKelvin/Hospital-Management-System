/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitaldrug;

import hospitaldialog.HospitalDialog;
import hospitaldb.HospitalDB;
import java.io.File;
import java.sql.PreparedStatement;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class DeleteDrugController {

    @FXML
    private Label label;
    private Drug selectedDrug;
    private File drugImageFile;
    private PreparedStatement ps;
    
    public void setSelectedDrug(Drug selectedDrug) {
        this.selectedDrug=selectedDrug;
        label.setText("Are your sure you want to Delete " + selectedDrug.getName() + "?");
    }
    
    public void setDrugImageFile(File drugImageFile){
        this.drugImageFile=drugImageFile;
    }
    
    @FXML
    private void yesAction(){
        try {
            ps = HospitalDB.getCon().prepareStatement("Delete From Drug Where Name=?");
            ps.setString(1, selectedDrug.getName());
            ps.executeUpdate();

            //Delete the Selected Drug Image
            drugImageFile.delete();

            HospitalDialog.dialog.close();
        } catch (Exception e) {
        }
    }
    
    @FXML
    private void noAction(){
        HospitalDialog.dialog.close();
    }
    
}
