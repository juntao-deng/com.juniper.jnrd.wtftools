package net.juniper.jmp.wtf.ctx;

import java.util.List;

public class Page<T> {
	private Integer pageSize;
	private Integer totalRecords;
	private Integer pageIndex;
	private List<T> records;
	
	public Page(List<T> records){
		this.records = records;
		this.pageSize = -1;
		this.totalRecords = records == null ? 0 : this.records.size();
		this.pageIndex = 0;
	}
	
	public Page(List<T> records, int pageIndex, int pageSize, int totalRecords){
		this.records = records;
		this.pageSize = pageSize;
		this.totalRecords = totalRecords;
		this.pageIndex = pageIndex;
	}

	public Page(List<T> records, Pageable pageable, Integer totalRecords) {
		this.records = records;
		this.pageSize = pageable.getPageSize();
		this.totalRecords = totalRecords;
		this.pageIndex = pageable.getPageIndex();
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
