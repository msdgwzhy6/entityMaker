package top.hejiaxuan;

import freemarker.cache.*;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class FreeMarkerUtils {

    public static String getJavaClass(Object subjectParams, String templetPath) throws Exception {
        StringTemplateLoader loader = new StringTemplateLoader();
        Scanner scanner = new Scanner(Thread.currentThread().getContextClassLoader().getResourceAsStream(templetPath));
        StringBuilder builder = new StringBuilder();
        while (scanner.hasNext()) {
            builder.append(scanner.nextLine()).append("\n");
        }
        String name = System.currentTimeMillis() + "";
        loader.putTemplate(name, builder.toString());
        //第一步：实例化Freemarker的配置类
        Configuration conf = new Configuration();
        conf.setObjectWrapper(new DefaultObjectWrapper());
        conf.setLocale(Locale.CHINA);
        conf.setDefaultEncoding("utf-8");
        conf.setTemplateLoader(loader);
        //处理空值为空字符串
        conf.setClassicCompatible(true);
        Template template = conf.getTemplate(name);

        Writer out = new StringWriter(2048);
        template.process(subjectParams, out);
        String javaClass = out.toString();
        return javaClass;
    }
}
