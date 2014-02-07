package net.juniper.jmp.persist.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Map;

import net.juniper.jmp.persist.SQLParameter;
import net.juniper.jmp.persist.impl.PersistenceHelper;
import net.juniper.jmp.persist.jdbc.trans.SqlTranslator;
import net.juniper.jmp.wtf.ctx.Sort;

public class SQLHelper {
	public static String getInsertSQL(String table, String names[]) {
		StringBuffer buffer = new StringBuffer("INSERT INTO " + table + " (");
		for (int i = 0; i < names.length; i++) {
			buffer.append(names[i] + ",");
		}
		buffer.setLength(buffer.length() - 1);
		buffer.append(") VALUES (");
		for (int i = 0; i < names.length; i++) {
			buffer.append("?,");
		}
		buffer.setLength(buffer.length() - 1);
		buffer.append(")");
		return buffer.toString();
	}

	public static String getUpdateSQL(String tableName, String[] names,
			String pkName) {
		StringBuffer sql = new StringBuffer("UPDATE " + tableName + " SET  ");
		for (int i = 0; i < names.length; i++) {
			sql.append(names[i] + "=?,");
		}
		sql.setLength(sql.length() - 1);
		sql.append(" WHERE ").append(pkName).append("=?");
		return sql.toString();
	}

	public static String getUpdateSQL(String tableName, String[] names) {
		StringBuffer sql = new StringBuffer("UPDATE " + tableName + " SET  ");
		for (int i = 0; i < names.length; i++) {
			sql.append(names[i] + "=?,");
		}
		sql.setLength(sql.length() - 1);
		return sql.toString();
	}

	public static String getDeleteByPKSQL(String tableName, String pkName) {
		return "DELETE FROM " + tableName + " WHERE " + pkName + "=?";
	}

	public static String getDeleteSQL(String tableName, String[] names) {
		StringBuffer sql = new StringBuffer("DELETE FROM " + tableName + " WHERE ");
		for (int i = 0; i < names.length; i++) {
			sql.append(names[i] + "=? AND ");
		}
		sql.setLength(sql.length() - 4);
		return sql.toString();
	}

	public static String getSelectSQL(String tableName, String[] names,
			boolean isAnd, String[] fields) {
		StringBuffer sql = new StringBuffer();
		if (fields == null)
			sql.append("SELECT * FROM " + tableName);
		else {

			sql.append("SELECT ");
			for (int i = 0; i < fields.length; i++) {
				sql.append(fields[i] + ",");

			}
			sql.setLength(sql.length() - 1);
			sql.append(" FROM " + tableName);
		}
		String append = "AND ";
		if (!isAnd)
			append = "OR ";
		if (names == null || names.length == 0)
			return sql.toString();
		sql.append(" WHERE ");
		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			sql.append(name + "=? ");
			if (i != names.length - 1)
				sql.append(append);
		}
		return sql.toString();

	}

	public static String getSelectSQL(String tableName, String[] fields) {
		StringBuffer sql = new StringBuffer();
		if (fields == null)
			sql.append("SELECT * FROM " + tableName);
		else {

			sql.append("SELECT ");
			for (int i = 0; i < fields.length; i++) {
				sql.append(fields[i] + ",");

			}
			sql.setLength(sql.length() - 1);
			sql.append(" FROM " + tableName);
		}

		return sql.toString();

	}

	public static String getSelectSQL(String tableName, String[] fields,
			String[] names) {
		StringBuffer sql = new StringBuffer();
		if (fields == null)
			sql.append("SELECT * FROM " + tableName);
		else {

			sql.append("SELECT ");
			for (int i = 0; i < fields.length; i++) {
				sql.append(fields[i] + ",");

			}
			sql.setLength(sql.length() - 1);
			sql.append(" FROM " + tableName);
		}
		String append = "AND ";

		if (names == null || names.length == 0)
			return sql.toString();
		sql.append(" WHERE ");
		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			sql.append(name + "=? ");
			if (i != names.length - 1)
				sql.append(append);
		}
		return sql.toString();

	}
	
	public static SQLParameter getSQLParam(String dataSource, Object entity, String names[]) {
		Map<String, Integer> types = PersistenceHelper.getColumnTypes(dataSource, entity);
		SQLParameter params = new SQLParameter();
		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			int type = types.get(name.toLowerCase());
			Object value = PersistenceHelper.getEntityValue(entity, name);
			if (value == null) {
				params.addNullParam(type);
				continue;
			}
			if (type == Types.BLOB || type == Types.LONGVARBINARY || type == Types.VARBINARY || type == Types.BINARY) {
				params.addBlobParam(value);
				continue;
			}
			if (type == Types.CLOB || type == Types.LONGVARCHAR) {
				params.addClobParam(String.valueOf(value));
				continue;
			}
			params.addParam(value);

		}
		return params;
	}
	
	public static String buildSql(String dataSource, Class<?> clazz, String condition, String[] fields, Sort sort) {
		String pkField = PersistenceHelper.getPkField(dataSource, clazz);
		String tableName = PersistenceHelper.getTableName(dataSource, clazz);
		boolean hasPKField = false;
		StringBuffer buffer = new StringBuffer();
		if (fields == null)
			buffer.append("SELECT * FROM ").append(tableName);
		else {
			buffer.append("SELECT ");
			for (int i = 0; i < fields.length; i++) {
				buffer.append(fields[i]).append(",");
				if (fields[i].equalsIgnoreCase(pkField))
					hasPKField = true;
			}
			if (!hasPKField)
				buffer.append(pkField).append(",");
			buffer.setLength(buffer.length() - 1);
			buffer.append(" FROM ").append(tableName);
		}
		if (condition != null && condition.length() != 0) {
			if (condition.toUpperCase().trim().startsWith("ORDER "))
				buffer.append(" ").append(condition);
			else
				buffer.append(" WHERE ").append(condition);
		}

		return buffer.toString();
	}
	
	public static String translate(String sql, CrossDBConnection conn) throws SQLException {
		if (sql == null || sql.equals(""))
			return sql;
		
		if (!conn.isSQLTranslatorEnabled()) {
			return sql;
		}
		
		SQLLRUCache cache = SQLCacheProvider.getInstance().getCache(conn.getDsName());
		String trans = (String) cache.getPreparedSQL(sql);
		if (trans == null) {
			trans = SqlTranslator.getInstance(conn.getDbType()).getResultSql(sql);
			cache.putPreparedSQL(sql, trans);
		}
		return trans;
	}
	
	
	public static void setStatementParameter(PreparedStatement statement, SQLParameter params) throws SQLException {
		int count = params.getCount();
		for (int i = 0; i < count; i++) {
			Object param = params.get(i);
			if (param instanceof NullParamType) {
				statement.setNull(i + 1, ((NullParamType) param).getType());
			} 
			else if (param instanceof Integer) {
				statement.setInt(i + 1, ((Integer) param).intValue());
			} 
			else if (param instanceof Short) {
				statement.setShort(i + 1, ((Short) param).shortValue());
			} 
			else if (param instanceof Timestamp) {
				statement.setTimestamp(i + 1, (Timestamp) param);
			} 
			else if (param instanceof Time) {
				statement.setTime(i + 1, (Time) param);
			} 
			else if (param instanceof String) {
				String s = (String) param;
				statement.setString(i + 1, s);
			} 
			else if (param instanceof Double) {
				statement.setDouble(i + 1, ((Double) param).doubleValue());
			}
			else if (param instanceof Float) {
				statement.setFloat(i + 1, ((Float) param).floatValue());
			} 
			else if (param instanceof Long) {
				statement.setLong(i + 1, ((Long) param).longValue());
			} 
			else if (param instanceof Boolean) {
				statement.setBoolean(i + 1, ((Boolean) param).booleanValue());
			} 
			else if (param instanceof java.sql.Date) {
				statement.setDate(i + 1, (java.sql.Date) param);
			}
			else if (param instanceof BlobParamType) {
				statement.setBytes(i + 1, ((BlobParamType) param).getBytes());
			}
			else if (param instanceof ClobParamType) {
				ClobParamType clob = (ClobParamType) param;
				statement.setCharacterStream(i + 1, clob.getReader());
			} 
			else {
				statement.setObject(i + 1, param);
			}
		}
	}
}
