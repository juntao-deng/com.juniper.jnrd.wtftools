package net.juniper.jmp.persist.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.juniper.jmp.persist.utils.SqlLogger;
/**
 * Statement decorator, translate sqls for assigned db before executing
 * @author juntaod
 *
 */
public class CrossDBStatement implements Statement{
	protected Statement passthru;
	protected CrossDBConnection conn;
	protected List<CrossDBResultSet> resultSets = Collections.synchronizedList(new ArrayList<CrossDBResultSet>());

	public CrossDBStatement() {
		super();
	}

	public CrossDBStatement(Statement passthru, CrossDBConnection conn) {
		this();
		this.conn = conn;
		this.passthru = passthru;
	}


	@Override
	public void addBatch(String sql) throws SQLException {
		String sqlFixed = SQLHelper.translate(sql, conn);
		passthru.addBatch(sqlFixed);
	}

	@Override
	public void cancel() throws SQLException {
		passthru.cancel();
	}
	
	@Override
	public void clearBatch() throws SQLException {
		passthru.clearBatch();
	}

	@Override
	public void clearWarnings() throws SQLException {
		passthru.clearWarnings();
	}

	@Override
	public void close() throws SQLException {
		closeResultSets();
		passthru.close();
		conn.deregisterStatement(this);
	}

	public void closeResultSets() {
		ResultSet[] rs = resultSets.toArray(new ResultSet[0]);
		for (int i = 0; i < rs.length; i ++) {
			if ((rs[i] instanceof CrossDBResultSet)) {
				try{
					((CrossDBResultSet) rs[i]).close();
				}
				catch(Throwable e){
					SqlLogger.error(e.getMessage(), e);
				}
			}
		}
	}

	protected void deregisterResultSet(CrossDBResultSet rs) {
		resultSets.remove(rs);
	}

	@Override
	public boolean execute(String sql) throws SQLException {
		String fixedSQL = SQLHelper.translate(sql, conn);
		return passthru.execute(fixedSQL);
	}

	@Override
	public int[] executeBatch() throws SQLException {
		int[] result = passthru.executeBatch();
		return result;
	}

	@Override
	public CrossDBResultSet executeQuery(String sql) throws SQLException {
		String sqlFixed = SQLHelper.translate(sql, conn);
		CrossDBResultSet rs = new CrossDBResultSet(passthru.executeQuery(sqlFixed), this);
		registerResultSet(rs);
		return rs;
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		String sqlFixed = SQLHelper.translate(sql, conn);
		int result = passthru.executeUpdate(sqlFixed);
		return result;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return conn;
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
	public int getMaxFieldSize() throws SQLException {
		return passthru.getMaxFieldSize();
	}

	@Override
	public int getMaxRows() throws SQLException {
		return passthru.getMaxRows();
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		return passthru.getMoreResults();
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		return passthru.getQueryTimeout();
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		CrossDBResultSet rs = new CrossDBResultSet(passthru.getResultSet(), this);
		return rs;
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		return passthru.getResultSetConcurrency();
	}

	@Override
	public int getResultSetType() throws SQLException {
		return passthru.getResultSetType();
	}


	@Override
	public int getUpdateCount() throws SQLException {
		return passthru.getUpdateCount();
	}

	@Override
	public java.sql.SQLWarning getWarnings() throws SQLException {
		return passthru.getWarnings();
	}

	protected void registerResultSet(CrossDBResultSet rs) {
		resultSets.add(rs);
	}

	@Override
	public void setCursorName(String name) throws SQLException {
		passthru.setCursorName(name);
	}

	@Override
	public void setEscapeProcessing(boolean b) throws SQLException {
		passthru.setEscapeProcessing(b);
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
	public void setMaxFieldSize(int size) throws SQLException {
		passthru.setMaxFieldSize(size);
	}

	@Override
	public void setMaxRows(int maxRows) throws SQLException {
		passthru.setMaxRows(maxRows);
	}

	@Override
	public void setQueryTimeout(int time) throws SQLException {
		passthru.setQueryTimeout(time);
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

	public Statement getRealStatement() {
		return passthru;
	}

	@Override
	public boolean isClosed() throws SQLException {
		return passthru.isClosed();
	}

	@Override
	public boolean isPoolable() throws SQLException {
		return passthru.isPoolable();
	}

	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		passthru.setPoolable(poolable);
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