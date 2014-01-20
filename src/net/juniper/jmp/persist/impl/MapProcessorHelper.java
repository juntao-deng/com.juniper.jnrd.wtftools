package net.juniper.jmp.persist.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.juniper.jmp.persist.exp.JmpDbException;

public class MapProcessorHelper extends BeanProcessorHelper {
	public Map<String, Object> toMap(ResultSet rs) throws JmpDbException{
		if(rs == null)
			return null;
		try{
			if(rs.next()){
				ResultSetMetaData metaData = rs.getMetaData();
				int cols = metaData.getColumnCount();
				Map<String, Object> rsValues = new HashMap<String, Object>();
				for (int i = 1; i <= cols; i++) {
					Object value = getColumnValue(metaData.getColumnType(i), rs, i);
					rsValues.put(metaData.getColumnName(i).toLowerCase(), value);
				}
				return rsValues;
			}
			else
				return null;
		}
		catch(SQLException e){
			throw new JmpDbException(e.getMessage(), e);
		}
	}
}
