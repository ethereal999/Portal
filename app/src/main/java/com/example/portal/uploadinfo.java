package com.example.portal;

public class uploadinfo {
    public String imageURL;
    public uploadinfo(){}
    public uploadinfo(String url) {
        this.imageURL = url;
    }
    public String getImageURL() {
        return imageURL;
    }
}
