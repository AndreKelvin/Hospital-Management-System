/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalmain;

import hospitalappointment.HospitalAppointment;
import hospitalbill.HospitalBill;
import hospitalbloodtabpane.HospitalBloodTabPane;
import hospitaldb.HospitalDB;
import hospitaldeathbirthtabpane.HospitalDeathBirthTabPane;
import hospitaldepartment.HospitalDepartment;
import hospitaldrug.HospitalDrug;
import hospitalinfo.HospitalInfo;
import hospitalpatient.HospitalPatient;
import hospitalreport.HospitalReport;
import hospitalstafftabpane.HospitalStaffTabPane;
import hospitaltreatmenttabpane.HospitalTreatmentTabPane;
import hospitalwardroomtabpane.HospitalWardRoomTabPane;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Andre Kelvin
 */
public class HospitalMain extends Application {

    private Parent root,rootLogin;
    private FXMLLoader loader,loaderLogin;
    private Stage mainStage,loginStage;
    private HospitalMainController main;
    
    @Override
    public void init() throws Exception {
        //Create Database For the First Time in the user home directory
        File file = new File(System.getProperty("user.home") + File.separator + "HospitalDB");

            if (!file.exists()) {
                file.mkdir();
                System.setProperty("derby.system.home", System.getProperty("user.home") + File.separator + "HospitalDB");
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                Connection connect = DriverManager.getConnection("jdbc:derby:Hospital;create=true;");
                
                Statement stm = connect.createStatement();
                stm.executeUpdate("create table APPOINTMENT (" +
                            "	DOCTOR VARCHAR(255) not null," +
                            "	PATIENT VARCHAR(255) not null," +
                            "	DATE VARCHAR(255) not null," +
                            "	START VARCHAR(8) not null," +
                            "	ENDING VARCHAR(8) not null," +
                            "	STATUS VARCHAR(7) not null )");
                
                stm.executeUpdate("create table BILL (" +
                            "	PATIENT_NAME VARCHAR(255) not null," +
                            "	DESCRIPTION LONG VARCHAR not null," +
                            "	AMOUNT INTEGER not null," +
                            "	TOTAL VARCHAR(10) not null )" +
                            ")");
                
                stm.executeUpdate("create table BIRTH (" +
                            "	BABY VARCHAR(255) not null," +
                            "	MOTHER VARCHAR(255) not null," +
                            "	FATHER VARCHAR(255) not null," +
                            "	DATE DATE not null," +
                            "	TIME VARCHAR(255) not null," +
                            "	DOCTOR VARCHAR(255) not null," +
                            "	GENDER CHAR(1) not null," +
                            "	WEIGHT VARCHAR(7) not null," +
                            "	BLOOD_GROUP VARCHAR(3) not null )");
                
                stm.executeUpdate("create table BLOOD_DONOR (" +
                            "	ID INTEGER Generate Always as Identity (start with 1 increment by 1) not null," +
                            "	NAME VARCHAR(255) not null," +
                            "	AGE DATE not null," +
                            "	GENDER CHAR(1) not null," +
                            "	BLOOD_GROUP VARCHAR(3) not null," +
                            "	DONATION_DATE DATE not null," +
                            "	PHONE VARCHAR(15) not null," +
                            "	EMAIL VARCHAR(100)," +
                            "	ADDRESS VARCHAR(255) not null )");
                
                stm.executeUpdate("create table BLOOD_GROUP (" +
                            "	NAME VARCHAR(3) not null," +
                            "	BAGS INTEGER not null )");
                
                stm.executeUpdate("create table DEATH (" +
                            "	ID VARCHAR(10) not null," +
                            "	NAME VARCHAR(255) not null," +
                            "	DATE DATE not null," +
                            "	TIME VARCHAR(8) not null," +
                            "	COURSE_OF_DEATH VARCHAR(255) not null," +
                            "	TREATMENT VARCHAR(3) not null," +
                            "	GENDER VARCHAR(1) not null )");
                
                stm.executeUpdate("create table DEPARTMENT (" +
                            "	NAME VARCHAR(255) not null," +
                            "	DESCRIPTION LONG VARCHAR not null )");
                
                stm.executeUpdate("create table DISCHARGED_DATE (" +
                            "	PATIENT_NAME VARCHAR(255) not null," +
                            "	DATE DATE not null )");
                
                stm.executeUpdate("create table DOCTOR (" +
                            "	ID INTEGER Generate Always as Identity (start with 1 increment by 1) not null," +
                            "	NAME VARCHAR(255) not null," +
                            "	DATE_OF_BIRTH DATE not null," +
                            "	GENDER CHAR(1) not null," +
                            "	ADDRESS VARCHAR(255) not null," +
                            "	CITY VARCHAR(100) not null," +
                            "	PHONE VARCHAR(15) not null," +
                            "	MARITAL_STATUS VARCHAR(7) not null," +
                            "	DATE_JOINED VARCHAR(10) not null," +
                            "	SALARY VARCHAR(10) not null," +
                            "	EMAIL VARCHAR(100) not null," +
                            "	DEPARTMENT VARCHAR(255) not null," +
                            "	DEGREE VARCHAR(10) not null," +
                            "	PHOTO VARCHAR(255) not null )");
                
                stm.executeUpdate("create table DRUG (" +
                            "	ID INTEGER Generate Always as Identity (start with 1 increment by 1) not null," +
                            "	NAME VARCHAR(255) not null," +
                            "	DESCRIPTION LONG VARCHAR not null," +
                            "	CATEGORY VARCHAR(255) not null," +
                            "	QUANTITY INTEGER not null," +
                            "	PRICE INTEGER not null," +
                            "	MANUFACTURER VARCHAR(255) not null," +
                            "	PHOTO VARCHAR(255) )");
                
                stm.executeUpdate("create table DRUG_CATEGORY (" +
                            "	CATEGORY_NAME VARCHAR(255) not null )");
                
                stm.executeUpdate("create table INFO (" +
                            "	HOSPITAL_NAME VARCHAR(255) not null," +
                            "	ADDRESS VARCHAR(255) not null," +
                            "	COUNTRY VARCHAR(200) not null," +
                            "	PHONE INTEGER not null," +
                            "	LOGO VARCHAR(255) not null )");
                
                stm.executeUpdate("create table LABTECHNICIAN (" +
                            "	ID INTEGER Generate Always as Identity (start with 1 increment by 1) not null," +
                            "	NAME VARCHAR(255) not null," +
                            "	DATE_OF_BIRTH DATE not null," +
                            "	GENDER CHAR(1) not null," +
                            "	ADDRESS VARCHAR(255) not null," +
                            "	CITY VARCHAR(100) not null," +
                            "	PHONE VARCHAR(15) not null," +
                            "	MARITAL_STATUS VARCHAR(7) not null," +
                            "	DATE_JOINED VARCHAR(10) not null," +
                            "	SALARY VARCHAR(10) not null," +
                            "	EMAIL VARCHAR(100) not null," +
                            "	DEPARTMENT VARCHAR(255) not null," +
                            "	DEGREE VARCHAR(10) not null," +
                            "	PHOTO VARCHAR(255) not null )");
                
                stm.executeUpdate("create table NOTIFICATION (" +
                            "	REMINDER VARCHAR(22) not null," +
                            "	DOCTOR VARCHAR(255) not null," +
                            "	PATIENT VARCHAR(255) not null )");
                
                stm.executeUpdate("create table NURSE (" +
                            "	ID INTEGER Generate Always as Identity (start with 1 increment by 1) not null," +
                            "	NAME VARCHAR(255) not null," +
                            "	DATE_OF_BIRTH DATE not null," +
                            "	GENDER CHAR(1) not null," +
                            "	ADDRESS VARCHAR(255) not null," +
                            "	CITY VARCHAR(100) not null," +
                            "	PHONE VARCHAR(15) not null," +
                            "	MARITAL_STATUS VARCHAR(7) not null," +
                            "	DATE_JOINED VARCHAR(10) not null," +
                            "	SALARY VARCHAR(10) not null," +
                            "	EMAIL VARCHAR(100) not null," +
                            "	DEPARTMENT VARCHAR(255) not null," +
                            "	DEGREE VARCHAR(10) not null," +
                            "	PHOTO VARCHAR(255) not null )");
                
                stm.executeUpdate("create table PATIENT (" +
                            "	ID INTEGER Generate Always as Identity (start with 1 increment by 1) not null," +
                            "	PATIENT_NAME VARCHAR(255) not null," +
                            "	ADMITED_DATE DATE not null," +
                            "	DATE_OF_BIRTH DATE not null," +
                            "	GENDER CHAR(1) not null," +
                            "	MARITAL_STATUS VARCHAR(10) not null," +
                            "	ADDRESS VARCHAR(255) not null," +
                            "	CITY VARCHAR(100) not null," +
                            "	PHONE VARCHAR(15)," +
                            "	CATEGORY VARCHAR(17)," +
                            "	WARD VARCHAR(255)," +
                            "	BED INTEGER," +
                            "	ROOM INTEGER," +
                            "	FLOOR VARCHAR(7)," +
                            "	DIAGNOSIS VARCHAR(255) not null," +
                            "	DOCTOR VARCHAR(255) not null," +
                            "	BLOOD_GROUP VARCHAR(3) not null )");
                
                stm.executeUpdate("create table PATIENT_TREATMENT" +
                            "	PATIENT_NAME VARCHAR(255) not null," +
                            "	DIAGNOSIS VARCHAR(255) not null," +
                            "	TREATMENT_NAME VARCHAR(255) not null," +
                            "	DRUGS VARCHAR(255) not null )");
                
                stm.executeUpdate("create table PHARMACIST" +
                            "	ID INTEGER Generate Always as Identity (start with 1 increment by 1) not null," +
                            "	NAME VARCHAR(255) not null," +
                            "	DATE_OF_BIRTH DATE not null," +
                            "	GENDER CHAR(1) not null," +
                            "	ADDRESS VARCHAR(255) not null," +
                            "	CITY VARCHAR(100) not null," +
                            "	PHONE VARCHAR(15) not null," +
                            "	MARITAL_STATUS VARCHAR(7) not null," +
                            "	DATE_JOINED VARCHAR(10) not null," +
                            "	SALARY VARCHAR(10) not null," +
                            "	EMAIL VARCHAR(100) not null," +
                            "	DEPARTMENT VARCHAR(255) not null," +
                            "	DEGREE VARCHAR(10) not null," +
                            "	PHOTO VARCHAR(255) not null )");
                
                stm.executeUpdate("create table ROOM" +
                            "	FLOOR VARCHAR(7) not null," +
                            "	ROOM INTEGER not null )");
                
                stm.executeUpdate("create table TREATMENT" +
                            "	TREATMENT_NAME VARCHAR(255) not null," +
                            "	PRICE INTEGER not null )");
                
                stm.executeUpdate("create table WARD (" +
                            "	WARD VARCHAR(255) not null," +
                            "	BEDS INTEGER not null )");
                
                stm.executeUpdate("create table HOSPITALLOGIN (" +
                            "	PASSWORD VARCHAR(30) not null )");
                
                HospitalDB.initDB();
                PreparedStatement ps=HospitalDB.getCon().prepareStatement("Insert Into HospitalLogin Values(?)");
                ps.setString(1, "");
                ps.executeUpdate();
            }
        
        HospitalDeathBirthTabPane deathBirthTab=new HospitalDeathBirthTabPane();
        HospitalStaffTabPane staffTabPane=new HospitalStaffTabPane();
        HospitalDepartment department=new HospitalDepartment();
        HospitalBill bill=new HospitalBill();
        HospitalAppointment app=new HospitalAppointment();
        HospitalBloodTabPane blood=new HospitalBloodTabPane();
        HospitalDrug drug=new HospitalDrug();
        HospitalTreatmentTabPane treat=new HospitalTreatmentTabPane();
        HospitalPatient patient=new HospitalPatient();
        HospitalInfo info=new HospitalInfo();
        HospitalWardRoomTabPane wardRoomTabPane=new HospitalWardRoomTabPane();
        HospitalReport report=new HospitalReport();
        
        loader = new FXMLLoader(getClass().getResource("HospitalMain.fxml"));
        root=loader.load();
        
        main=loader.getController();
        
        main.setStaffTabPaneRoot(staffTabPane.getFxml());
        main.setDepartmentRoot(department.getFxml());
        main.setBillRoot(bill.getFxml());
        main.setAppointmentRoot(app.getFxml());
        main.setBloodBankRoot(blood.getFxml());
        main.setPatientRoot(patient.getFxml());
        main.setDrugRoot(drug.getFxml());
        main.setTreatmentTabPaneRoot(treat.getFxml());
        main.setHospitalInfoRoot(info.getFxml());
        main.setDeathBirthTabPaneRoot(deathBirthTab.geFxml());
        main.setWardRoomTabPaneRoot(wardRoomTabPane.getFxml());
        main.setReportRoot(report.getFxml());
        
        //For changing the Theme of Modules That's displayed in TabPanes
        main.setDeathRoot(deathBirthTab.getStackPaneDeath());
        main.setBirthRoot(deathBirthTab.getStackPaneBirth());
        
        main.setBloodGroupRoot(blood.getStackPaneBloodGruop());
        main.setBloodDonorRoot(blood.getStackPaneBloodDonor());
        
        main.setWardRoot(wardRoomTabPane.getStackPaneWard());
        main.setRoomRoot(wardRoomTabPane.getStackPaneRoom());
        
        main.setTreatRoot(treat.getStackPaneTreat());
        main.setPatientTreatRoot(treat.getStackPanePatientTreat());
        
        main.setDoctorRoot(staffTabPane.getStackPaneDoc());
        main.setNurseRoot(staffTabPane.getStackPaneNurse());
        main.setLabTechRoot(staffTabPane.getStackPaneLabTech());
        main.setPharmacistRoot(staffTabPane.getStackPanePhar());
    }
    
    
    
    @Override
    public void start(Stage stage) throws Exception {
        loaderLogin = new FXMLLoader(getClass().getResource("HospitalLogin.fxml"));
        rootLogin=loaderLogin.load();
        
        mainStage=new Stage();
        Scene mainScene=new Scene(root);
        mainStage.setScene(mainScene);
        Platform.runLater(() -> root.requestFocus());
        
        //get Login Controller and pass Main Stage
        HospitalLoginController login=loaderLogin.getController();
        login.setStage(mainStage);
        
        Scene scene = new Scene(rootLogin);
        
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon/hms.png")));
        stage.setOnCloseRequest((WindowEvent event) -> {
            System.exit(0);
        });
        stage.setResizable(false);
        stage.setTitle("Hospital Management System");
        stage.show();
        
        //get Login Stage and pass it to Main Controller
        loginStage=login.getLoginStage();
        main.setLoginStage(loginStage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
