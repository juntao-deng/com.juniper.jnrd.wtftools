package net.juniper.jmp.persist;

import java.sql.DatabaseMetaData;
import java.util.List;

import net.juniper.jmp.core.ctx.Pageable;
import net.juniper.jmp.core.ctx.Sort;
import net.juniper.jmp.persist.impl.DbSession;

public interface IJmpPersistenceManager{
	/**
	 * release the persistence
	 */
    public void release();

    public DbSession getDbSession();

    public Object insert(Object entity);
    
    public Object[] insert(final List<? extends Object> entities);

    public Object[] insert(final Object entities[]);

    public Object[] insertWithPK(List<? extends Object> entities);
    
    public Object insertWithPK(Object entity);
    
    public Object[] insertWithPK(Object entities[]);
    
    public int update(final Object entity);

    public int update(final List<? extends Object> entities);

    public int update(final Object entity[]);

    public int update(final Object[] entity, String[] fieldNames);

    public int update(final Object[] entity, String[] fieldNames, String whereClause, SQLParameter param);

    public int delete(Object entity);

    public int delete(Object entity[]);

    public int delete(List<? extends Object> entities);

    public int deleteByPK(Class<?> clazz, String pk);

    public int deleteByPKs(Class<?> clazz, String[] pks);

    public int deleteByClause(Class<?> clazz, String wherestr);

    public int deleteByClause(Class<?> clazz, String wherestr, SQLParameter params);

    public Object findByPK(Class<?> clazz, String pk);

    public Object findByPK(Class<?> clazz, String pk, String[] selectedFields);

    public List<? extends Object> findByEntityAttribute(Object entity, Sort sort);
    
    public List<? extends Object> findByEntityAttribute(Object entity, Sort sort, Pageable pageable);

    public Object findByEntityAttribute(Object entity, ResultSetProcessor processor);

    public List<? extends Object> findAll(Class<?> clazz, Sort sort);
    
    public List<? extends Object> findAll(Class<?> clazz, Sort sort, Pageable pageable);

    public List<? extends Object> findAllByClause(Class<?> clazz, String condition, String[] fields, Sort sort);
    
    public List<? extends Object> findAllByClause(Class<?> clazz, String condition, String[] fields, Sort sort, Pageable pageable);

    public List<? extends Object> findAllByClause(Class<?> clazz, String condition, Sort sort);
    
    public List<? extends Object> findAllByClause(Class<?> clazz, String condition, Sort sort, Pageable pageable);
    
    public List<? extends Object> findAllByClause(Class<?> className, String condition, String[] fields, SQLParameter parameters, Sort sort);
    
    public List<? extends Object> findAllByClause(Class<?> className, String condition, String[] fields, SQLParameter parameters, Sort sort, Pageable pageable);
    
    public int getDBType();

    public void setSQLTranslator(boolean isTranslator);
   
    public DatabaseMetaData getMetaData();

    public String getCatalog();

    public String getSchema();

    public int getMaxRows();
    
    public void setMaxRows(int maxRows);

	public String getDataSourceName();
}
