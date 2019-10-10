package com.derek.client.db;

import velly.db.annotation.DbTable;

/**
 * @Auther: admin
 * @Date: 2019/10/10
 * @Describe :
 */
@DbTable("tb_photo")
public class Photo {
    public String time;

    public String path;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
