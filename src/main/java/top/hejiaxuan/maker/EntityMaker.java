package top.hejiaxuan.maker;

import com.mysql.jdbc.Connection;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import top.hejiaxuan.NameConvert;
import top.hejiaxuan.model.EntityModel;
import top.hejiaxuan.util.FileUtils;
import top.hejiaxuan.util.FreeMarkerUtils;
import top.hejiaxuan.util.SqlUtils;
import top.hejiaxuan.util.XmlUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Map;

/**
 * 数据库表生成entity工具
 */
public class EntityMaker {

    static final String DOT = ".";

    static final String FILE_TYPE = ".java";

    static final String ENTITY_TEMPLET_PATH = "EntityTemp.ftl";

    static final String CONFIG_PATH = "config.xml";

    /**
     * 数据库连接
     */
    private Connection connection;

    /**
     * 数据库字段类型对应java属性类型映射
     */
    private ColumnFieldTypeMapping columnFieldTypeMapping;

    /**
     * 生成的class包名
     */
    private String entityPackage;

    /**
     * 生成的文件基础路径
     */
    private String basePath;


    /**
     * 生成entity
     *
     * @param imports 需要导入的特殊的class
     */
    public void maker(Class... imports) {
        List<String> tableNames = showTables();
        for (String tableName : tableNames) {
            String createTableSql = getCreateTableSql(tableName);
            EntityModel entityModel = makeModelBySql(createTableSql);
            for (Class importClass : imports) {
                entityModel.addImport(importClass);
            }
            boolean b = makeOneClass(entityModel);
            System.out.printf("创建class：%-20s %-20s  %s \n", entityModel.getClassDoc(), tableName, b);
            Map<String, Class> fields = entityModel.getFields();
            for (Map.Entry<String, Class> entry : fields.entrySet()) {
                System.out.printf("         字段：%-20s  %s \n", entry.getKey(), entry.getValue().getSimpleName());
            }
        }
    }

    /**
     * 根据建表语句组装EntityModel
     *
     * @param createTableSql
     * @return
     */
    EntityModel makeModelBySql(String createTableSql) {
        Formatter formatter = new Formatter();
        EntityModel model = new EntityModel();
        String tableComment = SqlUtils.getTableComment(createTableSql);
        String tableName = SqlUtils.getTableName(createTableSql);
        String id = SqlUtils.getId(createTableSql);
        model.addIdColumnName(id);
        model.setClassName(NameConvert.entityName(tableName));
        model.setTableName(tableName);
        //注释是null的时候用数据库表名作为注释
        model.setClassDoc(tableComment == null ? tableName : tableComment);
        List<String> line = SqlUtils.getColumnSqls(createTableSql);
        for (String oneLine : line) {
            String columnName = SqlUtils.getByPattern(oneLine, "`(.*)`", 1);
            String comment = SqlUtils.getColumnComment(oneLine);
            String columnType = SqlUtils.getByPattern(oneLine, "`" + columnName + "` ([A-Za-z]*)", 1);
            String fieldName = NameConvert.fieldName(columnName);
            Class fieldClass = columnFieldTypeMapping.getFieldType(columnType);
            if (fieldClass == null) {
                formatter.format("table:%s columnName:%s sql类型:%s 没有映射类型", tableName, columnName, columnType);
                throw new UnsupportedOperationException(formatter.toString());
            }
            model.addfield(fieldName, fieldClass);
            //字段注释是null的时候用数据库字段名作为注释
            model.addfieldDoc(fieldName, comment == null ? columnName : comment);
            model.addfieldSqlName(fieldName, columnName);
            model.addImport(fieldClass);
        }
        return model;
    }


    /**
     * 找出数据库表的建表语句
     *
     * @param tableName
     * @return
     */
    String getCreateTableSql(String tableName) {
        String createTableSql = null;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("show CREATE TABLE " + tableName);
            while (resultSet.next()) {
                createTableSql = resultSet.getString(2);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return createTableSql;
    }

    /**
     * 显示所有的数据库中的表
     *
     * @return
     */
    List<String> showTables() {
        List<String> tables = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SHOW TABLES;");
            while (resultSet.next()) {
                String tableName = resultSet.getString(1);
                tables.add(tableName);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    /**
     * 用于生成一个类文件
     *
     * @param entityModel
     * @return
     */
    boolean makeOneClass(EntityModel entityModel) {
        entityModel.setPackageName(entityPackage);
        String filePath = basePath + "/" + entityPackage.replace(DOT, "/") + "/" + entityModel.getClassName() + FILE_TYPE;
        try {
            String javaClassString = FreeMarkerUtils.getJavaClass(entityModel, ENTITY_TEMPLET_PATH);
            FileUtils.write(filePath, javaClassString);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public EntityMaker() {
        Document configXml = XmlUtils.getConfigDocument(CONFIG_PATH);
        Element element = configXml.getDocumentElement();
        String jdbcUrl = element.getElementsByTagName("jdbc.url").item(0).getTextContent();
        String username = element.getElementsByTagName("jdbc.username").item(0).getTextContent();
        String password = element.getElementsByTagName("jdbc.password").item(0).getTextContent();
        String basePath = element.getElementsByTagName("basePath").item(0).getTextContent();
        String entityPackage = element.getElementsByTagName("entityPackage").item(0).getTextContent();
        try {
            Connection conn = (Connection) DriverManager.getConnection(jdbcUrl, username, password);
            this.connection = conn;
            this.basePath = basePath;
            this.entityPackage = entityPackage;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void setColumnFieldTypeMapping(ColumnFieldTypeMapping columnFieldTypeMapping) {
        this.columnFieldTypeMapping = columnFieldTypeMapping;
    }

}
