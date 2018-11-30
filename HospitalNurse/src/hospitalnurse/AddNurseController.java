/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalnurse;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import hospitaldb.HospitalDB;
import hospitaldialog.HospitalDialog;
import hospitalstaff.AddStaff;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class AddNurseController implements Initializable /*extends AddStaff*/ {
    
//    public AddNurseController(){
//        super.setImageFolder("NurseImage");
//        super.setTableName("Nurse");
//    }

    @FXML
    private JFXDatePicker datePicker;
    @FXML
    private JFXRadioButton radioM, radioF;
    @FXML
    private ToggleGroup tg, tg1;
    @FXML
    private JFXRadioButton radioSingle, radioMar, radioDiv;
    @FXML
    private JFXComboBox comboDept;
    @FXML
    private JFXTextField textName, textPhone, textDateJoined, textAddress, textCity, textDegree, textEmail, textSalary;
    @FXML
    private ImageView imageView;
    private PreparedStatement ps;
    private DecimalFormat format;
    private Image defaultImage;
    private FileChooser chooser;
    private FileChooser.ExtensionFilter imageFilter;
    private File imageFile;
    private String comma, nurseName, phone, address, city, degree, email, salary;
    private InputStream input;
    private OutputStream output;
    private byte[] data;
    private int size;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            chooser = new FileChooser();
            imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg");
            defaultImage = imageView.getImage();
            format = new DecimalFormat();
            format.applyPattern(",000");
            data = new byte[1024];

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

            SimpleDateFormat date = new SimpleDateFormat("Y-MM-d");
            textDateJoined.setText(date.format(new Date()));
        } catch (Exception e) {
        }
    }
    
    public void setComboBoxItems(ObservableList obList){
        comboDept.getItems().clear();
        comboDept.getItems().addAll(obList);
    }

    @FXML
    private void imageClicked() {
        try {
            chooser.getExtensionFilters().add(imageFilter);
            chooser.setTitle("Choose Image");

            imageFile = chooser.showOpenDialog(imageView.getScene().getWindow());
            if (imageFile!=null) {
                Image chooserImage = new Image(imageFile.toURI().toURL().toString());
                imageView.setImage(chooserImage);
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
            nurseName = textName.getText().trim();
            email = textEmail.getText().trim();
            phone = textPhone.getText().trim();
            salary = textSalary.getText().trim();

            if (address.isEmpty() || city.isEmpty() || degree.isEmpty() || nurseName.isEmpty() || email.isEmpty() || phone.isEmpty() || salary.isEmpty()
                    || datePicker.getValue() == null || tg.getSelectedToggle() == null || tg1.getSelectedToggle() == null || comboDept.getValue() == null
                    || imageView.getImage() == defaultImage) {

                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "Please Fill All Required Input", textName.getScene().getWindow());
            } 
            else {
                ps = HospitalDB.getCon().prepareStatement("Insert Into Nurse (NAME, DATE_OF_BIRTH, GENDER, ADDRESS, CITY, PHONE, MARITAL_STATUS,"
                        + "DATE_JOINED, SALARY, EMAIL, DEPARTMENT, DEGREE, PHOTO) Values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
                ps.setString(1, nurseName);
                ps.setDate(2, java.sql.Date.valueOf(datePicker.getValue()));

                if (radioM.isSelected()) {
                    ps.setString(3, radioM.getText());
                } else {
                    ps.setString(3, radioF.getText());
                }

                ps.setString(4, address);
                ps.setString(5, city);
                ps.setString(6, phone);

                if (radioSingle.isSelected()) {
                    ps.setString(7, radioSingle.getText());
                } else if (radioMar.isSelected()) {
                    ps.setString(7, radioMar.getText());
                } else if (radioDiv.isSelected()) {
                    ps.setString(7, radioDiv.getText());
                }

                ps.setDate(8, java.sql.Date.valueOf(textDateJoined.getText()));
                ps.setString(9, salary);
                ps.setString(10, email);
                ps.setString(11, comboDept.getValue().toString());
                ps.setString(12, degree);
                ps.setString(13, System.getProperty("user.home") + File.separator + "NurseImage" + File.separator + imageFile.getName());
                ps.executeUpdate();

                //Save the Actual Image to NurseImage File
                input = new FileInputStream(imageFile);
                output = new FileOutputStream(System.getProperty("user.home") + File.separator + "NurseImage" + File.separator + imageFile.getName());

                while ((size = input.read(data)) != -1) {//-1 means if the end of the stream is reached
                    output.write(data, 0, size);
                }
                output.close();
                
                HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Save Successful", textName.getScene().getWindow());
                
                textAddress.clear();
                textCity.clear();
                textDegree.clear();
                textName.clear();
                textEmail.clear();
                textPhone.clear();
                textSalary.clear();
                comboDept.setValue(null);
                datePicker.setValue(null);
                tg.getSelectedToggle().setSelected(false);
                tg1.getSelectedToggle().setSelected(false);
                imageView.setImage(defaultImage);
            }
        } catch (Exception e) {
        }
    }
    
}
