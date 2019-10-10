package com.derek.client.db;

import java.util.List;

import velly.db.BaseDao;

public class UserDao extends BaseDao<User> {

    @Override
    public String createTable() {
        return "create table if not exists tb_user(user_Id int,name varchar(20),password varchar(10));";
    }

    @Override
    public List<User> query(String sql) {

        return null;
    }

    public User getCurrentUser() {
        User user=new User();
        user.setStatus(1);

        List<User> list=query(user);
        if(list.size()>0)
        {
            return list.get(0);
        }
        return null;
    }
}
