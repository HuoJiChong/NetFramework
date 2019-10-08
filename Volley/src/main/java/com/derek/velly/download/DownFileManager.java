package com.derek.velly.download;

import android.os.Environment;
import android.util.Log;

import com.derek.velly.HttpTask;
import com.derek.velly.Interface.IHttpListener;
import com.derek.velly.Interface.IHttpService;
import com.derek.velly.RequestHodler;
import com.derek.velly.ThreadPoolManager;
import com.derek.velly.download.Interface.IDownloadServiceCallable;

import java.io.File;
import java.util.Map;
import java.util.concurrent.FutureTask;

public class DownFileManager implements IDownloadServiceCallable {
    public static final String TAG = DownFileManager.class.getName();

    private byte[] lock = new byte[1];

    public void download(String url){
        synchronized (lock){
            String[] preFixs=url.split("/");
            String afterFix=preFixs[preFixs.length-1];

            File file=new File(Environment.getExternalStorageDirectory(),afterFix);
            //实例化DownloadItem
            DownloadItemInfo downloadItemInfo=new DownloadItemInfo(url,file.getAbsolutePath());

            RequestHodler requestHodler=new RequestHodler();
            //设置请求下载的策略
            IHttpService httpService=new FileDownHttpService();
            //得到请求头的参数 map
            Map<String,String> map=httpService.getHttpHeadMap();
            /**
             * 处理结果的策略
             */
            IHttpListener httpListener=new DownLoadListener(downloadItemInfo,this,httpService);

            requestHodler.setHttpListener(httpListener);
            requestHodler.setHttpService(httpService);
            requestHodler.setUrl(downloadItemInfo.getUrl());

            HttpTask httpTask=new HttpTask(requestHodler);
            try {
                ThreadPoolManager.getInstance().execute(new FutureTask<>(httpTask,null));
            } catch (InterruptedException e) {

            }

        }
    }

    @Override
    public void onDownloadStatusChanged(DownloadItemInfo downloadItemInfo) {
        Log.e(TAG,"onDownloadStatusChanged " + downloadItemInfo.getCurrentLength() );
    }

    @Override
    public void onTotalLengthReceived(DownloadItemInfo downloadItemInfo) {
        Log.e(TAG,"onTotalLengthReceived " + downloadItemInfo.getCurrentLength() );
    }

    @Override
    public void onCurrentSizeChanged(DownloadItemInfo downloadItemInfo, double downLenth, long speed) {
        Log.e(TAG,"onCurrentSizeChanged currentLength " + downloadItemInfo.getCurrentLength() );
        Log.e(TAG,"onCurrentSizeChanged downLength " + downLenth );
        Log.e(TAG,"onCurrentSizeChanged " + speed );
    }

    @Override
    public void onDownloadSuccess(DownloadItemInfo downloadItemInfo) {
        Log.e(TAG,"onDownloadSuccess " + downloadItemInfo.getCurrentLength() );
    }

    @Override
    public void onDownloadPause(DownloadItemInfo downloadItemInfo) {

    }

    @Override
    public void onDownloadError(DownloadItemInfo downloadItemInfo, int var2, String var3) {
        Log.e(TAG,"onDownloadError " + downloadItemInfo.getCurrentLength() );
    }
}
