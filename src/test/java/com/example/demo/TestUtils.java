package com.example.demo;

import org.mockito.internal.util.reflection.FieldSetter;

public class TestUtils {
	/**
	 * Reflects a field on the target with the provided value
	 * @param target The target instance
	 * @param field The name of the field in the instance
	 * @param value The value to inject into the instance
	 * @throws NoSuchFieldException When no field is found with the provided field name
	 */
	public static void mockField(Object target, String field, Object value) throws NoSuchFieldException {
		FieldSetter.setField(target, target.getClass().getDeclaredField(field), value);
	}
}
