/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitaldb;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Andre Kelvin
 */
public class HospitalDB {

    private static Connection connect;

    public static void initDB() {
        try {
            System.setProperty("derby.system.home", System.getProperty("user.home") + File.separator + "HospitalDB");
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

            connect = DriverManager.getConnection("jdbc:derby:Hospital", "hospital", "hospital");
        } catch (Exception e) {
        }
    }

    public static Connection getCon() {
        return connect;
    }

    public static void closeDB() {
        try {
            connect.close();
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (Exception e) {
        }
    }
    
}
