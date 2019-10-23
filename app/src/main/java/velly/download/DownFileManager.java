package velly.download;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import velly.HttpTask;
import velly.Interface.IHttpListener;
import velly.Interface.IHttpService;
import velly.RequestHodler;
import velly.db.DaoFactory;
import velly.download.Interface.IDownloadCallable;
import velly.download.Interface.IDownloadServiceCallable;
import velly.download.dao.DownLoadDao;
import velly.download.enums.DownloadStatus;
import velly.download.enums.DownloadStopMode;
import velly.download.enums.Priority;

public class DownFileManager implements IDownloadServiceCallable {
    public static final String TAG = DownFileManager.class.getName();

    private byte[] lock = new byte[1];
    private DownLoadDao downLoadDao = DaoFactory.getInstance().getDataHelper(DownLoadDao.class, DownloadItemInfo.class);
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 观察者模式
     */
    private final List<IDownloadCallable> appListeners = new CopyOnWriteArrayList<>();

    /**
     * 正在下载的所有任务
     */
    private static List<DownloadItemInfo> downloadFileTaskList = new CopyOnWriteArrayList<>();
    private Handler handler = new Handler(Looper.getMainLooper());

    public int download(String url) {
        String[] preFix = url.split("/");
        return this.download(url, Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + preFix[preFix.length - 1]);
    }

    public int download(String url, String filePath) {
        String[] preFix = url.split("/");
        String displayName = preFix[preFix.length - 1];
        return this.download(url, filePath, displayName);
    }

    public int download(String url, String filePath, String displayName) {
        return this.download(url, filePath, displayName, Priority.middle);
    }

    public int download(String url, String filePath, String displayName, Priority priority) {
        if (priority == null) {
            priority = Priority.low;
        }
        File file = new File(filePath);
        DownloadItemInfo downloadItemInfo = downLoadDao.findRecord(url, filePath);
//        没有下载
        if (downloadItemInfo == null) {
            // 根据文件路径查找
            List<DownloadItemInfo> samesFile = downLoadDao.findRecord(filePath);
            // 大于零，表示已经下载存在
            if (samesFile.size() > 0) {
                DownloadItemInfo sameDown = samesFile.get(0);
                // 已经下载完成
                if (sameDown.getCurrentLen() == sameDown.getTotalLen()) {
                    synchronized (appListeners) {
                        for (IDownloadCallable downloadCallable : appListeners) {
                            downloadCallable.onDownloadError(sameDown.getId(), 2, "文件已经下载了");
                        }
                    }
                }
            }

            /** ----------------------------------------------
             * 插入数据库
             * 可能插入失败
             * 因为filePath和 Id 是独一无二的，在数据库建表是已经确认了
             *
             */
            int recordId = downLoadDao.addRecord(url, filePath, displayName, priority.getValue());
            if (recordId != -1) {
                synchronized (appListeners) {
                    for (IDownloadCallable callable : appListeners) {
                        callable.onDownloadInfoAdd(downloadItemInfo.getId());
                    }
                }
            } else {
                // 插入失败时，再次进行查找，确保能查得到，
                downloadItemInfo = downLoadDao.findRecord(url, filePath);
            }
        }
//        是否正在下载
        if (isDowning(file.getAbsolutePath())) {
            synchronized (appListeners) {
                for (IDownloadCallable callable : appListeners) {
                    callable.onDownloadError(downloadItemInfo.getId(), 4, "正在下载。。。。");
                }
            }
            return downloadItemInfo.getId();
        }

        if (downloadItemInfo != null) {
            downloadItemInfo.setPriority(priority.getValue());
            downloadItemInfo.setStopMode(DownloadStopMode.auto.getValue());

//            判断数据库存的状态，是否是完成
            if (downloadItemInfo.getStatus() != DownloadStatus.finish.getValue()) {
                // 长度为零，还没有真正的开始下载
                if (downloadItemInfo.getTotalLen() == 0L || file.length() == 0L) {
                    downloadItemInfo.setStatus(DownloadStatus.failed.getValue());
                }
//                判断数据库保存的文件总长度值是否等于文件长度
                if (downloadItemInfo.getTotalLen() == file.length() && downloadItemInfo.getTotalLen() != 0) {
                    downloadItemInfo.setStatus(DownloadStatus.finish.getValue());
                    synchronized (appListeners) {
                        for (IDownloadCallable downloadCallable : appListeners) {
                            try {
                                downloadCallable.onDownloadError(downloadItemInfo.getId(), 3, "已经下载了");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } else {
                if (!file.exists() || (downloadItemInfo.getTotalLen() != downloadItemInfo.getCurrentLen())) {
                    downloadItemInfo.setStatus(DownloadStatus.failed.getValue());
                }
            }
            /**
             * 更新
             */
            downLoadDao.updateRecord(downloadItemInfo);
            /**
             * 判断是否下载完成
             */
            if (downloadItemInfo.getStatus() == DownloadStatus.finish.getValue()) {
                final int downId = downloadItemInfo.getId();
                synchronized (appListeners) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            for (IDownloadCallable callable : appListeners) {
                                callable.onDownloadStatusChanged(downId, DownloadStatus.finish);
                            }
                        }
                    });
                }
                downLoadDao.removeRecordFromMemery(downId);
                return downId;
            }
            //之前的下载 状态为暂停状态
            List<DownloadItemInfo> allDowning = downloadFileTaskList;
            //当前下载不是最高级  则先退出下载
            if (priority != Priority.high) {
                for (DownloadItemInfo downling : allDowning) {
                    //从下载表中  获取到全部正在下载的任务
                    downling = downLoadDao.findSigleRecord(downling.getFilePath());

                    if (downling != null && downling.getPriority() == Priority.high.getValue()) {

                        /**
                         *     更改---------
                         *     当前下载级别不是最高级 传进来的是middle    但是在数据库中查到路径一模一样 的记录   所以他也是最高级------------------------------
                         *     比如 第一次下载是用最高级下载，app闪退后，没有下载完成，第二次传的是默认级别，这样就应该是最高级别下载
                         */
                        if (downling.getFilePath().equals(downloadItemInfo.getFilePath())) {
                            break;
                        } else {
                            return downloadItemInfo.getId();
                        }
                    }
                }
            }
//          真正开始下载任务
            realDown(downloadItemInfo);
            if (priority == Priority.high || priority == Priority.middle) {
                synchronized (allDowning) {
                    for (DownloadItemInfo downloadItemInfo1 : allDowning) {
                        if (!downloadItemInfo.getFilePath().equals(downloadItemInfo1.getFilePath())) {
                            DownloadItemInfo downingInfo = downLoadDao.findSigleRecord(downloadItemInfo1.getFilePath());
                            if (downingInfo != null) {
                                pause(downloadItemInfo.getId(), DownloadStopMode.auto);
                            }
                        }
                    }
                }
                return downloadItemInfo.getId();
            }
        }
        return -1;
    }

    public DownloadItemInfo realDown(DownloadItemInfo downloadItemInfo) {
        synchronized (lock) {
            //实例化DownloadItem
            RequestHodler requestHodler = new RequestHodler();
            //设置请求下载的策略
            IHttpService httpService = new FileDownHttpService();
            //得到请求头的参数 map
            Map<String, String> map = httpService.getHttpHeadMap();
            /**
             * 处理结果的策略
             */
            IHttpListener httpListener = new DownLoadListener(downloadItemInfo, this, httpService);

            requestHodler.setHttpListener(httpListener);
            requestHodler.setHttpService(httpService);
            /**
             *  bug  url
             */
            requestHodler.setUrl(downloadItemInfo.getUrl());

            HttpTask httpTask = new HttpTask(requestHodler);
            downloadItemInfo.setHttpTask(httpTask);

            /**
             * 添加
             */
            downloadFileTaskList.add(downloadItemInfo);
            httpTask.start();

        }

        return downloadItemInfo;

    }

    /**
     * 停止
     *
     * @param downloadId
     * @param mode
     */
    public void pause(int downloadId, DownloadStopMode mode) {
        if (mode == null) {
            mode = DownloadStopMode.auto;
        }
        final DownloadItemInfo downloadInfo = downLoadDao.findRecordById(downloadId);
        if (downloadInfo != null) {
            // 更新停止状态
            if (downloadInfo != null) {
                downloadInfo.setStopMode(mode.getValue());
                downloadInfo.setStatus(DownloadStatus.pause.getValue());
                downLoadDao.updateRecord(downloadInfo);
            }
            for (DownloadItemInfo downing : downloadFileTaskList) {
                if (downloadId == downing.getId()) {
                    downing.getHttpTask().pause();
                }
            }
        }
    }

    /**
     * 判断当前文件是否正在下载
     *
     * @param absolutePath 文件路径
     * @return 是否下载
     */
    private boolean isDowning(String absolutePath) {
        for (DownloadItemInfo info : downloadFileTaskList) {
            if (info.getFilePath().equals(absolutePath)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 添加观察者
     *
     * @param downloadCallable
     */
    public void setDownCallable(IDownloadCallable downloadCallable) {
        synchronized (appListeners) {
            appListeners.add(downloadCallable);
        }

    }

    @Override
    public void onDownloadStatusChanged(DownloadItemInfo downloadItemInfo) {
        Log.e(TAG, "onDownloadStatusChanged " + downloadItemInfo.getCurrentLen());
    }

    @Override
    public void onTotalLengthReceived(DownloadItemInfo downloadItemInfo) {
        Log.e(TAG, " onTotalLengthReceived " + downloadItemInfo.getCurrentLen());
    }

    @Override
    public void onCurrentSizeChanged(DownloadItemInfo downloadItemInfo, double downLenth, long speed) {
        Log.e(TAG, " onCurrentSizeChanged currentLength " + downloadItemInfo.getCurrentLen());
        Log.e(TAG, " onCurrentSizeChanged downLength " + downLenth);
        Log.e(TAG, " onCurrentSizeChanged " + speed);
    }

    @Override
    public void onDownloadSuccess(DownloadItemInfo downloadItemInfo) {
        Log.e(TAG, " onDownloadSuccess " + downloadItemInfo.getCurrentLen());
        downloadItemInfo.setFinishTime(dateFormat.format(new Date()));
        downloadItemInfo.setStatus(DownloadStatus.finish.getValue());

        downLoadDao.updateRecord(downloadItemInfo);
    }

    @Override
    public void onDownloadPause(DownloadItemInfo downloadItemInfo) {

    }

    @Override
    public void onDownloadError(DownloadItemInfo downloadItemInfo, int var2, String var3) {
        Log.e(TAG, " onDownloadError " + downloadItemInfo.getCurrentLen());
    }
}
