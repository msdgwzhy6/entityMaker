package top.hejiaxuan.util;

import org.junit.Test;

import java.util.List;

public class SqlUtilsTest {

    public static final String sql = "CREATE TABLE `user` (                                                            \n" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',                         \n" +
            "  `name` varchar(225) DEFAULT NULL COMMENT '用户名',                             \n" +
            "  `create_date` datetime DEFAULT NULL,                                           \n" +
            "  `status` int(11) DEFAULT NULL,                                                 \n" +
            "  `age` int(11) DEFAULT NULL COMMENT '年龄',                                     \n" +
            "  `mark` varchar(225) DEFAULT NULL,                                              \n" +
            "  PRIMARY KEY (`id`)                                                             \n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=2104778081 DEFAULT CHARSET=latin1 COMMENT='用户表'  ";


    @Test
    public void getTableName() {
        System.out.println(SqlUtils.getTableName(sql));
    }

    @Test
    public void getTableComment() {
        System.out.println(SqlUtils.getTableComment(sql));
    }

    @Test
    public void getId() {
        System.out.println(SqlUtils.getId(sql));
    }

    @Test
    public void getColumns() {
        List<String> columns = SqlUtils.getColumnSqls(sql);
        for (String oneLine : columns) {
            System.out.println(oneLine);
            String columnName = SqlUtils.getByPattern(oneLine, "`(.*)`", 1);
            String comment = SqlUtils.getByPattern(oneLine, "COMMENT '(.*)'", 1);
            String columnType = SqlUtils.getByPattern(oneLine, "`" + columnName + "` ([A-Za-z]*)", 1);
            System.out.printf("名称：%-20s 类型：%-20s 注释：%-20s \n", columnName, columnType, comment);
        }
    }
}