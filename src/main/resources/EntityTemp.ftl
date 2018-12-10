package ${packageName};

<#--导入的包-->
<#list imports as import>
import ${import.name};
</#list>

<#--类名-->
<#if classDoc?length gt 0>
/**
 * ${classDoc}
 */
</#if>
@Table(value="${tableName}")
public class ${className} {

<#--属性名称-->
<#list fields?keys as key>
    <#assign  fieldDocStr = fieldDoc[key]>
    <#if fieldDocStr?length gt 0>
    /**${fieldDocStr}*/
    </#if>
    <#if idColumnNames?seq_contains(fieldSqlName[key])>
    @ID
    </#if>
    @Column(value="${fieldSqlName[key]}")
    private ${fields[key].simpleName} ${key};

</#list>
<#list fields?keys as key>
    <#assign  fieldClass = fields[key].simpleName>
<#--setter-->
    public void set${key?cap_first}(${fieldClass} ${key}){
        this.${key} = ${key};
    }

<#--getter-->
    public ${fieldClass} <#if fieldClass="boolean">is<#else>get</#if>${key?cap_first}(){
        return this.${key};
    }
</#list>

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("[");
<#list fields?keys as key>
        sb.append("${key}:").append(${key}).append(";    ");
</#list>
        sb.append("]");
        return sb.toString();
    }
}