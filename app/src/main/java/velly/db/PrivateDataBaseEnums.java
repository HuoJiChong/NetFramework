package velly.db;

import android.os.Environment;

import com.derek.client.db.User;
import com.derek.client.db.UserDao;

import java.io.File;

/**
 * @Auther: admin
 * @Date: 2019/10/9
 * @Describe :
 */
public enum PrivateDataBaseEnums {
    /**
     * 本地存储数据库的的引用
     */
    database("local/data/database/");

    private String value;

    PrivateDataBaseEnums(String value) {
        this.value = value;
    }

    public String getValue() {
        UserDao userDao = DaoFactory.getInstance().getDataHelper(UserDao.class,User.class);
        if(userDao!=null)
        {
            User currentUser=userDao.getCurrentUser();
            if(currentUser!=null)
            {
                File file=new File(Environment.getExternalStorageDirectory(),"update/" + currentUser.getUser_Id());
                if(!file.exists())
                {
                    file.mkdirs();
                }
                return file.getAbsolutePath() + "/logic.db";
            }
        }
        return value;
    }
}
