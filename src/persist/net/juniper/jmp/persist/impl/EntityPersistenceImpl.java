package net.juniper.jmp.persist.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import net.juniper.jmp.persist.BeanListProcessor;
import net.juniper.jmp.persist.IJmpPersistence;
import net.juniper.jmp.persist.ResultSetProcessor;
import net.juniper.jmp.persist.SQLParameter;
import net.juniper.jmp.persist.constant.DBConsts;
import net.juniper.jmp.persist.datasource.DataSourceCenter;
import net.juniper.jmp.persist.datasource.PersistenceCtx;
import net.juniper.jmp.persist.exp.JmpDbException;
import net.juniper.jmp.persist.exp.JmpDbRuntimeException;
import net.juniper.jmp.persist.jdbc.CrossDBConnection;
import net.juniper.jmp.persist.jdbc.SQLHelper;
import net.juniper.jmp.persist.ses.DbSession;
import net.juniper.jmp.persist.utils.DBUtil;

import org.jboss.resteasy.logging.Logger;

public class EntityPersistenceImpl implements IJmpPersistence{
	private Logger logger = Logger.getLogger(EntityPersistenceImpl.class);
	private DbSession session;
	private String dataSource = null;
	private DatabaseMetaData dbmd = null;

	public EntityPersistenceImpl() throws JmpDbException {
		this.dataSource = PersistenceCtx.getCurrentDatasource();
		init();
	}

	public EntityPersistenceImpl(String dataSource) throws JmpDbException {
		this.dataSource = dataSource;
		init();
	}
	
	private void init() throws JmpDbException {
		try{
			DataSource ds = DataSourceCenter.getInstance().getDataSource(this.dataSource);
			if(ds == null)
				throw new JmpDbRuntimeException("can not find datasource:" + ds);
			Connection conn = ds.getConnection();
			int dbType = DBUtil.getDbType(conn);
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

	}

	@Override
	public String insertWithPK(final Object entity) throws JmpDbException {
		String pk[] = insertWithPK(new Object[] {entity});
		return pk[0];
	}

	@Override
	public String[] insertWithPK(final Object[] entities) throws JmpDbException {
		return insert(entities, true);

	}
	
	@Override
	public String insert(final Object entity) throws JmpDbException {
		String pk[] = insert(new Object[] {entity});
		return pk[0];
	}

	@Override
	public String[] insertWithPK(final List<? extends Object> entities) throws JmpDbException {
		return insertWithPK(entities.toArray(new Object[0]));
	}

	@Override
	public String[] insert(final List<? extends Object> entities) throws JmpDbException {
		return insert(entities.toArray(new Object[0]));
	}

	@Override
	public String[] insert(final Object entities[]) throws JmpDbException {
		return insert(entities, false);
	}

	private String[] getPks(final Object entities[], boolean withPK, DbSession ses) {
		return null;
	}

	protected String[] insert(final Object entities[], boolean withPK) throws JmpDbException {
		if(entities == null || entities.length == 0)
			return new String[0];
	
		Object entity = entities[0];
		
		String tableName = PersistenceHelper.getTableName(entity);
		String names[] = PersistenceHelper.getInsertValidNames(entity);
		String sql = SQLHelper.getInsertSQL(tableName, names);


		if (entities.length == 1) {
			SQLParameter parameter = SQLHelper.getSQLParam(entity, names);
			session.executeUpdate(sql, parameter);
		} 
		else {
			SQLParameter[] parameters = new SQLParameter[entities.length];
			for (int i = 0; i < entities.length; i++) {
				if (entities[i] == null)
					continue;
				parameters[i] = SQLHelper.getSQLParam(entities[i], names);

			}
			session.addBatch(sql, parameters);
			session.executeBatch();
		}

		return getPks(entities, withPK, session);
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
	public Object retrieveByPK(Class<?> clazz, String pk) throws JmpDbException {
		return retrieveByPK(clazz, pk, null);
	}

	@Override
	public Object retrieveByPK(Class<?> clazz, String pk, String[] selectedFields) throws JmpDbException {
		if (pk == null)
			return null;
		SQLParameter param = new SQLParameter();
		param.addParam(pk);
		
		String pkField = PersistenceHelper.getPkField(clazz);
		List results = (List) retrieveByClause(clazz, pkField + "=?", selectedFields, param);
		if (results.size() >= 1)
			return (Object) results.get(0);
		return null;

	}

	@Override
	public List<? extends Object> retrieve(Object entity) throws JmpDbException {
		return (List<? extends Object>) retrieve(entity, new BeanListProcessor(entity.getClass()));
	}
	
	@Override
	public Object retrieve(Object entity, ResultSetProcessor processor)
			throws JmpDbException {
		return (List<? extends Object>) retrieve(entity, processor);
	}
	
	@Override
	public List<? extends Object> retrieveAll(Class<?> clazz) throws JmpDbException {
		String tableName = PersistenceHelper.getTableName(clazz);
		String sql = "SELECT * FROM " + tableName;
		return (List<? extends Object>) session.executeQuery(sql, new BeanListProcessor(clazz));

	}

	@Override
	public List<? extends Object> retrieveByClause(Class<?> className, String condition) throws JmpDbException {
		return retrieveByClause(className, condition, null);
	}

	@Override
	public List<? extends Object> retrieveByClause(Class<?> className, String condition, String[] fields, SQLParameter parameters) throws JmpDbException {
		BaseProcessor processor = new BeanListProcessor(className);
		String sql = SQLHelper.buildSql(className, condition, fields);
		return (List<? extends Object>) session.executeQuery(sql, parameters, processor);
	}

	@Override
	public List<? extends Object> retrieveByClause(Class<?> className, String condition, String[] fields) throws JmpDbException {
		return retrieveByClause(className, condition, fields, null);
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
		session.setSQLTranslator(isTranslator);
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

}
