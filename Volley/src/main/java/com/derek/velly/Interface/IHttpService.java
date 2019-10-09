package com.derek.velly.Interface;

import java.util.Map;

public interface IHttpService {
    void setUrl(String url);

    void setHttpListener(IHttpListener httpListener);

    void execute();

    void setRequestData(byte[] requestData);

    /**
     *
     * 以下的方法是 额外添加的
     * 获取请求头的map
     * @return
     */
    Map<String,String> getHttpHeadMap();

    void pause();

    boolean cancle();

    boolean isCancle();

    boolean isPause();

}
