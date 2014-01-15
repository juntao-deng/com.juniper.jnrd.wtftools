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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import net.juniper.jmp.persist.jdbc.trans.SqlTranslator;
import net.juniper.jmp.persist.utils.DbExceptionHelper;
import net.juniper.jmp.persist.utils.SqlLogger;
/**
 * a connection decorator that hides the differences between all kinds of dbs.
 * @author juntaod
 *
 */
/**
 * a connection decorator that hides the differences between all kinds of dbs.
 * @author juntaod
 *
 */
public class CrossDBConnection implements Connection {
	/**
	 * DataSource name
	 */
	private String dsName = null;

	/**
	 * real connection
	 */
	private Connection passthru = null;

	private List<Statement> statements = Collections.synchronizedList(new ArrayList<Statement>());

	/**
	 * Database type
	 */
	private int dbType = -1;

	/**
	 * sql translator that trans the basic sql to adapter all kinds of databases
	 */
	private SqlTranslator translator;
	
	/**
	 * if starting sql translator
	 */
	private boolean enableTranslator = true;

	/**
	 * the connection's metadata
	 */
	private DatabaseMetaData dbmd = null;

	/**
	 * @param conn
	 * @throws SQLException
	 */
	public CrossDBConnection(Connection conn, String dsName, int dbType) throws SQLException {
		super();
		this.dsName = dsName;
		passthru = conn;
		this.dbType = dbType;
	}

	public void clearWarnings() throws SQLException {
		passthru.clearWarnings();
	}

	public void close() throws SQLException {
		closeStatements();
		passthru.close();
	}

	public void commit() throws SQLException {
		passthru.commit();
	}

	public Statement createStatement() throws SQLException {
		CrossDBStatement st = new CrossDBStatement(passthru.createStatement(), this);
		statements.add(st);
		return st;
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		CrossDBStatement stat = new CrossDBStatement(passthru.createStatement(resultSetType, resultSetConcurrency), this);
		statements.add(stat);
		return stat;
	}

	public boolean getAutoCommit() throws SQLException {
		return passthru.getAutoCommit();
	}

	public String getCatalog() throws SQLException {
		return passthru.getCatalog();
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		if (dbmd == null) {
			dbmd = passthru.getMetaData();
		}
		return dbmd;
	}

	public int getTransactionIsolation() throws SQLException {
		return passthru.getTransactionIsolation();
	}

	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return passthru.getTypeMap();
	}

	public java.sql.SQLWarning getWarnings() throws SQLException {
		return passthru.getWarnings();
	}

	public boolean isClosed() throws SQLException {
		return passthru.isClosed();
	}

	public boolean isReadOnly() throws SQLException {
		return passthru.isReadOnly();
	}

	@Override
	public String nativeSQL(String sql) throws SQLException {
		String sqlFixed = SQLHelper.translate(sql, this);
		String val = passthru.nativeSQL(sqlFixed);
		return val;
	}

	public CallableStatement prepareCall(String sql) throws SQLException {
		String sqlFixed = SQLHelper.translate(sql, this);
		CallableStatement s = passthru.prepareCall(sqlFixed);
		statements.add(s);
		return s;
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		String sqlFixed = SQLHelper.translate(sql, this);
		CallableStatement s = passthru.prepareCall(sqlFixed);
		statements.add(s);
		return s;
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		String sqlFixed = SQLHelper.translate(sql, this);
		CrossDBPreparedStatement st = new CrossDBPreparedStatement(passthru.prepareStatement(sqlFixed), this);
		statements.add(st);
		return st;
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		String sqlFixed = SQLHelper.translate(sql, this);
		CrossDBPreparedStatement st = new CrossDBPreparedStatement(passthru.prepareStatement(sqlFixed, resultSetType, resultSetConcurrency), this);
		statements.add(st);
		return st;

	}

	public String getDsName() {
		return dsName;
	}
	
	public void rollback() throws SQLException {
		passthru.rollback();
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		passthru.setAutoCommit(autoCommit);
	}

	public void setCatalog(String arg0) throws SQLException {
		passthru.setCatalog(arg0);
	}

	public void setReadOnly(boolean arg0) throws SQLException {
		passthru.setReadOnly(arg0);
	}

	public void setSqlTrans(boolean b) {
		translator.setTransFlag(b);
	}

	public void setTransactionIsolation(int level) throws SQLException {
		passthru.setTransactionIsolation(level);
	}


	public void enableSQLTranslator(boolean newBEnableSQLTranslator) {
		enableTranslator = newBEnableSQLTranslator;
	}

	public SqlTranslator getSqlTranslator() {
		return translator;
	}


	public boolean isSQLTranslatorEnabled() {
		return enableTranslator;
	}

	public int getHoldability() throws SQLException {
		return passthru.getHoldability();
	}

	public void setHoldability(int holdability) throws SQLException {
		passthru.setHoldability(holdability);
	}

	public Savepoint setSavepoint() throws SQLException {
		return passthru.setSavepoint();
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		passthru.releaseSavepoint(savepoint);
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		passthru.rollback(savepoint);
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		CrossDBStatement st = new CrossDBStatement(passthru.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability), this);
		statements.add(st);
		return st;
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		String sqlFixed = SQLHelper.translate(sql, this);
		CallableStatement st = passthru.prepareCall(sqlFixed);
		statements.add(st);
		return st;
	}

	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		String sqlFixed = SQLHelper.translate(sql, this);
		CrossDBPreparedStatement st = new CrossDBPreparedStatement(passthru.prepareStatement(sqlFixed, resultSetType, resultSetConcurrency, resultSetHoldability), this);
		statements.add(st);
		return st;
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	public Savepoint setSavepoint(String name) throws SQLException {
		return passthru.setSavepoint(name);
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	public int getDbType() {
		return dbType;
	}


	private void closeStatements() {
		Statement[] sts = statements.toArray(new Statement[0]);
		for (int i = 0; i < sts.length; i++) {
			if (sts[i] instanceof CrossDBStatement) {
				try{
					sts[i].close();
				}
				catch(Throwable e){
					SqlLogger.error(e.getMessage(), e);
				}
			}
		}
		statements.clear();
	}
	protected void deregisterStatement(CrossDBStatement st) {
		statements.remove(st);
	}

	public Connection getRealConnection() {
		return passthru;
	}

	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		return passthru.createArrayOf(typeName, elements);
	}

	public Blob createBlob() throws SQLException {
		return passthru.createBlob();
	}

	@Override
	public Clob createClob() throws SQLException {
		return passthru.createClob();
	}

	@Override
	public NClob createNClob() throws SQLException {
		return passthru.createNClob();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		return passthru.createSQLXML();
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
		return passthru.createStruct(typeName, attributes);
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		return passthru.getClientInfo();
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		return passthru.getClientInfo(name);
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		return passthru.isValid(timeout);
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
		passthru.setTypeMap(map);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return passthru.isWrapperFor(iface);
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return passthru.unwrap(iface);
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
	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
		passthru.setNetworkTimeout(executor, milliseconds);
	}


	@Override
	public int getNetworkTimeout() throws SQLException {
		return passthru.getNetworkTimeout();
	}

}