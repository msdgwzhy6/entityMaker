package top.hejiaxuan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import top.hejiaxuan.maker.ColumnFieldTypeMapping;
import top.hejiaxuan.maker.EntityMaker;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.SQLException;

public class Main {


    /**
     * 程序入口
     *
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) {
        EntityMaker entityMaker = new EntityMaker();
        entityMaker.setColumnFieldTypeMapping(new ColumnFieldTypeMapping());
        entityMaker.maker(
//                ApiModel.class,
//                ApiModelProperty.class,
                Table.class,
                Column.class,
                Id.class
        );
    }
}
