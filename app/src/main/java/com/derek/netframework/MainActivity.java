package com.derek.netframework;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.derek.velly.Interface.IDataListener;
import com.derek.velly.Velly;
import com.derek.velly.download.DownFileManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private final static String RequestURl = "http://192.168.1.106:8080/DerekWeb/Login";
    private final static String DownloadURl = "http://dl.gamdream.com/download/apk/Monument_kv-36.4_3.2.2.73.1_20171206.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPer();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestPer() {
        String[] persm = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (checkSelfPermission(persm[0]) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(persm,200);
        }
    }

    public void onClickRequest(View v){
        for (int i = 0;i<10;i++){
            User user = new User("derek","123");
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
