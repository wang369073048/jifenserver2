package org.trc.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hzwzhen on 2017/6/23.
 */
public class ListUtils {

    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.size() == 0;
    }

    public static <T> boolean isNotEmpty(List<T> list) {
        return !isEmpty(list);
    }

    public static <T> boolean isBlank(List<T> list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        for (T t : list) {
            if (t == null) {
                //
            } else if (t instanceof String) {
                if (StringUtils.isNotBlank((String) t)) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean isNotBlank(List<T> list) {
        return !isBlank(list);
    }

    public static <T, P> boolean contains(List<T> list, P p) {
        if (isEmpty(list)) {
            return false;
        }
        return list.contains(p);
    }

    public static <T> T get(List<T> list, int index) {
        if (isEmpty(list)) {
            return null;
        }
        return list.get(index);
    }

    public static <T> T getFirst(List<T> list) {
        return get(list, 0);
    }

    public static <T> T getLast(List<T> list) {
        return get(list, list.size() - 1);
    }

    public static <T> List<T> emptyList() {
        return Collections.emptyList();
    }

    public static <T> List<T> defaultList(List<T> list) {
        if (list == null) {
            list = Collections.emptyList();
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> toMap(List<V> list, String field) {
        if (isEmpty(list)) {
            return null;
        }
        Map<K, V> map = new HashMap<K, V>();
        for (V t : list) {
            K fieldValue = (K) ReflectionUtils.getFieldValue(t, field);
            if (fieldValue == null) {
                continue;
            }
            map.put(fieldValue, t);
        }
        return map;
    }
}
