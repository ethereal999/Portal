package com.example.portal;


public class company {
    public String name, email,logo;

    public company(String name, String email,String logo) {
        this.name = name;
        this.email = email;
        this.logo = logo;
    }
    public company(){

    }



    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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
