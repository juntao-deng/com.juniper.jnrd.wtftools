package net.juniper.jmp.persist.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;

import net.juniper.jmp.persist.SQLParameter;
import net.juniper.jmp.persist.jdbc.BlobParamType;
import net.juniper.jmp.persist.jdbc.ClobParamType;
import net.juniper.jmp.persist.jdbc.NullParamType;


public class DBUtil {
	public static void setStatementParameter(PreparedStatement statement, SQLParameter params) throws SQLException {
		if (statement == null || params == null)
			throw new IllegalArgumentException("不能传入空的SQLParameter!");
		for (int i = 0; i < params.getCountParams(); i++) {
			Object param = params.get(i);
			if (param == null)
				throw new IllegalArgumentException("SQLParameter中的参数值不能为空");
			if (param instanceof NullParamType) {
				statement.setNull(i + 1, ((NullParamType) param).getType());
			} else if (param instanceof Integer) {
				statement.setInt(i + 1, ((Integer) param).intValue());
			} else if (param instanceof Short) {
				statement.setShort(i + 1, ((Short) param).shortValue());
			} else if (param instanceof Timestamp) {
				statement.setTimestamp(i + 1, (Timestamp) param);
			} else if (param instanceof Time) {
				statement.setTime(i + 1, (Time) param);
			} else if (param instanceof String) {
				String s = (String) param;
				statement.setString(i + 1, s);
			} 
			else if (param instanceof Double) {
				statement.setDouble(i + 1, ((Double) param).doubleValue());
			}
			else if (param instanceof Float) {
				statement.setFloat(i + 1, ((Float) param).floatValue());
			} else if (param instanceof Long) {
				statement.setLong(i + 1, ((Long) param).longValue());
			} else if (param instanceof Boolean) {
				statement.setBoolean(i + 1, ((Boolean) param).booleanValue());
			} else if (param instanceof java.sql.Date) {
				statement.setDate(i + 1, (java.sql.Date) param);
			}
			else if (param instanceof BlobParamType) {
				statement.setBytes(i + 1, ((BlobParamType) param).getBytes());
			}
			else if (param instanceof ClobParamType) {
				ClobParamType clob = (ClobParamType) param;
				statement.setCharacterStream(i + 1, clob.getReader(), clob
						.getLength());
			} else {
				statement.setObject(i + 1, param);
			}
		}
	}


	public static void closeConnection(Connection con) {
		try {
			if (con != null) {
				con.close();
				con = null;
			}
		} 
		catch (SQLException e) {
		}
	}

	public static void closeStmt(Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} 
		catch (SQLException e) {
		}
	}

	public static void closeRs(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
		} 
		catch (SQLException e) {
		}
	}

}
