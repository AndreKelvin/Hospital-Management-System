/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalstafftabpane;

import hospitaldoctor.HospitalDoctor;
import hospitallabtechnician.HospitalLabTechnician;
import hospitalnurse.HospitalNurse;
import hospitalpharmacist.HospitalPharmacist;
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
public class HospitalStaffTabPane extends Application {
    
    private Parent root;
    private FXMLLoader loader; 
    private StackPane stackPaneDoc,stackPaneNurse,stackPanePhar,stackPaneLabTech;
    
    @Override
    public void start(Stage stage) throws Exception {
//        loader = new FXMLLoader(getClass().getResource("HospitalStaffTabPane.fxml"));
//        root=loader.load();
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }
    
    public HospitalStaffTabPane(){
        try {
            HospitalDoctor doctor=new HospitalDoctor();
            HospitalNurse nurse=new HospitalNurse();
            HospitalLabTechnician labTech=new HospitalLabTechnician();
            HospitalPharmacist pharmacist=new HospitalPharmacist();
            
            loader = new FXMLLoader(getClass().getResource("HospitalStaffTabPane.fxml"));
            root=loader.load();
            
            HospitalStaffTabPaneController tab=loader.getController();
            tab.setRoots(doctor.getFxml(), nurse.getFxml(), labTech.getFxml(), pharmacist.getFxml());
            
            stackPaneDoc=doctor.getStackPane();
            stackPaneNurse=nurse.getStackPane();
            stackPaneLabTech=labTech.getStackPane();
            stackPanePhar=pharmacist.getStackPane();
            
            tab.openDoctorModule();
        } catch (Exception e) {
        }
    }
    
    public Parent getFxml(){
        return root;
    }

    public StackPane getStackPaneDoc() {
        return stackPaneDoc;
    }

    public StackPane getStackPaneNurse() {
        return stackPaneNurse;
    }

    public StackPane getStackPanePhar() {
        return stackPanePhar;
    }

    public StackPane getStackPaneLabTech() {
        return stackPaneLabTech;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
