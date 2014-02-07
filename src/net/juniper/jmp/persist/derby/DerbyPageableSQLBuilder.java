package net.juniper.jmp.persist.derby;

import net.juniper.jmp.persist.impl.PageableSQLBuilder;
import net.juniper.jmp.wtf.ctx.Pageable;

public class DerbyPageableSQLBuilder implements PageableSQLBuilder {

	@Override
	public String build(String sql, Pageable pageable) {
		sql = sql + " OFFSET " + (pageable.getPageIndex()) * pageable.getPageSize() + " ROWS FETCH NEXT " + pageable.getPageSize() + " ROWS ONLY";
		return sql;
	}

}
