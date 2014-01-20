package net.juniper.jmp.core.util;

import net.juniper.jmp.persist.exp.JmpDbRuntimeException;

public class BeanUtils {
	public static void copyPropertiesFromMoToEntity(Object source, Object target){
		try {
			org.apache.commons.beanutils.BeanUtils.copyProperties(target, source);
		} 
		catch (Exception e) {
			throw new JmpDbRuntimeException(e.getMessage(), e);
		}
	}
	public static void copyPropertiesFromEntityToMo(Object source, Object target){
		try {
			org.apache.commons.beanutils.BeanUtils.copyProperties(target, source);
		} 
		catch (Exception e) {
			throw new JmpDbRuntimeException(e.getMessage(), e);
		}
	}
}
