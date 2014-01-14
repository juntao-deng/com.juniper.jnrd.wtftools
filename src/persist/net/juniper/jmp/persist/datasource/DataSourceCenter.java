package net.juniper.jmp.persist.datasource;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import net.juniper.jmp.persist.constant.DBConsts;
import net.juniper.jmp.persist.exp.UnSupportDbException;
import net.juniper.jmp.persist.utils.DBUtil;
import net.juniper.jmp.persist.utils.SqlLogger;

public class DataSourceCenter {

    static DataSourceProvider provider;

    static private DataSourceCenter center = new DataSourceCenter();

    private Map<String, DBMetaInfo> metaCache = new ConcurrentHashMap<String, DBMetaInfo>();

    private Map<String, DataSource> dataSourceCache = new ConcurrentHashMap<String, DataSource>();

    private String defaultSource = "dataSource";

    private DataSourceCenter() {
    }

    static public DataSourceCenter getInstance() {

        return center;
    }

    public void clearCache() {
        center.metaCache.clear();
        center.dataSourceCache.clear();
    }

    public String getSourceName() {
        String dataSource = PersistenceContext.getUserDatasource();
        if (dataSource == null)
            return defaultSource;
        return dataSource;
    }

    public Connection getConnection() throws SQLException {
        String sourceName = getSourceName();
        Connection con = getConnection(sourceName);
        return con;
    }

    public DataSource getDataSource(String name) throws SQLException{
    	DataSource ds = dataSourceCache.get(name);
        if (ds == null) {
        	Connection conn = null;
        	try {
        	   ds = (DataSource) provider.getDataSource(name);
        	   dataSourceCache.put(name, ds);
               conn = getDiffConnection(ds);
               DatabaseMetaData DBMeta = conn.getMetaData();
               int dbType = getDbType(DBMeta);
               DBMetaInfo meta = new DBMetaInfo(dbType);
               metaCache.put(name, meta);
           } 
           catch (Throwable e) {
                try {
                    SqlLogger.error("get data source " + name + " error,can't connect to database", e);
                } 
                catch (Throwable e1) {
                    SqlLogger.error("try to get data source " + name + " error", e);
                    throw new SQLException(" get data source " + name + " error,can't connect to database");
                }
            }
           	finally{
           		if(conn != null)
           			conn.close();
           	}
        }
        return ds;
    }
    public Connection getConnection(String sourceName) throws SQLException {
        String name = sourceName;
        if (name == null) {
            name = getSourceName();
        }
        DataSource ds = getDataSource(name);
        return getDiffConnection(ds);
    }

    private Connection getDiffConnection(DataSource ds) throws SQLException {
        return ds == null ? null : ds.getConnection();
    }

    public int getDatabaseType() throws SQLException {
        String sourceName = getSourceName();
        return getDatabaseType(sourceName);
    }

    
    public int getDatabaseType(String sourceName) throws SQLException {
        if (sourceName == null)
            sourceName = getSourceName();
        DBMetaInfo meta = metaCache.get(sourceName);
        if (meta == null) {
            Connection con = null;
            try {
                con = getConnection(sourceName);
                meta = metaCache.get(sourceName);
            } 
            finally {
                DBUtil.closeConnection(con);
            }
        }
        return meta.getType();
    }


    /**
     * 得到数据库类型
     * 
     * @return
     * @throws SQLException
     *             如果出错抛出异常
     */
    public int getDbType(DatabaseMetaData dmd) throws SQLException {
        String dpn = dmd.getDatabaseProductName();
        String typeStr = dpn.toUpperCase();
        if (typeStr.indexOf("DB2") != -1)
            return DBConsts.DB2;
        if (typeStr.equals("MYSQL"))
        	return DBConsts.MYSQL;
        if (typeStr.indexOf("ORACLE") != -1)
            return DBConsts.ORACLE;
        if (typeStr.indexOf("SQL") != -1)
            return DBConsts.SQLSERVER;
        if (typeStr.indexOf("SYBASE") != -1)
            return DBConsts.SYBASE;
        if (typeStr.indexOf("INFORMIX") != -1)
            return DBConsts.INFORMIX;
        throw new UnSupportDbException("un support db," + typeStr);
    }

}
