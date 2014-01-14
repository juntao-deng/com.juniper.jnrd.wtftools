package net.juniper.jmp.persist.jdbc;

import java.util.HashMap;
import java.util.Map;

public class SQLCache {
	static SQLCache cache = new SQLCache();

	static Map<String, LRUCache> map = new HashMap<String, LRUCache>();

	private SQLCache() {
	}

	static public SQLCache getInstance() {
		return cache;
	}

	public LRUCache getCache(String dataSource) {
		LRUCache lruCache = map.get(dataSource);
		if (lruCache == null) {
			synchronized (this) {
				if (lruCache == null) {
					lruCache = new LRUCache();
					map.put(dataSource, lruCache);
				}
			}

		}
		return lruCache;
	}

}
