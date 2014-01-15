package net.juniper.jmp.persist;

import java.sql.ResultSet;

import net.juniper.jmp.persist.exp.JmpDbException;
import net.juniper.jmp.persist.impl.BaseProcessor;
import net.juniper.jmp.persist.impl.MapProcessorHelper;

public class MapProcessor extends BaseProcessor  {
	public Object processResultSet(ResultSet rs) throws JmpDbException {
        return new MapProcessorHelper().toMap(rs);
    }
}

