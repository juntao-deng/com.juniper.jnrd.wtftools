package net.juniper.jmp.persist;

import net.juniper.jmp.persist.exp.JmpDbException;
import net.juniper.jmp.persist.jdbc.CrossDBResultSet;

public interface ResultSetProcessor{
    public Object handleResultSet(CrossDBResultSet rs) throws JmpDbException;
}
