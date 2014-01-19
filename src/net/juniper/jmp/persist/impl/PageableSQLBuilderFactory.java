package net.juniper.jmp.persist.impl;

import net.juniper.jmp.persist.constant.DBConsts;
import net.juniper.jmp.persist.exp.UnSupportDbException;
import net.juniper.jmp.persist.mysql.MySqlPageableSQLBuilder;
import net.juniper.jmp.persist.oracle.OraclePageableSQLBuilder;

public class PageableSQLBuilderFactory {
    private static PageableSQLBuilderFactory ourInstance = new PageableSQLBuilderFactory();

    public static PageableSQLBuilderFactory getInstance() {
        return ourInstance;
    }

    private PageableSQLBuilderFactory() {
    }

    public PageableSQLBuilder createLimitSQLBuilder(int dbType) {
        switch (dbType) {
        	case DBConsts.MYSQL:
        		return new MySqlPageableSQLBuilder();
            case DBConsts.ORACLE:
                return (new OraclePageableSQLBuilder());
            default:
                throw new UnSupportDbException("unsupported database:" + dbType);
        }
    }
}
