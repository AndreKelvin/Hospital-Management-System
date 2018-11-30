/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalmain;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import hospitaldb.HospitalDB;
import hospitaldialog.HospitalDialog;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Andre Kelvin
 */
public class HospitalLoginController implements Initializable {

    @FXML
    private StackPane stackPane;
    @FXML
    private JFXPasswordField textPassword;
    @FXML
    private JFXTextField textUnMaskPassword;
    @FXML
    private JFXCheckBox checkBox;
    private Parent rootCreate, rootChange;
    private PreparedStatement ps;
    private ResultSet rs;
    @FXML
    private Label labelCreatePassword;
    private String dbPassword;
    private Stage mainStage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // Bind properties. textField and passwordField
            // visibility and managability properties mutually when checkbox's state is changed.
            // Because i want to display only one component (textField or passwordField)
            // on the scene at a time.
            textUnMaskPassword.managedProperty().bind(checkBox.selectedProperty());
            textUnMaskPassword.visibleProperty().bind(checkBox.selectedProperty());

            textPassword.managedProperty().bind(checkBox.selectedProperty().not());
            textPassword.visibleProperty().bind(checkBox.selectedProperty().not());

            disableCreatePassword();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getPasswordValue() throws SQLException {
        ps = HospitalDB.getCon().prepareStatement("Select * from HospitalLogin");
        rs = ps.executeQuery();

        if (rs.next()) {
            dbPassword = rs.getString("Password");
        }
        return dbPassword;
    }

    /**
     * Disable the Create Password Label So that user can only create password
     * once if they have already created one
     *
     * @throws SQLException
     */
    private void disableCreatePassword() throws SQLException {
        if (getPasswordValue().contentEquals("")) {

        } else {
            labelCreatePassword.setDisable(true);
        }
    }

    /**
     * This Method set Main Stage to be displayed when user login
     *
     * @param stage
     */
    public void setStage(Stage stage) {
        mainStage = stage;
    }

    public Stage getLoginStage() {
        return (Stage) textPassword.getScene().getWindow();
    }

    @FXML
    private void login() {
        try {
            String password = textPassword.getText();
            if (password.isEmpty() || !getPasswordValue().contentEquals(password)) {
                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "invalid password", textPassword.getScene().getWindow());
            } 
            else {
                mainStage.getIcons().add(new Image(getClass().getResourceAsStream("icon/hms.png")));
                
                mainStage.setOnCloseRequest((WindowEvent event) -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Exit");
                    alert.setContentText("Are you sure you want to exit?");

                    ButtonType Yes = new ButtonType("Yes");
                    ButtonType No = new ButtonType("No");

                    //Remove the current Buttons and Add "Yes" "No" Buttons to Alert Dialog
                    alert.getButtonTypes().setAll(Yes, No);

                    Optional<ButtonType> option = alert.showAndWait();
                    if (option.get().equals(Yes)) {
                        HospitalDB.closeDB();
                        System.exit(0);
                    } else {
                        event.consume();
                    }
                });
                
                mainStage.setTitle("Hospital Management System");
                mainStage.show();

                textPassword.clear();
                textUnMaskPassword.clear();

                //Hide Login Stage
                textPassword.getScene().getWindow().hide();
            }
        } catch (Exception e) {
        }
    }

    @FXML
    private void openChangePasswordDialog() {
        try {
            if (rootChange == null) {
                rootChange = FXMLLoader.load(getClass().getResource("ChangePassword.fxml"));
            }
            HospitalDialog.showDialog(stackPane, rootChange);
        } catch (Exception e) {
        }
    }

    @FXML
    private void openCreatePasswordDialog() {
        try {
            if (rootCreate == null) {
                rootCreate = FXMLLoader.load(getClass().getResource("CreatePassword.fxml"));
            }
            HospitalDialog.showDialog(stackPane, rootCreate);

            HospitalDialog.dialog.setOnDialogClosed((JFXDialogEvent event) -> {
                try {
                    disableCreatePassword();
                } catch (SQLException ex) {

                }
            });
        } catch (Exception e) {
        }
    }

    @FXML
    private void showOrHidePassword() {
        // Bind the textField and passwordField text values bidirectionally.
        textUnMaskPassword.textProperty().bindBidirectional(textPassword.textProperty());
    }

}
