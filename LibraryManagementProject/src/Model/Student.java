package Model;

/**
 *
 * @author Theodore & David
 * 
 */
public class Student {
    private int studentId;
    private String name;
    private String contact;

    public Student(int studentId, String name, String contact) {
        this.studentId = studentId;
        this.name = name;
        this.contact = contact;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}