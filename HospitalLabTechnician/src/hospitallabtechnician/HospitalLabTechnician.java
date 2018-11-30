/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitallabtechnician;

import hospitaldb.HospitalDB;
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
public class HospitalLabTechnician extends Application {
    
    private Parent root;
    private StackPane stackPane;
    private FXMLLoader loader;
    
    @Override
    public void start(Stage stage) throws Exception {
//        HospitalDB.initDB();
//        Parent root = FXMLLoader.load(getClass().getResource("HospitalLabTechnician.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }
    
    public HospitalLabTechnician(){
        try {
            HospitalDB.initDB();
            loader=new FXMLLoader(getClass().getResource("HospitalLabTechnician.fxml"));
            root=loader.load();
            
            HospitalLabTechnicianController lab=loader.getController();
            stackPane=lab.getStackPane();
        } catch (Exception e) {
        }
    }
    
    public Parent getFxml(){
        return root;
    }
    
    public StackPane getStackPane() {
        return stackPane;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
