/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalbill;

import com.jfoenix.controls.JFXComboBox;
import hospitalbridge.HospitalBridge;
import hospitaldb.HospitalDB;
import hospitaldialog.HospitalDialog;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.PrintResolution;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import javafx.util.StringConverter;
import javafx.util.converter.FormatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
 *
 * @author Andre Kelvin
 */
public class HospitalBillController implements Initializable {

    @FXML
    private StackPane stackPane;
    @FXML
    private ImageView imageView;
    @FXML
    private JFXComboBox comboPatientName;
    @FXML
    private Label labelHospitalName, labelHospitalAddress, labelHospitalPhone, labelDiagnosis, labelTreatment, labelAdmit, labelDischarged, labelAge, labelTotalAmount;
    @FXML
    private TableView<Bill> tableView;
    @FXML
    private TableColumn columnDescription, columnAmount;
    private PreparedStatement ps;
    private ResultSet rs;
    private ObservableList obList;
    private byte age;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            obList = FXCollections.observableArrayList();
            tableView.setItems(obList);
            
            //Pass Combo Box,Image View,Hospital(Name,Address,Phone)
            HospitalBridge.comboBoxBillPatientName=comboPatientName;
            HospitalBridge.imageViewBillLogo=imageView;
            HospitalBridge.billHospitalName=labelHospitalName;
            HospitalBridge.billHospitalAddress=labelHospitalAddress;
            HospitalBridge.billHospitalPhone=labelHospitalPhone;

            columnDescription.setCellValueFactory(new PropertyValueFactory("descrip"));
            columnAmount.setCellValueFactory(new PropertyValueFactory("amount"));

            columnDescription.setCellFactory(TextFieldTableCell.forTableColumn());
            columnAmount.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

            ps = HospitalDB.getCon().prepareStatement("Select Patient_Name From Patient Where Category=?");
            ps.setString(1, "Discharge Patient");
            rs = ps.executeQuery();

            while (rs.next()) {
                comboPatientName.getItems().add(rs.getString("Patient_Name"));
            }
            
            ps=HospitalDB.getCon().prepareStatement("Select Hospital_Name,Address,Phone,Logo From Info");
            rs=ps.executeQuery();
            
            if(rs.next()){
                labelHospitalName.setText(rs.getString("Hospital_Name"));
                labelHospitalAddress.setText(rs.getString("Address"));
                labelHospitalPhone.setText(rs.getString("Phone"));
                
                File file = new File(rs.getString("Logo"));
                BufferedImage buff = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(buff, null);
                imageView.setImage(image);
            }
            ps.close();

            Calendar calendar = new GregorianCalendar();

            //Display Details
            comboPatientName.getSelectionModel().selectedItemProperty().addListener((ObservableValue observable, Object oldValue, Object newValue) -> {
                try {
                    ps = HospitalDB.getCon().prepareStatement("Select Patient.Diagnosis,Admited_Date,Date_Of_Birth,Treatment_Name,Discharged_Date.Date "
                            + "From Patient "
                            + "Left Join Patient_Treatment "
                            + "on Patient.Patient_Name=Patient_Treatment.Patient_Name "
                            + "Left Join Discharged_Date "
                            + "on Discharged_Date.Patient_Name=Patient.Patient_Name "
                            + "Where Patient.Patient_Name=?");
                    ps.setString(1, newValue.toString());
                    rs = ps.executeQuery();

                    if (rs.next()) {
                        labelDiagnosis.setText(rs.getString("Diagnosis"));
                        labelAdmit.setText(rs.getDate("Admited_Date").toString());

                        age = (byte) (calendar.get(Calendar.YEAR) - rs.getDate("Date_Of_Birth").toLocalDate().getYear());
                        labelAge.setText(age + "");

                        labelTreatment.setText(rs.getString("Treatment_Name"));
                        labelDischarged.setText(rs.getString("Date"));
                    }

                    ps = HospitalDB.getCon().prepareStatement("Select Description,Amount,Total From Bill Where Patient_Name=?");
                    ps.setString(1, newValue.toString());
                    rs = ps.executeQuery();

                    tableView.getItems().clear();
                    labelTotalAmount.setText("");

                    while (rs.next()) {
                        obList.add(new Bill(rs.getString("Description"), rs.getInt("Amount")));
                        labelTotalAmount.setText(rs.getString("Total"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
        }
    }

    @FXML
    private void generateTotal() {
        int total = 0;
        for (Bill item : tableView.getItems()) {
            total += (int) columnAmount.getCellData(item);
        }
        labelTotalAmount.setText(new DecimalFormat(",000").format(total));
    }

    @FXML
    private void print() {
        try {

            PrinterJob job = PrinterJob.createPrinterJob();

            if (job != null && job.showPrintDialog(stackPane.getScene().getWindow())) {
                labelDiagnosis.getScene().setCursor(Cursor.WAIT);
                Printer printer = job.getPrinter();
                PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, Printer.MarginType.HARDWARE_MINIMUM);

                double width = stackPane.getWidth();
                double height = stackPane.getHeight();

                PrintResolution resolution = job.getJobSettings().getPrintResolution();

                width /= resolution.getFeedResolution();

                height /= resolution.getCrossFeedResolution();

                double scaleX = pageLayout.getPrintableWidth() / width / 600;
                double scaleY = pageLayout.getPrintableHeight() / height / 600;

                Scale scale = new Scale(scaleX, scaleY);

                stackPane.getTransforms().add(scale);

                boolean success = job.printPage(pageLayout, stackPane);
                if (success) {
                    job.endJob();
                    labelDiagnosis.getScene().setCursor(Cursor.DEFAULT);
                }
                stackPane.getTransforms().remove(scale);
            }

//            Printer printer = Printer.getDefaultPrinter();
//            PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);
//            
//            PrinterJob job = PrinterJob.createPrinterJob();
//            
//            stackPane.setPrefSize(pageLayout.getPrintableWidth(), pageLayout.getPrintableHeight());
//            
//            if (job != null && job.showPrintDialog(stackPane.getScene().getWindow())){
//                boolean success = job.printPage(pageLayout,stackPane);
//                if (success) {
//                    job.endJob();
//                }
//            }
//            WritableImage nodeshot = stackPane.snapshot(new SnapshotParameters(), null);
//            ByteArrayOutputStream output = new ByteArrayOutputStream();
//            //File file = new File("C:/Users/Andre Kelvin/Desktop/TheNode.png");
//            ImageIO.write(SwingFXUtils.fromFXImage(nodeshot, null), "png", output);
//
//            PDDocument doc = new PDDocument();
//            PDPage page = new PDPage();
//            PDImageXObject pdimage;
//            PDPageContentStream content;
//
//            pdimage = PDImageXObject.createFromByteArray(doc, output.toByteArray(), "png");
//            //PDImageXObject.createFromFile("C:/Users/Andre Kelvin/Desktop/TheNode.png", doc);
//            content = new PDPageContentStream(doc, page);
//            
//            PDRectangle box = page.getMediaBox();
//            double factor = Math.min(box.getWidth() / nodeshot.getWidth(), box.getHeight() / nodeshot.getHeight());
//
//            float height = (float) (nodeshot.getHeight() * factor);
//            
//            content.drawImage(pdimage, 0, box.getHeight() - height, (float) (nodeshot.getWidth() * factor), height);
//            //content.drawImage(pdimage, 0, 0,(float)stackPane.getMaxWidth(),(float)stackPane.getMaxHeight());
//            content.close();
//            doc.addPage(page);
//            File outputFile = new File("C:/Users/Andre Kelvin/Desktop/PDFNode.pdf");
//            doc.save(outputFile);
//            doc.close();
//            //file.delete();
//
//            //This Line Automatically Opens the user defualt pdf file viewer
//            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:/Users/Andre Kelvin/Desktop/PDFNode.pdf");
        } catch (Exception e) {
        }
    }

    @FXML
    private void save() {
        try {
            if (tableView.getItems().isEmpty()||comboPatientName.getValue()==null||labelTotalAmount.getText().isEmpty()) {
                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "Invalid", labelDiagnosis.getScene().getWindow());
            } else {
                ps = HospitalDB.getCon().prepareStatement("Insert Into Bill Values(?,?,?,?)");
                for (Bill item : tableView.getItems()) {
                    ps.setString(1, comboPatientName.getValue().toString());
                    ps.setString(2, columnDescription.getCellData(item).toString());
                    ps.setInt(3, (int) columnAmount.getCellData(item));
                    ps.setString(4, labelTotalAmount.getText());
                    ps.addBatch();
                }
                ps.executeBatch();

                HospitalDialog.dialogAlert(Alert.AlertType.INFORMATION, "Save Successful", labelDiagnosis.getScene().getWindow());
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addMoreCell() {
        obList.add(new Bill("null", 0));
    }

}
