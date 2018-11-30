/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitallauncher;

import com.sun.javafx.application.LauncherImpl;
import hospitalmain.HospitalMain;
import hospitalpreloader.HospitalPreloader;

/**
 *
 * @author Andre Kelvin
 */
public class HospitalLauncher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LauncherImpl.launchApplication(HospitalMain.class, HospitalPreloader.class, args);
    }
    
}
