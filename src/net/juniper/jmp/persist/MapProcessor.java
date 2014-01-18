package net.juniper.jmp.persist;

import net.juniper.jmp.persist.exp.JmpDbException;
import net.juniper.jmp.persist.impl.BaseProcessor;
import net.juniper.jmp.persist.impl.MapProcessorHelper;
import net.juniper.jmp.persist.jdbc.CrossDBResultSet;

public class MapProcessor extends BaseProcessor  {
	@Override
	public Object processResultSet(CrossDBResultSet rs) throws JmpDbException {
        return new MapProcessorHelper().toMap(rs);
    }
}

