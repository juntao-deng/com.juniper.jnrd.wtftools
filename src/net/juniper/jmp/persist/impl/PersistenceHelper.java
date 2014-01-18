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
	private static Map<String, Map<Class<?>, IMappingMeta>> dsMetaMap = new ConcurrentHashMap<String, Map<Class<?>, IMappingMeta>>(2);
	public static String getTableName(String dataSource, Object obj) {
		return getTableName(dataSource, obj.getClass());
	}
	
	public static String getTableName(String dataSource, Class<?> c){
		IMappingMeta mm = getMappingMeta(dataSource, c);
		return mm == null ? null : mm.getTableName();
	}
	
	public static IMappingMeta getMappingMeta(String dataSource, Object obj){
		IMappingMeta mm = getMappingMeta(dataSource, obj.getClass());
		if(mm == NullMappingMeta.INSTANCE)
			return null;
		return mm;
	}
	
	public static IMappingMeta getMappingMeta(String dataSource, Class<?> c){
		Map<Class<?>, IMappingMeta> metaMap = dsMetaMap.get(dataSource);
		if(metaMap == null){
			synchronized(dsMetaMap){
				metaMap = dsMetaMap.get(dataSource);
				if(metaMap == null){
					metaMap = new ConcurrentHashMap<Class<?>, IMappingMeta>();
					dsMetaMap.put(dataSource, metaMap);
				}
			}
		}
		
		IMappingMeta meta = metaMap.get(c);
		if(meta == null){
			synchronized(metaMap){
				meta = metaMap.get(c);
				if(meta == null){
					try{
						meta = buildMappingMeta(dataSource, c);
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
	private static IMappingMeta buildMappingMeta(String dataSource, Class<?> clazz){
		if(JmpEntity.class == clazz){
			return new JmpEntityMappingMeta(dataSource, (Class<JmpEntity>) clazz);
		}
		else
			return new JpaEntityMappingMeta(dataSource, (Class<Object>) clazz);
	}

	public static String getPkField(String dataSource, Object entity) {
		return getPkField(dataSource, entity.getClass());
	}
	
	public static String getPkField(String dataSource, Class<?> entity) {
		return getMappingMeta(dataSource, entity).getPrimaryKey();
	}

	public static String[] getInsertValidNames(String dataSource, Object entity) {
		IMappingMeta mapping = getMappingMeta(dataSource, entity);
		String[] fields = mapping.getValidFields();
		return fields;
	}
	
	public static String[] getUpdateValidNames(String dataSource, Object entity) {
		IMappingMeta mapping = getMappingMeta(dataSource, entity);
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

	public static Object getPrimaryKey(String dataSource, Object entity) {
		return getEntityValue(entity, getPkField(dataSource, entity));
	}

	public static String[] getValidNames(String dataSource, Object entity) {
		return getMappingMeta(dataSource, entity).getValidFields();
	}

	public static Map<String, Integer> getColumnTypes(String dataSource, Object entity) {
		return getColumnTypes(dataSource, entity.getClass());
	}

	public static Map<String, Integer> getColumnTypes(String dataSource, Class<?> entity){
		return getMappingMeta(dataSource, entity).getColumnMap();
	}
}
