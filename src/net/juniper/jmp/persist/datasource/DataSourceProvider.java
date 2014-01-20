package net.juniper.jmp.persist.datasource;

import javax.sql.DataSource;

public interface DataSourceProvider {
	public DataSource getDataSource(String name);
}
