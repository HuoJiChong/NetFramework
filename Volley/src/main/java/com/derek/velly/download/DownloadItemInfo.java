package com.derek.velly.download;


public class DownloadItemInfo extends BaseEntity<DownloadItemInfo> {
    private String filePath;
    private String url;

    private long currentLength;
    private long totalLength;

    private DownloadStatus status;

    public DownloadItemInfo() {
    }

    public DownloadItemInfo( String url,String filePath) {
        this.filePath = filePath;
        this.url = url;
    }

    public DownloadStatus getStatus() {
        return status;
    }

    public void setStatus(DownloadStatus status) {
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

    public long getCurrentLength() {
        return currentLength;
    }

    public void setCurrentLength(long currentLength) {
        this.currentLength = currentLength;
    }

    public long getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(long totalLength) {
        this.totalLength = totalLength;
    }
}
