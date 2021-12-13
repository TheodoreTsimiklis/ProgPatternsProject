/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.util.Map;
import Model.StudentsModel;

/**
 *
 * @author Theod
 */
public class StudentsView {

    public void printStudentTable(Map map) {
        System.out.println(map);
    }

    public void printStudents(StudentsModel s) {
        System.out.println(s);
    }
}
