package net.juniper.jmp.persist.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.sql.DataSource;

import net.juniper.jmp.persist.datasource.DataSourceCenter;
import net.juniper.jmp.persist.exp.JmpDbRuntimeException;
import net.juniper.jmp.persist.jdbc.FieldMeta;

import com.sun.xml.internal.ws.util.StringUtils;

/**
 * Mapping meta from JPA Entity to Db table
 * @author juntaod
 *
 */
public final class JpaEntityMappingMeta extends MappingMeta<Object> {
	private static final long serialVersionUID = 8015429393959008969L;
	public JpaEntityMappingMeta(String dataSource, Class<Object> clazz) {
		super(dataSource, clazz);
	}

	@Override
	public void resolve(String dataSource, Class<Object> clazz) {
		Entity entity = clazz.getAnnotation(Entity.class);
		if(entity == null)
			throw new JmpDbRuntimeException("This class is not a jpa entity," + clazz.getName());
		
		String tableName = detectTableName(clazz);
		Map<String, Integer> typeMap = getTypeParam(dataSource, tableName);
		
		setTableName(tableName);
		
		Method[] methods = clazz.getDeclaredMethods();
		List<FieldMeta> fms = new ArrayList<FieldMeta>();
		for (int i = 0; i < methods.length; i++) {
			Method m = methods[i];
			String name = m.getName();
			FieldMeta fm = null;
			if(name.startsWith("get")){
				String simpleName = name.substring("get".length());
				simpleName = StringUtils.decapitalize(simpleName);
				fm = getFieldMeta(simpleName, methods, clazz);
			}
			else if(name.startsWith("is")){
				String simpleName = name.substring("is".length());
				simpleName = StringUtils.decapitalize(simpleName);
				fm = getFieldMeta(simpleName, methods, clazz);
			}
			else
				continue;
			if(fm != null){
				String column = fm.getColumn();
				Integer type = typeMap.get(column);
				if(type == null)
					continue;
				fm.setColumnType(type);
				fms.add(fm);
			}
		}
		
		
	}

	private Map<String, Integer> getTypeParam(String dsName, String table) {
		try {
			Map<String, Integer> typeMap = new HashMap<String, Integer>();
			DataSource ds = DataSourceCenter.getInstance().getDataSource("");
			DatabaseMetaData md = ds.getConnection().getMetaData();
			ResultSet rsColumns = md.getColumns(null, getSchema(md), table.toUpperCase(), "%");
			while (rsColumns.next()) {
				String columnName = rsColumns.getString("COLUMN_NAME").toUpperCase();
				int columnType = rsColumns.getShort("DATA_TYPE");
				typeMap.put(columnName, columnType);
			}
			return typeMap;
		} 
		catch (SQLException e) {
			throw new JmpDbRuntimeException("error while getting metadata", e);
		}
	}
	
	private String getSchema(DatabaseMetaData md) {
		try {
			String schema = md.getUserName();
			return schema.toUpperCase();
		} 
		catch (SQLException e) {
			throw new JmpDbRuntimeException("error while getting schema", e);
		}
	}
	
	private FieldMeta getFieldMeta(String simpleName, Method[] methods, Class<Object> clazz) {
		String name = StringUtils.capitalize(simpleName);
		String methodName = "set" + name;
		for(int i = 0; i < methods.length; i ++){
			Method method = methods[i];
			if(method.getName().equals(methodName)){
				return buildFieldMeta(simpleName, method, clazz);
			}
		}
		return null;
	}

	private FieldMeta buildFieldMeta(String simpleName, Method method, Class<Object> clazz) {
		Field f = null;
		try {
			f = clazz.getDeclaredField(simpleName);
		} 
		catch (Exception e) {
		}
		
		Column columnAnnotation = getColumnAnnotation(f, method);
		String column = null;
		if(columnAnnotation != null)
			column = columnAnnotation.name();
		if(column == null)
			column = simpleName;
		boolean nullable = false;
		if(columnAnnotation != null)
			nullable = columnAnnotation.nullable();
		
		FieldMeta fm = new FieldMeta();
		fm.setColumn(column);
		fm.setFieldType(method.getParameterTypes()[0].getClass());
		fm.setField(simpleName);
		fm.setNullable(nullable);
		return fm;
	}
	
	private Column getColumnAnnotation(Field f, Method method) {
		Column columnAnnotation = null;
		if(f != null)
			columnAnnotation = f.getAnnotation(Column.class);
		if(columnAnnotation == null)
			columnAnnotation = method.getAnnotation(Column.class);
		return columnAnnotation;
	}

	/**
	 * try to find db table name
	 * @param clazz
	 * @return
	 */
	private String detectTableName(Class<Object> clazz) {
		Table table = clazz.getAnnotation(Table.class);
		if(table != null){
			return table.name();
		}
		Entity entity = clazz.getAnnotation(Entity.class);
		String name = entity.name();
		if(name != null && !name.equals(""))
			return name;
		return clazz.getSimpleName();
	}
}
