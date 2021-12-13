/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.BooksController;
import DBHelper.LibraryDBConnection;
import java.util.List;
import Model.BooksModel;
import Model.StudentsModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws Exception {
        List<BooksModel> bookModel = retrieveBookList();

        BooksView booksView = new BooksView();
        BooksController bookController = new BooksController(bookModel, booksView);
//        Student studentModel = retrieveStudent();
        StudentsView studentView = new StudentsView();
//        StudentController studentController = new StudentController(studentModel, studentView);
        Connection con = LibraryDBConnection.getConnection();
        BooksModel b = new BooksModel("001", "The Lost City of Atlantis", "John Green", "Jeffrey.inc", 32, 15, 2, "08/07/2021");
        BooksModel c = new BooksModel("002", "The Haunted Mansion", "Divine Summers", "Glendale,inc", 52.01, 4, 0, "12/05/2011");
        Scanner console = new Scanner(System.in);
        Locale locale = new Locale("", "");

        System.out.println("WELCOME TO LIBRARY MANAGEMENT SYSTEM \n\n");

        System.out.print("Choose between these 2 Languages:\n"
                + "press '1' for english\n"
                + "press '2' for french\n");

        switch (console.nextInt()) {
            case 1:
                locale = new Locale("en", "CA");
                break;
            case 2:
                locale = new Locale("fr", "CA");
                break;
            default:
                System.out.println("Invalid choice\nDefaulted to Canada, English\n");
        }

        int trackOf = 0;
        boolean condition = false;
        while (condition == false) {
            System.out.println("To login as a student, type 1\nto login as a librarian, type 2");
            trackOf = console.nextInt();
            if (trackOf == 1) {
                System.out.println("type in your ID: ");
                int id = console.nextInt();
                System.out.println("type in your name: ");
                String name = console.nextLine();

//                while (condition == false){
//                    if (!isNameValid(name)) {
//                        System.out.println("invalid name");
//                        break;
//                    } else {
//                        condition = true;
//                        break;
//                    }
//                }
            }
        }

    }

    public static List<BooksModel> retrieveBookList() {
        List<BooksModel> bookList = new ArrayList();
        BooksModel b = new BooksModel("001", "The Lost City of Atlantis", "John Green", "Jeffrey.inc", 32, 15, 2, "08/07/2021");
        BooksModel c = new BooksModel("002", "The Haunted Mansion", "Divine Summers", "Glendale,inc", 52.01, 4, 0, "12/05/2011");
        bookList.add(new BooksModel("003", "Quantum Mechanics", "Albert Lepstein", " Lepstein.inc.", 79.65, 90, 12, "01/20/2013"));

        return bookList;
    }

    public static boolean isPhoneNumberValid(String s) {
        Pattern p = Pattern.compile("^\\d{10}$");
        Matcher m = p.matcher(s);

        return (m.matches());
    }

    public static boolean isFullNameValid(String name) {
        return name.matches("[a-zA-Z]+");
    }

    public static void createTable(Connection con) throws Exception {
        Statement stmt = con.createStatement();
        String query = "CREATE TABLE ISSUEDBOOKS"
                + "(ID INTEGER         PRIMARY KEY         AUTOINCREMENT,"
                + "STUDENTNAME         TEXT                NOT NULL,"
                + "ISSUEDATE           TEXT                NOT NULL,"
                + "CONTACTNUM          TEXT                NOT NULL,"
                + "SERIALNUM           TEXT                NOT NULL,"
                + "STID                INT                 NOT NULL,"
                + "FOREIGN KEY(SERIALNUM) REFERENCES BOOKS(SERIALNUM),"
                + "FOREIGN KEY(STID) REFERENCES STUDENTS(STID))";
        stmt.executeUpdate("DROP TABLE if exists ISSUEDBOOKS;");
        stmt.executeUpdate(query);
        System.out.println("Table ISSUEDBOOKS created...");
    }

    public static StudentsModel retrieveStudent(StudentsModel s) throws Exception {
        Connection con = LibraryDBConnection.getConnection();
        StudentsModel student = s;
        Statement stmt = con.createStatement();
        Statement stmt1 = con.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT * FROM STUDENTS");

        String insertQuery = "INSERT INTO STUDENTS (STID, NAME, CONTACTNUM)"
                + "VALUES (" + s.getStId() + ", '" + s.getName() + "','" + s.getContactNum() + "');";
        stmt1.executeUpdate(insertQuery);

        return student;
    }
}
