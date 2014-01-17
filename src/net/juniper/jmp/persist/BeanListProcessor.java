package net.juniper.jmp.persist;

import java.sql.ResultSet;

import net.juniper.jmp.persist.exp.JmpDbException;
import net.juniper.jmp.persist.impl.BaseProcessor;
import net.juniper.jmp.persist.impl.BeanListProcessorHelper;

public class BeanListProcessor extends BaseProcessor {
	private Class<?> type = null;

    public BeanListProcessor(Class<?> type) {
        this.type = type;
    }

    @Override
	public Object processResultSet(ResultSet rs) throws JmpDbException {
        return new BeanListProcessorHelper().toBeanList(rs, type);
    }
}
