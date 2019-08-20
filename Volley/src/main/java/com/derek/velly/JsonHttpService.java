package com.derek.velly;

import com.derek.velly.Interface.IHttpListener;
import com.derek.velly.Interface.IHttpService;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;

public class JsonHttpService implements IHttpService {

    private HttpPost httpPost;
    private HttpClient httpClient;
    private String Url;
    private byte[] requestData;

    private IHttpListener httpListener;

    private JsonHttpResponseHandle responseHandle = new JsonHttpResponseHandle();

    @Override
    public void setUrl(String url) {
        this.Url = url;
    }

    @Override
    public void setHttpListener(IHttpListener httpListener) {
        this.httpListener = httpListener;
    }

    @Override
    public void execute() {
        httpPost = new HttpPost(Url);
        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(requestData);
        httpPost.setEntity(byteArrayEntity);

        try {
            httpClient.execute(httpPost,responseHandle);
        } catch (IOException e) {
            httpListener.onFail();
            e.printStackTrace();
        }

    }

    @Override
    public void setRequestData(byte[] requestData) {
        this.requestData = requestData;
    }

    private class JsonHttpResponseHandle extends BasicResponseHandler{
        @Override
        public String handleResponse(HttpResponse response) throws HttpResponseException, IOException {
            int returnCode = response.getStatusLine().getStatusCode();
            if (returnCode == 200 ){
                httpListener.onSuccess(response.getEntity());
            }else{
                httpListener.onFail();
            }
            return null;
        }
    }
}
