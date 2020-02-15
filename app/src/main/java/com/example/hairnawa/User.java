package com.example.hairnawa;

public class User {

    public String username, email, nickname, phoneNumber, businessNumber;
    public String password; //private로는 값 못가져오나..?
    public boolean isCEO;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String password, String nickname, String phoneNumber, boolean isCEO, String businessNumber) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.isCEO = isCEO;
        this.businessNumber = businessNumber;
    }

    public String getPassword() {
        return password;
    }
    public boolean getIsCEO() { return isCEO; }



}
