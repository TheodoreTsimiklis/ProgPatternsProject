package Controller;
import Model.Student;
import Database.DatabaseConnection;
import java.sql.*;

public class StudentsController {
    Connection connection;
    Student currentStudent;

    public StudentsController(Student currentStudent){
        this.connection = DatabaseConnection.getConnection();
        this.currentStudent = currentStudent;
    }
    
    public void viewBooksByTitle(String title){
        try {
            String query = "SELECT * FROM BOOKS";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            
            System.out.println(result.getString(1));
            
            System.out.println("BOOKS CONTAINING \"" + title + "\":\n");
            
            while (result.next()){
                if (result.getString("TITLE").contains(title)){
                    System.out.println("SERIAL NUMBER: " + result.getString("SERIALNUMBER"));
                    System.out.println("TITLE: " + result.getString("TITLE"));
                    System.out.println("AUTHOR: " + result.getString("AUTHOR"));
                    System.out.println("PUBLISHER: " + result.getString("PUBLISHER"));
                    System.out.println("QUANTITY: " + result.getInt("QUANTITY"));
                    System.out.println("ADDED DATE: " + result.getDate("ADDEDDATE") + "\n");
                }
            }
        }
        
        catch(Exception e) {
            System.out.println("ERROR VIEWING BOOKS BY TITLE...");
        }
        
    }
    
    public void viewBooksByAuthor(String author){
        try {
            String query = "SELECT * FROM BOOKS";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            
            System.out.println("BOOKS CONTAINING \"" + author + "\":\n");
            
            while (result.next()){            
                if (result.getString("AUTHOR").contains(author)){
                    System.out.println("SERIAL NUMBER: " + result.getString("SERIALNUMBER"));
                    System.out.println("TITLE: " + result.getString("TITLE"));
                    System.out.println("AUTHOR: " + result.getString("AUTHOR"));
                    System.out.println("PUBLISHER: " + result.getString("PUBLISHER"));
                    System.out.println("QUANTITY: " + result.getInt("QUANTITY"));
                    System.out.println("ADDED DATE: " + result.getDate("ADDEDDATE") + "\n");
                }
            }
        }
        
        catch(Exception e) {
            System.out.println("ERROR VIEWING BOOKS BY AUTHOR...");
        }
    }
    
    public void viewCatalogue(){
        try {
            String query = "SELECT * FROM BOOKS WHERE QUANTITY > 0";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            
            System.out.println("AVAILABLE BOOKS:\n");
            
            while (result.next()){
                System.out.println("SERIAL NUMBER: " + result.getString("SERIALNUMBER"));
                System.out.println("TITLE: " + result.getString("TITLE"));
                System.out.println("AUTHOR: " + result.getString("AUTHOR"));
                System.out.println("PUBLISHER: " + result.getString("PUBLISHER"));
                System.out.println("QUANTITY: " + result.getInt("QUANTITY"));
                System.out.println("ADDED DATE: " + result.getDate("ADDEDDATE") + "\n");
            }
            
            query = "SELECT I.SERIALNUMBER, B.SERIALNUMBER, B.TITLE, I.ISSUEDATE, I.RETURNDATE, S.STUDENTID, S.NAME "
                    + "FROM ISSUEDBOOKS I, BOOKS B, STUDENTS S WHERE I.SERIALNUMBER = B.SERIALNUMBER AND S.STUDENTID = I.STUDENTID";
            statement = connection.createStatement();
            result = statement.executeQuery(query);
            
            System.out.println("CURRENTLY ISSUED BOOKS:\n");
            
            while (result.next()){
                if (result.getDate("RETURNDATE") == null){
                    System.out.println("SERIAL NUMBER: " + result.getString("SERIALNUMBER"));
                    System.out.println("BOOK TITLE: " + result.getString("TITLE"));
                    System.out.println("STUDENT NAME & ID: " + result.getString("NAME") + ", " + result.getInt("STUDENTID"));
                    System.out.println("ISSUE DATE: " + result.getDate("ISSUEDATE") + "\n");
                }
            }
        }
        
        catch(Exception e) {
            System.out.println("ERROR VIEWING CATALOGUE...");
        }
    }
    
    public void borrowBook(String serialNumber){
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
            prepStatement.setInt(3, currentStudent.getStudentId());
            prepStatement.setDate(4, new Date(System.currentTimeMillis()));
            prepStatement.executeUpdate();
        }
        
        catch(Exception e) {
            System.out.println("COULDN'T BORROW BOOK..." + e);
        }
    }
    
    public void returnBook(String serialNumber){
        try {
            String query = "SELECT COUNT(*) FROM ISSUEDBOOKS WHERE SERIALNUMBER = ? AND STUDENTID = ?";
            PreparedStatement prepStatement = connection.prepareStatement(query);
            prepStatement.setString(1, serialNumber);
            prepStatement.setInt(2, currentStudent.getStudentId());
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
            prepStatement.setInt(3, currentStudent.getStudentId());
            prepStatement.setString(4, serialNumber);
            prepStatement.setInt(5, currentStudent.getStudentId());
            prepStatement.executeUpdate();
        }
        
        catch(Exception e) {
            System.out.println("COULDN'T RETURN BOOK...");
        }
    }
}
