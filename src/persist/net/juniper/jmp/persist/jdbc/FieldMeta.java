package net.juniper.jmp.persist.jdbc;

import java.lang.ref.Reference;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.Converter;

public class FieldMeta {
	private String field;
	private String column;
	private boolean nullable;
	private Integer columnType;
	private Reference<Method> readRef;
	private Reference<Method> writeRef;
	private Converter converter;
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public boolean isNullable() {
		return nullable;
	}
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}
	public Reference<Method> getReadRef() {
		return readRef;
	}
	public void setReadRef(Reference<Method> readRef) {
		this.readRef = readRef;
	}
	public Reference<Method> getWriteRef() {
		return writeRef;
	}
	public void setWriteRef(Reference<Method> writeRef) {
		this.writeRef = writeRef;
	}
	public Integer getColumnType() {
		return columnType;
	}
	public void setColumnType(Integer columnType) {
		this.columnType = columnType;
	}
	public Converter getConverter() {
		return converter;
	}
	public void setConverter(Converter converter) {
		this.converter = converter;
	}
}
