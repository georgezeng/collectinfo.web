package com.collectinfo.util;

import java.math.BigDecimal;
import java.math.BigInteger;

public class DatabaseUtil {
	public static long getCount(Object count) {
		if (count != null) {
			if (BigInteger.class.isAssignableFrom(count.getClass())) {
				return ((BigInteger) count).longValue();
			} else if (long.class.isAssignableFrom(count.getClass()) || Long.class.isAssignableFrom(count.getClass())) {
				return (Long) count;
			} else if (int.class.isAssignableFrom(count.getClass()) || Integer.class.isAssignableFrom(count.getClass())) {
				return (Integer) count;
			} else if (BigDecimal.class.isAssignableFrom(count.getClass())) {
				return ((BigDecimal) count).longValue();
			}
		}
		return 0;
	}

}
