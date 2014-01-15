package net.juniper.jmp.persist.jdbc;

import java.util.HashMap;
import java.util.Map;

/**
 * Sql Cache
 * @author juntaod
 *
 */
public class SQLCache {
	private static SQLCache instance = new SQLCache();
	private static Map<String, SQLLRUCache> map = new HashMap<String, SQLLRUCache>();

	private SQLCache() {
	}

	static public SQLCache getInstance() {
		return instance;
	}

	public SQLLRUCache getCache(String dsName) {
		SQLLRUCache cache = map.get(dsName);
		if (cache == null) {
			synchronized (this) {
				cache = map.get(dsName);
				if (cache == null) {
					cache = new SQLLRUCache();
					map.put(dsName, cache);
				}
			}
		}
		return cache;
	}
}
