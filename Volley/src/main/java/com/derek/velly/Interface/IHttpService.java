package com.derek.velly.Interface;

public interface IHttpService {
    void setUrl(String url);
    void setHttpListener(IHttpListener httpListener);
    void execute();
    void setRequestData(byte[] requestData);

}
