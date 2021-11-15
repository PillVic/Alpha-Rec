package com.alpharec.util;

public class ObjectAnalyzer {
    public static <T> String ToString(T t) {
        StringBuilder s = new StringBuilder();
        s.append(String.format("%s{", t.getClass().getName()));
        for (var field : t.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object obj = null;
            try {
                obj = field.get(t);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (obj != null) {
                s.append(String.format("%s=%s;", field.getName(), obj));
            }
        }
        s.trimToSize();
        s.append("}");
        return s.toString();
    }

    public static <T> int HashCode(T t) {
        return ToString(t).hashCode();
    }
}
