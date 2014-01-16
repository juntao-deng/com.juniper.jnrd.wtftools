package net.juniper.jmp.persist.datasource;

import javax.sql.DataSource;
import javax.sql.XADataSource;

public interface DataSourceProvider {
	public DataSource getDataSource(String name);
	public XADataSource getXaDataSource(String name);
}
