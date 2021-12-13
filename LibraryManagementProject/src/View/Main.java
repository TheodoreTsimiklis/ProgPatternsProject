/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.LibrarianController;
import Database.DatabaseConnection;
import java.util.List;
import Model.Book;
import Model.Student;
import java.sql.*;
import Controller.StudentsController;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        createBooksTable();
        createStudentsTable();
        createIssuedBooksTable();
        
        Scanner console = new Scanner(System.in);
        
        int menuOption = 0;
        
        while (menuOption != 3){
            try {
                System.out.println("SELECT FROM THE FOLLOWING OPTIONS:");
                System.out.println("[1] LOG IN AS LIBRARIAN");
                System.out.println("[2] LOG IN AS STUDENT");
                System.out.println("[3] EXIT\n");
                System.out.print("OPTION: "); menuOption = console.nextInt();
                System.out.println("");
            }
                        
            catch(Exception e){
                console.nextLine();
                continue;
            }
            
            int subMenuOption = 0;
                        
            switch (menuOption){
                case 1:   
                    subMenuOption = 0;
                    
                    LibrarianController librarian = new LibrarianController();
                    
                    while (subMenuOption != 5){
                        try {
                            System.out.println("SELECT FROM THE FOLLOWING OPTIONS:");
                            System.out.println("[1] ADD A BOOK TO THE CATALOG");
                            System.out.println("[2] ISSUE A BOOK");
                            System.out.println("[3] RETURN A BOOK");
                            System.out.println("[4] VIEW ISSUED BOOKS");
                            System.out.println("[5] BACK\n");
                            System.out.print("OPTION: "); subMenuOption = console.nextInt();
                            System.out.println("");
                        }
                        
                        catch(Exception e){
                            console.nextLine();
                            continue;
                        }
                        
                        switch (subMenuOption){
                            case 1:
                                try {                                    
                                    System.out.print("ENTER SERIAL NUMBER: ");
                                    String serialNumber = console.next();

                                    System.out.print("ENTER TITLE: ");
                                    String title = console.next();

                                    System.out.print("ENTER AUTHOR: ");
                                    String author = console.next();

                                    System.out.print("ENTER PUBLISHER: ");
                                    String publisher = console.next();

                                    System.out.print("ENTER QUANTITY: ");
                                    int quantity = console.nextInt();

                                    Date date = new Date(System.currentTimeMillis());
                               
                                    Book book = new Book(serialNumber, title, author, publisher, quantity, date);
                                    
                                    librarian.addBook(book);
                                }
                                
                                catch(Exception e){
                                    System.out.println("FORMATTING ERROR...");
                                }
                                
                                break;
                            case 2:
                                try {                                    
                                    System.out.print("ENTER SERIAL NUMBER: ");
                                    String serialNumber = console.next();

                                    System.out.print("ENTER STUDENT ID: ");
                                    int studentId = console.nextInt();
                                    
                                    librarian.issueBook(serialNumber, studentId);
                                }
                                
                                catch(Exception e){
                                    System.out.println("FORMATTING ERROR...");
                                }
                                
                                break;
                            case 3:
                                try {                                    
                                    System.out.print("ENTER SERIAL NUMBER: ");
                                    String serialNumber = console.next();

                                    System.out.print("ENTER STUDENT ID: ");
                                    int studentId = console.nextInt();
                                    
                                    librarian.returnBook(serialNumber, studentId);
                                }
                                
                                catch(Exception e){
                                    System.out.println("FORMATTING ERROR...");
                                }
                                
                                break;
                            case 4:
                                librarian.viewIssuedBooks();
                                
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                    
                case 2:
                    int studentId;
                    
                    try {
                        System.out.print("ENTER STUDENT ID: ");
                        studentId = console.nextInt();
                    }
                    
                    catch(Exception e){
                        console.nextLine();
                        continue;
                    }
                    
                    if (!studentExists(studentId)){
                        System.out.print("ENTER STUDENT NAME: ");
                        String name = console.next();

                        System.out.print("ENTER STUDENT CONTACT: ");
                        String contact = console.next();
                        
                        addStudent(new Student(0, name, contact));
                    }
                    
                    StudentsController student = new StudentsController(getStudent(studentId));
                    
                    if (getStudent(studentId) == null){ continue; }
                    
                    subMenuOption = 0;
                    
                    while (subMenuOption != 6){
                        try {
                            System.out.println("SELECT FROM THE FOLLOWING OPTIONS:");
                            System.out.println("[1] SEARCH BOOKS BY TITLE");
                            System.out.println("[2] SEARCH BOOKS BY AUTHOR");
                            System.out.println("[3] VIEW CATALOGUE");
                            System.out.println("[4] BORROW BOOK");
                            System.out.println("[5] RETURN BOOK");
                            System.out.println("[6] BACK\n");
                            System.out.print("OPTION: "); subMenuOption = console.nextInt();
                            System.out.println("");
                        }
                        
                        catch(Exception e){
                            console.nextLine();
                            continue;
                        }
                        
                        switch (subMenuOption){
                            case 1:
                                System.out.print("SEARCH BY TITLE: ");
                                String title = console.next();
                                
                                student.viewBooksByTitle(title);                               
                                break;
                            case 2:
                                System.out.print("SEARCH BY AUTHOR: ");
                                String author = console.next();
                                
                                student.viewBooksByAuthor(author);
                                break;
                            case 3:
                                student.viewCatalogue();
                                
                                break;
                            case 4:
                                System.out.print("BOOK SERIALNUMBER: ");
                                String serialBorrow = console.next();
                                
                                student.borrowBook(serialBorrow);                               
                                break;
                            case 5:
                                System.out.print("BOOK SERIALNUMBER: ");
                                String serialReturn = console.next();
                                
                                student.returnBook(serialReturn); 
                                break;  
                            default:
                                break;
                        }
                    }
                    break;
                default:
                    System.exit(0);
            }
        }
    }
    
    public static void addStudent(Student student){
        try {
            Connection connection = DatabaseConnection.getConnection();
            
            String query = "SELECT COUNT(*) FROM STUDENTS";          
            Statement statement = connection.createStatement();           
            ResultSet result = statement.executeQuery(query);
            int studentCount = result.getInt(1);
            
            query = "INSERT INTO STUDENTS (STUDENTID, NAME, CONTACT) "
                    + "VALUES (?, ?, ?)";
        
            PreparedStatement prepStatement = connection.prepareStatement(query);
            
            prepStatement.setInt(1, studentCount + 1);
            prepStatement.setString(2, student.getName());
            prepStatement.setString(3, student.getContact());
            
            prepStatement.executeUpdate();
            
            System.out.println("STUDENT ADDED... ID = " + (studentCount + 1));
        }
        
        catch(Exception e){
            System.out.println("STUDENT ALREADY EXISTS...");
        }
    }
    
    public static Student getStudent(int studentId){
        try {
            Connection connection = DatabaseConnection.getConnection();
            
            String query = "SELECT * FROM STUDENTS WHERE STUDENTID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentId);
            
            ResultSet result = statement.executeQuery();
            
            return new Student(studentId, result.getString("NAME"), result.getString("CONTACT"));
            
        }
        
        catch(Exception e){
            System.out.println("COULDN'T RETRIVE STUDENT");
            return null;
        }
    }
    
    public static boolean studentExists(int studentId){
        try {
            Connection connection = DatabaseConnection.getConnection();
            
            String query = "SELECT COUNT(*) FROM STUDENTS WHERE STUDENTID = ?";
            PreparedStatement statement = connection.prepareStatement(query); 
            statement.setInt(1, studentId);
            
            ResultSet result = statement.executeQuery();
            int studentCount = result.getInt(1);
            
            return studentCount != 0;
        }
        
        catch(Exception e){
            System.out.println("STUDENT DOESN'T EXIST");
            
            return false;
        }
    }
    
    public static void createBooksTable(){
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();

            String query = "CREATE TABLE BOOKS"
                    + "(SERIALNUMBER   TEXT PRIMARY KEY    NOT NULL,"
                    + "TITLE           TEXT                NOT NULL,"
                    + "AUTHOR          TEXT                NOT NULL,"
                    + "PUBLISHER       TEXT                NOT NULL,"
                    + "QUANTITY        INT                 NOT NULL,"
                    + "ADDEDDATE       DATE                NOT NULL)";

            statement.executeUpdate(query);

            System.out.println("TABLE BOOKS CREATED...");
        }
        
        catch(Exception e) {
            System.out.println("TABLE BOOKS ALREADY EXISTS...");
        }
    }
    
    public static void createStudentsTable(){
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();

            String query = "CREATE TABLE STUDENTS"
                    + "(STUDENTID      INT PRIMARY KEY     NOT NULL,"
                    + "NAME            TEXT                NOT NULL,"
                    + "CONTACT         TEXT                NOT NULL)";

            statement.executeUpdate(query);

            System.out.println("TABLE STUDENTS CREATED...");
        }
        
        catch(Exception e) {
            System.out.println("TABLE STUDENTS ALREADY EXISTS...");
        }
    }

    public static void createIssuedBooksTable(){
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            
            String query = "CREATE TABLE ISSUEDBOOKS"
                    + "(ID                 INT PRIMARY KEY     NOT NULL,"
                    + "SERIALNUMBER        TEXT                NOT NULL,"
                    + "STUDENTID           INT                 NOT NULL,"
                    + "ISSUEDATE           DATE                NOT NULL,"
                    + "RETURNDATE          DATE                NULL,"                   
                    + "FOREIGN KEY(SERIALNUMBER) REFERENCES BOOKS(SERIALNUMBER)," // FK BOOKS
                    + "FOREIGN KEY(STUDENTID) REFERENCES STUDENTS(STUDENTID))"; // FK STUDENTS
            
            statement.executeUpdate(query);
            
            System.out.println("TABLE ISSUEDBOOKS CREATED...");
        }
        
        catch(Exception e) {
            System.out.println("TABLE ISSUEDBOOKS ALREADY EXISTS...");
        }
    }
    
    public static Student insertStudent(Student s) throws Exception {
        Connection con = DatabaseConnection.getConnection();
        Student student = s;
        Statement stmt = con.createStatement();
        Statement stmt1 = con.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT * FROM STUDENTS");

        String insertQuery = "INSERT INTO STUDENTS (STID, NAME, CONTACTNUM)"
                + "VALUES (" + s.getStudentId() + ", '" + s.getName() + "','" + s.getContact() + "');";
        stmt1.executeUpdate(insertQuery);

        return student;
    }
}
