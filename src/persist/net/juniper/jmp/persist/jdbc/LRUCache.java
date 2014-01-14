package net.juniper.jmp.persist.jdbc;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class LRUCache {

	private final int SELECT = 0;

	private final int INSERT = 1;

	private final int DELETE = 2;

	private final int UPDATE = 3;

	Map<CacheKey, String> selectPrepPool = new ConcurrentHashMap<CacheKey, String>(
			new LRUMap<CacheKey, String>(3500));

	Map<CacheKey, String> updatePrepPool = new ConcurrentHashMap<CacheKey, String>(
			new LRUMap<CacheKey, String>(2000));

	Map<CacheKey, String> insertPrepPool = new ConcurrentHashMap<CacheKey, String>(
			new LRUMap<CacheKey, String>(2000));

	Map<CacheKey, String> deletePrepPool = new ConcurrentHashMap<CacheKey, String>(
			new LRUMap<CacheKey, String>(500));

	Map<String, String> statementPool = new ConcurrentHashMap<String, String>(
			new LRUMap<String, String>(1000));

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
			return SELECT;
		if (result.startsWith("update"))
			return UPDATE;
		if (result.startsWith("insert"))
			return INSERT;
		if (result.startsWith("delete"))
			return DELETE;
		return SELECT;
	}

	public Object getPreparedSQL(String key) {
		CacheKey cacheKey = CacheKey.createKey(key);
		switch (getType(key)) {
		case SELECT:
			return selectPrepPool.get(cacheKey);

		case UPDATE:
			return updatePrepPool.get(cacheKey);

		case INSERT:
			return insertPrepPool.get(cacheKey);

		case DELETE:
			return deletePrepPool.get(cacheKey);

		default:
			return selectPrepPool.get(cacheKey);
		}

	}

	public void putPreparedSQL(String key, String value) {
		CacheKey cacheKey = CacheKey.createKey(key);
		switch (getType(key)) {
		case SELECT:
			selectPrepPool.put(cacheKey, value);
			break;
		case UPDATE:
			updatePrepPool.put(cacheKey, value);
			break;
		case INSERT:
			insertPrepPool.put(cacheKey, value);
			break;
		case DELETE:
			deletePrepPool.put(cacheKey, value);
			break;
		default:
			selectPrepPool.put(cacheKey, value);
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
