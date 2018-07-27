package top.hejiaxuan.util.maker;

import com.mysql.jdbc.Connection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import top.hejiaxuan.util.FileUtils;
import top.hejiaxuan.util.FreeMarkerUtils;
import top.hejiaxuan.util.NameConvert;
import top.hejiaxuan.util.jdbc.annotation.Column;
import top.hejiaxuan.util.jdbc.annotation.Table;
import top.hejiaxuan.util.model.EntityModel;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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
     * 读取数据库信息
     */
    void maker() throws SQLException {
        List<EntityModel> classModels = new ArrayList<>();
        DatabaseMetaData metaData = connection.getMetaData();
        final ResultSet dataTables = metaData.getTables(null, "%", "%", new String[]{"TABLE"});
        while (dataTables.next()) {
            String tableName = dataTables.getString("TABLE_NAME");
            String remarks = dataTables.getString("REMARKS");
            ResultSet columns = metaData.getColumns(null, "%", tableName, "%");
            EntityModel model = makeModel(tableName, remarks, columns);
            boolean b = makeOneClass(model);
            System.out.printf("Result: %-5s entity: %-40s\n", b, model.getClassName());
        }
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

    /**
     * @param tableName
     * @param classRemarks
     * @param resultSet
     * @return
     */
    EntityModel makeModel(String tableName, String classRemarks, ResultSet resultSet) {
        try {
            EntityModel model = new EntityModel();
            while (resultSet.next()) {
                String columnName = resultSet.getString("COLUMN_NAME");
                String columnType = resultSet.getString("TYPE_NAME");
                String remarks = resultSet.getString("REMARKS");
                //System.out.printf("columnName:%-20s columnType:%-20s \n", columnName, columnType);
                String fieldName = nameConvert.fieldName(columnName);
                Class fieldClass = columnFieldTypeMapping.getFieldType(columnType);
                if (fieldClass == null) {
                    Formatter formatter = new Formatter();
                    formatter.format("table:%s columnName:%s sql类型:%s 没有映射类型", tableName, columnName, columnType);
                    throw new UnsupportedOperationException(formatter.toString());
                }
                model.addfield(fieldName, fieldClass);
                model.addfieldDoc(columnName, remarks);
                model.addfieldSqlName(fieldName, columnName);
                model.addImport(fieldClass);
            }
            model.addImport(Column.class);
            model.addImport(Table.class);
            model.setClassName(nameConvert.entityName(tableName));
            model.setTableName(tableName);
            return model;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
