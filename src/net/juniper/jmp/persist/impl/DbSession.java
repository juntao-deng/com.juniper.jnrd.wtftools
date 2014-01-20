package net.juniper.jmp.persist.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.juniper.jmp.persist.ResultSetProcessor;
import net.juniper.jmp.persist.SQLParameter;
import net.juniper.jmp.persist.exp.JmpDbException;
import net.juniper.jmp.persist.jdbc.CrossDBConnection;
import net.juniper.jmp.persist.jdbc.CrossDBPreparedStatement;
import net.juniper.jmp.persist.jdbc.CrossDBResultSet;
import net.juniper.jmp.persist.jdbc.CrossDBStatement;
import net.juniper.jmp.persist.jdbc.SQLHelper;
import net.juniper.jmp.persist.utils.DbExceptionHelper;
import net.juniper.jmp.persist.utils.SqlLogger;

public final class DbSession {
	private final int DEFAULT_BATCH_SIZE = 800;
	
	private CrossDBConnection conn = null;

	private int maxRows = -1;

	private int dbType = 0;

	private CrossDBPreparedStatement prepStatement = null;

	private CrossDBStatement statement = null;

	private String lastSQL = null;

	private Batch batch = null;

	private DatabaseMetaData dbmd = null;

	private int batchSize = DEFAULT_BATCH_SIZE;

	private int size = 0;

	private int batchRows = 0;
	
	private List<Object> pkList;

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
		CrossDBResultSet rs = null;
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
		CrossDBResultSet rs = null;
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

	/**
	 * This method is just like executeUpdate, but return inserted pks
	 * @param sql
	 * @param paramter
	 * @return
	 * @throws JmpDbException
	 */
	public Object[] executeInsert(String sql, SQLParameter parameter) throws JmpDbException{
		executeUpdate(sql, parameter);
		List<Object> pkList = new ArrayList<Object>();
		try {
			ResultSet rs = prepStatement.getGeneratedKeys();
			while(rs.next()){
				pkList.add(rs.getObject(1));
			}
			return pkList.toArray(new Object[0]);
		} 
		catch (SQLException e) {
			throw DbExceptionHelper.getException(dbType, e.getMessage(), e);
		}
		
	}
	
	public Object[] executeInsert(String sql) throws JmpDbException{
		executeUpdate(sql);
		List<Object> pkList = new ArrayList<Object>();
		try {
			ResultSet rs = statement.getGeneratedKeys();
			while(rs.next()){
				pkList.add(rs.getObject(1));
			}
			return pkList.toArray(new Object[0]);
		} 
		catch (SQLException e) {
			throw DbExceptionHelper.getException(dbType, e.getMessage(), e);
		}
	}
	
	public void addBatchInsert(String sql, SQLParameter parameters) throws JmpDbException {
		if (batch == null)
			batch = new InsertBatch(batchSize, conn);
		else if(!(batch instanceof InsertBatch))
			throw new JmpDbException("you can only add insert sql this batch time");
		try {
			batch.addBatch(sql, parameters);
			size ++;
			if (size % batchSize == 0) {
				if(pkList == null){
					pkList = new ArrayList<Object>();
				}
				Object[] pks = internalExecuteBatchInsert();
				if(pks != null)
					pkList.addAll(Arrays.asList(pks));
				size = 0;
			}
		} 
		catch (SQLException e) {
			throw DbExceptionHelper.getException(dbType, e.getMessage(), e);
		}
	}
	
	public Object[] executeBatchInsert() throws JmpDbException {
		try {
			Object[] pks = internalExecuteBatchInsert();
			if(pkList == null)
				return pks;
			else{
				if(pks != null)
					pkList.addAll(Arrays.asList(pks));
				return pkList.toArray(new Object[0]);
			}
		} 
		finally {
			if (batch != null) {
				batch.cleanupBatch();
				batch = null;
			}
		}
	}
	
	public int executeUpdate(String sql, SQLParameter parameter) throws JmpDbException {
		try {
			if ((!sql.equalsIgnoreCase(lastSQL)) || (prepStatement == null)) {
				if (prepStatement != null) {
					closeStmt(prepStatement);
				}
				prepStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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
			return statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
		} 
		catch (SQLException e) {
			throw DbExceptionHelper.getException(dbType, e.getMessage(), e);
		}
	}

	public void addBatch(String sql, SQLParameter parameters) throws JmpDbException {
		if (batch == null)
			batch = new Batch(batchSize, conn);
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
				batch = new Batch(batchSize, conn);
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
	
	private Object[] internalExecuteBatchInsert() throws JmpDbException {
		try {
			if (batch != null) {
				return ((InsertBatch)batch).executeBatchInsert();
			}
			return null;
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
