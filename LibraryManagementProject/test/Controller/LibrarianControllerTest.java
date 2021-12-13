/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.BooksModel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Theod
 */
public class LibrarianControllerTest {
    
    private LibrarianControllerTest instance;
    
    public LibrarianControllerTest() {
    }
    
    @Before
    public void initialize() {
        instance = new LibrarianControllerTest();
    }
    /**
     * Test of addBook method, of class LibrarianController.
     */
    @Test
    public void testAddBook() {
        System.out.println("addBook");
        BooksModel book = new BooksModel(10203, "TestName", "TestAuthor", "TestPublisher", 5, );
        LibrarianController instance = new LibrarianController();
        instance.addBook(book);

    }

    /**
     * Test of issueBook method, of class LibrarianController.
     */
    @Test
    public void testIssueBook() {
        System.out.println("issueBook");
        String serialNumber = "";
        int studentId = 0;
        LibrarianController instance = new LibrarianController();
        instance.issueBook(serialNumber, studentId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of returnBook method, of class LibrarianController.
     */
    @Test
    public void testReturnBook() {
        System.out.println("returnBook");
        String serialNumber = "";
        int studentId = 0;
        LibrarianController instance = new LibrarianController();
        instance.returnBook(serialNumber, studentId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of viewIssuedBooks method, of class LibrarianController.
     */
    @Test
    public void testViewIssuedBooks() {
        System.out.println("viewIssuedBooks");
        LibrarianController instance = new LibrarianController();
        instance.viewIssuedBooks();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
