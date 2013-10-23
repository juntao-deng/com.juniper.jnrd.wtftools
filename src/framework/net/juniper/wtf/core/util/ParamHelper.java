package net.juniper.wtf.core.util;

import java.util.List;

import javax.ws.rs.core.PathSegment;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class ParamHelper {
	private static Logger log = LoggerFactory.getLogger(ParamHelper.class);
	private static final String S_FILTER = "s_filter";

	public static Pageable extractPageableInfo(PathSegment seg){
		String sortStr = getParameter("s_sort", seg);
		Sort sort = null;
		if(sortStr != null && !sortStr.equals("")){
//			Sort sort = new Sort();
		}
		
		int index = 0;
		int size = Integer.MAX_VALUE;
		String pageInfoStr = seg.getPath();
		String[] pageInfo = pageInfoStr.split("=");
		try{
			if(pageInfo.length == 2 && pageInfo[0].equals("s_page")){
				String[] ss = pageInfo[1].split(",");
				index = Integer.parseInt(ss[0]);
				size = Integer.parseInt(ss[1]);
				if(size <= 0){
					size = Integer.MAX_VALUE;
				}
			}
		}
		catch(Exception e){
			log.error(e.getMessage(), e);
		}
		
		PageRequest page = new PageRequest(index, size, sort);
		return page;
	}
	
	public static <T>Specification<T> extractSpecification(PathSegment seg, Class<T> c){
		String filter = getParameter(S_FILTER, seg);
		if(filter != null && !filter.equals("")){
			JSONObject json = JSONObject.fromObject(filter);
			return new WtfSpecification<T>(json);
		}
		return null;
	}
	
	public static String getParameter(String key, PathSegment seg){
		String[] params = getParameters(key, seg);
		return (params == null || params.length == 0) ? null : params[0];
	}
	
	public static String[] getParameters(String key, PathSegment seg){
		List<String> params = seg.getMatrixParameters().get(key);
		return (params == null || params.size() == 0) ? null : params.toArray(new String[0]);
	}
}
