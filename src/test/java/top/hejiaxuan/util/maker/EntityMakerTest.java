package top.hejiaxuan.util.maker;

import org.junit.Test;
import top.hejiaxuan.util.NameConvert;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EntityMakerTest {

    EntityMaker entityMaker = new EntityMaker();

    {
        Map<String, Class> sqlFieldTypeMapping = new HashMap<>();
        sqlFieldTypeMapping.put("VARCHAR", String.class);
        sqlFieldTypeMapping.put("CHAR", String.class);
        sqlFieldTypeMapping.put("TEXT", String.class);
        sqlFieldTypeMapping.put("MEDIUMTEXT", String.class);
        sqlFieldTypeMapping.put("LONGTEXT", String.class);
        sqlFieldTypeMapping.put("TINYTEXT", String.class);

        sqlFieldTypeMapping.put("BIT", Boolean.class);

        sqlFieldTypeMapping.put("INT", int.class);
        sqlFieldTypeMapping.put("BIGINT", long.class);
        sqlFieldTypeMapping.put("DOUBLE", double.class);
        sqlFieldTypeMapping.put("TINYINT", int.class);
        sqlFieldTypeMapping.put("FLOAT", float.class);
        sqlFieldTypeMapping.put("DECIMAL", BigDecimal.class);

        sqlFieldTypeMapping.put("INT UNSIGNED", int.class);
        sqlFieldTypeMapping.put("BIGINT UNSIGNED", int.class);
        sqlFieldTypeMapping.put("DECIMAL UNSIGNED", BigDecimal.class);

        sqlFieldTypeMapping.put("DATETIME", Date.class);
        sqlFieldTypeMapping.put("TIME", Date.class);
        sqlFieldTypeMapping.put("DATE", Date.class);
        sqlFieldTypeMapping.put("TIMESTAMP", Date.class);

        entityMaker.setSqlFieldTypeMapping(sqlFieldTypeMapping);
        entityMaker.setNameConvert(new NameConvert());
    }

    @Test
    public void maker() throws SQLException {
        entityMaker.maker();
    }

}