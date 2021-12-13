package Model;
import java.sql.Date;

/**
 *
 * @author Theodore & David
 * 
 */
public class Book {
    private String serialNumber;
    private String title;
    private String author;
    private String publisher;
    private int quantity;
    private Date addedDate;

    public Book(String serialNumber, String title, String author, String publisher,
            int quantity, Date addedDate) {
        this.serialNumber = serialNumber;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.quantity = quantity;
        this.addedDate = addedDate;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }
}