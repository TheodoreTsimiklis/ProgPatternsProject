package Controller;

import Model.Book;
import java.sql.*;
import Database.DatabaseConnection;

/**
 *
 * @author Theodore & David
 * 
 */
public class LibrarianController {
    Connection connection;

    public LibrarianController(){
        this.connection = DatabaseConnection.getConnection();
    }

    public void addBook(Book book){
        try {
            String query = "INSERT INTO BOOKS (SERIALNUMBER, TITLE, AUTHOR,"
                + "PUBLISHER, QUANTITY, ADDEDDATE) VALUES (?, ?, ?, ?, ?, ?)";
        
            PreparedStatement statement = connection.prepareStatement(query);
            
            statement.setString(1, book.getSerialNumber());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setString(4, book.getPublisher());
            statement.setInt(5, book.getQuantity());
            statement.setDate(6, book.getAddedDate());
            
            statement.executeUpdate();
            
            System.out.println("BOOK ADDED...");
        }
        
        catch(Exception e){
            System.out.println("BOOK ALREADY EXISTS...\n");
        }
    }
    
    public void issueBook(String serialNumber, int studentId){
        try {
            String query = "SELECT COUNT(*) FROM BOOKS WHERE SERIALNUMBER = ?";
            PreparedStatement prepStatement = connection.prepareStatement(query);
            prepStatement.setString(1, serialNumber);
            ResultSet result = prepStatement.executeQuery();
            int bookCount = result.getInt(1);
            
            if (bookCount == 0){ System.out.println("BOOK DOESN'T EXIST...."); return; }
            
            Statement statement = connection.createStatement();   
            query = "SELECT COUNT(*) FROM ISSUEDBOOKS";
            result = statement.executeQuery(query);
            int issueId = result.getInt(1) + 1;
             
            query = "SELECT QUANTITY FROM BOOKS WHERE SERIALNUMBER = ?";
            prepStatement = connection.prepareStatement(query);
            prepStatement.setString(1, serialNumber);
            result = prepStatement.executeQuery();
            int quantity = result.getInt(1);
            
            if (quantity < 1){ System.out.println("NOT AVAILABLE..."); return; }
            
            query = "UPDATE BOOKS SET QUANTITY = ? WHERE SERIALNUMBER = ?";
            prepStatement = connection.prepareStatement(query);
            prepStatement.setInt(1, quantity - 1);
            prepStatement.setString(2, serialNumber);
            prepStatement.executeUpdate();

            query = "INSERT INTO ISSUEDBOOKS (ID, SERIALNUMBER, STUDENTID, "
                    + "ISSUEDATE) VALUES (?, ?, ?, ?)";
            prepStatement = connection.prepareStatement(query);
            prepStatement.setInt(1, issueId);
            prepStatement.setString(2, serialNumber);
            prepStatement.setInt(3, studentId);
            prepStatement.setDate(4, new Date(System.currentTimeMillis()));
            prepStatement.executeUpdate();
        }
        
        catch(Exception e) {
            System.out.println("COULDN'T ISSUE BOOK...\n");
        }
    }
    
    public void returnBook(String serialNumber, int studentId){
        try {
            String query = "SELECT COUNT(*) FROM ISSUEDBOOKS WHERE SERIALNUMBER = ? AND STUDENTID = ?";
            PreparedStatement prepStatement = connection.prepareStatement(query);
            prepStatement.setString(1, serialNumber);
            prepStatement.setInt(2, studentId);
            ResultSet result = prepStatement.executeQuery();
            int entryCount = result.getInt(1);
            
            if (entryCount == 0){ System.out.println("ENTRY DOESN'T EXIST..."); return; }
            
            query = "SELECT QUANTITY FROM BOOKS WHERE SERIALNUMBER = ?";
            prepStatement = connection.prepareStatement(query);
            prepStatement.setString(1, serialNumber);
            result = prepStatement.executeQuery();
            int quantity = result.getInt(1);
            
            query = "UPDATE BOOKS SET QUANTITY = ? WHERE SERIALNUMBER = ?";
            prepStatement = connection.prepareStatement(query);
            prepStatement.setInt(1, quantity + 1);
            prepStatement.setString(2, serialNumber);
            prepStatement.executeUpdate();
            
            query = "UPDATE ISSUEDBOOKS SET RETURNDATE = ? WHERE SERIALNUMBER = ? AND STUDENTID = ? AND "
                    + "ID = (SELECT MAX(ID) FROM ISSUEDBOOKS WHERE SERIALNUMBER = ? AND STUDENTID = ?)";
            prepStatement = connection.prepareStatement(query);
            prepStatement.setDate(1, new Date(System.currentTimeMillis()));
            prepStatement.setString(2, serialNumber);
            prepStatement.setInt(3, studentId);
            prepStatement.setString(4, serialNumber);
            prepStatement.setInt(5, studentId);
            prepStatement.executeUpdate();
        }
        
        catch(Exception e) {
            System.out.println("COULDN'T RETURN BOOK...\n");
        }
    }
    
    public void viewIssuedBooks(){
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM ISSUEDBOOKS";
            ResultSet result = statement.executeQuery(query);
            
            while (result.next()){
                System.out.println("ISSUE ID: " + result.getInt("ID"));
                System.out.println("SERIAL NUMBER: " + result.getString("SERIALNUMBER"));
                
                query = "SELECT * FROM STUDENTS WHERE STUDENTID = ?";
                PreparedStatement prepStatement = connection.prepareStatement(query);
                prepStatement.setInt(1, result.getInt("STUDENTID"));
                ResultSet student = prepStatement.executeQuery();
                System.out.println("STUDENT ID: " + student.getInt("STUDENTID"));
                System.out.println("NAME: " + student.getString("NAME"));
                System.out.println("CONTACT: " + student.getString("CONTACT"));
                
                System.out.println("ISSUE DATE: " + result.getDate("ISSUEDATE"));
                System.out.println("RETURN DATE: " + result.getDate("RETURNDATE") + "\n");
            }
        }
        
        catch(Exception e){
            System.out.println("ERROR VIEWING ISSUED BOOKS...\n");
        }
    }
}