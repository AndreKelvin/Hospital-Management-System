/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalstafftabpane;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Andre Kelvin
 */
public class HospitalStaffTabPaneController {
    
    @FXML
    private BorderPane bPaneDoctor,bPaneNurse,bPanePharmacist,bPaneLabTech; 
    private Parent rootDoctor,rootNurse,rootLabTech,rootPharmacist;
    
    public void setRoots(Parent rootDoctor,Parent rootNurse,Parent rootLabTech,Parent rootPharmacist){
        this.rootDoctor=rootDoctor;
        this.rootNurse=rootNurse;
        this.rootLabTech=rootLabTech;
        this.rootPharmacist=rootPharmacist;
    }
    
    @FXML
    public void openDoctorModule() {
        bPaneDoctor.setCenter(rootDoctor);
    }

    @FXML
    private void openLabTechModule() {
        bPaneLabTech.setCenter(rootLabTech);
    }

    @FXML
    private void openNurseModule() {
        bPaneNurse.setCenter(rootNurse);
    }

    @FXML
    private void openPharmacistModule() {
        bPanePharmacist.setCenter(rootPharmacist);
    }
    
}
