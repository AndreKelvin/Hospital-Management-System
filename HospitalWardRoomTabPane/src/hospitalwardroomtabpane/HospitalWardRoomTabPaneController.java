/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalwardroomtabpane;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Andre Kelvin
 */
public class HospitalWardRoomTabPaneController {
    
    @FXML
    private BorderPane bPaneWard,bPaneRoom;
    private Parent rootWard,rootRoom;
    
    public void setRoots(Parent rootWard, Parent rootRoom){
        this.rootWard=rootWard;
        this.rootRoom=rootRoom;
    }

    @FXML
    private void openRoomModule() {
        bPaneRoom.setCenter(rootRoom);
    }

    @FXML
    public void openWardModule() {
        bPaneWard.setCenter(rootWard);
    }
    
}
