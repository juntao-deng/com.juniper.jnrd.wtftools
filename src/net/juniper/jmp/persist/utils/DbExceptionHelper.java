package net.juniper.jmp.persist.utils;

import java.sql.SQLException;

import net.juniper.jmp.persist.constant.DBConsts;
import net.juniper.jmp.persist.derby.JmpDerbyException;
import net.juniper.jmp.persist.exp.JmpDbException;
import net.juniper.jmp.persist.exp.UnSupportDbException;
import net.juniper.jmp.persist.mysql.JmpMySQLException;
import net.juniper.jmp.persist.oracle.JmpOracleException;

public class DbExceptionHelper {

	public static JmpDbException getException(int dbType, String msg, SQLException e) {
		switch (dbType) {
			case DBConsts.ORACLE:
				return new JmpOracleException(msg, e);
			case DBConsts.MYSQL:
				return new JmpMySQLException(msg, e);
			case DBConsts.DERBY:
				return new JmpDerbyException(msg, e);
			default:
				throw new UnSupportDbException("unsupported db:" + dbType);
		}
	}

	public static JmpDbException getException(int dbType, String msg) {
		switch (dbType) {
			case DBConsts.ORACLE:
				return new JmpOracleException(msg);
			case DBConsts.MYSQL:
				return new JmpMySQLException(msg);
			case DBConsts.DERBY:
				return new JmpDerbyException(msg);
			default:
				throw new UnSupportDbException("unsupported db:" + dbType);
		}
	}
	
	public static UnSupportDbException getUnsupportedException() {
		return new UnSupportDbException("un supported");
	}

	public static IllegalArgumentException getInvalidValueException(String msg) {
		return new IllegalArgumentException(msg);
	}
}
