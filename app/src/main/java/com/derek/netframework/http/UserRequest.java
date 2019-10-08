package com.derek.netframework.http;

public class UserRequest {
    public String name;
    public String pwd;

    public UserRequest(String name, String pwd) {
        this.name = name;
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }
}
