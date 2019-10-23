package velly.db;


import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//import net.sqlcipher.database.SQLiteDatabase;

/**
 * user.db，多用户表
 */
public class DaoFactory {
    public static final String USER_DB_NAME = "/user.db";

    private static class DaoFactoryHolder {
        private static DaoFactory instance = new DaoFactory();
    }

    private String sqlDatabasePath;
    private SQLiteDatabase sqLiteDatabase;
    private SQLiteDatabase userDatabase;

    private Map<String, BaseDao> map = null;

    private DaoFactory() {
        Log.e("derek", "DaoFactory constructor");
        map = Collections.synchronizedMap(new HashMap<String, BaseDao>());
        File file = new File(Environment.getExternalStorageDirectory(), "update");
        if (!file.exists()) {
            file.mkdirs();
        }
        sqlDatabasePath = file.getAbsolutePath() + USER_DB_NAME;
        openDatabase();
    }

    private void openDatabase() {
//        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(path,this.password,null);
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(sqlDatabasePath, null);
    }

    public static DaoFactory getInstance() {
        return DaoFactoryHolder.instance;
    }

    public synchronized <T extends BaseDao<M>, M> T getDataHelper(Class<T> clazz, Class<M> entity) {
        BaseDao baseDao = null;
        if (map.get(clazz.getSimpleName()) != null) {
            Log.e("derek", "cache BaseDao : " + clazz.getSimpleName());
            return (T) map.get(clazz.getSimpleName());
        }
        try {
            baseDao = clazz.newInstance();
            if (baseDao.init(entity, sqLiteDatabase))
                map.put(clazz.getSimpleName(), baseDao);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return (T) baseDao;
    }

    public synchronized <T extends BaseDao<M>, M> T getUserHelper(Class<T> clazz, Class<M> entity) {
        userDatabase = SQLiteDatabase.openOrCreateDatabase(PrivateDataBaseEnums.database.getValue(), null);
        BaseDao baseDao = null;
        try {
            baseDao = clazz.newInstance();
            baseDao.init(entity, userDatabase);
            map.put(clazz.getSimpleName(), baseDao);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return (T) baseDao;
    }

    public void onDestroy() {
        for (BaseDao db : map.values()) {
            Log.e("derek ", db.database.getPath());
            db.onClose();
        }
        map.clear();

    }
}
