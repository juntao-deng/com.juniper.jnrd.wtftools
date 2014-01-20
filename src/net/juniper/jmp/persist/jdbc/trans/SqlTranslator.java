package net.juniper.jmp.persist.jdbc.trans;

import net.juniper.jmp.persist.constant.DBConsts;
import net.juniper.jmp.persist.derby.TranslateToDerby;
import net.juniper.jmp.persist.exp.UnSupportDbException;
import net.juniper.jmp.persist.mysql.TranslateToMysql;
import net.juniper.jmp.persist.oracle.TranslateToOracle;

public class SqlTranslator {
	public static ITranslator getInstance(Integer dbType){
		return initTranslator(dbType);
	}
	
    /**
     * get translator by db type
     * @param dbType
     */
    private static ITranslator initTranslator(int dbType) {
        switch (dbType) {
	        case DBConsts.ORACLE:
	            return new TranslateToOracle();
	        case DBConsts.MYSQL:
	        	return new TranslateToMysql();
	        case DBConsts.DERBY:
	        	return new TranslateToDerby();
	        default:
	        	throw new UnSupportDbException("not supported, for dbtype:" + dbType);
	    }
    }
}