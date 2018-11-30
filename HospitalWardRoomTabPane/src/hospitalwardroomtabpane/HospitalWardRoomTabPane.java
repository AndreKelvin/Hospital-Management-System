/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalwardroomtabpane;

import hospitalroom.HospitalRoom;
import hospitalward.HospitalWard;
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
public class HospitalWardRoomTabPane extends Application {
    
    private Parent root;
    private FXMLLoader loader;
    private StackPane stackPaneRoom,stackPaneWard;
    
    @Override
    public void start(Stage stage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("HospitalWardRoomTabPane.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }
    
    public HospitalWardRoomTabPane(){
        try {
            HospitalWard ward=new HospitalWard();
            HospitalRoom room=new HospitalRoom();
            
            loader=new FXMLLoader(getClass().getResource("HospitalWardRoomTabPane.fxml"));
            root=loader.load();
            
            HospitalWardRoomTabPaneController tab=loader.getController();
            tab.setRoots(ward.getFxml(), room.getFxml());
            
            stackPaneRoom=room.getStackPane();
            stackPaneWard=ward.getStackPane();
            
            tab.openWardModule();
        } catch (Exception e) {
        }
    }
    
    public Parent getFxml(){
        return root;
    }

    public StackPane getStackPaneRoom() {
        return stackPaneRoom;
    }

    public StackPane getStackPaneWard() {
        return stackPaneWard;
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
