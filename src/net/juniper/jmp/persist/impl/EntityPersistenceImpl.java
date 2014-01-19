package net.juniper.jmp.persist.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.juniper.jmp.core.ctx.PageResult;
import net.juniper.jmp.core.ctx.Pageable;
import net.juniper.jmp.core.ctx.Sort;
import net.juniper.jmp.exception.JMPRuntimeException;
import net.juniper.jmp.persist.BeanListProcessor;
import net.juniper.jmp.persist.IJmpPersistenceManager;
import net.juniper.jmp.persist.IReleaseCallback;
import net.juniper.jmp.persist.MapProcessor;
import net.juniper.jmp.persist.ResultSetProcessor;
import net.juniper.jmp.persist.SQLParameter;
import net.juniper.jmp.persist.constant.DBConsts;
import net.juniper.jmp.persist.datasource.DBMetaInfo;
import net.juniper.jmp.persist.datasource.DataSourceCenter;
import net.juniper.jmp.persist.datasource.PersistenceCtx;
import net.juniper.jmp.persist.exp.JmpDbException;
import net.juniper.jmp.persist.exp.JmpDbRuntimeException;
import net.juniper.jmp.persist.jdbc.CrossDBConnection;
import net.juniper.jmp.persist.jdbc.SQLHelper;

import org.apache.commons.beanutils.PropertyUtils;
import org.jboss.resteasy.logging.Logger;

public class EntityPersistenceImpl implements IJmpPersistenceManager{
	private Logger logger = Logger.getLogger(EntityPersistenceImpl.class);
	private DbSession session;
	private String dataSource = null;
	private DatabaseMetaData dbmd = null;
	private IReleaseCallback releaseCallback;
	private boolean released = false;
	public EntityPersistenceImpl(String dsName, IReleaseCallback callback) {
		if(dsName == null || dsName.equals(""))
			dsName = PersistenceCtx.getCurrentDatasource();
		this.dataSource = dsName;
		this.releaseCallback = callback;
		init();
	}
	
	private void init() {
		try{
			DataSource ds = DataSourceCenter.getInstance().getDataSource(this.dataSource);
			if(ds == null)
				throw new JmpDbRuntimeException("can not find datasource:" + ds);
			DBMetaInfo dbMeta = DataSourceCenter.getInstance().getDbMeta(this.dataSource);
			int dbType = dbMeta.getDbType();
			Connection conn = ds.getConnection();
			CrossDBConnection crossDbConn = new CrossDBConnection(conn, this.dataSource, dbType);
			session = new DbSession(crossDbConn, dbType);
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new JmpDbRuntimeException("error while get connection", e);
		}
	}
	
	@Override
	public DbSession getDbSession() {
		return session;
	}

	@Override
	public void release() {
		this.released = true;
		if (dbmd != null)
			dbmd = null;
		if (session != null) {
			session.closeAll();
			session = null;
		}
		
		releaseCallback.callback(this);
	}

	@Override
	public Object insertWithPK(final Object entity) {
		Object pk[] = insertWithPK(new Object[] {entity});
		return pk[0];
	}

	@Override
	public Object[] insertWithPK(final Object[] entities) {
		return insert(entities, true);
	}
	
	@Override
	public Object insert(final Object entity) {
		Object pk[] = insert(new Object[] {entity});
		return pk[0];
	}

	@Override
	public Object[] insertWithPK(final List<? extends Object> entities) {
		return insertWithPK(entities.toArray(new Object[0]));
	}

	@Override
	public Object[] insert(final List<? extends Object> entities) {
		return insert(entities.toArray(new Object[0]));
	}

	@Override
	public Object[] insert(final Object[] entities) {
		return insert(entities, false);
	}

	private Object[] getPks(final Object[] entities) {
		if(entities == null || entities.length == 0)
			return null;
		Object[] pks = new Object[entities.length];
		String pkField = PersistenceHelper.getPkField(this.dataSource, entities[0]);
		for (int i = 0; i < entities.length; i++) {
			try {
				pks[i] = PropertyUtils.getProperty(entities[i], pkField);
			} 
			catch (Throwable e){
				throw new JmpDbRuntimeException("error while fetch pk");
			}
		}
		return pks;
	}

	protected Object[] insert(final Object[] entities, boolean withPK) {
		checkSessionState();
		if(entities == null || entities.length == 0)
			return new String[0];
		
		try{
			Object entity = entities[0];
			
			String tableName = PersistenceHelper.getTableName(this.dataSource, entity);
			String names[] = PersistenceHelper.getInsertValidNames(this.dataSource, entity);
			String sql = SQLHelper.getInsertSQL(tableName, names);
	
			Object[] pks = null;
			if(withPK){
				pks = getPks(entities);
			}
			
			if (entities.length == 1) {
				SQLParameter parameter = SQLHelper.getSQLParam(this.dataSource, entity, names);
				Object[] returnPks = session.executeInsert(sql, parameter);
				if(!withPK){
					pks = returnPks;
					fillEntities(entities, pks);
				}
			} 
			else {
				SQLParameter[] parameters = new SQLParameter[entities.length];
				for (int i = 0; i < entities.length; i++) {
					if (entities[i] == null)
						continue;
					parameters[i] = SQLHelper.getSQLParam(this.dataSource, entities[i], names);
				}
				session.addBatch(sql, parameters);
				Object[] returnPks = session.executeBatchInsert();
				if(!withPK){
					pks = returnPks;
					fillEntities(entities, pks);
				}
			}
			return pks;
		}
		catch(JmpDbException e){
			throw new JMPRuntimeException("error while inserting entities", e);
		}
	}

	private void checkSessionState() {
		if(released)
			throw new JmpDbRuntimeException("The instance of PersistenceManager has already been released");
		if(session == null)
			init();
	}

	private void fillEntities(Object[] entities, Object[] pks) {
		if(entities.length != pks.length)
			throw new JmpDbRuntimeException("the count of entities is not equal to pks");
		//should query by pks again, and fetch version information and merge into the entities.
		String pkField = PersistenceHelper.getPkField(this.dataSource, entities[0]);
		for(int i = 0; i < entities.length; i ++){
			try {
				PropertyUtils.setProperty(entities[i], pkField, pks[i]);
			} 
			catch (Exception e) {
				throw new JmpDbRuntimeException("error while setting pk to entities");
			}
		}
	}

	@Override
	public int update(final Object entity) {
		if (entity == null) {
			throw new IllegalArgumentException("entity can not be null");
		}
		return update(new Object[] {entity}, null);
	}

	@Override
	public int update(final List<? extends Object> entities) {
		return update(entities.toArray(new Object[0]), null);

	}

	@Override
	public int update(final Object[] vo) {
		return update(vo, null);

	}

	@Override
	public int update(final Object[] vo, String[] fieldNames) {
		return update(vo, fieldNames, null, null);
	}

	@Override
	public int update(final Object[] entities, String[] fieldNames, String whereClause, SQLParameter param) {
		if (entities == null || entities.length == 0)
			return 0;
		
		try{
			Object entity = entities[0];
			int row = 0;
			String tableName = PersistenceHelper.getTableName(this.dataSource, entity);
			String pkName = PersistenceHelper.getPkField(this.dataSource, entity);
			String[] names;
			if (fieldNames != null) {
				names = fieldNames;
			} 
			else {
				names = PersistenceHelper.getUpdateValidNames(this.dataSource, entity);
			}
			
			Object pk = PersistenceHelper.getPrimaryKey(this.dataSource, entity);
			String sql = SQLHelper.getUpdateSQL(tableName, names, pkName);
			if (entities.length == 1) {
				SQLParameter parameter = SQLHelper.getSQLParam(this.dataSource, entity, names);
				parameter.addParam(pk);
				if (whereClause == null)
					row = session.executeUpdate(sql, parameter);
				else {
					addParameter(parameter, param);
					row = session.executeUpdate(sql + " and " + whereClause, parameter);
				}
			} 
			else {
				for (int i = 0; i < entities.length; i++) {
					if (entities[i] == null)
						continue;
					SQLParameter parameter = SQLHelper.getSQLParam(this.dataSource, entity, names);
					parameter.addParam(pk);
					if (whereClause == null)
						session.addBatch(sql, parameter);
					else {
						addParameter(parameter, param);
						session.addBatch(sql + " and " + whereClause, parameter);
					}
				}
				row = session.executeBatch();
			}
			return row;
		}
		catch(JmpDbException e){
			throw new JmpDbRuntimeException("error while updating entities", e);
		}
	}


	private void addParameter(SQLParameter parameter, SQLParameter addParams) {
		if (addParams != null){
			int count = addParams.getCount();
			for (int i = 0; i < count; i++) {
				parameter.addParam(addParams.get(i));
			}
		}
	}

	@Override
	public int delete(final List<? extends Object> entities) {
		return delete(entities.toArray(new Object[] {}));
	}

	@Override
	public int delete(final Object entity) {
		return delete(new Object[] { entity });
	}

	@Override
	public int delete(final Object entities[]) {
		if (entities.length == 0)
			return 0;
		try{
			Object entity = entities[0];
			String tableName = PersistenceHelper.getTableName(this.dataSource, entity);
			String pkField = PersistenceHelper.getPkField(this.dataSource, entity);
			String sql = SQLHelper.getDeleteByPKSQL(tableName, pkField);
	
			for (int i = 0; i < entities.length; i++) {
				if (entities[i] == null)
					continue;
				SQLParameter parameter = new SQLParameter();
				Object pk = PersistenceHelper.getPrimaryKey(this.dataSource, entities[i]);
				parameter.addParam(pk);
				session.addBatch(sql, parameter);
			}
			return session.executeBatch();
		}
		catch(JmpDbException e){
			throw new JmpDbRuntimeException("error while deleting entities", e);
		}
	}

	@Override
	public int deleteByPK(Class<?> className, String pk) {
		return deleteByPKs(className, new String[] { pk });
	}

	@Override
	public int deleteByPKs(Class<?> clazz, String[] pks) {
		try{
			String tableName = PersistenceHelper.getTableName(this.dataSource, clazz);
			String pkField = PersistenceHelper.getPkField(this.dataSource, clazz);
			String sql = "DELETE FROM " + tableName + " WHERE " + pkField + "=?";
			for (int i = 0; i < pks.length; i++) {
				SQLParameter parameter = new SQLParameter();
				parameter.addParam(pks[i]);
				session.addBatch(sql, parameter);
			}
			return session.executeBatch();
		}
		catch(JmpDbException e){
			throw new JmpDbRuntimeException("error while deleting entities by pk", e);
		}
	}


	@Override
	public int deleteByClause(Class<?> className, String wherestr) {
		return deleteByClause(className, wherestr, null);
	}

	@Override
	public int deleteByClause(Class<?> clazz, String whereStr, SQLParameter params) {
		try{
			String tableName = PersistenceHelper.getTableName(this.dataSource, clazz);
			String sql = new StringBuffer().append("DELETE FROM ").append(tableName).toString();
			if (whereStr != null) {
				whereStr = whereStr.trim();
				if (whereStr.length() > 0) {
					if (whereStr.toLowerCase().startsWith("WHERE"))
						sql += " " + whereStr;
				}
			}
			if (params == null)
				return session.executeUpdate(sql);
			else
				return session.executeUpdate(sql, params);
		}
		catch(JmpDbException e){
			throw new JmpDbRuntimeException("error while deleting by clause", e);
		}
	}

	@Override
	public <T>T findByPK(Class<T> clazz, String pk) {
		return findByPK(clazz, pk, null);
	}

	@Override
	public <T>T findByPK(Class<T> clazz, String pk, String[] selectedFields) {
		if (pk == null)
			return null;
		
		SQLParameter param = new SQLParameter();
		param.addParam(pk);
		
		String pkField = PersistenceHelper.getPkField(this.dataSource, clazz);
		List<T> results = findAllByClause(clazz, pkField + "=?", selectedFields, param, null);
		if (results.size() >= 1)
			return results.get(0);
		return null;
	}

	@Override
	public List<? extends Object> findByEntityAttribute(Object entity, Sort sort) {
		return null;
	}
	
	@Override
	public PageResult<? extends Object> findByEntityAttribute(Object entity, Sort sort, Pageable pageable){
//		String tableName = PersistenceHelper.getTableName(this.dataSource, entity);
//		Map types = getColmnTypes(tableName);
//		// 得到合法的字段列表
//		String names[] = getNotNullValidNames(vo, types);
//		// 得到插入的SQL语句
//		String sql = SQLHelper.getSelectSQL(tableName, names, true, null);
//		SQLParameter param = getSQLParam(entity, names);
//		// session.setReadOnly(true);
//		return session.executeQuery(sql, param, processor);
		return null;
	}
	
	@Override
	public Object findByEntityAttribute(Object entity, ResultSetProcessor processor) {
		return null;
	}
	
	@Override
	public <T>List<T> findAll(Class<T> clazz, Sort sort) {
		return findAllByClause(clazz, null, sort);
	}

	@Override
	public <T>List<T> findAllByClause(Class<T> clazz, String condition, Sort sort) {
		return findAllByClause(clazz, condition, null, sort);
	}

	@Override
	public <T>List<T> findAllByClause(Class<T> clazz, String condition, String[] fields, SQLParameter parameters, Sort sort) {
		try {
			String sql = SQLHelper.buildSql(this.dataSource, clazz, condition, fields, sort);
			return (List<T>) session.executeQuery(sql, parameters, new BeanListProcessor(clazz));
		} 
		catch (JmpDbException e) {
			throw new JmpDbRuntimeException("error while query", e);
		}
	}

	@Override
	public <T>List<T> findAllByClause(Class<T> clazz, String condition, String[] fields, Sort sort) {
		return findAllByClause(clazz, condition, fields, null, sort);
	}
	
	@Override
	public <T>PageResult<T> findAllByClause(Class<T> clazz, String condition, String[] fields, SQLParameter parameters, Sort sort, Pageable pageable) {
		if(pageable == null)
			throw new JmpDbRuntimeException("Pagination information can not be null");
		try {
			String sql = SQLHelper.buildSql(this.dataSource, clazz, condition, fields, null);
			String countSql = "select count(1) as c from (" + sql + ") as a";
			Map obj = (Map) session.executeQuery(countSql, parameters, new MapProcessor());
			int recordsCount = (Integer)obj.get("c");
			
			PageableSQLBuilder builder = PageableSQLBuilderFactory.getInstance().createLimitSQLBuilder(this.getDBType());
			
			sql = SQLHelper.buildSql(this.dataSource, clazz, condition, fields, sort);
			sql = builder.build(sql, pageable);
			List<T> result = (List<T>) session.executeQuery(sql, parameters, new BeanListProcessor(clazz));
			PageResult<T> pr = new PageResult<T>(result, pageable.getPageIndex(), pageable.getPageSize(), recordsCount);
			return pr;
		} 
		catch (JmpDbException e) {
			throw new JmpDbRuntimeException("error while query", e);
		}
	}
	
	@Override
	public <T>PageResult<T> findAllByClause(Class<T> clazz, String condition, String[] fields, Sort sort, Pageable pageable) {
		return findAllByClause(clazz, condition, fields, null, sort, pageable);
	}

	@Override
	public <T>PageResult<T> findAll(Class<T> clazz, Sort sort, Pageable pageable) {
		return findAllByClause(clazz, null, sort, pageable);
	}

	@Override
	public <T>PageResult<T> findAllByClause(Class<T> clazz, String condition, Sort sort, Pageable pageable) {
		return findAllByClause(clazz, condition, null, sort, pageable);
	}
	
	@Override
	public int getDBType() {
		return session.getDbType();
	}

	@Override
	public DatabaseMetaData getMetaData() {
		if (dbmd == null)
			dbmd = session.getMetaData();
		return dbmd;
	}

	@Override
	public void setSQLTranslator(boolean isTranslator) {
		session.setSQLTranslatorEnabled(isTranslator);
	}

	@Override
	public String getSchema() {
		String strSche = null;
		try {
			String schema = getMetaData().getUserName();
			switch (getDBType()) {
				case DBConsts.MYSQL: 
				case DBConsts.ORACLE: {
					strSche = schema.toUpperCase();
					break;
				}
			}
		} 
		catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new JmpDbRuntimeException("error while getting schema", e);
		}
		return strSche;
	}


	@Override
	public void setMaxRows(int maxRows) {
		session.setMaxRows(maxRows);
	}


	@Override
	public String getCatalog() {
		String catalog = null;
		switch (getDBType()) {
			case DBConsts.MYSQL:
			case DBConsts.ORACLE:
				catalog = "";
				break;
		}
		return catalog;
	}

	@Override
	public int getMaxRows() {
		return session.getMaxRows();
	}

	@Override
	public String getDataSourceName() {
		return this.dataSource;
	}
}
