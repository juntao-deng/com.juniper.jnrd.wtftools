package net.juniper.jmp.persist.utils;

import java.util.Map;

import net.juniper.jmp.persist.jdbc.IMappingMeta;

import org.apache.commons.beanutils.PropertyUtils;
import org.jboss.resteasy.logging.Logger;

public class PersistenceHelper {
	private static Logger logger = Logger.getLogger(PersistenceHelper.class);
	public static String getTableName(Object obj) {
		return getTableName(obj.getClass());
	}
	
	public static String getTableName(Class<?> c){
		return null;
	}
	
	public static IMappingMeta getMappingMeta(Object obj){
		return getMappingMeta(obj.getClass());
	}
	
	public static IMappingMeta getMappingMeta(Class<?> c){
		return null;
	}

	public static String getPkField(Object entity) {
		return null;
	}
	
	public static String getPkField(Class<?> entity) {
		return null;
	}

	public static String[] getInsertValidNames(Object entity) {
		IMappingMeta mapping = getMappingMeta(entity);
		String[] fields = mapping.getValidFields();
		return fields;
	}
	
	public static String[] getUpdateValidNames(Object entity) {
		IMappingMeta mapping = getMappingMeta(entity);
		String[] fields = mapping.getValidFieldsWithoutPk();
		return fields;
	}
	
	public static Object getEntityValue(Object entity, String field){
		try {
			return PropertyUtils.getProperty(entity, field);
		} 
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static Object getPrimaryKey(Object entity) {
		return getEntityValue(entity, getPkField(entity));
	}

	public static String[] getValidNames(Object entity) {
		return null;
	}

	public static Map<String, Integer> getColumnTypes(Object entity) {
		return getColumnTypes(entity.getClass());
	}

	public static Map<String, Integer> getColumnTypes(Class<?> entity){
		return getMappingMeta(entity).getColumnMap();
	}
}
