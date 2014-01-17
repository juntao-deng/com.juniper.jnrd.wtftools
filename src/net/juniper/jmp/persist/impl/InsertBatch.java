package net.juniper.jmp.persist.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.juniper.jmp.persist.SQLParameter;
import net.juniper.jmp.persist.jdbc.SQLHelper;
import net.juniper.jmp.persist.utils.DBUtil;

public class InsertBatch extends Batch {

	public InsertBatch(int batchSize, Connection conn) {
		super(batchSize, conn);
	}
	/**
	 * this can only be executed when all sqls in the batch are insert sqls
	 * @return
	 * @throws SQLException
	 */
	public Object[] executeBatchInsert() throws SQLException {
		List<Object> pkList = new ArrayList<Object>();
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
					pkList.addAll(internalExecuteInsert(lastStmt));
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
						pkList.addAll(internalExecuteInsert(ps));
					}
				}
			} 
			else {
				now.addBatch(bs.sql);
				rbSize++;
				if (rbSize % batchSize == 0) {
					pkList.addAll(internalExecuteInsert(now));
				}

			}
		}

		if (lastStmt != null && rbSize % batchSize != 0) {
			pkList.addAll(internalExecuteInsert(lastStmt));
		}

		return pkList.toArray(new Object[0]);
	}
	
	private List<Object> internalExecuteInsert(Statement ps) throws SQLException {
		ps.executeBatch();
		ResultSet rs = ps.getGeneratedKeys();
		List<Object> pkList = new ArrayList<Object>();
		while(rs.next()){
			pkList.add(rs.getObject(1));
		}
		return pkList;
	}
}
