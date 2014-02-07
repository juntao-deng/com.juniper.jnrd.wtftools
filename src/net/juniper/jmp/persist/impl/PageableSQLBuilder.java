package net.juniper.jmp.persist.impl;

import net.juniper.jmp.wtf.ctx.Pageable;

public interface PageableSQLBuilder {
    public String build(String sql, Pageable pageable);
}
