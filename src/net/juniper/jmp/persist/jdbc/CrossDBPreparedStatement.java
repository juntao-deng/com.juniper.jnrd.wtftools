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
import java.util.Calendar;

public class CrossDBPreparedStatement extends CrossDBStatement implements PreparedStatement {
	public CrossDBPreparedStatement(PreparedStatement passthru, CrossDBConnection conn) {
		super(passthru, conn);
	}

	@Override
	public void addBatch() throws SQLException {
		((PreparedStatement)passthru).addBatch();
	}

	@Override
	public void clearParameters() throws SQLException {
		((PreparedStatement) passthru).clearParameters();
	}

	@Override
	public boolean execute() throws SQLException {
		return ((PreparedStatement) passthru).execute();
	}

	@Override
	public CrossDBResultSet executeQuery() throws SQLException {
		CrossDBResultSet rs = new CrossDBResultSet(((PreparedStatement) passthru).executeQuery(), this);
		registerResultSet(rs);
		return rs;
	}

	@Override
	public int executeUpdate() throws SQLException {
		return ((PreparedStatement) passthru).executeUpdate();
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return ((PreparedStatement) passthru).getMetaData();
	}

	@Override
	public void setArray(int i, Array x) throws SQLException {
		((PreparedStatement) passthru).setArray(i, x);
	}

	@Override
	public void setAsciiStream(int parameterIndex, java.io.InputStream x, int length) throws SQLException {
		((PreparedStatement) passthru).setAsciiStream(parameterIndex, x, length);
	}

	@Override
	public void setBigDecimal(int parameterIndex, java.math.BigDecimal x) throws SQLException {
		((PreparedStatement) passthru).setBigDecimal(parameterIndex, x);
	}

	@Override
	public void setBinaryStream(int parameterIndex, java.io.InputStream x, int length) throws SQLException {
		((PreparedStatement) passthru).setBinaryStream(parameterIndex, x, length);
	}

	@Override
	public void setBlob(int parameterIndex, Blob x) throws SQLException {
		((PreparedStatement) passthru).setBlob(parameterIndex, x);
	}

	@Override
	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		((PreparedStatement) passthru).setBoolean(parameterIndex, x);
	}

	@Override
	public void setByte(int parameterIndex, byte x) throws SQLException {
		((PreparedStatement) passthru).setByte(parameterIndex, x);
	}

	@Override
	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		((PreparedStatement) passthru).setBytes(parameterIndex, x);
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
		((PreparedStatement) passthru).setCharacterStream(parameterIndex, reader, length);
	}

	@Override
	public void setClob(int parameterIndex, Clob x) throws SQLException {
		((PreparedStatement) passthru).setClob(parameterIndex, x);
	}

	@Override
	public void setDate(int parameterIndex, Date x) throws SQLException {
		((PreparedStatement) passthru).setDate(parameterIndex, x);
	}

	@Override
	public void setDate(int parameterIndex, Date date, Calendar cal) throws SQLException {
		((PreparedStatement) passthru).setDate(parameterIndex, date, cal);
	}

	@Override
	public void setDouble(int parameterIndex, double d) throws SQLException {
		((PreparedStatement) passthru).setDouble(parameterIndex, d);
	}

	@Override
	public void setFloat(int parameterIndex, float x) throws SQLException {
		((PreparedStatement) passthru).setFloat(parameterIndex, x);
	}

	@Override
	public void setInt(int parameterIndex, int x) throws SQLException {
		((PreparedStatement) passthru).setInt(parameterIndex, x);
	}

	@Override
	public void setLong(int parameterIndex, long x) throws SQLException {
		((PreparedStatement) passthru).setLong(parameterIndex, x);
	}

	@Override
	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		((PreparedStatement) passthru).setNull(parameterIndex, sqlType);
	}

	@Override
	public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException {
		((PreparedStatement) passthru).setNull(paramIndex, sqlType, typeName);
	}

	@Override
	public void setObject(int parameterIndex, Object x) throws SQLException {
		((PreparedStatement) passthru).setObject(parameterIndex, x);
	}

	@Override
	public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
		((PreparedStatement) passthru).setObject(parameterIndex, x, targetSqlType);
	}

	@Override
	public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) throws SQLException {
		((PreparedStatement) passthru).setObject(parameterIndex, x, targetSqlType, scale);
	}

	@Override
	public void setRef(int parameterIndex, Ref x) throws SQLException {
		((PreparedStatement) passthru).setRef(parameterIndex, x);
	}

	@Override
	public void setShort(int parameterIndex, short x) throws SQLException {
		((PreparedStatement) passthru).setShort(parameterIndex, x);
	}

	@Override
	public void setString(int parameterIndex, String x) throws SQLException {
		((PreparedStatement) passthru).setString(parameterIndex, x);
	}

	@Override
	public void setTime(int parameterIndex, Time x) throws SQLException {
		((PreparedStatement) passthru).setTime(parameterIndex, x);
	}

	@Override
	public void setTime(int parameterIndex, Time x, java.util.Calendar calendar) throws SQLException {
		((PreparedStatement) passthru).setTime(parameterIndex, x);
	}

	@Override
	public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
		((PreparedStatement) passthru).setTimestamp(parameterIndex, x);
	}

	@Override
	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
		((PreparedStatement) passthru).setTimestamp(parameterIndex, x, cal);
	}

	@Override
	public void setUnicodeStream(int parameterIndex, java.io.InputStream x, int length) throws SQLException {
		((PreparedStatement) passthru).setUnicodeStream(parameterIndex, x, length);
	}

	@Override
	public void setURL(int parameterIndex, URL x) throws SQLException {
		((PreparedStatement) passthru).setURL(parameterIndex, x);
	}

	@Override
	public ParameterMetaData getParameterMetaData() throws SQLException {
		return ((PreparedStatement) passthru).getParameterMetaData();
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		return passthru.getResultSetHoldability();
	}

	@Override
	public boolean getMoreResults(int current) throws SQLException {
		return passthru.getMoreResults(current);
	}

	@Override
	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		return passthru.executeUpdate(sql, autoGeneratedKeys);
	}

	@Override
	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		return passthru.execute(sql, autoGeneratedKeys);
	}

	@Override
	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		return passthru.executeUpdate(sql, columnIndexes);
	}

	@Override
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		return passthru.execute(sql, columnIndexes);
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		return passthru.getGeneratedKeys();
	}

	@Override
	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		return passthru.executeUpdate(sql, columnNames);
	}

	@Override
	public boolean execute(String sql, String[] columnNames) throws SQLException {
		return passthru.execute(sql, columnNames);
	}

	@Override
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
		((PreparedStatement) passthru).setBinaryStream(parameterIndex, x, length);
	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
		((PreparedStatement) passthru).setBlob(parameterIndex, inputStream);
	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
		((PreparedStatement) passthru).setBlob(parameterIndex, inputStream, length);
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
		((PreparedStatement) passthru).setCharacterStream(parameterIndex, reader);
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
		((PreparedStatement) passthru).setCharacterStream(parameterIndex, reader, length);
	}

	@Override
	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		((PreparedStatement) passthru).setClob(parameterIndex, reader);
	}

	@Override
	public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
		((PreparedStatement) passthru).setClob(parameterIndex, reader, length);
	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
		((PreparedStatement) passthru).setNCharacterStream(parameterIndex, value);
	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
		((PreparedStatement) passthru).setNCharacterStream(parameterIndex, value, length);
	}

	@Override
	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		((PreparedStatement) passthru).setNClob(parameterIndex, value);
	}

	@Override
	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		((PreparedStatement) passthru).setNClob(parameterIndex, reader);
	}

	@Override
	public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
		((PreparedStatement) passthru).setNClob(parameterIndex, reader, length);
	}

	@Override
	public void setNString(int parameterIndex, String value) throws SQLException {
		((PreparedStatement) passthru).setNString(parameterIndex, value);
	}

	@Override
	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		((PreparedStatement) passthru).setRowId(parameterIndex, x);
	}

	@Override
	public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
		((PreparedStatement) passthru).setSQLXML(CLOSE_ALL_RESULTS, xmlObject);
	}
}