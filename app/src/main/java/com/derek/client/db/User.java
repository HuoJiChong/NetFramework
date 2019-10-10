package com.derek.client.db;

import velly.db.annotation.DbFiled;
import velly.db.annotation.DbTable;

@DbTable("tb_user")
public class User {

    @DbFiled("user_Id")
    private String user_Id;

    @DbFiled("name")
    private String name;
    //123456
    @DbFiled("password")
    private String password;

    @DbFiled("status")
    private  Integer status;

    public User() {
    }

    public String getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(String user_Id) {
        this.user_Id = user_Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BeanUser{" +
                "user_Id=" + user_Id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
