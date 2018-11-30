/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitaldrug;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andre Kelvin
 */
public class Drug {

    private IntegerProperty id, qty, price;
    private StringProperty name, descrip, cate, manufac;

    public Drug(int id, String name, String descrip, String cate, int qty, int price, String manufac) {
        this.id=new SimpleIntegerProperty(id);
        this.name=new SimpleStringProperty(name);
        this.descrip=new SimpleStringProperty(descrip);
        this.cate=new SimpleStringProperty(cate);
        this.qty=new SimpleIntegerProperty(qty);
        this.price=new SimpleIntegerProperty(price);
        this.manufac=new SimpleStringProperty(manufac);
    }
    
    public Drug(String name){
        this.name=new SimpleStringProperty(name);
    }

    public int getId() {
        return id.get();
    }

    public int getQty() {
        return qty.get();
    }

    public int getPrice() {
        return price.get();
    }

    public String getName() {
        return name.get();
    }

    public String getDescrip() {
        return descrip.get();
    }

    public String getCate() {
        return cate.get();
    }

    public String getManufac() {
        return manufac.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setQty(int qty) {
        this.qty.set(qty);
    }

    public void setPrice(int price) {
        this.price.set(price);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setDescrip(String descrip) {
        this.descrip.set(descrip);
    }

    public void setCate(String cate) {
        this.cate.set(cate);
    }

    public void setManufac(String manufac) {
        this.manufac.set(manufac);
    }
    
    public IntegerProperty idProperty(){
        return id;
    }
    
    public IntegerProperty qtyProperty(){
        return qty;
    }
    
    public IntegerProperty priceProperty(){
        return price;
    }
    
    public StringProperty nameProperty(){
        return name;
    }
    
    public StringProperty descripProperty(){
        return descrip;
    }
    
    public StringProperty cateProperty(){
        return cate;
    }
    
    public StringProperty manufacProperty(){
        return manufac;
    }

}
