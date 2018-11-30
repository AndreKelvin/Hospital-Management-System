/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitaldialog;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXDialog;
import java.io.IOException;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 *
 * @author Andre Kelvin
 */
public class HospitalDialog {

    public static JFXDialog dialog;
    private static Alert alert;
    private static JFXAlert jfxAlert;

    public static void showDialog(StackPane stackPane, Parent root) throws IOException {
        dialog = new JFXDialog(stackPane, (Region) root, JFXDialog.DialogTransition.TOP);
        dialog.show();
    }

    public static void dialogAlert(Alert.AlertType alertType, String msg, Window window) throws IOException {
        alert = new Alert(alertType);
        alert.setContentText(msg);
        jfxAlert = new JFXAlert((Stage) window);
        jfxAlert.setContent(alert.getDialogPane());
        alert.setOnCloseRequest((DialogEvent event) -> {
            jfxAlert.close();
        });
        jfxAlert.showAndWait();
    }

}
