package velly;

import velly.Interface.IHttpListener;
import velly.Interface.IHttpService;

public class RequestHodler<T> {
    private IHttpService httpService;
    private IHttpListener httpListener;

    private T responseInfo;
    private String url;

    public IHttpService getHttpService() {
        return httpService;
    }

    public void setHttpService(IHttpService httpService) {
        this.httpService = httpService;
    }

    public IHttpListener getHttpListener() {
        return httpListener;
    }

    public void setHttpListener(IHttpListener httpListener) {
        this.httpListener = httpListener;
    }

    public T getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(T responseInfo) {
        this.responseInfo = responseInfo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
