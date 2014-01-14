package net.juniper.jmp.persist.jdbc.trans;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.juniper.jmp.persist.exp.CrossDBSQLException;
import net.juniper.jmp.persist.jdbc.CrossDBPreparedStatement;



public abstract class BaseAdapter implements Adapter {
	protected Connection nativeConn;

	public void setNativeConn(Connection nativeConn) throws SQLException {
		this.nativeConn = nativeConn;
		init();
	}

	public void init() throws SQLException {
	}

	public String getName() {
		return getClass().getName();
	}

	public String getBinaryConstant(String s) {
		return "X'" + s + "'";
	}

	public String getNow() throws SQLException {
		return "NOW()";
	}

	public SQLException convertThrowable(Throwable e) {
		if (e instanceof SQLException) {
			return convertSQLException((SQLException) e);
		}
		return new CrossDBSQLException(e.getMessage(), e.getMessage(), e);
	}

	public SQLException convertSQLException(SQLException e) {
		return new CrossDBSQLException(e.getMessage(), e.getSQLState(), e);
	}

	public void cancel(Statement stat) throws SQLException {
		stat.cancel();
	}

	public byte[] getBytes(ResultSet rs, int columnIndex) throws SQLException {
		return rs.getBytes(columnIndex);
	}

	public byte[] getBytes(ResultSet rs, String columnname) throws SQLException {
		return rs.getBytes(columnname);
	}

	public String getString(ResultSet rs, int columnIndex) throws SQLException {
		return rs.getString(columnIndex);
	}

	public String getString(ResultSet rs, String columnName)
			throws SQLException {
		return rs.getString(columnName);
	}

	public Object getObject(ResultSet rs, int columnIndex, int scale)
			throws SQLException {
		return rs.getObject(columnIndex);
	}

	public Object getObject(ResultSet rs, String columnName, int scale)
			throws SQLException {
		return rs.getObject(columnName);
	}

	public Reader getCharacterStream(ResultSet rs, int columnIndex)
			throws SQLException {
		return rs.getCharacterStream(columnIndex);
	}

	public Reader getCharacterStream(ResultSet rs, String columnName)
			throws SQLException {
		return rs.getCharacterStream(columnName);
	}

	public String getClobString(ResultSet rs, int columnIndex)
			throws SQLException {
		return rs.getString(columnIndex);
	}

	public String getClobString(ResultSet rs, String columnName)
			throws SQLException {
		return rs.getString(columnName);
	}

	public byte[] getBlobByte(ResultSet rs, String columnName)
			throws SQLException, IOException {
		java.io.InputStream is = rs.getBinaryStream(columnName);
		if (is == null)
			return null;
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len = -1;
		while ((len = is.read(buf)) != -1) {
			baos.write(buf, 0, len);
		}
		return baos.toByteArray();
	}

	public byte[] getBlobByte(ResultSet rs, int index) throws SQLException,
			IOException {
		java.io.InputStream is = rs.getBinaryStream(index);
		if (is == null)
			return null;
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len = -1;
		while ((len = is.read(buf)) != -1) {
			baos.write(buf, 0, len);
		}
		return baos.toByteArray();
	}

	public void setString(CrossDBPreparedStatement prep, int parameterIndex,
			String x) throws SQLException {
		((PreparedStatement) prep.getVendorObject()).setString(parameterIndex,
				x);
	}

	public void setCharacterStream(CrossDBPreparedStatement prep,
			int parameterIndex, Reader x, int length) throws SQLException {
		((PreparedStatement) prep.getVendorObject()).setCharacterStream(
				parameterIndex, x, length);
	}

	public void setAsciiStream(CrossDBPreparedStatement prep,
			int parameterIndex, InputStream x, int length) throws SQLException {
		((PreparedStatement) prep.getVendorObject()).setAsciiStream(
				parameterIndex, x, length);
	}

	public void setNull(CrossDBPreparedStatement prep, int parameterIndex,
			int sqlType) throws SQLException {
		((PreparedStatement) prep.getVendorObject()).setNull(parameterIndex,
				sqlType);
	}

	public void setBytes(CrossDBPreparedStatement prep, int parameterIndex,
			byte[] x) throws SQLException {
		((PreparedStatement) prep.getVendorObject())
				.setBytes(parameterIndex, x);
	}

	public void setBinaryStream(CrossDBPreparedStatement prep,
			int parameterIndex, InputStream x, int length) throws SQLException {
		((PreparedStatement) prep.getVendorObject()).setBinaryStream(
				parameterIndex, x, length);
	}

}
