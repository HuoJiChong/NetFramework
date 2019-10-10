package com.derek.client.db;

import java.util.List;

import velly.db.BaseDao;

/**
 * @Auther: admin
 * @Date: 2019/10/10
 * @Describe :
 */
public class PhotoDao extends BaseDao<Photo> {
    @Override
    public String createTable() {
        return "create table if not exists tb_photo(\n" +
                "                time TEXT,\n" +
                "                path TEXT,\n" +
                "                to_user TEXT\n" +
                "                )";
    }

    @Override
    public List<Photo> query(String sql) {
        return null;
    }
}
