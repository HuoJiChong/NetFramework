package com.derek.netframework.db;

import com.derek.db.BaseDao;

import java.util.List;

public class UserDao extends BaseDao {

    @Override
    public String createTable() {
        return "create table if not exists tb_user(user_Id int,name varchar(20),password varchar(10));";
    }

    @Override
    public List<UserDao> query(String sql) {

        return null;
    }
}
