package velly.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Auther: admin
 * @Date: 2019/10/9
 * @Describe : 文件工具类
 */
public final class FileUtil {

    /**
     * 复制单个文件(可更名复制)
     * @param oldPathFile 准备复制的文件源
     * @param newPathFile 拷贝到新绝对路径带文件名(注：目录路径需带文件名)
     * @return
     */
    public static void CopySingleFile(String oldPathFile, String newPathFile) {
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPathFile);
            File newFile=new File(newPathFile);
            File parentFile=newFile.getParentFile();
            if(!parentFile.exists())
            {
                parentFile.mkdirs();
            }
            if (oldfile.exists()) { //文件存在时
                inStream = new FileInputStream(oldPathFile); //读入原文件
                fs = new FileOutputStream(newPathFile);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeQuietly(inStream);
            closeQuietly(fs);
        }
    }

    /**
     * 关闭流
     * @param closeable a source or destination of data that can be closed.
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
