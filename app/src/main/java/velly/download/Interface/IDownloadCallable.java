package velly.download.Interface;

import velly.download.enums.DownloadStatus;

/**
 * @Auther: admin
 * @Date: 2019/10/8
 * @Describe :
 */
public interface IDownloadCallable {
    /**
     *  新增下载任务的监听
     * @param downloadId
     */
    void onDownloadInfoAdd(int downloadId);

    /**
     * 删除下载任务的监听
     * @param downloadId
     */
    void onDownloadInfoRemove(int downloadId);

    /**
     * 下载状态变化
     * @param downloadId
     * @param status
     */
    void onDownloadStatusChanged(int downloadId, DownloadStatus status);

    /**
     * 下载文件总长度
     * @param downloadId
     * @param totalLength
     */
    void onTotalLengthReceived(int downloadId, Long totalLength);

    /**
     * 下载速度
     * @param downloadId
     * @param downloadPercent
     * @param speed
     */
    void onCurrentSizeChanged(int downloadId, double downloadPercent, long speed);

    /**
     * 下载成功
     * @param downloadId
     */
    void onDownloadSuccess(int downloadId);

    /**
     *  下载错误
     * @param downloadId
     * @param errorCode
     * @param errorMsg
     */
    void onDownloadError(int downloadId, int errorCode, String errorMsg);
}
