package com.derek.velly.download.dao;

import android.database.Cursor;

import com.derek.velly.db.BaseDao;
import com.derek.velly.download.DownloadItemInfo;
import com.derek.velly.download.enums.DownloadStatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 下载文件
 * 数据库的操作类
 */
public class DownLoadDao extends BaseDao<DownloadItemInfo> {
    /**
     *  保存应该下载的合集，但不包括已经下载成功的
      */
    private List<DownloadItemInfo> downloadItemInfos =
            Collections.synchronizedList(new ArrayList<DownloadItemInfo>());

    private DownloadInfoComparator comparator = new DownloadInfoComparator();

    @Override
    public String createTable() {
        return "create table if not exists  t_downloadInfo("
                + "id Integer primary key, "
                + "url TEXT not null,"
                + "filePath TEXT not null, "
                + "displayName TEXT, "
                + "status Integer, "
                + "totalLen Long, "
                + "currentLen Long,"
                + "startTime TEXT,"
                + "finishTime TEXT,"
                + "userId TEXT, "
                + "httpTaskType TEXT,"
                + "priority  Integer,"
                + "stopMode Integer,"
                + "downloadMaxSizeKey TEXT,"
                + "unique(filePath));";
    }

    @Override
    public List<DownloadItemInfo> query(String sql) {
        return null;
    }

    /**
     * 根据下载地址和下载文件路径查找下载记录
     * @param url 下载地址
     * @param filePath 下载文件路径
     * @return 记录
     */
    public DownloadItemInfo findRecord(String url,String filePath){
        synchronized (DownLoadDao.class){
            for (DownloadItemInfo record : downloadItemInfos){
                if (record.getUrl().equals(url) && record.getFilePath().equals(filePath)){
                    return record;
                }
            }

//          在内存中找不到，就去数据库中查找

            DownloadItemInfo where = new DownloadItemInfo();
            where.setUrl(url);
            where.setFilePath(filePath);
            List<DownloadItemInfo> resultList = super.query(where);
            if (resultList.size() > 0){
                return resultList.get(0);
            }
            return null;
        }
    }

    /**
     * 根据文件路径查找记录
     * @param filePath 文件路径
     * @return 记录
     */
    public List<DownloadItemInfo> findRecord(String filePath){
        synchronized (DownLoadDao.class){
            DownloadItemInfo where = new DownloadItemInfo();
            where.setFilePath(filePath);
            List<DownloadItemInfo> resultList = super.query(where);
            return resultList;
        }
    }

    public Integer addRecord(String url,String filePath,String displayName,int priority){
        synchronized (DownLoadDao.class){
            DownloadItemInfo exitDownloadInfo = findRecord(url,filePath);
            if (exitDownloadInfo == null){
                DownloadItemInfo record = new DownloadItemInfo();
                record.setId(generateRecordId());
                record.setUrl(url);
                record.setFilePath(filePath);
                record.setDisplayName(displayName);
                record.setPriority(priority);
                record.setStatus(DownloadStatus.waitting.getValue());
                record.setTotalLen(0L);
                record.setCurrentLen(0L);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                record.setStartTime(dateFormat.format(new Date()) );
                record.setFinishTime("0");
                super.insert(record);
                downloadItemInfos.add(record);
                return record.getId();
            }

            return -1;
        }
    }

    /**
     * 生成ID
     * @return 返回下载的id
     */
    private Integer generateRecordId() {
        int maxId = 0;
        String sql = "select max(id) from " + getTableName();
        synchronized (DownLoadDao.class){
            Cursor cursor = this.database.rawQuery(sql,null);
            if (cursor.moveToNext()){
                int index = cursor.getColumnIndex("max(id)");
                if (index != -1){
                    Object value = cursor.getInt(index);
                    maxId = Integer.parseInt(String.valueOf(value));
                }
            }
        }
        return maxId + 1;
    }

    /**
     *比较器
     */
    class DownloadInfoComparator implements Comparator<DownloadItemInfo>{

        @Override
        public int compare(DownloadItemInfo lhs, DownloadItemInfo rhs) {
            return lhs.getId() - rhs.getId();
        }
    }
}
