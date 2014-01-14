package net.juniper.jmp.persist;

import java.sql.DatabaseMetaData;
import java.util.List;

import net.juniper.jmp.persist.exp.JmpDbException;
import net.juniper.jmp.persist.jdbc.SQLParameter;
import net.juniper.jmp.persist.ses.DbSession;

public interface IJmpPersistence {
	/**
	 * release the persistence
	 */
    public abstract void release();

    public abstract DbSession getDbSession();

    public abstract String insert(Object vo) throws JmpDbException;
    
    public abstract String[] insert(final List<Object> vos) throws JmpDbException;

    public abstract String[] insert(final Object vo[]) throws JmpDbException;

    public abstract String[] insertWithPK(List<? extends Object> vos) throws JmpDbException;
    
    public abstract String insertWithPK(Object vo) throws JmpDbException;
    
    public abstract String[] insertWithPK(Object vos[]) throws JmpDbException;
    
    public abstract int update(final Object vo) throws JmpDbException;

    public abstract int update(final List<Object> vos) throws JmpDbException;

    public abstract int update(final Object vo[]) throws JmpDbException;

    public abstract int update(final Object[] vo, String[] fieldNames) throws JmpDbException;

    public abstract int update(final Object[] vo, String[] fieldNames, String whereClause, SQLParameter param) throws JmpDbException;

    public abstract int delete(Object vo) throws JmpDbException;

    public abstract int delete(Object vo[]) throws JmpDbException;

    public abstract int delete(List<Object> vos) throws JmpDbException;

    public abstract int deleteByPK(Class<? extends Object> className, String pk) throws JmpDbException;

    public abstract int deleteByPKs(Class<? extends Object> className, String[] pks) throws JmpDbException;

    public abstract int deleteByClause(Class<? extends Object> className, String wherestr) throws JmpDbException;

    public abstract int deleteByClause(Class<? extends Object> className, String wherestr, SQLParameter params) throws JmpDbException;

    public abstract Object retrieveByPK(Class<? extends Object> className, String pk) throws JmpDbException;

    public abstract Object retrieveByPK(Class<? extends Object> className, String pk, String[] selectedFields) throws JmpDbException;

    public abstract <M extends Object> List<M> retrieve(M vo) throws JmpDbException;

    public abstract Object retrieve(Object vo, ResultSetProcessor processor) throws JmpDbException;

    public abstract List<? extends Object> retrieveAll(Class<? extends Object> className) throws JmpDbException;

    public abstract List<? extends Object> retrieveByClause(Class<? extends Object> className, String condition, String[] fields) throws JmpDbException;

    public abstract List<? extends Object> retrieveByClause(Class<? extends Object> className, String condition) throws JmpDbException;

    public abstract int getDBType();

    public abstract void setSQLTranslator(boolean isTranslator);
   
    public abstract DatabaseMetaData getMetaData();

    public abstract String getCatalog();

    public abstract String getSchema() throws JmpDbException;

    public abstract void setReadOnly(boolean isReadOnly) throws JmpDbException;

    public int getMaxRows();
    
    public void setMaxRows(int maxRows);
}
