/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitaldeathbirthtabpane;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Andre Kelvin
 */
public class HospitalDeathBirthTabPaneController {
    
    @FXML
    private BorderPane bPaneDeath,bPaneBirth;
    private Parent rootDeath,rootBirth;
    
    public void setRoots(Parent rootDeath, Parent rootBirth){
        this.rootDeath=rootDeath;
        this.rootBirth=rootBirth;
    }

    @FXML
    private void openBirthModule() {
        bPaneBirth.setCenter(rootBirth);
    }

    @FXML
    public void openDeathModule() {
        bPaneDeath.setCenter(rootDeath);
    }

    
}
