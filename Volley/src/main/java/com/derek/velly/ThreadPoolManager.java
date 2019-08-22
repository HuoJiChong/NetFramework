package com.derek.velly;

import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 枚举单例模式是最安全的单例模式
 *
 * 请求池
 *
 */
public class ThreadPoolManager {

    private static final String TAG = ThreadPoolManager.class.getName();
    private static ThreadPoolManager instance = new ThreadPoolManager();

    //任务队列
    private LinkedBlockingQueue<Future<?>> taskQuene = new LinkedBlockingQueue<>();
    private ThreadPoolExecutor threadPoolExecutor;

    public static ThreadPoolManager getInstance(){
        return instance;
    }

    private ThreadPoolManager(){
        threadPoolExecutor = new ThreadPoolExecutor(4,10,10,TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(4),handler);
        threadPoolExecutor.execute(runnable);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true){
                FutureTask task = null;

                try {
                    Log.i(TAG,"等待队列     "+taskQuene.size());
                    task = (FutureTask) taskQuene.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (task != null ){
                    Log.i(TAG,"任务执行    ");
                    threadPoolExecutor.execute(task);
                }else{

                }

                Log.i(TAG,"线程池大小      "+threadPoolExecutor.getPoolSize());
            }
        }
    };

    public <T> void execute(FutureTask<T> task) throws InterruptedException {
        taskQuene.put(task);
    }

    /**
     * 拒绝策略
     */
    private RejectedExecutionHandler handler = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                taskQuene.put(new FutureTask<Object>(r,null) {});
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

}
