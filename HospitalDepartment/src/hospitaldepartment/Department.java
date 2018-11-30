/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitaldepartment;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andre Kelvin
 */
public class Department {
    
    private StringProperty department;
    private StringProperty description;
    
    public Department(String department,String description){
        this.department=new SimpleStringProperty(department);
        this.description=new SimpleStringProperty(description);
    }
    
    public String getDepartment() {
        return department.get();
    }

    public String getDescription() {
        return description.get();
    }

    public void setDepartment(String department) {
        this.department.set(department);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }
    
    public StringProperty departmentProperty(){
        return department;
    }
    
    public StringProperty descriptionProperty(){
        return description;
    }
}
