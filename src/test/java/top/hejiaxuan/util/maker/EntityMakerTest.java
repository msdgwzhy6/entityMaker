package top.hejiaxuan.util.maker;

import com.mysql.jdbc.Connection;
import org.junit.Test;
import top.hejiaxuan.util.NameConvert;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EntityMakerTest {

    EntityMaker entityMaker = new EntityMaker();

    {
        try {
            Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://{ip}:{port}/pay?user={username}&password={password}&useSSL=true");

            Map<String, Class> sqlFieldTypeMapping = new HashMap<>();
            sqlFieldTypeMapping.put("INT", int.class);
            sqlFieldTypeMapping.put("VARCHAR", String.class);
            sqlFieldTypeMapping.put("DATETIME", Date.class);
            sqlFieldTypeMapping.put("FLOAT", float.class);
            sqlFieldTypeMapping.put("DOUBLE", double.class);
            sqlFieldTypeMapping.put("TINYINT", int.class);

            entityMaker.setEntityClassPackage("top.hejiaxuan");
            entityMaker.setConnection(conn);
            entityMaker.setSqlFieldTypeMapping(sqlFieldTypeMapping);
            entityMaker.setBasePath("/home/hjx/Class");
            entityMaker.setNameConvert(new NameConvert());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void name() {
        entityMaker.maker();
    }

}