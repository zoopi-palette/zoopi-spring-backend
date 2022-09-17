package com.zoopi.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FunctionalUtils {

	public static <T, R> List<R> mapFrom(List<T> list, Function<T, R> func) {
		return list.stream().map(func).collect(Collectors.toList());
	}

	public static <T> List<T> filter(List<T> list, Predicate<T> func) {
		return list.stream().filter(func).collect(Collectors.toList());
	}

	public static <T, K, V> Map<K, V> associateFrom(List<T> list, Function<T, K> key, Function<T, V> value) {
		return list.stream().collect(HashMap::new, (map, t) -> map.put(key.apply(t), value.apply(t)), HashMap::putAll);
	}

}

