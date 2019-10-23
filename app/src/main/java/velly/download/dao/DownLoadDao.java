package velly.download.dao;

import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import velly.db.BaseDao;
import velly.download.DownloadItemInfo;
import velly.download.enums.DownloadStatus;

/**
 * 下载文件
 * 数据库的操作类
 */
public class DownLoadDao extends BaseDao<DownloadItemInfo> {
    /**
     * 保存应该下载的合集，但不包括已经下载成功的
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
     *
     * @param url      下载地址
     * @param filePath 下载文件路径
     * @return 记录
     */
    public DownloadItemInfo findRecord(String url, String filePath) {
        synchronized (DownLoadDao.class) {
            for (DownloadItemInfo record : downloadItemInfos) {
                if (record.getUrl().equals(url) && record.getFilePath().equals(filePath)) {
                    return record;
                }
            }

//          在内存中找不到，就去数据库中查找

            DownloadItemInfo where = new DownloadItemInfo();
            where.setUrl(url);
            where.setFilePath(filePath);
            List<DownloadItemInfo> resultList = super.query(where);
            if (resultList.size() > 0) {
                return resultList.get(0);
            }
            return null;
        }
    }

    /**
     * 根据文件路径查找记录
     *
     * @param filePath 文件路径
     * @return 记录
     */
    public List<DownloadItemInfo> findRecord(String filePath) {
        synchronized (DownLoadDao.class) {
            DownloadItemInfo where = new DownloadItemInfo();
            where.setFilePath(filePath);
            List<DownloadItemInfo> resultList = super.query(where);
            return resultList;
        }
    }

    /**
     * 添加下载记录
     *
     * @param url         下载地址
     * @param filePath    下载文件路径
     * @param displayName 文件显示名
     * @param priority    下载优先级
     * @return 下载ID，失败时返回-1，成功时返回正数
     */
    public Integer addRecord(String url, String filePath, String displayName, int priority) {
        synchronized (DownLoadDao.class) {
            DownloadItemInfo exitDownloadInfo = findRecord(url, filePath);
            if (exitDownloadInfo == null) {
                DownloadItemInfo record = new DownloadItemInfo();
                record.setId(generateRecordId());
                record.setUrl(url);
                record.setFilePath(filePath);
                record.setDisplayName(displayName);
                record.setPriority(priority);
                record.setStatus(DownloadStatus.waitting.getValue());
                record.setTotalLen(0L);
                record.setCurrentLen(0L);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                record.setStartTime(dateFormat.format(new Date()));
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
     *
     * @return 返回下载的id
     */
    private Integer generateRecordId() {
        int maxId = 0;
        String sql = "select max(id) from " + getTableName();
        synchronized (DownLoadDao.class) {
            Cursor cursor = this.database.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int index = cursor.getColumnIndex("max(id)");
                if (index != -1) {
                    Object value = cursor.getInt(index);
                    maxId = Integer.parseInt(String.valueOf(value));
                }
            }
        }
        return maxId + 1;
    }

    /**
     * 更新下载记录
     *
     * @param record 记录
     * @return 更新结果
     */
    public int updateRecord(DownloadItemInfo record) {
        DownloadItemInfo where = new DownloadItemInfo();
        where.setId(record.getId());
        int result = 0;
        synchronized (DownLoadDao.class) {
            try {
                // 更新数据库中的数据
                result = super.update(record, where);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (result > 0) {
                // 更新内存中的数据
                for (int i = 0; i < downloadItemInfos.size(); i++) {
                    if (downloadItemInfos.get(i).getId().intValue() == record.getId()) {
                        downloadItemInfos.set(i, record);
                        break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * @param downId 下载ID
     * @return 移除结果
     */
    public boolean removeRecordFromMemery(int downId) {
        synchronized (DownLoadDao.class) {
            boolean result = false;
            for (int i = 0; i < downloadItemInfos.size(); i++) {
                if (downloadItemInfos.get(i).getId() == downId) {
                    downloadItemInfos.remove(i);
                    result = true;
                    break;
                }
            }

            return result;
        }
    }

    /**
     * 查找下载记录
     *
     * @param filePath
     * @return
     */
    public DownloadItemInfo findSigleRecord(String filePath) {
        List<DownloadItemInfo> downloadInfoList = findRecord(filePath);
        if (downloadInfoList.isEmpty()) {
            return null;
        }
        return downloadInfoList.get(0);
    }

    /**
     * 根据id查找下载记录对象
     *
     * @param recordId
     * @return
     */
    public DownloadItemInfo findRecordById(int recordId) {
        synchronized (DownLoadDao.class) {
            for (DownloadItemInfo record : downloadItemInfos) {
                if (record.getId() == recordId) {
                    return record;
                }
            }

            DownloadItemInfo where = new DownloadItemInfo();
            where.setId(recordId);
            List<DownloadItemInfo> resultList = super.query(where);
            if (resultList.size() > 0) {
                return resultList.get(0);
            }
            return null;
        }

    }

    /**
     * 比较器
     */
    class DownloadInfoComparator implements Comparator<DownloadItemInfo> {

        @Override
        public int compare(DownloadItemInfo lhs, DownloadItemInfo rhs) {
            return lhs.getId() - rhs.getId();
        }
    }
}
