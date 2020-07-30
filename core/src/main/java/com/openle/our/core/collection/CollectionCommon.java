/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openle.our.core.collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author xiaodong
 */
public class CollectionCommon {

    //  解决List<子类>与List<父类>转换时报unchecked警告
    public static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

    public static Object listOne(List<Map<String, Object>> list) {
        if (list != null && list.size() > 0) {
            Map<String, Object> map = list.get(0);
            if (map != null) {
                Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    Object val = entry.getValue();
                    return val;
                }
            }
        }
        return null;
    }

    public List<Map<String, Object>> sortedList(List<Map<String, Object>> events, String sortKey) {
        Map<String, Map<String, Object>> rMap = new HashMap<>();
        events.forEach((map) -> {
            map.keySet().stream().filter(key -> key.equals(sortKey)).map(key -> map.get(key).toString()).forEachOrdered((k) -> {
                rMap.put(k, map);
            });
        });
        events.clear();
        rMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEachOrdered(t -> {
            events.add(t.getValue());
        });
        return events;
    }

    // 有序去重
    public static List<String> removeDuplicateWithOrder(List<String> list) {
        Set<String> set = new HashSet<>();
        List<String> newList = new ArrayList<>();
        for (Iterator<String> iter = list.iterator(); iter.hasNext();) {
            String element = iter.next().trim();
            if (set.add(element.toLowerCase())) {
                newList.add(element);
            }
        }
        return newList;
    }

    // 无序去重
    public static List<String> removeDuplicate(List<String> list) {
        HashSet<String> hashSet = new HashSet<>(list);
        list.clear();
        list.addAll(hashSet);

        return list;
    }

}
