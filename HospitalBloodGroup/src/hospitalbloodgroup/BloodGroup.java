/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalbloodgroup;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andre Kelvin
 */
public class BloodGroup {
    
    private StringProperty blood;
    private IntegerProperty bags;
    
    public BloodGroup(String blood, int bags){
        this.blood=new SimpleStringProperty(blood);
        this.bags=new SimpleIntegerProperty(bags);
    }

    public String getBlood() {
        return blood.get();
    }

    public int getBags() {
        return bags.get();
    }

    public void setBlood(String blood) {
        this.blood.set(blood);
    }

    public void setBags(int bags) {
        this.bags.set(bags);
    }
    
    public StringProperty bloodProperty(){
        return blood;
    }
    
    public IntegerProperty bagsProperty(){
        return bags;
    }
}
