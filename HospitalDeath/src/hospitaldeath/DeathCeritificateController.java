/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitaldeath;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import hospitaldb.HospitalDB;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.PrintResolution;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.transform.Scale;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author Andre Kelvin
 */
public class DeathCeritificateController {

    @FXML
    private BorderPane bPane;
    @FXML
    private ImageView imageView;
    @FXML
    private Label labelHosName, labelAddress, labelPhone;
    @FXML
    private JFXTextField textPatientName, textHosName, textDate, textTime, textGender;
    @FXML
    private JFXButton buttonPrint;
    private PreparedStatement ps;
    private ResultSet rs;

    public void getSelectedDeadPatient(String selectedDeadPatient) throws SQLException, IOException {
        ps = HospitalDB.getCon().prepareStatement("Select * From Death Where Name=?");
        ps.setString(1, selectedDeadPatient);
        rs = ps.executeQuery();

        if (rs.next()) {
            textPatientName.setText(rs.getString("Name"));
            textDate.setText(rs.getDate("Date").toString());
            textTime.setText(rs.getString("Time"));
            textGender.setText(rs.getString("Gender"));
        }
        
        ps = HospitalDB.getCon().prepareStatement("Select * From Info");
        rs = ps.executeQuery();
        
        if (rs.next()) {
            labelHosName.setText(rs.getString("Hospital_Name"));
            textHosName.setText(rs.getString("Hospital_Name"));
            labelAddress.setText(rs.getString("Address"));
            labelPhone.setText(rs.getString("Phone"));
            
            File file=new File(rs.getString("Logo"));
            BufferedImage buff=ImageIO.read(file);
            Image logo= SwingFXUtils.toFXImage(buff, null);
            imageView.setImage(logo);
        }
    }

    @FXML
    private void printCertificate() {
        PrinterJob job = PrinterJob.createPrinterJob();

        if (job != null && job.showPrintDialog(bPane.getScene().getWindow())) {
            buttonPrint.setVisible(false);
            textPatientName.getScene().setCursor(Cursor.WAIT);
            Printer printer = job.getPrinter();
            PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, Printer.MarginType.HARDWARE_MINIMUM);

            double width = bPane.getWidth();
            double height = bPane.getHeight();

            PrintResolution resolution = job.getJobSettings().getPrintResolution();

            width /= resolution.getFeedResolution();

            height /= resolution.getCrossFeedResolution();

            double scaleX = pageLayout.getPrintableWidth() / width / 600;
            double scaleY = pageLayout.getPrintableHeight() / height / 600;

            Scale scale = new Scale(scaleX, scaleY);

            bPane.getTransforms().add(scale);

            boolean success = job.printPage(pageLayout, bPane);
            if (success) {
                job.endJob();
                textPatientName.getScene().setCursor(Cursor.DEFAULT);
            }
            bPane.getTransforms().remove(scale);
            buttonPrint.setVisible(true);
        }
    }

}
