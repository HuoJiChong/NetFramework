package com.derek.velly.Interface;

import org.apache.http.HttpEntity;

import java.util.Map;

/**
 * http监听
 */
public interface IHttpListener {

    /**
     * 请求成功的回调
     * @param httpEntity 返回的信息实体
     */
    void onSuccess(HttpEntity httpEntity);

    /**
     * 失败
     */
    void onFail();

    /**
     * 请求头信息
     * @param headerMap
     */
    void addHttpHeader(Map<String,String> headerMap);
}
