package net.juniper.jmp.persist.jdbc.trans;

import java.sql.SQLException;

public interface ITranslator {
	public int getDestDbType();

	public String getSourceSql();

	public String getSql() throws Exception;

	public SQLException getSqlException();

	public void setSql(String sql);

	public void setSqlException(java.sql.SQLException e);
}