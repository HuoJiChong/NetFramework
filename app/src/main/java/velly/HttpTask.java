package velly;

import com.alibaba.fastjson.JSON;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.FutureTask;

import velly.Interface.IHttpListener;
import velly.Interface.IHttpService;

/**
 * 请求对象
 * @param <T>
 */
public class HttpTask<T> implements Runnable {

    private IHttpService httpService;
    private FutureTask futureTask;

    public HttpTask(RequestHodler<T> requestHodler){
        httpService = requestHodler.getHttpService();
        httpService.setHttpListener(requestHodler.getHttpListener());
        httpService.setUrl(requestHodler.getUrl());
        IHttpListener httpListener = requestHodler.getHttpListener();
        httpListener.addHttpHeader(httpService.getHttpHeadMap());

        try {
            T request = requestHodler.getResponseInfo();
            if (request != null){
                String info = JSON.toJSONString(request);
                httpService.setRequestData(info.getBytes("UTF-8"));
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        httpService.execute();
    }

    /**
     * 新增方法
     */
    public void start()
    {
        futureTask=new FutureTask(this,null);
        try {
            ThreadPoolManager.getInstance().execute(futureTask);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 新增方法
     */
    public  void pause()
    {
        httpService.pause();
        if(futureTask!=null)
        {
            ThreadPoolManager.getInstance().removeTask(futureTask);
        }

    }
}
