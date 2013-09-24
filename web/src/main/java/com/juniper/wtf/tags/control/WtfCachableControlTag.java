package com.juniper.wtf.tags.control;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.juniper.wtf.tags.WtfRuntimeException;

public abstract class WtfCachableControlTag extends WtfBaseControlTag {
	private Map<String, CachedObject> cacheMap = new ConcurrentHashMap<String, CachedObject>();
	@Override
	protected String readContent() {
		CachedObject content = readFromCache(getType());
		if(content == null){
			content = readFromLocal();
			if(content == null)
				throw new WtfRuntimeException("error occurred while reading content for id:" + getId() + ", type:" + getType());
			putToCachecontent(getType(), content);
		}
		return content.content;
	}

	protected abstract CachedObject readFromLocal();

	private void putToCachecontent(String key, CachedObject content) {
		cacheMap.put(key, content);
	}


	private CachedObject readFromCache(String key) {
		CachedObject cachedObj = cacheMap.get(key);
		if(cachedObj != null)
			return cachedObj;
		return null;
	}

	class CachedObject {
		private String content;
		private long ts;
		public CachedObject(String content, long modifiedDate) {
			this.content = content;
			this.ts = modifiedDate;
		}
	}
}
	
