package com.collectinfo.util;

import java.util.Collection;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.collectinfo.exception.BusinessException;

public class AssertUtil extends Assert {
	public static void isNotEmpty(String value, String message) {
		if (StringUtils.isEmpty(value)) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void isNotEmptyForBusiness(String value, String message) {
		if (StringUtils.isEmpty(value)) {
			throw new BusinessException(message);
		}
	}
	
	public static void isNotEmpty(Object[] arr, String message) {
		if (arr == null || arr.length == 0) {
			throw new IllegalArgumentException(message);
		}
	}
	
	public static void isNotEmptyForBusiness(Object[] arr, String message) {
		if (arr == null || arr.length == 0) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void isNotEmpty(Collection<?> collection, String message) {
		if (collection == null || collection.isEmpty()) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void isNotEmptyForBusiness(Collection<?> collection, String message) {
		if (collection == null || collection.isEmpty()) {
			throw new BusinessException(message);
		}
	}

	public static void isTrueForBusiness(boolean expression, String message) {
		if (!expression) {
			throw new BusinessException(message);
		}
	}

	public static void isEqualsForBusiness(Object o1, Object o2, String message) {
		boolean isEqual = false;
		if (o1 == o2) {
			isEqual = true;
		} else if (o1 != null && o2 != null) {
			isEqual = o1.equals(o2);
		}
		if (!isEqual) {
			throw new BusinessException(message);
		}
	}

	public static void isNotEqualsForBusiness(Object o1, Object o2, String message) {
		boolean isEqual = false;
		if (o1 == o2) {
			isEqual = true;
		} else if (o1 != null && o2 != null) {
			isEqual = o1.equals(o2);
		}
		if (isEqual) {
			throw new BusinessException(message);
		}
	}

	public static void isEquals(Object o1, Object o2, String message) {
		boolean isEqual = false;
		if (o1 == o2) {
			isEqual = true;
		} else if (o1 != null && o2 != null) {
			isEqual = o1.equals(o2);
		}
		if (!isEqual) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void notNullForBusiness(Object value, String message) {
		if (value == null) {
			throw new BusinessException(message);
		}
	}

	public static void isNullForBusiness(Object value, String message) {
		if (value != null) {
			throw new BusinessException(message);
		}
	}
}
