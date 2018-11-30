/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalroom;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andre Kelvin
 */
public class Room {
    
    private StringProperty floor;
    private IntegerProperty room;
    
    public Room(String floor, int room){
        this.floor=new SimpleStringProperty(floor);
        this.room=new SimpleIntegerProperty(room);
    }

    public String getFloor() {
        return floor.get();
    }

    public int getRoom() {
        return room.get();
    }

    public void setFloor(String floor) {
        this.floor.set(floor);
    }

    public void setRoom(int room) {
        this.room.set(room);
    }
    
    public StringProperty floorProperty(){
        return floor;
    }
    
    public IntegerProperty roomProperty(){
        return room;
    }
    
}
