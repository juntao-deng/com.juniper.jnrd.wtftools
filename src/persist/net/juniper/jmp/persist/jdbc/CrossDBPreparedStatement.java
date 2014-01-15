package net.juniper.jmp.persist.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;

import net.juniper.jmp.persist.utils.DbExceptionHelper;

public class CrossDBPreparedStatement extends CrossDBStatement implements PreparedStatement {
	public CrossDBPreparedStatement(PreparedStatement passthru, CrossDBConnection conn) {
		super(passthru, conn);
	}

	public void addBatch() throws SQLException {
		((PreparedStatement)passthru).addBatch();
	}

	public void clearParameters() throws SQLException {
		((PreparedStatement) passthru).clearParameters();
	}

	public boolean execute() throws SQLException {
		return ((PreparedStatement) passthru).execute();
	}

	public ResultSet executeQuery() throws SQLException {
		CrossDBResultSet rs = new CrossDBResultSet(((PreparedStatement) passthru).executeQuery(), this);
		registerResultSet(rs);
		return rs;
	}

	public int executeUpdate() throws SQLException {
		int ret = ((PreparedStatement) passthru).executeUpdate();
		return ret;
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		return ((PreparedStatement) passthru).getMetaData();
	}

	public void setArray(int i, Array x) throws SQLException {
		((PreparedStatement) passthru).setArray(i, x);
	}

	@Override
	public void setAsciiStream(int parameterIndex, java.io.InputStream x, int length) throws SQLException {
		((PreparedStatement) passthru).setAsciiStream(parameterIndex, x, length);
	}

	public void setBigDecimal(int parameterIndex, java.math.BigDecimal x) throws SQLException {
		((PreparedStatement) passthru).setBigDecimal(parameterIndex, x);
	}

	public void setBinaryStream(int parameterIndex, java.io.InputStream x, int length) throws SQLException {
		((PreparedStatement) passthru).setBinaryStream(parameterIndex, x, length);
	}

	public void setBlob(int i, Blob x) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		((PreparedStatement) passthru).setBoolean(parameterIndex, x);
	}

	public void setByte(int parameterIndex, byte x) throws SQLException {
		((PreparedStatement) passthru).setByte(parameterIndex, x);
	}

	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		((PreparedStatement) passthru).setBytes(parameterIndex, x);
	}

	public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
		((PreparedStatement) passthru).setCharacterStream(parameterIndex, reader, length);
	}

	public void setClob(int i, Clob x) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	public void setDate(int parameterIndex, Date x) throws SQLException {
		((PreparedStatement) passthru).setDate((parameterIndex), x);
	}

	public void setDate(int parameterIndex, Date date, Calendar cal) throws SQLException {
		((PreparedStatement) passthru).setDate(parameterIndex, date, cal);
	}

	public void setDouble(int parameterIndex, double d) throws SQLException {
		((PreparedStatement) passthru).setDouble(parameterIndex, d);
	}

	public void setFloat(int parameterIndex, float x) throws SQLException {
		((PreparedStatement) passthru).setFloat(parameterIndex, x);
	}

	public void setInt(int parameterIndex, int x) throws SQLException {
		((PreparedStatement) passthru).setInt(parameterIndex, x);
	}

	public void setLong(int parameterIndex, long x) throws SQLException {
		((PreparedStatement) passthru).setLong(parameterIndex, x);
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
				((PreparedStatement) passthru).setObject(parameterIndex, x);
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
				((PreparedStatement) passthru).setObject(parameterIndex, x, targetSqlType);
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
				((PreparedStatement) passthru).setObject(parameterIndex, x, targetSqlType, scale);
			}
		} catch (SQLException e) {
			
			throw trans.getSqlException(e);
		}
	}

	public void setRef(int i, Ref x) throws SQLException {
		try {
			((PreparedStatement) passthru).setRef(i, x);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void setShort(int parameterIndex, short x) throws SQLException {
		try {
			((PreparedStatement) passthru).setShort(parameterIndex, x);
		} 
		catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void setString(int parameterIndex, String x) throws SQLException {
		try {
			if (x == null) {
				setNull(parameterIndex, Types.VARCHAR);
				return;
			}
			adapter.setString(this, parameterIndex, x);

		} catch (SQLException e) {
			
			throw trans.getSqlException(e);
		}
	}

	public void setTime(int parameterIndex, Time x) throws SQLException {
		try {
			if (x == null) {
				setNull(parameterIndex, Types.TIME);
				return;
			}
			((PreparedStatement) passthru).setTime(parameterIndex, x);
		} catch (SQLException e) {
			
			throw trans.getSqlException(e);
		}
	}

	public void setTime(int parameterIndex, Time x, java.util.Calendar calendar) throws SQLException {
		((PreparedStatement) passthru).setTime(parameterIndex, x);
	}

	public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
		((PreparedStatement) passthru).setTimestamp(parameterIndex, x);
	}

	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
		((PreparedStatement) passthru).setTimestamp(parameterIndex, x, cal);
	}

	public void setUnicodeStream(int parameterIndex, java.io.InputStream x, int arg2) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	public void setURL(int parameterIndex, URL x) throws SQLException {
		((PreparedStatement) passthru).setURL(parameterIndex, x);
	}

	public ParameterMetaData getParameterMetaData() throws SQLException {
		return ((PreparedStatement) passthru).getParameterMetaData();
	}

	public int getResultSetHoldability() throws SQLException {
		return passthru.getResultSetHoldability();
	}

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
		} else if (x instanceof Date) {
			originalSqlType = Types.DATE;
		} else if (x instanceof Time) {
			originalSqlType = Types.TIME;
		} else if (x instanceof Timestamp) {
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

	public int[] executeBatch() throws SQLException {
		return passthru.executeBatch();
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
		((PreparedStatement) passthru).setAsciiStream(parameterIndex, x);
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
		((PreparedStatement) passthru).setAsciiStream(parameterIndex, x, length);
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
		((PreparedStatement) passthru).setBinaryStream(parameterIndex, x);
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