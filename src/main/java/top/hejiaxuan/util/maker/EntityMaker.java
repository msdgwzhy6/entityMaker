package top.hejiaxuan.util.maker;

import com.mysql.jdbc.Connection;
import top.hejiaxuan.util.FileUtils;
import top.hejiaxuan.util.FreeMarkerUtils;
import top.hejiaxuan.util.NameConvert;
import top.hejiaxuan.util.jdbc.annotation.Column;
import top.hejiaxuan.util.jdbc.annotation.Table;
import top.hejiaxuan.util.model.EntityModel;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据库表生成entity工具
 */
public class EntityMaker {

    static final String DOT = ".";

    static final String FILE_TYPE = ".java";

    static final String ENTITY_TEMPLET_PATH = "EntityTemp.ftl";

    /**
     * 数据库连接
     */
    private Connection connection;

    /**
     * 数据库字段类型对应java属性类型映射
     */
    private Map<String, Class> sqlFieldTypeMapping;

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

    public void maker() {
        List<EntityModel> classModels = readDatabases();
        for (EntityModel classModel : classModels) {
            makeOneClass(classModel);
        }
        System.out.println("SUCCESS :" + classModels.size() + " files.");
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
     * 读取数据库信息
     */
    List<EntityModel> readDatabases() {
        List<EntityModel> classModels = new ArrayList<>();
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet dataTables = metaData.getTables(null, "%", "%", new String[]{"TABLE"});
            while (dataTables.next()) {
                String tableName = dataTables.getString("TABLE_NAME");
                String remarks = dataTables.getString("REMARKS");
                ResultSet columns = metaData.getColumns(null, "%", tableName, "%");
                EntityModel model = makeModel(tableName, remarks, columns);
                classModels.add(model);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classModels;
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
                System.out.printf("columnName:%-20s columnType:%-20s \n", columnName, columnType);
                String fieldName = nameConvert.fieldName(columnName);
                Class fieldClass = sqlFieldTypeMapping.get(columnType);
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
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setSqlFieldTypeMapping(Map<String, Class> sqlFieldTypeMapping) {
        this.sqlFieldTypeMapping = sqlFieldTypeMapping;
    }

    public EntityMaker(String entityClassPackage) {
        this.entityClassPackage = entityClassPackage;
    }

    public void setEntityClassPackage(String entityClassPackage) {
        this.entityClassPackage = entityClassPackage;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public NameConvert getNameConvert() {
        return nameConvert;
    }

    public void setNameConvert(NameConvert nameConvert) {
        this.nameConvert = nameConvert;
    }
}
