package com.darkbeast0106.firebase14s_21_22;

public class User {
    private String email;
    private String username;
    private String fullName;

    public User() {
    }

    public User(String email, String username, String fullName) {
        this.email = email;
        this.username = username;
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
