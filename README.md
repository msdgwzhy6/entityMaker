# 从数据库生成实体类

## 使用

1.  配置数据库链接

    编辑文件：**entityMaker/src/main/resources/config.xml**

    ```xml
    <xml>
        <jdbc.url>数据库链接</jdbc.url>
        <jdbc.username>数据库用户名</jdbc.username>
        <jdbc.password>数据库密码</jdbc.password>
        <basePath>项目路径/src/test/java</basePath>
        <entityPackage>生成实体类的包名</entityPackage>
    </xml>
    ```

2.  执行代码

    运行 EntityMaker.main()

    ```java
     /**
      * 程序入口
      *
      * @param args
      * @throws SQLException
      */
     public static void main(String[] args) {
         EntityMaker entityMaker = new EntityMaker();
         entityMaker.setColumnFieldTypeMapping(new ColumnFieldTypeMapping());
         //参数是要额外要 import 的类
         entityMaker.maker(
                 Table.class,
                 Column.class,
                 Id.class
         );
     }
    ```

## 如果需要修改模板的话

使用**freemarker**语法。

编辑模板文件：**entityMaker/src/main/resources/EntityTemp.ftl**



