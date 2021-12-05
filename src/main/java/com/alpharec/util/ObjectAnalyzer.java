package com.alpharec.util;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.min;

/**
 * 通用的静态处理方法
 * @author pillvic
* */
public class ObjectAnalyzer {
    public static <T> String toJsonString(T t) {
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

    public static <T> boolean isNullOrEmptyList(List<T> list) {
        return list == null || list.isEmpty();
    }

    public static <K, V> boolean isNullOrEmptyMap(Map<K, V> map) {
        return map == null || map.isEmpty();
    }

    public static <T> boolean isNullOrEmptySet(Set<T> set) {
        return set == null || set.isEmpty();
    }

    public static <T> int getHashCode(T t) {
        return toJsonString(t).hashCode();
    }

    public static <T> List<T> getTopN(int n, List<T> list, Comparator<T> comparator) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        return list.stream().sorted(comparator)
                .limit(n).collect(Collectors.toList());
    }

    public static <T> List<List<T>> subList(List<T> list, int subSize) {
        List<List<T>> r = new ArrayList<>();
        if (list == null || list.isEmpty()) {
            return r;
        }
        for (int i = 0; i < list.size(); i += subSize) {
            r.add(list.subList(i, min(i + subSize, list.size())));
        }
        return r;
    }
}
