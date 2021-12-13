/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.BooksModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import DBHelper.LibraryDBConnection;
import Model.StudentsModel;
import View.BooksView;

/**
 *
 * @author Theod
 */
public class BooksController {

    List<BooksModel> model;
    BooksView view;
    Connection con;

    public BooksController(List<BooksModel> model, BooksView view) throws Exception {
        this.model = model;
        this.view = view;
        con = LibraryDBConnection.getConnection();
    }

    public void createBooksTable() throws Exception {
        Statement stmt = con.createStatement();
        String query = "CREATE TABLE BOOKS"
                + "(SERIALNUM      TEXT PRIMARY KEY    NOT NULL,"
                + "TITLE           TEXT                NOT NULL,"
                + "AUTHOR          TEXT                NOT NULL,"
                + "PUBLISHER       TEXT                NOT NULL,"
                + "PRICE           INT                 NOT NULL,"
                + "QUANTITY        INT                 NOT NULL,"
                + "ISSUED          INT                 NOT NULL,"
                + "ADDEDDATE       TEXT                NOT NULL)";
        stmt.executeUpdate("DROP TABLE if exists BOOKS;");
        stmt.executeUpdate(query);
        System.out.println("Table BOOKS created...");
    }

    public void AddBooks(BooksModel b) throws Exception {
        Statement stmt = con.createStatement();
        String query = "INSERT INTO BOOKS (SERIALNUM, TITLE, AUTHOR, PUBLISHER, "
                + "PRICE, QUANTITY, ISSUED, ADDEDDATE) "
                + "VALUES ('" + b.getSerialNum() + "', '" + b.getTitle() + "', '"
                + b.getAuthor() + "', '" + b.getPublisher() + "', "
                + b.getPrice() + ", " + b.getQty() + ", "
                + b.getIssued() + ", '" + b.getDateofPurchase() + "');";
        stmt.executeUpdate(query);
        System.out.println("Book Added");
    }

    public Boolean issueBook(BooksModel b, StudentsModel s) throws Exception {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy"); //formatted date
        LocalDate localDate = LocalDate.now();
        String dateString = dtf.format(localDate); ///getting current date
        Statement stmt = con.createStatement();
        Statement stmt1 = con.createStatement();
        Statement stmt2 = con.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT * FROM ISSUEDBOOKS");

        while (rs.next()) {
            if (!b.getSerialNum().contains(rs.getString("SERIALNUM"))
                    && s.getStId() != (rs.getInt("STID")) || rs.next()) {
                System.out.println("Book does not exist");
                return false;
            }
        }

        ResultSet rs1 = stmt2.executeQuery("SELECT * FROM BOOKS "
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
                    System.out.println("Unfortunately it seems we ran out of books.");
                    return false;
                }
            }
        }

        String insertQuery = "INSERT INTO ISSUEDBOOKS (ID, STUDENTNAME, "
                + "ISSUEDATE, CONTACTNUM, SERIALNUM, STID) "
                + "VALUES (" + null + ", '" + s.getName() + "', '" + dateString + "','"
                + s.getContactNum() + "', '" + b.getSerialNum() + "', "
                + +s.getStId() + ");";
        stmt1.executeUpdate(insertQuery);

        return true;
    }

    public Boolean returnBook(BooksModel b, StudentsModel s) throws Exception {
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ISSUEDBOOKS");

        while (rs.next()) {
            if (!b.getSerialNum().contains(rs.getString("SERIALNUM"))
                    && s.getStId() != (rs.getInt("STID")) || rs.next()) {
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
                String deleteQuery = "DELETE FROM ISSUEDBOOKS WHERE SERIALNUM='" + b.getSerialNum() + "' AND " + "STID=" + s.getStId() + ";";
                String updateQuery = "UPDATE BOOKS SET QUANTITY=" + newQty + ",ISSUED="
                        + newIssued + " WHERE SERIALNUM='" + b.getSerialNum() + "';";
                stmt1.executeUpdate(deleteQuery);
                stmt1.executeUpdate(updateQuery);
            } else {
                System.out.println("You do not have any books to return.");
                return false;
            }
        }
        return true;
    }

    public static Map viewIssuedCatalog() throws Exception {
        Map<String, String> issuedMap = new HashMap();
        Connection con = LibraryDBConnection.getConnection();

        String query = "SELECT * FROM ISSUEDBOOKS";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            issuedMap.put("Serial Number: " + rs.getString("SERIALNUM"), "Student's Id: " + rs.getInt("STID") + ", "
                    + "Student's name: " + rs.getString("STUDENTNAME") + ", "
                    + "Issue date: " + rs.getString("ISSUEDATE") + " ,"
                    + "Contact number: " + rs.getString("CONTACTNUM"));
        }
        return issuedMap;
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

    public void updateView(Map map) {
        view.printBooksTable(map);
    }

    public void updateBooksView(BooksModel b) {
        view.printBooks(b);
    }
}