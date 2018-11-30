/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitallabtechnician;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import hospitalbridge.HospitalBridge;
import hospitaldb.HospitalDB;
import hospitaldialog.HospitalDialog;
import hospitalstaff.MainStaff;
import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javax.imageio.ImageIO;

/**
 *
 * @author Andre Kelvin
 */
public class HospitalLabTechnicianController implements Initializable /*extends MainStaff*/ {
    
//    public HospitalLabTechnicianController(){
//        super.setImageFolder("LabTechnicianImage");
//        super.setTableName("LabTechnician");
//        super.setFxml("AddLabTechnician.fxml", "EditLabTechnician.fxml", "DeleteLabTechnician.fxml");
//    }
    
    @FXML
    private StackPane stackPane;
    @FXML
    private JFXComboBox comboDept;
    @FXML
    private JFXListView listView;
    @FXML
    private JFXButton buttonEdit, buttonDelete;
    @FXML
    private Label labelName, labelPhone, labelCity, labelAddress, labelMatStatus, labelGender, labelAge, labelEmail, labelID, labelDateJoined, labelSalary, labelDegree;
    @FXML
    private ImageView imageView;
    @FXML
    private JFXTextField textSearch;
    private FXMLLoader loaderAdd, loaderEdit, loaderDelete;
    private Parent rootAdd, rootEdit, rootDelete;
    private ObservableList obList;
    private PreparedStatement ps;
    private ResultSet rs;
    private BufferedImage buff;
    private Image image, defaultImage;
    private List searchList;
    private SuggestionProvider suggestions;
    private String searchName,selectedTechnicianName;
    private File technicianImageFile;
    private AddLabTechnicianController add;
    private DeleteLabTechnicianController del;
    private EditLabTechnicianController edit;
    private byte age;
    private Calendar calendar;

    
    private void searchLabTech() {
        try {
            searchList.clear();
            ps = HospitalDB.getCon().prepareStatement("Select Name From LabTechnician");
            rs = ps.executeQuery();

            while (rs.next()) {
                searchList.add(rs.getString("Name"));
            }
            ps.close();

            suggestions.clearSuggestions();
            suggestions.addPossibleSuggestions(searchList);
        } catch (Exception e) {
        }
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            new File(System.getProperty("user.home") + File.separator + "LabTechnicianImage").mkdir();
            
            //Pass Department Combo Box
            HospitalBridge.comboBoxDepartmentLabTech=comboDept;
            
            calendar = new GregorianCalendar();
            defaultImage = imageView.getImage();
            searchList = new ArrayList();
            suggestions = SuggestionProvider.create(searchList);
            obList = FXCollections.observableArrayList();
            listView.setItems(obList);
            
            buttonEdit.setDisable(true);
            buttonDelete.setDisable(true);

            searchLabTech();
            //Bind Search Textfield with Suggestions
            AutoCompletionTextFieldBinding autoCompletionTextFieldBinding = new AutoCompletionTextFieldBinding<>(textSearch, suggestions);

            //When Pharmacist Name is Selected thats when the Search Begins
            textSearch.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                searchAction();
            });

            //Populate Department Combo Box
            ps = HospitalDB.getCon().prepareStatement("Select Name From Department");
            rs = ps.executeQuery();

            while (rs.next()) {
                comboDept.getItems().add(rs.getString("Name"));
            }
            ps.close();

            //Display LabTechnician Name on List View When Department is been Selected
            comboDept.getSelectionModel().selectedItemProperty().addListener((ObservableValue observable, Object oldValue, Object newValue) -> {
                try {
                    displayStaffName();

                    //Empty All Info
                    labelID.setText("");
                    labelName.setText("");
                    labelAge.setText("");
                    labelGender.setText("");
                    labelAddress.setText("");
                    labelCity.setText("");
                    labelPhone.setText("");
                    labelMatStatus.setText("");
                    labelDateJoined.setText("");
                    labelSalary.setText("");
                    labelEmail.setText("");
                    labelDegree.setText("");
                    imageView.setImage(defaultImage);
                    
                    buttonEdit.setDisable(true);
                    buttonDelete.setDisable(true);

                } catch (Exception e) {
                }
            });

            //Display Info When LabTechnician Name is been Selected
            listView.getSelectionModel().selectedItemProperty().addListener((ObservableValue observable, Object oldValue, Object newValue) -> {
                try {
                    if (newValue == null) {

                    } 
                    else {
                        selectedTechnicianName=newValue.toString();
                        
                        ps = HospitalDB.getCon().prepareStatement("Select * From LabTechnician Where Name=?");
                        ps.setString(1, selectedTechnicianName);
                        rs = ps.executeQuery();

                        if (rs.next()) {
                            labelID.setText(rs.getInt("ID") + "");
                            labelName.setText(rs.getString("Name"));

                            //Subtract System Date Year with Date_Of_Birth Year to get the LabTechnician Age 
                            age = (byte) (calendar.get(Calendar.YEAR) - rs.getDate("Date_Of_Birth").toLocalDate().getYear());
                            labelAge.setText(age + "");
                            labelGender.setText(rs.getString("Gender"));
                            labelAddress.setText(rs.getString("Address"));
                            labelCity.setText(rs.getString("City"));
                            labelPhone.setText(rs.getString("Phone"));
                            labelMatStatus.setText(rs.getString("Marital_Status"));
                            labelDateJoined.setText(rs.getString("Date_Joined"));
                            labelSalary.setText(rs.getString("Salary"));
                            labelEmail.setText(rs.getString("Email"));
                            labelDegree.setText(rs.getString("Degree"));

                            technicianImageFile = new File(rs.getString("Photo"));
                            buff = ImageIO.read(technicianImageFile);
                            image = SwingFXUtils.toFXImage(buff, null);
                            imageView.setImage(image);
                        }
                        ps.close();
                        
                        textSearch.clear();
                        
                        buttonEdit.setDisable(false);
                        buttonDelete.setDisable(false);
                    }
                } catch (Exception e) {
                }
            });
        } catch (Exception e) {
        }
    }
    
    private void displayStaffName() {
        try {
            obList.clear();
            ps = HospitalDB.getCon().prepareStatement("Select Name From LabTechnician Where Department=?");
            ps.setString(1, comboDept.getValue().toString());
            rs = ps.executeQuery();

            while (rs.next()) {
                obList.add(rs.getString("Name"));
            }
            ps.close();
        } catch (Exception e) {
        }
    }

    private void searchAction() {
        try {
            searchName = textSearch.getText();
            ps = HospitalDB.getCon().prepareStatement("Select Department From LabTechnician Where Name=?");
            ps.setString(1, searchName);
            rs = ps.executeQuery();

            if (rs.next()) {
                comboDept.setValue(rs.getString("Department"));
                listView.getSelectionModel().select(searchName);
            }
            ps.close();
        } catch (Exception e) {
        }
    }

    @FXML
    private void addAction() {
        try {
            if (loaderAdd == null) {
                loaderAdd = new FXMLLoader(getClass().getResource("AddLabTechnician.fxml"));
                rootAdd = loaderAdd.load();
                add=loaderAdd.getController();
            }
            add.setComboBoxItems(comboDept.getItems());
            HospitalDialog.showDialog(stackPane, rootAdd);

            //Refresh List View and Search Suguestion
            HospitalDialog.dialog.setOnDialogClosed((JFXDialogEvent event) -> {
                displayStaffName();
                searchLabTech();
            });
        } catch (Exception e) {
        }
    }

    @FXML
    private void editAction() {
        try {
            if (loaderEdit == null) {
                loaderEdit = new FXMLLoader(getClass().getResource("EditLabTechnician.fxml"));
                rootEdit = loaderEdit.load();
                edit=loaderEdit.getController();
            }
            edit.setComboBoxItems(comboDept.getItems());
            edit.setSelectedLabTechnicianNameAndID(selectedTechnicianName,Integer.parseInt(labelID.getText()));
            
            HospitalDialog.showDialog(stackPane, rootEdit);
            
            //Refresh List View and Search Suguestion
            HospitalDialog.dialog.setOnDialogClosed((JFXDialogEvent event) -> {
                displayStaffName();
                searchLabTech();
                
                listView.getSelectionModel().select(selectedTechnicianName);
                listView.getSelectionModel().clearSelection();
                
                buttonEdit.setDisable(true);
                buttonDelete.setDisable(true);
            });
        } catch (Exception e) {
        }
    }

    @FXML
    private void deleteAction() {
        try {
            if (loaderDelete == null) {
                loaderDelete = new FXMLLoader(getClass().getResource("DeleteLabTechnician.fxml"));
                rootDelete = loaderDelete.load();
                
                del=loaderDelete.getController();
                
                del.setLabelID(labelID);
                del.setLabelName(labelName);
                del.setLabelAge(labelAge);
                del.setLabelGender(labelGender);
                del.setLabelAddress(labelAddress);
                del.setLabelCity(labelCity);
                del.setLabelPhone(labelPhone);
                del.setLabelMatStatus(labelMatStatus);
                del.setLabelDateJoined(labelDateJoined);
                del.setLabelSalary(labelSalary);
                del.setLabelEmail(labelEmail);
                del.setLabelDegree(labelDegree);
                del.setImageView(imageView);
                del.setDefaultImage(defaultImage);
            }
            del.setSelectedLabTechnicianName(selectedTechnicianName);
            del.setLabTechnicianImageFile(technicianImageFile);
            del.setLabTechnicianID(Integer.parseInt(labelID.getText()));
            
            HospitalDialog.showDialog(stackPane, rootDelete);
            
            //Refresh List View and Search Suguestion
            HospitalDialog.dialog.setOnDialogClosed((JFXDialogEvent event) -> {
                displayStaffName();
                searchLabTech();
                
                buttonEdit.setDisable(true);
                buttonDelete.setDisable(true);
            });
        } catch (Exception e) {
        }
    }
    
    public StackPane getStackPane() {
        return stackPane;
    }
    
}
