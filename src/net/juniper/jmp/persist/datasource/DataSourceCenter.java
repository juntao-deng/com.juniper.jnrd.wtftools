package net.juniper.jmp.persist.datasource;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import net.juniper.jmp.core.util.JmpClassUtil;
import net.juniper.jmp.persist.constant.DBConsts;
import net.juniper.jmp.persist.exp.UnSupportDbException;
import net.juniper.jmp.persist.utils.SqlLogger;

/**
 * DataSource Manager that can get managed datasources through DataSourceProvider
 * @author juntaod
 *
 */
public class DataSourceCenter {

    private static DataSourceProvider provider;

    private static DataSourceCenter instance = new DataSourceCenter();

    private Map<String, Reference<DataSource>> dataSourceCache = new ConcurrentHashMap<String, Reference<DataSource>>();

    private Map<String, DBMetaInfo> metaCache = new HashMap<String, DBMetaInfo>();
    
    private DataSourceCenter() {
    	String providerClazz = System.getProperty("JMP_DS_PROVIDER");
    	if(providerClazz != null){
    		provider = JmpClassUtil.newInstance(providerClazz);
    	}
    	else{
    		provider = new JNDIDatasourceProvider();
    	}
    }

    public static DataSourceCenter getInstance() {
        return instance;
    }

    public DataSource getDataSource(String name) throws SQLException{
    	Reference<DataSource> dsRef = dataSourceCache.get(name);
        if (dsRef == null) {
        	Connection conn = null;
        	try {
        	   DataSource ds = (DataSource) provider.getDataSource(name);
        	   dataSourceCache.put(name, new WeakReference<DataSource>(ds));
               conn = getDiffConnection(ds);
               DatabaseMetaData DBMeta = conn.getMetaData();
               int dbType = getDbType(DBMeta);
               DBMetaInfo meta = new DBMetaInfo(dbType);
               metaCache.put(name, meta);
           } 
           catch (Throwable e) {
        	   SqlLogger.error("get data source " + name + " error,can't connect to database", e);
           }
           finally{
        	   if(conn != null)
        		   conn.close();
           }
        }
        return dsRef.get();
    }

    public DBMetaInfo getDbMeta(String name) {
    	return metaCache.get(name);
    }
    
    /**
     * Give a chance to get a connection that may be wrapped by middleware
     * @param ds
     * @return
     * @throws SQLException
     */
    private Connection getDiffConnection(DataSource ds) throws SQLException {
        return ds == null ? null : ds.getConnection();
    }


    /**
     * get Database Type
     * @param dmd
     * @return
     * @throws SQLException
     */
    public int getDbType(DatabaseMetaData dmd) throws SQLException {
        String dpn = dmd.getDatabaseProductName();
        String typeStr = dpn.toUpperCase();
        if (typeStr.indexOf(DBConsts.DB2_NAME) != -1)
            return DBConsts.DB2;
        if (typeStr.equals(DBConsts.MSSQL_NAME))
        	return DBConsts.MYSQL;
        if (typeStr.indexOf(DBConsts.ORACLE_NAME) != -1)
            return DBConsts.ORACLE;
        if (typeStr.indexOf(DBConsts.SQLSERVER_NAME) != -1)
            return DBConsts.SQLSERVER;
        if (typeStr.indexOf(DBConsts.SYBASE_NAME) != -1)
            return DBConsts.SYBASE;
        if (typeStr.indexOf(DBConsts.INFORMIX_NAME) != -1)
            return DBConsts.INFORMIX;
        throw new UnSupportDbException("un support db," + typeStr);
    }

}
