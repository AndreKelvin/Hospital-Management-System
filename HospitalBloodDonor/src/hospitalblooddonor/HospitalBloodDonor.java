/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalblooddonor;

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
public class HospitalBloodDonor extends Application {

    private Parent root;
    private FXMLLoader loader;
    private StackPane stackPane;

    @Override
    public void start(Stage stage) throws Exception {
//        HospitalDB.initDB();
//        root = FXMLLoader.load(getClass().getResource("HospitalBloodDonor.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    public HospitalBloodDonor() {
        try {
            HospitalDB.initDB();
            loader=new FXMLLoader(getClass().getResource("HospitalBloodDonor.fxml"));
            root=loader.load();
            
            HospitalBloodDonorController bDonor=loader.getController();
            stackPane=bDonor.getStackPane();
        } catch (Exception e) {
        }
    }

    public Parent getFxml() {
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
