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
public class DatabaseConnection {

    private static Connection connection;

    private static Connection createConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:Library.db");
        } 
        
        catch (ClassNotFoundException e) {
            System.out.println("SQL Driver not found [" + e + "]");
        } 
        
        catch (SQLException e) {
            System.out.println("SQL Exception [" + e + "]");
        }
        
        return connection;
    }

    public static Connection getConnection() {
        if (connection == null) {
            connection = createConnection();
        }
        
        return connection;
    }
}
