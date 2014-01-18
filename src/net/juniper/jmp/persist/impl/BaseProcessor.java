package net.juniper.jmp.persist.impl;

import net.juniper.jmp.persist.ResultSetProcessor;
import net.juniper.jmp.persist.exp.JmpDbException;
import net.juniper.jmp.persist.jdbc.CrossDBResultSet;
import net.juniper.jmp.persist.utils.DBUtil;

public abstract class BaseProcessor implements ResultSetProcessor {

    @Override
	public Object handleResultSet(CrossDBResultSet rs) throws JmpDbException {
        if (rs == null)
            throw new IllegalArgumentException("resultset can't be null");
        try {
            return processResultSet(rs);
        }
        finally {
            DBUtil.closeRs(rs);
        }

    }

    public abstract Object processResultSet(CrossDBResultSet rs) throws JmpDbException;

}
