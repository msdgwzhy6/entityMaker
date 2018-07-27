package top.hejiaxuan.util.maker;

import org.junit.Test;
import top.hejiaxuan.util.NameConvert;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EntityMakerTest {

    EntityMaker entityMaker = new EntityMaker();

    {
        entityMaker.setColumnFieldTypeMapping(new ColumnFieldTypeMapping());
        entityMaker.setNameConvert(new NameConvert());
    }

    @Test
    public void maker() throws SQLException {
        entityMaker.maker();
    }

}