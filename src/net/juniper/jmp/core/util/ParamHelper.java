package net.juniper.jmp.core.util;

import java.util.List;

import javax.ws.rs.core.PathSegment;

import net.juniper.jmp.core.ctx.Filterable;
import net.juniper.jmp.core.ctx.IRequest;
import net.juniper.jmp.core.ctx.Pageable;
import net.juniper.jmp.core.ctx.Sort;
import net.juniper.jmp.core.ctx.impl.PageRequest;

import org.apache.log4j.Logger;

public class ParamHelper {
	private static Logger log = Logger.getLogger(ParamHelper.class);
	private static final String S_SORT = "s_sort";
	private static final String S_FILTER = "s_filter";
	private static final String S_PAGE = "s_page";
//	private static final String S_ENTITY = "s_entity";

	public static Pageable extractPageableInfo(IRequest request){
		String sortStr = request.getParameter(S_SORT);
		Sort sort = null;
		if(sortStr != null && !sortStr.equals("")){
//			Sort sort = new Sort();
		}
		
		int index = 0;
		int size = Integer.MAX_VALUE;
		String pageInfoStr = request.getParameter(S_PAGE);
		if(pageInfoStr != null && !pageInfoStr.equals("")){
			String[] pageInfo = pageInfoStr.split(",");
			try{
				if(pageInfo.length == 2){
					index = Integer.parseInt(pageInfo[0]);
					size = Integer.parseInt(pageInfo[1]);
					if(size <= 0){
						size = Integer.MAX_VALUE;
					}
				}
			}
			catch(Exception e){
				log.error(e.getMessage(), e);
			}
			
		}
		
		PageRequest page = new PageRequest(index, size);
		return page;
	}
	
	public static Filterable extractSpecification(IRequest request){
//		String filter = request.getParameter(S_FILTER);
//		if(filter != null && !filter.equals("")){
//			JSONObject json = JSONObject.fromObject(filter);
//			return new WtfSpecification(json);
//		}
		return null;
	}
	
	public static String[] getParameters(String key, PathSegment seg){
		List<String> params = seg.getMatrixParameters().get(key);
		return (params == null || params.size() == 0) ? null : params.toArray(new String[0]);
	}
}
