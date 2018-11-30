/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitaltreatment;

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
public class HospitalTreatment extends Application {
    
    private Parent root;
    private StackPane stackPane;
    private FXMLLoader loader;
    
    @Override
    public void start(Stage stage) throws Exception {
//        HospitalDB.initDB();
//        root = FXMLLoader.load(getClass().getResource("HospitalTreatment.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }
    
    public HospitalTreatment(){
        try {
            HospitalDB.initDB();
            loader=new FXMLLoader(getClass().getResource("HospitalTreatment.fxml"));
            root=loader.load();
            
            HospitalTreatmentController treat=loader.getController();
            stackPane=treat.getStackPane();
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
