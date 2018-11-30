/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalbloodtabpane;

import hospitalblooddonor.HospitalBloodDonor;
import hospitalbloodgroup.HospitalBloodGroup;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Andre Kelvin
 */
public class HospitalBloodTabPane extends Application {
    
    private Parent root;
    private FXMLLoader loader;
    private StackPane stackPaneBloodGroup,stackPaneBloodDonor;
    
    @Override
    public void start(Stage stage) throws Exception {
        //root = FXMLLoader.load(getClass().getResource("HospitalBloodTabPane.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }
    
    public HospitalBloodTabPane(){
        try {
            HospitalBloodDonor donor=new HospitalBloodDonor();
            HospitalBloodGroup group=new HospitalBloodGroup();
            
            loader=new FXMLLoader(getClass().getResource("HospitalBloodTabPane.fxml"));
            root = loader.load();
            
            HospitalBloodTabPaneController tab=loader.getController();
            tab.setRoots(group.getFxml(),donor.getFxml());
            
            stackPaneBloodGroup=group.getStackPane();
            stackPaneBloodDonor=donor.getStackPane();
            
            tab.openBloodGroupModule();
        } catch (Exception e) {
        }
    }
    
    public Parent getFxml(){
        return root;
    }

    public StackPane getStackPaneBloodGruop() {
        return stackPaneBloodGroup;
    }
    
    public StackPane getStackPaneBloodDonor() {
        return stackPaneBloodDonor;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
