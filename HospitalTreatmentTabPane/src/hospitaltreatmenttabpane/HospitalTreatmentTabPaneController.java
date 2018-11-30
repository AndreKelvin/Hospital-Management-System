/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitaltreatmenttabpane;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Andre Kelvin
 */
public class HospitalTreatmentTabPaneController {
    
    @FXML
    private BorderPane bPaneTreatment,bPanePatientTreatment;
    private Parent rootTreatment,rootPatientTreatment;
    
    public void setRoots(Parent rootTreatment, Parent rootPatientTreatment){
        this.rootTreatment=rootTreatment;
        this.rootPatientTreatment=rootPatientTreatment;
    }

    @FXML
    private void openPatientTreatmentModule() {
        bPanePatientTreatment.setCenter(rootPatientTreatment);
    }

    @FXML
    public void openTreatmentModule() {
        bPaneTreatment.setCenter(rootTreatment);
    }
    
}
