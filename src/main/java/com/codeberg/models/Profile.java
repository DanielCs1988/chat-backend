package com.codeberg.models;

public class Profile {

    private String name;
    private String email;
    private String pictureUrl;

    public Profile() {
    }

    public Profile(String name, String email, String pictureUrl) {
        this.name = name;
        this.email = email;
        this.pictureUrl = pictureUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
