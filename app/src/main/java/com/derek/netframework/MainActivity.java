package com.derek.netframework;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.derek.velly.Interface.IDataListener;
import com.derek.velly.Velly;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private String URl = "http://192.168.1.106:8080/DerekWeb/Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v){
        for (int i = 0;i<10;i++){
            User user = new User("derek","123");
            Velly.sendRequest(user,URl,LoginResponse.class,new IDataListener<LoginResponse>(){

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
}
