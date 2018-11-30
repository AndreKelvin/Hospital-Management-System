/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalblooddonor;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andre Kelvin
 */
public class BloodDonor {
    
    private IntegerProperty id;
    private StringProperty name;
    private StringProperty age;
    private StringProperty sex;
    private StringProperty bloodGroup;
    private StringProperty donationDate;
    private StringProperty phone;
    private StringProperty email;
    private StringProperty address;
    
    public BloodDonor(int id,String name,String age,String sex,String bloodGroup,String donationDate,String phone,String email,String address){
        this.id=new SimpleIntegerProperty(id);
        this.name=new SimpleStringProperty(name);
        this.age=new SimpleStringProperty(age);
        this.sex=new SimpleStringProperty(sex);
        this.bloodGroup=new SimpleStringProperty(bloodGroup);
        this.donationDate=new SimpleStringProperty(donationDate);
        this.phone=new SimpleStringProperty(phone);
        this.email=new SimpleStringProperty(email);
        this.address=new SimpleStringProperty(address);
    }
    
    public int getId(){
        return id.get();
    }
    
    public String getName(){
        return name.get();
    }

    public String getAge() {
        return age.get();
    }

    public String getSex() {
        return sex.get();
    }

    public String getBloodGroup() {
        return bloodGroup.get();
    }

    public String getDonationDate() {
        return donationDate.get();
    }

    public String getPhone() {
        return phone.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getAddress() {
        return address.get();
    }
    
    public void setId(int id){
        this.id.set(id);
    }
    
    public void setName(String name){
        this.name.set(name);
    }

    public void setAge(String age) {
        this.age.set(age);
    }

    public void setSex(String sex) {
        this.sex.set(sex);
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup.set(bloodGroup);
    }

    public void setDonationDate(String donationDate) {
        this.donationDate.set(donationDate);
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setAddress(String address) {
        this.address.set(address);
    }
    
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty ageProperty() {
        return age;
    }

    public StringProperty sexProperty() {
        return sex;
    }

    public StringProperty bloodGroupProperty() {
        return bloodGroup;
    }

    public StringProperty donationDateProperty() {
        return donationDate;
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty addressProperty() {
        return address;
    }
    
}
