package top.hejiaxuan.model;

import java.util.*;

/**
 * 用于生成java Entity文件的类
 */
public class ClassModel {

    /**
     * java 中不需要引包的类型
     */
    private static List<Class> baseClass = Arrays.asList(
            int.class,
            double.class,
            float.class,
            long.class,
            short.class,
            byte.class,
            char.class,
            boolean.class,
            String.class
    );

    /**
     * 类注释
     */
    private String classDoc;

    /**
     * 类名
     */
    private String className;

    /**
     * 类 包名
     */
    private String packageName;

    /**
     * K:属性名称
     * V:属性类型
     */
    private Map<String, Class> fields = new HashMap<>();

    /**
     * 属性的注释
     */
    private Map<String, String> fieldDoc = new HashMap<>();
    ;

    private List<Class> imports = new ArrayList<>();

    /**
     * 添加需要导入的包
     *
     * @param importClass
     */
    public void addImport(Class importClass) {
        if (baseClass.indexOf(importClass) != -1) {
            return;
        }
        if (imports.indexOf(importClass) == -1) {
            imports.add(importClass);
        }
    }

    /**
     * 添加属性
     *
     * @param fieldName  属性名称
     * @param fieldClass 属性类型
     */
    public void addfield(String fieldName, Class fieldClass) {
        if (!fields.containsKey(fieldName)) {
            fields.put(fieldName, fieldClass);
        }
    }

    /**
     * 添加属性注释
     *
     * @param fieldName 属性名称
     * @param fieldDoc  属性注释
     */
    public void addfieldDoc(String fieldName, String fieldDoc) {
        if (!this.fieldDoc.containsKey(fieldName)) {
            this.fieldDoc.put(fieldName, fieldDoc);
        }
    }

    public List<Class> getImports() {
        return imports;
    }

    public void setImports(List<Class> imports) {
        this.imports = imports;
    }

    public String getClassDoc() {
        return classDoc;
    }

    public void setClassDoc(String classDoc) {
        this.classDoc = classDoc;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Map<String, Class> getFields() {
        return fields;
    }

    public void setFields(Map<String, Class> fields) {
        this.fields = fields;
    }

    public Map<String, String> getFieldDoc() {
        return fieldDoc;
    }

    public void setFieldDoc(Map<String, String> fieldDoc) {
        this.fieldDoc = fieldDoc;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("            \"classDoc\"=\"").append(classDoc).append('\"');
        sb.append(",             \"className\"=\"").append(className).append('\"');
        sb.append(",             \"packageName\"=\"").append(packageName).append('\"');
        sb.append(",             \"fields\"=").append(fields);
        sb.append(",             \"fieldDoc\"=").append(fieldDoc);
        sb.append(",             \"imports\"=").append(imports);
        sb.append('}');
        return sb.toString();
    }
}
