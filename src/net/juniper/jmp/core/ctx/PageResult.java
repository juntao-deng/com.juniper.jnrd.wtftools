package net.juniper.jmp.core.ctx;

import java.util.List;

public class PageResult<T> {
	private Integer pageSize;
	private Integer totalRecords;
	private Integer pageIndex;
	private List<T> records;
	
	public PageResult(List<T> records){
		this.records = records;
		this.pageSize = -1;
		this.totalRecords = records == null ? 0 : this.records.size();
		this.pageIndex = 0;
	}
	
	public PageResult(List<T> records, int pageIndex, int pageSize, int totalRecords){
		this.records = records;
		this.pageSize = pageSize;
		this.totalRecords = totalRecords;
		this.pageIndex = pageIndex;
	}

	public List<T> getRecords() {
		return records;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public Integer getTotalRecords() {
		return totalRecords;
	}
	public Integer getPageIndex() {
		return pageIndex;
	}
}
