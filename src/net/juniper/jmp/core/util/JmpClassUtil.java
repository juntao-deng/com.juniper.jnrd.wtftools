package net.juniper.jmp.core.util;

import net.juniper.jmp.exception.JMPRuntimeException;
import net.juniper.jmp.persist.datasource.DataSourceProvider;

public class JmpClassUtil {
	public static <T>T newInstance(Class<T> c){
		try {
			return c.newInstance();
		} 
		catch (Exception e) {
			throw new JMPRuntimeException("Cannot create " + c.getName() + ":" + e.getMessage(), e);
		}
	}

	public static DataSourceProvider newInstance(String providerClazz) {
		try {
			Class<DataSourceProvider> c = (Class<DataSourceProvider>) Class.forName(providerClazz);
			return newInstance(c);
		} catch (ClassNotFoundException e) {
			throw new JMPRuntimeException("Cannot create " + providerClazz + ":" + e.getMessage(), e);
		}
	}
}
