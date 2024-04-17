package com.rickiey_innovates.juditonspringapp.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoggedInUser {
    private User user;
    public static LoggedInUser instance;

    public static LoggedInUser getInstance() {
        if (instance == null) {
            instance = new LoggedInUser();
        }

        return instance;
    }
}
