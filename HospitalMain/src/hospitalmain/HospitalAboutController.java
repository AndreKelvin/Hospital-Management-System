/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalmain;

import java.awt.Desktop;
import java.net.URL;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class HospitalAboutController {

    @FXML
    private Label label;
    
    @FXML
    private void faceBookContact() {
        try {
            Desktop.getDesktop().browse(new URL("https://web.facebook.com/okpoh.kelvin?_rdc=1&_rdr").toURI());
        } catch (Exception e) {
        }
    }

    @FXML
    private void gmailContact() {
        try {
            Desktop.getDesktop().browse(new URL("https://mail.google.com/mail/u/0/?view=cm&fs=1&to=andrekelvin8@gmail.com").toURI());
        } catch (Exception e) {
        }
    }

    @FXML
    private void instagramContact() {
        try {
            Desktop.getDesktop().browse(new URL("https://www.instagram.com/itzdrekel").toURI());
        } catch (Exception e) {
        }
    }

    @FXML
    private void twitterContact() {
        try {
            Desktop.getDesktop().browse(new URL("https://twitter.com/itzdrekel").toURI());
        } catch (Exception e) {
        }
    }

    @FXML
    private void whatsAppContact() {
        FadeTransition fade=new FadeTransition(Duration.millis(20000), label);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.play();
    }    
    
}
