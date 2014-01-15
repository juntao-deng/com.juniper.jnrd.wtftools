package net.juniper.jmp.persist.jdbc.trans;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.juniper.jmp.persist.constant.DBConsts;
import net.juniper.jmp.persist.exp.UnSupportDbException;
import net.juniper.jmp.persist.jdbc.TranslatorObject;
import net.juniper.jmp.persist.mysql.TranslateToMysql;
import net.juniper.jmp.persist.oracle.TranslateToOracle;

public class SqlTranslator {
	private static Map<Integer, SqlTranslator> instanceMap = new ConcurrentHashMap<Integer, SqlTranslator>(2);
	public static SqlTranslator getInstance(Integer dbType){
		SqlTranslator trans = instanceMap.get(dbType);
		if(trans == null){
			synchronized(instanceMap){
				trans = instanceMap.get(dbType);
				if(trans == null){
					trans = new SqlTranslator(dbType);
					instanceMap.put(dbType, trans);
				}
			}
		}
		return trans;
	}
	
    private SqlTranslator(int dbType) {
        super();
        m_dbType = dbType;
        initTranslator(dbType);
    }

    public String getSql(String srcSql) throws java.sql.SQLException {
        int destDbType = getDestDbType();
        return getSql(srcSql, destDbType);
    }

    public String getSql(String srcSql, int destDbType) throws java.sql.SQLException {
        if (m_trans == null || m_trans.getDestDbType() != destDbType) {
            initTranslator(destDbType);
        }
        ((TranslatorObject) m_trans).setDbVersion(m_DbVersion);
        return getResultSql(srcSql);
    }


    /**
     * 返回变换时间。
     * 创建日期：(00-6-22 14:16:58)
     * @return long
     */
    public long getTransTime() {
        return m_lTime;
    }

    /**
     * 根据目标数据库类型,启动不同的翻译器
     * 缺省为sql server
     **/
    private void initTranslator(int dbType) {
        switch (dbType) {
	        case DBConsts.ORACLE:
	            m_trans = new TranslateToOracle();
	            break;
	        case DBConsts.MYSQL:
	        	m_trans = new TranslateToMysql();
	        	break;
	        default:
	        	throw new UnSupportDbException("not supported, for dbtype:" + dbType);
	    }
    }

    public void setTransFlag(boolean b) {
        m_bTranslate = b;
    }

    private boolean m_bTranslate = true;

    private int m_dbType = -1;

    public int getDestDbType() {
    	return m_dbType;
    }

    synchronized public String getResultSql(String srcSql) throws java.sql.SQLException {
        if (!m_bTranslate)
            return srcSql;
        m_lTime = System.currentTimeMillis();
        String sResult;
        try {

            if (srcSql == null)
                return "";

            srcSql = srcSql.trim();
            srcSql = trimPreChars(srcSql);
           

            m_trans.setSql(srcSql);
            sResult = m_trans.getSql();

          
        } catch (Exception e) {
            throw new java.sql.SQLException(e.getMessage());
        }

        //计算所用时间
        m_lTime = System.currentTimeMillis() - m_lTime;
        return sResult;
    }

    public String trimPreChars(String srcSql) throws java.sql.SQLException {
        if (srcSql == null || srcSql.length() < 1)
            return "";
        int pos = 0;
        String lineSep = System.getProperty("line.separator");
        while (pos < srcSql.length() && (srcSql.charAt(pos) == ' ' || srcSql.charAt(pos) == '\t' || lineSep.indexOf(srcSql.charAt(pos)) >= 0)) {
            pos++;
        }
        return srcSql.substring(pos);
    }

    //数据库版本
    private int m_DbVersion = 0;

    //是否需要翻译标志		
    private long m_lTime = 0;//翻译所需时间		

    private ITranslator m_trans = null;//翻译器

}