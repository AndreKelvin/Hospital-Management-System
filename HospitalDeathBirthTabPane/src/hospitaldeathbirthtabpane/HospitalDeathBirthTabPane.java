/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitaldeathbirthtabpane;

import hospitalbirth.HospitalBirth;
import hospitaldeath.HospitalDeath;
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
public class HospitalDeathBirthTabPane extends Application {
    
    private Parent root;
    private FXMLLoader loader;
    private StackPane stackPaneDeath,stackPaneBirth;
    
    @Override
    public void start(Stage stage) throws Exception {
//        root = FXMLLoader.load(getClass().getResource("HospitalDeathBirthTabPane.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }
    
    public HospitalDeathBirthTabPane(){
        try {
            HospitalDeath death=new HospitalDeath();
            HospitalBirth birth=new HospitalBirth();
            
            loader = new FXMLLoader(getClass().getResource("HospitalDeathBirthTabPane.fxml"));
            root=loader.load();
            
            HospitalDeathBirthTabPaneController tab=loader.getController();
            tab.setRoots(death.getFxml(), birth.getFxml());
            
            stackPaneDeath=death.getStackPane();
            stackPaneBirth=birth.getStackPane();
            
            tab.openDeathModule();
        } catch (Exception e) {
        }
    }
    
    public Parent geFxml(){
        return root;
    }
    
    /**
     * Passing the Root(Stack Pane) from HospitalDeath class to HospitalMain class
     * @return 
     */
    public StackPane getStackPaneDeath(){
        return stackPaneDeath;
    }
    
    /**
     * Passing the Root(Stack Pane) from HospitalBirth class to HospitalMain class
     * @return 
     */
    public StackPane getStackPaneBirth(){
        return stackPaneBirth;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
