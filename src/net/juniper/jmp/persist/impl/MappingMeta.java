package net.juniper.jmp.persist.impl;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

import net.juniper.jmp.persist.jdbc.FieldMeta;
import net.juniper.jmp.persist.jdbc.IMappingMeta;
/**
 * Mapping information from one Entity Object to one Db table
 * @author juntaod
 *
 * @param <T>
 */
public abstract class MappingMeta<T> implements IMappingMeta {
	private static final long serialVersionUID = -3192362496510980507L;
	private String primaryKey;
	private String tableName;
	private String versionName;
	private Map<String, FieldMeta> columnFieldMap;
	private Map<String, FieldMeta> fieldFieldMap;
	private List<FieldMeta> entityFields;
	private String[] validFieldsWithoutPk;
	private String[] valieFields;
	private Reference<Class<T>> entityRef;
	public MappingMeta(String dataSource, Class<T> entity){
		if(entity != null){
			entityRef = new WeakReference<Class<T>>(entity);
			resolve(dataSource, entity);
		}
	}
	
	@Override
	public Object getEntity() {
		return entityRef.get();
	}
	
	@Override
	public String getPrimaryKey() {
		return primaryKey;
	}

	@Override
	public String getTableName() {
		return tableName;
	}

	@Override
	public FieldMeta[] getEntityFields() {
		return null;
	}

	@Override
	public Map<String, Integer> getColumnMap() {
		return null;
	}

	@Override
	public String[] getValidFields() {
		return valieFields;
	}

	@Override
	public String[] getValidFieldsWithoutPk() {
		return validFieldsWithoutPk;
	}

	@Override
	public FieldMeta getFieldMetaByColumn(String column) {
		return null;
	}
	
	protected abstract void resolve(String dataSource, Class<T> entity);

	public Map<String, FieldMeta> getColumnFieldMap() {
		return columnFieldMap;
	}

	public void setColumnFieldMap(Map<String, FieldMeta> columnFieldMap) {
		this.columnFieldMap = columnFieldMap;
	}

	public Map<String, FieldMeta> getFieldFieldMap() {
		return fieldFieldMap;
	}

	public void setFieldFieldMap(Map<String, FieldMeta> fieldFieldMap) {
		this.fieldFieldMap = fieldFieldMap;
	}

	public String[] getValieFields() {
		return valieFields;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setEntityFields(List<FieldMeta> entityFields) {
		this.entityFields = entityFields;
	}

	public void setValidFieldsWithoutPk(String[] validFieldsWithoutPk) {
		this.validFieldsWithoutPk = validFieldsWithoutPk;
	}

	@Override
	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
}
