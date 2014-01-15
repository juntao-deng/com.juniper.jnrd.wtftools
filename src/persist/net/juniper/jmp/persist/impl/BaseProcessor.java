package net.juniper.jmp.persist.impl;

import java.sql.ResultSet;

import net.juniper.jmp.persist.ResultSetProcessor;
import net.juniper.jmp.persist.exp.JmpDbException;
import net.juniper.jmp.persist.utils.DBUtil;

public abstract class BaseProcessor implements ResultSetProcessor {

    public Object handleResultSet(ResultSet rs) throws JmpDbException {
        if (rs == null)
            throw new IllegalArgumentException("resultset can't be null");
        try {
            return processResultSet(rs);
        }
        finally {
            DBUtil.closeRs(rs);
        }

    }

    public abstract Object processResultSet(ResultSet rs) throws JmpDbException;

}
