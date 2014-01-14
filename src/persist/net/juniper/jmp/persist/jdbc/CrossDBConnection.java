package net.juniper.jmp.persist.jdbc;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.Executor;

import net.juniper.jmp.persist.datasource.DataSourceCenter;
import net.juniper.jmp.persist.jdbc.trans.Adapter;
import net.juniper.jmp.persist.jdbc.trans.AdapterFactory;
import net.juniper.jmp.persist.jdbc.trans.SqlTranslator;
import net.juniper.jmp.persist.utils.DbExceptionHelper;



public class CrossDBConnection implements Connection {

	private String dataSource = null;

	private Adapter adapter = null;

	// 包装的connection
	private Connection passthru = null;

	// Statement缓冲
	private Vector<Statement> statements = new Vector<Statement>();

	// 数据库类型
	private int dbType = -1;

	// 数据库代码
	private String dbCode = null;

	// SQL转换器
	private SqlTranslator translator = null;

	// 是否关闭
	private boolean closed = false;

	// 在本连接中是否启动SQL翻译器
	private boolean enableTranslator = true;

	private DatabaseMetaData dbmd = null;

	// 翻译语句缓冲
	private LRUCache cache = null;

	/**
	 * @param conn
	 * @throws SQLException
	 */
	public CrossDBConnection(Connection conn, String dataSource) throws SQLException {
		super();
		this.dataSource = dataSource;
		DataSourceCenter sourceCenter = DataSourceCenter.getInstance();
		passthru = conn;
		dbType = sourceCenter.getDatabaseType(dataSource);
		cache = SQLCache.getInstance().getCache(dataSource);
		translator = new SqlTranslator(dbType);
		adapter = AdapterFactory.getAdapter(dbType, passthru);
		adapter.setNativeConn(passthru);
	}


	/**
	 * @param conn
	 * @param moduleLang
	 *          默认为0，如果不是0就进行内码转换
	 * @throws SQLException
	 */
	public CrossDBConnection(Connection conn) throws SQLException {
		this(conn, DataSourceCenter.getInstance().getSourceName());
	}

	public void clearWarnings() throws SQLException {
		try {
			// if (Trace.isEnabled())
			// Trace.trace(getId());
			passthru.clearWarnings();
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public void close() throws SQLException {
		try {
			closed = true;
			closeStatements();
			passthru.close();

		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public void commit() throws SQLException {
		try {
			passthru.commit();
		} 
		catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public Statement createStatement() throws SQLException {
		try {
			CrossDBStatement st = null;
			st = new CrossDBStatement(passthru.createStatement(), this, dbType, cache, dataSource);
			registerStatement(st);
			return st;
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		try {
			CrossDBStatement stat = new CrossDBStatement(passthru.createStatement(resultSetType, resultSetConcurrency), this, dbType, cache, dataSource);
			registerStatement(stat);
			return stat;
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}

	}

	public boolean getAutoCommit() throws SQLException {
		try {
			return passthru.getAutoCommit();
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public String getCatalog() throws SQLException {
		checkClosed();
		try {
			return passthru.getCatalog();
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	/**
	 * 获得metadata一个connection只获取一次
	 * 
	 * @return
	 * @throws SQLException
	 */
	public java.sql.DatabaseMetaData getMetaData() throws SQLException {
		checkClosed();
		try {
			if (dbmd == null) {
				dbmd = passthru.getMetaData();
			}
			return dbmd;
		} catch (SQLException e) {
			e.printStackTrace();
			throw translator.getSqlException(e);
		}
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public int getTransactionIsolation() throws SQLException {
		checkClosed();
		try {
			return passthru.getTransactionIsolation();
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public Map<String, Class<?>> getTypeMap() throws SQLException {
		checkClosed();
		return passthru.getTypeMap();
	}

	public java.sql.SQLWarning getWarnings() throws SQLException {
		checkClosed();
		try {
			return passthru.getWarnings();
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public boolean isClosed() throws SQLException {
		checkClosed();
		return passthru.isClosed();
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public boolean isReadOnly() throws SQLException {
		checkClosed();
		try {
			return passthru.isReadOnly();
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}

	}

	public String nativeSQL(String sql) throws SQLException {
		checkClosed();
		try {
			String sqlFixed = translate(sql);
			String val = passthru.nativeSQL(sqlFixed);
			return val;
		} 
		catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public CallableStatement prepareCall(String sql) throws SQLException {
		// if (Trace.isEnabled())
		// Trace.traceQuote(getId(), sql);
		checkClosed();
		try {
			String sqlFixed = translate(sql);
			CallableStatement s = passthru.prepareCall(sqlFixed);
			registerStatement(s);
			return s;
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		// if (Trace.isEnabled())
		// Trace.traceQuote(getId(), sql);
		checkClosed();
		try {

			String sqlFixed = translate(sql);
			// if (isJdbcOdbcBug())
			// sqlFixed = fixJdbcOdbcCharToByte(sqlFixed);
			CallableStatement s = passthru.prepareCall(sqlFixed);
			registerStatement(s);
			return s;
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		checkClosed();
		CrossDBPreparedStatement s = null;
		String sqlFixed = translate(sql);
		s = new CrossDBPreparedStatement(passthru.prepareStatement(sqlFixed), this, sqlFixed, dbType, cache, dataSource);
		s.setDbType(dbType);
		registerStatement(s);
		return s;
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		checkClosed();
		CrossDBPreparedStatement s = null;
		String sqlFixed = translate(sql);
		s = new CrossDBPreparedStatement(passthru.prepareStatement(sqlFixed, resultSetType, resultSetConcurrency), this, sqlFixed, dbType, cache, dataSource);
		s.setDbType(dbType);
		registerStatement(s);
		return s;

	}

	protected void registerStatement(Statement s) {
		statements.addElement(s);
		return;
	}

	public void rollback() throws SQLException {
		checkClosed();
		try {
			passthru.rollback();
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}

	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		try {
			passthru.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public void setCatalog(String arg0) throws SQLException {
		try {
			passthru.setCatalog(arg0);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public void setReadOnly(boolean arg0) throws SQLException {
		try {
			passthru.setReadOnly(arg0);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public void setSqlTrans(boolean b) {
		translator.setTransFlag(b);
	}

	public void setTransactionIsolation(int level) throws SQLException {
		try {
			passthru.setTransactionIsolation(level);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}


	public void enableSQLTranslator(boolean newBEnableSQLTranslator) {
		enableTranslator = newBEnableSQLTranslator;
	}

	public SqlTranslator getSqlTranslator() {
		return translator;
	}


	/**
	 * 此处插入方法说明。 创建日期：(2002-1-30 13:22:20)
	 * 
	 * @return boolean
	 */
	public boolean isSQLTranslatorEnabled() {
		return enableTranslator;
	}


	/**
	 * 此处插入方法说明。 创建日期：(2003-6-26 14:22:30)
	 * 
	 * @param i
	 *          int
	 */
	public void setDatabaseType(int i) {
		dbType = i;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.Connection#getHoldability()
	 */
	public int getHoldability() throws SQLException {
		checkClosed();
		try {
			return passthru.getHoldability();
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.Connection#setHoldability(int)
	 */
	public void setHoldability(int holdability) throws SQLException {
		try {
			passthru.setHoldability(holdability);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.Connection#setSavepoint()
	 */
	public Savepoint setSavepoint() throws SQLException {
		try {
			return passthru.setSavepoint();
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
	 */
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {

		try {
			passthru.releaseSavepoint(savepoint);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}

	}

	/*
	 * 
	 */
	public void rollback(Savepoint savepoint) throws SQLException {
		try {
			passthru.rollback(savepoint);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	/*
	 * 
	 */
	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		try {
			CrossDBStatement stat = new CrossDBStatement(passthru.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability), this, dbType, cache,
					dataSource);
			registerStatement(stat);
			return stat;
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		checkClosed();
		try {
			String sqlFixed = translate(sql);
			CallableStatement s = passthru.prepareCall(sqlFixed);
			registerStatement(s);
			return s;
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		checkClosed();
		CrossDBPreparedStatement s = null;
		String sqlFixed = translate(sql);
		s = new CrossDBPreparedStatement(passthru.prepareStatement(sqlFixed, resultSetType, resultSetConcurrency, resultSetHoldability), this, sqlFixed, dbType, cache, dataSource);
		s.setDbType(dbType);
		registerStatement(s);
		return s;
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	public Savepoint setSavepoint(String name) throws SQLException {
		try {
			return passthru.setSavepoint(name);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	/**
	 * 得到数据库类型
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int getDatabaseType() throws SQLException {
		return dbType;
	}

	/**
	 * @return
	 */
	public String getDbcode() {
		return dbCode;
	}

	protected String translate(String sql) throws SQLException {
		if (sql == null)
			return null;
		if (!isSQLTranslatorEnabled()) {
			return sql;
		}
		if (sql.length() > 8000) {
			String sqlFixed = translator.getSql(sql);
			return sqlFixed;
		}
		String key = sql;
		String result = (String) cache.getPreparedSQL(key);
		if (result != null) {
			return result;
		}

		sql = translator.getSql(sql);
		cache.putPreparedSQL(key, sql);
		return sql;
	}

	/**
	 * 关闭所有Statement
	 */
	private void closeStatements() {
		Object[] ss;
		synchronized (statements) {
			ss = new Object[statements.size()];
			statements.copyInto(ss);
		}
		for (int i = 0; i < ss.length; i++) {
			if (ss[i] == null || !(ss[i] instanceof CrossDBStatement)) {
			} else {
				try {
					((CrossDBStatement) ss[i]).close();
				} catch (SQLException e) {
				}
			}
		}
		statements = new Vector<Statement>();
	}

	/**
	 * 检查连接是否关闭
	 * 
	 * @throws SQLException
	 *           如果关闭抛出异常
	 */
	private void checkClosed() throws SQLException {
		if (closed)
			throw new SQLException("连接已经关闭");
	}

	/**
	 * @param s
	 */
	protected void deregisterStatement(CrossDBStatement s) {
		if (s == null) {
			return;
		}
		if (!statements.removeElement(s)) {
			return;
		}
	}

	protected Adapter getAdapter() {
		return adapter;
	}

	public Connection getPConnection() {
		return passthru;
	}

	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		try {
			return passthru.createArrayOf(typeName, elements);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public Blob createBlob() throws SQLException {
		try {
			return passthru.createBlob();
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	@Override
	public Clob createClob() throws SQLException {
		try {
			return passthru.createClob();
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	@Override
	public NClob createNClob() throws SQLException {
		try {
			return passthru.createNClob();
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		try {
			return passthru.createSQLXML();
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
		try {
			return passthru.createStruct(typeName, attributes);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		try {
			return passthru.getClientInfo();
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		try {
			return passthru.getClientInfo(name);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		try {
			return passthru.isValid(timeout);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	@Override
	public void setClientInfo(Properties properties) throws SQLClientInfoException {
		passthru.setClientInfo(properties);
	}

	@Override
	public void setClientInfo(String name, String value) throws SQLClientInfoException {

		passthru.setClientInfo(name, value);

	}

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		try {
			passthru.setTypeMap(map);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		try {
			return passthru.isWrapperFor(iface);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		try {
			return passthru.unwrap(iface);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public String getDataSource() {
		return dataSource;
	}


	@Override
	public void setSchema(String schema) throws SQLException {
		passthru.setSchema(schema);
	}


	@Override
	public String getSchema() throws SQLException {
		return passthru.getSchema();
	}


	@Override
	public void abort(Executor executor) throws SQLException {
		passthru.abort(executor);
	}


	@Override
	public void setNetworkTimeout(Executor executor, int milliseconds)
			throws SQLException {
		passthru.setNetworkTimeout(executor, milliseconds);
	}


	@Override
	public int getNetworkTimeout() throws SQLException {
		return passthru.getNetworkTimeout();
	}

}