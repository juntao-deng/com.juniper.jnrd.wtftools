package net.juniper.jmp.persist.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import net.juniper.jmp.persist.jdbc.trans.Adapter;
import net.juniper.jmp.persist.jdbc.trans.SqlTranslator;
import net.juniper.jmp.persist.utils.DbExceptionHelper;

public class CrossDBStatement implements Statement{
	// 记数器
	static int counter;

	int id = counter++;

	protected Statement passthru;

	protected CrossDBConnection con;

	protected Vector<CrossDBResultSet> resultsets = new Vector<CrossDBResultSet>();

	SqlTranslator trans = null;

	private boolean sqlTranslator = true;

	int dbType = -1;

	protected Adapter adapter = null;

	private LRUCache cache;

	private String dataSource = null;

	private StringBuffer batchSql = new StringBuffer();

	int maxRows;

	/**
	 * UFStatement1 构造子注解。
	 */
	public CrossDBStatement() {
		super();
	}

	/**
	 * 
	 * @param passthru
	 * @param con
	 * @param dbType
	 * @param batchSupport
	 * @param odbcBug
	 * @param cache
	 */
	public CrossDBStatement(java.sql.Statement passthru, CrossDBConnection con, int dbType, LRUCache cache, String dataSource) {
		this();
		this.dataSource = dataSource;
		this.con = con;
		this.cache = cache;
		this.adapter = con.getAdapter();
		this.passthru = passthru;
		this.dbType = dbType;
		this.trans = con.getSqlTranslator();
		sqlTranslator = con.isSQLTranslatorEnabled();
	}

	public void supportHugeData() throws SQLException {
		adapter.supportHugeData(this);
	}

	public void addBatch(String sql) throws SQLException {
		try {
			String sqlFixed = translate(sql);
			passthru.addBatch(sqlFixed);
			batchSql.append(sqlFixed).append("; ");
		} 
		catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void cancel() throws SQLException {
		try {
			adapter.cancel(passthru);
		} 
		catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	/**
	 * 
	 * @throws SQLException
	 */
	public void clearBatch() throws SQLException {

		try {
			passthru.clearBatch();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}

	}

	/**
	 * 
	 * @throws SQLException
	 */
	public void clearWarnings() throws SQLException {
		try {
			passthru.clearWarnings();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	synchronized public void close() throws SQLException {
		try {
			closeResultSets();
			passthru.close();
			con.deregisterStatement(this);
		} 
		catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void closeResultSets() {
		Object[] rs;
		synchronized (resultsets) {
			rs = new Object[resultsets.size()];
			resultsets.copyInto(rs);
		}
		for (int i = 0; i < rs.length; i++) {
			if (rs[i] == null || !(rs[i] instanceof CrossDBResultSet)) {
			} else {
				try {
					((CrossDBResultSet) rs[i]).close();
				} catch (SQLException e) {
				}
			}
		}
		resultsets = new Vector<CrossDBResultSet>();
	}

	protected void deregisterResultSet(CrossDBResultSet r) {
		if (r == null) {
			return;
		}
		if (!resultsets.removeElement(r)) {
			return;
		}
	}

	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public boolean execute(String sql) throws SQLException {
		String fixedSQL = translate(sql);
		try {
			return passthru.execute(fixedSQL);
		} 
		catch (SQLException e) {
			throw trans.getSqlException(e);
		} 
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public int[] executeBatch() throws SQLException {
		int[] result = null;
		try {
			result = passthru.executeBatch();
			return result;
		} 
		catch (SQLException e) {
			throw trans.getSqlException(e);
		} 
		finally {
			this.batchSql = new StringBuffer();
		}
	}

	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public ResultSet executeQuery(String sql) throws SQLException {
		String sqlFixed = translate(sql);

		try {
			CrossDBResultSet r = new CrossDBResultSet(passthru
					.executeQuery(sqlFixed), this);
			r.setMaxRows(maxRows);
			registerResultSet(r);
			return r;
		} 
		catch (SQLException e) {
			throw trans.getSqlException(e);
		}
		
	}

	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public int executeUpdate(String sql) throws SQLException {
		String sqlFixed = translate(sql);
		try {
			int result = passthru.executeUpdate(sqlFixed);
			return result;
		} 
		catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public java.sql.Connection getConnection() throws SQLException {
		return con;
	}

	public int getDbType() {
		return dbType;
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

	public int getMaxFieldSize() throws SQLException {
		try {
			return passthru.getMaxFieldSize();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public int getMaxRows() throws SQLException {
		try {
			return passthru.getMaxRows();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public boolean getMoreResults() throws SQLException {
		try {
			return passthru.getMoreResults();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public int getQueryTimeout() throws SQLException {
		try {
			return passthru.getQueryTimeout();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public ResultSet getResultSet() throws SQLException {
		try {
			CrossDBResultSet rs = new CrossDBResultSet(passthru.getResultSet(), this);
			rs.setMaxRows(maxRows);
			return rs;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	/**
	 * JDBC 2.0
	 * <p/>
	 * Retrieves the result set concurrency.
	 */
	public int getResultSetConcurrency() throws SQLException {
		try {
			return passthru.getResultSetConcurrency();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	/**
	 * JDBC 2.0
	 * <p/>
	 * Determine the result set type.
	 */
	public int getResultSetType() throws SQLException {
		try {
			return passthru.getResultSetType();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public SqlTranslator getSqlTranslator() {
		return trans;
	}

	public int getUpdateCount() throws SQLException {
		try {
			return passthru.getUpdateCount();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public java.sql.SQLWarning getWarnings() throws SQLException {
		try {
			return passthru.getWarnings();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}


	/**
	 * 此处插入方法说明。 创建日期：(2002-1-30 13:27:44)
	 * 
	 * @return boolean
	 */
	protected boolean isSQLTranslatorEnabled() {
		return sqlTranslator;
	}

	protected void registerResultSet(CrossDBResultSet r) {
		resultsets.addElement(r);
	}

	public void setCursorName(String name) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	public void setDbType(int newDbType) {
		dbType = newDbType;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-29 20:35:10)
	 * 
	 * @return boolean
	 */
	public void setEscapeProcessing(boolean arg0) throws SQLException {
		try {
			passthru.setEscapeProcessing(arg0);
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

	public void setMaxFieldSize(int size) throws SQLException {
		try {
			passthru.setMaxFieldSize(size);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void setMaxRows(int rows) throws SQLException {
		try {
			if (rows < 0) {
				rows = 0;
			}
			passthru.setMaxRows(rows);
			maxRows = rows;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void setQueryTimeout(int time) throws SQLException {
		try {
			passthru.setQueryTimeout(time);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}


	protected String translate(String sql)
			throws SQLException {

		if (!isSQLTranslatorEnabled()) {
			return sql;
		}
		if (sql.length() > 5000) {
			String sqlFixed = trans.getSql(sql);
			return sqlFixed;
		}
		String key = sql;
		String result;
		result = (String) cache.getStatementSQL(key);
		if (result != null) {
			return result;
		}
		String sqlFixed = trans.getSql(sql);
		cache.putStatementSQL(key, sqlFixed);
		return sqlFixed;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.Statement#getResultSetHoldability()
	 */

	public int getResultSetHoldability() throws SQLException {
		return passthru.getResultSetHoldability();
		// throw DbExceptionHelper.getUnsupportedException();

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.Statement#getMoreResults(int)
	 */

	public boolean getMoreResults(int current) throws SQLException {
		return passthru.getMoreResults(current);
		// throw DbExceptionHelper.getUnsupportedException();

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.Statement#executeUpdate(java.lang.String, int)
	 */

	public int executeUpdate(String sql, int autoGeneratedKeys)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.Statement#execute(java.lang.String, int)
	 */

	public boolean execute(String sql, int autoGeneratedKeys)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.Statement#executeUpdate(java.lang.String, int[])
	 */

	public int executeUpdate(String sql, int[] columnIndexes)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.Statement#execute(java.lang.String, int[])
	 */

	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.Statement#getGeneratedKeys()
	 */

	public ResultSet getGeneratedKeys() throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.Statement#executeUpdate(java.lang.String,
	 * java.lang.String[])
	 */

	public int executeUpdate(String sql, String[] columnNames)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.Statement#execute(java.lang.String, java.lang.String[])
	 */

	public boolean execute(String sql, String[] columnNames)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}


	public Adapter getAdapter() {
		return adapter;
	}

	public Statement getVendorObject() {
		return passthru;
	}


	@Override
	public boolean isClosed() throws SQLException {
		try {
			return passthru.isClosed();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}

	}

	@Override
	public boolean isPoolable() throws SQLException {
		try {
			return passthru.isPoolable();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}

	}

	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		try {
			passthru.setPoolable(poolable);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}

	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		try {
			return passthru.isWrapperFor(iface);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		try {
			return passthru.unwrap(iface);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	protected String getDataSource() {
		return dataSource;
	}

	@Override
	public void closeOnCompletion() throws SQLException {
		this.passthru.closeOnCompletion();
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		return passthru.isCloseOnCompletion();
	}
}