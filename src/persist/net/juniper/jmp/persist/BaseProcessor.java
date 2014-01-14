package net.juniper.jmp.persist;

import java.sql.ResultSet;
import java.sql.SQLException;
import net.juniper.jmp.persist.utils.DBUtil;

public abstract class BaseProcessor implements ResultSetProcessor {

    public Object handleResultSet(ResultSet rs) throws SQLException {
        if (rs == null)
            throw new IllegalArgumentException("resultset can't be null");
        try {
            return processResultSet(rs);
        } 
        finally {
            DBUtil.closeRs(rs);
        }

    }

    public abstract Object processResultSet(ResultSet rs) throws SQLException;

}
