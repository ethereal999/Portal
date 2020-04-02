package models;

public class company {
    public String name, email;

    public company(String name, String email) {
        this.name = name;
        this.email = email;
    }
    public company(){

    }

    public String company_getName() {
        return name;
    }

    public String company_getEmail() {
        return email;
    }

    public void company_setName(String name) {
        this.name = name;
    }

    public void company_setEmail(String email) {
        this.email = email;
    }
}
