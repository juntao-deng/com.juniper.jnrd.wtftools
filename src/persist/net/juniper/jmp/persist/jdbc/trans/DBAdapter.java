package net.juniper.jmp.persist.jdbc.trans;

import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.juniper.jmp.persist.jdbc.CrossDBPreparedStatement;
import net.juniper.jmp.persist.jdbc.CrossDBStatement;

public interface DBAdapter {
	public void setNativeConn(Connection nativeConn) throws SQLException;

	public void init() throws SQLException;

	String getName();

	String getDriverClass();

	String getBinaryConstant(String s);

	String getNow() throws SQLException;
//
//	SQLException convertThrowable(Throwable e);
//
//	SQLException convertSQLException(SQLException e);

	void cancel(Statement stat) throws SQLException;

	byte[] getBytes(ResultSet rs, int columnIndex) throws SQLException;

	byte[] getBytes(ResultSet rs, String columnname) throws SQLException;

	String getString(ResultSet rs, int columnIndex) throws SQLException;

	String getString(ResultSet rs, String columnName) throws SQLException;

	Object getObject(ResultSet rs, int columnIndex, int scale)
			throws SQLException;

	Object getObject(ResultSet rs, String columnName, int scale)
			throws SQLException;

	Reader getCharacterStream(ResultSet rs, int columnIndex)
			throws SQLException;

	Reader getCharacterStream(ResultSet rs, String columnName)
			throws SQLException;

	String getClobString(ResultSet rs, int columnIndex) throws SQLException;

	String getClobString(ResultSet rs, String columnName) throws SQLException;

	public byte[] getBlobByte(ResultSet rs, String columnName)
			throws SQLException, java.io.IOException;

	public byte[] getBlobByte(ResultSet rs, int index) throws SQLException,
			java.io.IOException;

	void setString(CrossDBPreparedStatement prep, int parameterIndex, String x)
			throws SQLException;

	void setCharacterStream(CrossDBPreparedStatement prep, int parameterIndex,
			Reader x, int length) throws SQLException;

	void setAsciiStream(CrossDBPreparedStatement prep, int parameterIndex,
			InputStream x, int length) throws SQLException;

	void setNull(CrossDBPreparedStatement prep, int parameterIndex, int sqlType)
			throws SQLException;

	void setBytes(CrossDBPreparedStatement prep, int parameterIndex, byte[] x)
			throws SQLException;

	void setBinaryStream(CrossDBPreparedStatement prep, int parameterIndex,
			InputStream x, int length) throws SQLException;

	void supportHugeData(CrossDBStatement stmt) throws SQLException;

}
