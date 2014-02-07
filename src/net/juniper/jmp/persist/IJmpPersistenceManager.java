package net.juniper.jmp.persist;

import java.sql.DatabaseMetaData;
import java.util.List;

import net.juniper.jmp.persist.impl.DbSession;
import net.juniper.jmp.wtf.ctx.Page;
import net.juniper.jmp.wtf.ctx.Pageable;
import net.juniper.jmp.wtf.ctx.Sort;

public interface IJmpPersistenceManager{
	/**
	 * release the persistence
	 */
    public void release();

    public DbSession getDbSession();

    public <T>T insert(T entity);
    
    public <T>T[] insert(final List<T> entities);

    public <T>T[] insert(final T[] entities);

    public <T>T[] insertWithPK(List<T> entities);
    
    public <T>T insertWithPK(T entity);
    
    public <T>T[] insertWithPK(T[] entities);
    
    public <T>T update(final T entity);

    public <T>T update(final List<T> entities);

    public <T>T update(final T entity[]);

    public <T>T update(final T[] entity, String[] fieldNames);

    public <T>T update(final T[] entity, String[] fieldNames, String whereClause, SQLParameter param);

    public int delete(Object entity);

    public int delete(Object entity[]);

    public int delete(List<? extends Object> entities);

    public int deleteByPK(Class<?> clazz, Object pk);

    public int deleteByPKs(Class<?> clazz, Object[] pks);

    public int deleteByClause(Class<?> clazz, String wherestr);

    public int deleteByClause(Class<?> clazz, String wherestr, SQLParameter params);

    public <T>T findByPK(Class<T> clazz, Object pk);

    public <T>T findByPK(Class<T> clazz, Object pk, String[] selectedFields);

    public <T>List<T> findByEntityAttribute(T entity, Sort sort);
    
    public Object findByEntityAttribute(Object entity, ResultSetProcessor processor);

    public <T>List<T> findAll(Class<T> clazz, Sort sort);
    
    public <T>List<T> findAllByClause(Class<T> clazz, String condition, Sort sort);
    
    public <T>List<T> findAllByClause(Class<T> clazz, String condition, String[] fields, Sort sort);
    
    public <T>List<T> findAllByClause(Class<T> clazz, String condition, String[] fields, SQLParameter parameters, Sort sort);
    
    public <T>Page<T> findByEntityAttribute(Object entity, Sort sort, Pageable pageable);
    
    public <T>Page<T> findAll(Class<T> clazz, Sort sort, Pageable pageable);
    
    public <T>Page<T> findAllByClause(Class<T> clazz, String condition, String[] fields, Sort sort, Pageable pageable);
    
    public <T>Page<T> findAllByClause(Class<T> clazz, String condition, Sort sort, Pageable pageable);
    
    public <T>Page<T> findAllByClause(Class<T> clazz, String condition, String[] fields, SQLParameter parameters, Sort sort, Pageable pageable);
    
    public int getDBType();

    public void setSQLTranslator(boolean isTranslator);
   
    public DatabaseMetaData getMetaData();

    public String getCatalog();

    public String getSchema();

    public int getMaxRows();
    
    public void setMaxRows(int maxRows);

	public String getDataSourceName();
}
