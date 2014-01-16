package net.juniper.jmp.persist.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.juniper.jmp.persist.ResultSetProcessor;
import net.juniper.jmp.persist.SQLParameter;
import net.juniper.jmp.persist.exp.JmpDbException;
import net.juniper.jmp.persist.jdbc.CrossDBConnection;
import net.juniper.jmp.persist.jdbc.SQLHelper;
import net.juniper.jmp.persist.utils.DbExceptionHelper;
import net.juniper.jmp.persist.utils.SqlLogger;

public final class DbSession {
	private final int DEFAULT_BATCH_SIZE = 800;
	
	private CrossDBConnection conn = null;

	private int maxRows = -1;

	private int dbType = 0;

	private PreparedStatement prepStatement = null;

	private Statement statement = null;

	private String lastSQL = null;

	private Batch batch = null;

	private DatabaseMetaData dbmd = null;

	private int batchSize = DEFAULT_BATCH_SIZE;

	private int size = 0;

	private int batchRows = 0;

	public DbSession(CrossDBConnection conn, int dbType) throws JmpDbException {
		this.dbType = dbType;
		this.conn = conn;
	}

	public void setSQLTranslatorEnabled(boolean isTranslator) {
		conn.enableSQLTranslator(isTranslator);
	}

	public void setAutoCommit(boolean autoCommit) throws JmpDbException {
		try {
			conn.setAutoCommit(autoCommit);
		} 
		catch (SQLException e) {
			throw new JmpDbException(e.getMessage(), e);
		}
	}

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}

	public int getMaxRows() {
		return maxRows;
	}

	public Object executeQuery(String sql, SQLParameter parameter, ResultSetProcessor processor) throws JmpDbException {
		Object result = null;
		ResultSet rs = null;
		try {
			if ((!sql.equalsIgnoreCase(lastSQL)) || (prepStatement == null)) {
				if (prepStatement != null) {
					closeStmt(prepStatement);
				}
				prepStatement = conn.prepareStatement(sql);
				lastSQL = sql;
			}
			else
				prepStatement.clearParameters();
			
			prepStatement.setMaxRows(maxRows > 0 ? maxRows : 0);
			
			if (parameter != null) {
				SQLHelper.setStatementParameter(prepStatement, parameter);
			}
			rs = prepStatement.executeQuery();
			result = processor.handleResultSet(rs);
		}

		catch (SQLException e) {
			throw DbExceptionHelper.getException(dbType, e.getMessage(), e);
		} 
		finally {
			closeRs(rs);
		}
		return result;
	}

	public Object executeQuery(String sql, ResultSetProcessor processor) throws JmpDbException {
		Object result = null;
		ResultSet rs = null;
		try {
			if (statement == null)
				statement = conn.createStatement();

			statement.setMaxRows(maxRows > 0 ? maxRows : 0);
			rs = statement.executeQuery(sql);
			result = processor.handleResultSet(rs);
		} 
		catch (SQLException e) {
			throw DbExceptionHelper.getException(dbType, e.getMessage(), e);
		} 
		finally {
			closeRs(rs);
		}
		return result;
	}

	public int executeUpdate(String sql, SQLParameter parameter) throws JmpDbException {
		try {
			if ((!sql.equalsIgnoreCase(lastSQL)) || (prepStatement == null)) {
				if (prepStatement != null) {
					closeStmt(prepStatement);
				}
				prepStatement = conn.prepareStatement(sql);
				lastSQL = sql;
			}
			else
				prepStatement.clearParameters();
			if (parameter != null) {
				SQLHelper.setStatementParameter(prepStatement, parameter);
			}
			return prepStatement.executeUpdate();
		} 
		catch (SQLException e) {
			throw DbExceptionHelper.getException(dbType, e.getMessage(), e);
		}
	}

	public int executeUpdate(String sql) throws JmpDbException {
		try {
			if (statement == null)
				statement = conn.createStatement();
			return statement.executeUpdate(sql);
		} 
		catch (SQLException e) {
			throw DbExceptionHelper.getException(dbType, e.getMessage(), e);
		}
	}

	public void addBatch(String sql, SQLParameter parameters) throws JmpDbException {
		if (batch == null)
			batch = new Batch();
		try {
			batch.addBatch(sql, parameters);
			size ++;
			if (size % batchSize == 0) {
				batchRows = batchRows + internalExecuteBatch();
				size = 0;
			}
		} 
		catch (SQLException e) {
			throw DbExceptionHelper.getException(dbType, e.getMessage(), e);
		}
	}

	public void addBatch(String sql, SQLParameter[] parametersArray) throws JmpDbException {
		try {
			if (batch == null)
				batch = new Batch();
			size = size + parametersArray.length;
			batch.addBatch(sql, parametersArray);
			if (size % batchSize == 0 || size > batchSize) {
				batchRows = batchRows + internalExecuteBatch();
				size = 0;
			}
		} 
		catch (SQLException e) {
			throw DbExceptionHelper.getException(dbType, e.getMessage(), e);
		}
	}

	public void addBatch(String sql) throws JmpDbException {
		addBatch(sql, (SQLParameter) null);
	}

	private int internalExecuteBatch() throws JmpDbException {
		try {
			int rows = 0;
			if (batch != null) {
				rows = batchRows + batch.executeBatch();
			}
			batchRows = 0;
			size = 0;
			return rows;
		} 
		catch (SQLException e) {
			throw DbExceptionHelper.getException(dbType, e.getMessage(), e);
		} 
	}

	public int executeBatch() throws JmpDbException {
		try {
			return internalExecuteBatch();
		} 
		finally {
			if (batch != null) {
				batch.cleanupBatch();
				batch = null;
			}
		}
	}


	public void closeAll() {
		closeStmt(statement);
		closeStmt(prepStatement);
		closeConnection(conn);
	}

	public DatabaseMetaData getMetaData() {
		if (dbmd == null){
			try {
				dbmd = conn.getMetaData();
			} 
			catch (SQLException e) {
				SqlLogger.error("get metadata error", e);
			}
		}
		return dbmd;
	}

	/**
	 * private batch struct
	 *
	 */
	private class BatchStruct {
		String sql = null;
		SQLParameter[] params;

		public BatchStruct(String sql, SQLParameter[] params) {
			this.sql = sql;
			this.params = params;
		}

		public BatchStruct(String sql, SQLParameter param) {
			this.sql = sql;
			if (param != null) {
				this.params = new SQLParameter[] { param };
			}
		}
	}

	/**
	 * private batch
	 *
	 */
	private class Batch {
		private List<BatchStruct> batchStructs = new ArrayList<BatchStruct>();

		private Map<String, PreparedStatement> cachedStatement = new HashMap<String, PreparedStatement>();

		private Statement stmt = null;

		public Batch() {
		}

		public void addBatch(String sql, SQLParameter[] pas) throws SQLException {
			batchStructs.add(new BatchStruct(sql, pas));
		}

		public void addBatch(String sql, SQLParameter pa) throws SQLException {
			batchStructs.add(new BatchStruct(sql, pa));
		}

		private Statement getStatement(String sql, boolean prepare)
				throws SQLException {
			if (prepare) {
				PreparedStatement stmt = cachedStatement.get(sql);
				if (stmt == null) {
					stmt = conn.prepareStatement(sql);
					cachedStatement.put(sql, stmt);
				}
				return stmt;
			} 
			else {
				if (stmt == null) {
					stmt = conn.createStatement();
				}
				return stmt;
			}
		}

		public int executeBatch() throws SQLException {
			int totalRowCount = 0;
			Iterator<BatchStruct> itr = batchStructs.iterator();
			int rbSize = 0;
			Statement lastStmt = null;
			String lastSql = null;
			while (itr.hasNext()) {
				BatchStruct bs = itr.next();
				itr.remove();
				Statement now = getStatement(bs.sql, bs.params != null);
				if (now != lastStmt) {
					if (lastStmt != null) {
						totalRowCount += internalExecute(lastStmt);
						rbSize = 0;
						if (now != stmt) {
							closeStmt(lastStmt);
							cachedStatement.remove(lastSql);
						}
					}
					lastStmt = now;
					lastSql = bs.sql;
				}
				if (bs.params != null) {
					PreparedStatement ps = (PreparedStatement) now;
					for (SQLParameter parameter : bs.params) {
						if (parameter != null) {
							SQLHelper.setStatementParameter(ps, parameter);
						}
						ps.addBatch();
						rbSize++;
						if (rbSize % batchSize == 0) {
							totalRowCount += internalExecute(ps);
						}
					}
				} 
				else {
					now.addBatch(bs.sql);
					rbSize++;
					if (rbSize % batchSize == 0) {
						totalRowCount += internalExecute(now);
					}

				}
			}

			if (lastStmt != null && rbSize % batchSize != 0) {
				totalRowCount += internalExecute(lastStmt);
			}

			return totalRowCount;
		}

		private int internalExecute(Statement ps) throws SQLException {
			int tc = 0;
			int[] rowCounts = ps.executeBatch();
			for (int j = 0; j < rowCounts.length; j++) {
				if (rowCounts[j] == Statement.SUCCESS_NO_INFO) {
				}
				else if (rowCounts[j] == Statement.EXECUTE_FAILED) {
				} 
				else {
					tc += rowCounts[j];
				}
			}
			return tc;

		}

		public void cleanupBatch() throws JmpDbException {
			Map<String, PreparedStatement> old = cachedStatement;
			cachedStatement = new HashMap<String, PreparedStatement>();
			for (PreparedStatement ps : old.values()) {
				closeStmt(ps);
			}
			batchStructs.clear();
			closeStmt(stmt);
			stmt = null;
		}
	}

	public CrossDBConnection getConnection() {
		return conn;
	}

	public int getDbType() {
		return dbType;
	}

	private void closeConnection(Connection con) {
		try {
			if (con != null) {
				con.close();
				con = null;
			}
		} 
		catch (SQLException e) {
			SqlLogger.error(e.getMessage(), e);
		}
	}

	private void closeStmt(Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} 
		catch (SQLException e) {
			SqlLogger.error(e.getMessage(), e);
		}
	}

	private void closeRs(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
		} 
		catch (SQLException e) {
			SqlLogger.error(e.getMessage(), e);
		}
	}
}
