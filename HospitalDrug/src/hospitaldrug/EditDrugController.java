/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitaldrug;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hospitaldialog.HospitalDialog;
import hospitaldb.HospitalDB;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class EditDrugController implements Initializable {

    @FXML
    private JFXComboBox comboCat;
    @FXML
    private JFXTextField textName, textQty, textManufact, textPrice;
    @FXML
    private JFXTextArea textAreaDescrip;
    @FXML
    private ImageView imageView;
    private PreparedStatement ps;
    private ResultSet rs;
    private FileChooser chooser;
    private FileChooser.ExtensionFilter imageFilter;
    private Image image;
    private File chooserImageFile, selectedImageFile;
    private BufferedImage buff;
    private String name, descrip, qty, price, manufact;
    private InputStream input;
    private OutputStream output;
    private int size;
    private byte[] data;
    private DecimalFormat format;
    private Drug selectedDrug;
    private ImageView imageView2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chooser = new FileChooser();
        imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg");
        data = new byte[1024];
        format = new DecimalFormat();
        format.applyPattern(",000");

        //Force the Textfield to be Numeric only
        textQty.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                textQty.setText(newValue.replaceAll("[^\\d]", ""));
                Toolkit.getDefaultToolkit().beep();
            }
        });
    }

    public void populateCategoryCombo() throws SQLException {
        comboCat.getItems().clear();
        ps = HospitalDB.getCon().prepareStatement("Select * From Drug_Category");
        rs = ps.executeQuery();

        while (rs.next()) {
            comboCat.getItems().add(rs.getString("Category_Name"));
        }
    }
    
    public void setImageView(ImageView imageView2){
        this.imageView2=imageView2;
    }

    public void setSelectedDrug(Drug selectedDrug) {
        try {
            this.selectedDrug = selectedDrug;

            ps = HospitalDB.getCon().prepareStatement("Select * From Drug Where Name=?");
            ps.setString(1, selectedDrug.getName());
            rs = ps.executeQuery();

            if (rs.next()) {
                textName.setText(rs.getString("Name"));
                textAreaDescrip.setText(rs.getString("Description"));
                comboCat.setValue(rs.getString("Category"));
                textQty.setText(rs.getInt("Quantity") + "");
                textPrice.setText(rs.getInt("Price") + "");
                textManufact.setText(rs.getString("Manufacturer"));

                selectedImageFile = new File(rs.getString("Photo"));
                buff = ImageIO.read(selectedImageFile);
                image = SwingFXUtils.toFXImage(buff, null);
                imageView.setImage(image);
            }
        } catch (Exception e) {
        }
    }

    @FXML
    private void imageClick() {
        try {
            chooser.getExtensionFilters().add(imageFilter);
            chooser.setTitle("Choose Image");

            chooserImageFile = chooser.showOpenDialog(textName.getScene().getWindow());
            if (chooserImageFile != null) {
                image = new Image(chooserImageFile.toURI().toURL().toString());
                imageView.setImage(image);
            }
        } catch (Exception e) {
        }
    }

    @FXML
    private void saveAction() {
        try {
            name = textName.getText().trim();
            descrip = textAreaDescrip.getText().trim();
            qty = textQty.getText().trim();
            price = textPrice.getText().trim();
            manufact = textManufact.getText().trim();
            
            if (name.isEmpty() || descrip.isEmpty() || qty.isEmpty() || price.isEmpty() || manufact.isEmpty() || comboCat.getValue() == null) {
                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "Please Fill The Required Input", textName.getScene().getWindow());
            } 
            else {
                ps = HospitalDB.getCon().prepareStatement("Update Drug Set Name=?,Description=?,Category=?,Quantity=?,Price=?,Manufacturer=?,Photo=? Where Name=?");
                ps.setString(1, name);
                ps.setString(2, descrip);
                ps.setString(3, comboCat.getValue().toString());
                ps.setInt(4, Integer.parseInt(qty));
                ps.setInt(5, Integer.parseInt(price));
                ps.setString(6, manufact);
                
                /*
                 Save File Chooser Image to DrugImage File
                 Than Save file Path to Database
                 if image is not Choosen 
                 Save the same Available Image path to DataBase
                */
                if (chooserImageFile!=null) {
                    /*
                     Delete the recent image
                     This will avoid Unwanted image in DrugImage File
                    */
                    selectedImageFile.delete();
                    
                    //Save the Actual Image to DoctorImage File
                    input=new FileInputStream(chooserImageFile);
                    output=new FileOutputStream(System.getProperty("user.home")+File.separator+"DrugImage"+File.separator+chooserImageFile.getName());
                    
                    while((size=input.read(data)) != -1){//-1 means if the end of the stream is reached
                        output.write(data, 0, size);
                    }
                    output.close();
                    
                    //Save Path to database
                    ps.setString(7, System.getProperty("user.home")+File.separator+"DrugImage"+File.separator+chooserImageFile.getName());
                    
                    //Display The Choosen Image to HospitalDrug Controller Image View
                    imageView2.setImage(imageView.getImage());
                }
                else{
                    ps.setString(7, System.getProperty("user.home")+File.separator+"DrugImage"+File.separator+selectedImageFile.getName());
                }
                ps.setString(8, selectedDrug.getName());
                ps.executeUpdate();
                
                selectedDrug.setCate(comboCat.getValue().toString());
                selectedDrug.setDescrip(descrip);
                selectedDrug.setManufac(manufact);
                selectedDrug.setName(name);
                selectedDrug.setPrice(Integer.parseInt(price));
                selectedDrug.setQty(Integer.parseInt(qty));
                
                HospitalDialog.dialog.close();
                HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Save Succesful", textName.getScene().getWindow());
                
                textAreaDescrip.clear();
                textManufact.clear();
                textName.clear();
                textPrice.clear();
                textQty.clear();
                comboCat.setValue(null);
                imageView.setImage(null);
                
            }
        } catch (Exception e) {
        }
    }

}
