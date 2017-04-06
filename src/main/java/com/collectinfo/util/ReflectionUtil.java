package com.collectinfo.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.util.ReflectionUtils;

public class ReflectionUtil extends ReflectionUtils {
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getActualTypeArgumentClass(Class<?> clazz, int typeOrder) {
		Class<T> result = null;
		while (!Object.class.equals(clazz.getSuperclass())) {
			try {
				Type type = clazz.getGenericSuperclass();
				if (type instanceof ParameterizedType) {
					ParameterizedType pType = (ParameterizedType) type;
					result = (Class<T>) pType.getActualTypeArguments()[typeOrder];
				}
				break;
			} catch (Exception e) {
				clazz = clazz.getSuperclass();
			}
		}
		return result;
	}
}
