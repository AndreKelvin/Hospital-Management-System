/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalward;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andre Kelvin
 */
public class Ward {
    
    private StringProperty ward;
    private IntegerProperty bed;
    
    public Ward(String ward, int bed){
        this.ward=new SimpleStringProperty(ward);
        this.bed=new SimpleIntegerProperty(bed);
    }

    public String getWard() {
        return ward.get();
    }

    public int getBed() {
        return bed.get();
    }

    public void setWard(String ward) {
        this.ward.set(ward);
    }

    public void setBed(int bed) {
        this.bed.set(bed);
    }
    
    public StringProperty wardProperty(){
        return ward;
    }
    
    public IntegerProperty bedProperty(){
        return bed;
    }
    
}
