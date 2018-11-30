/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitaldoctor;

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
public class HospitalDoctor extends Application {
    
    private Parent root;
    private StackPane stackPane;
    private FXMLLoader loader;
    
    @Override
    public void start(Stage stage) throws Exception {
//        HospitalDB.initDB();
//        root = FXMLLoader.load(getClass().getResource("HospitalDoctor.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }
    
    public HospitalDoctor(){
        try {
            HospitalDB.initDB();
            loader=new FXMLLoader(getClass().getResource("HospitalDoctor.fxml"));
            root=loader.load();
            
            HospitalDoctorController doc=loader.getController();
            stackPane=doc.getStackPane();
        } catch (Exception e) {e.printStackTrace();
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
