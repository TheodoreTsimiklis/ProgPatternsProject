/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.BooksModel;
import java.util.Map;

/**
 *
 * @author Theod
 */
public class BooksView {

    public void printBooksTable(Map map) {
        System.out.println(map);
    }

    public void printBooks(BooksModel books) {
        System.out.println(books);
    }
}
