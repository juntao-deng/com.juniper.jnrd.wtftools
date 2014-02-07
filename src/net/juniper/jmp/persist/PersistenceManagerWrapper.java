package net.juniper.jmp.persist;

import java.sql.DatabaseMetaData;
import java.util.List;

import net.juniper.jmp.persist.impl.DbSession;
import net.juniper.jmp.wtf.ctx.Page;
import net.juniper.jmp.wtf.ctx.Pageable;
import net.juniper.jmp.wtf.ctx.Sort;
/**
 * It's a wrapper for persistence manager.So it can be used in CDI environment, and fetch the true instance from cache
 * @author Juntao
 *
 */
public class PersistenceManagerWrapper implements IJmpPersistenceManager {
  
  private IJmpPersistenceManager passthru;
  
  public PersistenceManagerWrapper(){
  }
  
  @Override
  public void release() {
      getPassthru().release();
  }

  private IJmpPersistenceManager getPassthru() {
      if(passthru == null)
          passthru = JmpPersistenceContext.getInstance();
      return passthru;
  }

  @Override
  public DbSession getDbSession() {
      return getPassthru().getDbSession();
  }

  @Override
  public <T> T insert(T entity) {
      return getPassthru().insert(entity);
  }

  @Override
  public <T> T[] insert(List<T> entities) {
      return getPassthru().insert(entities);
  }

  @Override
  public <T> T[] insert(T[] entities) {
      return getPassthru().insert(entities);
  }

  @Override
  public <T> T[] insertWithPK(List<T> entities) {
      return getPassthru().insertWithPK(entities);
  }

  @Override
  public <T> T insertWithPK(T entity) {
      return getPassthru().insertWithPK(entity);
  }

  @Override
  public <T> T[] insertWithPK(T[] entities) {
      return getPassthru().insertWithPK(entities);
  }

  @Override
  public <T> T update(T entity) {
      return getPassthru().update(entity);
  }

  @Override
  public <T> T update(List<T> entities) {
      return getPassthru().update(entities);
  }

  @Override
  public <T> T update(T[] entity) {
      return getPassthru().update(entity);
  }

  @Override
  public <T> T update(T[] entity, String[] fieldNames) {
      return getPassthru().update(entity, fieldNames);
  }

  @Override
  public <T> T update(T[] entity, String[] fieldNames, String whereClause, SQLParameter param) {
      return getPassthru().update(entity, fieldNames, whereClause, param);
  }

  @Override
  public int delete(Object entity) {
      return getPassthru().delete(entity);
  }

  @Override
  public int delete(Object[] entity) {
      return getPassthru().delete(entity);
  }

  @Override
  public int delete(List<? extends Object> entities) {
      return getPassthru().delete(entities);
  }

  @Override
  public int deleteByPK(Class<?> clazz, Object pk) {
      return getPassthru().deleteByPK(clazz, pk);
  }

  @Override
  public int deleteByPKs(Class<?> clazz, Object[] pks) {
      return getPassthru().deleteByPKs(clazz, pks);
  }

  @Override
  public int deleteByClause(Class<?> clazz, String wherestr) {
      return getPassthru().deleteByClause(clazz, wherestr);
  }

  @Override
  public int deleteByClause(Class<?> clazz, String wherestr, SQLParameter params) {
      return getPassthru().deleteByClause(clazz, wherestr, params);
  }

  @Override
  public <T> T findByPK(Class<T> clazz, Object pk) {
      return getPassthru().findByPK(clazz, pk);
  }

  @Override
  public <T> T findByPK(Class<T> clazz, Object pk, String[] selectedFields) {
      return getPassthru().findByPK(clazz, pk, selectedFields);
  }

  @Override
  public <T> List<T> findByEntityAttribute(T entity, Sort sort) {
      return getPassthru().findByEntityAttribute(entity, sort);
  }

  @Override
  public Object findByEntityAttribute(Object entity, ResultSetProcessor processor) {
      return getPassthru().findByEntityAttribute(entity, processor);
  }

  @Override
  public <T> List<T> findAll(Class<T> clazz, Sort sort) {
      return getPassthru().findAll(clazz, sort);
  }

  @Override
  public <T> List<T> findAllByClause(Class<T> clazz, String condition, Sort sort) {
      return getPassthru().findAllByClause(clazz, condition, sort);
  }

  @Override
  public <T> List<T> findAllByClause(Class<T> clazz, String condition, String[] fields, Sort sort) {
      return getPassthru().findAllByClause(clazz, condition, fields, sort);
  }

  @Override
  public <T> List<T> findAllByClause(Class<T> clazz, String condition, String[] fields, SQLParameter parameters, Sort sort) {
      return getPassthru().findAllByClause(clazz, condition, fields, parameters, sort);
  }

  @Override
  public <T> Page<T> findByEntityAttribute(Object entity, Sort sort, Pageable pageable) {
      return getPassthru().findByEntityAttribute(entity, sort, pageable);
  }

  @Override
  public <T> Page<T> findAll(Class<T> clazz, Sort sort, Pageable pageable) {
      return getPassthru().findAll(clazz, sort, pageable);
  }

  @Override
  public <T> Page<T> findAllByClause(Class<T> clazz, String condition, String[] fields, Sort sort, Pageable pageable) {
      return getPassthru().findAllByClause(clazz, condition, fields, sort, pageable);
  }

  @Override
  public <T> Page<T> findAllByClause(Class<T> clazz, String condition, Sort sort, Pageable pageable) {
      return getPassthru().findAllByClause(clazz, condition, sort, pageable);
  }

  @Override
  public <T> Page<T> findAllByClause(Class<T> clazz, String condition, String[] fields, SQLParameter parameters, Sort sort, Pageable pageable) {
      return getPassthru().findAllByClause(clazz, condition, fields, parameters, sort, pageable);
  }

  @Override
  public int getDBType() {
      return getPassthru().getDBType();
  }

  @Override
  public void setSQLTranslator(boolean isTranslator) {
      getPassthru().setSQLTranslator(isTranslator);
  }

  @Override
  public DatabaseMetaData getMetaData() {
      return getPassthru().getMetaData();
  }

  @Override
  public String getCatalog() {
      return getPassthru().getCatalog();
  }

  @Override
  public String getSchema() {
      return getPassthru().getSchema();
  }

  @Override
  public int getMaxRows() {
      return getPassthru().getMaxRows();
  }

  @Override
  public void setMaxRows(int maxRows) {
      getPassthru().setMaxRows(maxRows);
  }

  @Override
  public String getDataSourceName() {
      return getPassthru().getDataSourceName();
  }

}
