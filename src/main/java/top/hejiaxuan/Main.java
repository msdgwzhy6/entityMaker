package top.hejiaxuan;

import top.hejiaxuan.maker.ColumnFieldTypeMapping;
import top.hejiaxuan.maker.EntityMaker;

import java.sql.SQLException;

public class Main {


    public static void main(String[] args) throws SQLException {
        EntityMaker entityMaker = new EntityMaker();
        entityMaker.setColumnFieldTypeMapping(new ColumnFieldTypeMapping());
        entityMaker.maker();
    }
}
