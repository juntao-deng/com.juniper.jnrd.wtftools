package net.juniper.jmp.persist.mysql;

import net.juniper.jmp.persist.impl.PageableSQLBuilder;
import net.juniper.jmp.wtf.ctx.Pageable;

public class MySqlPageableSQLBuilder implements PageableSQLBuilder {

	@Override
	public String build(String sql, Pageable pageable) {
		sql = sql + " LIMIT " + (pageable.getPageIndex()) * pageable.getPageSize() + "," + pageable.getPageSize();
		return sql;
	}

}
