package net.juniper.jmp.persist.oracle;

import net.juniper.jmp.persist.impl.PageableSQLBuilder;
import net.juniper.jmp.wtf.ctx.Pageable;

public class OraclePageableSQLBuilder implements PageableSQLBuilder {
    @Override
	public String build(String sql, Pageable pageable) {
        StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
        pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
        pagingSelect.append(sql);
        pagingSelect.append(" ) row_ where rownum <= " + (pageable.getPageIndex() + 1) * pageable.getPageSize() + ") where rownum_ > " + (pageable.getPageIndex()) * pageable.getPageSize());
        return pagingSelect.toString();
    }
}
