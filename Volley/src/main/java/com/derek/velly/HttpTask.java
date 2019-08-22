package com.derek.velly;

import com.alibaba.fastjson.JSON;
import com.derek.velly.Interface.IHttpListener;
import com.derek.velly.Interface.IHttpService;

import java.io.UnsupportedEncodingException;

/**
 * 请求对象
 * @param <T>
 */
public class HttpTask<T> implements Runnable {

    private IHttpService httpService;
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
}
