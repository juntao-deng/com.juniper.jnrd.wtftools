package net.juniper.jmp.persist.jdbc.trans;

import java.sql.SQLException;

/**
 * The translator for suitable db type
 * @author juntaod
 *
 */
public interface ITranslator {
	public String getResultSql(String srcSql) throws SQLException;
}