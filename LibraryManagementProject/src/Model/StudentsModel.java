package Model;

import java.util.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Theod
 */
public class StudentsModel {

    // student id, student name and student contact number
    private int stId;
    private String name;
    private String contactNum;

    //connection between 3 databases
    private static Connection libraryCon;

    public StudentsModel() {
    }

    public StudentsModel(int stId, String name, String contactNum) {
        this.stId = stId;
        this.name = name;
        this.contactNum = contactNum;
    }

    public int getStId() {
        return stId;
    }

    public String getName() {
        return name;
    }

    public String getContactNum() {
        return contactNum;
    }

    public List<BooksModel> searchBookByTitle(String bookTitle) throws Exception {
        List<BooksModel> bookList = new ArrayList<>();
        Statement stmt = libraryCon.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM BOOKS");

        while (rs.next()) {
            String serialNum = rs.getString("SERIALNUM");
            String title = rs.getString("TITLE");
            String author = rs.getString("AUTHOR");
            String publisher = rs.getString("PUBLISHER");
            double price = rs.getDouble("PRICE");
            int qty = rs.getInt("QTY");
            int issued = rs.getInt("ISSUED");
            String date = rs.getString("DATE");

            if (title.equals(bookTitle)) {
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
        Statement stmt = libraryCon.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM BOOKS");

        while (rs.next()) {
            String serialNum = rs.getString("SERIALNUM");
            String title = rs.getString("TITLE");
            String author = rs.getString("AUTHOR");
            String publisher = rs.getString("PUBLISHER");
            double price = rs.getDouble("PRICE");
            int qty = rs.getInt("QTY");
            int issued = rs.getInt("ISSUED");
            String date = rs.getString("DATE");

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
        Statement stmt = libraryCon.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM BOOKS");

        while (rs.next()) {
            String title = rs.getString("TITLE");
            String author = rs.getString("AUTHOR");
            String publisher = rs.getString("PUBLISHER");
            double price = rs.getDouble("PRICE");
            int qty = rs.getInt("QTY");
            int issued = rs.getInt("ISSUED");
            String date = rs.getString("DATE");
            //check date and price
            bookMap.put(rs.getString("SERIALNUM"), title + author + publisher
                    + String.format("%.2f", price) + qty + issued
                    + formatter.format(date));
        }
        return bookMap;
    }

    public Boolean borow(BooksModel b) throws Exception {
        DateTimeFormatter dtf = DateTimeFormatter.BASIC_ISO_DATE;
        Statement stmt = libraryCon.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM BOOKS "
                + "WHERE SERIALNUM= " + b.getSerialNum() + ";");

        while (rs.next()) {
            int qty = rs.getInt("QTY");
            int issued = rs.getInt("ISSUED");

            if (qty > 0) {
                qty--;
                issued++;
                Statement stmt2 = libraryCon.createStatement();
                String insert = "INSERT INTO ISSUEDBOOKS (ID, SERIALNUM, "
                        + "STUDENTNAME, CONTACTNUM, ISSUEDATE) "
                        + "VALUES (" + this.stId + ", '" + b.getSerialNum() + "', '"
                        + this.name + "', '" + this.contactNum + "', '"
                        + dtf.format(LocalDateTime.now()) + "');";
                stmt2.execute(insert);
            }
        }
        return true;
    }

    public Boolean toReturn(BooksModel b) throws Exception {
        Statement stmt = libraryCon
                .createStatement();
        String query = "DELETE FROM ISSUEDBOOKS WHERE SERIALNUM="
                + b.getSerialNum() + "AND STID=" + getStId() + ";";
        stmt.execute(query);

        ResultSet rs = stmt.executeQuery("SELECT * FROM BOOKS "
                + "WHERE SERIALNUM= " + b.getSerialNum() + ";");

        while (rs.next()) {
            int qty = rs.getInt("QTY");
            int issued = rs.getInt("ISSUED");

            if (issued > 0) {
                qty++;
                issued--;
            }
        }
        return true;
    }
}