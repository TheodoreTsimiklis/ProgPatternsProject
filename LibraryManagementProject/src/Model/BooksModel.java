package Model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Theod
 */
public class BooksModel {
    
    private String serialNum;
    private String title;
    private String author;
    private String publisher;
    private double price;
    private int qty;
    private int issued;
    private String dateofPurchase;

    public BooksModel(String serialNum, String title, String author, String publisher, double price, int qty, int issued, String dateofPurchase) {
        this.serialNum = serialNum;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
        this.qty = qty;
        this.issued = 0;
        this.dateofPurchase = dateofPurchase;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getIssued() {
        return issued;
    }

    public void setIssued(int issued) {
        this.issued = issued;
    }

    public String getDateofPurchase() {
        return dateofPurchase;
    }

    public void setDateofPurchase(String dateofPurchase) {
        this.dateofPurchase = dateofPurchase;
    }

    @Override
    public String toString() {
        return "Book{" + "serialNum=" + serialNum + ", title=" + title + ", author=" 
                + author + ", publisher=" + publisher + ", price=" + price + ", qty=" 
                    + qty + ", issued=" + issued + ", dateofPurchase=" + dateofPurchase + '}';
    }

}