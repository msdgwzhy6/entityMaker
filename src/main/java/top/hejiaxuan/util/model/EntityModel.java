package top.hejiaxuan.util.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库映射
 */
public class EntityModel extends ClassModel {

    /**
     * 数据库名称
     */
    private String tableName;

    /**
     * 数据库中的Id字段名称
     */
    private List<String> idColumnNames = new ArrayList<>();

    /**
     * 类属性名对应数据库字段映射
     * key: class 属性名称
     * value：数据库字段名
     */
    private Map<String, String> fieldSqlName = new HashMap<>();

    /**
     * 添加class 属性映射和 数据库 字段映射
     *
     * @param fieldName
     * @param sqlName
     */
    public void addfieldSqlName(String fieldName, String sqlName) {
        if (!fieldSqlName.containsKey(fieldName)) {
            fieldSqlName.put(fieldName, sqlName);
        }
    }

    /**
     * 添加id字段名
     *
     * @param idColumnName
     */
    public void addIdColumnName(String idColumnName) {
        idColumnNames.add(idColumnName);
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, String> getFieldSqlName() {
        return fieldSqlName;
    }

    public void setFieldSqlName(Map<String, String> fieldSqlName) {
        this.fieldSqlName = fieldSqlName;
    }

    public List<String> getIdColumnNames() {
        return idColumnNames;
    }

    public void setIdColumnNames(List<String> idColumnNames) {
        this.idColumnNames = idColumnNames;
    }
}
