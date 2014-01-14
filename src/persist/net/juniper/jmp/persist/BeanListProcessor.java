package net.juniper.jmp.persist;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BeanListProcessor extends BaseProcessor {
	private Class<?> type = null;

    public BeanListProcessor(Class<?> type) {
        this.type = type;
    }

    public Object processResultSet(ResultSet rs) throws SQLException {
        return ProcessorUtils.toBeanList(rs, type);
    }
}
