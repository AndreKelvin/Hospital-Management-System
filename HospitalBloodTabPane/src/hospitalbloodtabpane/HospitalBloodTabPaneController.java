/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalbloodtabpane;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Andre Kelvin
 */
public class HospitalBloodTabPaneController {
    
    @FXML
    private BorderPane bPaneBloodGroup,bPaneBloodDonor;
    private Parent rootBloodGroup,rootBloodDonor;
    
    public void setRoots(Parent rootBloodGroup, Parent rootBloodDonor){
        this.rootBloodGroup=rootBloodGroup;
        this.rootBloodDonor=rootBloodDonor;
    }

    @FXML
    private void openBloodDonorModule() {
        bPaneBloodDonor.setCenter(rootBloodDonor);
    }

    @FXML
    public void openBloodGroupModule() {
        bPaneBloodGroup.setCenter(rootBloodGroup);
    }
    
}
