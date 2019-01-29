package com.hebaibai.entitymaker;

import org.junit.Test;
import com.hebaibai.entitymaker.util.NameConvert;

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