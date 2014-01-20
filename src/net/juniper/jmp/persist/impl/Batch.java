package net.juniper.jmp.persist.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.juniper.jmp.persist.SQLParameter;
import net.juniper.jmp.persist.exp.JmpDbException;
import net.juniper.jmp.persist.jdbc.SQLHelper;
import net.juniper.jmp.persist.utils.DBUtil;


public class Batch {
	protected List<BatchStruct> batchStructs = new ArrayList<BatchStruct>();

	protected Map<String, PreparedStatement> cachedStatement = new HashMap<String, PreparedStatement>();

	protected Statement stmt = null;

	protected int batchSize = 1000;
	
	private Connection conn;
	
	public Batch(int batchSize, Connection conn) {
		this.batchSize = batchSize;
		this.conn = conn;
	}

	public void addBatch(String sql, SQLParameter[] pas) throws SQLException {
		batchStructs.add(new BatchStruct(sql, pas));
	}

	public void addBatch(String sql, SQLParameter pa) throws SQLException {
		batchStructs.add(new BatchStruct(sql, pa));
	}

	protected Statement getStatement(String sql, boolean prepare)
			throws SQLException {
		if (prepare) {
			PreparedStatement stmt = cachedStatement.get(sql);
			if (stmt == null) {
				stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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
						DBUtil.closeStmt(lastStmt);
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
			DBUtil.closeStmt(ps);
		}
		batchStructs.clear();
		DBUtil.closeStmt(stmt);
		stmt = null;
	}
	
}

/**
 * batch struct
 *
 */
class BatchStruct {
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