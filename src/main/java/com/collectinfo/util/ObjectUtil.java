package com.collectinfo.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObjectUtil {
	public static boolean equals(Object o1, Object o2) {
		return o1 == o2 || (o1 != null && o1.equals(o2)) || (o2 != null && o2.equals(o1));
	}

	public static List<Field> getAllDeclareFields(Class<?> clazz) {
		List<Field> fs = new ArrayList<Field>();
		Class<?> currentClass = clazz;
		while (!currentClass.equals(Object.class)) {
			Field[] fields = currentClass.getDeclaredFields();
			if (fields != null) {
				fs.addAll(Arrays.asList(fields));
			}
			currentClass = currentClass.getSuperclass();
		}
		return fs;
	}
}
