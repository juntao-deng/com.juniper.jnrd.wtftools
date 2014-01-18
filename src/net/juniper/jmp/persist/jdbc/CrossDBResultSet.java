package net.juniper.jmp.persist.jdbc;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * A simple decorator for resultset, in case we need do something special later(eg, tracer)
 * @author juntaod
 *
 */
public class CrossDBResultSet implements ResultSet {
	private ResultSet passthru;
	private CrossDBStatement stmt;

	public CrossDBResultSet() {
		super();
	}

	public CrossDBResultSet(ResultSet passthru, CrossDBStatement stmt) {
		super();
		this.stmt = stmt;
		this.passthru = passthru;
	}

	public ResultSet getResultSet() {
		return passthru;
	}

	@Override
	public boolean absolute(int row) throws SQLException {
		return passthru.absolute(row);
	}

	@Override
	public void afterLast() throws SQLException {
		passthru.afterLast();
	}

	@Override
	public void beforeFirst() throws SQLException {
		passthru.beforeFirst();
	}

	@Override
	public void cancelRowUpdates() throws SQLException {
		passthru.cancelRowUpdates();
	}

	@Override
	public void clearWarnings() throws SQLException {
		passthru.clearWarnings();
	}

	@Override
	public void close() throws SQLException {
		if (passthru != null) {
			passthru.close();
		}
	}

	@Override
	public void deleteRow() throws SQLException {
		passthru.deleteRow();
	}

	@Override
	public int findColumn(String columnName) throws SQLException {
		return passthru.findColumn(columnName);
	}

	@Override
	public boolean first() throws SQLException {
		return passthru.first();
	}

	@Override
	public Array getArray(int index) throws SQLException {
		return passthru.getArray(index);
	}

	@Override
	public Array getArray(String colName) throws SQLException {
		return passthru.getArray(colName);
	}

	@Override
	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		return passthru.getAsciiStream(columnIndex);
	}

	@Override
	public InputStream getAsciiStream(String columnName) throws SQLException {
		return passthru.getAsciiStream(columnName);
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		return passthru.getBigDecimal(columnIndex);
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
		return passthru.getBigDecimal(columnIndex, scale);
	}

	@Override
	public BigDecimal getBigDecimal(String columnName) throws SQLException {
		return passthru.getBigDecimal(columnName);
	}

	@Override
	public BigDecimal getBigDecimal(String columnIndex, int scale) throws SQLException {
		return passthru.getBigDecimal(columnIndex, scale);
	}

	@Override
	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		return passthru.getBinaryStream(columnIndex);
	}

	@Override
	public InputStream getBinaryStream(String columnName) throws SQLException {
		return passthru.getBinaryStream(columnName);
	}

	@Override
	public Blob getBlob(int columnIndex) throws SQLException {
		return passthru.getBlob(columnIndex);
	}

	@Override
	public Blob getBlob(String colName) throws SQLException {
		return passthru.getBlob(colName);
	}

	@Override
	public boolean getBoolean(int columnIndex) throws SQLException {
		return passthru.getBoolean(columnIndex);
	}

	@Override
	public boolean getBoolean(String columnName) throws SQLException {
		return passthru.getBoolean(columnName);
	}

	@Override
	public byte getByte(int columnIndex) throws SQLException {
		return passthru.getByte(columnIndex);
	}

	@Override
	public byte getByte(String columnName) throws SQLException {
		return passthru.getByte(columnName);
	}

	@Override
	public byte[] getBytes(int columnIndex) throws SQLException {
		return passthru.getBytes(columnIndex);
	}

	@Override
	public byte[] getBytes(String columnName) throws SQLException {
		return passthru.getBytes(columnName);
	}

	@Override
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		return passthru.getCharacterStream(columnIndex);
	}

	@Override
	public Reader getCharacterStream(String columnName) throws SQLException {
		return passthru.getCharacterStream(columnName);
	}

	@Override
	public Clob getClob(int columnIndex) throws SQLException {
		return passthru.getClob(columnIndex);
	}

	@Override
	public Clob getClob(String colName) throws SQLException {
		return passthru.getClob(colName);
	}

	@Override
	public int getConcurrency() throws SQLException {
		return passthru.getConcurrency();
	}

	@Override
	public String getCursorName() throws SQLException {
		return passthru.getCursorName();
	}

	@Override
	public Date getDate(int columnIndex) throws SQLException {
		return passthru.getDate(columnIndex);
	}

	@Override
	public Date getDate(int columnIndex, java.util.Calendar cal) throws SQLException {
		return passthru.getDate(columnIndex, cal);
	}

	@Override
	public Date getDate(String columnName) throws SQLException {
		return passthru.getDate(columnName);
	}

	@Override
	public Date getDate(String columnName, Calendar cal) throws SQLException {
		return passthru.getDate(columnName, cal);
	}

	@Override
	public double getDouble(int columnIndex) throws SQLException {
		return passthru.getDouble(columnIndex);
	}

	@Override
	public double getDouble(String columnName) throws SQLException {
		return passthru.getDouble(columnName);
	}

	@Override
	public int getFetchDirection() throws SQLException {
		return passthru.getFetchDirection();
	}

	@Override
	public int getFetchSize() throws SQLException {
		return passthru.getFetchSize();
	}

	@Override
	public float getFloat(int columnIndex) throws SQLException {
		return passthru.getFloat(columnIndex);
	}

	@Override
	public float getFloat(String columnName) throws SQLException {
		return passthru.getFloat(columnName);
	}

	@Override
	public int getInt(int columnIndex) throws SQLException {
		return passthru.getInt(columnIndex);
	}

	@Override
	public int getInt(String columnName) throws SQLException {
		return passthru.getInt(columnName);
	}

	@Override
	public long getLong(int columnIndex) throws SQLException {
		return passthru.getLong(columnIndex);
	}

	@Override
	public long getLong(String columnName) throws SQLException {
		return passthru.getLong(columnName);
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return passthru.getMetaData();
	}

	@Override
	public Object getObject(int columnIndex) throws SQLException {
		return passthru.getObject(columnIndex);
	}

	@Override
	public Object getObject(String columnName) throws SQLException {
		return passthru.getObject(columnName);
	}

	@Override
	public Ref getRef(int columnIndex) throws SQLException {
		return passthru.getRef(columnIndex);
	}

	@Override
	public Ref getRef(String colName) throws SQLException {
		return passthru.getRef(colName);
	}

	@Override
	public int getRow() throws SQLException {
		return passthru.getRow();
	}

	@Override
	public short getShort(int columnIndex) throws SQLException {
		return passthru.getShort(columnIndex);
	}

	@Override
	public short getShort(String columnName) throws SQLException {
		return passthru.getShort(columnName);
	}

	@Override
	public Statement getStatement() throws SQLException {
		return this.stmt;
	}

	@Override
	public String getString(int columnIndex) throws SQLException {
		return passthru.getString(columnIndex);
	}

	@Override
	public String getString(String columnName) throws SQLException {
		return passthru.getString(columnName);
	}

	@Override
	public Time getTime(int columnIndex) throws SQLException {
		return passthru.getTime(columnIndex);
	}

	@Override
	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		return passthru.getTime(columnIndex, cal);
	}

	@Override
	public Time getTime(String columnName) throws SQLException {
		return passthru.getTime(columnName);
	}

	@Override
	public Time getTime(String columnName, Calendar cal) throws SQLException {
		return passthru.getTime(columnName, cal);
	}

	@Override
	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		return passthru.getTimestamp(columnIndex);
	}

	@Override
	public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
		return passthru.getTimestamp(columnIndex, cal);
	}

	@Override
	public Timestamp getTimestamp(String arg0) throws SQLException {
		return passthru.getTimestamp(arg0);
	}

	@Override
	public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
		return passthru.getTimestamp(columnName, cal);
	}

	@Override
	public int getType() throws SQLException {
		return passthru.getType();
	}

	@Override
	@Deprecated
	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		return passthru.getUnicodeStream(columnIndex);
	}

	@Override
	@Deprecated
	public InputStream getUnicodeStream(String columnName) throws SQLException {
		return passthru.getUnicodeStream(columnName);
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return passthru.getWarnings();
	}

	@Override
	public void insertRow() throws SQLException {
		passthru.insertRow();
	}

	@Override
	public boolean isAfterLast() throws SQLException {
		return passthru.isAfterLast();
	}

	@Override
	public boolean isBeforeFirst() throws SQLException {
		return passthru.isBeforeFirst();
	}

	@Override
	public boolean isFirst() throws SQLException {
		return passthru.isFirst();
	}

	@Override
	public boolean isLast() throws SQLException {
		return passthru.isLast();
	}

	@Override
	public boolean last() throws SQLException {
		return passthru.last();
	}

	@Override
	public void moveToCurrentRow() throws SQLException {
		passthru.moveToCurrentRow();
	}

	@Override
	public void moveToInsertRow() throws SQLException {
		passthru.moveToInsertRow();
	}

	@Override
	public boolean next() throws SQLException {
		return passthru.next();
	}

	@Override
	public boolean previous() throws SQLException {
		return passthru.previous();
	}

	@Override
	public void refreshRow() throws SQLException {
		passthru.refreshRow();
	}

	@Override
	public boolean relative(int rows) throws SQLException {
		return passthru.relative(rows);
	}

	@Override
	public boolean rowDeleted() throws SQLException {
		return passthru.rowDeleted();
	}

	@Override
	public boolean rowInserted() throws SQLException {
		return passthru.rowInserted();
	}

	@Override
	public boolean rowUpdated() throws SQLException {
		return passthru.rowUpdated();
	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		passthru.setFetchDirection(direction);
	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		passthru.setFetchSize(rows);
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
		passthru.updateAsciiStream(columnIndex, x, length);
	}

	@Override
	public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
		passthru.updateAsciiStream(columnName, x, length);
	}

	@Override
	public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
		passthru.updateBigDecimal(columnIndex, x);
	}

	@Override
	public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
		passthru.updateBigDecimal(columnName, x);
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
		passthru.updateBinaryStream(columnIndex, x, length);
	}

	@Override
	public void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException {
		passthru.updateBinaryStream(columnName, x, length);

	}

	@Override
	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		passthru.updateBoolean(columnIndex, x);
	}

	@Override
	public void updateBoolean(String columnName, boolean x) throws SQLException {
		passthru.updateBoolean(columnName, x);
	}

	@Override
	public void updateByte(int columnIndex, byte x) throws SQLException {
		passthru.updateByte(columnIndex, x);
	}

	@Override
	public void updateByte(String columnName, byte x) throws SQLException {
		passthru.updateByte(columnName, x);
	}

	@Override
	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		passthru.updateBytes(columnIndex, x);
	}

	@Override
	public void updateBytes(String columnName, byte[] x) throws SQLException {
		passthru.updateBytes(columnName, x);
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
		passthru.updateCharacterStream(columnIndex, x, length);
	}

	@Override
	public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {
		passthru.updateCharacterStream(columnName, reader, length);
	}

	@Override
	public void updateDate(int columnIndex, Date x) throws SQLException {
		passthru.updateDate(columnIndex, x);
	}

	@Override
	public void updateDate(String columnName, Date x) throws SQLException {
		passthru.updateDate(columnName, x);
	}

	@Override
	public void updateDouble(int columnIndex, double x) throws SQLException {
		passthru.updateDouble(columnIndex, x);
	}

	@Override
	public void updateDouble(String columnName, double x) throws SQLException {
		passthru.updateDouble(columnName, x);
	}

	@Override
	public void updateFloat(int columnIndex, float x) throws SQLException {
		passthru.updateFloat(columnIndex, x);
	}

	@Override
	public void updateFloat(String columnName, float x) throws SQLException {
		passthru.updateFloat(columnName, x);
	}

	@Override
	public void updateInt(int columnIndex, int x) throws SQLException {
		passthru.updateInt(columnIndex, x);
	}

	@Override
	public void updateInt(String columnName, int x) throws SQLException {
		passthru.updateInt(columnName, x);
	}

	@Override
	public void updateLong(int columnIndex, long x) throws SQLException {
		passthru.updateLong(columnIndex, x);
	}

	@Override
	public void updateLong(String columnName, long x) throws SQLException {
		passthru.updateLong(columnName, x);
	}

	@Override
	public void updateNull(int columnIndex) throws SQLException {
		passthru.updateNull(columnIndex);
	}

	@Override
	public void updateNull(String columnName) throws SQLException {
		passthru.updateNull(columnName);
	}

	@Override
	public void updateObject(int columnIndex, Object x) throws SQLException {
		passthru.updateObject(columnIndex, x);
	}

	@Override
	public void updateObject(int columnIndex, Object x, int scale) throws SQLException {
		passthru.updateObject(columnIndex, x, scale);
	}

	@Override
	public void updateObject(String columnName, Object x) throws SQLException {
		passthru.updateObject(columnName, x);
	}

	@Override
	public void updateObject(String columnName, Object x, int scale) throws SQLException {
		passthru.updateObject(columnName, x, scale);
	}

	@Override
	public void updateRow() throws SQLException {
		passthru.updateRow();
	}

	@Override
	public void updateShort(int columnIndex, short x) throws SQLException {
		passthru.updateShort(columnIndex, x);
	}

	@Override
	public void updateShort(String columnName, short x) throws SQLException {
		passthru.updateShort(columnName, x);
	}

	@Override
	public void updateString(int columnIndex, String x) throws SQLException {
		passthru.updateString(columnIndex, x);
	}

	@Override
	public void updateString(String columnName, String x) throws SQLException {
		passthru.updateString(columnName, x);
	}

	@Override
	public void updateTime(int columnIndex, Time x) throws SQLException {
		passthru.updateTime(columnIndex, x);
	}

	@Override
	public void updateTime(String columnName, Time x) throws SQLException {
		passthru.updateTime(columnName, x);
	}

	@Override
	public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
		passthru.updateTimestamp(columnIndex, x);
	}

	@Override
	public void updateTimestamp(String columnName, Timestamp x) throws SQLException {
		passthru.updateTimestamp(columnName, x);
	}

	@Override
	public boolean wasNull() throws SQLException {
		return passthru.wasNull();
	}

	@Override
	public URL getURL(int columnIndex) throws SQLException {
		return passthru.getURL(columnIndex);
	}

	@Override
	public void updateArray(int columnIndex, Array x) throws SQLException {
		passthru.updateArray(columnIndex, x);
	}

	@Override
	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		passthru.updateBlob(columnIndex, x);
	}

	@Override
	public void updateClob(int columnIndex, Clob x) throws SQLException {
		passthru.updateClob(columnIndex, x);
	}

	@Override
	public void updateRef(int columnIndex, Ref x) throws SQLException {
		passthru.updateRef(columnIndex, x);
	}

	@Override
	public URL getURL(String columnName) throws SQLException {
		return passthru.getURL(columnName);
	}

	@Override
	public void updateArray(String columnName, Array x) throws SQLException {
		passthru.updateArray(columnName, x);
	}

	@Override
	public void updateBlob(String columnName, Blob x) throws SQLException {
		passthru.updateBlob(columnName, x);
	}

	@Override
	public void updateClob(String columnName, Clob x) throws SQLException {
		passthru.updateClob(columnName, x);
	}

	@Override
	public void updateRef(String columnName, Ref x) throws SQLException {
		passthru.updateRef(columnName, x);
	}

	@Override
	public int getHoldability() throws SQLException {
		return passthru.getHoldability();
	}

	@Override
	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		return passthru.getNCharacterStream(columnIndex);
	}

	@Override
	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		return passthru.getNCharacterStream(columnLabel);
	}

	@Override
	public NClob getNClob(int columnIndex) throws SQLException {
		return passthru.getNClob(columnIndex);
	}

	@Override
	public NClob getNClob(String columnLabel) throws SQLException {
		return passthru.getNClob(columnLabel);
	}

	@Override
	public String getNString(int columnIndex) throws SQLException {
		return passthru.getNString(columnIndex);
	}

	@Override
	public String getNString(String columnLabel) throws SQLException {
		return passthru.getNString(columnLabel);
	}

	@Override
	public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
		return passthru.getObject(columnIndex, map);
	}

	@Override
	public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
		return passthru.getObject(columnLabel, map);
	}

	@Override
	public RowId getRowId(int columnIndex) throws SQLException {
		return passthru.getRowId(columnIndex);
	}

	@Override
	public RowId getRowId(String columnLabel) throws SQLException {
		return passthru.getRowId(columnLabel);
	}

	@Override
	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		return passthru.getSQLXML(columnIndex);
	}

	@Override
	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		return passthru.getSQLXML(columnLabel);
	}

	@Override
	public boolean isClosed() throws SQLException {
		return passthru.isClosed();
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
		passthru.updateAsciiStream(columnIndex, x);
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
		passthru.updateAsciiStream(columnLabel, x);
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
		passthru.updateAsciiStream(columnIndex, x, length);
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
		passthru.updateAsciiStream(columnLabel, x, length);
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
		passthru.updateBinaryStream(columnIndex, x);
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
		passthru.updateBinaryStream(columnLabel, x);
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
		passthru.updateBinaryStream(columnIndex, x, length);
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
		passthru.updateBinaryStream(columnLabel, x, length);
	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
		passthru.updateBlob(columnIndex, inputStream);
	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
		passthru.updateBlob(columnLabel, inputStream);
	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
		passthru.updateBlob(columnIndex, inputStream, length);
	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
		passthru.updateBlob(columnLabel, inputStream, length);
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
		passthru.updateCharacterStream(columnIndex, x);
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
		passthru.updateCharacterStream(columnLabel, reader);
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		passthru.updateCharacterStream(columnIndex, x, length);
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		passthru.updateCharacterStream(columnLabel, reader, length);
	}

	@Override
	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		passthru.updateClob(columnIndex, reader);
	}

	@Override
	public void updateClob(String columnLabel, Reader reader) throws SQLException {
		passthru.updateClob(columnLabel, reader);
	}

	@Override
	public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
		passthru.updateClob(columnIndex, reader, length);
	}

	@Override
	public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
		passthru.updateClob(columnLabel, reader, length);
	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
		passthru.updateNCharacterStream(columnIndex, x);
	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
		passthru.updateNCharacterStream(columnLabel, reader);
	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		passthru.updateNCharacterStream(columnIndex, x, length);
	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		passthru.updateNCharacterStream(columnLabel, reader, length);
	}

	@Override
	public void updateNClob(int columnIndex, NClob clob) throws SQLException {
		passthru.updateNClob(columnIndex, clob);
	}

	@Override
	public void updateNClob(String columnLabel, NClob clob) throws SQLException {
		passthru.updateNClob(columnLabel, clob);
	}

	@Override
	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		passthru.updateNClob(columnIndex, reader);
	}

	@Override
	public void updateNClob(String columnLabel, Reader reader) throws SQLException {
		passthru.updateNClob(columnLabel, reader);
	}

	@Override
	public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
		passthru.updateNClob(columnIndex, reader, length);
	}

	@Override
	public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
		passthru.updateNClob(columnLabel, reader, length);
	}

	@Override
	public void updateNString(int columnIndex, String str) throws SQLException {
		passthru.updateNString(columnIndex, str);
	}

	@Override
	public void updateNString(String columnLabel, String str) throws SQLException {
		passthru.updateNString(columnLabel, str);
	}

	@Override
	public void updateRowId(int columnIndex, RowId x) throws SQLException {
		passthru.updateRowId(columnIndex, x);
	}

	@Override
	public void updateRowId(String columnLabel, RowId x) throws SQLException {
		passthru.updateRowId(columnLabel, x);
	}

	@Override
	public void updateSQLXML(int columnIndex, SQLXML xmlObject)
			throws SQLException {
		passthru.updateSQLXML(columnIndex, xmlObject);
	}

	@Override
	public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
		passthru.updateSQLXML(columnLabel, xmlObject);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return passthru.isWrapperFor(iface);
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return passthru.unwrap(iface);
	}

	public String getDataSourceName() {
		// TODO Auto-generated method stub
		return null;
	}

}