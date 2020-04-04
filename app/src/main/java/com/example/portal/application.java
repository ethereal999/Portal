package com.example.portal;

import java.io.Serializable;

public class application  implements Serializable {
    public String studentname, studentemail,type,companyname,companyemail,appliedposition;

    public application(String studentname, String studentemail, String type, String companyname, String companyemail, String appliedposition) {
        this.studentname = studentname;
        this.studentemail = studentemail;
        this.type = type;
        this.companyname = companyname;
        this.companyemail = companyemail;
        this.appliedposition = appliedposition;
    }
    public application(){

    }

    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    public String getStudentemail() {
        return studentemail;
    }

    public void setStudentemail(String studentemail) {
        this.studentemail = studentemail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getCompanyemail() {
        return companyemail;
    }

    public void setCompanyemail(String companyemail) {
        this.companyemail = companyemail;
    }

    public String getAppliedposition() {
        return appliedposition;
    }


    public void setAppliedposition(String appliedposition) {
        this.appliedposition = appliedposition;
    }
}
