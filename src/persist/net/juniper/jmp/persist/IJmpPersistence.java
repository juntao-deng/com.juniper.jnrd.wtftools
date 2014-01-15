package net.juniper.jmp.persist;

import java.sql.DatabaseMetaData;
import java.util.List;

import net.juniper.jmp.persist.exp.JmpDbException;
import net.juniper.jmp.persist.ses.DbSession;

public interface IJmpPersistence {
	/**
	 * release the persistence
	 */
    public abstract void release();

    public abstract DbSession getDbSession();

    public abstract String insert(Object entity) throws JmpDbException;
    
    public abstract String[] insert(final List<? extends Object> entities) throws JmpDbException;

    public abstract String[] insert(final Object entities[]) throws JmpDbException;

    public abstract String[] insertWithPK(List<? extends Object> entities) throws JmpDbException;
    
    public abstract String insertWithPK(Object entity) throws JmpDbException;
    
    public abstract String[] insertWithPK(Object entities[]) throws JmpDbException;
    
    public abstract int update(final Object entity) throws JmpDbException;

    public abstract int update(final List<? extends Object> entities) throws JmpDbException;

    public abstract int update(final Object entity[]) throws JmpDbException;

    public abstract int update(final Object[] entity, String[] fieldNames) throws JmpDbException;

    public abstract int update(final Object[] entity, String[] fieldNames, String whereClause, SQLParameter param) throws JmpDbException;

    public abstract int delete(Object entity) throws JmpDbException;

    public abstract int delete(Object entity[]) throws JmpDbException;

    public abstract int delete(List<? extends Object> entities) throws JmpDbException;

    public abstract int deleteByPK(Class<?> clazz, String pk) throws JmpDbException;

    public abstract int deleteByPKs(Class<?> clazz, String[] pks) throws JmpDbException;

    public abstract int deleteByClause(Class<?> clazz, String wherestr) throws JmpDbException;

    public abstract int deleteByClause(Class<?> clazz, String wherestr, SQLParameter params) throws JmpDbException;

    public abstract Object retrieveByPK(Class<?> clazz, String pk) throws JmpDbException;

    public abstract Object retrieveByPK(Class<?> clazz, String pk, String[] selectedFields) throws JmpDbException;

    public abstract List<? extends Object> retrieve(Object entity) throws JmpDbException;

    public abstract Object retrieve(Object entity, ResultSetProcessor processor) throws JmpDbException;

    public abstract List<? extends Object> retrieveAll(Class<?> clazz) throws JmpDbException;

    public abstract List<? extends Object> retrieveByClause(Class<?> clazz, String condition, String[] fields) throws JmpDbException;

    public abstract List<? extends Object> retrieveByClause(Class<?> clazz, String condition) throws JmpDbException;
    
    public List<? extends Object> retrieveByClause(Class<?> className, String condition, String[] fields, SQLParameter parameters) throws JmpDbException;
    
    public abstract int getDBType();

    public abstract void setSQLTranslator(boolean isTranslator);
   
    public abstract DatabaseMetaData getMetaData();

    public abstract String getCatalog();

    public abstract String getSchema() throws JmpDbException;

    public int getMaxRows();
    
    public void setMaxRows(int maxRows);
}
