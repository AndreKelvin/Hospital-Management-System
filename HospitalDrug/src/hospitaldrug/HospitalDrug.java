/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitaldrug;

import hospitaldb.HospitalDB;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Andre Kelvin
 */
public class HospitalDrug extends Application {
    
    private Parent root;
    
    @Override
    public void start(Stage stage) throws Exception {
//        HospitalDB.initDB();
//        root = FXMLLoader.load(getClass().getResource("HospitalDrug.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setOnCloseRequest((WindowEvent event) -> {
            HospitalDB.closeDB();
        });
        stage.show();
    }
    
    public HospitalDrug(){
        try {
            HospitalDB.initDB();
            root = FXMLLoader.load(getClass().getResource("HospitalDrug.fxml"));
        } catch (Exception e) {
        }
    }
    
    public Parent getFxml(){
        return root;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
