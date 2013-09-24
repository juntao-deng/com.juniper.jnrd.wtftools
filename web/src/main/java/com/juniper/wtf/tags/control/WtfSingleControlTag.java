package com.juniper.wtf.tags.control;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public abstract class WtfSingleControlTag extends WtfCachableControlTag {
	private Logger log = Logger.getLogger(WtfCachableControlTag.class);
	@Override
	protected CachedObject readFromLocal() {
		try {
			String dirPath = getWebBasePath();
			StringBuffer buf = new StringBuffer();
			buf.append("");
			File jsFile = new File(dirPath + "/" + getType() + ".js");
			String content = FileUtils.readFileToString(jsFile);
			long modifiedDate = jsFile.lastModified();
			return new CachedObject(content, modifiedDate);
		} 
		catch (IOException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

}
