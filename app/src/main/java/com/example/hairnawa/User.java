package com.example.hairnawa;

public class User {

    public String name, email, nickname, phoneNumber, businessNumber, userID, position;
    public String userPwd; //private로는 값 못가져오나..?
    //public boolean isCEO;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String email, String userPwd, String nickname, String phoneNumber, String position, String businessNumber, String userID) {
        this.name = name;
        this.email = email;
        this.userPwd = userPwd;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.position = position;
        this.businessNumber = businessNumber;
        this.userID = userID;
    }

    public String getUserPwd() {
        return userPwd;
    }
    public String getPosition() { return position; }

}
