/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Theod
 */
public class IssuedBooksDBConnection {
    private static Connection con;

    private static Connection createConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:IssuedBooks.db");
        } catch (ClassNotFoundException e) {
            System.out.println("SQL Driver not found [" + e + "]");
        } catch (SQLException e) {
            System.out.println("SQL Exception [" + e + "]");
        }
        return con;
    }

    public static Connection getConnection() {
        if (con == null) {
            con = createConnection();
        }
        return con;
    }
}
