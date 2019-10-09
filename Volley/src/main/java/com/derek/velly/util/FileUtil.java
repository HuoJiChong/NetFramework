package com.derek.velly.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * @Auther: admin
 * @Date: 2019/10/9
 * @Describe : 文件工具类
 */
public final class FileUtil {

    /**
     * 流关闭
     * @param closeable 流文件
     */
    public static void closeQuietly(Closeable closeable){
        if (closeable != null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
