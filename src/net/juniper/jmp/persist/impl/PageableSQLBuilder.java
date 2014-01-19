package net.juniper.jmp.persist.impl;

import net.juniper.jmp.core.ctx.Pageable;

public interface PageableSQLBuilder {
    public String build(String sql, Pageable pageable);
}
