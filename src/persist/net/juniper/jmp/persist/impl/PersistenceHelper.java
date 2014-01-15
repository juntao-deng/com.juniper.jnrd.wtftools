package net.juniper.jmp.persist.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.juniper.jmp.entity.JmpEntity;
import net.juniper.jmp.persist.jdbc.IMappingMeta;
import net.juniper.jmp.persist.utils.SqlLogger;

import org.apache.commons.beanutils.PropertyUtils;
import org.jboss.resteasy.logging.Logger;

public class PersistenceHelper {
	private static Logger logger = Logger.getLogger(PersistenceHelper.class);
	private static Map<Class<?>, IMappingMeta> metaMap = new ConcurrentHashMap<Class<?>, IMappingMeta>();
	public static String getTableName(Object obj) {
		return getTableName(obj.getClass());
	}
	
	public static String getTableName(Class<?> c){
		IMappingMeta mm = getMappingMeta(c);
		return mm == null ? null : mm.getTableName();
	}
	
	public static IMappingMeta getMappingMeta(Object obj){
		IMappingMeta mm = getMappingMeta(obj.getClass());
		if(mm == NullMappingMeta.INSTANCE)
			return null;
		return mm;
	}
	
	public static IMappingMeta getMappingMeta(Class<?> c){
		IMappingMeta meta = metaMap.get(c);
		if(meta == null){
			synchronized(metaMap){
				meta = metaMap.get(c);
				if(meta == null){
					try{
						meta = buildMappingMeta(c);
						metaMap.put(c, meta);
					}
					catch(Throwable e){
						SqlLogger.error(e.getMessage(), e);
						metaMap.put(c, NullMappingMeta.INSTANCE);
					}
				}
			}
		}
		return meta;
	}
	
	@SuppressWarnings("unchecked")
	private static IMappingMeta buildMappingMeta(Class<?> clazz){
		if(JmpEntity.class == clazz){
			return new JmpEntityMappingMeta((Class<JmpEntity>) clazz);
		}
		else
			return new JpaEntityMappingMeta((Class<Object>) clazz);
	}

	public static String getPkField(Object entity) {
		return getPkField(entity.getClass());
	}
	
	public static String getPkField(Class<?> entity) {
		return getMappingMeta(entity).getPrimaryKey();
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
		return getMappingMeta(entity).getValidFields();
	}

	public static Map<String, Integer> getColumnTypes(Object entity) {
		return getColumnTypes(entity.getClass());
	}

	public static Map<String, Integer> getColumnTypes(Class<?> entity){
		return getMappingMeta(entity).getColumnMap();
	}
}
