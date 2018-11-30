/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitaldoctor;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import hospitaldialog.HospitalDialog;
import hospitaldb.HospitalDB;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class EditDoctorController implements Initializable /*extends EditStaff*/ {
    
//    public EditDoctorController(){
//        super.setImageFolder("DoctorImage");
//        super.setTableName("Doctor");
//    }

    @FXML
    private JFXDatePicker date,dateJoined;
    @FXML
    private JFXRadioButton radioM, radioF, radioSingle, radioMar, radioDiv;
    @FXML
    private ToggleGroup tg, tg1;
    @FXML
    private JFXComboBox comboDept;
    @FXML
    private JFXTextField textName, textPhone, textAddress, textCity, textDegree, textEmail, textSalary;
    @FXML
    private ImageView imageView;
    private PreparedStatement ps;
    private ResultSet rs;
    private File selectedImageFile,chooserImageFile;
    private BufferedImage buff;
    private Image image;
    private FileChooser chooser;
    private FileChooser.ExtensionFilter imageFilter;
    private String selectedDocName, comma, docName, phone, address, city, degree, email, salary;
    private InputStream input;
    private OutputStream output;
    private byte[] data;
    private int size,selectedDocID;
    private DecimalFormat format;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            chooser = new FileChooser();
            imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg");
            data = new byte[1024];
            format = new DecimalFormat();
            format.applyPattern(",000");
            
            //Input Comma Sign if needed
            textSalary.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                salary = textSalary.getText();
                try {
                    if (newValue && salary.contains(",")) {//when text gain focus
                        textSalary.setText(salary.replaceAll(",", ""));
                    }
                    if (oldValue) {//when text lose focus
                        comma = format.format(Integer.parseInt(textSalary.getText()));
                        textSalary.setText(comma);
                    }
                } catch (NumberFormatException e) {
                }
            });
            
            //Force the Textfield to be Numeric only
            textPhone.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (!newValue.matches("\\d*")) {
                    textPhone.setText(newValue.replaceAll("[^\\d]", ""));
                    Toolkit.getDefaultToolkit().beep();
                }
            });
        } catch (Exception e) {
        }
    }
    
    public void setComboBoxItems(ObservableList obList){
        comboDept.getItems().clear();
        comboDept.getItems().addAll(obList);
    }

    public void setSelectedDocNameAndID(String selectedDocName, int selectedDocID) throws SQLException, IOException {
        this.selectedDocName = selectedDocName;
        this.selectedDocID=selectedDocID;
        
        ps = HospitalDB.getCon().prepareStatement("Select * From Doctor Where Name=? and ID=?");
        ps.setString(1, selectedDocName);
        ps.setInt(2, selectedDocID);
        rs = ps.executeQuery();

        if (rs.next()) {
            textName.setText(rs.getString("Name"));
            date.setValue(rs.getDate("Date_Of_Birth").toLocalDate());

            if (rs.getString("Gender").contentEquals("M")) {
                radioM.setSelected(true);
            } else {
                radioF.setSelected(true);
            }

            textAddress.setText(rs.getString("Address"));
            textCity.setText(rs.getString("City"));
            textPhone.setText(rs.getString("Phone"));

            switch (rs.getString("Marital_Status")) {
                case "Single":
                    radioSingle.setSelected(true);
                    break;
                case "Married":
                    radioMar.setSelected(true);
                    break;
                default:
                    radioDiv.setSelected(true);
                    break;
            }
            
            dateJoined.setValue(rs.getDate("Date_Joined").toLocalDate());
            textSalary.setText(rs.getString("Salary"));
            textEmail.setText(rs.getString("Email"));
            textDegree.setText(rs.getString("Degree"));
            comboDept.setValue(rs.getString("Department"));
            
            selectedImageFile=new File(rs.getString("Photo"));
            buff=ImageIO.read(selectedImageFile);
            image=SwingFXUtils.toFXImage(buff, null);
            imageView.setImage(image);
        }
        ps.close();
    }

    @FXML
    private void imageClicked() {
        try {
            chooser.getExtensionFilters().add(imageFilter);
            chooser.setTitle("Choose Image");

            chooserImageFile = chooser.showOpenDialog(imageView.getScene().getWindow());
            if (chooserImageFile!=null) {
                image = new Image(chooserImageFile.toURI().toURL().toString());
                imageView.setImage(image);
            }
        } catch (Exception e) {
        }
    }

    @FXML
    private void saveAction() {
        try {
            address = textAddress.getText().trim();
            city = textCity.getText().trim();
            degree = textDegree.getText().trim();
            docName = textName.getText().trim();
            email = textEmail.getText().trim();
            phone = textPhone.getText().trim();
            salary = textSalary.getText().trim();

            if (address.isEmpty() || city.isEmpty() || degree.isEmpty() || docName.isEmpty() || email.isEmpty() || phone.isEmpty() || salary.isEmpty()
                    || date.getValue() == null || tg.getSelectedToggle() == null || tg1.getSelectedToggle() == null || comboDept.getValue() == null) {

                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "Please Fill All Required Input", textName.getScene().getWindow());
            } 
            else {
                ps=HospitalDB.getCon().prepareStatement("Update Doctor Set Name=?,Address=?,City=?,Degree=?,Email=?,Phone=?,Salary=?,Date_Of_Birth=?,"
                        + "Gender=?,Marital_Status=?,Date_Joined=?,Department=?,Photo=? Where Name=? and ID=?");
                ps.setString(1, docName);
                ps.setString(2, address);
                ps.setString(3, city);
                ps.setString(4, degree);
                ps.setString(5, email);
                ps.setString(6, phone);
                ps.setString(7, salary);
                ps.setDate(8, Date.valueOf(date.getValue()));
                
                if (radioM.isSelected()) {
                    ps.setString(9, radioM.getText());
                } else {
                    ps.setString(9, radioF.getText());
                }
                
                if (radioSingle.isSelected()) {
                    ps.setString(10, radioSingle.getText());
                } else if (radioMar.isSelected()) {
                    ps.setString(10, radioMar.getText());
                } else if (radioDiv.isSelected()) {
                    ps.setString(10, radioDiv.getText());
                }
                
                ps.setString(11, dateJoined.getValue().toString());
                ps.setString(12, comboDept.getValue().toString());
                
                /*
                 Save File Chooser Image to DoctorImage File
                 Than Save file Path to Database
                 if image is not Choosen 
                 Save the same Available Image path to DataBase
                */
                if (chooserImageFile!=null) {
                    /*
                     Delete the recent image
                     This will avoid Unwanted image in DoctorImage File
                    */
                    selectedImageFile.delete();
                    
                    //Save the Actual Image to DoctorImage File
                    input = new FileInputStream(chooserImageFile);
                    output = new FileOutputStream(System.getProperty("user.home") + File.separator + "DoctorImage" + File.separator + chooserImageFile.getName());

                    while ((size = input.read(data)) != -1) {//-1 means if the end of the stream is reached
                        output.write(data, 0, size);
                    }
                    output.close();
                    
                    //Save Path to database
                    ps.setString(13, System.getProperty("user.home") + File.separator + "DoctorImage" + File.separator + chooserImageFile.getName());
                    chooserImageFile=null;
                }
                else {
                    ps.setString(13, System.getProperty("user.home") + File.separator + "DoctorImage" + File.separator + selectedImageFile.getName());
                }
                ps.setString(14, selectedDocName);
                ps.setInt(15, selectedDocID);
                ps.executeUpdate();
                
                HospitalDialog.dialog.close();
                HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Save Succesful", textName.getScene().getWindow());
                
                textAddress.clear();
                textCity.clear();
                textDegree.clear();
                textName.clear();
                textEmail.clear();
                textPhone.clear();
                textSalary.clear();
                comboDept.setValue(null);
                date.setValue(null);
                tg.getSelectedToggle().setSelected(false);
                tg1.getSelectedToggle().setSelected(false);
                imageView.setImage(null);
            }
        } catch (Exception e) {
        }
    }
}
