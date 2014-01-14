package net.juniper.jmp.persist.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Types;
import java.util.Calendar;

import net.juniper.jmp.persist.utils.DbExceptionHelper;

public class CrossDBPreparedStatement extends CrossDBStatement implements PreparedStatement {
	int id = counter++;
	private String sqlTemplate;

	public CrossDBPreparedStatement(java.sql.PreparedStatement dummy, CrossDBConnection con, String sql, int dbtype, LRUCache cache, String dataSource) {
		super(dummy, con, dbtype, cache, dataSource);
		sqlTemplate = sql;

	}

	public CrossDBPreparedStatement(java.sql.Statement dummy, CrossDBConnection con, String sql, int dbtype, LRUCache cache, String dataSource) {
		super(dummy, con, dbtype, cache, dataSource);
		sqlTemplate = sql;
	}

	public void addBatch() throws SQLException {
		try {
			((java.sql.PreparedStatement) passthru).addBatch();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}

	}

	public void clearParameters() throws SQLException {
		try {
			((java.sql.PreparedStatement) passthru).clearParameters();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public boolean execute() throws SQLException {
		try {
			return ((PreparedStatement) passthru).execute();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public ResultSet executeQuery() throws SQLException {
		try {
			CrossDBResultSet r = new CrossDBResultSet(((java.sql.PreparedStatement) passthru).executeQuery(), this);
			r.setMaxRows(maxRows);
			registerResultSet(r);
			return r;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public int executeUpdate() throws SQLException {
		try {
			int ret = ((java.sql.PreparedStatement) passthru).executeUpdate();
			return ret;
		} 
		catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public java.sql.ResultSetMetaData getMetaData() throws SQLException {
		return ((java.sql.PreparedStatement) passthru).getMetaData();
	}

	public void setArray(int i, java.sql.Array x) throws SQLException {
		try {
			((java.sql.PreparedStatement) passthru).setArray(i, x);
		} 
		catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void setAsciiStream(int parameterIndex, java.io.InputStream x, int length) throws SQLException {
		try {
			if (x == null) {
				setNull(parameterIndex, Types.CLOB);
			} else {
				adapter.setAsciiStream(this, parameterIndex, x, length);
			}
		} catch (SQLException e) {
			
			throw trans.getSqlException(e);
		}
	}

	public void setBigDecimal(int parameterIndex, java.math.BigDecimal x) throws SQLException {
		
		try {
			((java.sql.PreparedStatement) passthru).setBigDecimal(parameterIndex, x);
		} catch (SQLException e) {
			
			throw trans.getSqlException(e);
		}
	}

	public void setBinaryStream(int parameterIndex, java.io.InputStream x, int length) throws SQLException {
		
		try {

			if (x == null) {
				setNull(parameterIndex, Types.BINARY);
			} else {
				adapter.setBinaryStream(this, parameterIndex, x, length);
			}
		} catch (SQLException e) {
			
			throw trans.getSqlException(e);
		}
	}

	public void setBlob(int i, java.sql.Blob x) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		try {
			((java.sql.PreparedStatement) passthru).setBoolean(parameterIndex, x);
		} catch (SQLException e) {
			
			throw trans.getSqlException(e);
		}
	}

	public void setByte(int parameterIndex, byte x) throws SQLException {
		try {
			((java.sql.PreparedStatement) passthru).setByte(parameterIndex, x);
		} catch (SQLException e) {
			
			throw trans.getSqlException(e);
		}
	}

	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		try {
			if (x == null) {
				setNull(parameterIndex, Types.BINARY);
			} 
			else {
				adapter.setBytes(this, parameterIndex, x);
			}
		} 
		catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void setCharacterStream(int parameterIndex, java.io.Reader reader, int length) throws SQLException {
		try {
			if (reader == null) {
				setNull(parameterIndex, Types.CLOB);
			} 
			else {
				adapter.setCharacterStream(this, parameterIndex, reader, length);
			}
		} 
		catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void setClob(int i, java.sql.Clob x) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	public void setDate(int parameterIndex, java.sql.Date x) throws SQLException {
		((java.sql.PreparedStatement) passthru).setDate((parameterIndex), x);
	}

	public void setDate(int parameterIndex, java.sql.Date x, java.util.Calendar calendar) throws SQLException {
		
		try {
			if (x != null) {
				calendar = (Calendar) calendar.clone();
				calendar.setTime(x);
				Calendar local = Calendar.getInstance();
				convertTime(calendar, local);
				x = new java.sql.Date(local.getTime().getTime());
			}
			((java.sql.PreparedStatement) passthru).setDate(parameterIndex, x);
		} 
		catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void setDBEncoding(String encoding) {
	}

	public void setDouble(int arg0, double arg1) throws SQLException {
		try {
			((java.sql.PreparedStatement) passthru).setDouble(arg0, arg1);
		} 
		catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void setFloat(int parameterIndex, float x) throws SQLException {
		try {
			((java.sql.PreparedStatement) passthru).setFloat(parameterIndex, x);
		} 
		catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void setInt(int parameterIndex, int x) throws SQLException {
		try {
			((java.sql.PreparedStatement) passthru).setInt(parameterIndex, x);
		} 
		catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void setLong(int parameterIndex, long x) throws SQLException {
		try {
			((java.sql.PreparedStatement) passthru).setLong(parameterIndex, x);
		} catch (SQLException e) {
			
			throw trans.getSqlException(e);
		}
	}

	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		try {
			adapter.setNull(this, parameterIndex, sqlType);
		} 
		catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException {
		setNull(paramIndex, sqlType);
	}

	public void setObject(int parameterIndex, Object x) throws SQLException {
		try {
			if (x == null) {
				throw DbExceptionHelper.getInvalidValueException("x:" + x);
			}
			if (x != null && x instanceof String) {
				setString(parameterIndex, (String) x);
			} else {
				((java.sql.PreparedStatement) passthru).setObject(parameterIndex, x);
			}
		} catch (SQLException e) {
			
			throw trans.getSqlException(e);
		}

	}

	public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
		try {
			x = convertObject(x, targetSqlType);
			if (x == null) {
				setNull(parameterIndex, targetSqlType);
			} else {
				((java.sql.PreparedStatement) passthru).setObject(parameterIndex, x, targetSqlType);
			}
		} 
		catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) throws SQLException {
		try {
			x = convertObject(x, targetSqlType);
			if (x == null) {
				setNull(parameterIndex, targetSqlType);
			} else {
				((java.sql.PreparedStatement) passthru).setObject(parameterIndex, x, targetSqlType, scale);
			}
		} catch (SQLException e) {
			
			throw trans.getSqlException(e);
		}
	}

	public void setRef(int i, java.sql.Ref x) throws SQLException {
		try {
			((java.sql.PreparedStatement) passthru).setRef(i, x);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void setShort(int parameterIndex, short x) throws SQLException {
		try {
			((java.sql.PreparedStatement) passthru).setShort(parameterIndex, x);
		} 
		catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void setString(int parameterIndex, String x) throws SQLException {
		try {
			if (x == null) {
				setNull(parameterIndex, java.sql.Types.VARCHAR);
				return;
			}
			adapter.setString(this, parameterIndex, x);

		} catch (SQLException e) {
			
			throw trans.getSqlException(e);
		}
	}

	public void setTime(int parameterIndex, java.sql.Time x) throws SQLException {
		try {
			if (x == null) {
				setNull(parameterIndex, java.sql.Types.TIME);
				return;
			}
			((java.sql.PreparedStatement) passthru).setTime(parameterIndex, x);
		} catch (SQLException e) {
			
			throw trans.getSqlException(e);
		}
	}

	public void setTime(int parameterIndex, java.sql.Time x, java.util.Calendar calendar) throws SQLException {
		

		if (x != null) {
			calendar = (Calendar) calendar.clone();
			calendar.setTime(x);
			Calendar local = Calendar.getInstance();
			convertTime(calendar, local);
			x = new java.sql.Time(local.getTime().getTime());
		}
		try {
			((java.sql.PreparedStatement) passthru).setTime(parameterIndex, x);
		} catch (SQLException e) {
			
			throw e;
		}
	}

	public void setTimestamp(int parameterIndex, java.sql.Timestamp x) throws SQLException {
		try {
			if (x == null) {
				setNull(parameterIndex, java.sql.Types.TIMESTAMP);
				return;
			}
			((java.sql.PreparedStatement) passthru).setTimestamp(parameterIndex, x);
		} catch (SQLException e) {
			
			throw trans.getSqlException(e);
		}
	}

	public void setTimestamp(int parameterIndex, java.sql.Timestamp x, java.util.Calendar calendar) throws SQLException {
		try {
			if (x != null) {
				calendar = (Calendar) calendar.clone();
				calendar.setTime(x);
				Calendar local = Calendar.getInstance();
				convertTime(calendar, local);
				x = new java.sql.Timestamp(local.getTime().getTime());
			}
			((java.sql.PreparedStatement) passthru).setTimestamp(parameterIndex, x);
		} catch (SQLException e) {
			
			throw trans.getSqlException(e);
		}
	}

	public void setUnicodeStream(int parameterIndex, java.io.InputStream x, int arg2) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	public void setURL(int parameterIndex, URL x) throws SQLException {
		try {
			((java.sql.PreparedStatement) passthru).setURL(parameterIndex, x);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}


	/*
	 * £¨·Ç Javadoc£©
	 * 
	 * @see java.sql.PreparedStatement#getParameterMetaData()
	 */
	public ParameterMetaData getParameterMetaData() throws SQLException {
		return ((java.sql.PreparedStatement) passthru).getParameterMetaData();

	}

	/*
	 * £¨·Ç Javadoc£©
	 * 
	 * @see java.sql.Statement#getResultSetHoldability()
	 */
	public int getResultSetHoldability() throws SQLException {
		return passthru.getResultSetHoldability();

	}

	/*
	 * £¨·Ç Javadoc£©
	 * 
	 * @see java.sql.Statement#getMoreResults(int)
	 */
	public boolean getMoreResults(int current) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	public ResultSet getGeneratedKeys() throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	public boolean execute(String sql, String[] columnNames) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	private Object convertObject(Object x, int targetSqlType) {
		if (x == null) {
			return null;
		}
		int originalSqlType;
		if (x instanceof String) {
			originalSqlType = Types.VARCHAR;
		} else if (x instanceof java.math.BigDecimal) {
			originalSqlType = Types.DECIMAL;
		} else if (x instanceof Boolean) {
			originalSqlType = Types.BIT;
		} else if (x instanceof Integer) {
			originalSqlType = Types.INTEGER;
		} else if (x instanceof Long) {
			originalSqlType = Types.BIGINT;
		} else if (x instanceof Float) {
			originalSqlType = Types.REAL;
		} else if (x instanceof Double) {
			originalSqlType = Types.DOUBLE;
		} else if (x instanceof byte[]) {
			originalSqlType = Types.BINARY;
		} else if (x instanceof java.sql.Date) {
			originalSqlType = Types.DATE;
		} else if (x instanceof java.sql.Time) {
			originalSqlType = Types.TIME;
		} else if (x instanceof java.sql.Timestamp) {
			originalSqlType = Types.TIMESTAMP;
		} else {
			return x;
		}
		if (originalSqlType == targetSqlType) {
			return x;
		}
		switch (targetSqlType) {
		case Types.VARCHAR:
			return x.toString();
		case Types.DECIMAL:
			return new java.math.BigDecimal(x.toString());
		case Types.BIT:
			return Boolean.valueOf(x.toString());
		case Types.INTEGER:
			return Integer.valueOf(x.toString());
		case Types.BIGINT:
			return Long.valueOf(x.toString());
		case Types.REAL:
			return Float.valueOf(x.toString());
		case Types.DOUBLE:
			return Double.valueOf(x.toString());
		case Types.BINARY:
			return x;
		case Types.DATE:
			return x;
		case Types.TIME:
			return x;
		case Types.TIMESTAMP:
			return x;
		default:
			return x;
		}
	}

	private void convertTime(Calendar from, Calendar to) {
		to.set(Calendar.YEAR, from.get(Calendar.YEAR));
		to.set(Calendar.MONTH, from.get(Calendar.MONTH));
		to.set(Calendar.DAY_OF_MONTH, from.get(Calendar.DAY_OF_MONTH));
		to.set(Calendar.HOUR_OF_DAY, from.get(Calendar.HOUR_OF_DAY));
		to.set(Calendar.MINUTE, from.get(Calendar.MINUTE));
		to.set(Calendar.SECOND, from.get(Calendar.SECOND));
		to.set(Calendar.MILLISECOND, from.get(Calendar.MILLISECOND));
	}


//	public String getSQLString() {
//		StringBuffer buf = new StringBuffer();
//		int qMarkCount = 0;
//		StringTokenizer tok = new StringTokenizer(sqlTemplate + " ", "?");
//		while (tok.hasMoreTokens()) {
//			String oneChunk = tok.nextToken();
//			buf.append(oneChunk);
//			try {
//				Object value;
//				if (parameterValues.size() > 1 + qMarkCount) {
//					value = parameterValues.get(1 + qMarkCount++);
//				} else {
//					if (tok.hasMoreTokens()) {
//						value = null;
//					} else {
//						value = "";
//					}
//				}
//				buf.append("" + value);
//			} catch (Throwable e) {
//				buf.append("ERROR WHEN PRODUCING QUERY STRING FOR LOG." + e.toString());
//			}
//		}
//		return buf.toString().trim();
//	}

	public int[] executeBatch() throws SQLException {
		try {
			int[] result = passthru.executeBatch();
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {

	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {

	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
		try {

			if (x == null) {
				setNull(parameterIndex, Types.BINARY);
			} else {
				adapter.setBinaryStream(this, parameterIndex, x, -1);
			}
		} catch (SQLException e) {
			
			throw trans.getSqlException(e);
		}

	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void setNString(int parameterIndex, String value) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

}