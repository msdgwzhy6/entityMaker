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

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

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
    private String entityClassPackage;

    /**
     * 生成的文件基础路径
     */
    private String basePath;

    /**
     * 数据库字段转换器
     */
    private NameConvert nameConvert;

    /**
     * 生成entity
     */
    public void maker() {
        List<String> tableNames = showTables();
        for (String tableName : tableNames) {
            String createTableSql = getCreateTableSql(tableName);
            EntityModel entityModel = makeModelBySql(tableName, createTableSql);

            //添加其他的导入class
            entityModel.addImport(ApiModel.class);
            entityModel.addImport(ApiModelProperty.class);

            boolean b = makeOneClass(entityModel);
            System.out.printf("创建class：%-20s %-20s  %s \n", entityModel.getClassDoc(), tableName, b);
            Map<String, Class> fields = entityModel.getFields();
            for (Map.Entry<String, Class> entry : fields.entrySet()) {
                System.out.printf("         字段：%-20s  %s \n", entry.getKey(), entry.getValue().getSimpleName());
            }
        }
    }

    /**
     * @param tableName
     * @param createTableSql
     * @return
     */
    EntityModel makeModelBySql(String tableName, String createTableSql) {
        Formatter formatter = new Formatter();
        EntityModel model = new EntityModel();
        List<String> line = SqlUtils.byLine(createTableSql);
        model.setClassName(nameConvert.entityName(tableName));
        model.setTableName(tableName);
        model.addImport(Table.class);
        model.addImport(Column.class);
        String tableComment = SqlUtils.getTableComment(line.get(line.size() - 1));
        //注释是null的时候用数据库表名作为注释
        model.setClassDoc(tableComment == null ? tableName : tableComment);

        //解析数据库字段名，字段类型，字段注释
        //从第1行开始解析,解析到倒数第2行
        for (int i = 1; i < line.size(); i++) {
            String oneLine = line.get(i);
            String columnName = SqlUtils.getColumnName(oneLine);
            String comment = SqlUtils.getComment(oneLine);
            String columnType = SqlUtils.getColumnType(columnName, oneLine);
            String id = SqlUtils.getId(oneLine);
            //如果到了id 的一行 或者 没有找到有效字段，跳出循环
            if (id != null || columnName == null) {
                model.addIdColumnName(id);
                model.addImport(Id.class);
                break;
            }
            String fieldName = nameConvert.fieldName(columnName);
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
     * @param model
     * @return
     */
    boolean makeOneClass(EntityModel model) {
        model.setPackageName(entityClassPackage);
        String filePath = basePath + "/" + entityClassPackage.replace(DOT, "/") + "/" + model.getClassName() + FILE_TYPE;
        try {
            String javaClassString = FreeMarkerUtils.getJavaClass(model, ENTITY_TEMPLET_PATH);
            FileUtils.write(filePath, javaClassString);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public EntityMaker() {
        Document configXml = getConfigXml();
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
            this.entityClassPackage = entityPackage;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    Document getConfigXml() {
        try {
            InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_PATH);
            Scanner scanner = new Scanner(resourceAsStream);
            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine()).append("\n");
            }
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilde = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilde.parse(new ByteArrayInputStream(stringBuilder.toString().getBytes()));
            return document;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setColumnFieldTypeMapping(ColumnFieldTypeMapping columnFieldTypeMapping) {
        this.columnFieldTypeMapping = columnFieldTypeMapping;
    }

    public void setNameConvert(NameConvert nameConvert) {
        this.nameConvert = nameConvert;
    }
}
