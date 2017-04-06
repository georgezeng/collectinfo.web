package com.collectinfo.util;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonUtil {
	public static Map<String, Object> parseToMap(String json) {
		return parseToMap(json, true);
	}

	public static Map<String, Object> parseToMap(String json, boolean failOnIgnore) {
		try {
			return createMapper(failOnIgnore).readValue(json, new TypeReference<HashMap<String, Object>>() {
			});
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static <T> T parseToObject(String json, Class<T> clazz, boolean failOnIgnore) {
		try {
			return createMapper(failOnIgnore).readValue(json, clazz);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static ObjectMapper createMapper(boolean failOnIgnore) {
		return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, failOnIgnore);
	}
}
