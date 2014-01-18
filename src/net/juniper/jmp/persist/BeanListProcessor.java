package net.juniper.jmp.persist;

import net.juniper.jmp.persist.exp.JmpDbException;
import net.juniper.jmp.persist.impl.BaseProcessor;
import net.juniper.jmp.persist.impl.BeanListProcessorHelper;
import net.juniper.jmp.persist.jdbc.CrossDBResultSet;

public class BeanListProcessor extends BaseProcessor {
	private Class<?> type = null;

    public BeanListProcessor(Class<?> type) {
        this.type = type;
    }

    @Override
	public Object processResultSet(CrossDBResultSet rs) throws JmpDbException {
        return new BeanListProcessorHelper().toBeanList(rs, type);
    }
}
