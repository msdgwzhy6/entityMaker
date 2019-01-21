package top.hejiaxuan;

/**
 * 字段名称转换
 * @author hjx
 */
public class NameConvert {

    /**
     * 类名称转换
     *
     * @param tableName
     * @return
     */
    public static String entityName(String tableName) {
        String lowerCaseName = tableName.toLowerCase();
        StringBuilder newName = new StringBuilder();
        char[] chars = lowerCaseName.toCharArray();
        boolean change = false;
        for (int i = 0; i < chars.length; i++) {
            char aChar = chars[i];
            if (aChar == '_' && !change) {
                change = true;
                continue;
            }
            //首字母大写
            if (i == 0) {
                aChar = Character.toUpperCase(aChar);
            }
            if (change) {
                aChar = Character.toUpperCase(aChar);
                change = false;
            }
            newName.append(aChar);
        }
        return newName.toString();
    }

    /**
     * 属性名称转换
     *
     * @param name
     * @return
     */
    public static String fieldName(String name) {
        name = name.toLowerCase();
        StringBuilder newName = new StringBuilder();
        char[] chars = name.toCharArray();
        boolean change = false;
        for (int i = 0; i < chars.length; i++) {
            char aChar = chars[i];
            if (aChar == '_' && !change) {
                change = true;
                continue;
            }
            if (change) {
                aChar = Character.toUpperCase(aChar);
                change = false;
            }
            newName.append(aChar);
        }
        return newName.toString();
    }
}
