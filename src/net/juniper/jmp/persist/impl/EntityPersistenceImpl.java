package net.juniper.jmp.persist.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import net.juniper.jmp.core.ctx.Pageable;
import net.juniper.jmp.core.ctx.Sort;
import net.juniper.jmp.persist.BeanListProcessor;
import net.juniper.jmp.persist.IJmpPersistence;
import net.juniper.jmp.persist.IReleaseCallback;
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

public class EntityPersistenceImpl implements IJmpPersistence{
	private Logger logger = Logger.getLogger(EntityPersistenceImpl.class);
	private DbSession session;
	private String dataSource = null;
	private DatabaseMetaData dbmd = null;
	private IReleaseCallback releaseCallback;
	public EntityPersistenceImpl(String dsName, IReleaseCallback callback) throws JmpDbException {
		if(dsName == null || dsName.equals(""))
			dsName = PersistenceCtx.getCurrentDatasource();
		this.dataSource = dsName;
		this.releaseCallback = callback;
		init();
	}
	
	private void init() throws JmpDbException {
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
		catch(SQLException e){
			logger.error(e.getMessage(), e);
			throw new JmpDbException("error while get connection", e);
		}
	}
	
	@Override
	public DbSession getDbSession() {
		return session;
	}

	@Override
	public void release() {
		if (dbmd != null)
			dbmd = null;
		if (session != null) {
			session.closeAll();
			session = null;
		}
		
		releaseCallback.callback(this);
	}

	@Override
	public Object insertWithPK(final Object entity) throws JmpDbException {
		Object pk[] = insertWithPK(new Object[] {entity});
		return pk[0];
	}

	@Override
	public Object[] insertWithPK(final Object[] entities) throws JmpDbException {
		return insert(entities, true);

	}
	
	@Override
	public Object insert(final Object entity) throws JmpDbException {
		Object pk[] = insert(new Object[] {entity});
		return pk[0];
	}

	@Override
	public Object[] insertWithPK(final List<? extends Object> entities) throws JmpDbException {
		return insertWithPK(entities.toArray(new Object[0]));
	}

	@Override
	public Object[] insert(final List<? extends Object> entities) throws JmpDbException {
		return insert(entities.toArray(new Object[0]));
	}

	@Override
	public Object[] insert(final Object[] entities) throws JmpDbException {
		return insert(entities, false);
	}

	private Object[] getPks(final Object[] entities) throws JmpDbException {
		if(entities == null || entities.length == 0)
			return null;
		Object[] pks = new Object[entities.length];
		String pkField = PersistenceHelper.getPkField(entities[0]);
		for (int i = 0; i < entities.length; i++) {
			try {
				pks[i] = PropertyUtils.getProperty(entities[i], pkField);
			} 
			catch (Throwable e){
				throw new JmpDbException("error while fetch pk");
			}
		}
		return pks;
	}

	protected Object[] insert(final Object[] entities, boolean withPK) throws JmpDbException {
		if(entities == null || entities.length == 0)
			return new String[0];
		Object entity = entities[0];
		
		String tableName = PersistenceHelper.getTableName(entity);
		String names[] = PersistenceHelper.getInsertValidNames(entity);
		String sql = SQLHelper.getInsertSQL(tableName, names);

		Object[] pks = null;
		if(withPK){
			pks = getPks(entities);
		}
		
		if (entities.length == 1) {
			SQLParameter parameter = SQLHelper.getSQLParam(entity, names);
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
				parameters[i] = SQLHelper.getSQLParam(entities[i], names);
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

	private void fillEntities(Object[] entities, Object[] pks) throws JmpDbException {
		if(entities.length != pks.length)
			throw new JmpDbException("the count of entities is not equal to pks");
		//should query by pks again, and fetch version information and merge into the entities.
		String pkField = PersistenceHelper.getPkField(entities[0]);
		for(int i = 0; i < entities.length; i ++){
			try {
				PropertyUtils.setProperty(entities[i], pkField, pks[i]);
			} 
			catch (Exception e) {
				throw new JmpDbException("error while setting pk to entities");
			}
		}
	}

	@Override
	public int update(final Object entity) throws JmpDbException {
		if (entity == null) {
			throw new IllegalArgumentException("entity can not be null");
		}
		return update(new Object[] {entity}, null);
	}

	@Override
	public int update(final List<? extends Object> entities) throws JmpDbException {
		return update(entities.toArray(new Object[0]), null);

	}

	@Override
	public int update(final Object[] vo) throws JmpDbException {
		return update(vo, null);

	}

	@Override
	public int update(final Object[] vo, String[] fieldNames)
			throws JmpDbException {
		return update(vo, fieldNames, null, null);
	}

	@Override
	public int update(final Object[] entities, String[] fieldNames, String whereClause, SQLParameter param) throws JmpDbException {
		if (entities == null || entities.length == 0)
			return 0;
		Object entity = entities[0];
		int row = 0;
		String tableName = PersistenceHelper.getTableName(entity);
		String pkName = PersistenceHelper.getPkField(entity);
		String[] names;
		if (fieldNames != null) {
			names = fieldNames;
		} 
		else {
			names = PersistenceHelper.getUpdateValidNames(entity);
		}
		
		Object pk = PersistenceHelper.getPrimaryKey(entity);
		String sql = SQLHelper.getUpdateSQL(tableName, names, pkName);
		if (entities.length == 1) {
			SQLParameter parameter = SQLHelper.getSQLParam(entity, names);
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
				SQLParameter parameter = SQLHelper.getSQLParam(entity, names);
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


	private void addParameter(SQLParameter parameter, SQLParameter addParams) {
		if (addParams != null){
			int count = addParams.getCount();
			for (int i = 0; i < count; i++) {
				parameter.addParam(addParams.get(i));
			}
		}
	}

	@Override
	public int delete(final List<? extends Object> entities) throws JmpDbException {
		return delete(entities.toArray(new Object[] {}));
	}

	@Override
	public int delete(final Object entity) throws JmpDbException {
		return delete(new Object[] { entity });
	}

	@Override
	public int delete(final Object entities[]) throws JmpDbException {
		if (entities.length == 0)
			return 0;
		Object entity = entities[0];
		String tableName = PersistenceHelper.getTableName(entity);
		String pkField = PersistenceHelper.getPkField(entity);
		String sql = SQLHelper.getDeleteByPKSQL(tableName, pkField);

		for (int i = 0; i < entities.length; i++) {
			if (entities[i] == null)
				continue;
			SQLParameter parameter = new SQLParameter();
			Object pk = PersistenceHelper.getPrimaryKey(entities[i]);
			parameter.addParam(pk);
			session.addBatch(sql, parameter);
		}
		return session.executeBatch();
	}

	@Override
	public int deleteByPK(Class<?> className, String pk) throws JmpDbException {
		return deleteByPKs(className, new String[] { pk });
	}

	@Override
	public int deleteByPKs(Class<?> clazz, String[] pks) throws JmpDbException {
		String tableName = PersistenceHelper.getTableName(clazz);
		String pkField = PersistenceHelper.getPkField(clazz);
		String sql = "DELETE FROM " + tableName + " WHERE " + pkField + "=?";
		for (int i = 0; i < pks.length; i++) {
			SQLParameter parameter = new SQLParameter();
			parameter.addParam(pks[i]);
			session.addBatch(sql, parameter);
		}
		return session.executeBatch();
	}


	@Override
	public int deleteByClause(Class<?> className, String wherestr) throws JmpDbException {
		return deleteByClause(className, wherestr, null);
	}

	@Override
	public int deleteByClause(Class<?> clazz, String whereStr, SQLParameter params) throws JmpDbException {
		String tableName = PersistenceHelper.getTableName(clazz);
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

	@Override
	public Object findByPK(Class<?> clazz, String pk) throws JmpDbException {
		return findByPK(clazz, pk, null);
	}

	@Override
	public Object findByPK(Class<?> clazz, String pk, String[] selectedFields) throws JmpDbException {
		if (pk == null)
			return null;
		SQLParameter param = new SQLParameter();
		param.addParam(pk);
		
		String pkField = PersistenceHelper.getPkField(clazz);
		List results = (List) findAllByClause(clazz, pkField + "=?", selectedFields, param, null);
		if (results.size() >= 1)
			return (Object) results.get(0);
		return null;
	}

	@Override
	public List<? extends Object> findByEntityAttribute(Object entity, Sort sort) throws JmpDbException {
		return null;
	}
	
	@Override
	public List<? extends Object> findByEntityAttribute(Object entity, Sort sort, Pageable pageable) throws JmpDbException{
		return null;
	}
	
	@Override
	public Object findByEntityAttribute(Object entity, ResultSetProcessor processor) throws JmpDbException {
		return null;
	}
	
	@Override
	public List<? extends Object> findAll(Class<?> clazz, Sort sort) throws JmpDbException {
		String tableName = PersistenceHelper.getTableName(clazz);
		String sql = "SELECT * FROM " + tableName;
		return (List<? extends Object>) session.executeQuery(sql, new BeanListProcessor(clazz));

	}

	@Override
	public List<? extends Object> findAllByClause(Class<?> className, String condition, Sort sort) throws JmpDbException {
		return findAllByClause(className, condition, null, sort);
	}

	@Override
	public List<? extends Object> findAllByClause(Class<?> className, String condition, String[] fields, SQLParameter parameters, Sort sort) throws JmpDbException {
		return findAllByClause(className, condition, fields, parameters, sort, null);
	}
	
	@Override
	public List<? extends Object> findAllByClause(Class<?> className, String condition, String[] fields, SQLParameter parameters, Sort sort, Pageable pageable) throws JmpDbException {
		BaseProcessor processor = new BeanListProcessor(className);
		String sql = SQLHelper.buildSql(className, condition, fields, sort);
		return (List<? extends Object>) session.executeQuery(sql, parameters, processor);
	}

	@Override
	public List<? extends Object> findAllByClause(Class<?> className, String condition, String[] fields, Sort sort) throws JmpDbException {
		return findAllByClause(className, condition, fields, sort, null);
	}
	
	@Override
	public List<? extends Object> findAllByClause(Class<?> className, String condition, String[] fields, Sort sort, Pageable pageable) throws JmpDbException {
		return null;
	}

	@Override
	public List<? extends Object> findAll(Class<?> clazz, Sort sort, Pageable pageable) throws JmpDbException {
		return null;
	}

	@Override
	public List<? extends Object> findAllByClause(Class<?> clazz, String condition, Sort sort, Pageable pageable) throws JmpDbException {
		return null;
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
	public String getSchema() throws JmpDbException {
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
			throw new JmpDbException("error while getting schema", e);
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
