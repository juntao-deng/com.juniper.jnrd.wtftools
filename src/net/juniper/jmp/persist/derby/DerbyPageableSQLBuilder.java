package net.juniper.jmp.persist.derby;

import net.juniper.jmp.core.ctx.Pageable;
import net.juniper.jmp.persist.impl.PageableSQLBuilder;

public class DerbyPageableSQLBuilder implements PageableSQLBuilder {

	@Override
	public String build(String sql, Pageable pageable) {
		sql = sql + " OFFSET " + (pageable.getPageIndex()) * pageable.getPageSize() + " ROWS FETCH NEXT " + pageable.getPageSize() + " ROWS ONLY";
		return sql;
	}

}
