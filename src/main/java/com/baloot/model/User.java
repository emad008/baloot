package com.baloot.model;

import java.util.Map;

public class User extends Model {
    private static final String USERNAME_REGEX = "^[a-zA-Z_]([a-zA-Z0-9_])*$";
    private final String username;
    private String password, email, birthDate, address;
    private int credit;
    public User(
        String username,
        String password,
        String email,
        String birthDate,
        String address,
        int credit
    ) {
        if (!username.matches(USERNAME_REGEX))
            throw new RuntimeException("Invalid username");

        this.credit = credit;
        this.username = username;
        this.password = password;
        this.email = email;
        this.birthDate = birthDate;
        this.address = address;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void decreaseCredit(int creditChange) {
        this.credit -= creditChange;
    }

    public void increaseCredit(int creditChange) {
        this.credit += creditChange;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getEmail() {
        return this.email;
    }

    public String getBirthDate() {
        return this.birthDate;
    }

    public String getAddress() {
        return this.address;
    }

    public int getCredit() {
        return this.credit;
    }

    @Override
    public Map<String, Object> getKey() {
        return Map.of("username", this.getUsername());
    }

    @Override
    public Map<String, Object> describe() {
        return Map.of(
            "username", this.getUsername(),
            "email", this.getEmail(),
            "birthDate", this.getBirthDate(),
            "address", this.getAddress(),
            "credit", this.getCredit()
        );
    }
}
