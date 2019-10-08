package com.derek.netframework.db;

import com.derek.velly.db.BaseDao;

import java.util.List;

public class UserDao extends BaseDao<User> {

    @Override
    public String createTable() {
        return "create table if not exists tb_user(user_Id int,name varchar(20),password varchar(10));";
    }

    @Override
    public List<User> query(String sql) {

        return null;
    }
}
