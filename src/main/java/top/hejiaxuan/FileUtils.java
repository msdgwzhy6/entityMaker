package top.hejiaxuan;

import java.io.*;

public class FileUtils {

    /**
     * 写入文件
     *
     * @param path    文件路径
     * @param content 文件内容
     */
    public static void write(String path, String content) {
        File file = new File(path);
        File parentFile = file.getParentFile();
        try {
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
