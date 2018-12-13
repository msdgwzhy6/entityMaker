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

    public static String getId(String sql) {
        if (sql.indexOf("PRIMARY KEY") == -1) {
            return null;
        }
        Pattern pattern = Pattern.compile("`(.*)`");
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String getColumnName(String sql) {
        Pattern pattern = Pattern.compile("`(.*)`");
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String getComment(String sql) {
        Pattern pattern = Pattern.compile("COMMENT '(.*)'");
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String getTableComment(String sql) {
        Pattern pattern = Pattern.compile("COMMENT='(.*)'");
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String getColumnType(String columnName, String sql) {
        Pattern pattern = Pattern.compile("`" + columnName + "` ([A-Za-z]*)");
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
