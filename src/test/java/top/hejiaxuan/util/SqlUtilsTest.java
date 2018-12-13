package top.hejiaxuan.util;

import org.junit.Test;

public class SqlUtilsTest {

    public static final String sql = "CREATE TABLE `tbl_accesscore_log` (                                                      \n" +
            " `ID` char(32) NOT NULL COMMENT '主键',                                                 \n" +
            " `POLICY_NO` varchar(30) DEFAULT NULL COMMENT '保单号',                                 \n" +
            " `BEIGIN_TIME` timestamp NULL DEFAULT NULL COMMENT '当前时间',                          \n" +
            " `RESPONSE_MESSAGE` text COMMENT '响应报文',                                            \n" +
            " `DIVIDER` char(3) DEFAULT NULL COMMENT '分库标识',                                     \n" +
            " `REQUEST_MESSAGE` text COMMENT '发送给核心的报文',                                     \n" +
            " `END_TIME` datetime DEFAULT NULL COMMENT '发送结束时间',                               \n" +
            " `LOG_STATUS` varchar(10) DEFAULT NULL COMMENT '状态 1:success 0:error',                \n" +
            " `LOG_TYPE` varchar(3) DEFAULT NULL COMMENT '1: request\\n0: response',                  \n" +
            " `REQUEST_TYPE` varchar(225) DEFAULT NULL COMMENT '请求类型: 保费计算,核保....的编号',  \n" +
            " PRIMARY KEY (`ID`)                                                                     \n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='核心交互日志表'     ";


    @Test
    public void getId() {
        System.out.println(SqlUtils.getId(sql));
    }

    @Test
    public void getCommentType() {
        String s = "   `BEIGIN_TIME` timestamp NULL DEFAULT NULL COMMENT '当前时间', ";
        System.out.println(SqlUtils.getColumnType("BEIGIN_TIME", s));
    }

    @Test
    public void byLine() {
        System.out.println(SqlUtils.byLine(sql));
    }


}