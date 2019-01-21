package top.hejiaxuan;

import org.junit.Test;

import static org.junit.Assert.*;

public class NameConvertTest {

    @Test
    public void entityName() {
        System.out.println(NameConvert.entityName("qeweqw_123qwe"));
    }

    @Test
    public void fieldName() {
        System.out.println(NameConvert.fieldName("qeweqw_sdas_123"));
    }
}