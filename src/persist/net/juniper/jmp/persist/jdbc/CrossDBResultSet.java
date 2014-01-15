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
import java.util.Map;

import net.juniper.jmp.persist.jdbc.trans.SqlTranslator;
import net.juniper.jmp.persist.utils.DbExceptionHelper;
public class CrossDBResultSet implements ResultSet {
	private static int MAX_ROW = 300000;
	private ResultSet passthru;
	private CrossDBStatement stmt;

	private SqlTranslator trans = null;

	private int nRowCount = 0;


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

	public boolean absolute(int row) throws SQLException {
		return passthru.absolute(row);
	}

	public void afterLast() throws SQLException {
		passthru.afterLast();
	}

	public void beforeFirst() throws SQLException {
		passthru.beforeFirst();
	}

	public void cancelRowUpdates() throws SQLException {
		passthru.cancelRowUpdates();
	}

	public void clearWarnings() throws SQLException {
		passthru.clearWarnings();
	}

	public void close() throws SQLException {
		if (passthru != null) {
			passthru.close();
		}
	}

	public void deleteRow() throws SQLException {
		passthru.deleteRow();
	}

	public int findColumn(String arg0) throws SQLException {
		return passthru.findColumn(arg0);
	}

	public boolean first() throws SQLException {
		return passthru.first();
	}

	public Array getArray(int index) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	public Array getArray(String colName) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		return passthru.getAsciiStream(columnIndex);
	}

	public InputStream getAsciiStream(String columnName) throws SQLException {
		InputStream result = passthru.getAsciiStream(columnName);
		return result;
	}

	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		return passthru.getBigDecimal(columnIndex);
	}

	public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	public java.math.BigDecimal getBigDecimal(String columnName) throws SQLException {
		return passthru.getBigDecimal(columnName);
	}

	public java.math.BigDecimal getBigDecimal(String columnIndex, int scale) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	public java.io.InputStream getBinaryStream(int columnIndex)
			throws SQLException {
		InputStream result = passthru.getBinaryStream(columnIndex);
		return result;
	}

	public java.io.InputStream getBinaryStream(String columnName) throws SQLException {
		InputStream result = passthru.getBinaryStream(columnName);
		return result;
	}

	public Blob getBlob(int i) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	public Blob getBlob(String colName) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	public boolean getBoolean(int columnIndex) throws SQLException {
		boolean result = passthru.getBoolean(columnIndex);
		return result;
	}

	public boolean getBoolean(String columnName) throws SQLException {
		boolean result = passthru.getBoolean(columnName);
		return result;
	}

	public byte getByte(int columnIndex) throws SQLException {
		byte result = passthru.getByte(columnIndex);
		return result;
	}

	public byte getByte(String columnName) throws SQLException {
		try {

			byte result = passthru.getByte(columnName);
			// if (Trace.isDetailed())
			// resultString = resultString.append("|").append(result);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public byte[] getBytes(int columnIndex) throws SQLException {
		try {

			byte[] result = adapter.getBytes(passthru, columnIndex);
			// if (Trace.isDetailed())
			// resultString =
			// resultString.append("|").append(Trace.quoteObject(result));
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public byte[] getBytes(String columnName) throws SQLException {
		try {

			byte[] result = adapter.getBytes(passthru, columnName);
			// if (Trace.isDetailed())
			// resultString =
			// resultString.append("|").append(Trace.quoteObject(result));
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public java.io.Reader getCharacterStream(int columnIndex)
			throws SQLException {
		try {

			Reader result = adapter.getCharacterStream(passthru, columnIndex);
			// if (Trace.isDetailed())
			// resultString = resultString.append("|").append(result);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public java.io.Reader getCharacterStream(String columnName) throws SQLException {
		try {
			return adapter.getCharacterStream(passthru, columnName);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public Clob getClob(int columnIndex) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	public Clob getClob(String colName) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	public int getConcurrency() throws SQLException {
		return passthru.getConcurrency();
	}

	public String getCursorName() throws SQLException {
		return passthru.getCursorName();
	}

	public Date getDate(int arg0) throws SQLException {
		return passthru.getDate(arg0);
	}

	public Date getDate(int columnIndex, java.util.Calendar cal)
			throws SQLException {
		try {
			Date result = passthru.getDate(columnIndex, cal);
			// if (Trace.isDetailed())
			// resultString = resultString.append("|").append(result);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public Date getDate(String arg0) throws SQLException {
		try {
			Date result = passthru.getDate(arg0);
			// if (Trace.isDetailed())
			// resultString = resultString.append("|").append(result);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public Date getDate(String columnName, java.util.Calendar cal)
			throws SQLException {
		try {
			Date result = passthru.getDate(columnName, cal);
			// if (Trace.isDetailed())
			// resultString = resultString.append("|").append(result);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public double getDouble(int columnIndex) throws SQLException {
		try {
			double result = passthru.getDouble(columnIndex);
			// if (Trace.isDetailed())
			// resultString = resultString.append("|").append(result);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public double getDouble(String columnName) throws SQLException {
		try {

			double result = passthru.getDouble(columnName);
			// if (Trace.isDetailed())
			// resultString = resultString.append("|").append(result);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public int getFetchDirection() throws SQLException {
		try {
			return passthru.getFetchDirection();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public int getFetchSize() throws SQLException {
		try {
			return passthru.getFetchSize();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public float getFloat(int columnIndex) throws SQLException {
		try {

			float result = passthru.getFloat(columnIndex);
			// if (Trace.isDetailed())
			// resultString = resultString.append("|").append(result);
			return result;

		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public float getFloat(String columnName) throws SQLException {
		try {

			float result = passthru.getFloat(columnName);
			// if (Trace.isDetailed())
			// resultString = resultString.append("|").append(result);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public int getInt(int columnIndex) throws SQLException {
		try {
			return passthru.getInt(columnIndex);
			// if (Trace.isDetailed())
			// resultString = resultString.append("|").append(result);
			// return result;

		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public int getInt(String columnName) throws SQLException {
		try {

			return passthru.getInt(columnName);
			// if (Trace.isDetailed())
			// resultString = resultString.append("|").append(result);
			// return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public long getLong(int columnIndex) throws SQLException {
		try {

			long result = passthru.getLong(columnIndex);
			// if (Trace.isDetailed())
			// resultString = resultString.append("|").append(result);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public long getLong(String columnName) throws SQLException {
		try {
			long result = passthru.getLong(columnName);
			// if (Trace.isDetailed())
			// resultString = resultString.append("|").append(result);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	private ResultSetMetaData metaData = null;
	// todo
	public ResultSetMetaData getMetaData() throws SQLException {
		if(metaData==null){
			try {
				metaData = new CrossDBResultSetMetaData(passthru.getMetaData());
			} catch (SQLException e) {
				throw trans.getSqlException(e);
			}
		}
		return metaData;
		
	}

	public Object getObject(int index) throws SQLException {
		try {

			return adapter.getObject(passthru, index, getRSMD().getScale(index));
			// Object val= rs.getObject(index);
			// 
			// if (GU_CONVERT && val != null && val instanceof String)
			// val = noucFromUnicode((String) val);
			// 
			// return val;
		} catch (NegativeArraySizeException e) {
			return null;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public Object getObject(String columnName) throws SQLException {
		try {

			return adapter.getObject(passthru, columnName, getRSMD().getScale(
					getColumnIndex(columnName)));
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public Ref getRef(int columnIndex) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	public Ref getRef(String colName) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	public int getRow() throws SQLException {
		return passthru.getRow();
	}

	public short getShort(int columnIndex) throws SQLException {
		try {

			short result = passthru.getShort(columnIndex);
			// if (Trace.isDetailed())
			// resultString = resultString.append("|").append(result);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public short getShort(String columnName) throws SQLException {
		try {
			short result = passthru.getShort(columnName);
			// resultString = resultString.append("|").append(result);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public Statement getStatement() throws SQLException {
		return this.stmt;
	}

	public String getString(int columnIndex) throws SQLException {
		// String result;
		try {
			return passthru.getString(columnIndex);

			// if (GU_CONVERT && result != null)
			// result = noucFromUnicode(result);
			// if (Trace.isDetailed())
			// resultString = resultString.append("|").append(result);
			// return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public String getString(String columnName) throws SQLException {
		// String result;
		try {
			// if (stmt.isJdbcOdbcBug()) {
			// byte[] b = rs.getBytes(columnName);
			// result = new String(b);
			// } else {
			return passthru.getString(columnName);
			// }
			// if (GU_CONVERT && result != null)
			// result = noucFromUnicode(result);
			// if (Trace.isDetailed())
			// resultString = resultString.append("|").append(result);
			// return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public Time getTime(int columnIndex) throws SQLException {
		try {
			Time result = passthru.getTime(columnIndex);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public Time getTime(int columnIndex, java.util.Calendar cal)
			throws SQLException {
		try {
			Time result = passthru.getTime(columnIndex, cal);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}

	}

	public Time getTime(String arg0) throws SQLException {
		return passthru.getTime(arg0);
	}

	public Time getTime(String columnName, java.util.Calendar cal)
			throws SQLException {
		try {
			Time result = passthru.getTime(columnName, cal);
			// if (Trace.isDetailed())
			// resultString = resultString.append("|").append(result);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public Timestamp getTimestamp(int arg0) throws SQLException {
		try {
			Timestamp result = passthru.getTimestamp(arg0);
			// if (Trace.isDetailed())
			// resultString = resultString.append("|").append(result);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public Timestamp getTimestamp(int columnIndex,
			java.util.Calendar cal) throws SQLException {
		try {
			Timestamp result = passthru.getTimestamp(columnIndex, cal);
			// if (Trace.isDetailed())
			// resultString = resultString.append("|").append(result);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public Timestamp getTimestamp(String arg0) throws SQLException {
		try {
			Timestamp result = passthru.getTimestamp(arg0);
			// if (Trace.isDetailed())
			// resultString = resultString.append("|").append(result);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public Timestamp getTimestamp(String columnName,
			java.util.Calendar cal) throws SQLException {
		try {
			Timestamp result = passthru.getTimestamp(columnName, cal);
			// if (Trace.isDetailed())
			// resultString = resultString.append("|").append(result);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public int getType() throws SQLException {
		return passthru.getType();
	}

	public java.io.InputStream getUnicodeStream(int columnIndex)
			throws SQLException {

		throw DbExceptionHelper.getUnsupportedException();
	}

	public java.io.InputStream getUnicodeStream(String columnName)
			throws SQLException {

		throw DbExceptionHelper.getUnsupportedException();
	}

	public SQLWarning getWarnings() throws SQLException {
		try {
			return passthru.getWarnings();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void insertRow() throws SQLException {
		passthru.insertRow();
	}

	public boolean isAfterLast() throws SQLException {
		try {
			return passthru.isAfterLast();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public boolean isBeforeFirst() throws SQLException {
		try {
			return passthru.isBeforeFirst();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public boolean isFirst() throws SQLException {
		try {
			return passthru.isFirst();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public boolean isLast() throws SQLException {
		try {
			return passthru.isLast();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public boolean last() throws SQLException {
		try {
			return passthru.last();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void moveToCurrentRow() throws SQLException {
		try {
			passthru.moveToCurrentRow();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void moveToInsertRow() throws SQLException {
		try {
			passthru.moveToInsertRow();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public boolean next() throws SQLException {
		try {
			if (maxRows > 0 && nRowCount > maxRows) {
				throw new SQLException("一次性从数据库中读取数据过多: " + maxRows, "RSLMT:" + maxRows);

			} else if (nRowCount > MAX_ROW) {
				throw new SQLException("一次性从数据库中读取数据过多(-1): " + MAX_ROW, "RSLMT:" + MAX_ROW);
			}
			boolean hasNext = passthru.next();

			if (hasNext) {
				nRowCount++;
			} 

			return hasNext;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public boolean previous() throws SQLException {
		try {
			return passthru.previous();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void refreshRow() throws SQLException {
		try {
			passthru.refreshRow();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public boolean relative(int rows) throws SQLException {
		try {
			return passthru.relative(rows);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}

	}

	public boolean rowDeleted() throws SQLException {
		try {
			return passthru.rowDeleted();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public boolean rowInserted() throws SQLException {
		try {
			return passthru.rowInserted();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public boolean rowUpdated() throws SQLException {
		try {
			return passthru.rowUpdated();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void setFetchDirection(int direction) throws SQLException {
		try {
			passthru.setFetchDirection(direction);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void setFetchSize(int rows) throws SQLException {
		try {
			passthru.setFetchSize(rows);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void updateAsciiStream(int columnIndex, java.io.InputStream x,
			int length) throws SQLException {
		passthru.updateAsciiStream(columnIndex, x, length);

	}

	public void updateAsciiStream(String columnName, java.io.InputStream x,
			int length) throws SQLException {
		passthru.updateAsciiStream(columnName, x, length);

	}

	public void updateBigDecimal(int columnIndex, java.math.BigDecimal x)
			throws SQLException {
		passthru.updateBigDecimal(columnIndex, x);

	}

	public void updateBigDecimal(String columnName, java.math.BigDecimal x)
			throws SQLException {
		passthru.updateBigDecimal(columnName, x);

	}

	public void updateBinaryStream(int columnIndex, java.io.InputStream x,
			int length) throws SQLException {
		passthru.updateBinaryStream(columnIndex, x, length);

	}

	public void updateBinaryStream(String columnName, java.io.InputStream x,
			int length) throws SQLException {
		passthru.updateBinaryStream(columnName, x, length);

	}

	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		passthru.updateBoolean(columnIndex, x);

	}

	public void updateBoolean(String columnName, boolean x) throws SQLException {
		passthru.updateBoolean(columnName, x);

	}

	public void updateByte(int columnIndex, byte x) throws SQLException {
		passthru.updateByte(columnIndex, x);

	}

	public void updateByte(String columnName, byte x) throws SQLException {
		passthru.updateByte(columnName, x);

	}

	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		passthru.updateBytes(columnIndex, x);

	}

	public void updateBytes(String columnName, byte[] x) throws SQLException {
		passthru.updateBytes(columnName, x);

	}

	public void updateCharacterStream(int columnIndex, java.io.Reader x,
			int length) throws SQLException {
		passthru.updateCharacterStream(columnIndex, x, length);

	}

	public void updateCharacterStream(String columnName, java.io.Reader reader,
			int length) throws SQLException {
		passthru.updateCharacterStream(columnName, reader, length);

	}

	public void updateDate(int columnIndex, Date x)
			throws SQLException {
		passthru.updateDate(columnIndex, x);

	}

	public void updateDate(String columnName, Date x)
			throws SQLException {
		passthru.updateDate(columnName, x);
	}

	public void updateDouble(int columnIndex, double x) throws SQLException {
		passthru.updateDouble(columnIndex, x);

	}

	public void updateDouble(String columnName, double x) throws SQLException {
		passthru.updateDouble(columnName, x);

	}

	public void updateFloat(int columnIndex, float x) throws SQLException {
		passthru.updateFloat(columnIndex, x);

	}

	public void updateFloat(String columnName, float x) throws SQLException {

		passthru.updateFloat(columnName, x);
	}

	public void updateInt(int columnIndex, int x) throws SQLException {
		passthru.updateInt(columnIndex, x);

	}

	public void updateInt(String columnName, int x) throws SQLException {
		passthru.updateInt(columnName, x);

	}

	public void updateLong(int columnIndex, long x) throws SQLException {
		passthru.updateLong(columnIndex, x);

	}

	public void updateLong(String columnName, long x) throws SQLException {
		passthru.updateLong(columnName, x);

	}

	public void updateNull(int columnIndex) throws SQLException {
		passthru.updateNull(columnIndex);

	}

	public void updateNull(String columnName) throws SQLException {
		passthru.updateNull(columnName);

	}

	public void updateObject(int columnIndex, Object x) throws SQLException {
		passthru.updateObject(columnIndex, x);

	}

	public void updateObject(int columnIndex, Object x, int scale)
			throws SQLException {
		passthru.updateObject(columnIndex, x, scale);

	}

	public void updateObject(String columnName, Object x) throws SQLException {
		passthru.updateObject(columnName, x);

	}

	public void updateObject(String columnName, Object x, int scale)
			throws SQLException {
		passthru.updateObject(columnName, x, scale);

	}

	public void updateRow() throws SQLException {
		passthru.updateRow();

	}

	public void updateShort(int columnIndex, short x) throws SQLException {
		passthru.updateShort(columnIndex, x);
	}

	public void updateShort(String columnName, short x) throws SQLException {
		passthru.updateShort(columnName, x);

	}

	public void updateString(int columnIndex, String x) throws SQLException {
		passthru.updateString(columnIndex, x);

	}

	public void updateString(String columnName, String x) throws SQLException {
		passthru.updateString(columnName, x);

	}

	public void updateTime(int columnIndex, Time x)
			throws SQLException {
		passthru.updateTime(columnIndex, x);

	}

	public void updateTime(String columnName, Time x)
			throws SQLException {
		passthru.updateTime(columnName, x);

	}

	public void updateTimestamp(int columnIndex, Timestamp x)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	public void updateTimestamp(String columnName, Timestamp x)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	public boolean wasNull() throws SQLException {
		try {
			return passthru.wasNull();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	private ResultSetMetaData aRSMD = null;

	private String[] columnNames = null;

	public int getColumnIndex(String colName) throws SQLException {
		if (columnNames == null) {
			getRSMD();
		}
		int idx = 0;
		for (int i = 0; i < columnNames.length; i++) {
			if (columnNames[i].equalsIgnoreCase(colName)) {
				idx = i + 1;
				break;
			}
		}
		return idx;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-15 10:44:01)
	 * 
	 * @return ResultSetMetaData
	 */
	public ResultSetMetaData getRSMD() throws SQLException {
		if (aRSMD == null) {
			try {
				aRSMD = getMetaData();
				int nColumns = aRSMD.getColumnCount();
				if (nColumns <= 0)
					throw new SQLException("结果集中没有数据列!");
				columnNames = new String[nColumns];
				for (int i = 0; i < nColumns; i++) {
					columnNames[i] = aRSMD.getColumnName(i + 1);
				}
			} catch (SQLException e) {
				throw e;
			} catch (Exception e) {
				throw new SQLException(e.getMessage());
			}
		}
		return aRSMD;
	}

	/**
	 * 为了得到申请结果集的位置
	 */
	protected Exception eCreateResultSetException;

	/**
	 * 读取过多少行
	 */
	// private int nRowCount = 0;
	/**
	 * 此处插入方法说明。 创建日期：(2002-8-7 19:19:59)
	 * 
	 * @param index
	 *            int
	 * @return byte[]
	 */
	public byte[] getBlobBytes(int index) throws SQLException,
			java.io.IOException {
		try {
			return adapter.getBlobByte(passthru, index);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}

	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-10-28 16:13:01)
	 * 
	 * @param
	 * @return byte[]
	 */
	public byte[] getBlobBytes(String columnName) throws SQLException,
			java.io.IOException {
		try {
			return adapter.getBlobByte(passthru, columnName);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public String getClobStrings(int columnIndex) throws SQLException {
		try {
			String result = adapter.getClobString(passthru, columnIndex);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}

	}

	public String getClobStrings(String colName) throws SQLException {
		try {
			String result = adapter.getClobString(passthru, colName);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see ResultSet#getURL(int)
	 */
	public URL getURL(int columnIndex) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see ResultSet#updateArray(int, Array)
	 */
	public void updateArray(int columnIndex, Array x) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see ResultSet#updateBlob(int, Blob)
	 */
	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see ResultSet#updateClob(int, Clob)
	 */
	public void updateClob(int columnIndex, Clob x) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see ResultSet#updateRef(int, Ref)
	 */
	public void updateRef(int columnIndex, Ref x) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see ResultSet#getURL(java.lang.String)
	 */
	public URL getURL(String columnName) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see ResultSet#updateArray(java.lang.String, Array)
	 */
	public void updateArray(String columnName, Array x) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see ResultSet#updateBlob(java.lang.String, Blob)
	 */
	public void updateBlob(String columnName, Blob x) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see ResultSet#updateClob(java.lang.String, Clob)
	 */
	public void updateClob(String columnName, Clob x) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see ResultSet#updateRef(java.lang.String, Ref)
	 */
	public void updateRef(String columnName, Ref x) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	public int getMaxRows() {
		return maxRows;
	}

	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}

	@Override
	public int getHoldability() throws SQLException {
		return 0;
	}

	@Override
	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public NClob getNClob(int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public NClob getNClob(String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public String getNString(int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public String getNString(String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public Object getObject(int columnIndex, Map<String, Class<?>> map)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	@Override
	public Object getObject(String columnLabel, Map<String, Class<?>> map)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	@Override
	public RowId getRowId(int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public RowId getRowId(String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public boolean isClosed() throws SQLException {
		return this.passthru.isClosed();
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, long length)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x,
			long length) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream, long length)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream,
			long length) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	@Override
	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	@Override
	public void updateClob(String columnLabel, Reader reader)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	@Override
	public void updateClob(int columnIndex, Reader reader, long length)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	@Override
	public void updateClob(String columnLabel, Reader reader, long length)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	@Override
	public void updateNClob(int columnIndex, NClob clob) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void updateNClob(String columnLabel, NClob clob) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void updateNClob(String columnLabel, Reader reader)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void updateNClob(int columnIndex, Reader reader, long length)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void updateNClob(String columnLabel, Reader reader, long length)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void updateNString(int columnIndex, String string)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	@Override
	public void updateNString(String columnLabel, String string)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void updateRowId(int columnIndex, RowId x) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void updateRowId(String columnLabel, RowId x) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	@Override
	public void updateSQLXML(int columnIndex, SQLXML xmlObject)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	@Override
	public void updateSQLXML(String columnLabel, SQLXML xmlObject)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new SQLException("not supported");
	}

	@Override
	public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
		return null;
	}

	@Override
	public <T> T getObject(String columnLabel, Class<T> type)
			throws SQLException {
		return null;
	}
}