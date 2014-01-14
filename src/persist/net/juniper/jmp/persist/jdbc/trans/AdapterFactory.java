package net.juniper.jmp.persist.jdbc.trans;

import java.sql.Connection;
import java.sql.SQLException;

import net.juniper.jmp.persist.constant.DBConsts;
import net.juniper.jmp.persist.exp.UnSupportDbException;
import net.juniper.jmp.persist.oracle.OracleAdapter;

public abstract class AdapterFactory   {

	private AdapterFactory() {
	}

	static public Adapter getAdapter(int dbType, Connection realConnection) throws SQLException {
		Adapter adapter = null;
		switch (dbType) {
			case DBConsts.ORACLE:
				adapter = new OracleAdapter();
				break;
			default:
				throw new UnSupportDbException("unsupport, dbtype:" + dbType);
		}
		
		adapter.setNativeConn(realConnection);
		return adapter;
	}
}
