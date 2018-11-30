/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalreport;

import com.jfoenix.controls.JFXDatePicker;
import hospitaldb.HospitalDB;
import hospitaldialog.HospitalDialog;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.PrintResolution;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Cursor;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;

/**
 *
 * @author Andre Kelvin
 */
public class HospitalReportController implements Initializable {

    @FXML
    private StackPane stackPane;
    @FXML
    private JFXDatePicker dateFrom, dateTo;
    @FXML
    private TableView<Report> tableView;
    @FXML
    private TableView<Report> tableViewDes;
    @FXML
    private TableColumn columnDescrip, columnTotal, columnPercentage;
    @FXML
    private BarChart barChart;
    private ObservableList obList;
    private PreparedStatement ps;
    private ResultSet rs;
    private List arrayList;
    private Calendar calender;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            obList = FXCollections.observableArrayList();
            tableView.setItems(obList);

            calender = new GregorianCalendar();

            columnDescrip.setCellValueFactory(new PropertyValueFactory("descrip"));
            columnTotal.setCellValueFactory(new PropertyValueFactory("total"));
            columnPercentage.setCellValueFactory(new PropertyValueFactory("percent"));

            String[] descriptions = {"Admited Patient", "In Patient", "Out Patient", "Discharged", "Deaths", "Births", "Above 18", "Below 18"};
            arrayList = new ArrayList(Arrays.asList(descriptions));

            for (int i = 0; i <= descriptions.length - 1; i++) {
                tableViewDes.getItems().add(new Report(descriptions[i]));
            }

            tableViewDes.getSelectionModel().clearSelection();
        } catch (Exception e) {
        }
    }

    @FXML
    private void generateTotal() {
        try {
            if (dateFrom.getValue() == null || dateTo.getValue() == null) {
                HospitalDialog.dialogAlert(Alert.AlertType.ERROR, "Invalid! Select Dates", dateFrom.getScene().getWindow());
            } else {
                XYChart.Series ser = new XYChart.Series();
                ser.setName("Report Summary");

                //Get Total Patient Admited Between the Inputed Dates
                //Which will be used to get Perentage
                int total = 0, index = 0;
                
                ps = HospitalDB.getCon().prepareStatement("SELECT Count(PATIENT_NAME) FROM PATIENT WHERE ADMITED_DATE Between ? and ?");
                ps.setString(1, dateFrom.getValue().toString());
                ps.setString(2, dateTo.getValue().toString());
                rs = ps.executeQuery();

                if (rs.next()) {
                    total = Integer.parseInt(rs.getString(1));
                }

                obList.clear();
                barChart.getData().clear();

                //Display Total and Perentage of Admited,In,Out,Discharge Patient Between the Inputed Dates
                ps = HospitalDB.getCon().prepareStatement("SELECT Count(PATIENT_NAME) FROM PATIENT WHERE ADMITED_DATE Between ? and ?"
                        + "Union All "
                        + "SELECT Count(PATIENT_NAME) FROM PATIENT Where CATEGORY=? and ADMITED_DATE Between ? and ?"
                        + "Union All "
                        + "SELECT Count(PATIENT_NAME) FROM PATIENT Where CATEGORY=? and ADMITED_DATE Between ? and ?"
                        + "Union All "
                        + "SELECT Count(PATIENT_NAME) FROM PATIENT Where CATEGORY=? and ADMITED_DATE Between ? and ?");
                ps.setString(1, dateFrom.getValue().toString());
                ps.setString(2, dateTo.getValue().toString());
                ps.setString(3, "In Patient");
                ps.setString(4, dateFrom.getValue().toString());
                ps.setString(5, dateTo.getValue().toString());
                ps.setString(6, "Out Patient");
                ps.setString(7, dateFrom.getValue().toString());
                ps.setString(8, dateTo.getValue().toString());
                ps.setString(9, "Discharge Patient");
                ps.setString(10, dateFrom.getValue().toString());
                ps.setString(11, dateTo.getValue().toString());
                rs = ps.executeQuery();

                while (rs.next()) {
                    obList.add(new Report(rs.getString(1), Math.round((Double.parseDouble(rs.getString(1)) / total) * 100) + "%"));
                    ser.getData().add(new XYChart.Data(arrayList.get(index), Integer.parseInt(rs.getString(1))));
                    index++;
                }

                //Display Total and Perentage of Death and Birth Patient Between the Inputed Dates
                ps = HospitalDB.getCon().prepareStatement("SELECT Count(Name) FROM DEATH Where DATE Between ? and ?"
                        + "Union All "
                        + "SELECT Count(Baby) FROM BIRTH Where DATE Between ? and ?");
                ps.setString(1, dateFrom.getValue().toString());
                ps.setString(2, dateTo.getValue().toString());
                ps.setString(3, dateFrom.getValue().toString());
                ps.setString(4, dateTo.getValue().toString());
                rs = ps.executeQuery();

                while (rs.next()) {
                    obList.add(new Report(rs.getString(1), Math.round((Double.parseDouble(rs.getString(1)) / total) * 100) + "%"));
                    ser.getData().add(new XYChart.Data(arrayList.get(index), Integer.parseInt(rs.getString(1))));
                    index++;
                }

                //Display Total and Perentage of Patient Below 18 and Patient Above 18 Between the Inputed Dates
                byte age;
                int below18 = 0, above18 = 0;
                ps = HospitalDB.getCon().prepareStatement("SELECT DATE_OF_BIRTH from PATIENT Where ADMITED_DATE Between ? and ?");
                ps.setString(1, dateFrom.getValue().toString());
                ps.setString(2, dateTo.getValue().toString());
                rs = ps.executeQuery();

                while (rs.next()) {
                    age = (byte) (calender.get(Calendar.YEAR) - rs.getDate("Date_Of_Birth").toLocalDate().getYear());
                    if (age >= 18) {
                        above18++;
                    } else {
                        below18++;
                    }
                }
                obList.add(new Report(above18 + "", Math.round((Double.parseDouble(above18 + "") / total) * 100) + "%"));
                obList.add(new Report(below18 + "", Math.round((Double.parseDouble(below18 + "") / total) * 100) + "%"));

                ser.getData().add(new XYChart.Data(arrayList.get(index), above18));
                index++;
                ser.getData().add(new XYChart.Data(arrayList.get(index), below18));
                //index++;

                barChart.getData().addAll(ser);
            }
        } catch (Exception e) {
        }
    }

    @FXML
    private void print() {
        try {
            PrinterJob job = PrinterJob.createPrinterJob();

            if (job != null && job.showPrintDialog(stackPane.getScene().getWindow())) {
                dateFrom.getScene().setCursor(Cursor.WAIT);
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
                    dateFrom.getScene().setCursor(Cursor.DEFAULT);
                }

                stackPane.getTransforms().remove(scale);
            }

            dateFrom.getScene().setCursor(Cursor.DEFAULT);
        } catch (Exception e) {
        }
    }

}
