package top.hejiaxuan.util;

/**
 * 字段名称转换
 */
public class NameConvert {

    public String entityName(String name) {
        StringBuilder newName = new StringBuilder();
        char[] chars = name.toCharArray();
        boolean change = false;
        for (int i = 0; i < chars.length; i++) {
            char aChar = chars[i];
            if (i == 0) {
                continue;
            } else if (aChar == '_' && !change) {
                change = true;
                continue;
            } else if (change) {
                aChar = Character.toUpperCase(aChar);
                change = false;
            }
            newName.append(aChar);
        }
        return newName.toString();
    }

    public String fieldName(String name) {
        StringBuilder newName = new StringBuilder();
        char[] chars = name.toCharArray();
        boolean change = false;
        for (int i = 0; i < chars.length; i++) {
            char aChar = chars[i];
            if (aChar == '_' && !change) {
                change = true;
                continue;
            } else if (change) {
                aChar = Character.toUpperCase(aChar);
                change = false;
            }
            newName.append(aChar);
        }
        return newName.toString();
    }
}
