package net.juniper.jmp.persist.impl;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.juniper.jmp.core.util.JmpClassUtil;
import net.juniper.jmp.persist.exp.JmpDbException;
import net.juniper.jmp.persist.exp.JmpDbRuntimeException;
import net.juniper.jmp.persist.jdbc.CrossDBResultSet;
import net.juniper.jmp.persist.jdbc.FieldMeta;
import net.juniper.jmp.persist.jdbc.IMappingMeta;
import net.juniper.jmp.persist.utils.SqlLogger;

import org.apache.commons.beanutils.Converter;

public class BeanListProcessorHelper extends BeanProcessorHelper {
	public <T>List<T> toBeanList(CrossDBResultSet resultSet, Class<T> type) throws JmpDbException {
		if (resultSet == null)
			throw new JmpDbException("result set is null");
		return toBeanListInner(resultSet, type);
	}
	
	private <T>List<T> toBeanListInner(CrossDBResultSet resultSet, Class<T> type) throws JmpDbException {
		String dataSource = resultSet.getDsName();
		IMappingMeta mm = PersistenceHelper.getMappingMeta(dataSource, type);
		if(mm == null)
			throw new JmpDbRuntimeException("can not find mapping meta for type:" + type.getName() + ", please check type is a valid entity type");
		List<T> result = new ArrayList<T>();
		try{
			FieldMeta[] fms = getEntityFieldsByRs(mm, resultSet);
			while (resultSet.next()) {
				T target = JmpClassUtil.newInstance(type);
				for (int i = 0; i < fms.length; i++) {
					FieldMeta fm = fms[i];
					if(fm == null){
						continue;
					}
					Object value = getColumnValue(fm.getColumnType(), resultSet, i + 1);
					if (value == null)
						continue;
					Method invoke = fm.getWriteRef().get();
					if (invoke == null) {
						throw new JmpDbRuntimeException("can not find mapping method for field:" + fm.getField());
					}
					Converter converter = fm.getConverter();
					if (converter != null)
						value = converter.convert(invoke.getParameterTypes()[0], value);
					setBeanValue(invoke, target, value);
				}
				result.add(target);
			}
		}
		catch(SQLException e){
			throw new JmpDbException(e.getMessage(), e);
		}
		return result;
	}
	
	private FieldMeta[] getEntityFieldsByRs(IMappingMeta mm, ResultSet resultSet) throws SQLException {
		ResultSetMetaData metaData = resultSet.getMetaData();
		int cols = metaData.getColumnCount();
		FieldMeta[] fields = new FieldMeta[cols];
		for (int i = 0; i < cols; i++) {
			String column = metaData.getColumnName(i + 1).toLowerCase();
			FieldMeta fm = mm.getFieldMetaByColumn(column);
			if(fm == null){
				SqlLogger.warn("can not find mapping filed for column : " + column);
			}
			fields[i] = fm;
		}
		return fields;
	}
	
	private void setBeanValue(Method m, Object target, Object value){
		try {
			m.invoke(target, value);
		} 
		catch (Exception e) {
			SqlLogger.error(e.getMessage(), e);
		}
	}
}
