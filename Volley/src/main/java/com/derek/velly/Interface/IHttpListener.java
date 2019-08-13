package com.derek.velly.Interface;

import org.apache.http.HttpEntity;

public interface IHttpListener {

    void onSuccess(HttpEntity httpEntry);
    void onFail();

}
