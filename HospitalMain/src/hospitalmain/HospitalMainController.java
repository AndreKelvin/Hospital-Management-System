/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalmain;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import hospitaldb.HospitalDB;
import hospitaldialog.HospitalDialog;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Andre Kelvin
 */
public class HospitalMainController implements Initializable {

    @FXML
    private StackPane stackPane;
    @FXML
    private BorderPane borderPane;
    @FXML
    private JFXButton buttonTheme;
    @FXML
    private VBox vBoxDashBoard;
    @FXML
    private Label labelTotalPatient;
    @FXML
    private Label labelTotalDoctor, labelTotalNurse, labelTotalPhar, labelTotalLabTech, labelTotalWard, labelTotalRoom, labelTotalBirth, labelTotalDeath, labelTotalDept;
    private JFXPopup popup;
    private Parent rootDepartment, rootStaffTabPane, rootPatient, rootAppointment, rootBloodBank, rootBill, rootDrug, rootHospitalInfo, rootTreatmentTabPane;
    private Parent rootDeathBirthTabPane, rootWardRoomTabPane, rootReport, rootDeath, rootBirth, rootBloodGroup, rootBloodDonor, rootWard, rootRoom, rootTreat;
    private Parent rootPatientTreat, rootDoctor, rootNurse, rootPharmacist, rootLabTech, rootAbout;
    private Stage loginStage;

    private void displayTotalDashBoardValues() {
        try {
            PreparedStatement ps = HospitalDB.getCon().prepareStatement("SELECT Count(Patient_Name) FROM PATIENT where CATEGORY=? "
                    + "Union All "
                    + "SELECT Count(Name) FROM DOCTOR "
                    + "Union All "
                    + "SELECT Count(Name) FROM NURSE "
                    + "Union All "
                    + "SELECT Count(Name) FROM LABTECHNICIAN "
                    + "Union All "
                    + "SELECT Count(Name) FROM PHARMACIST "
                    + "Union All "
                    + "SELECT Count(Ward) FROM WARD "
                    + "Union All "
                    + "SELECT Sum(Room) FROM ROOM "
                    + "Union All "
                    + "SELECT Count(Baby) FROM BIRTH "
                    + "Union All "
                    + "SELECT Count(Name) FROM DEATH "
                    + "Union All "
                    + "SELECT Count(Name) FROM DEPARTMENT");
            ps.setString(1, "In Patient");
            ResultSet rs = ps.executeQuery();

            rs.next();
            labelTotalPatient.setText(rs.getString(1));
            rs.next();
            labelTotalDoctor.setText(rs.getString(1));
            rs.next();
            labelTotalNurse.setText(rs.getString(1));
            rs.next();
            labelTotalLabTech.setText(rs.getString(1));
            rs.next();
            labelTotalPhar.setText(rs.getString(1));
            rs.next();
            labelTotalWard.setText(rs.getString(1));
            rs.next();
            labelTotalRoom.setText(rs.getString(1));
            rs.next();
            labelTotalBirth.setText(rs.getString(1));
            rs.next();
            labelTotalDeath.setText(rs.getString(1));
            rs.next();
            labelTotalDept.setText(rs.getString(1));
        } catch (Exception e) {
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayTotalDashBoardValues();

        popup = new JFXPopup();

        Label blue = new Label("Blue");
        Label green = new Label("Green");
        Label red = new Label("Red");
        Label orange = new Label("Orange");
        Label purple = new Label("Purple");

        blue.setPadding(new Insets(10));
        green.setPadding(new Insets(10));
        red.setPadding(new Insets(10));
        orange.setPadding(new Insets(10));
        purple.setPadding(new Insets(10));

        VBox vBox = new VBox(blue, green, red, orange, purple);

        popup.setPopupContent(vBox);

        //Include pointing hand cursor to popup labels
        blue.getStylesheets().setAll(getClass().getResource("PopupLabel.css").toExternalForm());
        green.getStylesheets().setAll(getClass().getResource("PopupLabel.css").toExternalForm());
        red.getStylesheets().setAll(getClass().getResource("PopupLabel.css").toExternalForm());
        orange.getStylesheets().setAll(getClass().getResource("PopupLabel.css").toExternalForm());
        purple.getStylesheets().setAll(getClass().getResource("PopupLabel.css").toExternalForm());

        //Attach Image to label
        Image imageBlue = new Image(getClass().getResourceAsStream("icon/blue.png"));
        blue.setGraphic(new ImageView(imageBlue));

        Image imageGreen = new Image(getClass().getResourceAsStream("icon/green.jpg"));
        green.setGraphic(new ImageView(imageGreen));

        Image imageRed = new Image(getClass().getResourceAsStream("icon/redd.jpg"));
        red.setGraphic(new ImageView(imageRed));

        Image imageOrange = new Image(getClass().getResourceAsStream("icon/orange.png"));
        orange.setGraphic(new ImageView(imageOrange));

        Image imagePurple = new Image(getClass().getResourceAsStream("icon/purple.png"));
        purple.setGraphic(new ImageView(imagePurple));

        //Clicking on any popup Label. Changes the Theme
        blue.setOnMouseClicked((MouseEvent event) -> {
            try {
                stackPane.getStylesheets().setAll(getClass().getResource("blue.css").toExternalForm());
                rootReport.getStylesheets().setAll(getClass().getResource("blue.css").toExternalForm());
                rootPatient.getStylesheets().setAll(getClass().getResource("blue.css").toExternalForm());

                rootDeathBirthTabPane.getStylesheets().setAll(getClass().getResource("blue.css").toExternalForm());
                rootDeath.getStylesheets().setAll(getClass().getResource("blue.css").toExternalForm());
                rootBirth.getStylesheets().setAll(getClass().getResource("blue.css").toExternalForm());

                rootBloodBank.getStylesheets().setAll(getClass().getResource("blue.css").toExternalForm());
                rootBloodGroup.getStylesheets().setAll(getClass().getResource("blue.css").toExternalForm());
                rootBloodDonor.getStylesheets().setAll(getClass().getResource("blue.css").toExternalForm());

                rootWardRoomTabPane.getStylesheets().setAll(getClass().getResource("blue.css").toExternalForm());
                rootWard.getStylesheets().setAll(getClass().getResource("blue.css").toExternalForm());
                rootRoom.getStylesheets().setAll(getClass().getResource("blue.css").toExternalForm());

                rootTreatmentTabPane.getStylesheets().setAll(getClass().getResource("blue.css").toExternalForm());
                rootTreat.getStylesheets().setAll(getClass().getResource("blue.css").toExternalForm());
                rootPatientTreat.getStylesheets().setAll(getClass().getResource("blue.css").toExternalForm());

                rootStaffTabPane.getStylesheets().setAll(getClass().getResource("blue.css").toExternalForm());
                rootDoctor.getStylesheets().setAll(getClass().getResource("blue.css").toExternalForm());
                rootNurse.getStylesheets().setAll(getClass().getResource("blue.css").toExternalForm());
                rootPharmacist.getStylesheets().setAll(getClass().getResource("blue.css").toExternalForm());
                rootLabTech.getStylesheets().setAll(getClass().getResource("blue.css").toExternalForm());

                rootDepartment.getStylesheets().setAll(getClass().getResource("blue.css").toExternalForm());
                rootBill.getStylesheets().setAll(getClass().getResource("blue.css").toExternalForm());
                rootDrug.getStylesheets().setAll(getClass().getResource("blue.css").toExternalForm());
                rootAppointment.getStylesheets().setAll(getClass().getResource("blue.css").toExternalForm());
                rootHospitalInfo.getStylesheets().setAll(getClass().getResource("blue.css").toExternalForm());
            } catch (Exception e) {
            }
        });

        green.setOnMouseClicked((MouseEvent event) -> {
            try {
                stackPane.getStylesheets().setAll(getClass().getResource("Green.css").toExternalForm());
                rootReport.getStylesheets().setAll(getClass().getResource("Green.css").toExternalForm());
                rootPatient.getStylesheets().setAll(getClass().getResource("Green.css").toExternalForm());

                rootDeathBirthTabPane.getStylesheets().setAll(getClass().getResource("Green.css").toExternalForm());
                rootDeath.getStylesheets().setAll(getClass().getResource("Green.css").toExternalForm());
                rootBirth.getStylesheets().setAll(getClass().getResource("Green.css").toExternalForm());

                rootBloodBank.getStylesheets().setAll(getClass().getResource("Green.css").toExternalForm());
                rootBloodGroup.getStylesheets().setAll(getClass().getResource("Green.css").toExternalForm());
                rootBloodDonor.getStylesheets().setAll(getClass().getResource("Green.css").toExternalForm());

                rootWardRoomTabPane.getStylesheets().setAll(getClass().getResource("Green.css").toExternalForm());
                rootWard.getStylesheets().setAll(getClass().getResource("Green.css").toExternalForm());
                rootRoom.getStylesheets().setAll(getClass().getResource("Green.css").toExternalForm());

                rootTreatmentTabPane.getStylesheets().setAll(getClass().getResource("Green.css").toExternalForm());
                rootTreat.getStylesheets().setAll(getClass().getResource("Green.css").toExternalForm());
                rootPatientTreat.getStylesheets().setAll(getClass().getResource("Green.css").toExternalForm());

                rootStaffTabPane.getStylesheets().setAll(getClass().getResource("Green.css").toExternalForm());
                rootDoctor.getStylesheets().setAll(getClass().getResource("Green.css").toExternalForm());
                rootNurse.getStylesheets().setAll(getClass().getResource("Green.css").toExternalForm());
                rootPharmacist.getStylesheets().setAll(getClass().getResource("Green.css").toExternalForm());
                rootLabTech.getStylesheets().setAll(getClass().getResource("Green.css").toExternalForm());

                rootDepartment.getStylesheets().setAll(getClass().getResource("Green.css").toExternalForm());
                rootBill.getStylesheets().setAll(getClass().getResource("Green.css").toExternalForm());
                rootDrug.getStylesheets().setAll(getClass().getResource("Green.css").toExternalForm());
                rootAppointment.getStylesheets().setAll(getClass().getResource("Green.css").toExternalForm());
                rootHospitalInfo.getStylesheets().setAll(getClass().getResource("Green.css").toExternalForm());
            } catch (Exception e) {
            }
        });

        red.setOnMouseClicked((MouseEvent event) -> {
            try {
                stackPane.getStylesheets().setAll(getClass().getResource("Main.css").toExternalForm());
                rootReport.getStylesheets().setAll(getClass().getResource("Main.css").toExternalForm());
                rootPatient.getStylesheets().setAll(getClass().getResource("Main.css").toExternalForm());

                rootDeathBirthTabPane.getStylesheets().setAll(getClass().getResource("Main.css").toExternalForm());
                rootDeath.getStylesheets().setAll(getClass().getResource("Main.css").toExternalForm());
                rootBirth.getStylesheets().setAll(getClass().getResource("Main.css").toExternalForm());

                rootBloodBank.getStylesheets().setAll(getClass().getResource("Main.css").toExternalForm());
                rootBloodGroup.getStylesheets().setAll(getClass().getResource("Main.css").toExternalForm());
                rootBloodDonor.getStylesheets().setAll(getClass().getResource("Main.css").toExternalForm());

                rootWardRoomTabPane.getStylesheets().setAll(getClass().getResource("Main.css").toExternalForm());
                rootWard.getStylesheets().setAll(getClass().getResource("Main.css").toExternalForm());
                rootRoom.getStylesheets().setAll(getClass().getResource("Main.css").toExternalForm());

                rootTreatmentTabPane.getStylesheets().setAll(getClass().getResource("Main.css").toExternalForm());
                rootTreat.getStylesheets().setAll(getClass().getResource("Main.css").toExternalForm());
                rootPatientTreat.getStylesheets().setAll(getClass().getResource("Main.css").toExternalForm());

                rootStaffTabPane.getStylesheets().setAll(getClass().getResource("Main.css").toExternalForm());
                rootDoctor.getStylesheets().setAll(getClass().getResource("Main.css").toExternalForm());
                rootNurse.getStylesheets().setAll(getClass().getResource("Main.css").toExternalForm());
                rootPharmacist.getStylesheets().setAll(getClass().getResource("Main.css").toExternalForm());
                rootLabTech.getStylesheets().setAll(getClass().getResource("Main.css").toExternalForm());

                rootDepartment.getStylesheets().setAll(getClass().getResource("Main.css").toExternalForm());
                rootBill.getStylesheets().setAll(getClass().getResource("Main.css").toExternalForm());
                rootDrug.getStylesheets().setAll(getClass().getResource("Main.css").toExternalForm());
                rootAppointment.getStylesheets().setAll(getClass().getResource("Main.css").toExternalForm());
                rootHospitalInfo.getStylesheets().setAll(getClass().getResource("Main.css").toExternalForm());
            } catch (Exception e) {
            }
        });

        orange.setOnMouseClicked((MouseEvent event) -> {
            try {
                stackPane.getStylesheets().setAll(getClass().getResource("Orange.css").toExternalForm());
                rootReport.getStylesheets().setAll(getClass().getResource("Orange.css").toExternalForm());
                rootPatient.getStylesheets().setAll(getClass().getResource("Orange.css").toExternalForm());

                rootDeathBirthTabPane.getStylesheets().setAll(getClass().getResource("Orange.css").toExternalForm());
                rootDeath.getStylesheets().setAll(getClass().getResource("Orange.css").toExternalForm());
                rootBirth.getStylesheets().setAll(getClass().getResource("Orange.css").toExternalForm());

                rootBloodBank.getStylesheets().setAll(getClass().getResource("Orange.css").toExternalForm());
                rootBloodGroup.getStylesheets().setAll(getClass().getResource("Orange.css").toExternalForm());
                rootBloodDonor.getStylesheets().setAll(getClass().getResource("Orange.css").toExternalForm());

                rootWardRoomTabPane.getStylesheets().setAll(getClass().getResource("Orange.css").toExternalForm());
                rootWard.getStylesheets().setAll(getClass().getResource("Orange.css").toExternalForm());
                rootRoom.getStylesheets().setAll(getClass().getResource("Orange.css").toExternalForm());

                rootTreatmentTabPane.getStylesheets().setAll(getClass().getResource("Orange.css").toExternalForm());
                rootTreat.getStylesheets().setAll(getClass().getResource("Orange.css").toExternalForm());
                rootPatientTreat.getStylesheets().setAll(getClass().getResource("Orange.css").toExternalForm());

                rootStaffTabPane.getStylesheets().setAll(getClass().getResource("Orange.css").toExternalForm());
                rootDoctor.getStylesheets().setAll(getClass().getResource("Orange.css").toExternalForm());
                rootNurse.getStylesheets().setAll(getClass().getResource("Orange.css").toExternalForm());
                rootPharmacist.getStylesheets().setAll(getClass().getResource("Orange.css").toExternalForm());
                rootLabTech.getStylesheets().setAll(getClass().getResource("Orange.css").toExternalForm());

                rootDepartment.getStylesheets().setAll(getClass().getResource("Orange.css").toExternalForm());
                rootBill.getStylesheets().setAll(getClass().getResource("Orange.css").toExternalForm());
                rootDrug.getStylesheets().setAll(getClass().getResource("Orange.css").toExternalForm());
                rootAppointment.getStylesheets().setAll(getClass().getResource("Orange.css").toExternalForm());
                rootHospitalInfo.getStylesheets().setAll(getClass().getResource("Orange.css").toExternalForm());
            } catch (Exception e) {
            }
        });

        purple.setOnMouseClicked((MouseEvent event) -> {
            try {
                stackPane.getStylesheets().setAll(getClass().getResource("Purple.css").toExternalForm());
                rootReport.getStylesheets().setAll(getClass().getResource("Purple.css").toExternalForm());
                rootPatient.getStylesheets().setAll(getClass().getResource("Purple.css").toExternalForm());

                rootDeathBirthTabPane.getStylesheets().setAll(getClass().getResource("Purple.css").toExternalForm());
                rootDeath.getStylesheets().setAll(getClass().getResource("Purple.css").toExternalForm());
                rootBirth.getStylesheets().setAll(getClass().getResource("Purple.css").toExternalForm());

                rootBloodBank.getStylesheets().setAll(getClass().getResource("Purple.css").toExternalForm());
                rootBloodGroup.getStylesheets().setAll(getClass().getResource("Purple.css").toExternalForm());
                rootBloodDonor.getStylesheets().setAll(getClass().getResource("Purple.css").toExternalForm());

                rootWardRoomTabPane.getStylesheets().setAll(getClass().getResource("Purple.css").toExternalForm());
                rootWard.getStylesheets().setAll(getClass().getResource("Purple.css").toExternalForm());
                rootRoom.getStylesheets().setAll(getClass().getResource("Purple.css").toExternalForm());

                rootTreatmentTabPane.getStylesheets().setAll(getClass().getResource("Purple.css").toExternalForm());
                rootTreat.getStylesheets().setAll(getClass().getResource("Purple.css").toExternalForm());
                rootPatientTreat.getStylesheets().setAll(getClass().getResource("Purple.css").toExternalForm());

                rootStaffTabPane.getStylesheets().setAll(getClass().getResource("Purple.css").toExternalForm());
                rootDoctor.getStylesheets().setAll(getClass().getResource("Purple.css").toExternalForm());
                rootNurse.getStylesheets().setAll(getClass().getResource("Purple.css").toExternalForm());
                rootPharmacist.getStylesheets().setAll(getClass().getResource("Purple.css").toExternalForm());
                rootLabTech.getStylesheets().setAll(getClass().getResource("Purple.css").toExternalForm());

                rootDepartment.getStylesheets().setAll(getClass().getResource("Purple.css").toExternalForm());
                rootBill.getStylesheets().setAll(getClass().getResource("Purple.css").toExternalForm());
                rootDrug.getStylesheets().setAll(getClass().getResource("Purple.css").toExternalForm());
                rootAppointment.getStylesheets().setAll(getClass().getResource("Purple.css").toExternalForm());
                rootHospitalInfo.getStylesheets().setAll(getClass().getResource("Purple.css").toExternalForm());
            } catch (Exception e) {
            }
        });
    }

    public void setDepartmentRoot(Parent rootDepartment) {
        this.rootDepartment = rootDepartment;
    }

    public void setStaffTabPaneRoot(Parent rootStaffTabPane) {
        this.rootStaffTabPane = rootStaffTabPane;
    }

    public void setPatientRoot(Parent rootPatient) {
        this.rootPatient = rootPatient;
    }

    public void setAppointmentRoot(Parent rootAppointment) {
        this.rootAppointment = rootAppointment;
    }

    public void setBloodBankRoot(Parent rootBloodBank) {
        this.rootBloodBank = rootBloodBank;
    }

    public void setBillRoot(Parent rootBill) {
        this.rootBill = rootBill;
    }

    public void setDrugRoot(Parent rootDrug) {
        this.rootDrug = rootDrug;
    }

    public void setHospitalInfoRoot(Parent rootHospitalInfo) {
        this.rootHospitalInfo = rootHospitalInfo;
    }

    public void setTreatmentTabPaneRoot(Parent rootTreatmentTabPane) {
        this.rootTreatmentTabPane = rootTreatmentTabPane;
    }

    public void setDeathBirthTabPaneRoot(Parent rootDeathBirthTabPane) {
        this.rootDeathBirthTabPane = rootDeathBirthTabPane;
    }

    public void setWardRoomTabPaneRoot(Parent rootWardRoomTabPane) {
        this.rootWardRoomTabPane = rootWardRoomTabPane;
    }

    public void setReportRoot(Parent rootReport) {
        this.rootReport = rootReport;
    }

    /*
    The Purpose of the Below Codes
    Will be for changing the Theme of Modules That's displayed in TabPanes
     */
    public void setDeathRoot(Parent rootDeath) {
        this.rootDeath = rootDeath;
    }

    public void setBirthRoot(Parent rootBirth) {
        this.rootBirth = rootBirth;
    }

    public void setBloodGroupRoot(Parent rootBloodGroup) {
        this.rootBloodGroup = rootBloodGroup;
    }

    public void setBloodDonorRoot(Parent rootBloodDonor) {
        this.rootBloodDonor = rootBloodDonor;
    }

    public void setWardRoot(Parent rootWard) {
        this.rootWard = rootWard;
    }

    public void setRoomRoot(Parent rootRoom) {
        this.rootRoom = rootRoom;
    }

    public void setTreatRoot(Parent rootTreat) {
        this.rootTreat = rootTreat;
    }

    public void setPatientTreatRoot(Parent rootPatientTreat) {
        this.rootPatientTreat = rootPatientTreat;
    }

    public void setDoctorRoot(Parent rootDoctor) {
        this.rootDoctor = rootDoctor;
    }

    public void setNurseRoot(Parent rootNurse) {
        this.rootNurse = rootNurse;
    }

    public void setPharmacistRoot(Parent rootPharmacist) {
        this.rootPharmacist = rootPharmacist;
    }

    public void setLabTechRoot(Parent rootLabTech) {
        this.rootLabTech = rootLabTech;
    }
    //End

    @FXML
    private void openAboutModule() {
        try {
            if (rootAbout == null) {
                rootAbout = FXMLLoader.load(getClass().getResource("HospitalAbout.fxml"));
            }
            HospitalDialog.showDialog(stackPane, rootAbout);
        } catch (Exception e) {
        }
    }

    @FXML
    private void openAppointmentModule() {
        borderPane.setCenter(rootAppointment);
    }

    @FXML
    private void openBillModule() {
        borderPane.setCenter(rootBill);
    }

    @FXML
    private void openBloodBankModule() {
        borderPane.setCenter(rootBloodBank);
    }

    @FXML
    private void openDeathBirthModule() {
        borderPane.setCenter(rootDeathBirthTabPane);
    }

    @FXML
    private void openDepartmentModule() {
        borderPane.setCenter(rootDepartment);
    }

    @FXML
    private void openDrugModule() {
        borderPane.setCenter(rootDrug);
    }

    @FXML
    private void openHospitalInfoModule() {
        borderPane.setCenter(rootHospitalInfo);
    }

    @FXML
    private void openPatientModule() {
        borderPane.setCenter(rootPatient);
    }

    @FXML
    private void openReportModule() {
        borderPane.setCenter(rootReport);
    }

    @FXML
    private void openStaffModule() {
        borderPane.setCenter(rootStaffTabPane);
    }

    @FXML
    private void openThemeModule() {
        popup.show(buttonTheme, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT);
    }

    @FXML
    private void openTreatmentModule() {
        borderPane.setCenter(rootTreatmentTabPane);
    }

    @FXML
    private void openWardRoomModule() {
        borderPane.setCenter(rootWardRoomTabPane);
    }

    /**
     * This Method set Login Stage to be displayed when user logout
     *
     * @param loginStage
     */
    public void setLoginStage(Stage loginStage) {
        this.loginStage = loginStage;
    }

    @FXML
    private void logoOut() {
        stackPane.getScene().getWindow().hide();

        loginStage.show();
    }

    @FXML
    private void openDashboard() {
        displayTotalDashBoardValues();
        borderPane.setCenter(vBoxDashBoard);
    }

}
