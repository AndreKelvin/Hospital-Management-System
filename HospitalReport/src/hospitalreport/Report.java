/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalreport;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andre Kelvin
 */
public class Report {
    
    private StringProperty descrip;
    private StringProperty total;
    private StringProperty percent;
    
    public Report(String total, String percent){
        this.total=new SimpleStringProperty(total);
        this.percent=new SimpleStringProperty(percent);
    }
    
    public Report(String descrip){
        this.descrip=new SimpleStringProperty(descrip);
    }

    public String getDescrip() {
        return descrip.get();
    }

    public String getTotal() {
        return total.get();
    }

    public String getPercent() {
        return percent.get();
    }
    
}
