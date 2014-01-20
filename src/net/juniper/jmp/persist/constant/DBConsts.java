package net.juniper.jmp.persist.constant;
/**
 * Persistence Api constants
 * @author juntaod
 *
 */
public interface DBConsts {
	public final static int MYSQL = 0;
	public final static int ORACLE = 1;
	public final static int SQLSERVER = 2;
	public final static int SYBASE = 3;
	public final static int INFORMIX = 4;
	public final static int HSQL = 5;
	public final static int OSCAR = 6;
	public final static int POSTGRESQL = 7;
	public final static int DB2 = 8;
	public final static int DERBY = 9;

	public final static String ORACLE_NAME = "ORACLE";
	public final static String SQLSERVER_NAME = "SQLSERVER";
	public final static String MSSQL_NAME = "MSSQL";
	public final static String DB2_NAME = "DB2";
	public final static String HSQL_NAME = "HSQL";
	public final static String SYBASE_NAME = "SYBASE";
	public final static String INFORMIX_NAME = "INFORMIX";
	public final static String OSCAR_NAME = "OSCAR";
	public final static String POSTGRESQL_NAME = "POSTGRESQL";
	public final static String DERBY_NAME = "APACHE DERBY";
	
	public final static int SQL_SELECT = 1;
	public final static int SQL_INSERT = 2;
	public final static int SQL_CREATE = 3;
	public final static int SQL_DROP = 4;
	public final static int SQL_DELETE = 5;
	public final static int SQL_UPDATE = 6;
	public final static int SQL_EXPLAIN = 7;

	/**
	 * supported functions
	 */
	public static String[] functions = { "coalesce", "len", "left", "right",
			"substring", "lower", "upper", "ltrim", "rtrim", "sqrt", "abs",
			"square", "sign", "count", "max", "min", "sum", "avg", "cast" };
	
}