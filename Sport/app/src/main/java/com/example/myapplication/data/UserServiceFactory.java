package com.example.myapplication.data;

public class UserServiceFactory {
    private static UserService userService = null;

    public static UserService getInstance() {
        if(userService==null){
            userService = new LocalUser();
        }
        return userService;
    }
}
