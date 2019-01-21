package top.hejiaxuan.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hjx
 */
public class SqlUtils {

    public static List<String> byLine(String sql) {
        List<String> lines = new ArrayList<>();
        Scanner scanner = new Scanner(sql);
        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();
            lines.add(nextLine);
        }
        return lines;
    }

    /**
     * 获得表的名称
     *
     * @param sql
     * @return
     */
    public static String getTableName(String sql) {
        return getByPattern(sql, "CREATE TABLE `(.*)`", 1);
    }

    /**
     * 获取表的注释
     *
     * @param sql
     * @return
     */
    public static String getTableComment(String sql) {
        return getByPattern(sql, "\\) .* COMMENT='(.*)'", 1);
    }

    /**
     * 获取id
     *
     * @param sql
     * @return
     */
    public static String getId(String sql) {
        return getByPattern(sql, "PRIMARY KEY \\(`(.*)`\\)", 1);
    }

    /**
     * 获取建表语句中和字段相关的sql
     *
     * @param sql
     * @return
     */
    public static List<String> getColumnSqls(String sql) {
        List<String> lines = new ArrayList<>();
        Scanner scanner = new Scanner(sql);
        boolean start = false;
        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();
            if (nextLine.indexOf("CREATE TABLE") != -1) {
                start = true;
                continue;
            }
            //没想到有的表没有id /(ㄒoㄒ)/~~
            if (nextLine.indexOf("PRIMARY KEY") != -1 || nextLine.indexOf("ENGINE=") != -1) {
                start = false;
                continue;
            }
            if (start) {
                lines.add(nextLine);
            }
        }
        return lines;
    }


    /**
     * 根据正则查找
     *
     * @param sql
     * @param pattern
     * @param group
     * @return
     */
    public static String getByPattern(String sql, String pattern, int group) {
        Pattern compile = Pattern.compile(pattern);
        Matcher matcher = compile.matcher(sql);
        while (matcher.find()) {
            return matcher.group(group);
        }
        return null;
    }
}
