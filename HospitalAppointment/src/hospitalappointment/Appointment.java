/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalappointment;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andre Kelvin
 */
public class Appointment {
    
    private StringProperty doctor;
    private StringProperty patient;
    private StringProperty date;
    private StringProperty from;
    private StringProperty to;
    private StringProperty status;
    
    public Appointment(String doctor,String patient,String date,String from,String to,String status){
        this.doctor=new SimpleStringProperty(doctor);
        this.patient=new SimpleStringProperty(patient);
        this.date=new SimpleStringProperty(date);
        this.from=new SimpleStringProperty(from);
        this.to=new SimpleStringProperty(to);
        this.status=new SimpleStringProperty(status);
    }
    
    public String getDoctor(){
        return doctor.get();
    }

    public String getPatient() {
        return patient.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getFrom() {
        return from.get();
    }

    public String getTo() {
        return to.get();
    }

    public String getStatus() {
        return status.get();
    }
    
    public void setDoctor(String doctor){
        this.doctor.set(doctor);
    }

    public void setPatient(String patient) {
        this.patient.set(patient);
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public void setFrom(String from) {
        this.from.set(from);
    }

    public void setTo(String to) {
        this.to.set(to);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
    
    public StringProperty doctorProperty() {
        return doctor;
    }

    public StringProperty patientProperty() {
        return patient;
    }

    public StringProperty dateProperty() {
        return date;
    }

    public StringProperty fromProperty() {
        return from;
    }

    public StringProperty toProperty() {
        return to;
    }

    public StringProperty statusProperty() {
        return status;
    }
    
}
