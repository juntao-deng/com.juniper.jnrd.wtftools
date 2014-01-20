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
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.juniper.jmp.persist.jdbc.trans.SqlTranslator;
import net.juniper.jmp.persist.utils.SqlLogger;
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

	@Override
	public void clearWarnings() throws SQLException {
		passthru.clearWarnings();
	}

	@Override
	public void close() throws SQLException {
		closeStatements();
		passthru.close();
	}

	@Override
	public void commit() throws SQLException {
		passthru.commit();
	}

	@Override
	public CrossDBStatement createStatement() throws SQLException {
		CrossDBStatement st = new CrossDBStatement(passthru.createStatement(), this);
		statements.add(st);
		return st;
	}

	@Override
	public CrossDBStatement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		CrossDBStatement stat = new CrossDBStatement(passthru.createStatement(resultSetType, resultSetConcurrency), this);
		statements.add(stat);
		return stat;
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		return passthru.getAutoCommit();
	}

	@Override
	public String getCatalog() throws SQLException {
		return passthru.getCatalog();
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		if (dbmd == null) {
			dbmd = passthru.getMetaData();
		}
		return dbmd;
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		return passthru.getTransactionIsolation();
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return passthru.getTypeMap();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return passthru.getWarnings();
	}

	@Override
	public boolean isClosed() throws SQLException {
		return passthru.isClosed();
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		return passthru.isReadOnly();
	}

	@Override
	public String nativeSQL(String sql) throws SQLException {
		String sqlFixed = SQLHelper.translate(sql, this);
		String val = passthru.nativeSQL(sqlFixed);
		return val;
	}

	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		String sqlFixed = SQLHelper.translate(sql, this);
		CallableStatement s = passthru.prepareCall(sqlFixed);
		statements.add(s);
		return s;
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		String sqlFixed = SQLHelper.translate(sql, this);
		CallableStatement s = passthru.prepareCall(sqlFixed);
		statements.add(s);
		return s;
	}

	@Override
	public CrossDBPreparedStatement prepareStatement(String sql) throws SQLException {
		String sqlFixed = SQLHelper.translate(sql, this);
		CrossDBPreparedStatement st = new CrossDBPreparedStatement(passthru.prepareStatement(sqlFixed), this);
		statements.add(st);
		return st;
	}

	@Override
	public CrossDBPreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		String sqlFixed = SQLHelper.translate(sql, this);
		CrossDBPreparedStatement st = new CrossDBPreparedStatement(passthru.prepareStatement(sqlFixed, resultSetType, resultSetConcurrency), this);
		statements.add(st);
		return st;
	}

	public String getDsName() {
		return dsName;
	}
	
	@Override
	public void rollback() throws SQLException {
		passthru.rollback();
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		passthru.setAutoCommit(autoCommit);
	}

	@Override
	public void setCatalog(String arg0) throws SQLException {
		passthru.setCatalog(arg0);
	}

	@Override
	public void setReadOnly(boolean arg0) throws SQLException {
		passthru.setReadOnly(arg0);
	}

	@Override
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

	@Override
	public int getHoldability() throws SQLException {
		return passthru.getHoldability();
	}

	@Override
	public void setHoldability(int holdability) throws SQLException {
		passthru.setHoldability(holdability);
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		return passthru.setSavepoint();
	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		passthru.releaseSavepoint(savepoint);
	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		passthru.rollback(savepoint);
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		CrossDBStatement st = new CrossDBStatement(passthru.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability), this);
		statements.add(st);
		return st;
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		String sqlFixed = SQLHelper.translate(sql, this);
		CallableStatement st = passthru.prepareCall(sqlFixed);
		statements.add(st);
		return st;
	}

	@Override
	public CrossDBPreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		String sqlFixed = SQLHelper.translate(sql, this);
		CrossDBPreparedStatement st = new CrossDBPreparedStatement(passthru.prepareStatement(sqlFixed, autoGeneratedKeys), this);
		statements.add(st);
		return st;
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		String sqlFixed = SQLHelper.translate(sql, this);
		CrossDBPreparedStatement st = new CrossDBPreparedStatement(passthru.prepareStatement(sqlFixed, resultSetType, resultSetConcurrency, resultSetHoldability), this);
		statements.add(st);
		return st;
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		return passthru.prepareStatement(sql, columnIndexes);
	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		return passthru.setSavepoint(name);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		return passthru.prepareStatement(sql, columnNames);
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

	@Override
	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		return passthru.createArrayOf(typeName, elements);
	}

	@Override
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

}