package com.derek.client.db;

import java.util.List;

import velly.db.BaseDao;

public class UserDao extends BaseDao<User> {

    @Override
    public String createTable() {
        return "create table if not exists tb_user(user_id int,name varchar(20),password varchar(10),status int);";
    }

    @Override
    public List<User> query(String sql) {
        return null;
    }

    /**
     * 获取当前登录的用户
     *
     * @return
     */
    public User getCurrentUser() {
        User user = new User();
        user.setStatus(1);

        List<User> list = query(user);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public void setCurrentUser(User user) {
        User whereUser = getCurrentUser();
        User entityUser = new User();
        if (whereUser != null) {

            entityUser.setStatus(0);
            update(entityUser, whereUser);
        }

        if (user != null) {
            entityUser.setStatus(1);
            update(entityUser, user);
        }
    }
}
