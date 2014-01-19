package net.juniper.jmp.persist.jdbc;

import java.io.Serializable;
import java.util.Map;
/**
 * Mapping Data for Entity Object
 * @author juntaod
 *
 */
public interface IMappingMeta extends Serializable {
   
    public String getPrimaryKey();

    public String getTableName();
    
    public String getVersionName();

    public FieldMeta[] getEntityFields();
    
    public Map<String, Integer> getColumnMap();
    
    public String[] getValidFields();

	public String[] getValidFieldsWithoutPk();

	public FieldMeta getFieldMetaByColumn(String column);
	
	public Object getEntity();
}