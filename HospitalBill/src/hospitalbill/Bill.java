/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalbill;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andre Kelvin
 */
public class Bill {

    private StringProperty descrip;
    private IntegerProperty amount;

    public Bill(String descrip, int amount) {
        this.descrip = new SimpleStringProperty(descrip);
        this.amount = new SimpleIntegerProperty(amount);
        
    }

    public String getDescrip() {
        return descrip.get();
    }

    public int getAmount() {
        return amount.get();
    }

    public void setDescrip(String descrip) {
        this.descrip.set(descrip);
    }

    public void setAmount(int amount) {
        this.amount.set(amount);
    }

    public StringProperty descripProperty() {
        return descrip;
    }

    public IntegerProperty amountProperty() {
        return amount;
    }

}
