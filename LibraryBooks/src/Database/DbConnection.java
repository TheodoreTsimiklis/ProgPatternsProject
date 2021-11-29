/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Theod
 */
public class DbConnection {
    
    private static Connection con;

    private static void createConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Theod\\Documents\\NetBeansProjects\\LibraryBooks\\src\\Library.db");
        } catch (ClassNotFoundException e) {
            System.out.println("SQL Driver not found [" + e + "]");
        } catch (SQLException e) {
            System.out.println("SQL Exception [" + e + "]");
        }
    }

    public static Connection getConnection() {
        if (con == null) {
            createConnection();
        }
        return con;
    }
    
    public static void main(String[] args) {
        createConnection();
    }
}
