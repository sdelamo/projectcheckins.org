// Copyright 2024 Object Computing, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

//     http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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
