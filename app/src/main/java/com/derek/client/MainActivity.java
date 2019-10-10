package com.derek.client;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.derek.client.db.Photo;
import com.derek.client.db.PhotoDao;
import com.derek.client.db.User;
import com.derek.client.db.UserDao;
import com.derek.client.http.LoginResponse;
import com.derek.client.http.UserRequest;

import java.util.Date;
import java.util.Locale;

import velly.Interface.IDataListener;
import velly.Velly;
import velly.db.DaoFactory;
import velly.download.DownFileManager;
import velly.update.UpdateManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private final static String RequestURl = "http://192.168.1.106:8080/DerekWeb/Login";
    private final static String DownloadURl = "http://gdown.baidu.com/data/wisegame/8be18d2c0dc8a9c9/WPSOffice_177.apk";

    UpdateManager updateManager;
    UserDao baseDao;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPer();
        updateManager = new UpdateManager();
        baseDao = DaoFactory.getInstance().getDataHelper(UserDao.class,User.class);

        Locale locale = Locale.US;
        String str = "%s:%s:%s:%d:%d:%d";
        Object[] objArr = new Object[6];
        objArr[0] = 1;
        objArr[1] = 2;
        objArr[2] = 3;

        objArr[3] = 4;

        objArr[4] = 5;
        objArr[5] = 6;

        Log.e("derek", String.format(locale, str, objArr));
    }

    private void requestPer() {
        String[] persm = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(persm[0]) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(persm,200);
            }
        }
    }

    public void onClickRequest(View v){
        for (int i = 0;i<10;i++){
            UserRequest user = new UserRequest("derek","123");
            Velly.sendRequest(user, RequestURl,LoginResponse.class,new IDataListener<LoginResponse>(){

                @Override
                public void onSuccess(LoginResponse loginResponse) {
                    Log.i(TAG,loginResponse.toString());
                }

                @Override
                public void onFail() {
                    Log.i(TAG,"失败");
                }
            });
        }
    }

    public void onClickDownload(View v){
        DownFileManager manager = new DownFileManager();
        manager.download(DownloadURl);
    }

    /**
     * 数据库分库，实现多用户登录
     * @param v view
     */
    public void login(View v){
        User user=new User();
        user.setName("V00"+(i++));
        user.setPassword("123456");
        user.setName("张三"+i);
        user.setUser_Id("N000"+i);
        baseDao.insert(user);
//        updateManager.checkThisVersionTable(getApplicationContext());
    }

    public void insert(View v){
        Photo photo=new Photo();
        photo.setPath("data/data/my.jpg");
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        photo.setTime(dateFormat.format(new Date()));
        PhotoDao photoDao = DaoFactory.getInstance().getUserHelper(PhotoDao.class,Photo.class);
        photoDao.insert(photo);
    }

    public void write(View v){
        updateManager.saveVersionInfo(getApplicationContext(),"V002");
    }

    public void update(View v){
        updateManager.checkThisVersionTable(getApplicationContext());

        updateManager.startUpdateDb(getApplicationContext());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
