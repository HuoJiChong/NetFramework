package velly;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.Map;

import velly.Interface.IHttpListener;
import velly.Interface.IHttpService;

public class JsonHttpService implements IHttpService {

    private HttpPost httpPost;
    private HttpClient httpClient = new DefaultHttpClient();
    private String Url;
    private byte[] requestData;

    private IHttpListener httpListener;

    private HttpRespnceHandler responseHandler = new HttpRespnceHandler();

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
            httpClient.execute(httpPost, responseHandler);
        } catch (IOException e) {
            httpListener.onFail();
            e.printStackTrace();
        }

    }

    @Override
    public void setRequestData(byte[] requestData) {
        this.requestData = requestData;
    }

    @Override
    public Map<String, String> getHttpHeadMap() {
        return null;
    }

    @Override
    public void pause() {

    }

    @Override
    public boolean cancle() {
        return false;
    }

    @Override
    public boolean isCancle() {
        return false;
    }

    @Override
    public boolean isPause() {
        return false;
    }

    private class HttpRespnceHandler extends BasicResponseHandler {
        @Override
        public String handleResponse(HttpResponse response) {
            int returnCode = response.getStatusLine().getStatusCode();
            if (returnCode == 200) {
                httpListener.onSuccess(response.getEntity());
            } else {
                httpListener.onFail();
            }
            return null;
        }
    }
}
