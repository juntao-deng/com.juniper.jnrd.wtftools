package net.juniper.jmp.persist.jdbc;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.juniper.jmp.persist.constant.DBConsts;
/**
 * The lru cache for translated sqls
 * @author juntaod
 *
 */
public class SQLLRUCache {

	private Map<String, String> selectPrepPool = new ConcurrentHashMap<String, String>(new LRUMap<String, String>(3500));

	private Map<String, String> updatePrepPool = new ConcurrentHashMap<String, String>(new LRUMap<String, String>(2000));

	private Map<String, String> insertPrepPool = new ConcurrentHashMap<String, String>(new LRUMap<String, String>(2000));

	private Map<String, String> deletePrepPool = new ConcurrentHashMap<String, String>(new LRUMap<String, String>(500));

	private Map<String, String> statementPool = new ConcurrentHashMap<String, String>(new LRUMap<String, String>(1000));

	private class LRUMap<K, V> extends LinkedHashMap<K, V> {

		private static final long serialVersionUID = 1L;

		private int lruSize = 500;

		public LRUMap(int initSize) {
			super(initSize, 1f, true);
			this.lruSize = initSize;
		}

		@Override
		protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
			if (size() > lruSize)
				return true;
			else
				return false;
		}
	}

	private int getType(String sql) {
		String result = sql.trim().substring(0, 10).toLowerCase();
		if (result.startsWith("select"))
			return DBConsts.SQL_SELECT;
		if (result.startsWith("update"))
			return DBConsts.SQL_UPDATE;
		if (result.startsWith("insert"))
			return DBConsts.SQL_INSERT;
		if (result.startsWith("delete"))
			return DBConsts.SQL_DELETE;
		return DBConsts.SQL_SELECT;
	}

	public Object getPreparedSQL(String key) {
		switch (getType(key)) {
			case DBConsts.SQL_SELECT:
				return selectPrepPool.get(key);
			case DBConsts.SQL_UPDATE:
				return updatePrepPool.get(key);
			case DBConsts.SQL_INSERT:
				return insertPrepPool.get(key);
			case DBConsts.SQL_DELETE:
				return deletePrepPool.get(key);
			default:
				return selectPrepPool.get(key);
		}
	}

	public void putPreparedSQL(String key, String value) {
		switch (getType(key)) {
		case DBConsts.SQL_SELECT:
			selectPrepPool.put(key, value);
			break;
		case DBConsts.SQL_UPDATE:
			updatePrepPool.put(key, value);
			break;
		case DBConsts.SQL_INSERT:
			insertPrepPool.put(key, value);
			break;
		case DBConsts.SQL_DELETE:
			deletePrepPool.put(key, value);
			break;
		default:
			selectPrepPool.put(key, value);
			break;
		}
	}

	public Object getStatementSQL(String key) {
		return statementPool.get(key);
	}

	public void putStatementSQL(String key, String value) {
		statementPool.put(key, value);
	}
}
