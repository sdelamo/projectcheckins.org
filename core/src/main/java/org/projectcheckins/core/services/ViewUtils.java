package org.projectcheckins.core.services;

import java.util.Comparator;
import java.util.Map;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;

public class ViewUtils {

    public static <T, K, V> List<T> encapsulate(List<? extends V> list,
                                                Function<V, K> groupBy,
                                                Comparator<? super K> sortBy,
                                                BiFunction<K, List<V>, T> creator) {
        final Map<K, List<V>> map = list.stream().collect(groupingBy(groupBy));
        return map.keySet().stream().sorted(sortBy).map(k -> creator.apply(k, map.get(k))).toList();
    }
}
