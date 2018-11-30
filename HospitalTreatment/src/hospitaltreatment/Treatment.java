/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitaltreatment;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andre Kelvin
 */
public class Treatment {
    
    private StringProperty treatment;
    private IntegerProperty price;
    
    public Treatment(String treatment, int price){
        this.treatment=new SimpleStringProperty(treatment);
        this.price=new SimpleIntegerProperty(price);
    }
    
    public String getTreatment(){
        return treatment.get();
    }
    
    public int getPrice(){
        return price.get();
    }
    
    public void setTreatment(String treatment){
        this.treatment.set(treatment);
    }
    
    public void setPrice(int price){
        this.price.set(price);
    }
    
    public StringProperty treatmentProperty(){
        return treatment;
    }
    
    public IntegerProperty priceProperty(){
        return price;
    }
    
}
