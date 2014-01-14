package net.juniper.jmp.persist.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.juniper.jmp.persist.BaseProcessor;
import net.juniper.jmp.persist.BeanListProcessor;
import net.juniper.jmp.persist.IJmpPersistence;
import net.juniper.jmp.persist.constant.DBConsts;
import net.juniper.jmp.persist.datasource.DataSourceCenter;
import net.juniper.jmp.persist.datasource.PersistenceContext;
import net.juniper.jmp.persist.exp.JmpDbException;
import net.juniper.jmp.persist.jdbc.CrossDBConnection;
import net.juniper.jmp.persist.jdbc.SQLParameter;
import net.juniper.jmp.persist.ses.DbSession;
import net.juniper.jmp.persist.utils.DBUtil;
import net.juniper.jmp.persist.utils.PersistenceHelper;
import net.juniper.jmp.persist.utils.SQLHelper;
import net.juniper.jmp.persist.utils.SqlLogger;

import org.jboss.resteasy.logging.Logger;

public class EntityPersistenceImpl implements IJmpPersistence{
	private Logger logger = Logger.getLogger(EntityPersistenceImpl.class);
	private DbSession session;
	private String dataSource = null;
	private DatabaseMetaData dbmd = null;

	private static Map cache = new ConcurrentHashMap();

	protected EntityPersistenceImpl() throws JmpDbException {
		this.dataSource = PersistenceContext.getUserDatasource();
		init();
	}

	protected EntityPersistenceImpl(String dataSource) throws JmpDbException {
		this.dataSource = dataSource;
		init();
	}
	
	public DbSession getJdbcSession() {
		return session;
	}


	public void release() {
		if (dbmd != null)
			dbmd = null;
		if (session != null) {
			session.closeAll();
			session = null;
		}

	}

	public String insertWithPK(final Object entity) throws JmpDbException {
		String pk[] = insertWithPK(new Object[] {entity});
		return pk[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.jdbc.framework.ee#insertWithPK(nc.vo.pub.Object[])
	 */
	public String[] insertWithPK(final Object[] entities) throws JmpDbException {
		return insert(entities, true);

	}
	public String insert(final Object entity) throws JmpDbException {
		String pk[] = insert(new Object[] {entity});
		return pk[0];
	}

	public String[] insertWithPK(final List vos) throws JmpDbException {
		return insertWithPK(vos.toArray(new Object[0]));
	}


	public String[] insert(final List vos) throws JmpDbException {
		return insert(vos.toArray(new Object[0]));
	}

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
		
		String tableName = getTableName(entity);
		Map<String, Integer> types = getColmnTypes(tableName);

		String names[] = PersistenceHelper.getInsertValidNames(entity);
		String sql = SQLHelper.getInsertSQL(tableName, names);


		if (entities.length == 1) {
			SQLParameter parameter = getSQLParam(entity, names, types);
			session.executeUpdate(sql, parameter);
		} 
		else {
			SQLParameter[] parameters = new SQLParameter[entities.length];
			for (int i = 0; i < entities.length; i++) {
				if (entities[i] == null)
					continue;
				parameters[i] = getSQLParam(entities[i], names, types);

			}
			session.addBatch(sql, parameters);
			session.executeBatch();
		}

		return getPks(entities, withPK, session);
	}
	
	private String getTableName(Object entity){
		return PersistenceHelper.getTableName(entity);
	}


	public int update(final Object entity) throws JmpDbException {
		if (entity == null) {
			throw new IllegalArgumentException("entity can not be null");
		}
		return update(new Object[] {entity}, null);
	}

	public int update(final List vos) throws JmpDbException {
		return update(vos.toArray(new Object[0]), null);

	}

	public int update(final Object[] vo) throws JmpDbException {
		return update(vo, null);

	}

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
		Map<String, Integer> types = getColmnTypes(tableName);
		if (fieldNames != null) {
			names = fieldNames;
		} 
		else {
			names = PersistenceHelper.getUpdateValidNames(entity);
		}
		
		Object pk = PersistenceHelper.getPrimaryKey(entity);
		String sql = SQLHelper.getUpdateSQL(tableName, names, pkName);
		if (entities.length == 1) {
			SQLParameter parameter = getSQLParam(entity, names, types);
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
				SQLParameter parameter = getSQLParam(entity, names, types);
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
		if (addParams != null)
			for (int i = 0; i < addParams.getCountParams(); i++) {
				parameter.addParam(addParams.get(i));
			}
	}


	public int delete(final List entities) throws JmpDbException {
		return delete(entities.toArray(new Object[] {}));
	}

	public int delete(final Object entity) throws JmpDbException {
		return delete(new Object[] { entity });
	}

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

	public int deleteByPK(Class<?> className, String pk) throws JmpDbException {
		return deleteByPKs(className, new String[] { pk });
	}

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



	public int deleteByClause(Class<?> className, String wherestr) throws JmpDbException {
		return deleteByClause(className, wherestr, null);
	}

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


	public Object retrieveByPK(Class className, String pk) throws JmpDbException {
		return retrieveByPK(className, pk, null);
	}


	public Object retrieveByPK(Class clazz, String pk, String[] selectedFields) throws JmpDbException {
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

	public List<Object> retrieve(Object vo) throws JmpDbException {
		return (List<Object>) retrieve(vo, new BeanListProcessor(vo.getClass()));
	}
	
	public List<? extends Object> retrieveAll(Class<?> clazz) throws JmpDbException {
		String tableName = PersistenceHelper.getTableName(clazz);
		String sql = "SELECT * FROM " + tableName;
		return (List<? extends Object>) session.executeQuery(sql, new BeanListProcessor(clazz));

	}

	public List<? extends Object> retrieveByClause(Class className, String condition) throws JmpDbException {
		return retrieveByClause(className, condition, null);
	}

	public List<? extends Object> retrieveByClause(Class className, String condition, String[] fields, SQLParameter parameters) throws JmpDbException {
		BaseProcessor processor = new BeanListProcessor(className);
		return (List<? extends Object>) session.executeQuery(buildSql(className, condition, fields), parameters, processor);
	}

	public List<? extends Object> retrieveByClause(Class className, String condition, String[] fields) throws JmpDbException {
		return retrieveByClause(className, condition, fields, null);
	}


	public int getDBType() {
		return session.getDbType();
	}

	public DatabaseMetaData getMetaData() {
		if (dbmd == null)
			dbmd = session.getMetaData();
		return dbmd;
	}

	public void setSQLTranslator(boolean isTranslator) {
		session.setSQLTranslator(isTranslator);
	}

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

	private void init() throws JmpDbException {
		try{
			Connection conn = DataSourceCenter.getInstance().getConnection(this.dataSource);
			CrossDBConnection crossDbConn = new CrossDBConnection(conn);
			session = new DbSession(crossDbConn);
		}
		catch(SQLException e){
			logger.error(e.getMessage(), e);
			throw new JmpDbException("error while get connection", e);
		}
	}

	public void setMaxRows(int maxRows) {
		session.setMaxRows(maxRows);
	}

	private SQLParameter getSQLParam(Object entity, String names[], Map<String, Integer> types) {
		SQLParameter params = new SQLParameter();
		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			int type = types.get(name.toUpperCase());
			Object value = PersistenceHelper.getEntityValue(entity, name);
			if (value == null) {
				params.addNullParam(type);
				continue;
			}
			if (type == Types.BLOB || type == Types.LONGVARBINARY || type == Types.VARBINARY || type == Types.BINARY) {
				params.addBlobParam(value);
				continue;
			}
			if (type == Types.CLOB || type == Types.LONGVARCHAR) {
				params.addClobParam(String.valueOf(value));
				continue;
			}
			params.addParam(value);

		}
		return params;
	}

	public Connection getConnection() {
		if (session != null)
			return session.getConnection();
		return null;
	}

	private Map<String, Integer> getColmnTypes(String tableName) throws JmpDbException {
		Map<String, Integer> typeMap = (Map<String, Integer>) cache.get(tableName);
		if (typeMap == null) {
			typeMap = new HashMap<String, Integer>();
			ResultSet rsColumns = null;
			try {
				rsColumns = getMetaData().getColumns(null, getSchema(), tableName.toUpperCase(), "%");
				while (rsColumns.next()) {
					String columnName = rsColumns.getString("COLUMN_NAME").toUpperCase();
					int columnType = rsColumns.getShort("DATA_TYPE");
					typeMap.put(columnName, columnType);
				}
				cache.put(tableName, typeMap);
			} 
			catch (SQLException e) {
				SqlLogger.error("get table metadata error", e);
			} 
			finally {
				DBUtil.closeRs(rsColumns);
			}
		}
		return typeMap;
	}


	private String buildSql(Class<?> clazz, String condition, String[] fields) {
		String pkField = PersistenceHelper.getPkField(clazz);
		String tableName = PersistenceHelper.getTableName(clazz);
		boolean hasPKField = false;
		StringBuffer buffer = new StringBuffer();
		if (fields == null)
			buffer.append("SELECT * FROM ").append(tableName);
		else {
			buffer.append("SELECT ");
			for (int i = 0; i < fields.length; i++) {
				buffer.append(fields[i]).append(",");
				if (fields[i].equalsIgnoreCase(pkField))
					hasPKField = true;
			}
			if (!hasPKField)
				buffer.append(pkField).append(",");
			buffer.setLength(buffer.length() - 1);
			buffer.append(" FROM ").append(tableName);
		}
		if (condition != null && condition.length() != 0) {
			if (condition.toUpperCase().trim().startsWith("ORDER "))
				buffer.append(" ").append(condition);
			else
				buffer.append(" WHERE ").append(condition);
		}

		return buffer.toString();
	}



}
