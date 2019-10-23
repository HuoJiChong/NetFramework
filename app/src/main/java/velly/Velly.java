package velly;

import java.util.concurrent.FutureTask;

import velly.Interface.IDataListener;
import velly.Interface.IHttpListener;
import velly.Interface.IHttpService;

public class Velly {

    public static <T, M> void sendRequest(T requestInfo, String url, Class<M> response, IDataListener<M> dataListener) {
        RequestHodler<T> requestHodler = new RequestHodler<>();
        requestHodler.setUrl(url);

        IHttpService httpService = new JsonHttpService();
        IHttpListener httpListener = new JsonDealListener<>(response, dataListener);
        requestHodler.setHttpListener(httpListener);
        requestHodler.setHttpService(httpService);
        requestHodler.setResponseInfo(requestInfo);

        HttpTask<T> task = new HttpTask<>(requestHodler);

        try {
            ThreadPoolManager.getInstance().execute(new FutureTask<>(task, null));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
