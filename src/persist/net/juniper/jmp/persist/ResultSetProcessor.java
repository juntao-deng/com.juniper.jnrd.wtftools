package net.juniper.jmp.persist;

import java.sql.ResultSet;

import net.juniper.jmp.persist.exp.JmpDbException;

public interface ResultSetProcessor{
    public Object handleResultSet(ResultSet rs) throws JmpDbException;
}
