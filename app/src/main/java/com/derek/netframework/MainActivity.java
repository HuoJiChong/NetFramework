package com.derek.netframework;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.derek.velly.db.DaoFactory;
import com.derek.netframework.db.User;
import com.derek.netframework.db.UserDao;
import com.derek.netframework.http.LoginResponse;
import com.derek.netframework.http.UserRequest;
import com.derek.velly.Interface.IDataListener;
import com.derek.velly.Velly;
import com.derek.velly.download.DownFileManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private final static String RequestURl = "http://192.168.1.106:8080/DerekWeb/Login";
    private final static String DownloadURl = "http://gdown.baidu.com/data/wisegame/8be18d2c0dc8a9c9/WPSOffice_177.apk";

    private static final String dbPwd = "123456";
    private static final String dbName = "teacher.db";

    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPer();

        DaoFactory.getInstance().init(getApplicationContext(),dbPwd,dbName);
        userDao = DaoFactory.getInstance().getDataHelper(UserDao.class,User.class);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
