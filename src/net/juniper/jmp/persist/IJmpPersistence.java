package net.juniper.jmp.persist;

import java.sql.DatabaseMetaData;
import java.util.List;

import net.juniper.jmp.core.ctx.Pageable;
import net.juniper.jmp.core.ctx.Sort;
import net.juniper.jmp.persist.exp.JmpDbException;
import net.juniper.jmp.persist.impl.DbSession;

public interface IJmpPersistence{
	/**
	 * release the persistence
	 */
    public void release();

    public DbSession getDbSession();

    public String insert(Object entity) throws JmpDbException;
    
    public String[] insert(final List<? extends Object> entities) throws JmpDbException;

    public String[] insert(final Object entities[]) throws JmpDbException;

    public String[] insertWithPK(List<? extends Object> entities) throws JmpDbException;
    
    public String insertWithPK(Object entity) throws JmpDbException;
    
    public String[] insertWithPK(Object entities[]) throws JmpDbException;
    
    public int update(final Object entity) throws JmpDbException;

    public int update(final List<? extends Object> entities) throws JmpDbException;

    public int update(final Object entity[]) throws JmpDbException;

    public int update(final Object[] entity, String[] fieldNames) throws JmpDbException;

    public int update(final Object[] entity, String[] fieldNames, String whereClause, SQLParameter param) throws JmpDbException;

    public int delete(Object entity) throws JmpDbException;

    public int delete(Object entity[]) throws JmpDbException;

    public int delete(List<? extends Object> entities) throws JmpDbException;

    public int deleteByPK(Class<?> clazz, String pk) throws JmpDbException;

    public int deleteByPKs(Class<?> clazz, String[] pks) throws JmpDbException;

    public int deleteByClause(Class<?> clazz, String wherestr) throws JmpDbException;

    public int deleteByClause(Class<?> clazz, String wherestr, SQLParameter params) throws JmpDbException;

    public Object findByPK(Class<?> clazz, String pk) throws JmpDbException;

    public Object findByPK(Class<?> clazz, String pk, String[] selectedFields) throws JmpDbException;

    public List<? extends Object> findByEntityAttribute(Object entity, Sort sort) throws JmpDbException;
    
    public List<? extends Object> findByEntityAttribute(Object entity, Sort sort, Pageable pageable) throws JmpDbException;

    public Object findByEntityAttribute(Object entity, ResultSetProcessor processor) throws JmpDbException;

    public List<? extends Object> findAll(Class<?> clazz, Sort sort) throws JmpDbException;
    
    public List<? extends Object> findAll(Class<?> clazz, Sort sort, Pageable pageable) throws JmpDbException;

    public List<? extends Object> findAllByClause(Class<?> clazz, String condition, String[] fields, Sort sort) throws JmpDbException;
    
    public List<? extends Object> findAllByClause(Class<?> clazz, String condition, String[] fields, Sort sort, Pageable pageable) throws JmpDbException;

    public List<? extends Object> findAllByClause(Class<?> clazz, String condition, Sort sort) throws JmpDbException;
    
    public List<? extends Object> findAllByClause(Class<?> clazz, String condition, Sort sort, Pageable pageable) throws JmpDbException;
    
    public List<? extends Object> findAllByClause(Class<?> className, String condition, String[] fields, SQLParameter parameters, Sort sort) throws JmpDbException;
    
    public List<? extends Object> findAllByClause(Class<?> className, String condition, String[] fields, SQLParameter parameters, Sort sort, Pageable pageable) throws JmpDbException;
    
    public int getDBType();

    public void setSQLTranslator(boolean isTranslator);
   
    public DatabaseMetaData getMetaData();

    public String getCatalog();

    public String getSchema() throws JmpDbException;

    public int getMaxRows();
    
    public void setMaxRows(int maxRows);
}
