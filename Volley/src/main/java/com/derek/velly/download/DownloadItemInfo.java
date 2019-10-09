package com.derek.velly.download;


import com.derek.velly.HttpTask;
import com.derek.velly.db.annotation.DbTable;

@DbTable("t_downloadInfo")
public class DownloadItemInfo extends BaseEntity<DownloadItemInfo> {

//    private long currentLength;
//    private long totalLength;
    public transient HttpTask httpTask;

    private String filePath;
    private String url;

    private Integer id;
    private String displayName;
    private Long totalLen;
    private Long currentLen;
    private String startTime;
    private String finishTime;

    private String userId;
    /**
     * 下载任务类型
     */
    private String httpTaskType;
    /**
     * 下载优先级
     */
    private Integer priority;
    /**
     * 下载停止模式
     */
    private Integer stopMode;

    private Integer status;

    public DownloadItemInfo() {
    }

    public DownloadItemInfo( String url,String filePath) {
        this.filePath = filePath;
        this.url = url;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

//    public long getCurrentLength() {
//        return currentLength;
//    }
//
//    public void setCurrentLength(long currentLength) {
//        this.currentLength = currentLength;
//    }
//
//    public long getTotalLength() {
//        return totalLength;
//    }
//
//    public void setTotalLength(long totalLength) {
//        this.totalLength = totalLength;
//    }

    public HttpTask getHttpTask() {
        return httpTask;
    }

    public void setHttpTask(HttpTask httpTask) {
        this.httpTask = httpTask;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Long getTotalLen() {
        return totalLen;
    }

    public void setTotalLen(Long totalLen) {
        this.totalLen = totalLen;
    }

    public Long getCurrentLen() {
        return currentLen;
    }

    public void setCurrentLen(Long currentLen) {
        this.currentLen = currentLen;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHttpTaskType() {
        return httpTaskType;
    }

    public void setHttpTaskType(String httpTaskType) {
        this.httpTaskType = httpTaskType;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getStopMode() {
        return stopMode;
    }

    public void setStopMode(Integer stopMode) {
        this.stopMode = stopMode;
    }
}
