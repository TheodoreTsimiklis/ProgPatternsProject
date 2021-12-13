/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

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
public class StudentsControllerTest {
    
    public StudentsControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of viewBooksByTitle method, of class StudentsController.
     */
    @Test
    public void testViewBooksByTitle() {
        System.out.println("viewBooksByTitle");
        String title = "";
        StudentsController instance = null;
        instance.viewBooksByTitle(title);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of viewBooksByAuthor method, of class StudentsController.
     */
    @Test
    public void testViewBooksByAuthor() {
        System.out.println("viewBooksByAuthor");
        String author = "";
        StudentsController instance = null;
        instance.viewBooksByAuthor(author);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of viewCatalogue method, of class StudentsController.
     */
    @Test
    public void testViewCatalogue() {
        System.out.println("viewCatalogue");
        StudentsController instance = null;
        instance.viewCatalogue();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of borrowBook method, of class StudentsController.
     */
    @Test
    public void testBorrowBook() {
        System.out.println("borrowBook");
        String serialNumber = "";
        StudentsController instance = null;
        instance.borrowBook(serialNumber);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of returnBook method, of class StudentsController.
     */
    @Test
    public void testReturnBook() {
        System.out.println("returnBook");
        String serialNumber = "";
        StudentsController instance = null;
        instance.returnBook(serialNumber);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
