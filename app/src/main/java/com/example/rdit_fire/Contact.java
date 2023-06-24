package com.example.rdit_fire;

public class Contact {

    private String usercontact;
    private String username;

    public Contact() {
    }

    public Contact(String usercontact, String username) {
        this.usercontact = usercontact;
        this.username = username;
    }

    public String getUsercontact() {
        return usercontact;
    }

    public void setUsercontact(String usercontact) {
        this.usercontact = usercontact;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "usercontact='" + usercontact + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
