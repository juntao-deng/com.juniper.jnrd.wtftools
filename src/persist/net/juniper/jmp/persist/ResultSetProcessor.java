package net.juniper.jmp.persist;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetProcessor{
    public Object handleResultSet(ResultSet rs) throws SQLException;
}
