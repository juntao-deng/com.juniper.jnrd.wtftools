package net.juniper.jmp.persist.mysql;

import net.juniper.jmp.core.ctx.Pageable;
import net.juniper.jmp.persist.impl.PageableSQLBuilder;

public class MySqlPageableSQLBuilder implements PageableSQLBuilder {

	@Override
	public String build(String sql, Pageable pageable) {
		sql = sql + " LIMIT " + (pageable.getPageIndex()) * pageable.getPageSize() + "," + pageable.getPageSize();
		return sql;
	}

}
