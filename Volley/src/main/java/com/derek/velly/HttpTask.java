package com.derek.velly;

import com.alibaba.fastjson.JSON;
import com.derek.velly.Interface.IHttpService;

import java.io.UnsupportedEncodingException;

public class HttpTask<T> implements Runnable {

    private IHttpService httpService;
    public HttpTask(RequestHodler<T> requestHodler){
        httpService = requestHodler.getHttpService();
        httpService.setHttpListener(requestHodler.getHttpListener());
        httpService.setUrl(requestHodler.getUrl());
        T request = requestHodler.getResponseInfo();
        String info = JSON.toJSONString(request);
        try {
            httpService.setRequestData(info.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        httpService.execute();
    }
}
