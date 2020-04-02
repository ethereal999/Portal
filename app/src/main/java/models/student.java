package models;

public class student {
    public String name, email, branch, cpi;

    public student(String name, String email, String branch, String cpi) {
        this.name = name;
        this.email = email;
        this.branch = branch;
        this.cpi = cpi;
    }

    public String student_getName() {
        return name;
    }

    public void student_setName(String name) {
        this.name = name;
    }

    public String student_getEmail() {
        return email;
    }

    public void student_setEmail(String email) {
        this.email = email;
    }

    public String student_getBranch() {
        return branch;
    }

    public void student_setBranch(String branch) {
        this.branch = branch;
    }

    public String student_getCpi() {
        return cpi;
    }

    public void student_setCpi(String cpi) {
        this.cpi = cpi;
    }
}
