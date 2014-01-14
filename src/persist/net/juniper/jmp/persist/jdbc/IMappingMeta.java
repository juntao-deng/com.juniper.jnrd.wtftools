package net.juniper.jmp.persist.jdbc;

import java.io.Serializable;
import java.util.Map;

public interface IMappingMeta extends Serializable {
   
    public abstract String getPrimaryKey();

    public abstract String getTableName();

    public abstract FieldMeta[] getEntityFields();
    
    public abstract Map<String, Integer> getColumnMap();
    
    public abstract String[] getValidFields();

	public abstract String[] getValidFieldsWithoutPk();
}