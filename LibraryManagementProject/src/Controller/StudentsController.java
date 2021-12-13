/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.util.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import Model.StudentsModel;
import Model.BooksModel;
import DBHelper.LibraryDBConnection;
import View.StudentsView;

/**
 *
 * @author Theod
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class StudentsController {

    private StudentsModel model;
    private StudentsView view;
    Connection con;

    public StudentsController(StudentsModel model, StudentsView view) throws Exception {
        this.model = model;
        this.view = view;
        con = LibraryDBConnection.getConnection();
    }

    public void createStudentTable() throws Exception {
        String query = "CREATE TABLE STUDENTS"
                + "(STID            INT PRIMARY KEY     NOT NULL,"
                + "NAME            TEXT                NOT NULL,"
                + "CONTACTNUM      TEXT                NOT NULL)";
        Statement st = con.createStatement();
        st.executeUpdate("DROP table if exists STUDENTS");
        st.executeUpdate(query);
        System.out.println("Table Students created...");
    }

    public StudentsModel retrieveStudent(int id, String name, String contact) throws Exception {
        StudentsModel student = new StudentsModel(id, name, contact);
        Statement stmt = con.createStatement();
        Statement stmt1 = con.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT * FROM STUDENTS");

        String insertQuery = "INSERT INTO STUDENTS (STID, NAME, CONTACTNUM)"
                + "VALUES (" + id + ", '" + name + "','" + contact + "');";

        stmt1.executeUpdate(insertQuery);

        return student;
    }

    public List<BooksModel> searchBookByTitle(String bookTitle) throws Exception {
        List<BooksModel> bookList = new ArrayList<>();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM BOOKS");

        while (rs.next()) {
            String serialNum = rs.getString("SERIALNUM");
            String title = rs.getString("TITLE");
            String author = rs.getString("AUTHOR");
            String publisher = rs.getString("PUBLISHER");
            double price = rs.getDouble("PRICE");
            int qty = rs.getInt("QUANTITY");
            int issued = rs.getInt("ISSUED");
            String date = rs.getString("ADDEDDATE");

            if (title.equals(bookTitle)) {
                bookList.add(new BooksModel(serialNum, title, author, publisher, price,
                        qty, issued, date));
                bookList.sort((BooksModel n1, BooksModel n2) -> n1.getSerialNum().
                        compareTo(n2.getSerialNum()));
            }
        }
        return bookList;
    }

    public List<BooksModel> searchBookByAuthor(String name) throws Exception {
        List<BooksModel> bookList = new ArrayList<>();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM BOOKS");

        while (rs.next()) {
            String serialNum = rs.getString("SERIALNUM");
            String title = rs.getString("TITLE");
            String author = rs.getString("AUTHOR");
            String publisher = rs.getString("PUBLISHER");
            double price = rs.getDouble("PRICE");
            int qty = rs.getInt("QUANTITY");
            int issued = rs.getInt("ISSUED");
            String date = rs.getString("ADDEDDATE");

            if (author.equals(name)) {
                bookList.add(new BooksModel(serialNum, title, author, publisher, price,
                        qty, issued, date));
                bookList.sort((BooksModel n1, BooksModel n2) -> n1.getSerialNum().
                        compareTo(n2.getSerialNum()));
            }
        }
        return bookList;
    }
   
    public List<BooksModel> searchBookByPublisher(String bookPublisher)
            throws Exception {
        List<BooksModel> bookList = new ArrayList<>();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM BOOKS");

        while (rs.next()) {
            String serialNum = rs.getString("SERIALNUM");
            String title = rs.getString("TITLE");
            String author = rs.getString("AUTHOR");
            String publisher = rs.getString("PUBLISHER");
            double price = rs.getDouble("PRICE");
            int qty = rs.getInt("QUANTITY");
            int issued = rs.getInt("ISSUED");
            String date = rs.getString("ADDEDDATE");

            if (publisher.equals(bookPublisher)) {
                bookList.add(new BooksModel(serialNum, title, author, publisher, price,
                        qty, issued, date));
                bookList.sort((BooksModel n1, BooksModel n2) -> n1.getSerialNum().
                        compareTo(n2.getSerialNum()));
            }
        }
        return bookList;
    }

    public Map<String, String> viewCatalog() throws Exception {
        Map<String, String> bookMap = new HashMap();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM BOOKS");

        while (rs.next()) {
            String title = rs.getString("TITLE");
            String author = rs.getString("AUTHOR");
            String publisher = rs.getString("PUBLISHER");
            double price = rs.getDouble("PRICE");
            int qty = rs.getInt("QUANTITY");
            int issued = rs.getInt("ISSUED");
            String date = rs.getString("ADDEDDATE");
            //check date and price
            bookMap.put(rs.getString("SERIALNUM"), title + author + publisher
                    + String.format("%.2f", price) + qty + issued
                    + date);
        }
        return bookMap;
    }

    public Boolean borow(BooksModel b) throws Exception {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy"); //formatted date
        LocalDate localDate = LocalDate.now();
        String dateString = dtf.format(localDate); ///getting current date
        Statement stmt = con.createStatement();
        Statement stmt1 = con.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT * FROM ISSUEDBOOKS");

        //checks if serial num exists
        while (rs.next()) {
            if (!b.getSerialNum().contains(rs.getString("SERIALNUM"))) {
                System.out.println("Book does not exist");
                return false;
            }
        }

        ResultSet rs1 = stmt1.executeQuery("SELECT * FROM BOOKS "
                + "WHERE SERIALNUM='" + b.getSerialNum() + "';");

        while (rs1.next()) {
            if (b.getSerialNum().contains(rs1.getString("SERIALNUM"))) {
                if (rs1.getInt("QUANTITY") > 0) {
                    int newQty = rs1.getInt("QUANTITY") - 1;
                    int newIssued = rs1.getInt("ISSUED") + 1;
                    String updateQuery = "UPDATE BOOKS SET QUANTITY=" + newQty + ",ISSUED="
                            + newIssued + " WHERE SERIALNUM='" + b.getSerialNum() + "';";
                    stmt.executeUpdate(updateQuery);
                } else {
                    System.out.println("There book is not available");
                    return false;
                }
            }
        }

        String insertQuery = "INSERT INTO ISSUEDBOOKS (ID, STUDENTNAME, "
                + "ISSUEDATE, CONTACTNUM, SERIALNUM, STID) "
                + "VALUES (" + null + ", '" + model.getName() + "', '" + dateString + "','"
                + model.getContactNum() + "', '" + b.getSerialNum() + "', "
                + +model.getStId() + ");";
        stmt1.executeUpdate(insertQuery);

        return true;
    }

    public Boolean toReturn(BooksModel b) throws Exception {
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ISSUEDBOOKS");

        //checks if serial num exists
        while (rs.next()) {
            if (!b.getSerialNum().contains(rs.getString("SERIALNUM"))) {
                System.out.println("Book does not exist. Try again");
                return false;
            }
        }

        Statement stmt1 = con.createStatement();
        ResultSet rs1 = stmt.executeQuery("SELECT * FROM BOOKS "
                + "WHERE SERIALNUM='" + b.getSerialNum() + "';");

        while (rs1.next()) {
            if (rs1.getInt("ISSUED") > 0) {
                int newQty = rs1.getInt("QUANTITY") + 1;
                int newIssued = rs1.getInt("ISSUED") - 1;
                String deleteQuery = "DELETE FROM ISSUEDBOOKS WHERE SERIALNUM='" + b.getSerialNum() + "';";
                String updateQuery = "UPDATE BOOKS SET QUANTITY=" + newQty + ",ISSUED="
                        + newIssued + " WHERE SERIALNUM='" + b.getSerialNum() + "';";
                stmt1.executeUpdate(deleteQuery);
                stmt1.executeUpdate(updateQuery);
            } else {
                System.out.println("No books left to return");
                return false;
            }
        }
        return true;
    }

    //updates student table method from view
    public void updateStudentTable(Map map) {
        view.printStudentTable(map);
    }

    //update student info from views
    public void updateStudents(StudentsModel s) {
        view.printStudents(s);
    }
}
