package velly.download;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import velly.Interface.IHttpListener;
import velly.Interface.IHttpService;

public class FileDownHttpService implements IHttpService {

    private final static String TAG = FileDownHttpService.class.getName();

    /**
     *即将添加到请求头的信息 ,使用同步的Map
     */
    private Map<String,String> headerMap = Collections.synchronizedMap(new HashMap<String,String>());

    private String Url;
    private IHttpListener httpListener;

    private HttpClient httpClient = new DefaultHttpClient();

    private HttpGet httpGet;

    private DownloadResponseHandler responseHandler = new DownloadResponseHandler();

    private AtomicBoolean pause = new AtomicBoolean(false);

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
        httpGet = new HttpGet(Url);
        constrcutHeader();
        try {
            httpClient.execute(httpGet,responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
            httpListener.onFail();
        }
    }

    private void constrcutHeader() {
        Iterator iterator=headerMap.keySet().iterator();
        while (iterator.hasNext())
        {
            String key= (String) iterator.next();
            String value=headerMap.get(key);
            httpGet.addHeader(key,value);
        }
    }

    @Override
    public void setRequestData(byte[] requestData) {
    }

    @Override
    public Map<String, String> getHttpHeadMap() {
        return headerMap;
    }

    @Override
    public void pause() {
        pause.compareAndSet(false,true);
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
        return pause.get();
    }

    private class DownloadResponseHandler extends BasicResponseHandler{
        @Override
        public String handleResponse(HttpResponse response) {
            int code = response.getStatusLine().getStatusCode();
            if (code == 200){
                httpListener.onSuccess(response.getEntity());
            }else{
                httpListener.onFail();
            }
            Log.e("derek","handleResponse" + code);
            return null;
        }
    }
}
